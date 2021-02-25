package casa_licitatii_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import angajati_clienti.Administrator;
import angajati_clienti.Broker;
import angajati_clienti.Client;
import angajati_clienti.PersFizicaBuilder;
import casa_licitatii.CasaLicitatii;
import comisioane.ComisionC1;
import comisioane.ComisionC2;
import licitare_strategii.CautiousBid;
import licitare_strategii.EagerBid;
import licitare_strategii.MustHaveItBid;
import main.Main;
import produse.Produs;
import produse.ProdusBuilder;

public class CasaTest {
	private CasaLicitatii casaLicitatii;
	private Administrator administrator;
	private List<Broker> brokeri;
	private static int MAX_BROKERS=20;
	@Before
	public void setup() {
		casaLicitatii=CasaLicitatii.getInstance();
		administrator=Administrator.getInstance();
		brokeri=new ArrayList<>(MAX_BROKERS);
		for(int i=0;i<MAX_BROKERS;i++) {
			brokeri.add(new Broker());
		}
	}
	
	@Test
	public void testAddProduct() {
		Produs produs=new ProdusBuilder("tablou")
				.withId(1).withNume("x").withAn(2000)
				.withPretMinim(300).withPretVanzare(400).build();
		administrator.addProdus(casaLicitatii, produs);
		Assert.assertTrue(casaLicitatii.getProduseVanzare().size()==1&&casaLicitatii.getProduseVanzare().get(0).getId()==1);
	}
	
