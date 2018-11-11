package com.epam.computershop.util;

import com.epam.computershop.entity.Category;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CategoryUtil {
    private CategoryUtil() {
    }

    public static void fillCategoriesChildren(Map<Locale, CopyOnWriteArrayList<Category>> localesCategories){
        for (CopyOnWriteArrayList<Category> localeCategories:localesCategories.values()){
            localeCategories.forEach(category -> {
                if(category.getParentId()!=null){
                    localeCategories.stream()
                            .filter(parentCategory -> category.getParentId().equals(parentCategory.getId()))
                            .findFirst().ifPresent(parentCategory -> parentCategory.getChildren().add(category));
                }
            });
        }
    }

    public static void refreshLocalesCategories(Map<Locale, CopyOnWriteArrayList<Category>> oldLocalesCategories,
                                                Map<Locale, CopyOnWriteArrayList<Category>> newLocalesCategories){
        for (Map.Entry localeCategories: oldLocalesCategories.entrySet()){
            CopyOnWriteArrayList<Category> categories =(CopyOnWriteArrayList<Category>) localeCategories.getValue();
            categories.clear();
            categories.addAll(newLocalesCategories.get(localeCategories.getKey()));
        }
    }
}
