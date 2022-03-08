package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

		assertEquals(5, user.getId());
	}

}
