package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
   
        // Create the relation between the user and the room
        RoomUser roomUser1 = new RoomUser();
        roomUser1.setAdmin(false);
        roomUser1.setRoom(room);
        roomUser1.setUser(player1);

        this.entityManager.persist(room);
        this.entityManager.persist(player1);
        this.entityManager.persist(roomUser1);
        
        this.entityManager.flush();
        
        // Fetch the list of users inside the room created earlier
        List<RoomUser> roomUsers = entityManager
        .createNamedQuery("RoomUser.getRoom", RoomUser.class)
        .setParameter("roomId", room.getId())
        .getResultList();

        // Assert that our User exists in said list fetched previously
        assertTrue(roomUsers.contains(roomUser1));

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
   
        // Create the relation between the user and the room1
        RoomUser roomUser1 = new RoomUser();
        roomUser1.setAdmin(false);
        roomUser1.setRoom(room1);
        roomUser1.setUser(player);

        // Create the relation between the user and the room2
        RoomUser roomUser2 = new RoomUser();
        roomUser2.setAdmin(false);
        roomUser2.setRoom(room2);
        roomUser2.setUser(player);

        this.entityManager.persist(player);

        this.entityManager.persist(room1);
        this.entityManager.persist(roomUser1);

        this.entityManager.persist(room2);
        this.entityManager.persist(roomUser2);
        
        this.entityManager.flush();
        
        // Fetch the RoomUser where the userId is that of our user and roomId is that of room1
        RoomUser userRooms1 = entityManager
        .createNamedQuery("RoomUser.getRoomUser", RoomUser.class)
        .setParameter("userId", player.getId())
        .setParameter("roomId", room1.getId()).getSingleResult();

        // Fetch the RoomUser where the userId is that of our user and roomId is that of room2
        RoomUser userRooms2 = entityManager
        .createNamedQuery("RoomUser.getRoomUser", RoomUser.class)
        .setParameter("userId", player.getId())
        .setParameter("roomId", room2.getId()).getSingleResult();

        // Make sure that our user is in fact the same User fetched from both queries done previously
        assertEquals(player, userRooms1.getUser());
        assertEquals(player, userRooms2.getUser());
	}

	

}
