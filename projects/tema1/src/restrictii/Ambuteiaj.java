package restrictii;
/**
 * Restriction which allows different types and costs
 * @author Saceleanu Andrei
 *
 */
public class Ambuteiaj implements Restrictie{
	private String tip;
	private int cost;
	/**
	 * Default Constructor.
	 */
	public Ambuteiaj() {}

	/**
	 * Constructor with parameters
	 * @param tip	type of traffic jam
	 * @param cost	cost of traffic jam
	 */
	public Ambuteiaj(String tip, int cost) {
		this.tip = tip;
		this.cost = cost;
	}
	
	/**
	 * @return type of traffic jam
	 */
	public String getTip() {
		return tip;
	}

	/**
	 * @param type of traffic jam
	 */
	public void setTip(String tip) {
		this.tip = tip;
	}

	/**
	 * @return cost of traffic jam
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost of traffic jam
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	
}