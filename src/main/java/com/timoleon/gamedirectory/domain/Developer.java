package com.timoleon.gamedirectory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Developer.
 */
@Entity
@Table(name = "developer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Developer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "developers")
    @JsonIgnoreProperties(value = { "platforms", "developers", "publishers", "categories" }, allowSetters = true)
    private Set<GameDetails> gameDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Developer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Developer description(String description) {
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
            this.gameDetails.forEach(i -> i.removeDevelopers(this));
        }
        if (gameDetails != null) {
            gameDetails.forEach(i -> i.addDevelopers(this));
        }
        this.gameDetails = gameDetails;
    }

    public Developer gameDetails(Set<GameDetails> gameDetails) {
        this.setGameDetails(gameDetails);
        return this;
    }

    public Developer addGameDetails(GameDetails gameDetails) {
        this.gameDetails.add(gameDetails);
        gameDetails.getDevelopers().add(this);
        return this;
    }

    public Developer removeGameDetails(GameDetails gameDetails) {
        this.gameDetails.remove(gameDetails);
        gameDetails.getDevelopers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Developer)) {
            return false;
        }
        return id != null && id.equals(((Developer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Developer{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
