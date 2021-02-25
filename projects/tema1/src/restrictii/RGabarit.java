package restrictii;
/**
 * Restriction which limits the possible weight of a vehicle on a certain street
 * @author Saceleanu Andrei
 *
 */
public class RGabarit implements Restrictie{
	private int limita_gabarit;
	/**
	 * Default constructor.(Not used in current application.)
	 */
	public RGabarit() {}
	/**
	 * Constructor with parameters
	 * @param limita_gabarit	maximum allowed weight for a vehicle
	 */
	public RGabarit(int limita_gabarit) {
		this.limita_gabarit = limita_gabarit;
	}
	/**
	 * Default getter for field limita_gabarit
	 * @return	the value of private field limita_gabarit
	 */
	public int getLimita_gabarit() {
		return limita_gabarit;
	}

	/**
	 * Default setter for field limita_gabarit
	 * @param limita_gabarit	maximum allowed weight for a vehicle
	 */
	public void setLimita_gabarit(int limita_gabarit) {
		this.limita_gabarit = limita_gabarit;
	}
	
}