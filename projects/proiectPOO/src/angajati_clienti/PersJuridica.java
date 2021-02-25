package angajati_clienti;

import java.util.concurrent.ConcurrentHashMap;

import comisioane.ComisionC4;
import licitare_strategii.BidStrategy;

/**
 * Individual client
 * Apart from common fields,it also stores 2 other fields
 * (capitalSocial,companie)
 * If the number of participations is more than 25,the CommissionCalculator
 * will be updated.
 * @author Saceleanu Andrei
 *
 */
public class PersJuridica extends Client{
	private double capitalSocial;
	private Companie companie;
	
	
	public PersJuridica() {
		strategyForEachAuction=new ConcurrentHashMap<Long, BidStrategy>();
	}
	public double getCapitalSocial() {
		return capitalSocial;
	}
	public void setCapitalSocial(double capitalSocial) {
		this.capitalSocial = capitalSocial;
	}
	public Companie getCompanie() {
		return companie;
	}
	public void setCompanie(Companie companie) {
		this.companie = companie;
	}
	public void updateCalculator() {
		if(nrParticipari>=25) 
			this.setComisionAsociat(new ComisionC4());
	}

}
