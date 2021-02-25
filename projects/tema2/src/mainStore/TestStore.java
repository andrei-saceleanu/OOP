package mainStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import my_exceptions.CurrencyNotFoundException;
import my_exceptions.DiscountNotFoundException;
import my_exceptions.DuplicateDiscountException;
import my_exceptions.NegativePriceException;
import toystore.Product;
import toystore.Store;

/**
 * The class processes commands given and modifies the store accordingly
 * @author Saceleanu Andrei Iulian
 *
 */
public class TestStore {

	public static void main(String[] args) {
		Store store=Store.getInstance();
		BufferedReader br;
		
		try {
			br=new BufferedReader(new FileReader(new File("commands.txt")));
			String command;
			while((command = br.readLine()) != null) {
				if( "exit".equals(command) || "quit".equals(command) ) {
					break;
				}else if( command.indexOf("loadstore") == 0 ) {
					String fileName=command.substring( command.indexOf(' ')+1 );
					try {
						store.loadStore(fileName);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if( command.indexOf("savestore") == 0 ) {
					String fileName=command.substring(command.indexOf(' ')+1);
					try {
						store.saveStore(fileName);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if( command.indexOf("loadcsv") == 0 ) {
					String [] tokens=command.split(" ");
					if(store!=null)
						store.cleanStore();
					try {
						store.readCSV(tokens[1]);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else if( command.indexOf("savecsv") ==0 ) {
					String fileName=command.substring(command.indexOf(' ')+1);
					store.saveCSV(fileName);
				}else if( command.equals("listproducts") ) {
					store.listProducts();
				}else if(command.indexOf("showproduct")==0) {
					String productID=command.substring(command.indexOf(' ')+1);
					store.showProduct(productID);
				}else if(command.indexOf("listcurrencies")==0) {
					store.listCurrencies();
				}else if(command.indexOf("listmanufacturers")==0) {
					store.showManufacturers();
				}else if(command.indexOf("getstorecurrency")==0) {
					store.getPw().println(store.getStoreCurrency());
				}else if(command.indexOf("setstorecurrency")==0) {
					String newCurrency=command.substring(command.indexOf(' ')+1);
					try {
						store.setStoreCurrency(newCurrency);
					} catch (CurrencyNotFoundException e) {
						e.printStackTrace();
					}
				}else if(command.indexOf("addcurrency")==0) {
					String [] params=command.split(" ");
					store.addCurrency(params[1], params[2], Double.parseDouble(params[3]));
				}else if(command.indexOf("updateparity")==0) {
					String [] params=command.split(" ");
					store.updateParity(params[1], Double.parseDouble(params[2]));
				}else if(command.indexOf("listproductsbymanufacturer")==0) {
					String manufacturerName=command.substring(command.indexOf(' ')+1);
					store.listProductsByManufacturers(manufacturerName);
				}else if(command.indexOf("listdiscounts")==0) {
					store.listDiscounts();
				}else if(command.indexOf("adddiscount")==0) {
					String [] params=command.split(" ");
					String name="";
					for(int i=3;i<params.length;i++) {
						name+=params[i]+" ";
					}
					try {
						store.addDiscount(params[1], Double.parseDouble(params[2]),name );
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (DiscountNotFoundException e) {
						e.printStackTrace();
					}
				}else if(command.indexOf("applydiscount")==0) {
					String [] params=command.split(" ");
					try {
						store.applyDiscount(params[1],Double.parseDouble(params[2]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (DiscountNotFoundException e) {
						e.printStackTrace();
					} catch (NegativePriceException e) {
						e.printStackTrace();
					} catch (DuplicateDiscountException e) {
						e.printStackTrace();
					}
				}else if(command.indexOf("calculatetotal")==0) {
					String [] params=command.split(" ");
					List<Product> queryProducts=new ArrayList<Product>();
					for(int i=1;i<params.length;i++) {
						for(Product product:store.getProducts()) {
							if(product.getUniqueId().equals(params[i])) {
								queryProducts.add(product);
								break;
							}
						}
					}
					store.getPw().print(store.getCurrency().getSymbol());
					store.getPw().println(store.calculateTotal(queryProducts));
				}
			}
			br.close();
			store.closeLog();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
