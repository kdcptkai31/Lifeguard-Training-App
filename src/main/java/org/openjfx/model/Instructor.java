package org.openjfx.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Instructor {

    private int year;
    private int session;
    private String name;
    private Image image;

    /**
     * Default Constructor
     */
    public Instructor(){}

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

    /**
     * Sends the default pfp image if the variable image is null, else sends the real pfp.
     * @return
     */
    public Image getActualImage(){

        if(image == null){

            try {

                return new Image(getClass().getClassLoader().getResource("org/openjfx/images/blankpfp.png").toURI().toString(),
                        0, 187, true, true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else
            return image;

        return null;

    }

    //Setters
    public void setYear(int year) { this.year = year; }

    public void setSession(int session) { this.session = session; }

    public void setName(String name) { this.name = name; }

    public void setImage(Image i) { image = i; }

}
