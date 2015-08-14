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

package com.arcbees.plugin.template.presenter.popup;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.arcbees.plugin.template.create.presenter.CreatePopupPresenter;
import com.arcbees.plugin.template.domain.presenter.CreatedPopupPresenter;
import com.arcbees.plugin.template.domain.presenter.PopupPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;

public class CreatePopupPresenterTest {
    private final String PACKAGE_NAME = "com.arcbees.project.client.app";
    private final String APP_NAME = "MyAppHome";

    @Test
    public void testPopupPresenterCreationLocal() {
        PresenterOptions presenterOptions = new PresenterOptions();
        presenterOptions.setPackageName(PACKAGE_NAME);
        presenterOptions.setName(APP_NAME);

        PopupPresenterOptions options = new PopupPresenterOptions();
        options.setSingleton(true);

        CreatedPopupPresenter created;
        try {
            created = CreatePopupPresenter.run(presenterOptions, options, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
            return;
        }

        Assert.assertNotNull(created.getModule().getContents());
        Assert.assertNotNull(created.getPresenter().getContents());
        Assert.assertNotNull(created.getUihandlers().getContents());
        Assert.assertNotNull(created.getView().getContents());
        Assert.assertNotNull(created.getViewui().getContents());
    }

    @Test
    public void testPopupPresenterCreationRemote() {
        PresenterOptions presenterOptions = new PresenterOptions();
        presenterOptions.setPackageName(PACKAGE_NAME);
        presenterOptions.setName(APP_NAME);

        PopupPresenterOptions options = new PopupPresenterOptions();
        options.setSingleton(true);

        CreatedPopupPresenter created;
        try {
            created = CreatePopupPresenter.run(presenterOptions, options, true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
            return;
        }

        Assert.assertNotNull(created.getModule().getContents());
        Assert.assertNotNull(created.getPresenter().getContents());
        Assert.assertNotNull(created.getUihandlers().getContents());
        Assert.assertNotNull(created.getView().getContents());
        Assert.assertNotNull(created.getViewui().getContents());
    }
}
