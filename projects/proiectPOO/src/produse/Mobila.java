package produse;

/**
 * Furniture-type of product
 * Extends product capabilities and properties with
 * 2 new fields:type and material
 * @author Saceleanu Andrei
 *
 */
public class Mobila extends Produs{
	private String tip;
	private String material;

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
	
}
