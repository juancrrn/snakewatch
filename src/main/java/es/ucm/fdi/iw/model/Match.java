package es.ucm.fdi.iw.model;

@Entity
@Data
@NoArgsConstructor
public class Match implements Transferable<Room.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    /**
     * Room
     */
    @OneToOne // TODO Comprobar esto
    @Column(nullable = false)
    private Room room;

    /**
     * Level
     */
    @OneToOne // TODO Comprobar esto
    @Column(nullable = false)
    private Level level;

    /**
     * Status
     * 
     * - Waiting
     * - Ongoing
     * - Paused
     * - Ended
     */
    public enum Status {
        WAITING,
        ONGOING,
        PAUSED,
        ENDED
    }

    @Column(nullable = false)
    private Status status;

    /**
     * Maximum number of players
     * 
     * If null, the match has no maximum.
     */
    private int maxPlayers;

    /**
     * Winner
     * 
     * Might be null if the match is waiting, ongoing or paused, or if it has
     * ended and has no winner.
     */
    private User winner;

    /*
    @Getter
    @AllArgsConstructor
    private static class Transfer {
        private long id;
    }
    
    @Override
    public Transfer toTransfer() {
		return new Transfer(id,	roomname, received.size(), sent.size());
    }

	@Override
	public String toString() {
		return toTransfer().toString();
	}
    */
}