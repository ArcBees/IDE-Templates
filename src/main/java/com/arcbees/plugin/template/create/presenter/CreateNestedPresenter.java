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
import java.util.Map;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.arcbees.plugin.template.domain.presenter.CreatedNestedPresenter;
import com.arcbees.plugin.template.domain.presenter.NestedPresenterOptions;
import com.arcbees.plugin.template.domain.presenter.PresenterOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.arcbees.plugin.template.utils.FetchTemplate;
import com.arcbees.plugin.template.utils.FetchTemplates;

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
	private static final String TEMPLATE_MODULE = "__name__Module.java.vm";
	private static final String TEMPLATE_PRESENTER = "__name__Presenter.java.vm";
	private static final String TEMPLATE_UIHANDLERS = "__name__UiHandlers.java.vm";
	private static final String TEMPLATE_VIEW = "__name__View.java.vm";
	private static final String TEMPLATE_VIEWUI = "__name__View.ui.xml.vm";

    private final PresenterOptions presenterOptions;
    private final NestedPresenterOptions nestedPresenterOptions;

    private CreatedNestedPresenter createdNestedPresenter;
    private boolean remote;
	private Map<String, FetchTemplate> fetchedTemplates;

    private CreateNestedPresenter(PresenterOptions presenterOptions, NestedPresenterOptions nestedPresenterOptions,
            boolean remote) {
        this.presenterOptions = presenterOptions;
        this.nestedPresenterOptions = nestedPresenterOptions;
        this.remote = remote;
    }

    private void run() throws Exception {
        createdNestedPresenter = new CreatedNestedPresenter();
        
        fetchTemplates();
        
        if (fetchedTemplates != null && fetchedTemplates.size() > 0) {
        	process();
        } else {
        	throw new Exception("ERROR: Couldn't fetch templates...");
        }
    }

    private void fetchTemplates() {
		FetchTemplates fetchTemplates = new FetchTemplates();
		fetchTemplates.setRootPath(BASE_REMOTE);
		fetchTemplates.addPath(TEMPLATE_MODULE);
		fetchTemplates.addPath(TEMPLATE_PRESENTER);
		fetchTemplates.addPath(TEMPLATE_UIHANDLERS);
		fetchTemplates.addPath(TEMPLATE_VIEW);
		fetchTemplates.addPath(TEMPLATE_VIEWUI);
		fetchTemplates.run();
		
		fetchedTemplates = fetchTemplates.getPathsToFetch();
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
        RenderedTemplate rendered = processTemplate(TEMPLATE_MODULE);
        createdNestedPresenter.setModule(rendered);
    }

    private void processPresenter() throws ResourceNotFoundException, ParseErrorException, Exception {
        RenderedTemplate rendered = processTemplate(TEMPLATE_PRESENTER);
        createdNestedPresenter.setPresenter(rendered);
    }

    private void processUiHandlers() throws ResourceNotFoundException, ParseErrorException, Exception {
        RenderedTemplate rendered = processTemplate(TEMPLATE_UIHANDLERS);
        createdNestedPresenter.setUihandlers(rendered);
    }

    private void processView() throws ResourceNotFoundException, ParseErrorException, Exception {
        RenderedTemplate rendered = processTemplate(TEMPLATE_VIEW);
        createdNestedPresenter.setView(rendered);
    }

    private void processViewBinder() throws ResourceNotFoundException, ParseErrorException, Exception {
        RenderedTemplate rendered = processTemplate(TEMPLATE_VIEWUI);
        createdNestedPresenter.setViewui(rendered);
    }

    private RenderedTemplate processTemplate(String fileName) throws ResourceNotFoundException, ParseErrorException, Exception {
        FetchTemplate template = fetchedTemplates.get(fileName);
        
        VelocityContext context = getBaseVelocityContext();
        
		Template veloTemp = new Template();
		veloTemp.setName(template.getFileName());
		veloTemp.setData(template.getFetched());
		veloTemp.setEncoding("UTF-8");
		
        StringWriter writer = new StringWriter();
        veloTemp.merge(context, writer);
        RenderedTemplate rendered = new RenderedTemplate(renderFileName(fileName), writer.toString());
        return rendered;
    }

    private String renderFileName(String fileName) {
        String name = presenterOptions.getName();
        name = name.replace(".vm", "");
        return fileName.replace("__name__", name);
    }
}
