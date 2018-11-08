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
public class Texto {     
    public final int posX;
    public final int posY;
    public final String color;
    public final String texto;
    public Texto(int posx, int posy, String color, String texto) {
        this.posX = posx;
        this.posY = posy;
        this.color = color;
        this.texto = texto;
    }
    public void paint(Graphics2D g2d) {
        g2d.setColor( Color.decode(color));
        g2d.drawString(texto,posX,posY);
        
    }
}
