package com.example.pytkirja;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Meeting {
    protected String type;
    protected String date;
    protected String time;
    protected String location;
    protected ArrayList<Agenda> agendas = null;
    protected ArrayList<Minutes> minutes = null;

    //MEETINGS CONTAIN ONE AGENDA AND MINUTES
    //HOWEVER I CHOSE ARRAYLIST AS A WAY TO SAVE THE INFO AS IT WAS EASILY RETRIEVABLE
    //FROM THERE

    public Meeting () {
        type = "";
        date = "";
        time = "";
        location = "";
        this.agendas = new ArrayList<Agenda>();
        this.minutes = new ArrayList<Minutes>();
    }

    public Meeting (String ty, String d, String ti, String l) {
        type = ty;
        date = d;
        time = ti;
        location = l;
        this.agendas = new ArrayList<Agenda>();
        this.minutes = new ArrayList<Minutes>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String toString() {
        String spinner = this.type + " " + this.date;
        return spinner;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //OBSOLETE AS IT WAS IN THE END CREATED FROM THE
    //MEETING ACTIVITY
    public Agenda createAgenda () {
        Agenda agenda = new Agenda ();
        return agenda;
    }

    //OBSOLETE AS IT WAS IN THE END CREATED FROM THE
    //MEETING ACTIVITY
    public Minutes createMinutes () {
        Minutes minutes = new Minutes();
        return minutes;
    }
}
