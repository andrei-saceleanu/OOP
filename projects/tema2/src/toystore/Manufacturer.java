package toystore;

import java.io.Serializable;


/**
 * A class which models a manufacturer of a product in store
 * It has a name and the total count of products in store delivered by itself
 * @author Saceleanu Andrei Iulian
 *
 */
public class Manufacturer implements Serializable ,Comparable<Manufacturer>{
	private static final long serialVersionUID = 1L;
	private String name;
	private int countProducts;
	
	public Manufacturer() {}
	public Manufacturer(String name) {
		if(!"".equals(name))
			this.name=name;
		else {
			this.name="Not Available";
		}
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Manufacturer)) {
			return false;
		}
		Manufacturer otherManufacturer=(Manufacturer)obj;
		return this.name.equals(otherManufacturer.name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCountProducts() {
		return countProducts;
	}
	public void setCountProducts(int countProducts) {
		this.countProducts = countProducts;
	}
	@Override
	public int compareTo(Manufacturer arg0) {
		return this.name.compareTo(arg0.getName());
	}
	
}
