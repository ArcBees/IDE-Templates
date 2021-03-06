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

package com.arcbees.plugin.template.domain.presenter;

public class PresenterOptions {
    private String name;
    private String packageName;

    private boolean uiHandlers;
    private boolean manualReveal;

    public PresenterOptions() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(boolean uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public boolean getManualReveal() {
        return manualReveal;
    }

    public void setManualReveal(boolean manualReveal) {
        this.manualReveal = manualReveal;
    }
}
