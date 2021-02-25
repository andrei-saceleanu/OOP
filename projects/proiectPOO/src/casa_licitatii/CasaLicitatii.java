package casa_licitatii;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


import angajati_clienti.Broker;
import angajati_clienti.Client;
import casa_licitatii_summary.Summary;
import casa_licitatii_summary.SummaryEntry;
import licitare_strategii.BidStrategy;
import my_pair.MyTuple;
import produse.Produs;

/**
 * The main environment where the auctions are performed,
 * brokers are associated to clients for every auction
 * and the summary of the session is written to file
 * @author Saceleanu Andrei
 *
 */
public class CasaLicitatii{
	private static CasaLicitatii iNSTANCE=null;
	private List<Produs> produseVanzare;
	private List<Produs> produseVandute;
	private List<Client> clienti;
	private List<Licitatie> licitatiiActive;
	private Summary summary;
	
	private CasaLicitatii() {
		produseVanzare=new ArrayList<>();
		clienti=new ArrayList<>();
		licitatiiActive=new ArrayList<>();
		produseVandute=new ArrayList<>();
		summary=Summary.getInstance();
	}
	public static CasaLicitatii getInstance() {
		if(iNSTANCE==null)
			iNSTANCE=new CasaLicitatii();
		return iNSTANCE;
	}
	
	/**
	 * Gets product from produseVanzare list with corresponding id
	 * @param idProdus id of product searched
	 * @return the product with the id given,if any
	 */
	public synchronized Produs getProdusById(int idProdus) {
		return produseVanzare.stream().filter(p -> p.getId()==idProdus)
				.findFirst().orElse(new Produs());
	}
	
	/**
	 * Gets client from clienti list with corresponding id
	 * @param idClient id of client searched
	 * @return the client with id given,if any
	 */
	public Client getClientById(int idClient) {
		return clienti.stream().filter(p -> p.getId()==idClient)
				.findFirst().orElse(null);
	}
	
	/**
	 * Performs an auction and then informs the brokers about the results.
	 * Further,the brokers notify the clients who participated in this auction if
	 * they won or not. 
	 * @param brokeri the list of brokers who mediate the communication between the
	 * 			auction house and the clients
	 * @param licitatie the auction to be performed
	 */
	public void runLicitatie(List<Broker> brokeri,Licitatie licitatie) {
		double topPricePerRound = 0;
		
		for(int i = 0 ;i < licitatie.getNrPasiMaxim(); i++) {
			runOneRoundOfAuction(brokeri, licitatie, i);
		}
		
		topPricePerRound = calculateTopPriceOfCurrentRound(licitatie);
		final double winningPrice=topPricePerRound;
		
		//there is a winner if winningPrice is greater than minPrice of product auctioned
		if(minPriceOfProductFulfilled(licitatie, winningPrice)) {
			int winningBroker = calculateWinningBroker(brokeri, licitatie, winningPrice);
			appendToAuctionsSummary(brokeri, licitatie, winningPrice, winningBroker);//update summary
			informBrokersWithWin(brokeri, licitatie, winningBroker);//a broker has a client who won
			synchronized (produseVanzare) {//copy product from selling to sold products
				Produs prize=getProdusById(licitatie.getIdProdus());
				prize.setPretVanzare(winningPrice);
				produseVandute.add(prize);
			}
			brokeri.get(winningBroker).getPrizeAndRemove(produseVanzare,licitatie);//remove from "selling" the product
		}else {
			summary.getEntries().add(new SummaryEntry("NoWinner", licitatie.getId(), licitatie.getIdProdus(), -1, -1));
			informBrokersWithoutWin(brokeri, licitatie);//everybody is informed that they lost
		}
		
	}
	
	/**
	 * The result of every auction is appended to a summary.This method takes an auction
	 * and deals with this operation.
	 * @param brokeri the list of brokers available
	 * @param licitatie the auction whose results are recorded in summary
	 * @param winningPrice the final selling price of product
	 * @param winningBroker the broker who has to tell to auction house which client of him won
	 */
	public synchronized void appendToAuctionsSummary(List<Broker> brokeri, Licitatie licitatie, final double winningPrice,
			int winningBroker) {
		summary.getEntries().add(new SummaryEntry(brokeri.get(winningBroker).getClientByLicitatie(licitatie.getId()).getNume(),
				licitatie.getId(), licitatie.getIdProdus(), brokeri.get(winningBroker).getClientByLicitatie(licitatie.getId()).getId(),
				winningPrice));
	}
	
