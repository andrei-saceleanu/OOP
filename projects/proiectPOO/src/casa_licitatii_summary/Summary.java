package casa_licitatii_summary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Summary of a session of auctions which will be written to a file
 * It is a singleton class which contains a list of entries
 * @author Saceleanu Andrei
 *
 */
public class Summary {
	private List<SummaryEntry> entries;
	private static Summary iNSTANCE=null;
	private Summary() {
		entries=new ArrayList<>();
	}
	
	public static Summary getInstance() {
		if(iNSTANCE==null)
			iNSTANCE=new Summary();
		return iNSTANCE;
	}
	
	public List<SummaryEntry> getEntries() {
		return entries;
	}
	
	/**
	 * Print summary to file
	 */
	public void printSummary() {
		PrintWriter printWriter;
		try {
			entries.sort((s1 ,s2) -> s1.getIdLicitatie()-s2.getIdLicitatie());
			printWriter = new PrintWriter(new File("results.txt"));
			entries.stream().forEach(printWriter::println);
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
