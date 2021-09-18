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

import com.ibasco.agql.core.enums.RequestStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains all the properties associated with this request
 */
public class RequestDetails<R extends AbstractRequest, S extends AbstractResponse> {
    private static final Logger log = LoggerFactory.getLogger(RequestDetails.class);
    private R request;
    private CompletableFuture<S> clientPromise;
    private RequestStatus status;
    private Transport<R> transport;
    private Class<S> expectedResponseClass;
    private AtomicInteger retries = new AtomicInteger(0);
    private long timeCreated;

    public RequestDetails(R request, CompletableFuture<S> clientPromise, Transport<R> transport) {
        this.status = RequestStatus.NEW;
        this.request = request;
        this.clientPromise = clientPromise;
        this.transport = transport;
        this.timeCreated = System.currentTimeMillis();
    }

    /**
     * Copy Constructor
     *
     * @param requestDetails An {@link RequestDetails} that will be used as reference for the copy
     */
    public RequestDetails(RequestDetails<R, S> requestDetails) {
        this.request = requestDetails.getRequest();
        this.clientPromise = requestDetails.getClientPromise();
        this.status = requestDetails.getStatus();
        this.retries = new AtomicInteger(requestDetails.getRetries());
        this.expectedResponseClass = requestDetails.getExpectedResponseClass();
    }

    public Class<S> getExpectedResponseClass() {
        return expectedResponseClass;
    }

    public void setExpectedResponseClass(Class<S> expectedResponseClass) {
        this.expectedResponseClass = expectedResponseClass;
    }

    public synchronized CompletableFuture<S> getClientPromise() {
        return clientPromise;
    }

    public synchronized void setClientPromise(CompletableFuture<S> clientPromise) {
        this.clientPromise = clientPromise;
    }

    public int getRetries() {
        return retries.get();
    }

    public synchronized void setRequest(R request) {
        this.request = request;
    }

    public synchronized R getRequest() {
        return request;
    }

    public synchronized RequestStatus getStatus() {
        return this.status;
    }

    public synchronized void setStatus(RequestStatus status) {
        this.status = status;
    }

    public int incrementRetry() {
        return this.retries.getAndAdd(1);
    }

    public Transport<R> getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof RequestDetails))
            return false;
        final RequestDetails rhs = (RequestDetails) o;
        return new EqualsBuilder()
                .append(this.getClass(), rhs.getClass())
                .append(this.getRequest(), rhs.getRequest())
                .append(this.getTimeCreated(), rhs.getTimeCreated())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(327, 993)
                .append(this.getClass())
                .append(this.getRequest())
                .append(this.getTimeCreated())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("Request", this.getRequest().getClass().getSimpleName())
                .append("Created", this.getTimeCreated())
                .append("Status", this.getStatus())
                .toString();
    }
}
