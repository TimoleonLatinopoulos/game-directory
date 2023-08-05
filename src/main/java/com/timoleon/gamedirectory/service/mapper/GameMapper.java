package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.service.dto.GameDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class GameMapper extends EntityMapper<GameDTO, Game> {

    public abstract GameDTO toDto(Game game);

    public abstract Game toEntity(GameDTO gameDTO);

    public Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
