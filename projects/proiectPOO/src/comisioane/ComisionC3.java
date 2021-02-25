package comisioane;

/**
 * Type of computer associated with legal clients
 * with less than 25 participations
 * @author Saceleanu Andrei
 *
 */
public class ComisionC3 implements ComisionCalculator{

	@Override
	public double calculeaza(double valoarePropusa) {
		return 0.25*valoarePropusa;
	}

}
