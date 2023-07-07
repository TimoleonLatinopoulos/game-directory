package com.timoleon.gamedirectory.service.mapper;

import com.timoleon.gamedirectory.domain.GameDetails;
import com.timoleon.gamedirectory.service.dto.GameDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { GameMapper.class })
public abstract class GameDetailsMapper extends EntityMapper<GameDetailsDTO, GameDetails> {

    public abstract GameDetailsDTO toDto(GameDetails gameDetails);

    public abstract GameDetails toEntity(GameDetailsDTO gameDetailsDTO);

    public GameDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        GameDetails gameDetails = new GameDetails();
        gameDetails.setId(id);
        return gameDetails;
    }
}
