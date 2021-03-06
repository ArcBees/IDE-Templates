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

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;

public class PresenterTest {
    private VelocityEngine velocityEngine;

    @Before
    public void testSetup() throws Exception {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,
                "./src/main/resources/com/arcbees/plugin/template/presenter/nested/");
        velocityEngine.init();
    }

    @Test
    public void testModule() throws ResourceNotFoundException, ParseErrorException {
    Template t = velocityEngine.getTemplate("__name__Module.java.vm");

        VelocityContext context = new VelocityContext();
        context.put("package", "com.arcbees.project.client.application");
        context.put("name", "ApplicationTest");

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        // TODO
        System.out.println(writer.toString());
    }

    @Test
    public void testPresenter() throws ResourceNotFoundException, ParseErrorException {
        Template t = velocityEngine.getTemplate("__name__Presenter.java.vm");

        VelocityContext context = new VelocityContext();
        context.put("package", "com.arcbees.project.client.application");
        context.put("name", "ApplicationTest");
        context.put("uihandlers", false);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        // TODO
        System.out.println(writer.toString());
    }

    @Test
    public void testPresenterWithUiHandlers() throws ResourceNotFoundException, ParseErrorException {
        Template t = velocityEngine.getTemplate("__name__Presenter.java.vm");

        VelocityContext context = new VelocityContext();
        context.put("package", "com.arcbees.project.client.application");
        context.put("name", "ApplicationTest");
        context.put("uihandlers", true);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        // TODO
        System.out.println(writer.toString());
    }

    @Test
    public void testUiHandlers() throws ResourceNotFoundException, ParseErrorException {
        Template t = velocityEngine.getTemplate("__name__UiHandlers.java.vm");

        VelocityContext context = new VelocityContext();
        context.put("package", "com.arcbees.project.client.application");
        context.put("name", "ApplicationTest");
        context.put("uihandlers", false);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        // TODO
        System.out.println(writer.toString());
    }

    @Test
    public void testView() throws ResourceNotFoundException, ParseErrorException {
        Template t = velocityEngine.getTemplate("__name__View.java.vm");

        VelocityContext context = new VelocityContext();
        context.put("name", "ApplicationTest");
        context.put("uihandlers", false);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        // TODO
        System.out.println(writer.toString());
    }

    @Test
    public void testViewWithUiHandlers() throws ResourceNotFoundException, ParseErrorException {
        Template t = velocityEngine.getTemplate("__name__View.java.vm");

        VelocityContext context = new VelocityContext();
        context.put("name", "ApplicationTest");
        context.put("uihandlers", true);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        // TODO
        System.out.println(writer.toString());
    }

    @Test
    public void testViewUi() throws ResourceNotFoundException, ParseErrorException {
        Template t = velocityEngine.getTemplate("__name__View.ui.xml.vm");

        VelocityContext context = new VelocityContext();
        context.put("package", "com.arcbees.project.client.application");
        context.put("name", "ApplicationTest");
        context.put("uihandlers", false);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        // TODO
        System.out.println(writer.toString());
    }
}
