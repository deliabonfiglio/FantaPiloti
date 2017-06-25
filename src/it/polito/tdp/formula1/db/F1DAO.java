package it.polito.tdp.formula1.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.formula1.model.Circuit;
import it.polito.tdp.formula1.model.Driver;
import it.polito.tdp.formula1.model.FantaDriver;
import it.polito.tdp.formula1.model.LapTime;
import it.polito.tdp.formula1.model.Race;
import it.polito.tdp.formula1.model.Season;


public class F1DAO {

	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year desc " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(rs.getInt("year"), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public static void main(String[] args) {
		F1DAO dao = new F1DAO() ;
		
		List<Season> seasons = dao.getAllSeasons() ;
		System.out.println(seasons);
	}

	public List<Circuit> getAllCircuits(Season s) {
		String sql = "select c.* "+
						"from circuits as c, races as r "+
						"where r.year=? and r.circuitId=c.circuitId " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getYear());
			ResultSet rs = st.executeQuery() ;
			
			List<Circuit> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("circuitRef"), rs.getString("name"),
						rs.getString("location"), rs.getString("country"), rs.getDouble("lat"), rs.getDouble("lng"),
						rs.getInt("alt"), rs.getString("url")));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public Race getRace(Season s, Circuit c) {
		String sql = "select r.* "+
				"from circuits as c, races as r "+
				"where r.year=? and r.circuitId=c.circuitId and r.circuitId=?" ;

		try {
			Connection conn = DBConnect.getConnection() ;
		
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getYear());
			st.setInt(2, c.getCircuitId());
			ResultSet rs = st.executeQuery() ;
			
			Race r=null;
			
			if(rs.next()) {
				/*
				 * private int raceId ;
	private Year year ;
	private int round ;
	private int circuitId ; // refers to {@link Circuit}
	private String name ;
	private LocalDate date ;
	private LocalTime time ;
	private String url;
				 */
				r = new Race(rs.getInt("raceId"), Year.of(rs.getInt("year")), rs.getInt("round"),
						rs.getInt("circuitId"), rs.getString("name"), rs.getDate("date").toLocalDate(),
						rs.getString("url"));
			}
			
			conn.close();
			return r ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		}

	public List<Driver> getDriversOfRace(Race gara, Season s) {
		/*private int driverId ;
	private String driverRef ;
	private int number ;
	private String code ;
	private String forename ;
	private String surname ;
	private LocalDate dob ; // date of birth
	private String nationality ;
	private String url;
	*/
		String sql ="select distinct d.* "+
					"from races as r, drivers as d , results as res "+
					"where r.year=? and r.raceId=? and res.driverId=d.driverId ";
		
		List<Driver> list= new ArrayList<Driver>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, s.getYear());
			st.setInt(2, gara.getRaceId());
			ResultSet rs = st.executeQuery() ;
			
			while(rs.next()) {
				list.add(new Driver(rs.getInt("driverId"), rs.getString("driverRef"), 
						rs.getInt("number"), rs.getString("code"), rs.getString("forename"),
						rs.getString("surname"),rs.getString("nationality"), rs.getString("url")));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<FantaDriver> getFantaDriver(Driver d, Circuit c) {
		String sql = "SELECT r.year "+
					"from results as res, races as r "+
					"where res.driverId=? and res.raceId=r.raceId and r.circuitId=? order by r.year ";
	
		List<FantaDriver> list= new ArrayList<FantaDriver>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(2, c.getCircuitId());
			st.setInt(1, d.getDriverId());
			ResultSet rs = st.executeQuery() ;
			
			while(rs.next()) {
				
				list.add(new FantaDriver(rs.getInt("year")));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public Map<Integer, LapTime> getTempiFor(FantaDriver fd, Driver d, Circuit c) {
		String sql ="select l.* "+
					"from results as res, races as r, laptimes as l "+
					"where res.driverId= ? and r.raceId=res.raceId and r.circuitId=? and l.driverId=res.driverId and r.raceId=l.raceId and r.year=? ";
		Map<Integer, LapTime>map= new HashMap<Integer, LapTime>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, d.getDriverId());
			st.setInt(2, c.getCircuitId());
			st.setInt(3, fd.getAnno());
			ResultSet rs = st.executeQuery() ;
			
			while(rs.next()) {
				
				map.put(rs.getInt("lap"), new LapTime(
						rs.getInt("raceId"),
						rs.getInt("driverId"), rs.getInt("lap"), rs.getInt("position"), rs.getString("time"),
						rs.getInt("milliseconds")));
			}
			
			conn.close();
			return map ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
