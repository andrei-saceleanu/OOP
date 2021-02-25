package casa_licitatii;

import java.util.List;

import angajati_clienti.Broker;

/**
 * Runnable implementation which will perform an auction in
 * a distinct thread
 * @author Saceleanu Andrei
 *
 */
public class ParallelTask implements Runnable{
	private CasaLicitatii casaLicitatii;
	private List<Broker> brokeri;
	private Licitatie licitatie;
	
	public ParallelTask(CasaLicitatii casaLicitatii,List<Broker> brokeri,Licitatie licitatie) {
		this.casaLicitatii=casaLicitatii;
		this.brokeri=brokeri;
		this.licitatie=licitatie;
	}
	
	public void setBrokeri(List<Broker> brokeri) {
		this.brokeri = brokeri;
	}
	
	public void setLicitatie(Licitatie licitatie) {
		this.licitatie = licitatie;
	}
	
	@Override
	public void run() {
		casaLicitatii.runLicitatie(brokeri, licitatie);
	}
}
