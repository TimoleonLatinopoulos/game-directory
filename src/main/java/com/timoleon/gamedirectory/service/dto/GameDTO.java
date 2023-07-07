package com.timoleon.gamedirectory.service.dto;

public class GameDTO {

    private Long id;

    private String title;

    private GameDetailsDTO gameDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GameDetailsDTO getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(GameDetailsDTO gameDetails) {
        this.gameDetails = gameDetails;
    }
}
