package it.polimi.ingsw.cg25.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polimi.ingsw.cg25.model.dashboard.topological.City;

/**
 * 
 * @author Giovanni
 *
 */
public class GraphParser implements Parser {

	/**
	 * List of cities to use as nodes
	 */
	private final List<City> cities;
	/**
	 * The undirected graph created by the parser
	 */
	private final UndirectedGraph<City, DefaultEdge> roadNetwork;
	
	/**
	 * GraphParser class constructor
	 * @param cities the cities used as nodes
	 */
	public GraphParser(List<City> cities) {
		this.cities = cities;
		roadNetwork = new SimpleGraph<>(DefaultEdge.class);
	}
	
	@Override
	public void parseLineByLine(InputStreamReader inStrRdr) throws IOException {
		BufferedReader br = new BufferedReader(inStrRdr);
		String line;
		
		//Aggiungi tutte le città come nodi del grafo
		for(City c : cities) {
			roadNetwork.addVertex(c);
		}
		
		//Crea i collegamenti
		while((line = br.readLine()) != null) {
			if(!(line.startsWith("#"))) {
				//Elimino il terminatore
				String newLine = line.replaceAll(";", "");
				//Array dei due vertici del collegamento
				String[] vertex = newLine.split(",");
				try {
					roadNetwork.addEdge(cityFromName(vertex[0]), cityFromName(vertex[1]));
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * The method return a city from its name
	 * @param name the name of the city
	 * @return a City object
	 * @throws Exception if the collection of cities does not contain the specified one
	 */
	private City cityFromName(String name) {
		for (int i = 0; i < this.cities.size(); i++) {
			if (this.cities.get(i).getName().equals(name.replaceAll(" ","")))
				return this.cities.get(i);
		}
		throw new NoSuchElementException("the city ".concat(name).concat(" is not in the cities").concat(cities.toString()));
	}
	
	/**
	 * @return an undirected graph with all the specified connections among the cities
	 */
	public UndirectedGraph<City, DefaultEdge> getRoadNetwork() {
		return roadNetwork;
	}
	
}
