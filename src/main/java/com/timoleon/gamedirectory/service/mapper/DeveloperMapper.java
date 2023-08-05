package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.Developer;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DeveloperMapper {

    public Set<String> map(Set<Developer> value) {
        Set<String> result = new HashSet<>();
        result.addAll(value.stream().map(developer -> developer.getDescription()).toList());
        return result;
    }

    public Set<Developer> reverseMap(Set<String> value) {
        Set<Developer> result = new HashSet<>();
        for (String description : value) {
            Developer developer = new Developer();
            developer.setDescription(description);
            result.add(developer);
        }
        return result;
    }
}
