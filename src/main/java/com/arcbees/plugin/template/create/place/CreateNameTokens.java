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

package com.arcbees.plugin.template.create.place;

import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.arcbees.plugin.template.domain.place.CreatedNameTokens;
import com.arcbees.plugin.template.domain.place.NameToken;
import com.arcbees.plugin.template.domain.place.NameTokenOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;
import com.arcbees.plugin.template.utils.VelocityUtils;

public class CreateNameTokens {
    public final static Logger logger = Logger.getLogger(CreateNameTokens.class.getName());

    public static CreatedNameTokens run(NameTokenOptions nameTokenOptions, boolean remote, boolean processFileOnly) throws Exception {
        CreateNameTokens created = new CreateNameTokens(nameTokenOptions, remote);
        created.run(processFileOnly);
        return created.getCreatedNameTokens();
    }

    private static final String BASE_REMOTE = "https://raw.githubusercontent.com/ArcBees/IDE-Templates/1.2.0/src/main/resources/com/arcbees/plugin/template/place/";
    private final static String BASE_LOCAL = "./src/main/resources/com/arcbees/plugin/template/place/";

    private VelocityEngine velocityEngine;
    private NameTokenOptions nameTokenOptions;
    private CreatedNameTokens createdNameTokens;
    private boolean remote;

    private CreateNameTokens(NameTokenOptions nameTokenOptions, boolean remote) {
        this.nameTokenOptions = nameTokenOptions;
        this.remote = remote;
    }

    private CreatedNameTokens getCreatedNameTokens() {
        return createdNameTokens;
    }

    private void run(boolean processFileOnly) throws Exception {
        createdNameTokens = new CreatedNameTokens();

        if (remote) {
            setupVelocityRemote();
        } else {
            setupVelocityLocal();
        }

        if (processFileOnly) {
            processNameTokensFile();
        } else {
            processNameTokensFile();
            processNameTokens();
        }
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

    private VelocityContext getBaseVelocityContext() {
        VelocityContext context = new VelocityContext();
        context.put("package", nameTokenOptions.getPackageName());
        context.put("methodName", nameTokenOptions.getMethodName(0));
        return context;
    }

    private void processNameTokensFile() throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "NameTokens.java.vm";
        Template template = velocityEngine.getTemplate(fileName);
        VelocityContext context = getBaseVelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        RenderedTemplate rendered = new RenderedTemplate(renderFileName(fileName), writer.toString());
        createdNameTokens.setNameTokensFile(rendered);
    }

    private void processNameTokens() throws ResourceNotFoundException, ParseErrorException, Exception {
        List<NameToken> tokens = nameTokenOptions.getNameTokens();
        if (tokens == null) {
            return;
        }

        for (NameToken token : tokens) {
            processNameToken(token);
        }
    }

    private void processNameToken(NameToken token) throws ResourceNotFoundException, ParseErrorException, Exception {
        String field = processNameTokenFieldTemplate(token);
        String method = processNameTokenMethodTemplate(token);

        createdNameTokens.addField(field);
        createdNameTokens.addMethod(method);
    }

    private String processNameTokenFieldTemplate(NameToken token) throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "NameTokenField.vm";
        RenderedTemplate rendered = processTemplate(fileName, token);
        return rendered.getContents();
    }

    private String processNameTokenMethodTemplate(NameToken token) throws ResourceNotFoundException, ParseErrorException, Exception {
        String fileName = "NameTokenMethod.vm";
        RenderedTemplate rendered = processTemplate(fileName, token);
        return rendered.getContents();
    }

    private RenderedTemplate processTemplate(String fileName, NameToken token) throws ResourceNotFoundException, ParseErrorException, Exception {
        Template template = velocityEngine.getTemplate(fileName);
        VelocityContext context = getBaseVelocityContext();
        context.put("crawlable", token.getCrawlable());
        context.put("name", token.getToken());

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        RenderedTemplate rendered = new RenderedTemplate(renderFileName(fileName), writer.toString());
        return rendered;
    }

    private String renderFileName(String fileName) {
        fileName = fileName.replace(".vm", "");
        return fileName;
    }
}
