package angajati_clienti;

import java.util.ArrayList;
import java.util.List;


import casa_licitatii.Licitatie;
import produse.Produs;

/**
 * Broker Class-models the mediator between
 * the auction house and clients;
 * Every broker keeps a personal record of
 * clients and the auctions in which he represents
 * each one.
 * The constraint is that a broker cannot have more than 
 * one client per different auction.
 * @author Saceleanu Andrei
 *
 */
public class Broker implements Angajat{
	private List<Client> clienti;
	private List<Integer> idLicitatii;

	public Broker() {
		clienti=new ArrayList<>();
		idLicitatii=new ArrayList<>();
	}
	
	public List<Integer> getIdLicitatii() {
		return idLicitatii;
	}

	public void setIdLicitatii(List<Integer> idLicitatii) {
		this.idLicitatii = idLicitatii;
	}

	public List<Client> getClienti() {
		return clienti;
	}
	
	public void setClienti(List<Client> clienti) {
		this.clienti = clienti;
	}
	
	/**
	 * Search for corresponding client given id of an auction
	 * @param idLicitatie id of an auction
	 * @return client of broker for current auction
	 */
	public synchronized Client getClientByLicitatie(int idLicitatie) {
		int foundClient=0;
		int i=0;
		for(int id:idLicitatii) {
			if(idLicitatie==id) {
				foundClient=i;
				break;
			}
			i++;
		}
		return clienti.get(foundClient);
	}
	
	/**
	 * Informs client from given auction that he lost;
	 * Updates number of participations and commission calculator(if needed)
	 * The client and the corresponding id of auction are dropped from internal
	 * storage of broker
	 * @param idLicitatie id of a finished auction
	 */
	public synchronized void notifyLoss(int idLicitatie) {
		if(impliedInAuction(idLicitatie)) {
			int loser=0;
			int i=0;
			for(int id:idLicitatii) {
				if(id==idLicitatie) {
					loser=i;
					break;
				}
				i++;
			}
			
			System.out.println("Clientul "+clienti.get(loser).getNume()+" nu a castigat licitatia "+idLicitatie);
			clienti.get(loser).setNrParticipari(clienti.get(loser).getNrParticipari()+1);
			clienti.get(loser).updateCalculator();
			clienti.remove(loser);
			idLicitatii.remove(loser);
			
		}
	}
	
	/**
	 * Informs client from given auction that he won;
	 * Updates the number of participations and number of auctions won,
	 * as well as the commission calculator
	 * @param idLicitatie id of a finished auction
	 */
	public synchronized void notifyWin(int idLicitatie) {
		if(impliedInAuction(idLicitatie)) {
			int winner=0;
			int i=0;
			for(int id:idLicitatii) {
				if(id==idLicitatie) {
					winner=i;
					break;
				}
				i++;
			}
			System.out.println("Clientul "+clienti.get(winner).getNume()+" a castigat licitatia "+idLicitatie);
			
			clienti.get(winner).setNrParticipari(clienti.get(winner).getNrParticipari()+1);
			clienti.get(winner).setNrLicitatiiCastigate(clienti.get(winner).getNrLicitatiiCastigate()+1);
			clienti.get(winner).updateCalculator();
			clienti.remove(winner);
			idLicitatii.remove(winner);
			
		}
	}
	
	/**
	 * Informs client of given auction about current state of bids and
	 * mandates him to give a new bid which will be sent afterwards to
	 * the auction house;a broker with no participants in current auction
	 * will return 0
	 * @param oldBid previous bid of client
	 * @param topPriceOfCurrentRound max bid of current round
	 * @param maxPrice max price the client is willing to give
	 * @param licitatie the current auction
	 * @return new bid proposed by client
	 */
	public synchronized double notifyOnCurrentMax(double oldBid,double topPriceOfCurrentRound,double maxPrice,Licitatie licitatie) {
		int currentClient=-1;
		int i=0;
		for(int el:idLicitatii) {
			if(el==licitatie.getId()) {
				currentClient=i;
				break;
			}
			i++;
		}
		if(currentClient!=-1) {
			System.out.println("Momentan,in tranzactia "+licitatie.getId()+" s-a licitat maximum "+topPriceOfCurrentRound+
						".Cat vrei sa licitezi acum?");
			long currentId=Thread.currentThread().getId();//the client needs to know the thread id/auction to pick a strategy
			double newBid=clienti.get(currentClient).sendBid(oldBid,topPriceOfCurrentRound,maxPrice,currentId);
			System.out.println("Clientul " + clienti.get(currentClient).getNume() + 
					" liciteaza acum "+ newBid+" in tranzactia "+licitatie.getId());
			return newBid;
		}
		return 0;
	}
	
	/**
	 * Checks if broker has any clients in current auction with given id
	 * @param idLicitatie id of current auction
	 * @return true if broker has such clients,false otherwise
	 */
	public boolean impliedInAuction(int idLicitatie) {
		int currentClient=-1;
		for(int i=0;i<idLicitatii.size();i++) {
			if(idLicitatii.get(i)==idLicitatie) {
				currentClient=i;
				break;
			}
		}
		return currentClient!=-1;
	}
	
	/**
	 * Removes the won product from selling products list
	 * @param produseVanzare selling products list
	 * @param licitatie current auction
	 */
	public void getPrizeAndRemove(List<Produs> produseVanzare,Licitatie licitatie) {
		int idProdus=licitatie.getIdProdus();
		synchronized (produseVanzare) {
			for(Produs produs:produseVanzare) {
				if(produs.getId()==idProdus) {
					produseVanzare.remove(produs);
					break;
				}
			}
		}
	}
}
