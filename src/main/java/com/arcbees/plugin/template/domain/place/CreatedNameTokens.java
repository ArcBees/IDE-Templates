/**
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.plugin.template.domain.place;

import java.util.ArrayList;
import java.util.List;

import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;

public class CreatedNameTokens {
    private RenderedTemplate nameTokens;
    private List<String> fields;
    private List<String> methods;

    public CreatedNameTokens() {
    }

    public RenderedTemplate getNameTokens() {
        return nameTokens;
    }

    public void setNameTokens(RenderedTemplate nameTokens) {
        this.nameTokens = nameTokens;
    }
    
    public void addMethod(String method) {
        if (methods == null) {
            methods = new ArrayList<String>();
        }
        methods.add(method);
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
    
    public void addField(String field) {
        if (fields == null) {
            fields = new ArrayList<String>();
        }
        fields.add(field);
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
