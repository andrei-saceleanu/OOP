package comisioane;

/**
 * Type of computer associated with individual clients
 * with less than 5 participations
 * @author Saceleanu Andrei
 *
 */
public class ComisionC1 implements ComisionCalculator{

	@Override
	public double calculeaza(double valoarePropusa) {
		return 0.2*valoarePropusa;
	}

}