	/**
	 * The method makes every broker to tell the client who participated in
	 * auction licitatie,if any,that he/she did not win
	 * @param brokeri the list of brokers available
	 * @param licitatie a finished auction
	 */
	public void informBrokersWithoutWin(List<Broker> brokeri, Licitatie licitatie) {
		for(int j=0;j<brokeri.size();j++) {
			brokeri.get(j).notifyLoss(licitatie.getId());
		}
	}
	
	
	/**
	 * The method makes every broker to tell the client who participated in
	 * auction licitatie what the outcome was.All but one broker inform
	 * their clients that they lost the auction.
	 * @param brokeri the list of brokers available
	 * @param licitatie a finished auction
	 * @param winningBroker  the broker who has the winning client of auction
	 */
	public void informBrokersWithWin(List<Broker> brokeri, Licitatie licitatie, int winningBroker) {
		brokeri.get(winningBroker).notifyWin(licitatie.getId());
		for(int j=0;j<brokeri.size();j++) {
			if(j!=winningBroker) {
				brokeri.get(j).notifyLoss(licitatie.getId());
			}
		}
	}
	
	/**
	 * Performs one round of auction licitatie:
	 * calculates the best bid so far,informs the brokers who further 
	 * tell the clients to give new bids;these bids/values will be
	 * processed in a later round.
	 * @param brokeri list of brokers available
	 * @param licitatie	auction in progress
	 * @param i index of current round
	 */
	public void runOneRoundOfAuction(List<Broker> brokeri, Licitatie licitatie, int i) {
		double topPricePerRound;
		System.out.println( "RUNDA "+ (i + 1) + ":licitatia "+licitatie.getId() );
		topPricePerRound = calculateTopPriceOfCurrentRound(licitatie);
		List<MyTuple<Integer, Double, Double>> oldBids = storeOldBids(licitatie);//store before deleting
		licitatie.getPropuneriCurente().clear();
		for(int j = 0 ; j < oldBids.size(); j++) {
			double newBid = notifyAuctionStateToBroker(brokeri, licitatie, topPricePerRound, oldBids, j);
			storeNewBid(licitatie, oldBids, j, newBid);	
		}
	}
	
	/**
	 * Identifies the winningBroker based on winningPrice and the commission associated
	 * with each client.In case of equality after extracting the commission,the winner
	 * is the one with more auctions won so far
	 * @param brokeri the list of brokers available
	 * @param licitatie the auction in progress
	 * @param winningPrice maximum bid at the end of the last round of auction
	 * @return
	 */
	public synchronized int calculateWinningBroker(List<Broker> brokeri, Licitatie licitatie, final double winningPrice) {
		List<Integer> winningBrokers;
		winningBrokers=licitatie.getPropuneriCurente().stream()
				.filter(element -> element.getSnd()==winningPrice).map(MyTuple::getFst)
				.collect(Collectors.toList());
		int winningBroker=winningBrokers.get(0);
		int maxWon=brokeri.get(winningBroker).getClientByLicitatie(licitatie.getId()).getNrLicitatiiCastigate();
		double maxTrueBidWithoutComision = calculateMaxBidWithoutComision(brokeri, licitatie, winningPrice,
				winningBrokers, winningBroker);
		
		for(int index:winningBrokers) {
			double bidWithoutComision=winningPrice-brokeri.get(index).getClientByLicitatie(licitatie.getId())
					.getComisionAsociat().calculeaza(winningPrice);
			if(Double.doubleToLongBits(bidWithoutComision)!=Double.doubleToLongBits(maxTrueBidWithoutComision)) {
				continue;
			}
			int nrCastigate=brokeri.get(index).getClientByLicitatie(licitatie.getId()).getNrLicitatiiCastigate();
			if(nrCastigate>maxWon) {
				maxWon=nrCastigate;
				winningBroker=index;
			}
		}
		return winningBroker;
	}
	