	@Test
	public void testAddClient() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		casaLicitatii.getClienti().add(client);
		Assert.assertEquals(true, casaLicitatii.getClienti().size()==1&&casaLicitatii.getClienti().get(0).getNrParticipari()==0);
	}
	
	@Test
	public void runOneBid() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		Client client2=new PersFizicaBuilder().withId(200)
				.withNume("Name2").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("Y").build();
		Produs produs=new ProdusBuilder("tablou")
				.withId(1).withNume("x").withAn(2000)
				.withPretMinim(100).withPretVanzare(200).withParticipants(2).build();
		administrator.addProdus(casaLicitatii, produs);
		
		client.depuneSolicitare(brokeri, casaLicitatii, 1, 150, new CautiousBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 1, 175, new EagerBid());
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		Assert.assertEquals(1, client2.getNrLicitatiiCastigate());
	}
	
	@Test
	public void testConnectionToDatabase() {
		Main.loadProductsDatabase(casaLicitatii, administrator);
		Assert.assertTrue(casaLicitatii.getProduseVanzare().size()==23);
	}
	
	@Test
	public void testConnectionToSQLDatabase() {
		Main.loadProductsDatabaseSQL(casaLicitatii, administrator);
		Assert.assertTrue(casaLicitatii.getProduseVanzare().size()==23);
	}
	
	@Test
	public void testCommandFromFileRegisterClient() {
		try(BufferedReader bufferedReader=new BufferedReader(new FileReader(new File("commands.txt")))) {
			String command=bufferedReader.readLine();
			String [] tokens=command.split(" ");
			Main.registerClient(casaLicitatii, tokens);
			Assert.assertEquals(1, casaLicitatii.getClienti().size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testConcurrentAuctions() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		Client client2=new PersFizicaBuilder().withId(200)
				.withNume("Name2").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("Y").build();
		Produs produs=new ProdusBuilder("tablou")
				.withId(122).withNume("x").withAn(2000)
				.withPretMinim(100).withPretVanzare(200).withParticipants(2).build();
		
		Produs produs2=new ProdusBuilder("mobila")
				.withId(222).withNume("y").withAn(2003)
				.withPretMinim(200).withPretVanzare(400).withParticipants(2).build();
		administrator.addProdus(casaLicitatii, produs);
		administrator.addProdus(casaLicitatii, produs2);
		
		client.depuneSolicitare(brokeri, casaLicitatii, 122, 150, new CautiousBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 122, 175, new EagerBid());
		client.depuneSolicitare(brokeri, casaLicitatii, 222, 300, new MustHaveItBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 222, 400, new CautiousBid());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		Assert.assertEquals(2, client.getNrParticipari());
		Assert.assertEquals(1, client.getNrLicitatiiCastigate());
		Assert.assertEquals(1, client2.getNrLicitatiiCastigate());
	}
	
	@Test
	public void testComisionCalculator() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		Client client2=new PersFizicaBuilder().withId(200)
				.withNume("Name2").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("Y").build();
		Produs produs=new ProdusBuilder("tablou")
				.withId(1).withNume("x").withAn(2000)
				.withPretMinim(100).withPretVanzare(200).withParticipants(2).build();
		administrator.addProdus(casaLicitatii, produs);
		
		client.depuneSolicitare(brokeri, casaLicitatii, 1, 150, new CautiousBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 1, 175, new EagerBid());
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		Assert.assertFalse(client.getComisionAsociat() instanceof ComisionC2);
		Assert.assertTrue(client.getComisionAsociat() instanceof ComisionC1);
	}
	
	@Test
	public void testNoStartWithoutRequiredNumberOfParticipants() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		Produs produs=new ProdusBuilder("tablou")
				.withId(1).withNume("x").withAn(2000)
				.withPretMinim(100).withPretVanzare(200).withParticipants(3).build();
		administrator.addProdus(casaLicitatii, produs);
		
		client.depuneSolicitare(brokeri, casaLicitatii, 1, 150, new CautiousBid());
		Assert.assertFalse(casaLicitatii.getLicitatiiActive().get(0).getAssociatedThread().isAlive());
	}
	
	@Test
	public void testStartWithEnoughParticipants() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		Client client2=new PersFizicaBuilder().withId(200)
				.withNume("Name2").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("Y").build();
		Produs produs=new ProdusBuilder("tablou")
				.withId(1).withNume("x").withAn(2000)
				.withPretMinim(100).withPretVanzare(200).withParticipants(2).build();
		administrator.addProdus(casaLicitatii, produs);
		
		client.depuneSolicitare(brokeri, casaLicitatii, 1, 150, new CautiousBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 1, 175, new EagerBid());
		Assert.assertTrue(casaLicitatii.getLicitatiiActive().get(0).getAssociatedThread().isAlive());
	}
	
	@Test
	public void testParticipantWithMoreWinsReceivesTheProduct() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		Client client2=new PersFizicaBuilder().withId(200)
				.withNume("Name2").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("Y").build();
		Produs produs=new ProdusBuilder("tablou")
				.withId(122).withNume("x").withAn(2000)
				.withPretMinim(100).withPretVanzare(200).withParticipants(2).build();
		
		Produs produs2=new ProdusBuilder("mobila")
				.withId(222).withNume("y").withAn(2003)
				.withPretMinim(200).withPretVanzare(400).withParticipants(2).build();
		administrator.addProdus(casaLicitatii, produs);
		administrator.addProdus(casaLicitatii, produs2);
		
		client.depuneSolicitare(brokeri, casaLicitatii, 122, 150, new CautiousBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 122, 300, new EagerBid());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		client.depuneSolicitare(brokeri, casaLicitatii, 222, 500, new MustHaveItBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 222, 500, new MustHaveItBid());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		Assert.assertEquals(2, client2.getNrLicitatiiCastigate());
		
	}
	@Test
	public void testProductSellingPriceSet() {
		Client client=new PersFizicaBuilder().withId(100)
				.withNume("Name").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("X").build();
		Client client2=new PersFizicaBuilder().withId(200)
				.withNume("Name2").withParticipari(0).withLicitatiiCastigate(0)
				.withAdresa("Y").build();
		Produs produs=new ProdusBuilder("tablou")
				.withId(1).withNume("x").withAn(2000)
				.withPretMinim(100).withPretVanzare(200).withParticipants(2).build();
		administrator.addProdus(casaLicitatii, produs);
		
		client.depuneSolicitare(brokeri, casaLicitatii, 1, 150, new CautiousBid());
		client2.depuneSolicitare(brokeri, casaLicitatii, 1, 175, new EagerBid());
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		Assert.assertEquals(175, casaLicitatii.getProduseVandute().get(0).getPretVanzare(),0.001);
	}
	
	@After
	public void destroy() {
		casaLicitatii.getClienti().clear();
		casaLicitatii.getProduseVanzare().clear();
		casaLicitatii.getLicitatiiActive().clear();
		casaLicitatii.getProduseVandute().clear();
		administrator=null;
		brokeri=null;
	}
}
