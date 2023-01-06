package com.example.nhom_19;

import java.io.Serializable;

public class City implements Serializable {
    int id;
    String name;
    String country;
    String description;
    public City(int id, String name, String country, String description) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.description=description;
    }
    public City(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.description="";
    }

    public City(String name, String country) {
        this.name = name;
        this.country= country;
        this.description="";
    }

    public City() {
    }
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
