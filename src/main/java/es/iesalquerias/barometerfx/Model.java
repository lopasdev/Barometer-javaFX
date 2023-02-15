/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.iesalquerias.barometerfx;

import java.util.ArrayList;

/**
 *
 * @author lopas
 */
public class Model {
    
    //Declaración de variables
    private ArrayList<Measure> measure;
    private int rPreassure;

    //Constructor modelo
    public Model() {
        ArrayList<Measure> m = new ArrayList<>();
        this.rPreassure = rPreassure;
    }
    
    //Getter y Setters
    public ArrayList<Measure> getMeasure() {
        return measure;
    }

    public void setMeasure(ArrayList<Measure> measure) {
        this.measure = measure;
    }

    public int getrPreassure() {
        return rPreassure;
    }

    public void setrPreassure(int rPreassure) {
        this.rPreassure = rPreassure;
    }
    
}
