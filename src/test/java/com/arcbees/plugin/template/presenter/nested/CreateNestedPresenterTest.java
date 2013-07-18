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

package com.arcbees.plugin.template.presenter.nested;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.arcbees.plugin.template.create.presenter.CreateNestedPresenter;
import com.arcbees.plugin.template.domain.presenter.CreatedNestedPresenter;
import com.arcbees.plugin.template.domain.presenter.NestedPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;

public class CreateNestedPresenterTest {
    @Test
    public void testPresenterCreationLocal() {
        PresenterOptions presenterOptions = new PresenterOptions();
        presenterOptions.setPackageName("com.arcbees.project.client.app");
        presenterOptions.setName("MyAppHome");
        
        NestedPresenterOptions nestedPresenterOptions = new NestedPresenterOptions();
        nestedPresenterOptions.setCodeSplit(true);
        
        CreatedNestedPresenter created;
		try {
			created = CreateNestedPresenter.run(presenterOptions, nestedPresenterOptions, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			return;
		}
        
        // Debugging
        System.out.println(created.getModule().getContents());
        System.out.println(created.getPresenter().getContents());
        System.out.println(created.getUihandlers().getContents());
        System.out.println(created.getView().getContents());
        System.out.println(created.getViewui().getContents());
        
        Assert.assertNotNull(created.getModule().getContents());
        Assert.assertNotNull(created.getPresenter().getContents());
        Assert.assertNotNull(created.getUihandlers().getContents());
        Assert.assertNotNull(created.getView().getContents());
        Assert.assertNotNull(created.getViewui().getContents());
    }
    
    @Test
    public void testPresenterCreationRemote() {
        PresenterOptions presenterOptions = new PresenterOptions();
        presenterOptions.setPackageName("com.arcbees.project.client.app");
        presenterOptions.setName("MyAppHome");
        
        NestedPresenterOptions nestedPresenterOptions = new NestedPresenterOptions();
        nestedPresenterOptions.setCodeSplit(true);
        
        CreatedNestedPresenter created;
		try {
			created = CreateNestedPresenter.run(presenterOptions, nestedPresenterOptions, true);
		} catch (Exception e) {
			fail();
			return;
		}
        
        // Debugging
        System.out.println(created.getModule().getContents());
        System.out.println(created.getPresenter().getContents());
        System.out.println(created.getUihandlers().getContents());
        System.out.println(created.getView().getContents());
        System.out.println(created.getViewui().getContents());
        
        Assert.assertNotNull(created.getModule().getContents());
        Assert.assertNotNull(created.getPresenter().getContents());
        Assert.assertNotNull(created.getUihandlers().getContents());
        Assert.assertNotNull(created.getView().getContents());
        Assert.assertNotNull(created.getViewui().getContents());
    }
}
