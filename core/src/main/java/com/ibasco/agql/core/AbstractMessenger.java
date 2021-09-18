/*
 * Copyright 2021 Rafael Luis L. Ibasco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.agql.core.enums.ProcessingMode;
import com.ibasco.agql.core.enums.RequestStatus;
import com.ibasco.agql.core.exceptions.ResponseException;
import com.ibasco.agql.core.session.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * <p>The base implementation of the {@link Messenger} interface. Contains an internal queue for the requests and
 * process them based on priority</p>
 *
 * @param <A>
 *         The type of {@link AbstractRequest}
 * @param <B>
 *         The type {@link AbstractResponse}
 */
abstract public class AbstractMessenger<A extends AbstractRequest, B extends AbstractResponse> implements Messenger<A, B> {

    private static final Logger log = LoggerFactory.getLogger(AbstractMessenger.class);

    private static final int DEFAULT_REQUEST_QUEUE_CAPACITY = 30;

    private final AtomicBoolean processRequests = new AtomicBoolean(false);

    private ScheduledExecutorService messengerService;

    private SessionManager<A, B> sessionManager;

    private Transport<A> transport;

    private BlockingDeque<RequestDetails<A, B>> requestQueue;

    private Consumer<RequestDetails<A, B>> requestProcessor;

    private ProcessingMode processingMode;

    private ExecutorService executorService;

    public AbstractMessenger(ProcessingMode processingMode) {
        this(new DefaultSessionIdFactory(), processingMode);
    }

    public AbstractMessenger(AbstractSessionIdFactory keyFactory, ProcessingMode processingMode) {
        this(new DefaultSessionManager(keyFactory), processingMode, DEFAULT_REQUEST_QUEUE_CAPACITY, null);
    }

    public AbstractMessenger(AbstractSessionIdFactory keyFactory, ProcessingMode processingMode, ExecutorService executorService) {
        this(new DefaultSessionManager(keyFactory), processingMode, DEFAULT_REQUEST_QUEUE_CAPACITY, executorService);
    }

