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

package com.arcbees.plugin.template.domain.place;

import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.arcbees.plugin.template.create.place.CreatedNameTokens;
import com.arcbees.plugin.template.create.place.NameToken;
import com.arcbees.plugin.template.create.place.NameTokenOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;

public class CreateNameTokens {
    public static CreatedNameTokens run(NameTokenOptions nameTokenOptions) {
        CreateNameTokens created = new CreateNameTokens(nameTokenOptions);
        created.run();
        return created.getCreatedNameTokens();
    }

    private final static String BASE = "./src/main/resources/com/arcbees/plugin/template/place/";
    
    private VelocityEngine velocityEngine;
    private NameTokenOptions nameTokenOptions;
    private CreatedNameTokens createdNameTokens;
     
    private CreateNameTokens(NameTokenOptions nameTokenOptions) {
        this.nameTokenOptions = nameTokenOptions;
    }
    
    private CreatedNameTokens getCreatedNameTokens() {
        return createdNameTokens;
    }
    
    private void run() {
        createdNameTokens = new CreatedNameTokens();
        
        setupVelocity();
        processNameTokensFile();
        processNameTokens();
    }

    private void setupVelocity() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, BASE);
        velocityEngine.init();
    }
    
    private VelocityContext getBaseVelocityContext() {
        VelocityContext context = new VelocityContext();
        
        return context;
    }
    
    private void processNameTokensFile() {
        String fileName = "NameTokens.java.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        createdNameTokens.setNameTokens(new RenderedTemplate(renderFileName(fileName), writer.toString()));
    }
    
    private void processNameTokens() {
        List<NameToken> tokens = nameTokenOptions.getNameTokens();
        if (tokens == null) {
            return;
        }
        
        for (NameToken token : tokens) {
            processNameToken(token);
        }
    }
    
    private void processNameToken(NameToken token) {
        String field = processNameTokenFieldTemplate(token);
        String method = processNameTokenMethodTemplate(token);
       
        createdNameTokens.addField(field);
        createdNameTokens.addMethod(method);
    }

    private String processNameTokenFieldTemplate(NameToken token) {
        String fileName = "NameTokenField.java.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();
        context.put("crawlable", token.getCrawlable());
        context.put("name", token.getToken());

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        return writer.toString();
    }
    
    private String processNameTokenMethodTemplate(NameToken token) {
        String fileName = "NameTokenMethod.java.vm";
        Template template = velocityEngine.getTemplate(fileName);

        VelocityContext context = getBaseVelocityContext();
        context.put("crawlable", token.getCrawlable());
        context.put("name", token.getToken());

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        
        return writer.toString();
    }
    
    private String renderFileName(String fileName) {
        fileName = fileName.replace(".vm", "");
        return fileName;
    }
}
