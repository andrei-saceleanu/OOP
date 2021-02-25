package produse;

/**
 * Factory for the different types of products available
 * @author Saceleanu Andrei
 *
 */
public class ProdusFactory {
	
	public Produs makeProdus(String type) {
		switch (type) {
			case "tablou":return new Tablou();
			case "bijuterie":return new Bijuterie();
			case "mobila":return new Mobila();
			default:throw new IllegalStateException();
		}
	}
}
