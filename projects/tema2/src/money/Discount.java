package money;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * A class which models different discounts which may be applied to products in store
 * It has a name,a value,one out of 2 available types and the last Date/time it was
 * applied
 * @author Saceleanu Andrei Iulian
 *
 */
public class Discount implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private DiscountType discountType;
	private double value;
	private LocalDateTime lastDateApplied=null;
	
	public Discount() {}
	public Discount(Discount other) {
		this(other.getName(),other.getValue(),other.getDiscountType(),other.getLastDateApplied());
	}
	
	public Discount(String name, double value, DiscountType discountType, LocalDateTime lastDateApplied) {
		this.name=name;
		this.value=value;
		this.discountType=discountType;
		this.lastDateApplied=lastDateApplied;
		
	}
	public void setAsAppliedNow() {
		lastDateApplied=LocalDateTime.now();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DiscountType getDiscountType() {
		return discountType;
	}
	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public LocalDateTime getLastDateApplied() {
		return lastDateApplied;
	}
	public void setLastDateApplied(LocalDateTime lastDateApplied) {
		this.lastDateApplied = lastDateApplied;
	}
	@Override
	public String toString() {
		return discountType + " " + value + " " + name + " " + lastDateApplied;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discountType == null) ? 0 : discountType.hashCode());
		result = prime * result + ((lastDateApplied == null) ? 0 : lastDateApplied.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Discount other = (Discount) obj;
		if (discountType != other.discountType)
			return false;
		if (lastDateApplied == null) {
			if (other.lastDateApplied != null)
				return false;
		} else if (!lastDateApplied.equals(other.lastDateApplied))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}
	
	
	
}