    public AbstractMessenger(SessionManager sessionManager, ProcessingMode processingMode, int initQueueCapacity, ExecutorService executorService) {
        //Set processing mode
        this.processingMode = processingMode;

        log.debug("Using Processing Mode : {}", processingMode);

        //Use the default session manager if not specified
        this.executorService = executorService;
        //noinspection unchecked
        this.sessionManager = (sessionManager != null) ? sessionManager : new DefaultSessionManager<>(new DefaultSessionIdFactory());
        this.requestQueue = new LinkedBlockingDeque<>(initQueueCapacity); // new RequestComparator()
        this.transport = createTransportService();
        configureMappings(this.sessionManager.getLookupMap());
        this.messengerService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("messenger-%d").build());
        this.requestProcessor = (processingMode == ProcessingMode.SYNCHRONOUS) ? this::processSync : this::processAsync;
    }

    /**
     * Call to start processing requests
     */
    private void start() {
        if (!messengerService.isShutdown() && !processRequests.getAndSet(true)) {
            messengerService.execute(() -> {
                try {
                    while (processRequests.get()) {
                        requestProcessor.accept(requestQueue.take());
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    log.debug("Loop thread interrupted", e);
                } finally {
                    processRequests.set(false);
                }
            });
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * <p>Let the concrete messenger create and initialize the transport</p>
     *
     * @return The {@link Transport} service that will be used by this messenger
     */
    abstract protected Transport<A> createTransportService();

    /**
     * <p>Configure request - response mappings</p>
     *
     * @param map
     *         {@link Map} representing the {@link AbstractRequest} and {@link AbstractResponse} class mappings
     */
    abstract public void configureMappings(Map<Class<? extends A>, Class<? extends B>> map);

    /**
     * Send the request to the transport
     *
     * @param request
     *         An instance of {@link AbstractRequest} to be sent
     *
     * @return A {@link CompletableFuture} containing a value of {@link AbstractResponse}
     */
    @Override
    public CompletableFuture<B> send(A request) {
        log.debug("Adding request '{}' to queue", request.getClass().getSimpleName());
        CompletableFuture<B> promise = new CompletableFuture<>();
        try {
            requestQueue.put(new RequestDetails<>(request, promise, this.transport));
            start(); //start if not yet started
        } catch (InterruptedException e) {
            promise.completeExceptionally(e);
        }
        return promise;
    }

    /**
     * <p>Called by the transport when a response has been received from the server</p>
     *
     * @param response
     *         The response received from the server
     * @param error
     *         Error thrown by the transport while processing the request. Otherwise null.
     */
    @Override
    public void accept(B response, Throwable error) {
        log.debug("Receiving response '{}'", response, error);

        if (error instanceof ResponseException) {
            ResponseException ex = (ResponseException) error;
            if (ex.getOriginatingRequest() != null) {
                SessionId id = sessionManager.getId(ex.getOriginatingRequest());
                SessionValue<A, B> session = sessionManager.getSession(id);
                if (session != null) {
                    final CompletableFuture<B> clientPromise = session.getClientPromise();
                    clientPromise.completeExceptionally(ex);
                } else {
                    log.debug("Session not found for response: {}", response);
                }
            }
            return;
        }

        //Retrieve the existing session for this response
        final SessionValue<A, B> session = sessionManager.getSession(response);
        if (session != null) {
            //1) Retrieve our client promise from the session
            final CompletableFuture<B> clientPromise = session.getClientPromise();

            //2) Notify the client that we have successfully received a response from the server
            if (clientPromise.complete(response)) {
                log.debug("Notified client of completion event : {}", session.getId());
            } else
                log.debug("Unable to transition session to completion state : {}", session.getId());
        } else {
            log.debug("No associated session is found for Response '{}'", response);
        }

    }

    /**
     * Process requests synchronously
     */
    private void processSync(final RequestDetails<A, B> requestDetails) {
        //Since we are processing synchronously, we will not remove the head of the queue immediately but rather
        //only remove the head once it completes
        //final RequestDetails<A, B> requestDetails = requestQueue.peek();

        try {
            //Do we have any requests to process?
            if (requestDetails == null) {
                //ConcurrentUtils.sleepUninterrupted(50);
                return;
            }

            final RequestStatus status = requestDetails.getStatus();

            //Only process new REQUESTS
            if (status == RequestStatus.NEW) {
                log.debug("Processing NEW request from Queue: {}", requestDetails);
                requestDetails.setStatus(RequestStatus.ACCEPTED);

                //Register the request to the session manager
                final SessionId id = sessionManager.create(requestDetails);

                //Update the status to registered
                requestDetails.setStatus(RequestStatus.REGISTERED);

                final CompletableFuture<Void> writeFuture = transport.send(requestDetails.getRequest());

                requestDetails.setStatus(RequestStatus.AWAIT);

                //Perform actions upon write completion
                writeFuture.whenComplete((aVoid, writeError) -> {
                    //If we encounter a write error, notify the listeners then immediately remove it from the queue
                    if (writeError != null) {
                        requestDetails.setStatus(RequestStatus.DONE);
                        requestDetails.getClientPromise().completeExceptionally(writeError);
                        requestQueue.remove(requestDetails);
                        performSessionCleanup(id);
                        log.debug("Error sending request : {}", requestDetails.getRequest());
                    }
                    //Write operation successful
                    else {
                        log.debug("Request Successfully Sent to the Transport : {}", requestDetails.getRequest());
                        //Update status to SENT
                        requestDetails.setStatus(RequestStatus.SENT);

                        //Requests will only be removed from the queue when it completes
                        requestDetails.getClientPromise().whenComplete((res, error) -> {
                            requestDetails.setStatus(RequestStatus.DONE);
                            //Only remove from the queue once the task completes
                            requestQueue.remove(requestDetails);
                            //Perform session cleanup
                            performSessionCleanup(id);
                        });
                    }
                });
            } else {
                requestQueue.putLast(requestDetails);
            }
        } catch (Exception e) {
            if (requestDetails.getClientPromise() != null)
                requestDetails.getClientPromise().completeExceptionally(e);
        }
    }

    /**
     * Process requests asynchronously
     */
    private void processAsync(final RequestDetails<A, B> requestDetails) {
        //Remove the head of the queue immediately and process accordingly
        //final RequestDetails<A, B> requestDetails = requestQueue.take();

        //Only process new requests
        if (requestDetails.getStatus() == RequestStatus.NEW) {
            try {
                //Set status to ACCEPTED
                requestDetails.setStatus(RequestStatus.ACCEPTED);

                //Register the session immediately, duplicate requests will be queued in the order they are sent.
                final SessionId id = sessionManager.create(requestDetails);

                //Perform session cleanup operations on completion
                requestDetails.getClientPromise().whenComplete((response, throwable) -> performSessionCleanup(id));

                //Send then listen for the write completion status
                transport.send(requestDetails.getRequest()).whenComplete((aVoid, writeError) -> {
                    if (writeError != null) {
                        //If the write operation failed, we need to unregister from the session
                        log.error("Write operation failed, unregistering from session : {} = {}", id, writeError);
                        requestDetails.setStatus(RequestStatus.DONE);
                        //Notify listeners
                        if (requestDetails.getClientPromise() != null)
                            requestDetails.getClientPromise().completeExceptionally(writeError);
                    } else {
                        //Update the request status
                        requestDetails.setStatus(RequestStatus.SENT);
                    }
                });
                requestDetails.setStatus(RequestStatus.AWAIT);
            } catch (Exception e) {
                requestDetails.getClientPromise().completeExceptionally(e);
            }
        }

        //return null;
    }

    /**
     * Unregister from the session
     *
     * @param id
     *         The {@link SessionId} to unregister
     */
    private void performSessionCleanup(SessionId id) {
        final SessionValue session = sessionManager.getSession(id);
        if (session != null) {
            sessionManager.delete(session);
        }
    }

    /**
     * Retrieve the internal request queue
     *
     * @return The {@link BlockingDeque} of this messenger
     */
    public BlockingDeque<RequestDetails<A, B>> getRequestQueue() {
        return requestQueue;
    }

    /**
     * @return Processing mode of the messenger
     */
    public ProcessingMode getProcessingMode() {
        return processingMode;
    }

    /**
     * Sets the processing mode of the messenger
     *
     * @param processingMode
     *         The {@link ProcessingMode} for the messenger
     */
    public void setProcessingMode(ProcessingMode processingMode) {
        this.processingMode = processingMode;
    }

    /**
     * Returns the number of remaining requests in the session
     *
     * @return A {@link Collection} of session entries
     */
    public Collection<Map.Entry<SessionId, SessionValue<A, B>>> getRemaining() {
        return sessionManager.getSessionEntries();
    }

    /**
     * <p>Gracefully close/shutdown all expensive resources this messenger utilizes</p>
     *
     * @throws IOException
     *         Thrown when an attempt to close the resources managed by this Messenger has failed.
     */
    @Override
    public void close() throws IOException {
        if (!requestQueue.isEmpty()) {
            log.warn("Request queue is not yet empty (Total: {})", requestQueue.size());
            for (RequestDetails details : requestQueue) {
                log.warn("\tRequest: {}", details);
            }
        }
        if (!messengerService.isTerminated())
            processRequests.set(false);
        try {
            messengerService.shutdown();
            messengerService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Error on close", e);
        }
        sessionManager.close();
        transport.close();
    }
}
