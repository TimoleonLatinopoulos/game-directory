package com.timoleon.gamedirectory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.timoleon.gamedirectory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserGameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserGame.class);
        UserGame userGame1 = new UserGame();
        userGame1.setId(1L);
        UserGame userGame2 = new UserGame();
        userGame2.setId(userGame1.getId());
        assertThat(userGame1).isEqualTo(userGame2);
        userGame2.setId(2L);
        assertThat(userGame1).isNotEqualTo(userGame2);
        userGame1.setId(null);
        assertThat(userGame1).isNotEqualTo(userGame2);
    }
}
