package casa_licitatii_summary;

/**
 * A summary entry holds the essential information about a finished auction,such as
 * the winner's name and id,the auction id and the corresponding product id plus
 * the final winning price
 * @author Saceleanu Andrei
 *
 */
public class SummaryEntry {
	private String winnerName;
	private int idLicitatie;
	private int idProdus;
	private int idWinner;
	private double winningPrice;
	public SummaryEntry(String winnerName, int idLicitatie, int idProdus, int idWinner, double winningPrice) {
		this.winnerName = winnerName;
		this.idLicitatie = idLicitatie;
		this.idProdus = idProdus;
		this.idWinner = idWinner;
		this.winningPrice = winningPrice;
	}
	@Override
	public String toString() {
		return "SummaryEntry [winnerName=" + winnerName + ", idLicitatie=" + idLicitatie + ", idProdus=" + idProdus
				+ ", idWinner=" + idWinner + ", winningPrice=" + winningPrice + "]";
	}
	public int getIdLicitatie() {
		return idLicitatie;
	}
	
}
