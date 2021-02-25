package toystore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import money.Discount;
import money.DiscountType;
import my_exceptions.DuplicateDiscountException;

/**
 * This class models a product from ToyStore
 * It can be identified by an id or name
 * It stores a price and the quantity in store,as well as any discount applied to it
 * @author Saceleanu Andrei Iulian
 *
 */
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private String uniqueId,name;
	private Manufacturer manufacturer;
	private double price;
	private int quantity;
	private List<Discount> discounts;
	
	
	public Product() {
		discounts=new ArrayList<Discount>();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Product)) {
			return false;
		}
		Product otherProduct=(Product)obj;
		return this.uniqueId.equals(otherProduct.uniqueId);
	}
	
	/**
	 * Starting from default price,process every discount to calculate the current price
	 * @return the final price after discounts
	 */
	public double getPriceWithDiscounts() {
		double currentPrice=price;
		for(Discount discount:getDiscounts()) {
			if(discount.getDiscountType()==DiscountType.FIXED_DISCOUNT) {
				currentPrice-=discount.getValue();
			}else {
				currentPrice=currentPrice-discount.getValue()*currentPrice/100;
			}
		}
		return currentPrice;
	}
	
	
	
	/**
	 * Change price according to new currency/parity
	 * @param oldParity
	 * @param newParity
	 */
	public void updatePrice(double oldParity,double newParity) {
		price=newParity*price/oldParity;
	}
	
	/**
	 * Add discount if not already present
	 * @param discount
	 * @throws DuplicateDiscountException
	 */
	public void addDiscount(Discount discount) throws DuplicateDiscountException {
		if(getDiscounts().contains(discount)) {
			throw new DuplicateDiscountException();
		}
		getDiscounts().add(discount);
	}
	
	/**
	 * Construct a list of strings in the form of an entry from a CSV file
	 * @param symbolOfCurrentCurrency
	 * @return
	 */
	public List<String> csvRecord(String symbolOfCurrentCurrency) {
		String [] result=new String[] {uniqueId,name,manufacturer.getName(),
				symbolOfCurrentCurrency+String.valueOf(price),String.valueOf(quantity)};
		return Arrays.asList(result);
	}
	
	/**
	 * Print the details of product,csvRecord-style but concatenated with comma
	 * @param store
	 */
	public void printProduct(Store store) {
		store.getPw().println(csvRecord(store.currency.getSymbol()).stream()
				.collect(Collectors.joining(",")));
	}
	
	public String getUniqueId() {
		return uniqueId;
	}
	
	public String getName() {
		return name;
	}
	
	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	
	public double getPrice() {
		return price;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	public List<Discount> getDiscounts() {
		return discounts;
	}
	
}
