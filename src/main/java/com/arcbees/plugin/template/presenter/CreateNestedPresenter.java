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

import com.arcbees.plugin.template.domain.presenter.CreatedNestedPresenter;
import com.arcbees.plugin.template.domain.presenter.NestedPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;

public class CreateNestedPresenter {
    public static CreatedNestedPresenter run(PresenterOptions presenterOptions, NestedPresenterOptions nestedPresenterOptions) {
        CreateNestedPresenter createNestedPresenter = new CreateNestedPresenter(presenterOptions, nestedPresenterOptions);
        createNestedPresenter.run();
        return createNestedPresenter.getCreatedNestedPresenter();
    }
    
    private final static String BASE = "./src/main/resources/com/arcbees/plugin/template/presenter/nested";    
    
    private final PresenterOptions presenterOptions;
    private final NestedPresenterOptions nestedPresenterOptions;
    
    private VelocityEngine velocityEngine;
    private CreatedNestedPresenter createdNestedPresenter;

    private CreateNestedPresenter(PresenterOptions presenterOptions, NestedPresenterOptions nestedPresenterOptions) {
        this.presenterOptions = presenterOptions;
        this.nestedPresenterOptions = nestedPresenterOptions;
    }
    
    private void run() {
        createdNestedPresenter = new CreatedNestedPresenter();
        
        setupVelocity();
        process();
    }

    private CreatedNestedPresenter getCreatedNestedPresenter() {
        return createdNestedPresenter;
    }

    private void setupVelocity() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, BASE);
        velocityEngine.init();
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
        context.put("codesplit", nestedPresenterOptions.getCodeSplit());
        context.put("isplace", nestedPresenterOptions.getIsPlace());
        context.put("nametoken", nestedPresenterOptions.getNameToken());
        
        return context;
    }
    
    private void process() {
        processModule();
        processPresenter();
        processUiHandlers();
        processView();
        processViewBinder();
        
        processNameTokens();
        processGinModule(); // TODO ?
        processParentContentSlot(); // TODO ?
        // TODO ?
    }

    private void processModule() {
        String fileName = "__name__Module.java.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        createdNestedPresenter.setModule(new RenderedTemplate(renderFileName(fileName), writer.toString()));
    }

    private void processPresenter() {
        String fileName = "__name__Presenter.java.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        createdNestedPresenter.setPresenter(new RenderedTemplate(renderFileName(fileName), writer.toString()));
    }

    private void processUiHandlers() {
        String fileName = "__name__UiHandlers.java.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        createdNestedPresenter.setUihandlers(new RenderedTemplate(renderFileName(fileName), writer.toString()));
    }

    private void processView() {
        String fileName = "__name__View.java.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        createdNestedPresenter.setView(new RenderedTemplate(renderFileName(fileName), writer.toString()));
    }

    private void processViewBinder() {
        String fileName = "__name__View.ui.xml.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        createdNestedPresenter.setViewui(new RenderedTemplate(renderFileName(fileName), writer.toString()));
    }
    
    private void processNameTokens() {
        // TODO
    }

    private void processGinModule() {
        // TODO
    }

    private void processParentContentSlot() {
        // TODO
    }
    
    private String renderFileName(String fileName) {
        String name = presenterOptions.getName();
        name = name.replace(".vm", "");
        return fileName.replace("__name__", name);
    }
}
