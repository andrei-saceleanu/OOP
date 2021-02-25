package money;

import java.io.Serializable;

/**
 * A class which models different currencies available for a store
 * It has a name,symbol and a conversion rate with respect to EUR( euro )
 * @author Saceleanu Andrei Iulian
 *
 */
public class Currency implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name,symbol;
	private double parityToEur;
	
	public Currency() {}
	public Currency(String name, String symbol, double parityToEur) {
		this.name = name;
		this.symbol = symbol;
		this.parityToEur = parityToEur;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getParityToEur() {
		return parityToEur;
	}
	public void updateParity(double parityToEur) {
		this.parityToEur = parityToEur;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(parityToEur);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		Currency other = (Currency) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(parityToEur) != Double.doubleToLongBits(other.parityToEur))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
}
