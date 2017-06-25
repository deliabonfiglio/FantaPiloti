package it.polito.tdp.formula1.model;


import java.util.*;
import it.polito.tdp.formula1.db.F1DAO;

public class Model {

	private List<Season>seasons;
	private Map<Integer, Circuit> map;
	private Season s;
	private List<FantaDriver>diversOfRace;
	private Simulator sim;
	
	public List<Season> getSeason(){
		if(seasons==null){
			F1DAO dao = new F1DAO();
			seasons = dao.getAllSeasons();
		}
		
		return seasons;
	}

	public List<Circuit> getCircuitOfSeason(Season s){
		this.s=s;
		F1DAO dao = new F1DAO();
		List<Circuit> circuiti= dao.getAllCircuits(s);
		
		map = new HashMap<>();
		for(Circuit c: circuiti){
			map.put(c.getCircuitId(), c);
		}
		return circuiti;
	}

	public Race getGara(Season sta, Circuit c) {
		F1DAO dao = new F1DAO();
		if(!this.s.equals(sta)){
			this.getCircuitOfSeason(sta);
			s=sta;
		}
		return dao.getRace(s,c);
	}
	
	public List<FantaDriver> getFantaDrivers(Driver d, Circuit c){
		F1DAO dao = new F1DAO();
		diversOfRace= dao.getFantaDriver(d, c);
		
		return diversOfRace;
	}

	public List<Driver> getDriversOfRace(Race gara, Season s2) {
		F1DAO dao = new F1DAO();
		
		return dao.getDriversOfRace(gara, s2);
		
	}
	
	public List<FantaStat> getClassifica(Driver d, Circuit c){
		List<FantaDriver>partecipanti = this.getFantaDrivers(d, c);
		
		F1DAO dao = new F1DAO();
		for(FantaDriver fd: partecipanti){
			fd.setTempi(dao.getTempiFor(fd, d, c));
		}
		
		sim= new Simulator(partecipanti);
		sim.run();
		return sim.getClassifica();
	}
}
