package com.arcbees.plugin.template.create.place;

import java.util.ArrayList;
import java.util.List;

import com.arcbees.plugin.template.domain.presenter.RenderedTemplate;

public class CreatedNameTokens {
    private RenderedTemplate nameTokens;
    private List<String> fields;
    private List<String> methods;

    public CreatedNameTokens() {
    }

    public RenderedTemplate getNameTokens() {
        return nameTokens;
    }

    public void setNameTokens(RenderedTemplate nameTokens) {
        this.nameTokens = nameTokens;
    }
    
    public void addMethod(String method) {
        if (methods == null) {
            methods = new ArrayList<String>();
        }
        methods.add(method);
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
    
    public void addField(String field) {
        if (fields == null) {
            fields = new ArrayList<String>();
        }
        fields.add(field);
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
