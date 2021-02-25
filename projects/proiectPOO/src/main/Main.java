package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import java.sql.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import angajati_clienti.Administrator;
import angajati_clienti.Broker;
import angajati_clienti.Client;
import angajati_clienti.PersFizicaBuilder;
import angajati_clienti.PersJuridicaBuilder;
import casa_licitatii.CasaLicitatii;
import licitare_strategii.StrategyFactory;
import produse.Bijuterie;
import produse.Culori;
import produse.Mobila;
import produse.Produs;
import produse.ProdusBuilder;
import produse.Tablou;

/**
 * Main Class Of Auction Management Application
 * The auctions are performed inside an auction house(casaLicitatii),
 * with the help of a single administrator.
 * The clients can register to the auction house and they are
 * uniquely associated with a broker for every auction they opt
 * to participate. 
 * @author Saceleanu Andrei
 *
 */
public class Main {
	private static final int MAX_BROKERS=20;//maximum number of brokers available for auctions
	private static final int MAX_WAIT_MILIS=3000;//maximum milliseconds before writing summary to file
	public static void main(String [] args) {
		
		//singleton instances for auction house and administrator
		CasaLicitatii casaLicitatii=CasaLicitatii.getInstance();
		Administrator administrator=Administrator.getInstance();
		
		List<Broker> brokeri=new ArrayList<>();
		for(int i=0;i<MAX_BROKERS;i++) {
			brokeri.add(new Broker());
		}
		
		chooseDatabaseFormat(casaLicitatii, administrator);
		readCommands(casaLicitatii, brokeri);
		try {
			Thread.sleep(MAX_WAIT_MILIS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		
		casaLicitatii.getSummary().printSummary();
	}


	public static void chooseDatabaseFormat(CasaLicitatii casaLicitatii, Administrator administrator) {
		int response=JOptionPane.showConfirmDialog(null, "Which database do you want?(Yes for SQL,No for CSV)"
				,"Select an option...",JOptionPane.YES_NO_CANCEL_OPTION);
		if(response==JOptionPane.YES_OPTION) {
			loadProductsDatabaseSQL(casaLicitatii, administrator);
		}else if(response==JOptionPane.NO_OPTION) {
			loadProductsDatabase(casaLicitatii, administrator);
		}
	}

	
	/**
	 * Read commands line by line from a file and update the internal representation of
	 * auction house accordingly
	 * @param casaLicitatii auction house where the commands will take effect
	 * @param brokeri the list of brokers available to associate with clients
	 */
	public static void readCommands(CasaLicitatii casaLicitatii, List<Broker> brokeri) {
		try (BufferedReader bufferedReader=new BufferedReader(new FileReader(new File("commands.txt")))){
			String command=null;
			while((command=bufferedReader.readLine())!=null) {
				String [] tokens=command.split(" ");
				if(tokens[0].equals("registerClient")){//a command can be either a register or a bid proposal
					registerClient(casaLicitatii, tokens);
				}else if(tokens[0].equals("bid")) {
					Client client=casaLicitatii.getClientById(Integer.parseInt(tokens[1]));
					client.depuneSolicitare(brokeri, casaLicitatii, Integer.parseInt(tokens[2]), 
							Double.parseDouble(tokens[3]), StrategyFactory.getStrategy(tokens[4]));
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method adds a client to an auction house.This allows the client
	 * to participate in any future auction at that auction house
	 * @param casaLicitatii the auction house where the client registers
	 * @param tokens the elements which describe the client(Name,ID,Address)
	 */
	public static void registerClient(CasaLicitatii casaLicitatii, String[] tokens) {
		Client client=null;
		StringBuilder address = new StringBuilder();
		for(int i=4;i<tokens.length;i++) {//rejoin split tokens which make up the address
			address.append(tokens[i]);
			address.append(" ");
		}
		if(tokens[1].equals("fizic")) {//a client can be an individual or a legal person
			client=new PersFizicaBuilder().withId(Integer.parseInt(tokens[2]))
					.withLicitatiiCastigate(0)
					.withParticipari(0)
					.withNume(tokens[3])
					.withAdresa(address.toString()).build();
		}else {
			client=new PersJuridicaBuilder().withId(Integer.parseInt(tokens[2]))
					.withLicitatiiCastigate(0)
					.withParticipari(0)
					.withNume(tokens[3])
					.withAdresa(address.toString()).build();
		}
		casaLicitatii.getClienti().add(client);
	}
	
	
	/**
	 * Products are stored in a CSV Database.The method parses every record to construct
	 * the corresponding product and then this product is added to the auction house by the
	 * administrator.
	 * @param casaLicitatii auction house which owns the products from database
	 * @param administrator the administrator in charge of adding products to auction house
	 */
	public static void loadProductsDatabase(CasaLicitatii casaLicitatii, Administrator administrator) {
		FileReader fileReader;
		Iterable<CSVRecord> records;
		try {
			fileReader = new FileReader(new File("products.csv"));
			records=CSVFormat.DEFAULT.withHeader("id","tip","nume","an","pret_minim",
					"pret_vanzare","miscellaneous","auction_participants")
					.withFirstRecordAsHeader()
					.parse(fileReader);
			for(CSVRecord record:records) {
				Produs p = extractProductFromRecord(record);
				administrator.addProdus(casaLicitatii, p);
						
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadProductsDatabaseSQL(CasaLicitatii casaLicitatii,Administrator administrator) {
		String connectionUrl =
                "jdbc:sqlserver://DESKTOP-67S37F8\\SQLEXPRESS:1433;"
                        + "user=hello;password=hello;databaseName=products;";
		try(Connection connection = DriverManager.getConnection(connectionUrl);
				Statement st=connection.createStatement()){
            ResultSet rs=st.executeQuery("select * from [dbo].[Products]");
            while(rs.next()) {
            	Produs p = extractProductFromEntry(rs);
            	administrator.addProdus(casaLicitatii, p);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
	}


	public static Produs extractProductFromEntry(ResultSet rs) throws SQLException {
		Produs p=new ProdusBuilder(rs.getString(2)).withNume(rs.getString(3))
				.withAn(rs.getInt(4)).withId(rs.getInt(1)).withPretMinim(rs.getInt(5))
				.withPretVanzare(rs.getInt(6)).withParticipants(rs.getInt(8)).build();
		processMiscellaneousFieldSQL(rs, p);
		return p;
	}


	public static void processMiscellaneousFieldSQL(ResultSet rs, Produs p) throws SQLException {
		String [] misc=rs.getString(7).split(";");
		switch (rs.getString(2)) {
			case "tablou":
				((Tablou)p).setNumePictor(misc[0]);
				((Tablou)p).setMetodaPictare(Culori.getCulori(misc[1]));
				break;
			case "mobila":
				((Mobila)p).setTip(misc[0]);
				((Mobila)p).setMaterial(misc[1]);
				break;
			case "bijuterie":
				((Bijuterie)p).setMaterial(misc[0]);
				((Bijuterie)p).setPiatraPretioasa(Boolean.parseBoolean(misc[1]));
				break;
			default:
				throw new IllegalStateException();	
		}
	}

	/**
	 * Parse CSV record to internal representation of a product
	 * @param record an entry in the CSV database
	 * @return the extracted product
	 */
	public static Produs extractProductFromRecord(CSVRecord record) {
		Produs p=new ProdusBuilder(record.get("tip")).withNume(record.get("nume"))
				.withAn(Integer.parseInt(record.get("an")))
				.withId(Integer.parseInt(record.get("id")))
				.withPretMinim(Double.parseDouble(record.get("pret_minim")))
				.withPretVanzare(Double.parseDouble(record.get("pret_vanzare")))
				.withParticipants(Integer.parseInt(record.get("auction_participants")))
				.build();
		processMiscellaneousField(record, p);
		return p;
	}

	/**
	 * Depending on the type of the product,the Miscellaneous field in
	 * the CSV Database stores additional properties of products,
	 * specific to every known type.
	 * The method sets the corresponding fields in the product given as
	 * parameter. 
	 * @param record an entry in the CSV Database
	 * @param p the product whose miscellaneous fields have to be extracted
	 */
	public static void processMiscellaneousField(CSVRecord record, Produs p) {
		String [] misc=record.get("miscellaneous").split(";");
		switch (record.get("tip")) {
			case "tablou":
				((Tablou)p).setNumePictor(misc[0]);
				((Tablou)p).setMetodaPictare(Culori.getCulori(misc[1]));
				break;
			case "mobila":
				((Mobila)p).setTip(misc[0]);
				((Mobila)p).setMaterial(misc[1]);
				break;
			case "bijuterie":
				((Bijuterie)p).setMaterial(misc[0]);
				((Bijuterie)p).setPiatraPretioasa(Boolean.parseBoolean(misc[1]));
				break;
			default:
				throw new IllegalStateException();
		}
	}
}
