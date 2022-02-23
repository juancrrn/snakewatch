package es.ucm.fdi.iw.model;

@Entity
@Data
@NoArgsConstructor
public class Room implements Transferable<Room.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

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
}