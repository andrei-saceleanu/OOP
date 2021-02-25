package graf;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import my_pair.MyPair;
import my_pqueue.MyPriorityQueue;
import restrictii.Ambuteiaj;
import vehicule.VehicleFactory;
import vehicule.Vehicul;

/**
 * Graph class holds the internal representation of the roadmap given.
 * It is described by the number of edges(maximum),number of vertices and
 * current number of streets.
 * Every street is present in the list field streets
 * @author Saceleanu Andrei
 *
 */
public class Graph {
	private int noEdges,noVertices,currentEdges;
	private List<Strada> streets;
	private PrintWriter pw;
	/**
	 * Default Constructor
	 */
	public Graph() {}
	/**
	 * Constructor with parameters
	 * @param noEdges number of maximum edges for the map
	 * @param noVertices number of intersections/vertices
	 * @param pw print writer for drive method output
	 */
	public Graph(int noEdges,int noVertices,PrintWriter pw) {
		this.streets=new ArrayList<Strada>();
		this.currentEdges=0;
		this.pw=pw;
		this.setNoEdges(noEdges);
		this.setNoVertices(noVertices);
	}
	
	/**
	 * Adds street formed by parameters to the list and increments currentEdges
	 * @param start	start point
	 * @param end	end point
	 * @param cost	maximum speed on the street
	 * @param size	maximum weight for a vehicle travelling on that street
	 */
	public void addStreet(int start,int end,int cost,int size) {
		streets.add(new Strada(start, end, cost, size));
		this.currentEdges++;
	}
	
	/**
	 * Finds the street with start and end points given and adds restriction/traffic jam
	 * @param type	type of traffic jam
	 * @param start	start point
	 * @param end	end point
	 * @param cost	additional cost of traffic jam
	 */
	public void addRestriction(String type,int start,int end,int cost) {
		int index=-1;
		for(int i=0;i<currentEdges;i++) {//iterate over streets and find the appropriate one
			Strada s=streets.get(i);
			if(s.getStart()==start&&s.getEnd()==end) {
				index=i;
				break;
			}
		}
		streets.get(index).getAmbuteiaje().add(new Ambuteiaj(type, cost));//append traffic jam
	}
	
	/**
	 * Find the shortest path between endpoints given,for the particular type of vehicle
	 * given as parameter
	 * @param vehicle	type of vehicle on the road
	 * @param start	start point
	 * @param end	end point
	 */
	public void drive(String vehicle,int start,int end) {
		int [] dist=new int[noVertices];//array of distances
		int [] parent=new int[noVertices];//during Dijkstra traversal,will hold previous node
		for(int i=0;i<noVertices;i++) {
			dist[i]=Integer.MAX_VALUE;
		}
		MyPriorityQueue<MyPair> pq=new MyPriorityQueue<MyPair>();
		pq.add(new MyPair(0,start));//insert start node and initialize distance 0
		dist[start]=0;
		parent[start]=-1;//start point has no previous point
		VehicleFactory vf=new VehicleFactory();//get a factory and get a vehicle of appropriate type
		Vehicul ve=vf.getVehicle(vehicle);
		while(!pq.isEmpty()) {
			int u=pq.peek().getSecond();
			pq.poll();//for every node in queue ordered after distance from source
			/* iterate over incident edges and see if endpoint can be reached
			 * (maximum weight of vehicle is less than maximum weight available on the street)
			 * and if including this edge can reduce the distance.
			 * if so,enqueue endpoint and update parent and dist arrays
			 */
			Object[] s=streets.stream().filter(el -> el.getStart()==u).toArray();
			for(int i=0;i<s.length;i++) {
				int v=((Strada)s[i]).getEnd();
				int cost=((Strada)s[i]).getCostByVehicle(vehicle);
				if(dist[v]>dist[u]+cost&&ve.getGabarit()<=((Strada)s[i]).getGabarit().getLimita_gabarit()) {
					dist[v]=dist[u]+cost;
					pq.add(new MyPair(dist[v],v));
					parent[v]=u;
				}
			}
		}
		/*
		 * a final dist of Integer.MAX_VALUE means that the end cannot be reached,
		 * so we print null
		 */
		if(dist[end]==Integer.MAX_VALUE) {
			this.pw.printf("P%d P%d null\n", start,end);
		}else {
		/*
		 * otherwise,recover the path from parent array.
		 * starting from endpoint,the road is reversed so we use a stack to store and then
		 * print it to file.
		 */
			int curr=end;
			Stack<Integer> st=new Stack<Integer>();
			while(curr!=-1) {
				st.push(curr);
				curr=parent[curr];
			}
			while(!st.isEmpty()) {
				int element=st.pop();
				this.pw.printf("P%d ", element);
			}
			this.pw.printf("%d\n", dist[end]);
		}
	}
	/**
	 * @return the noEdges
	 */
	public int getNoEdges() {
		return noEdges;
	}
	/**
	 * @param noEdges the number of vertices to set
	 */
	public void setNoEdges(int noEdges) {
		this.noEdges = noEdges;
	}
	/**
	 * @return the noVertices
	 */
	public int getNoVertices() {
		return noVertices;
	}
	/**
	 * @param noVertices the number of vertices to set
	 */
	public void setNoVertices(int noVertices) {
		this.noVertices = noVertices;
	}
	
	
	
	
}
