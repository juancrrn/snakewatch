package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

		assertEquals(5, userInBD.getId());
	}

	/*@Test
	@Transactional
	void newFriendship() {
		ist<User> amigos = new ArrayList<User>();

        

        User adminUser = entityManager.createNamedQuery("User.byUsername", User.class).setParameter("username", "a").getSingleResult();
        List<User> users = entityManager.createNamedQuery("User.getUsersLessMe", User.class).setParameter("username", adminUser.getUsername()).getResultList();

        for(int i=0; i<users.size();i++){
            FriendshipKey fkey = new FriendshipKey(adminUser, users.get(i));
            Friendship friendship = new Friendship(fkey);
            if(entityManager.find(Friendship.class, friendship.getId()) == null) {
                entityManager.persist(friendship);
                entityManager.flush();
            }
        }

        List<Friendship> userFriendships = entityManager
                    .createNamedQuery("Friendship.getFriends", Friendship.class)
                    .setParameter("userid", adminUser.getId())
                    .getResultList();
                    
        for (Friendship friendship : userFriendships){
            if(friendship.getId().getUser1().getId() == adminUser.getId()){
                amigos.add(friendship.getId().getUser2());
            }
            else{
                amigos.add(friendship.getId().getUser1());
            }
        }
        
        model.addAttribute("amigos", amigos);
	}*/

}
