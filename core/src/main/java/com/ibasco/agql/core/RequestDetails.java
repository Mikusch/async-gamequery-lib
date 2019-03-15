/*
 * MIT License
 *
 * Copyright (c) 2019 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.core;

import com.ibasco.agql.core.enums.RequestPriority;
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
public class RequestDetails<R extends AbstractRequest, S extends AbstractResponse> implements Comparable<RequestDetails> {
    private static final Logger log = LoggerFactory.getLogger(RequestDetails.class);
    private R request;
    private CompletableFuture<S> clientPromise;
    private RequestStatus status;
    private RequestPriority priority;
    private Transport<R> transport;
    private Class<S> expectedResponseClass;
    private AtomicInteger retries = new AtomicInteger(0);
    private long timeCreated;

    public RequestDetails(R request, CompletableFuture<S> clientPromise, RequestPriority priority, Transport<R> transport) {
        this.status = RequestStatus.NEW;
        this.request = request;
        this.clientPromise = clientPromise;
        this.priority = priority;
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
        this.priority = requestDetails.getPriority();
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

    public synchronized RequestPriority getPriority() {
        return priority;
    }

    public synchronized void setPriority(RequestPriority priority) {
        this.priority = priority;
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
    public int compareTo(RequestDetails o) {
        return this.priority.compareTo(o.getPriority());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("Request", this.getRequest().getClass().getSimpleName())
                .append("Created", this.getTimeCreated())
                .append("Priority", this.getPriority())
                .append("Status", this.getStatus())
                .toString();
    }
}
