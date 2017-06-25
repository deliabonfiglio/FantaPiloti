package it.polito.tdp.formula1.model;

public class FantaStat implements Comparable<FantaStat>{
	private FantaDriver fd;
	private int punti;
	
	public FantaStat(FantaDriver fd, int punti) {
		super();
		this.fd = fd;
		this.punti = punti;
	}

	public FantaDriver getFd() {
		return fd;
	}

	public void setFd(FantaDriver fd) {
		this.fd = fd;
	}

	public int getPunti() {
		return punti;
	}

	public void setPunti(int punti) {
		this.punti = punti;
	}

	@Override
	public int compareTo(FantaStat arg0) {
		return -(this.punti-arg0.punti);
	}
	
	public String toString(){
		return fd.getAnno()+ " "+punti;
	}
	

}
