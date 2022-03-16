package es.ucm.fdi.iw.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Usage: "mvn test" on console 
 */
@SpringBootTest
class MatchTests {

    @Autowired
    private EntityManager entityManager;

	@Test
	public void contextLoads() throws Exception {
		assertNotNull(this.entityManager);
	}

	@Test
	@Transactional
	void newMatch() {
        
        Level level = new Level();

        Room room = new Room();
        room.setMaxUsers(10);
        room.setVisibility(Room.RoomType.PUBLIC);

        
        User player1 = new User();
        player1.setEnabled(true);
        player1.setUsername("player1");
        player1.setPassword("testpassword");

        User player2 = new User();
        player2.setEnabled(true);
        player2.setUsername("player2");
        player2.setPassword("testpassword");        

        
        RoomUser roomUser1 = new RoomUser();
        roomUser1.setAdmin(false);
        roomUser1.setRoom(room);
        roomUser1.setUser(player1);

        RoomUser roomUser2 = new RoomUser();
        roomUser2.setAdmin(false);
        roomUser2.setRoom(room);
        roomUser2.setUser(player2);

        

        Match match = new Match();
        match.setMaxPlayers(5);        
        match.setRoom(room);
        match.setLevel(level);
        match.setStatus(Match.Status.ENDED);
        match.setDate(LocalDate.now());

        MatchPlayer matchPlayer1 = new MatchPlayer();
        matchPlayer1.setMatch(match);
        matchPlayer1.setPlayer(player1);
        matchPlayer1.setPosition(1);

        MatchPlayer matchPlayer2 = new MatchPlayer();
        matchPlayer2.setMatch(match);
        matchPlayer2.setPlayer(player2);
        matchPlayer2.setPosition(2);


        this.entityManager.persist(level);
        this.entityManager.persist(room);
        this.entityManager.persist(player1);
        this.entityManager.persist(player2);
        this.entityManager.persist(roomUser1);
        this.entityManager.persist(roomUser2); 
        this.entityManager.persist(match);
        this.entityManager.persist(matchPlayer1);
        this.entityManager.persist(matchPlayer2);
        
        this.entityManager.flush();
        //User userInBD = entityManager.createNamedQuery("User.byUsername", User.class).setParameter("username", user.getUsername()).getSingleResult();

		//assertEquals(5, userInBD.getId());
	}

	

}
