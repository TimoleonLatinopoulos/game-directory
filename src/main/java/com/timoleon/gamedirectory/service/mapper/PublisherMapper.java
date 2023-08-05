package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.Publisher;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PublisherMapper {

    public Set<String> map(Set<Publisher> value) {
        Set<String> result = new HashSet<>();
        result.addAll(value.stream().map(publisher -> publisher.getDescription()).toList());
        return result;
    }

    public Set<Publisher> reverseMap(Set<String> value) {
        Set<Publisher> result = new HashSet<>();
        for (String description : value) {
            Publisher publisher = new Publisher();
            publisher.setDescription(description);
            result.add(publisher);
        }
        return result;
    }
}
