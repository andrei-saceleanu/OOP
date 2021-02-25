package toystore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;


import money.Currency;
import money.Discount;
import money.DiscountType;
import my_exceptions.CurrencyNotFoundException;
import my_exceptions.DiscountNotFoundException;
import my_exceptions.DuplicateDiscountException;
import my_exceptions.DuplicateProductException;
import my_exceptions.NegativePriceException;

/**
 * Main Class of ToyStore application
 * It stores all available products from the store,under various currencies
 * and with a dynamic list of discounts
 * The application can use an unique instance of this class => Singleton
 * @author Saceleanu Andrei Iulian
 *
 */
public class Store implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Store INSTANCE;
	private String name;
	//PrintWriter is not Serializable,the field has to be transient
	private transient PrintWriter pw;
	List<Currency> availableCurrencies;
	Currency currency;
	List<Product> products;
	List<Manufacturer> manufacturers;
	List<Discount> discounts;
	
	/**
	 * Private constructor specific to Singleton pattern,used once to initialize
	 * different existent fields
	 */
	private Store() {
		products=new ArrayList<Product>(10000);
		manufacturers=new ArrayList<Manufacturer>(10000);
		availableCurrencies=new ArrayList<Currency>();
		//Euro starts as the default currency
		availableCurrencies.add(new Currency("EUR", "\u20ac", 1.0));
		currency=availableCurrencies.get(0);
		discounts=new ArrayList<Discount>();
		try {
			pw=new PrintWriter(new FileWriter(new File("results.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * close writer to output file
	 */
	public void closeLog() {
		getPw().close();
	}
	
	/**
	 * @return sole instance of Store available throughout running the application
	 */
	public static Store getInstance() {
		if( INSTANCE == null ) {
			INSTANCE=new Store();
		}
		return INSTANCE;
	}

	
	/**
	 * Parse a CSV file and get every product available(**with existent/positive price)
	 * @param filename the path to CSV file from where the method reads the products
	 * @return the list of products read and stored
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Product> readCSV(String filename) throws IOException, ParseException{
		FileReader fileReader=new FileReader(filename);
		List<Product> newProducts=new ArrayList<Product>(1000);
		Iterable<CSVRecord> records=CSVFormat.DEFAULT
				.withHeader("uniq_id", "product_name", "manufacturer", "price", "number_available_in_stock")
				.withFirstRecordAsHeader()
				.parse(fileReader);
		//prices need to be recalculated if the currency from file is different from
		//the currency of the store
		//the comparation uses the symbols associated with each currency
		boolean needsPriceRecalculation=false;
		String oldSymbol="";
		for(CSVRecord record:records) {
			if(!"".equals(record.get("price"))) {//check for price if any
				//parse numbers to a consistent number format( European )
				NumberFormat nf=NumberFormat.getNumberInstance(Locale.GERMANY);
				double price=nf.parse(record.get("price").substring(1)).doubleValue();
				if(!record.get("price").substring(0, 1).equals(currency.getSymbol())) {
					needsPriceRecalculation=true;
					oldSymbol=record.get("price").substring(0, 1);
				}
				//the values read from file are used to build a current product
				//Builder Pattern use case
				//the split by non-alphanumeric characters gets the quantity separated from the string "NEW"
				Product currentProduct=new ProductBuilder()
										.withId(record.get("uniq_id"))
										.withName(record.get("product_name"))
										.addManufacturer(new Manufacturer(record.get("manufacturer")))
										.withPrice(price)
										.withQuantity(Integer.parseInt(record.get(4).split("\\W+")[0]))
										.build();
				newProducts.add(currentProduct);
				try {
					this.addManufacturer(currentProduct.getManufacturer());
					this.addProduct(currentProduct);
				} catch (DuplicateProductException e) {
					e.printStackTrace();
				}
			}
		}
		if(needsPriceRecalculation) {
			double oldParity=1;
			//find old currency in available currencies
			for(Currency currency:availableCurrencies) {
				if(currency.getSymbol().equals(oldSymbol)) {
					oldParity=currency.getParityToEur();
					break;
				}
			}
			for(Product product:products)
				product.updatePrice(oldParity, currency.getParityToEur());
		}
		return newProducts;
	}
	
	/**
	 * Removes current products in store and associated manufacturers
	 */
	public void cleanStore() {
		this.manufacturers.clear();
		this.products.clear();
	}
	
	
	/**
	 * Saves the current products in a CSV file
	 * @param filename path to CSV file where the products will be written
	 * @throws IOException
	 */
	public void saveCSV(String filename) throws IOException{
		FileWriter fileWriter=new FileWriter(new File(filename));
		CSVPrinter printer=new CSVPrinter(fileWriter, CSVFormat.DEFAULT);
		printer.printRecord("uniq_id", "product_name", "manufacturer", "price", "number_available_in_stock");
		for(Product currentProduct:this.products) {
			printer.printRecord(currentProduct.csvRecord(currency.getSymbol()));
		}
		printer.close();
	}
	
	/**
	 * Load a previous state of the store in the current instance/in the present
	 * @param filename path of binary file where a previous state of store is saved
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadStore(String filename) throws IOException,ClassNotFoundException{
		ObjectInputStream iStream=new ObjectInputStream(new FileInputStream(filename));
		Store savedStore=(Store)iStream.readObject();
		this.setName(savedStore.getName());
		this.setProducts(savedStore.getProducts());
		this.setManufacturers(savedStore.getManufacturers());
		this.setCurrency(savedStore.getCurrency());
		this.setAvailableCurrencies(savedStore.getAvailableCurrencies());
		this.setDiscounts(savedStore.getDiscounts());
		iStream.close();
	}
	
	
	/**
	 * Saves current state of the store in a file
	 * @param filename path to binary file where current state of store is saved
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void saveStore(String filename) throws IOException,ClassNotFoundException{
		ObjectOutputStream oStream=new ObjectOutputStream(new FileOutputStream(filename));
		oStream.writeObject(this);
		oStream.close();
	}
	
	/**
	 * List all currencies available in store
	 */
	public void listCurrencies() {
		for(Currency currency:availableCurrencies) {
			getPw().println(currency.getName()+" "+currency.getParityToEur());
		}
	}
	
	/**
	 * Add new currency if not present already in available currencies in store
	 * @param name	3-letter name of the currency
	 * @param symbol   associated symbol of currency
	 * @param parity	conversion rate with respect to EUR( euro )
	 */
	public void addCurrency(String name,String symbol,double parity) {
		if(!availableCurrencies.contains(new Currency(name,symbol,parity))) {
			availableCurrencies.add(new Currency(name,symbol,parity));
		}
	}
	
	
	/**
	 * Change the currency which is used to calculate prices
	 * @param name Name of new currency
	 * @throws CurrencyNotFoundException 
	 */
	public void setStoreCurrency(String name) throws CurrencyNotFoundException {
		double oldParity=currency.getParityToEur();
		for(Currency c:availableCurrencies) {
			if(c.getName().equals(name)) {
				this.currency=c;
				break;
			}
		}
		if(this.currency.getParityToEur()==oldParity)
			throw new CurrencyNotFoundException();
		for(Product product:products) {
			product.updatePrice(oldParity, currency.getParityToEur());
		}
	}
	
	/**
	 * Adds new product to store,duplicates are not allowed
	 * @param product
	 * @throws DuplicateProductException
	 */
	public void addProduct(Product product) throws DuplicateProductException {
		if(this.products.contains(product)) {
			throw new DuplicateProductException();
		}
		this.products.add(product);
	}
	
	/**
	 * List all products
	 */
	public void listProducts() {
		for(Product p:products) {
			p.printProduct(this);
		}
	}
	
	/**
	 * Show the details about a product with given id
	 * @param id
	 */
	public void showProduct(String id) {
		for(Product p:products) {
			if(id.equals(p.getUniqueId())) {
				p.printProduct(this);
				break;
			}
		}
	}
	
	/**
	 * Add manufacturer to list of recognized by the store
	 * @param manufacturer
	 */
	public void addManufacturer(Manufacturer manufacturer) {
		
		this.manufacturers.add(manufacturer);
	}
	
	/**
	 * Print all known manufacturers of products in store
	 */
	public void showManufacturers() {
		manufacturers.stream().map(Manufacturer::getName).distinct().sorted()
			.forEach(this.getPw()::println);
	}
	
	/**
	 * Print all products of a manufacturer
	 * @param manufacturerName
	 */
	public void listProductsByManufacturers(String manufacturerName) {
		for(Product p:products) {
			if(p.getManufacturer().getName().equals(manufacturerName)) {
				p.printProduct(this);
			}
		}
	}
	
	/**
	 * Apply discount to every product in store,if available(existent in discounts list)
	 * @param type	Percentage or fixed discount
	 * @param value	
	 * @throws DiscountNotFoundException
	 * @throws NegativePriceException
	 * @throws DuplicateDiscountException
	 */
	public void applyDiscount(String type,double value) throws DiscountNotFoundException,NegativePriceException, DuplicateDiscountException{
		Discount discount=null;
		DiscountType t;
		switch (type) {
			case "FIXED":t=DiscountType.FIXED_DISCOUNT;break;
			case "PERCENTAGE":t=DiscountType.PERCENTAGE_DISCOUNT;break;
			default:
				throw new IllegalStateException();
		}
		for(Discount existentDiscount:discounts) {
			if(existentDiscount.getDiscountType()==t&&existentDiscount.getValue()==value) {
				discount=new Discount(existentDiscount);
				discount.setAsAppliedNow();
				break;
			}
		}
		if(discount==null)
			throw new DiscountNotFoundException();
		for(Product product:products) {//check for possible negative prices after discount
			if(discount.getDiscountType()==DiscountType.FIXED_DISCOUNT && discount.getValue()>product.getPrice()) {
				throw new NegativePriceException();
			}
			if(product.getDiscounts().contains(discount))
				throw new DuplicateDiscountException();
			product.addDiscount(discount);
		}
	}
	
	/**
	 * List all discounts available
	 */
	public void listDiscounts() {
		discounts.stream().forEach(this.pw::println);
	}
	
	/**
	 * Add discount with given type,value and name if not already present in discounts
	 * @param type
	 * @param value
	 * @param name
	 * @throws DiscountNotFoundException
	 */
	public void addDiscount(String type,double value,String name) throws DiscountNotFoundException {
		if(!("PERCENTAGE".equals(type)||"FIXED".equals(type))) {
			throw new DiscountNotFoundException();
		}
		DiscountType discountType;
		switch(type) {
			case "PERCENTAGE":discountType=DiscountType.PERCENTAGE_DISCOUNT;break;
			case "FIXED":discountType=DiscountType.FIXED_DISCOUNT;break;
			default:throw new IllegalStateException();
		}
		//Set this new discount as applied now
		Discount discount=new Discount(name, value, discountType, LocalDateTime.now());
		if(!discounts.contains(discount)) {
			discounts.add(discount);
		}
	}
	
	/**
	 * Find every product with given manufacturer
	 * @param manufacturer
	 * @return array of products with given manufacturer
	 */
	Product[] getProductsByManufacturer(Manufacturer manufacturer) {
		List<Product> foundProducts=new ArrayList<Product>();
		for(Product product:products) {
			if(product.getManufacturer().equals(manufacturer)) {
				foundProducts.add(product);
			}
		}
		return (Product [])foundProducts.toArray();
	}
	
	/**
	 * Calculate total price(with applied discounts) of given list of products
	 * @param queryProducts
	 * @return
	 */
	public double calculateTotal(List<Product> queryProducts) {
		double totalCost=0;
		for(Product p:queryProducts) {
			totalCost+=(p.getPriceWithDiscounts()*p.getQuantity());
		}
		return totalCost;
	}
	
	/**
	 * Update parity of a certain currency
	 * If it is the current one in store,recalculate all prices
	 * @param name
	 * @param newParity
	 */
	public void updateParity(String name,double newParity) {
		Currency current=null;
		double oldParity=-1;
		for(Currency c:availableCurrencies) {
			if(c.getName().equals(name)) {
				current=c;
				oldParity=c.getParityToEur();
				c.updateParity(newParity);
				break;
			}
		}
		if(current==currency) {
			for(Product product:products) {
				product.updatePrice(oldParity, currency.getParityToEur());
			}
		}
	}
	
	//getters and setters
	public String getName() {
		return name;
	}

	public String getStoreCurrency() {
		return currency.getName();
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public List<Currency> getAvailableCurrencies() {
		return availableCurrencies;
	}

	public List<Discount> getDiscounts() {
		return discounts;
	}
	
	public List<Product> getProducts() {
		return products;
	}

	public PrintWriter getPw() {
		return pw;
	}

	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void setManufacturers(List<Manufacturer> manufacturers) {
		this.manufacturers = manufacturers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAvailableCurrencies(List<Currency> availableCurrencies) {
		this.availableCurrencies = availableCurrencies;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}
	public void setCurrency(Currency currency) {
		this.currency=currency;
	}
	
}
