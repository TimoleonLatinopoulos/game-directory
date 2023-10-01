package com.timoleon.gamedirectory.service.dto.steam;

public class MetacriticScoreDTO {

    private String score;
    private String url;

    public MetacriticScoreDTO() {}

    public MetacriticScoreDTO(String score, String url) {
        this.score = score;
        this.url = url;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
