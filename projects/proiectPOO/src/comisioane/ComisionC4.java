package comisioane;

/**
 * Type of computer associated with legal clients
 * with more than 25 participations
 * @author Saceleanu Andrei
 *
 */
public class ComisionC4 implements ComisionCalculator{

	@Override
	public double calculeaza(double valoarePropusa) {
		return 0.1*valoarePropusa;
	}

}

