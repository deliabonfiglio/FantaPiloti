package it.polito.tdp.formula1.model;

import java.util.HashMap;
import java.util.Map;

public class FantaDriver {

	private int anno;
	private int laptime;
	private Map<Integer, LapTime> tempi;

	public Map<Integer, LapTime> getTempi() {
		return tempi;
	}

	public void setTempi(Map<Integer, LapTime> tempi) {
		this.tempi = tempi;
	}

	public FantaDriver(int int1) {
		this.anno =int1;
		this.tempi= new HashMap<Integer, LapTime>();
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public int getLaptime() {
		return laptime;
	}

	public void setLaptime(int laptime) {
		this.laptime = laptime;
	}
	
}
