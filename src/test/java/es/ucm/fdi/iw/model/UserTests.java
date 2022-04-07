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

        User userInBD = entityManager.createNamedQuery("User.byUsername", User.class).setParameter("username", user.getUsername()).getSingleResult();

		assertEquals(9, userInBD.getId());
	}

	@Test
	@Transactional
	void newFriendship() {

        // 1º Get Admin and get rest the rest of users
        User adminUser = entityManager.createNamedQuery("User.byUsername", User.class).setParameter("username", "admin").getSingleResult();
        List<User> otherUsers = entityManager.createNamedQuery("User.getAllUsersExceptMe", User.class).setParameter("username", adminUser.getUsername()).getResultList();
        // 2º Make Admin be friends with every other user
        for(int i=0; i<otherUsers.size();i++){
            Friendship friendship = new Friendship(adminUser, otherUsers.get(i));
            if(entityManager.find(Friendship.class, friendship.getId()) == null) {
                entityManager.persist(friendship);
                entityManager.flush();
            }
        }
        

        // 3º Get friends of Admin and check that every other user is indeed a friend
        List<Friendship> adminFriendships = entityManager
                    .createNamedQuery("Friendship.getFriends", Friendship.class)
                    .setParameter("userId", adminUser.getId())
                    .getResultList();

        //assertEquals(otherUsers.size(), adminFriendships.size());
	}

}
