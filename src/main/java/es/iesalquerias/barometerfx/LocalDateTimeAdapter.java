/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.iesalquerias.barometerfx;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author lopas
 */
public class LocalDateTimeAdapter {
    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void write(JsonWriter out, LocalDateTime date) throws IOException {
        out.value(date.format(FORMATTER));
    }

    public LocalDateTime read(JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString(), FORMATTER);
    }
}
