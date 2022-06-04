package it.polito.tdp.yelp.model;

public class Giornalista {

	private int id; //i giornalisti sono rappresentati da un numero compreso tra 0 e x1-1
	private int numeroIntervistati;
	
	
	public Giornalista(int id) {
		super();
		this.id = id;
		this.numeroIntervistati=0;
	}


	public int getId() {
		return id;
	}


	public int getNumeroIntervistati() {
		return numeroIntervistati;
	}
	
	public void incrementaNumerointervistati() {
		this.numeroIntervistati++;
	}
	
	
}
