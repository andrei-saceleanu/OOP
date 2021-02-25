package licitare_strategii;

/**
 * MustHave strategy-the bid increases aggressively and starts at 1.5*min price of product
 * @author Saceleanu Andrei
 *
 */
public class MustHaveItBid implements BidStrategy{

	@Override
	public double proposeNewBid(double oldBid, double winninPrice, double maxPrice) {
		double newBid=Math.max(2*oldBid,winninPrice);
		if(newBid>maxPrice)
			return maxPrice;
		return newBid;
	}

	@Override
	public double startFactor() {
		return 0.5;
	}

	@Override
	public void name() {
		System.out.println("MustHaveIt");
	}

	
	
	
}
