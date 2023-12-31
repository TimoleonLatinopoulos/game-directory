package com.timoleon.gamedirectory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @JsonIgnoreProperties(value = { "platforms", "developers", "publishers", "categories", "game" }, allowSetters = true)
    private Set<GameDetails> gameDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
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
            this.gameDetails.forEach(i -> i.removeCategories(this));
        }
        if (gameDetails != null) {
            gameDetails.forEach(i -> i.addCategories(this));
        }
        this.gameDetails = gameDetails;
    }

    public Category gameDetails(Set<GameDetails> gameDetails) {
        this.setGameDetails(gameDetails);
        return this;
    }

    public Category addGameDetails(GameDetails gameDetails) {
        this.gameDetails.add(gameDetails);
        gameDetails.getCategories().add(this);
        return this;
    }

    public Category removeGameDetails(GameDetails gameDetails) {
        this.gameDetails.remove(gameDetails);
        gameDetails.getCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +
                ", description='" + getDescription() + "'" +
                "}";
    }
}
