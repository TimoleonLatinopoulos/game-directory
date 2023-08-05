package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.Category;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    public Set<String> map(Set<Category> value) {
        Set<String> result = new HashSet<>();
        result.addAll(value.stream().map(category -> category.getDescription()).toList());
        return result;
    }

    public Set<Category> reverseMap(Set<String> value) {
        Set<Category> result = new HashSet<>();
        for (String description : value) {
            Category category = new Category();
            category.setDescription(description);
            result.add(category);
        }
        return result;
    }
}
