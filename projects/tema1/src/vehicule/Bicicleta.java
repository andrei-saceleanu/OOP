package vehicule;

/**
 * A type of vehicle
 * @author Saceleanu Andrei
 *
 */
public class Bicicleta extends Vehicul{
	/**
	 * Default Constructor.
	 * The fields are set according to the specifications.
	 */
	public Bicicleta() {
		this.setCost(1);
		this.setGabarit(1);
		this.setTip("Bicicleta");
	}
}