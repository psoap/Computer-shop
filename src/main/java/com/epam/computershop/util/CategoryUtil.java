package com.epam.computershop.util;

import com.epam.computershop.entity.Category;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CategoryUtil {
    public static void fillChildrenCategories(Map<Locale, CopyOnWriteArrayList<Category>> langsCategories){
        for (CopyOnWriteArrayList<Category> lcats:langsCategories.values()){
            lcats.forEach(category -> {
                if(category.getParentId()!=null){
                    lcats.stream()
                            .filter(parentCategory -> parentCategory.getId()==category.getParentId())
                            .findFirst().ifPresent(cat -> cat.getChildren().add(category));
                }
            });
        }
    }

    public static void refreshLangsCategories(Map<Locale, CopyOnWriteArrayList<Category>> oldLangsCategories, Map<Locale, CopyOnWriteArrayList<Category>> newLangsCategories){
        for (Map.Entry langCategories: oldLangsCategories.entrySet()){
            CopyOnWriteArrayList<Category> categories =(CopyOnWriteArrayList<Category>) langCategories.getValue();
            categories.clear();
            categories.addAll(newLangsCategories.get(langCategories.getKey()));
        }
    }

    private CategoryUtil() {
    }
}
