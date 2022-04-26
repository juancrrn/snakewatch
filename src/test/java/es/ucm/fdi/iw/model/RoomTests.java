package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.*;

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

        // Create a new Room with max capacity = 10 and type = PUBLIC
        Room room = new Room();
        room.setMaxUsers(10);
        room.setVisibility(Room.RoomType.PUBLIC);

        // Create a new User with userName = testPlayer and password = testpassword0
        User player1 = new User();
        player1.setEnabled(true);
        player1.setUsername("testPlayer");
        player1.setPassword("testpassword0");
   
        room.getUsers().add(player1);
        room.setOwner(player1);

        this.entityManager.persist(room);
        this.entityManager.persist(player1);
        
        this.entityManager.flush();

        // Assert that our User has been properly inserted in the room database
        Room roomReloaded = entityManager.find(Room.class, room.getId());
        assertTrue(roomReloaded.getUsers().contains(player1));
	}

    @Test
	@Transactional
	void userJoinsMultiRooms() {

        // Create a new Room with max capacity = 5 and type = PUBLIC
        Room room1 = new Room();
        room1.setMaxUsers(5);
        room1.setVisibility(Room.RoomType.PUBLIC);

        // Create another Room with max capacity = 3 and type = PUBLIC
        Room room2 = new Room();
        room2.setMaxUsers(3);
        room2.setVisibility(Room.RoomType.PUBLIC);

        // Create a new User with userName = testPlayertest and password = testpassword000
        User player = new User();
        player.setEnabled(true);
        player.setUsername("testPlayertest");
        player.setPassword("testpassword000");
   
        room1.getUsers().add(player);
        room1.setOwner(player);

        room2.getUsers().add(player);
        room2.setOwner(player);


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
