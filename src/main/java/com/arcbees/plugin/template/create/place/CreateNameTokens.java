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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;

import com.arcbees.plugin.template.domain.place.CreatedNameTokens;
import com.arcbees.plugin.template.domain.place.NameToken;
import com.arcbees.plugin.template.domain.place.NameTokenOptions;
import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;

public class CreateNameTokens {
    public static CreatedNameTokens run(NameTokenOptions nameTokenOptions, boolean remote, boolean processFileOnly) {
        CreateNameTokens created = new CreateNameTokens(nameTokenOptions, remote);
        created.run(processFileOnly);
        return created.getCreatedNameTokens();
    }

    private static final String BASE_REMOTE = "https://raw.github.com/ArcBees/IDE-Templates/1.0.0/src/main/resources/com/arcbees/plugin/template/place/";
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
    
    private void run(boolean processFileOnly) {
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
        velocityEngine.init();
    }

    private void setupVelocityRemote() {
        URLResourceLoader loader = new URLResourceLoader();
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "url");
        velocityEngine.setProperty("url.resource.loader.instance", loader);
        velocityEngine.setProperty("url.resource.loader.timeout", new Integer(5000));
        velocityEngine.setProperty("url.resource.loader.root", BASE_REMOTE);
        velocityEngine.init();
    }
    
    private VelocityContext getBaseVelocityContext() {
        VelocityContext context = new VelocityContext();
        context.put("package", nameTokenOptions.getPackageName());
        context.put("methodName", nameTokenOptions.getMethodName(0));
        return context;
    }
    
    private void processNameTokensFile() {
        String fileName = "NameTokens.java.vm";
        Template template = velocityEngine.getTemplate(fileName);
        VelocityContext context = getBaseVelocityContext();
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        RenderedTemplate rendered = new RenderedTemplate(renderFileName(fileName), writer.toString());
        createdNameTokens.setNameTokensFile(rendered);
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
        String fileName = "NameTokenField.vm";
        RenderedTemplate rendered = processTemplate(fileName, token);
        return rendered.getContents();
    }
    
    private String processNameTokenMethodTemplate(NameToken token) {
        String fileName = "NameTokenMethod.vm";
        RenderedTemplate rendered = processTemplate(fileName, token);
        return rendered.getContents();
    }
    
    private RenderedTemplate processTemplate(String fileName, NameToken token) {
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
