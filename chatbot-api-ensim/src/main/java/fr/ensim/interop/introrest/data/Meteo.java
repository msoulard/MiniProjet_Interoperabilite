package fr.ensim.interop.introrest.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meteo {
    private String meteo;
    private String details;
    private String temperature;
    private LocalDateTime date;
    public String getMeteo() {
        return meteo;
    }

    public void setMeteo(String meteo) {
        this.meteo = meteo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getFormattedDate() {
        // Formatage de la date en String
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(outputFormatter);
        return formattedDate;
    }


}
