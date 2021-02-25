package produse;

/**
 * Product Class -the objects transacted in an auction
 * It is identified by an id,a name,a year,a min price in order
 * to be sold and a selling price(the outcome of a successful auction) 
 * @author Saceleanu Andrei
 *
 */
public class Produs {
	protected int id;
	protected int an;
	protected int auctionParticipants;
	protected String nume;
	protected double pretVanzare;
	protected double pretMinim;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAn() {
		return an;
	}
	public void setAn(int an) {
		this.an = an;
	}
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public double getPretVanzare() {
		return pretVanzare;
	}
	public void setPretVanzare(double pretVanzare) {
		this.pretVanzare = pretVanzare;
	}
	public double getPretMinim() {
		return pretMinim;
	}
	public void setPretMinim(double pretMinim) {
		this.pretMinim = pretMinim;
	}
	public int getAuctionParticipants() {
		return auctionParticipants;
	}
	public void setAuctionParticipants(int auctionParticipants) {
		this.auctionParticipants = auctionParticipants;
	}
}
