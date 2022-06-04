package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event> {
	
	public enum EventType{
		DA_INTERVISTARE, 
		FERIE  //non intervisto nessuno quel giorno
	}

	//chi intervista chi e quando 
	private int giorno;
	private User intervistato;
	private Giornalista giornalista;
	private EventType type;
	
	public Event(int giorno, EventType type, User intervistato, Giornalista giornalista) {
		super();
		this.giorno = giorno;
		this.intervistato = intervistato;
		this.giornalista = giornalista;
		this.type=type;
	}

	public int getGiorno() {
		return giorno;
	}

	public User getIntervistato() {
		return intervistato;
	}

	public Giornalista getGiornalista() {
		return giornalista;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public int compareTo(Event o) {
		//ordino per data 
		return this.giorno - o.giorno;  
	}
	
	
	
}
