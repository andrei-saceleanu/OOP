package graf;

import java.util.ArrayList;
import java.util.List;

import restrictii.Ambuteiaj;
import restrictii.RGabarit;
import restrictii.RViteza;
import vehicule.VehicleFactory;
import vehicule.Vehicul;

/**
 * Edge class which represents a road on the given map.
 * It is described by a start point,an endpoint,
 * restrictions for weight(gabarit field) and speed
 * (max_speed field).
 * Also,it encapsulates a list of traffic jams existent on that street
 * @author Saceleanu Andrei
 *
 */
public class Strada {
	private int start,end;
	private  RViteza max_speed;
	private RGabarit gabarit;
	private List<Ambuteiaj> ambuteiaje;

	/**
	 * Default constructor
	 */
	public Strada() {}
	/**
	 * Constructor with parameters
	 * @param start	start point
	 * @param end	end point
	 * @param cost	restriction on speed
	 * @param limita	maximum weight
	 */
	public Strada(int start, int end, int cost, int limita) {
		this.start = start;
		this.end = end;
		this.max_speed=new RViteza(cost);
		this.gabarit=new RGabarit(limita);
		this.ambuteiaje=new ArrayList<Ambuteiaj>();
	}

	/**
	 * Computes the cost of the current street according to the
	 * given type of vehicle and existent traffic jams 
	 * @param type	type of vehicle on the road
	 * @return total cost of travelling on current street
	 */
	public int getCostByVehicle(String type) {
		int cost_ambuteiaje=ambuteiaje.stream().map(el -> el.getCost()).reduce(0, (a,b)->a+b);
		int cost_strada=max_speed.getCost_aditional();
		int cost_masina=-1;
		VehicleFactory vf=new VehicleFactory();
		Vehicul v=vf.getVehicle(type);
		cost_masina=v.getCost();
		return cost_strada*cost_masina+cost_ambuteiaje;
	}
	/**
	 * @return the start point
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start point
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * @return the end point
	 */
	public int getEnd() {
		return end;
	}
	/**
	 * @param end the end point
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	/**
	 * @return the max_speed
	 */
	public RViteza getMax_speed() {
		return max_speed;
	}
	/**
	 * @param max_speed the max_speed 
	 */
	public void setMax_speed(RViteza max_speed) {
		this.max_speed = max_speed;
	}
	/**
	 * @return the gabarit field - maximum weight
	 */
	public RGabarit getGabarit() {
		return gabarit;
	}
	/**
	 * @param gabarit the gabarit field - maximum weight
	 */
	public void setGabarit(RGabarit gabarit) {
		this.gabarit = gabarit;
	}
	/**
	 * @return the ambuteiaje field - list of traffic jams
	 */
	public List<Ambuteiaj> getAmbuteiaje() {
		return ambuteiaje;
	}
	/**
	 * @param ambuteiaje list of traffic jams
	 */
	public void setAmbuteiaje(List<Ambuteiaj> ambuteiaje) {
		this.ambuteiaje = ambuteiaje;
	}
	
}