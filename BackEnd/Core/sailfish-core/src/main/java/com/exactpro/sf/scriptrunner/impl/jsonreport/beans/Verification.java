/*******************************************************************************
 * Copyright 2009-2019 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ******************************************************************************/

package com.exactpro.sf.scriptrunner.impl.jsonreport.beans;

import com.exactpro.sf.scriptrunner.StatusDescription;
import com.exactpro.sf.scriptrunner.impl.jsonreport.IJsonReportNode;

import java.util.ArrayList;
import java.util.List;

public class Verification implements IJsonReportNode {
    private Long messageId;
    private String name;
    private String description;
    private StatusDescription status;
    private List<VerificationEntry> entries;

    public Verification() {
        this.entries = new ArrayList<>();
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusDescription getStatus() {
        return status;
    }

    public void setStatus(StatusDescription status) {
        this.status = status;
    }

    public List<VerificationEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<VerificationEntry> entries) {
        this.entries = entries;
    }
}
