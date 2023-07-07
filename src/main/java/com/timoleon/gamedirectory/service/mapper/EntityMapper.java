package com.timoleon.gamedirectory.service.mapper;

import java.util.List;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public abstract class EntityMapper<D, E> {

    public abstract E toEntity(D dto);

    public abstract D toDto(E entity);

    public abstract List<E> toEntity(List<D> dtoList);

    public abstract List<D> toDto(List<E> entityList);
}
