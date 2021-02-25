package angajati_clienti;

import java.util.concurrent.ConcurrentHashMap;

import comisioane.ComisionC2;
import licitare_strategii.BidStrategy;

/**
 * Individual client
 * Apart from common fields,it also stores a birthday date
 * If the number of participations is more than 5,the CommissionCalculator
 * will be updated.
 * @author Saceleanu Andrei
 *
 */
public class PersFizica extends Client{
	private String dataNastere;

	
	public PersFizica() {
		strategyForEachAuction=new ConcurrentHashMap<Long, BidStrategy>();
	}
	public String getDataNastere() {
		return dataNastere;
	}

	public void setDataNastere(String dataNastere) {
		this.dataNastere = dataNastere;
	}
	public void updateCalculator() {
		if(nrParticipari>=5)
			this.setComisionAsociat(new ComisionC2());
	}
}
