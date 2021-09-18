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

package com.ibasco.agql.core.session;

import com.ibasco.agql.core.AbstractRequest;
import com.ibasco.agql.core.AbstractResponse;
import com.ibasco.agql.core.RequestDetails;
import com.ibasco.agql.core.Transport;
import io.netty.util.Timeout;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Contains the details of the given Session
 */
public class SessionValue<Req extends AbstractRequest, Res extends AbstractResponse> {
    private static final Logger log = LoggerFactory.getLogger(SessionValue.class);

    private Timeout timeout;
    private SessionId id;
    private RequestDetails<Req, Res> requestDetails;
    private final long timeRegistered = System.currentTimeMillis();
    private Class<? extends AbstractResponse> expectedResponse;
    private long index = -1;

    public SessionValue(SessionId id, RequestDetails<Req, Res> requestDetails, long index) {
        this.id = id;
        this.requestDetails = requestDetails;
        this.index = index;
    }

    public SessionId getId() {
        return id;
    }

    public void setId(SessionId id) {
        this.id = id;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }

    public long getTimeRegistered() {
        return timeRegistered;
    }

    public CompletableFuture<Res> getClientPromise() {
        return this.requestDetails.getClientPromise();
    }

    public Req getRequest() {
        return this.requestDetails.getRequest();
    }

    public Transport<Req> getRequestTransport() {
        return this.requestDetails.getTransport();
    }

    public RequestDetails<Req, Res> getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(RequestDetails<Req, Res> requestDetails) {
        this.requestDetails = requestDetails;
    }

    public Class<? extends AbstractResponse> getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(Class<? extends AbstractResponse> expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public long getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        log.info("Access equals for {}", this);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SessionValue<?, ?> that = (SessionValue<?, ?>) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getIndex(), that.getIndex())
                .append(getRequest(), that.getRequest())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(421, 743)
                .append(getId())
                .append(getIndex())
                .append(getRequest())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("Id", this.getId())
                .append("Index", this.getIndex())
                .append("Request", this.getRequestDetails().getRequest().getClass().getSimpleName())
                .append("Created", this.getTimeRegistered())
                .toString();
    }
}
