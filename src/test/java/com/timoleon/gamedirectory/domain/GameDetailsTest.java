package com.timoleon.gamedirectory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.timoleon.gamedirectory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameDetails.class);
        GameDetails gameDetails1 = new GameDetails();
        gameDetails1.setId(1L);
        GameDetails gameDetails2 = new GameDetails();
        gameDetails2.setId(gameDetails1.getId());
        assertThat(gameDetails1).isEqualTo(gameDetails2);
        gameDetails2.setId(2L);
        assertThat(gameDetails1).isNotEqualTo(gameDetails2);
        gameDetails1.setId(null);
        assertThat(gameDetails1).isNotEqualTo(gameDetails2);
    }
}
