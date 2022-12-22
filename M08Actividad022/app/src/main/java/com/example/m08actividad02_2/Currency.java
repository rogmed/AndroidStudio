package com.example.m08actividad02_2;

import java.util.Date;

public class Currency {
    final private int Id;
    final private String Name;
    final private Double Rate;
    final private String Date;

    public Currency(int id, String name, Double rate,String date) {
        this.Id = id;
        this.Name = name;
        this.Rate = rate;
        this.Date = date;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public Double getRate() {
        return Rate;
    }

    public String geDate() {
        return Date;
    }
}
