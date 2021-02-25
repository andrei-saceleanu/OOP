package restrictii;
/**
 * Restriction equivalent to the cost of an edge in the graph/street on the map.
 * @author Saceleanu Andrei
 *
 */
public class RViteza implements Restrictie{
	private int cost_aditional;
	
	/**
	 * Default constructor.
	 */
	public RViteza() {}
	
	/**
	 * Constructor with parameters.
	 * @param cost_aditional	maximum speed/cost of a street
	 */
	public RViteza(int cost_aditional) {
		this.cost_aditional = cost_aditional;
	}

	/**
	 * Default getter of field cost_aditional.
	 * @return	the value of the field cost_aditional.
	 */
	public int getCost_aditional() {
		return cost_aditional;
	}

	
	/**
	 * Default setter of sole field cost_aditional
	 * @param cost_aditional	the cost of the street with this restriction
	 */
	public void setCost_aditional(int cost_aditional) {
		this.cost_aditional = cost_aditional;
	}
	
}