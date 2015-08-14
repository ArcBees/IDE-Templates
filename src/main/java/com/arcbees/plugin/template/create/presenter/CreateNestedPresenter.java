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

import com.arcbees.plugin.template.domain.presenter.CreatedNestedPresenter;
import com.arcbees.plugin.template.domain.presenter.NestedPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.arcbees.plugin.template.utils.VelocityUtils;

public class CreateNestedPresenter {
    public final static Logger logger = Logger.getLogger(CreateNestedPresenter.class.getName());
            
    public static CreatedNestedPresenter run(PresenterOptions presenterOptions,
            NestedPresenterOptions nestedPresenterOptions, boolean remote) throws Exception {
        CreateNestedPresenter createNestedPresenter = new CreateNestedPresenter(presenterOptions,
                nestedPresenterOptions, remote);
        createNestedPresenter.run();
        return createNestedPresenter.getCreatedNestedPresenter();
    }

    private static final String BASE_REMOTE = "https://raw.githubusercontent.com/ArcBees/IDE-Templates/1.2.0/src/main/resources/com/arcbees/plugin/template/presenter/nested/";
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

    private void setupVelocityLocal() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, BASE_LOCAL);
        try {
        	//velocityEngine.reset();
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

    private CreatedNestedPresenter getCreatedNestedPresenter() {
        return createdNestedPresenter;
    }

    private VelocityContext getBaseVelocityContext() {
        VelocityContext context = new VelocityContext();

        // base
        context.put("package", presenterOptions.getPackageName());
        context.put("name", presenterOptions.getName());

        // extra options
        context.put("uihandlers", presenterOptions.getUiHandlers());
        context.put("manualreveal", presenterOptions.getManualReveal());

        // nested presenter options
        context.put("revealType", nestedPresenterOptions.getRevealType());
        context.put("codesplit", nestedPresenterOptions.getCodeSplit());
        context.put("isplace", nestedPresenterOptions.getIsPlace());
        context.put("contentSlotImport", nestedPresenterOptions.getContentSlotImport());
        context.put("nameTokenImport", nestedPresenterOptions.getNameTokenImport());
        context.put("nametoken", nestedPresenterOptions.getNameToken());

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
        createdNestedPresenter.setModule(rendered);
    }

    private void processPresenter() throws Exception {
        String fileName = "__name__Presenter.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setPresenter(rendered);
    }

    private void processUiHandlers() throws Exception {
        String fileName = "__name__UiHandlers.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setUihandlers(rendered);
    }

    private void processView() throws Exception {
        String fileName = "__name__View.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setView(rendered);
    }

    private void processViewBinder() throws Exception {
        String fileName = "__name__View.ui.xml.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdNestedPresenter.setViewui(rendered);
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
