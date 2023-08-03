package com.timoleon.gamedirectory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A UserGame.
 */
@Entity
@Table(name = "user_game")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserGame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "favourite")
    private Boolean favourite;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "gameDetails" }, allowSetters = true)
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserGame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFavourite() {
        return this.favourite;
    }

    public UserGame favourite(Boolean favourite) {
        this.setFavourite(favourite);
        return this;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public UserGame dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTitle() {
        return this.title;
    }

    public UserGame title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserGame user(User user) {
        this.setUser(user);
        return this;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public UserGame game(Game game) {
        this.setGame(game);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGame)) {
            return false;
        }
        return id != null && id.equals(((UserGame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserGame{" +
            "id=" + getId() +
            ", favourite='" + getFavourite() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
