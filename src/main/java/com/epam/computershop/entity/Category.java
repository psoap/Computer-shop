package com.epam.computershop.entity;

import java.util.ArrayList;
import java.util.List;

public class Category implements Entity, Cloneable {
    private short id;
    private String name;
    private Short parentId;
    private String langCode;
    private List<Category> children = new ArrayList<>();

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

    @Override
    public Category clone() {
        Category category = new Category();
        category.setName(name);
        category.setLangCode(langCode);
        category.setId(id);
        category.setParentId(parentId);
        return category;
    }

}