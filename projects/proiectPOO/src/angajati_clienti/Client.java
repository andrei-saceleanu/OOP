package angajati_clienti;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import casa_licitatii.CasaLicitatii;
import comisioane.ComisionCalculator;
import licitare_strategii.BidStrategy;
import produse.Produs;


/**
 * Client class - the participant in an auction which can be
 * either an individual or a legal person.
 * Besides some personal data,a client stores a calculator for
 * the commission entitled to his/her broker and a map between
 * an auction and the strategy of giving new bids
 * @author Saceleanu Andrei
 *
 */
public abstract class Client {
	protected int id;
	protected int nrParticipari;
	protected int nrLicitatiiCastigate;
	protected String nume;
	protected String adresa;
	protected ComisionCalculator comisionAsociat;
	protected ConcurrentHashMap<Long, BidStrategy> strategyForEachAuction;
	
	/**
	 * Calculates first bid of client in an auction
	 * @param produs product put to be auctioned
	 * @param strategy the strategy used by the client to give new values
	 * @return starting value of bid
	 */
	public synchronized double propuneStartingPrice(Produs produs,BidStrategy strategy) {
		double minPriceOfProduct=produs.getPretMinim();
		return minPriceOfProduct*(1+strategy.startFactor());
	}
	
	public ConcurrentMap<Long, BidStrategy> getStrategyForEachAuction() {
		return strategyForEachAuction;
	} 
	
	/**
	 * Register to the auction house
	 * @param brokeri list of available brokers
	 * @param casa the auction house where the client registers
	 * @param idProdus the product which is wanted by the client
	 * @param pretMaxim max price the client is willing to give
	 * @param strategy strategy used during the auction for the given product
	 */
	public synchronized void depuneSolicitare(List<Broker> brokeri,CasaLicitatii casa,int idProdus,double pretMaxim,BidStrategy strategy) {
		casa.acceptClient(this,brokeri,idProdus,pretMaxim,strategy);
	}
	
	/**
	 * Calculates new bid,based on oldBid,top price at current round of auction and max price
	 * @param oldBid previous bid given by client
	 * @param winninPrice top price of current round of auction
	 * @param maxPrice max price the client is willing to give
	 * @param currentId the thread id associated with the current auction
	 * @return
	 */
	public synchronized double sendBid(double oldBid,double winninPrice,double maxPrice,long currentId) {
		return strategyForEachAuction.get(currentId).proposeNewBid(oldBid,winninPrice,maxPrice);
	}
	public ComisionCalculator getComisionAsociat() {
		return comisionAsociat;
	}
	public void setComisionAsociat(ComisionCalculator comisionAsociat) {
		this.comisionAsociat = comisionAsociat;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNrParticipari() {
		return nrParticipari;
	}
	public void setNrParticipari(int nrParticipari) {
		this.nrParticipari = nrParticipari;
	}
	public int getNrLicitatiiCastigate() {
		return nrLicitatiiCastigate;
	}
	public void setNrLicitatiiCastigate(int nrLicitatiiCastigate) {
		this.nrLicitatiiCastigate = nrLicitatiiCastigate;
	}
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public abstract void updateCalculator();

	@Override
	public String toString() {
		return "Client [id=" + id + ", nrParticipari=" + nrParticipari + ", nrLicitatiiCastigate="
				+ nrLicitatiiCastigate + ", nume=" + nume + "]";
	}
	
	
}
