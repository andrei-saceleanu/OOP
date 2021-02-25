package produse;

/**
 * Builder for products
 * The builder can start setting common fields for a product
 * regardless of its type;the constructor will form a product
 * according to type given;
 * @author Saceleanu Andrei
 *
 */
public class ProdusBuilder {
	private final Produs produs;
	
	public ProdusBuilder(String type) {
		ProdusFactory factory=new ProdusFactory();
		produs=factory.makeProdus(type);
	}
	public Produs build() {
		return produs;
	}
	public ProdusBuilder withId(int id) {
		produs.setId(id);
		return this;
	}
	public ProdusBuilder withNume(String name) {
		produs.setNume(name);
		return this;
	}
	public ProdusBuilder withPretVanzare(double pretVanzare) {
		produs.setPretVanzare(pretVanzare);
		return this;
	}
	public ProdusBuilder withPretMinim(double pretMinim) {
		produs.setPretMinim(pretMinim);
		return this;
	}
	public ProdusBuilder withAn(int an) {
		produs.setAn(an);
		return this;
	}
	
	public ProdusBuilder withParticipants(int nrPart) {
		produs.setAuctionParticipants(nrPart);
		return this;
	}
}
