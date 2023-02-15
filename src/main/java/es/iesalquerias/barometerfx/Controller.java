package es.iesalquerias.barometerfx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Controller implements Initializable {

    @FXML
    private BorderPane bpFX;

    @FXML
    private Button btAdd;

    @FXML
    private Button btUpdate;

    @FXML
    private DatePicker dpCalendar;

    @FXML
    private ImageView ivIcon;

    @FXML
    private Label lAltitude;

    @FXML
    private Label lHour;

    @FXML
    private Label lPreassure;
    
    @FXML
    private Label lPrediction;

    @FXML
    private Menu mArchive;

    @FXML
    private Menu mLanguage;

    @FXML
    private MenuBar mbMenu;

    @FXML
    private MenuItem miEnglish;

    @FXML
    private MenuItem miExit;

    @FXML
    private MenuItem miExport;

    @FXML
    private MenuItem miSpanish;

    @FXML
    private ToggleGroup prMeasurement;

    @FXML
    private TextArea taHistory;

    @FXML
    private TextField tfHour;
    
    @FXML
    private TextField tfAltitude;

    @FXML
    private TextField tfPreassure;

    //Acción añade datos
    @FXML
    void addPreasure(ActionEvent event) throws IOException {

        //Actualización de los datos de la presión
        try{
            
            String preassure = tfPreassure.getText();
            LocalDate time = dpCalendar.getValue();
            String sHour = tfHour.getText();
            String[] hour = sHour.split(":");

            LocalDateTime ldt = time.atStartOfDay();
            ldt = ldt.plusHours(Integer.parseInt(hour[0]));
            ldt = ldt.plusMinutes(Integer.parseInt(hour[1]));

            
            Measure measure = new Measure(ldt, Integer.parseInt(preassure));

            ArrayList<Measure> measurements = model.getMeasure();

            measurements.add(measure);
            Collections.sort(measurements);

            model.setMeasure(measurements);
            updateHistory();
            saveData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        treatmentData();
    }

    @FXML
    void updateAltitude (ActionEvent event) throws IOException{

        //Actualización de la altura
        try{
            int rPreassure = 760;
            String altitude = tfAltitude.getText();
            int d = Integer.parseInt(altitude) / 11;
            int rData = (int) Math.round(d);
            int nData = 760 -rData;
            
            model.setrPreassure(nData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        treatmentData();
    }

    Model model;
    
    public String treatmentData(){
        //Tratamiento de datos para el barometro
        String answer = "Impreciso";
        ArrayList<Measure> measurements = model.getMeasure();
        
        Collections.sort(measurements, Collections.reverseOrder());
        
        try{
            //Declaracion de variables
            Measure h1 = measurements.get(22);
            Measure h2 = measurements.get(0);

            int d1 = h1.getPreassure();
            int d2 = h2.getPreassure();

            Image i = new Image(getClass().getResourceAsStream("icon.png"));
            ivIcon.setImage(i);

            String iconLocation = "images/";

            //Operaciones tratadas del pdf
            if(d2 < model.getrPreassure()){
                answer = "Borrasca profunda, impreciso";
                i = new Image(getClass()
                        .getResourceAsStream(iconLocation += "heavy_rain.png"));
                ivIcon.setImage(i);
            }
            if(d2 > model.getrPreassure()){
                answer = "Anticiclón, impreciso";
                i = new Image(getClass()
                        .getResourceAsStream(iconLocation += "sunny.png"));
                ivIcon.setImage(i);
            }
            if((d1-d2)>6 && d1 < model.getrPreassure()){
                answer = "Borrasca lejos, impreciso";
                i = new Image(getClass()
                        .getResourceAsStream(iconLocation += "rainy.png"));
                ivIcon.setImage(i);
            }
            if((d1-d2)>24 && d2 < model.getrPreassure()){
                answer = "Borrasca profunda, impreciso";
                i = new Image(getClass()
                        .getResourceAsStream(iconLocation += "heavy_rain.png"));
                ivIcon.setImage(i); 
            }
            if((d1-d2)<-6 && d1 > model.getrPreassure()){
                answer = "Anticiclón, impreciso";
                i = new Image(getClass()
                        .getResourceAsStream(iconLocation += "sunny.png"));
                ivIcon.setImage(i);
            }
            if((d1-d2)<-24 && d1 > model.getrPreassure()){
                answer = "Nublado, impreciso";
                i = new Image(getClass()
                        .getResourceAsStream(iconLocation += "cloudy.png"));
                ivIcon.setImage(i);
            }
        } catch (Exception ex) { 
            System.out.println(ex);
            answer = "Faltan datos, se necesitan al menos 24";
        }
        lPrediction.setText(answer);
        return answer;
    
    }
    
    public void saveData() throws IOException{
        //Guarda los datos en el json
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime
                    .class, new LocalDateTimeAdapter())
                    .setPrettyPrinting().create();
            String json = gson.toJson(model.getMeasure());    
            try (FileWriter fw = new FileWriter
        ("src\\main\\resources\\es\\iesalquerias\\barometrofx\\data.json")) {
                fw.write(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) { System.out.println(ex);}
    }
    
    public void loadData() throws FileNotFoundException{
        //Carga los datos del  json
        ArrayList<Measure> lMeasurements = new ArrayList<>();
        try {
            String json;
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime
                    .class, new LocalDateTimeAdapter())
                    .setPrettyPrinting().create();  
            
            json = new String(this.getClass()
                    .getResourceAsStream("src\\main\\resources\\json\\data.json")
                    .readAllBytes());

             
            java.lang.reflect.Type userListType = new 
                    TypeToken<ArrayList<Measure>>(){}.getType();
            lMeasurements = gson.fromJson(json, userListType);  
            
        } catch (Exception ex) { System.out.println(ex);}
        
        System.out.println(lMeasurements);
        model.setMeasure(lMeasurements);
        updateHistory();
    }
    
    public void updateHistory(){
        //Actualiza el historial con los datos del ArrayList
        ArrayList<Measure> measurements = model.getMeasure();
        
        taHistory.setText("");
        
        for (Measure m : measurements) {
            taHistory.appendText(m.toString());
            taHistory.appendText("\n");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Guarda la presion de referencia
        model.setrPreassure(760);
    }
    
    
    
}
