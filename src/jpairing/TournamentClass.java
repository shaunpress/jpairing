/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;

import java.util.Date;

/**
 *
 * @author Shaun
 */
public class TournamentClass {
    private String name;
    private String place;
    private Date begin_date;
    private Date end_date;
    private int total_rounds;
    private String director;
    
    public TournamentClass() {
        name = "";
        place = "";
        begin_date = new Date();
        end_date = new Date();
        total_rounds = 0;
        director = "";
    }
    
    public String fileOutput() {
        String outputText;
        
        outputText = name+'\n'+place+'\n';
        
        return outputText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(Date begin_date) {
        this.begin_date = begin_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getTotal_rounds() {
        return total_rounds;
    }

    public void setTotal_rounds(int total_rounds) {
        this.total_rounds = total_rounds;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
    
    
    
    
}
