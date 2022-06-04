package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulatore {
	
	//Dati in ingresso
	private int x1;
	private int x2;
	
	//Dati in uscita
	private List<Giornalista> giornalisti; //all'interno contiene il numero di persone intervistate
	private int numeroGiorni;
	
	//Modello del mondo --> mi serve sapere che utenti ho già intervistato
	private Set<User> intervistati; 
	private Graph<User, DefaultWeightedEdge> grafo;
	
	
	//Coda degli eventi
	private PriorityQueue<Event> coda;
	
	
	public Simulatore(Graph<User, DefaultWeightedEdge> grafo) {
		this.grafo=grafo;
	}
	
	public void init(int x1, int x2) {
		this.x1=x1;
		this.x2=x2;
		
		//Inizializzo i valori che servono per la simulazione
		this.intervistati=new HashSet<User>();
		this.numeroGiorni=0;
		this.giornalisti=new ArrayList<Giornalista>();
		for(int id=0; id<this.x1; id++) {
			this.giornalisti.add(new Giornalista(id));
		}
		
		//pre-carico la coda
		for(Giornalista g: this.giornalisti) {
			User intervistato= selezionaIntervistato(this.grafo.vertexSet());  //seleziono l'intervistato tra tutti quelli possibili
			
			this.intervistati.add(intervistato); //lo aggiungo agli intervistati
			g.incrementaNumerointervistati();
			
			//aggiungo l'evento alla coda
			this.coda.add(new Event(1, EventType.DA_INTERVISTARE, intervistato, g));
		}
		
	}
	
	public void run() {
		
		//condizioni di terminazione
		while(!this.coda.isEmpty() && this.intervistati.size()<x2) {
			
			//elaborazione degli eventi
			Event e=this.coda.poll();
			this.numeroGiorni=e.getGiorno();
			
			processEvent(e);
			
		}
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
		case DA_INTERVISTARE:
			
			double caso=Math.random();
			if(caso<0.6) {
				//caso 1
				User vicino= selezionaAdiacente(e.getIntervistato());
				
				if(vicino==null) {
					vicino= selezionaIntervistato(this.grafo.vertexSet());
				}
				
				this.coda.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, vicino, e.getGiornalista()));
				this.intervistati.add(vicino);
				e.getGiornalista().incrementaNumerointervistati();
				
			}else {
				if(caso<0.8) {
					//caso 2: rimando la scelta a domani
					this.coda.add(new Event(e.getGiorno()+1, EventType.FERIE, e.getIntervistato(), e.getGiornalista()));
					
				} else {
					//caso 3: domani intervisto lo stesso
					this.coda.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, e.getIntervistato(), e.getGiornalista()));
				}
			}
			break;
			
		case FERIE:
			
			break;
		}
		
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}

	public void setGiornalisti(List<Giornalista> giornalisti) {
		this.giornalisti = giornalisti;
	}

	public int getNumeroGiorni() {
		return numeroGiorni;
	}
	
	
	/**
	 * Seleziona un intervistato dalla lista specificata, evitando
	 * di selezionare colore che sono già in this.intervistati
	 * @param lista
	 * @return
	 */
	private User selezionaIntervistato(Collection<User> lista) {
	
		Set<User> candidati=new HashSet<User>(lista);
		candidati.removeAll(this.intervistati);  //tolgo dalla lista di persone quelli che sono già stati intervistati
		
		//tiro a caso un elemento 
		int scelto=(int)(Math.random()*candidati.size());
		
		//per prendere facilmente l'intervistato con il suo numero identificativo converto il set in una nuova lista
		return (new ArrayList<User>(candidati)).get(scelto);
    }
	
	
	/**
	 * metodo per risolvere il caso 60% dell'evento DA_INTERVISTARE
	 * @param u
	 * @return
	 */
	private User selezionaAdiacente(User u) {
	
		List<User> vicini=Graphs.neighborListOf(this.grafo, u);
		vicini.removeAll(this.intervistati);  //tolgo dalla lista di persone quelli che sono già stati intervistati
		
		//se il vertice non ha adiacenti o se tutti gli adiacenti sono già stati intervistati
		if(vicini.size()==0) {
			return null;
		}
		
		double max=0;
		for(User v: vicini) {
			double peso=this.grafo.getEdgeWeight(this.grafo.getEdge(u, v));
			if(peso>max)
				max=peso;
		}
		
		List<User> migliori=new ArrayList<>(); //non è mai vuota, deve contenerne almeno uno
		for(User v: vicini) {
			double peso=this.grafo.getEdgeWeight(this.grafo.getEdge(u, v));
			if(peso==max) {
				migliori.add(v);
			}
		}
		
		int scelto=(int)(Math.random()*migliori.size());
		
		return migliori.get(scelto);
    }
	

}
