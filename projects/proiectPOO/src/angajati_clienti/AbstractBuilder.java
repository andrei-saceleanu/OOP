package angajati_clienti;

/**
 * Client Builder which provides a common contract for
 * individual builder(PersFizicaBuilder) and legal person
 * builder(PersJuridicaBuilder)
 * @author Saceleanu Andrei
 *
 */
public abstract class AbstractBuilder {
	protected Client client;
	
	public Client build() {
		return client;
	}
	public AbstractBuilder withId(int id) {
		client.setId(id);
		return this;
	}
	public AbstractBuilder withNume(String name) {
		client.setNume(name);
		return this;
	}
	public AbstractBuilder withAdresa(String adresa) {
		client.setAdresa(adresa);
		return this;
	}
	public AbstractBuilder withParticipari(int nrParticipari) {
		client.setNrParticipari(nrParticipari);
		return this;
	}
	public AbstractBuilder withLicitatiiCastigate(int nrLicitatiiCastigate) {
		client.setNrLicitatiiCastigate(nrLicitatiiCastigate);
		return this;
	}
}
