package com.epam.computershop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Entity, Serializable {
    private short id;
    private String name;
    private Short parentId;
    private String langCode;
    private List<Category> children;

    public Category() {
        children = new ArrayList<>();
    }

    public Category(Category category) {
        id = category.id;
        name = category.name;
        parentId = category.parentId;
        langCode = category.langCode;
        children = category.children;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getParentId() {
        return parentId;
    }

    public void setParentId(Short parentId) {
        this.parentId = parentId;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

}