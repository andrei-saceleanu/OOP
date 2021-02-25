package angajati_clienti;

import comisioane.ComisionC1;

/**
 * Client Builder designed for individuals
 * A built client will be set with type C1 commission
 * @author Saceleanu Andrei
 *
 */
public class PersFizicaBuilder extends AbstractBuilder{
	public PersFizicaBuilder() {
		client=new PersFizica();
		client.setComisionAsociat(new ComisionC1());
	}
}
