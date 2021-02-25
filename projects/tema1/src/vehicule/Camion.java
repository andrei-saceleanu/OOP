package vehicule;

/**
 * A type of vehicle
 * @author Saceleanu Andrei
 *
 */
public class Camion extends Vehicul{
	/**
	 * Default Constructor.
	 * The fields are set according to the specifications.
	 */
	public Camion() {
		this.setCost(6);
		this.setGabarit(3);
		this.setTip("Camion");
	}
}