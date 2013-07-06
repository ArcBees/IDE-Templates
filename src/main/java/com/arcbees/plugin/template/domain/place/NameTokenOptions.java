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

import java.util.List;

public class NameTokenOptions {
    private String packageName;
    private List<NameToken> nameTokens;

    public NameTokenOptions() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<NameToken> getNameTokens() {
        return nameTokens;
    }

    public void setNameTokens(List<NameToken> nameTokens) {
        this.nameTokens = nameTokens;
    }

    public String getMethodName(int index) {
        if (nameTokens == null || nameTokens.size() - 1 < index) {
            return "";
        }
        String nameToken = nameTokens.get(index).getToken();
        String s = "";
        s += nameToken.substring(0, 1).toUpperCase();
        s += nameToken.substring(1);
        return s;
    }
}