	/**
	 * From the brokers whose clients had an equal bid with the maximum one,the method 
	 * extracts the commission given to broker to find out the true amount of money given to
	 * auction house.
	 * @param brokeri the list of available brokers
	 * @param licitatie the current auction
	 * @param winningPrice maximum bid offered in auction
	 * @param winningBrokers brokers whose clients tied the maximum bid
	 * @param winningBroker first broker from winningBrokers
	 * @return
	 */
	public double calculateMaxBidWithoutComision(List<Broker> brokeri, Licitatie licitatie, final double winningPrice,
			List<Integer> winningBrokers, int winningBroker) {
		double maxTrueBidWithoutComision=winningPrice-brokeri.get(winningBroker).getClientByLicitatie(licitatie.getId())
				.getComisionAsociat().calculeaza(winningPrice);
		for(int index:winningBrokers) {
			double bidWithoutComision=winningPrice-brokeri.get(index).getClientByLicitatie(licitatie.getId())
					.getComisionAsociat().calculeaza(winningPrice);
			if(Double.doubleToLongBits(bidWithoutComision)>Double.doubleToLongBits(maxTrueBidWithoutComision)) {
				maxTrueBidWithoutComision=bidWithoutComision;
			}
		}
		return maxTrueBidWithoutComision;
	}
	
	
	/**
	 * An auction is won if the max bid is greater than the minPrice of product sold.
	 * The method verifies if the condition is met.
	 * @param licitatie the current auction
	 * @param winningPrice maximum bid offered in auction
	 * @return true if the auction has a winner
	 */
	public boolean minPriceOfProductFulfilled(Licitatie licitatie, final double winningPrice) {
		return winningPrice>=this.getProdusById(licitatie.getIdProdus()).getPretMinim();
	}
	
	
	/**
	 * Append a new bid proposal to the current auction's list
	 * @param licitatie the current auction
	 * @param oldBids the previous list of bids
	 * @param j the corresponding index in oldBids
	 * @param newBid the new bid given by client
	 */
	public void storeNewBid(Licitatie licitatie, List<MyTuple<Integer, Double, Double>> oldBids, int j, double newBid) {
		licitatie.getPropuneriCurente().add(new MyTuple<>(oldBids.get(j).getFst(), newBid, oldBids.get(j).getTrd()));
	}
	
	/**
	 * Inform the broker that his client for the current auction,if any,has to give
	 * a new bid;the client can make a decision depending on max bid so far or his
	 * previous bids
	 * @param brokeri the list of available brokers
	 * @param licitatie the current auction
	 * @param topPricePerRound max bid so far
	 * @param oldBids previous bids of clients
	 * @param j the current broker to be informed
	 * @return the new bid proposed by the broker and taken from client
	 */
	public double notifyAuctionStateToBroker(List<Broker> brokeri, Licitatie licitatie, double topPricePerRound,
			List<MyTuple<Integer, Double, Double>> oldBids, int j) {
		return brokeri.get(oldBids.get(j).getFst())
				.notifyOnCurrentMax(oldBids.get(j).getSnd(),topPricePerRound,oldBids.get(j).getTrd(),licitatie);
	}
	
	/**
	 * Stores previous bids before requesting new ones
	 * @param licitatie the current auction
	 * @return saved previous bids
	 */
	public List<MyTuple<Integer, Double, Double>> storeOldBids(Licitatie licitatie) {
		return licitatie.getPropuneriCurente().stream().collect(Collectors.toList());
	}
	
	/**
	 * Calculates max bid for a round
	 * @param licitatie the current auction
	 * @return max bid so far
	 */
	public double calculateTopPriceOfCurrentRound(Licitatie licitatie) {
		double topPricePerRound;
		topPricePerRound=licitatie.getPropuneriCurente().stream()
				.max(Comparator.comparing(MyTuple::getSnd)).orElse(null).getSnd();
		return topPricePerRound;
	}
	
	/**
	 * A client from the database of clients registers to participate in an auction
	 * for a product with idProdus.The auction house associates the client with an
	 * available broker(one who has not a client for the same auction).
	 * The client provides the maximum price he is willing to give as well as 
	 * a strategy to propose new bids
	 * @param client the client who wants to register
	 * @param brokeri the list of available brokers to pair with
	 * @param idProdus the id of the product requested
	 * @param pretMaxim the max price the client is willing to give
	 * @param strategy the strategy which the client will use for this auction
	 */
	public synchronized void acceptClient(Client client,List<Broker> brokeri,int idProdus,double pretMaxim,BidStrategy strategy) {
		clienti.add(client);
		int randomBroker=0;
		Licitatie licitatie=null;
		for(Licitatie l:licitatiiActive) {//search if an auction for current product is already placed
			if(l.getIdProdus()==idProdus) {
				licitatie=l;
				break;
			}
		}
		if(licitatie==null) {//if not,construct auction and add client
			int idLicitatie=new Random().nextInt(10000);
			int participants=this.getProdusById(idProdus).getAuctionParticipants();
			Licitatie licitatieNoua=new Licitatie(idLicitatie, participants, idProdus, new Random().nextInt(10)+10);
			randomBroker = assignBrokerForFirstClient(client, brokeri, licitatieNoua);
			initialConstructAuction(client, brokeri, idProdus, pretMaxim, strategy, randomBroker, licitatieNoua);
		}else {//if the auction was found,just update with new client and start if there are enough participants
			randomBroker = assignBrokerForOtherThanFirst(client, brokeri, licitatie);
			updateAuctionWithNewClient(client, idProdus, pretMaxim, strategy, randomBroker, licitatie);
			if(licitatie.getNrParticipanti()==licitatie.getPropuneriCurente().size()) {
				licitatie.getAssociatedThread().start();
			}
		}
	}
	
	
	/**
	 * An existing auction receives a new participant
	 * @param client the new participant
	 * @param idProdus the product of current auction
	 * @param pretMaxim max price the client is willing to give
	 * @param strategy the strategy of current client for this auction
	 * @param randomBroker the broker paired with current client
	 * @param licitatie the current auction
	 */
	public void updateAuctionWithNewClient(Client client, int idProdus, double pretMaxim, BidStrategy strategy,
			int randomBroker, Licitatie licitatie) {
		licitatie.getPropuneriCurente()
		.add(new MyTuple<>(randomBroker,client.propuneStartingPrice(getProdusById(idProdus),strategy), pretMaxim));
		client.getStrategyForEachAuction().put(licitatie.getAssociatedThread().getId(), strategy);
	}
	
