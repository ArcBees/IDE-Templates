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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;

import com.arcbees.plugin.template.create.place.CreatedNameTokens;
import com.arcbees.plugin.template.create.place.NameToken;
import com.arcbees.plugin.template.create.place.NameTokenOptions;
import com.arcbees.plugin.template.domain.place.CreateNameTokens;
import com.arcbees.plugin.template.domain.presenter.CreatedNestedPresenter;
import com.arcbees.plugin.template.domain.presenter.NestedPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;

public class CreateNestedPresenter {
    public static CreatedNestedPresenter run(PresenterOptions presenterOptions,
            NestedPresenterOptions nestedPresenterOptions, boolean remote) {
        CreateNestedPresenter createNestedPresenter = new CreateNestedPresenter(presenterOptions,
                nestedPresenterOptions, remote);
        createNestedPresenter.run();
        return createNestedPresenter.getCreatedNestedPresenter();
    }

    private static final String BASE_REMOTE = "https://raw.github.com/ArcBees/IDE-Templates/1.0.0/src/main/resources/com/arcbees/plugin/template/presenter/nested";
    private final static String BASE_LOCAL = "./src/main/resources/com/arcbees/plugin/template/presenter/nested";

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

    private void run() {
        createdNestedPresenter = new CreatedNestedPresenter();

        if (remote) {
            setupVelocityRemote();
        } else {
            setupVelocityLocal();
        }
        
        process();
    }

    private void setupVelocityLocal() {
        String pathToTemplates = BASE_LOCAL;
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, pathToTemplates);
        velocityEngine.init();
    }
    
    private void setupVelocityRemote() { 
        String pathToTemplates = BASE_REMOTE;
        URLResourceLoader loader = new URLResourceLoader();
        velocityEngine = new VelocityEngine();
        // TODO
        velocityEngine.setProperty("resource.loader", pathToTemplates);
        velocityEngine.setProperty("url.resource.loader.instance", loader);
        velocityEngine.setProperty("url.resource.loader.timeout", new Integer(5000));
        
        //velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, logger);
        velocityEngine.setProperty("runtime.log.logsystem.test.level", "debug");
        
        velocityEngine.init();
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
        processFile(fileName);
    }

    private void processPresenter() {
        String fileName = "__name__Presenter.java.vm";
        processFile(fileName);
    }

    private void processUiHandlers() {
        String fileName = "__name__UiHandlers.java.vm";
        processFile(fileName);
    }

    private void processView() {
        String fileName = "__name__View.java.vm";
        processFile(fileName);
    }

    private void processViewBinder() {
        String fileName = "__name__View.ui.xml.vm";
        processFile(fileName);
    }

    private void processFile(String fileName) {
        Template template = velocityEngine.getTemplate(fileName);
        VelocityContext context = getBaseVelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        RenderedTemplate rendered = new RenderedTemplate(renderFileName(fileName), writer.toString());
        createdNestedPresenter.setModule(rendered);
        createFile(rendered);
    }

    private void createFile(RenderedTemplate rendered) {
        String fileName = rendered.getName();
        
        System.out.println("fileName=" + fileName + " " + rendered.getContents());
    }

    private void processNameTokens() {
        NameToken token = new NameToken();
        token.setCrawlable(nestedPresenterOptions.getCrawlable());
        token.setToken(nestedPresenterOptions.getNameToken());

        List<NameToken> nameTokens = new ArrayList<NameToken>();
        nameTokens.add(token);

        NameTokenOptions nameTokenOptions = new NameTokenOptions();
        nameTokenOptions.setNameTokens(nameTokens);

        CreatedNameTokens createdNameToken = CreateNameTokens.run(nameTokenOptions);
        createdNestedPresenter.setNameTokens(createdNameToken);
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
