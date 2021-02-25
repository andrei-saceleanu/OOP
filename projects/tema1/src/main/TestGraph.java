package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import graf.Graph;


/**
 * Main class of the project.
 * Deals with IO operations and initializations.
 * @author Saceleanu Andrei
 *
 */
public class TestGraph {

	public static void main(String[] args) {
		BufferedReader reader;
		PrintWriter writer;
		Graph g;
		try {
			/* open reader and writer from and to files */
			reader=new BufferedReader(new FileReader("map.in"));
			writer=new PrintWriter(new FileWriter("map.out"));
			
			/*process the input line by line and store the parsed data in a Graph structure */
			String line=reader.readLine();
			String [] first=line.split(" ");
			int noEdges=Integer.parseInt(first[0]);
			int noVertices=Integer.parseInt(first[1]);
			g=new Graph(noEdges,noVertices,writer);
			for(int i=0;i<noEdges;i++) {
				line=reader.readLine();
				String [] tokens=line.split(" ");
				int start=Integer.parseInt(tokens[0].substring(1));
				int end=Integer.parseInt(tokens[1].substring(1));
				int cost=Integer.parseInt(tokens[2]);
				int size=Integer.parseInt(tokens[3]);
				g.addStreet(start, end, cost, size);
			}
			line=reader.readLine();
			/* Process commands given. 
			 * Update current restrictions and traffic jams.
			 * Find the shortest path from a start point to an end point.
			 */
			while(line!=null) {
				String [] tokens=line.split(" ");
				if(!tokens[0].equals("drive")) {
					int start=Integer.parseInt(tokens[1].substring(1));
					int end=Integer.parseInt(tokens[2].substring(1));
					int cost=Integer.parseInt(tokens[3]);
					g.addRestriction(tokens[0], start, end, cost);
				}else {
					int start=Integer.parseInt(tokens[2].substring(1));
					int end=Integer.parseInt(tokens[3].substring(1));
					g.drive(tokens[1], start, end);
				}
				line=reader.readLine();
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
