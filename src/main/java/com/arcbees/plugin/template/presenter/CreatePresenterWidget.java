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

package com.arcbees.plugin.template.presenter;

import com.arcbees.plugin.template.domain.presenter.PresenterOptions;

public class CreatePresenterWidget {
    public static void run(PresenterOptions presenterOptions, PresenterWidgetOptions presenterWidgetOptions) {
        new CreatePresenterWidget(presenterOptions, presenterWidgetOptions).run();
    }
    
    private PresenterOptions presenterOptions;
    private PresenterWidgetOptions nestedWidgetOptions;

    private CreatePresenterWidget(PresenterOptions presenterOptions, PresenterWidgetOptions nestedWidgetOptions) {
        this.presenterOptions = presenterOptions;
        this.nestedWidgetOptions = nestedWidgetOptions;
    }
    
    private void run() {
        
    }
}
