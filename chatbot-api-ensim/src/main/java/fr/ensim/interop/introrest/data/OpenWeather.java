package fr.ensim.interop.introrest.data;


import fr.ensim.interop.introrest.Main;

import java.util.List;

public class OpenWeather {

    private List<Weather> weather;
    List<Weather> list;
    private Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }




}
