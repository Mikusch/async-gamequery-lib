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

package com.ibasco.agql.protocols.valve.source.query;

public final class SourceRconAuthStatus {
    private boolean authenticated;
    private String reason;

    public SourceRconAuthStatus(boolean authenticated, String reason) {
        this.authenticated = authenticated;
        this.reason = reason;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "SourceRconAuthStatus{" +
                "authenticated=" + authenticated +
                ", reason='" + reason + '\'' +
                '}';
    }
}
