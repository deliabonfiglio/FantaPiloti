package it.polito.tdp.formula1;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.formula1.model.Circuit;
import it.polito.tdp.formula1.model.Driver;
import it.polito.tdp.formula1.model.FantaStat;
import it.polito.tdp.formula1.model.Model;
import it.polito.tdp.formula1.model.Race;
import it.polito.tdp.formula1.model.Season;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Circuit> boxCircuit;

    @FXML
    private ComboBox<Driver> boxDriver;

    @FXML
    private ComboBox<Season> boxSeason;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void doFantaGara(ActionEvent event) {
    	Circuit c = boxCircuit.getValue();
    	Driver d = boxDriver.getValue();
    	
    	if(d==null || c==null){
    		txtResult.appendText("Scegliere circuito e pilota");
    		return;
    	}
    	List<FantaStat> classifica = model.getClassifica(d, c);

    	for(FantaStat fs: classifica){
    		txtResult.appendText(fs.toString()+"\n");
    	}
    }

    @FXML
    void doInfoGara(ActionEvent event) {

    	Season s = boxSeason.getValue();
    	
    	if(s==null){
    		txtResult.appendText("Scegliere stagione.\n");
    		return;
    	}
    	
    	Circuit c = boxCircuit.getValue();
    	if(c==null){
    		txtResult.appendText("Scegliere un circuito\n");
    		return;
    	}
    	Race gara = model.getGara(s, c);
    	txtResult.appendText(gara.toString()+"\n");
    	
    	List<Driver> drivers= model.getDriversOfRace(gara, s);
    	
    	txtResult.appendText("Piloti della gara: \n");
    	for(Driver d: drivers){
    		txtResult.appendText(d.toString()+"\n");
    	}
    	boxDriver.getItems().addAll(drivers);
    }

    @FXML
    void initialize() {
        assert boxCircuit != null : "fx:id=\"boxCircuit\" was not injected: check your FXML file 'Formula1.fxml'.";
        assert boxDriver != null : "fx:id=\"boxDriver\" was not injected: check your FXML file 'Formula1.fxml'.";
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'Formula1.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Formula1.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model ;
		this.boxSeason.getItems().addAll(model.getSeason());
		
		boxSeason.valueProperty().addListener(new ChangeListener<Season>() {
        	@Override
			public void changed(ObservableValue<? extends Season> observable, Season oldValue, Season newValue) {
        		boxCircuit.getItems().clear();
        		boxCircuit.getItems().addAll(model.getCircuitOfSeason(newValue));			
			}   
          });
	}
}
