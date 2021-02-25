package comisioane;

/**
 * Type of computer associated with individual clients
 * with more than 5 participations
 * @author Saceleanu Andrei
 *
 */
public class ComisionC2 implements ComisionCalculator{

	@Override
	public double calculeaza(double valoarePropusa) {
		return 0.15*valoarePropusa;
	}

}
