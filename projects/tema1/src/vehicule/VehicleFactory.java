package vehicule;

/**
 * A vehicle factory.
 * Provides instances of the available types of vehicles.
 * @author Saceleanu Andrei
 *
 */
public class VehicleFactory {
	
	/**
	 * Default Constructor.
	 */
	public VehicleFactory() {}
	/** 
	 * @param vehicle	type of vehicle requested
	 * @return an instance of vehicle with that type
	 */
	public Vehicul getVehicle(String vehicle) {
		if(vehicle.equals("a")) {
			return new Autoturism();
		}else if(vehicle.equals("b")) {
			return new Bicicleta();
		}else if(vehicle.equals("c")) {
			return new Camion();
		}else {
			return new Motocicleta();
		}
	}
}