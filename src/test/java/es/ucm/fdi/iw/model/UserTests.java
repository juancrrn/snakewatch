package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Usage: "mvn test" on console
 */
@SpringBootTest
class UserTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(this.entityManager);
    }

    @Test
    @Transactional
    void newUser() {
        User user = new User();

        user.setEnabled(true);
        user.setUsername("testuser");
        user.setPassword("testpassword");

        this.entityManager.persist(user);
        this.entityManager.flush();

        User userInBD = entityManager.createNamedQuery("User.byUsername", User.class)
                .setParameter("username", user.getUsername()).getSingleResult();

        assertEquals(9, userInBD.getId());
    }

    @Test
    @Transactional
    void newFriendship() {

        User user1 = new User();
        user1.setEnabled(true);
        user1.setUsername("testuser1");
        user1.setPassword("testpassword");

        User user2 = new User();
        user2.setEnabled(true);
        user2.setUsername("testuser2");
        user2.setPassword("testpassword");

        this.entityManager.persist(user1);
        this.entityManager.persist(user2);
        this.entityManager.flush();

        Friendship friendship1 = new Friendship(user1, user2);
        Friendship friendship2 = new Friendship(user2, user1);
        entityManager.persist(friendship1);
        entityManager.persist(friendship2);
        entityManager.flush();

        User user1Reloaded = entityManager.find(User.class, user1.getId());
        User user2Reloaded = entityManager.find(User.class, user2.getId());
        assertEquals(1, user1Reloaded.getFriendships().size());
        assertEquals(1, user2Reloaded.getFriendships().size());
    }

}
