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

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public final class SessionId implements Serializable, Comparable<SessionId> {
    private String id;

    public SessionId(SessionId id) {
        this(id.getId());
    }

    public SessionId(String id) {
        this.id = id;
    }

    public final String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof SessionId || o instanceof String))
            return false;
        if (o == this)
            return true;
        if (o instanceof String) {
            return defaultIfNull(getId(), "").equalsIgnoreCase((String) o);
        }
        return defaultIfNull(getId(), "").equalsIgnoreCase(((SessionId) o).getId());
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(61, 235).append(getId()).hashCode();
    }

    @Override
    public String toString() {
        return "SessionId{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public int compareTo(SessionId o) {
        return new CompareToBuilder().append(getId(), o.getId()).toComparison();
    }
}
