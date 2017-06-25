package it.polito.tdp.formula1.model;

import java.util.*;

public class Simulator {
	//coda di eventi
	private PriorityQueue<Evento> queue;
	
	//lista dei partecipanti alla fanta gara
	private List<FantaDriver> piloti;
	
	//mappa dei punti
	private Map<FantaDriver, Integer> punti;
	
	//mappa delle posizioni negli anni
	private Map<Integer, List<FantaDriver>> map;
	
	//tempo del pilota migliore
	private int bestLap;
	
	Simulator(List<FantaDriver> list){
		this.piloti=list;
		this.queue= new PriorityQueue<>();
		this.map= new HashMap<>();
		bestLap=0;
	}
	
	public void addEventi(){
		punti = new HashMap<>();
		
		for(FantaDriver dn: piloti){

			if(dn.getTempi().get(1)!=null){
				System.out.println(dn.getTempi().get(1).getMiliseconds());
				queue.add(new Evento(dn, 1, dn.getTempi().get(1).getMiliseconds()));
				punti.put(dn, 0);
			}
		}
	}
	
	public void run(){
		
		this.addEventi();
		
		while(!queue.isEmpty()){
			Evento e = queue.poll();
			
			if(map.get(e.getLap())==null){
				map.put(e.getLap(), new ArrayList<FantaDriver>());
				bestLap=e.getLap();
			}
			
			map.get(e.getLap()).add(e.getDriver());
			
			if(e.getLap()!=1){
				int posAttuale= map.get(e.getLap()).size()-1;
				
				int posPrec=0;
				int counter=0;
				
				//prendo lo stesso giocatore dell'anno precedente dalla mappa
				for(FantaDriver fd : map.get(e.getLap()-1)){
					if(e.getDriver().equals(fd)){
						posPrec=counter;
						break;
					}
					counter++;
				}
				
				if(posAttuale<posPrec){
					//aumento i punti di quel giocatore
					int punteggio = punti.get(e.getDriver());
					punti.put(e.getDriver(), punteggio+1);
				}
				
			}
			if(e.getLap()+2<bestLap){
				//se è 2 giri indietro al migliore lo elimino
				punti.remove(e.getDriver());
			} else {
				Evento nuovo = new Evento(e.getDriver(), e.getLap()+1, e.getDriver().getTempi().get(1).getMiliseconds());
				queue.add(nuovo);
			}
			
			
		}
	}

	public List<FantaStat> getClassifica() {
		List<FantaStat> classifica = new ArrayList<FantaStat>();
		
		for(FantaDriver fd: punti.keySet()){
			classifica.add(new FantaStat(fd, punti.get(fd)));
		}
		
		Collections.sort(classifica);
		return classifica;
	}
}
