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

package com.arcbees.plugin.template.create.presenter;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.arcbees.plugin.template.domain.presenter.CreatedPresenterWidget;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterWidgetOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.arcbees.plugin.template.utils.VelocityUtils;

public class CreatePresenterWidget {
    public final static Logger logger = Logger.getLogger(CreatePresenterWidget.class.getName());

    public static CreatedPresenterWidget run(PresenterOptions presenterOptions,
                    PresenterWidgetOptions presenterWidgetOptions, boolean remote) throws Exception {
        CreatePresenterWidget createdPresenterWidget = new CreatePresenterWidget(presenterOptions,
                        presenterWidgetOptions, remote);
        createdPresenterWidget.run();
        return createdPresenterWidget.getCreatedPresenterWidget();
    }

    private static final String BASE_REMOTE = "https://raw.githubusercontent.com/ArcBees/IDE-Templates/1.2.0/src/main/resources/com/arcbees/plugin/template/presenter/widget/";
    private final static String BASE_LOCAL = "./src/main/resources/com/arcbees/plugin/template/presenter/widget/";

    private final PresenterOptions presenterOptions;
    private PresenterWidgetOptions presenterWidgetOptions;

    private VelocityEngine velocityEngine;
    private CreatedPresenterWidget createdPresenterWidget;
    private boolean remote;

    private CreatePresenterWidget(PresenterOptions presenterOptions, PresenterWidgetOptions presenterWidgetOptions,
                    boolean remote) {
        this.presenterOptions = presenterOptions;
        this.presenterWidgetOptions = presenterWidgetOptions;
        this.remote = remote;
    }

    private void run() throws Exception {
        createdPresenterWidget = new CreatedPresenterWidget();

        if (remote) {
            setupVelocityRemote();
        } else {
            setupVelocityLocal();
        }

        process();
    }

    private void setupVelocityLocal() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, BASE_LOCAL);
        try {
            velocityEngine.init();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Velocity Init Error Local", e);
            e.printStackTrace();
        }
    }

    private void setupVelocityRemote() throws Exception {
        try {
            velocityEngine = VelocityUtils.createRemoveVelocityEngine(BASE_REMOTE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Velocity Init Error", e);
            e.printStackTrace();
            throw e;
        }
    }

    private CreatedPresenterWidget getCreatedPresenterWidget() {
        return createdPresenterWidget;
    }

    private VelocityContext getBaseVelocityContext() {
        VelocityContext context = new VelocityContext();

        // base
        context.put("package", presenterOptions.getPackageName());
        context.put("name", presenterOptions.getName());

        // extra options
        context.put("uihandlers", presenterOptions.getUihandlers());
        context.put("onbind", presenterOptions.getOnbind());
        context.put("onhide", presenterOptions.getOnhide());
        context.put("onreset", presenterOptions.getOnreset());
        context.put("onunbind", presenterOptions.getOnunbind());
        context.put("manualreveal", presenterOptions.getManualReveal());
        context.put("preparefromrequest", presenterOptions.getPrepareFromRequest());

        // presenter widget options
        context.put("singleton", presenterWidgetOptions.getSingleton());

        return context;
    }

    private void process() throws Exception {
        processModule();
        processPresenter();
        processUiHandlers();
        processView();
        processViewBinder();
    }

    private void processModule() throws Exception {
        String fileName = "__name__Module.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPresenterWidget.setModule(rendered);
    }

    private void processPresenter() throws Exception {
        String fileName = "__name__Presenter.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPresenterWidget.setPresenter(rendered);
    }

    private void processUiHandlers() throws Exception {
        String fileName = "__name__UiHandlers.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPresenterWidget.setUihandlers(rendered);
    }

    private void processView() throws Exception {
        String fileName = "__name__View.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPresenterWidget.setView(rendered);
    }

    private void processViewBinder() throws Exception {
        String fileName = "__name__View.ui.xml.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPresenterWidget.setViewui(rendered);
    }

    private RenderedTemplate processTemplate(String fileName) throws ResourceNotFoundException, ParseErrorException {
        Template template = velocityEngine.getTemplate(fileName);
        VelocityContext context = getBaseVelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return new RenderedTemplate(renderFileName(fileName), writer.toString());
    }

    private String renderFileName(String fileName) {
        String name = presenterOptions.getName();
        name = name.replace(".vm", "");

        return fileName.replace("__name__", name);
    }
}
