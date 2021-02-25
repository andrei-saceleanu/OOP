package licitare_strategii;

/**
 * Interface which will be implemented by various types of strategies
 * A strategy will give a startFactor which will be used in the computation
 * of a starting bid of a client and will compute a new bid given some
 * parameters(previous bid,top bid in auction,max price for client)
 * @author Saceleanu Andrei
 *
 */
public interface BidStrategy {
	double startFactor();
	double proposeNewBid(double oldBid,double winninPrice,double maxPrice);
	void name();
}
