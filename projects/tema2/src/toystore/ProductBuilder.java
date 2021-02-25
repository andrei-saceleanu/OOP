package toystore;

/**
 * Builder pattern used to construct a product given certain fields
 * @author Saceleanu Andrei Iulian
 *
 */
public class ProductBuilder {
	private final Product product=new Product();
	
	public Product build() {
		return product;
	}
	public ProductBuilder withId(String uniqueId) {
		product.setUniqueId(uniqueId);
		return this;
	}
	public ProductBuilder withName(String name) {
		product.setName(name);;
		return this;
	}
	public ProductBuilder addManufacturer(Manufacturer manufacturer) {
		product.setManufacturer(manufacturer);
		return this;
	}
	public ProductBuilder withPrice(double price) {
		product.setPrice(price);
		return this;
	}
	public ProductBuilder withQuantity(int quantity) {
		product.setQuantity(quantity);
		return this;
	}
	
}
