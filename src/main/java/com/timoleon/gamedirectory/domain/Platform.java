package com.timoleon.gamedirectory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Platform.
 */
@Entity
@Table(name = "platform")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Platform implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "platforms")
    @JsonIgnoreProperties(value = { "platforms", "developers", "publishers", "categories" }, allowSetters = true)
    private Set<GameDetails> gameDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Platform id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Platform description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GameDetails> getGameDetails() {
        return this.gameDetails;
    }

    public void setGameDetails(Set<GameDetails> gameDetails) {
        if (this.gameDetails != null) {
            this.gameDetails.forEach(i -> i.removePlatform(this));
        }
        if (gameDetails != null) {
            gameDetails.forEach(i -> i.addPlatform(this));
        }
        this.gameDetails = gameDetails;
    }

    public Platform gameDetails(Set<GameDetails> gameDetails) {
        this.setGameDetails(gameDetails);
        return this;
    }

    public Platform addGameDetails(GameDetails gameDetails) {
        this.gameDetails.add(gameDetails);
        gameDetails.getPlatforms().add(this);
        return this;
    }

    public Platform removeGameDetails(GameDetails gameDetails) {
        this.gameDetails.remove(gameDetails);
        gameDetails.getPlatforms().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Platform)) {
            return false;
        }
        return id != null && id.equals(((Platform) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Platform{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
