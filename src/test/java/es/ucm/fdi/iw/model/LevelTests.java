package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
class LevelTests {

    @Autowired
    private EntityManager entityManager;

	@Test
	public void contextLoads() throws Exception {
		assertNotNull(this.entityManager);
	}

	@Test
	@Transactional
	void newLevel() {
        
        // Create a new Level
        Level testLevel = new Level();

        // Create a new Room with max capacity = 5 and type = PUBLIC
        Room testRoom = new Room();
        testRoom.setMaxUsers(5);
        testRoom.setVisibility(Room.RoomType.PUBLIC);
        
        // Create a new Match and set its Room to testRoom and its Level to testLevel
        Match testMatch = new Match();
        testMatch.setMaxPlayers(5);   
        testMatch.setRoom(testRoom);     
        testMatch.setLevel(testLevel);
        testMatch.setStatus(Match.Status.PAUSED);
        testMatch.setDate(LocalDate.now());

        this.entityManager.persist(testRoom);
        this.entityManager.persist(testLevel);
        this.entityManager.persist(testMatch);

        this.entityManager.flush();

        // Fetch the list of Matches for our testRoom
        List<Match> roomMatches = entityManager
        .createNamedQuery("Match.getRoomsMatches", Match.class)
        .setParameter("roomId", testRoom.getId())
        .getResultList();

        // Assert that the Level of the Match of our testRoom is that of testLevel
        assertEquals(testLevel, roomMatches.get(0).getLevel());
        
        
	}

	

}
