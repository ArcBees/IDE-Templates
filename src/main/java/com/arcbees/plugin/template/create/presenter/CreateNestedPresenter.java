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

import arcbees.org.apache.velocity.Template;
import arcbees.org.apache.velocity.VelocityContext;
import arcbees.org.apache.velocity.app.VelocityEngine;
import arcbees.org.apache.velocity.exception.ParseErrorException;
import arcbees.org.apache.velocity.exception.ResourceNotFoundException;
import arcbees.org.apache.velocity.runtime.RuntimeConstants;
import arcbees.org.apache.velocity.runtime.resource.loader.URLResourceLoader;

import com.arcbees.plugin.template.domain.presenter.CreatedNestedPresenter;
import com.arcbees.plugin.template.domain.presenter.NestedPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;

public class CreateNestedPresenter {
    public final static Logger logger = Logger.getLogger(CreateNestedPresenter.class.getName());
            
    public static CreatedNestedPresenter run(PresenterOptions presenterOptions,
            NestedPresenterOptions nestedPresenterOptions, boolean remote) throws Exception {
        CreateNestedPresenter createNestedPresenter = new CreateNestedPresenter(presenterOptions,
                nestedPresenterOptions, remote);
        createNestedPresenter.run();
        return createNestedPresenter.getCreatedNestedPresenter();
    }

    private static final String BASE_REMOTE = "https://raw.github.com/ArcBees/IDE-Templates/1.0.0/src/main/resources/com/arcbees/plugin/template/presenter/nested/";
    private final static String BASE_LOCAL = "./src/main/resources/com/arcbees/plugin/template/presenter/nested/";

    private final PresenterOptions presenterOptions;
    private final NestedPresenterOptions nestedPresenterOptions;

    private VelocityEngine velocityEngine;
    private CreatedNestedPresenter createdNestedPresenter;
    private boolean remote;

    private CreateNestedPresenter(PresenterOptions presenterOptions, NestedPresenterOptions nestedPresenterOptions,
            boolean remote) {
        this.presenterOptions = presenterOptions;
        this.nestedPresenterOptions = nestedPresenterOptions;
        this.remote = remote;
    }

    private void run() throws Exception {
        createdNestedPresenter = new CreatedNestedPresenter();

        if (remote) {
            setupVelocityRemote();
        } else {
            setupVelocityLocal();
        }

        process();
    }

    private void setupVelocityLocal() throws Exception {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, BASE_LOCAL);
        velocityEngine.init();
    }

    private void setupVelocityRemote() {
        try {
//            URLResourceLoader loader = new URLResourceLoader();
            velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "url");
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_MANAGER_CLASS, URLResourceLoader.class.getName());
//            velocityEngine.setProperty("url.resource.loader.instance", loader);
            velocityEngine.setProperty("url.resource.loader.timeout", new Integer(5000));
            velocityEngine.setProperty("url.resource.loader.root", BASE_REMOTE);
            velocityEngine.setProperty("runtime.log.logsystem.class", "arcbees.org.apache.velocity.runtime.log.AvalonLogChute");
            velocityEngine.init();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Velocity Init Error", e);
            e.printStackTrace();
        }
    }

    private CreatedNestedPresenter getCreatedNestedPresenter() {
        return createdNestedPresenter;
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

        // nested presenter options
        context.put("revealType", nestedPresenterOptions.getRevealType());
        context.put("codesplit", nestedPresenterOptions.getCodeSplit());
        context.put("isplace", nestedPresenterOptions.getIsPlace());
        context.put("contentSlotImport", nestedPresenterOptions.getContentSlotImport());
        context.put("nameTokenImport", nestedPresenterOptions.getNameTokenImport());
        context.put("nametoken", nestedPresenterOptions.getNameToken());

        return context;
    }

    private void process() throws ResourceNotFoundException, ParseErrorException, Exception {
        processModule();
        processPresenter();
        processUiHandlers();
        processView();
        processViewBinder();
    }

    private void processModule() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__Module.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setModule(rendered);
    }

    private void processPresenter() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__Presenter.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setPresenter(rendered);
    }

    private void processUiHandlers() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__UiHandlers.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setUihandlers(rendered);
    }

    private void processView() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__View.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setView(rendered);
    }

    private void processViewBinder() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__View.ui.xml.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setViewui(rendered);
    }

    private RenderedTemplate processTemplate(String fileName) throws ResourceNotFoundException, ParseErrorException, Exception {
        Template template = velocityEngine.getTemplate(fileName);
        VelocityContext context = getBaseVelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        RenderedTemplate rendered = new RenderedTemplate(renderFileName(fileName), writer.toString());
        return rendered;
    }

    private String renderFileName(String fileName) {
        String name = presenterOptions.getName();
        name = name.replace(".vm", "");
        return fileName.replace("__name__", name);
    }
}
