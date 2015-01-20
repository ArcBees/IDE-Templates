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

import com.arcbees.plugin.template.domain.presenter.CreatedPopupPresenter;
import com.arcbees.plugin.template.domain.presenter.PopupPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.arcbees.plugin.template.utils.VelocityUtils;

public class CreatePopupPresenter {
    public final static Logger logger = Logger.getLogger(CreatePresenterWidget.class.getName());

    public static CreatedPopupPresenter run(PresenterOptions presenterOptions,
                    PopupPresenterOptions popupPresenterOptions, boolean remote) throws Exception {
        CreatePopupPresenter createdPopupPresenter = new CreatePopupPresenter(presenterOptions, popupPresenterOptions,
                        remote);
        createdPopupPresenter.run();
        return createdPopupPresenter.getCreatedPopupPresenter();
    }

    // TODO should be more
    private static final String BASE_REMOTE_GWT = "https://raw.githubusercontent.com/ArcBees/IDE-Templates/1.2.0/src/main/resources/com/arcbees/plugin/template/presenter/popup/gwt/";
    private static final String BASE_REMOTE_CUSTOM = "https://raw.githubusercontent.com/ArcBees/IDE-Templates/1.2.0/src/main/resources/com/arcbees/plugin/template/presenter/popup/custom/";
    private final static String BASE_LOCAL_GWT = "./src/main/resources/com/arcbees/plugin/template/presenter/popup/gwt/";
    private final static String BASE_LOCAL_CUSTOM = "./src/main/resources/com/arcbees/plugin/template/presenter/popup/custom/";

    private String baseLocal;
    private String baseRemote;

    private final PresenterOptions presenterOptions;
    private PopupPresenterOptions popupPresenterOptions;

    private VelocityEngine velocityEngine;
    private CreatedPopupPresenter createdPopupPresenter;
    private boolean remote;

    private CreatePopupPresenter(PresenterOptions presenterOptions, PopupPresenterOptions popupPresenterOptions,
                    boolean remote) {
        this.presenterOptions = presenterOptions;
        this.popupPresenterOptions = popupPresenterOptions;
        this.remote = remote;
    }

    private void run() throws Exception {
        createdPopupPresenter = new CreatedPopupPresenter();

        // decide which template base to use custom or gwt
        if (remote) {
            if (popupPresenterOptions.getCustom()) {
                baseRemote = BASE_REMOTE_CUSTOM;
            } else {
                baseRemote = BASE_REMOTE_GWT;
            }
        } else {
            if (popupPresenterOptions.getCustom()) {
                baseLocal = BASE_LOCAL_CUSTOM;
            } else {
                baseLocal = BASE_LOCAL_GWT;
            }
        }

        if (remote) {
            setupVelocityRemote();
        } else {
            setupVelocityLocal();
        }

        process();
    }

    private void setupVelocityLocal() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, baseLocal);
        try {
            velocityEngine.init();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Velocity Init Error Local", e);
            e.printStackTrace();
        }
    }

    private void setupVelocityRemote() throws Exception {
        try {
            velocityEngine = VelocityUtils.createRemoveVelocityEngine(baseRemote);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Velocity Init Error", e);
            e.printStackTrace();
            throw e;
        }
    }

    private CreatedPopupPresenter getCreatedPopupPresenter() {
        return createdPopupPresenter;
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

        // popup presenter options
        context.put("singleton", popupPresenterOptions.getSingleton());

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
        createdPopupPresenter.setModule(rendered);
    }

    private void processPresenter() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__Presenter.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPopupPresenter.setPresenter(rendered);
    }

    private void processUiHandlers() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__UiHandlers.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPopupPresenter.setUihandlers(rendered);
    }

    private void processView() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__View.java.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPopupPresenter.setView(rendered);
    }

    private void processViewBinder() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "__name__View.ui.xml.vm";
        RenderedTemplate rendered = processTemplate(fileName);
        createdPopupPresenter.setViewui(rendered);
    }

    private RenderedTemplate processTemplate(String fileName) throws ResourceNotFoundException, ParseErrorException,
                    Exception {
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
