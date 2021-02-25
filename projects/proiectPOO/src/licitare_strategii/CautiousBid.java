package licitare_strategii;

/**
 * Cautious strategy-the bid increases slowly and starts at 0.9*min price of product
 * @author Saceleanu Andrei
 *
 */
public class CautiousBid implements BidStrategy{

	@Override
	public double proposeNewBid(double oldBid,double winninPrice,double maxPrice) {
		double newBid=Math.min(1.01*oldBid,winninPrice+1);
		if(newBid>maxPrice)
			return maxPrice;
		return newBid;
	}

	@Override
	public double startFactor() {
		return -0.1;
	}

	@Override
	public void name() {
		System.out.println("Cautious");
	}
	
}
