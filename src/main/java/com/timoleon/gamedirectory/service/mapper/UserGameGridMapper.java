package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.UserGame;
import com.timoleon.gamedirectory.service.dto.GameGridDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { PlatformMapper.class, CategoryMapper.class, DeveloperMapper.class, PublisherMapper.class })
public abstract class UserGameGridMapper extends EntityMapper<GameGridDTO, UserGame> {

    @Mapping(source = "game.gameDetails", target = "gameDetails")
    @Mapping(source = "game.id", target = "id")
    public abstract GameGridDTO toDto(UserGame userGame);

    public abstract UserGame toEntity(GameGridDTO gameDTO);

    public UserGame fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserGame userGame = new UserGame();
        userGame.setId(id);
        return userGame;
    }
}
