package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.Platform;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PlatformMapper {

    public Set<String> map(Set<Platform> value) {
        Set<String> result = new HashSet<>();
        result.addAll(value.stream().map(platform -> platform.getDescription()).toList());
        return result;
    }

    public Set<Platform> reverseMap(Set<String> value) {
        Set<Platform> result = new HashSet<>();
        for (String description : value) {
            Platform platform = new Platform();
            platform.setDescription(description);
            result.add(platform);
        }
        return result;
    }
}
