package vehicule;

/**
 * A type of vehicle
 * @author Saceleanu Andrei
 *
 */
public class Autoturism extends Vehicul{
	/**
	 * Default Constructor.
	 * The fields are set according to the specifications.
	 */
	public Autoturism() {
		this.setCost(4);
		this.setGabarit(2);
		this.setTip("Autovehicul");
	}
}