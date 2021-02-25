package vehicule;
/**
 * A type of vehicle
 * @author Saceleanu Andrei
 *
 */
public class Motocicleta extends Vehicul{
	/**
	 * Default Constructor.
	 * The fields are set according to the specifications.
	 */
	public Motocicleta() {
		this.setCost(2);
		this.setGabarit(1);
		this.setTip("Motocicleta");
	}
}