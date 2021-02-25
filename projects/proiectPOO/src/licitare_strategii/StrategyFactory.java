package licitare_strategii;

/**
 * Strategy factory which parses a string and makes an instance
 * of appropriate strategy 
 * @author Saceleanu Andrei
 *
 */
public class StrategyFactory {
	
	private StrategyFactory() {}
	
	public static BidStrategy getStrategy(String type) {
		switch (type) {
			case "cautious":return new CautiousBid();
			case "eager":return new EagerBid();
			case "musthave":return new MustHaveItBid();
			default:throw new IllegalStateException();
		}
	}
}
