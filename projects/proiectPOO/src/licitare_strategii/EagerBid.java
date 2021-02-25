package licitare_strategii;

/**
 * Eager strategy-the bid increases moderately and starts at 1.2*min price of product
 * @author Saceleanu Andrei
 *
 */
public class EagerBid implements BidStrategy{

	@Override
	public double proposeNewBid(double oldBid,double winninPrice,double maxPrice) {
		double newBid=1.5*winninPrice;
		if(newBid>maxPrice)
			return maxPrice;
		return newBid;
	}

	@Override
	public double startFactor() {
		return 0.2;
	}

	@Override
	public void name() {
		System.out.println("Eager");
	}
	
}
