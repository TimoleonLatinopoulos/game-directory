package com.timoleon.gamedirectory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A GameDetails.
 */
@Entity
@Table(name = "game_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "required_age")
    private Integer requiredAge;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "pegi_rating")
    private String pegiRating;

    @Column(name = "metacritic_score")
    private Integer metacriticScore;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "price")
    private Float price;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    @Column(name = "steam_appid")
    private Long steamAppid;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_game_details__platform",
        joinColumns = @JoinColumn(name = "game_details_id"),
        inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    @JsonIgnoreProperties(value = { "gameDetails" }, allowSetters = true)
    private Set<Platform> platforms = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_game_details__developers",
        joinColumns = @JoinColumn(name = "game_details_id"),
        inverseJoinColumns = @JoinColumn(name = "developers_id")
    )
    @JsonIgnoreProperties(value = { "gameDetails" }, allowSetters = true)
    private Set<Developer> developers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_game_details__publishers",
        joinColumns = @JoinColumn(name = "game_details_id"),
        inverseJoinColumns = @JoinColumn(name = "publishers_id")
    )
    @JsonIgnoreProperties(value = { "gameDetails" }, allowSetters = true)
    private Set<Publisher> publishers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_game_details__categories",
        joinColumns = @JoinColumn(name = "game_details_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    @JsonIgnoreProperties(value = { "gameDetails" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRequiredAge() {
        return this.requiredAge;
    }

    public GameDetails requiredAge(Integer requiredAge) {
        this.setRequiredAge(requiredAge);
        return this;
    }

    public void setRequiredAge(Integer requiredAge) {
        this.requiredAge = requiredAge;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public GameDetails releaseDate(LocalDate releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPegiRating() {
        return this.pegiRating;
    }

    public GameDetails pegiRating(String pegiRating) {
        this.setPegiRating(pegiRating);
        return this;
    }

    public void setPegiRating(String pegiRating) {
        this.pegiRating = pegiRating;
    }

    public Integer getMetacriticScore() {
        return this.metacriticScore;
    }

    public GameDetails metacriticScore(Integer metacriticScore) {
        this.setMetacriticScore(metacriticScore);
        return this;
    }

    public void setMetacriticScore(Integer metacriticScore) {
        this.metacriticScore = metacriticScore;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public GameDetails imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public GameDetails thumbnailUrl(String thumbnailUrl) {
        this.setThumbnailUrl(thumbnailUrl);
        return this;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Float getPrice() {
        return this.price;
    }

    public GameDetails price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public GameDetails description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return this.notes;
    }

    public GameDetails notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getSteamAppid() {
        return this.steamAppid;
    }

    public GameDetails steamAppid(Long steamAppid) {
        this.setSteamAppid(steamAppid);
        return this;
    }

    public void setSteamAppid(Long steamAppid) {
        this.steamAppid = steamAppid;
    }

    public Set<Platform> getPlatforms() {
        return this.platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public GameDetails platforms(Set<Platform> platforms) {
        this.setPlatforms(platforms);
        return this;
    }

    public GameDetails addPlatform(Platform platform) {
        this.platforms.add(platform);
        platform.getGameDetails().add(this);
        return this;
    }

    public GameDetails removePlatform(Platform platform) {
        this.platforms.remove(platform);
        platform.getGameDetails().remove(this);
        return this;
    }

    public Set<Developer> getDevelopers() {
        return this.developers;
    }

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }

    public GameDetails developers(Set<Developer> developers) {
        this.setDevelopers(developers);
        return this;
    }

    public GameDetails addDevelopers(Developer developer) {
        this.developers.add(developer);
        developer.getGameDetails().add(this);
        return this;
    }

    public GameDetails removeDevelopers(Developer developer) {
        this.developers.remove(developer);
        developer.getGameDetails().remove(this);
        return this;
    }

    public Set<Publisher> getPublishers() {
        return this.publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers;
    }

    public GameDetails publishers(Set<Publisher> publishers) {
        this.setPublishers(publishers);
        return this;
    }

    public GameDetails addPublishers(Publisher publisher) {
        this.publishers.add(publisher);
        publisher.getGameDetails().add(this);
        return this;
    }

    public GameDetails removePublishers(Publisher publisher) {
        this.publishers.remove(publisher);
        publisher.getGameDetails().remove(this);
        return this;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public GameDetails categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public GameDetails addCategories(Category category) {
        this.categories.add(category);
        category.getGameDetails().add(this);
        return this;
    }

    public GameDetails removeCategories(Category category) {
        this.categories.remove(category);
        category.getGameDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameDetails)) {
            return false;
        }
        return id != null && id.equals(((GameDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameDetails{" +
            "id=" + getId() +
            ", requiredAge=" + getRequiredAge() +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", pegiRating='" + getPegiRating() + "'" +
            ", metacriticScore=" + getMetacriticScore() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", thumbnailUrl='" + getThumbnailUrl() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", steamAppid=" + getSteamAppid() +
            "}";
    }
}
