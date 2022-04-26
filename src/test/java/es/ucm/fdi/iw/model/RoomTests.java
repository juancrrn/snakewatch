package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.iw.model.Room.RoomType;

/**
 * Usage: "mvn test" on console 
 */
@SpringBootTest
class RoomTests {

    @Autowired
    private EntityManager entityManager;

	@Test
	public void contextLoads() throws Exception {
		assertNotNull(this.entityManager);
	}

	@Test
	@Transactional
	void userJoinsRoom() {

        // Create a new User with userName = testPlayer and password = testpassword0
        User player = new User();
        player.setEnabled(true);
        player.setUsername("testPlayer");
        player.setPassword("testpassword0");
        entityManager.persist(player);
        entityManager.flush();

        // Create a new Room with max capacity = 10 and type = PUBLIC
        Room room = new Room(player, RoomType.PUBLIC, 10);        
        entityManager.persist(room);        
        entityManager.flush();

        // Assert that our User has been properly inserted in the room database
        Room roomReloaded = entityManager.find(Room.class, room.getId());
        assertTrue(roomReloaded.getUsers().contains(player));
        assertTrue(roomReloaded.getOwner().getId() == player.getId());
	}

    @Test
	@Transactional
	void userJoinsMultiRooms() {

        User player = new User();
        player.setEnabled(true);
        player.setUsername("testPlayertest");
        player.setPassword("testpassword000");

        Room room1 = new Room(player, RoomType.PUBLIC, 10);  
        Room room2 = new Room(player, RoomType.PUBLIC, 10);    

        this.entityManager.persist(player);
        this.entityManager.persist(room1);
        this.entityManager.persist(room2);
        
        this.entityManager.flush();
        
        // Assert that our User has been properly inserted in the room database
        Room room1Reloaded = entityManager.find(Room.class, room1.getId());
        Room room2Reloaded = entityManager.find(Room.class, room2.getId());
        assertTrue(room1Reloaded.getUsers().contains(player));
        assertTrue(room2Reloaded.getUsers().contains(player));
	}

	

}
