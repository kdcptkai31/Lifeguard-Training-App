package org.openjfx.model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Instructor {

    private int year;
    private int session;
    private String name;
    private Image image;

    /**
     * Constructor
     * @param y
     * @param s
     * @param n
     * @param i
     */
    public Instructor(int y, int s, String n, Image i){

        year = y;
        session = s;
        name = n;
        image = i;

    }

    /**
     * Initial Constructor
     * @param y
     * @param s
     * @param n
     */
    public Instructor(int y, int s, String n){ this(y, s, n, null); }

    //Getters
    public int getYear() { return year; }

    public int getSession() { return session; }

    public String getName() { return name; }

    public Image getImage() { return image; }

    //Setters
    public void setYear(int year) { this.year = year; }

    public void setSession(int session) { this.session = session; }

    public void setName(String name) { this.name = name; }

    public void setImage(Image i) { image = i; }

}
