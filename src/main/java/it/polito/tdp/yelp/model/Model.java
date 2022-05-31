package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph<User, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private List<User> utenti;
	
	public Model() {
		dao=new YelpDao();
	}
	
	public String creaGrafo(int recensioni, int anno) {
		grafo=new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.utenti=dao.getUsersPerRecensioni(recensioni);  //ogni volta viene ricreata
		Graphs.addAllVertices(this.grafo, utenti);
		
		for(User u1: this.utenti) {
			for(User u2: this.utenti) {
				if(!u1.equals(u2) && u1.getUserId().compareTo(u2.getUserId())<0) {  //la seconda condizione mi evita di controllare sia u1-u2 che u2-u1
					int peso=dao.calcolaPeso(u1, u2, anno);
					if(peso>0) {
						Graphs.addEdge(this.grafo, u1, u2, peso);
					}
				}
			}
		}
		
		return "Grafo creato con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi\n";
	}

	
	public List<User> getUtenti() {
		return this.utenti;
	}

	
	public List<User> utentiPiuSimili(User u) {
		int max=0;
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {   //this.grafo.edgesOf(u) dà gli archi collegati ad u 
			if(this.grafo.getEdgeWeight(e)>max) {
				max=(int)this.grafo.getEdgeWeight(e);
			}
		}
		
		List<User> result=new ArrayList<User>();
		for(DefaultWeightedEdge e: this.grafo.edgesOf(u)) {
			if((int)this.grafo.getEdgeWeight(e)==max) {
				User u2=Graphs.getOppositeVertex(this.grafo, e, u);
				result.add(u2);
			}
		}
		return result;
	}
	
	
}
