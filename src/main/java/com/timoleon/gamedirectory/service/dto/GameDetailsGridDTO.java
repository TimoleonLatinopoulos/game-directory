package com.timoleon.gamedirectory.service.dto;

import com.timoleon.gamedirectory.domain.Category;
import com.timoleon.gamedirectory.domain.Developer;
import com.timoleon.gamedirectory.domain.Platform;
import com.timoleon.gamedirectory.domain.Publisher;
import java.util.HashSet;
import java.util.Set;

public class GameDetailsGridDTO {

    private String thumbnailUrl;

    private String snippet;

    private Set<Platform> platforms = new HashSet<>();

    private Set<Developer> developers = new HashSet<>();

    private Set<Publisher> publishers = new HashSet<>();

    private Set<Category> categories = new HashSet<>();

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

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }

    public Set<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
