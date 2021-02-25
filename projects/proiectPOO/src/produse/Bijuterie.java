package produse;


/**
 * Jewelry -type of product
 * Extends product capabilities and properties with
 * 2 new fields:gem boolean flag and material
 * @author Saceleanu Andrei
 *
 */
public class Bijuterie extends Produs{
	private String material;
	private boolean piatraPretioasa;
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public boolean isPiatraPretioasa() {
		return piatraPretioasa;
	}
	public void setPiatraPretioasa(boolean piatraPretioasa) {
		this.piatraPretioasa = piatraPretioasa;
	}
	
}
