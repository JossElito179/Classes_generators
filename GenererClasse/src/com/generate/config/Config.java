package com.generate.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Config {
    @JsonProperty("nom")
    private String nom;

    @JsonProperty("extension")
    private String extension;

    @JsonProperty("package")
    private String packageName;

    @JsonProperty("import")
    private String importStatement;

    @JsonProperty("class_declaration")
    private String classDeclaration;

    @JsonProperty("attributes")
    private String attributes;

    @JsonProperty("getters_setters")
    private String gettersSetters;

    @JsonProperty("constructors")
    private String constructors;

    @JsonProperty("attributes_type")
    private List<String> attributesTypes;

    @JsonProperty("import_statement")
    private List<String> importState;

    @JsonProperty("end_of_class")
    private String endOfClass;

    public Config() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getImportStatement() {
        return importStatement;
    }

    public void setImportStatement(String importStatement) {
        this.importStatement = importStatement;
    }

    public String getClassDeclaration() {
        return classDeclaration;
    }

    public void setClassDeclaration(String classDeclaration) {
        this.classDeclaration = classDeclaration;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getGettersSetters() {
        return gettersSetters;
    }

    public void setGettersSetters(String gettersSetters) {
        this.gettersSetters = gettersSetters;
    }

    public String getConstructors() {
        return constructors;
    }

    public void setConstructors(String constructors) {
        this.constructors = constructors;
    }

    public List<String> getAttributesTypes() {
        return attributesTypes;
    }

    public void setAttributesTypes(List<String> attributesTypes) {
        this.attributesTypes = attributesTypes;
    }

    public List<String> getImportState() {
        return importState;
    }

    public void setImportState(List<String> importState) {
        this.importState = importState;
    }

    public String getEndOfClass() {
        return endOfClass;
    }

    public void setEndOfClass(String endOfClass) {
        this.endOfClass = endOfClass;
    }
}
