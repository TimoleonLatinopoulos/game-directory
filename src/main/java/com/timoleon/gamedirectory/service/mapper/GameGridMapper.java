package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.Game;
import com.timoleon.gamedirectory.service.dto.GameGridDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class GameGridMapper extends EntityMapper<GameGridDTO, Game> {

    public abstract GameGridDTO toDto(Game game);

    public abstract Game toEntity(GameGridDTO gameDTO);

    public Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
