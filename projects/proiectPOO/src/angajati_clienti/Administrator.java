package angajati_clienti;

import casa_licitatii.CasaLicitatii;
import produse.Produs;

/**
 * Singleton class which models an administrator.
 * The main responsibility of the administrator is to add
 * products to the auction house list of products
 * @author Saceleanu Andrei
 *
 */
public class Administrator implements Angajat{
	private static Administrator iNSTANCE=null;
	private Administrator() {
		
	}
	public static Administrator getInstance() {
		if(iNSTANCE==null) {
			iNSTANCE=new Administrator();
		}
		return iNSTANCE;
	}
	
	public void addProdus(CasaLicitatii casaLicitatii,Produs produs) {
		casaLicitatii.getProduseVanzare().add(produs);
	}
}
