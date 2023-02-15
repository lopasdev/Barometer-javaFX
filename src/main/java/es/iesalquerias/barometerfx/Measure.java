/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.iesalquerias.barometerfx;

import java.time.LocalDateTime;

/**
 *
 * @author lopas
 */
class Measure implements Comparable {
    
    //Variables de medicion
    LocalDateTime time;
    int preassure;

    
    //Getters y Setters
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getPreassure() {
        return preassure;
    }

    public void setPreassure(int preassure) {
        this.preassure = preassure;
    }
    
    //Constructor
    public Measure(LocalDateTime time, int preassure) {
        this.time = time;
        this.preassure = preassure;
    }

    
    //Override methods
    @Override
    public String toString() {
        return "Measure{" + "time=" + time + ", preassure=" + preassure + '}';
    }

    @Override
    public int compareTo(Object o) {
        int aux = 0;
        if (this.time.isBefore(((Measure) o).getTime())) {
            aux = -1;
        } else {
            if (this.time.isAfter(((Measure) o).getTime())) {
                aux = 1;
            }
        }
        return aux;
    }

}
