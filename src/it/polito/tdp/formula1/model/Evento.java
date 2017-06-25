package it.polito.tdp.formula1.model;

public class Evento implements Comparable<Evento>{
	private FantaDriver d;
	private int lap;
	private int time;
	
	public Evento(FantaDriver d, int lap, int time) {
		super();
		this.d = d;
		this.lap = lap;
		this.time = time;
	}

	public FantaDriver getDriver() {
		return d;
	}

	public void setD(FantaDriver d) {
		this.d = d;
	}

	public int getLap() {
		return lap;
	}




	public void setLap(int lap) {
		this.lap = lap;
	}




	public int getTime() {
		return time;
	}




	public void setTime(int time) {
		this.time = time;
	}




	@Override
	public int compareTo(Evento arg0) {
		
		return this.time-arg0.time;
	}
	
	

}
