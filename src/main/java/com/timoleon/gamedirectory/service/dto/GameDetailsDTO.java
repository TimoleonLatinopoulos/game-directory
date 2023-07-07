package com.timoleon.gamedirectory.service.dto;

import com.timoleon.gamedirectory.domain.Category;
import com.timoleon.gamedirectory.domain.Developer;
import com.timoleon.gamedirectory.domain.Platform;
import com.timoleon.gamedirectory.domain.Publisher;
import com.timoleon.gamedirectory.domain.enumerations.PegiRating;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class GameDetailsDTO {

    private Long id;

    private LocalDate releaseDate;

    private PegiRating pegiRating;

    private Integer metacriticScore;

    private String imageUrl;

    private String thumbnailUrl;

    private Float price;

    private String description;

    private String snippet;

    private String notes;

    private Long steamAppid;

    private String steamUrl;

    private Set<Platform> platforms = new HashSet<>();

    private Set<Developer> developers = new HashSet<>();

    private Set<Publisher> publishers = new HashSet<>();

    private Set<Category> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public PegiRating getPegiRating() {
        return pegiRating;
    }

    public void setPegiRating(PegiRating pegiRating) {
        this.pegiRating = pegiRating;
    }

    public Integer getMetacriticScore() {
        return metacriticScore;
    }

    public void setMetacriticScore(Integer metacriticScore) {
        this.metacriticScore = metacriticScore;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getSteamAppid() {
        return steamAppid;
    }

    public void setSteamAppid(Long steamAppid) {
        this.steamAppid = steamAppid;
    }

    public String getSteamUrl() {
        return steamUrl;
    }

    public void setSteamUrl(String steamUrl) {
        this.steamUrl = steamUrl;
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