	/**
	 * Set up a new auction
	 * @param client first client to register for this auction
	 * @param brokeri the list of brokers available
	 * @param idProdus the id of requested product
	 * @param pretMaxim the max price the client is willing to give
	 * @param strategy the strategy of client for this new auction
	 * @param randomBroker the broker paired with current client
	 * @param licitatieNoua the new auction
	 */
	public void initialConstructAuction(Client client, List<Broker> brokeri, int idProdus, double pretMaxim,
			BidStrategy strategy, int randomBroker, Licitatie licitatieNoua) {
		licitatieNoua.getPropuneriCurente()
		.add(new MyTuple<>(randomBroker,client.propuneStartingPrice(getProdusById(idProdus),strategy), pretMaxim));
		licitatieNoua.setAssociatedThread(new Thread(new ParallelTask(this, brokeri, licitatieNoua)));
		client.getStrategyForEachAuction().put(licitatieNoua.getAssociatedThread().getId(),strategy);
		licitatiiActive.add(licitatieNoua);
	}
	
	/**
	 * Searches randomly for an available broker to pair with client
	 * (a broker who does not already have a client for the auction licitatie)
	 * @param client the current client
	 * @param brokeri the list of possible brokers to pair with
	 * @param licitatie the current auction
	 * @return the index of available broker
	 */
	public int assignBrokerForOtherThanFirst(Client client, List<Broker> brokeri, Licitatie licitatie) {
		int randomBroker;
		randomBroker = generateRandomAvailableBroker(brokeri, licitatie);
		brokeri.get(randomBroker).getClienti().add(client);
		brokeri.get(randomBroker).getIdLicitatii().add(licitatie.getId());
		return randomBroker;
	}
	
	/**
	 * For first client,every broker is available so the pairing does not need retrying
	 * @param client the current client
	 * @param brokeri the list of available brokers
	 * @param licitatieNoua the current auction
	 * @return the index of associated broker to client
	 */
	public int assignBrokerForFirstClient(Client client, List<Broker> brokeri, Licitatie licitatieNoua) {
		int randomBroker;
		randomBroker=new Random().nextInt(brokeri.size());
		brokeri.get(randomBroker).getClienti().add(client);
		brokeri.get(randomBroker).getIdLicitatii().add(licitatieNoua.getId());
		return randomBroker;
	}
	
	/**
	 * Generate the index for other than first clients
	 * @param brokeri the list of possible brokers to pair with
	 * @param licitatie the current auction
	 * @return the index of paired broker
	 */
	public int generateRandomAvailableBroker(List<Broker> brokeri, Licitatie licitatie) {
		int randomBroker;
		int randomIndex=new Random().nextInt(brokeri.size());
		while(brokeri.get(randomIndex).getIdLicitatii().contains(licitatie.getId())) {
			randomIndex=new Random().nextInt(brokeri.size());
		}
		randomBroker=randomIndex;
		return randomBroker;
	}
	
	public List<Produs> getProduseVanzare() {
		return produseVanzare;
	}
	
	public List<Client> getClienti() {
		return clienti;
	}
	
	public List<Licitatie> getLicitatiiActive() {
		return licitatiiActive;
	}
	
	public List<Produs> getProduseVandute() {
		return produseVandute;
	}
	
	public Summary getSummary() {
		return summary;
	}
	
	
}
