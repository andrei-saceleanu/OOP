package produse;


/**
 * Painting -type of product
 * Extends product capabilities and properties with
 * 2 new fields:name of painter and painting method
 * @author Saceleanu Andrei
 *
 */
public class Tablou extends Produs{
	private String numePictor;
	private Culori metodaPictare;
	public String getNumePictor() {
		return numePictor;
	}
	public void setNumePictor(String numePictor) {
		this.numePictor = numePictor;
	}
	public Culori getMetodaPictare() {
		return metodaPictare;
	}
	public void setMetodaPictare(Culori metodaPictare) {
		this.metodaPictare = metodaPictare;
	}
	
}
