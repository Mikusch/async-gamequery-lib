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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * "Acap" stands for Attribute Controlled Attached Particles. An object containing a list
 * of objects that describe the defined particle effects.
 * </p>
 */
public class SteamEconSchemaAcap {
    /**
     * The name of the particle system.
     */
    private String system;
    /**
     * The effect's ID, referred to by the attached particle effect attribute.
     */
    private int id;
    /**
     * A boolean that indicates whether or not the effect is attached to the "root" bone.
     * That is the bone of the item with no parent bones used for rotation and animation calculations.
     */
    private boolean attach_to_rootbone;

    /**
     * (Optional) A string indicating where the effect is attached.
     */
    private String attachment;

    /**
     * The localized name of the effect.
     */
    private String name;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAttach_to_rootbone() {
        return attach_to_rootbone;
    }

    public void setAttach_to_rootbone(boolean attach_to_rootbone) {
        this.attach_to_rootbone = attach_to_rootbone;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
