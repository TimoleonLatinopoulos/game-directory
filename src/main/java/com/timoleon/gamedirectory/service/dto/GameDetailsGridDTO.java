package com.timoleon.gamedirectory.service.dto;

import java.util.HashSet;
import java.util.Set;

public class GameDetailsGridDTO {

    private String thumbnailUrl;

    private String snippet;

    private Set<String> platforms = new HashSet<>();

    private Set<String> developers = new HashSet<>();

    private Set<String> publishers = new HashSet<>();

    private Set<String> categories = new HashSet<>();

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Set<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<String> platforms) {
        this.platforms = platforms;
    }

    public Set<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<String> developers) {
        this.developers = developers;
    }

    public Set<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<String> publishers) {
        this.publishers = publishers;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
