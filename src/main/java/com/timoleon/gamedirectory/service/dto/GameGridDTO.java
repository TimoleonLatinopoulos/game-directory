package com.timoleon.gamedirectory.service.dto;

public class GameGridDTO {

    private Long id;
    private String title;
    private GameDetailsGridDTO gameDetails;
    private Boolean favourite;

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

    public GameDetailsGridDTO getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(GameDetailsGridDTO gameDetails) {
        this.gameDetails = gameDetails;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }
}
