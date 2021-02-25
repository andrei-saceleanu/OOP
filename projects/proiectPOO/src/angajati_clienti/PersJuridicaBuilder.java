package angajati_clienti;

import comisioane.ComisionC3;

/**
 * Client Builder designed for legal people
 * A built client will be set with type C3 commission
 * @author Saceleanu Andrei
 *
 */
public class PersJuridicaBuilder extends AbstractBuilder{
	public PersJuridicaBuilder() {
		client=new PersJuridica();
		client.setComisionAsociat(new ComisionC3());
	}
}
