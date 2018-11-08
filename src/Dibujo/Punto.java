/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dibujo;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Luis
 */
public class Punto {
    public final int posX;
    public final int posY;
    public final String color;
    public final int diametro;
    public Punto(int posx, int posy, String color, int diametro) {
        this.posX = posx;
        this.posY = posy;
        this.color = color;
        this.diametro = diametro;
    }
    public void paint(Graphics2D g2d) {
        g2d.setColor( Color.decode(color));
        g2d.fillOval(posX, posY, diametro, diametro);        
    }
}
