package vehicule;

/**
 * Abstract class from which can be derived various types of vehicles.
 * @author Saceleanu Andrei
 *
 */
public abstract class Vehicul {
	protected String tip;
	protected int gabarit,cost;
	/**
	 * @return type of vehicle
	 */
	public String getTip() {
		return tip;
	}
	/**
	 * @param type type of vehicle
	 */
	public void setTip(String tip) {
		this.tip = tip;
	}
	/**
	 * @return weight of vehicle
	 */
	public int getGabarit() {
		return gabarit;
	}
	/**
	 * @param gabarit the weight of vehicle
	 */
	public void setGabarit(int gabarit) {
		this.gabarit = gabarit;
	}
	/**
	 * @return cost of vehicle
	 */
	public int getCost() {
		return cost;
	}
	/**
	 * @param cost the cost of vehicle
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
}