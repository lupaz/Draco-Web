/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dibujo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author Luis
 */
public class Linea {
    
    public final int posXi;
    public final int posYi;
    public final int posXf;
    public final int posYf; 
    public final String color;
    public final int grosor;
  
    public Linea(int posxi, int posyi,int posxf, int posyf, String color,int grosor) {
        this.posXi = posxi;
        this.posYi = posyi;
        this.posXf = posxf;
        this.posYf = posyf;
        this.color = color;
        this.grosor = grosor;
    }
    public void paint(Graphics2D g2d) {
        g2d.setColor( Color.decode(color));
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(this.grosor));
        g2d.drawLine(this.posXi, this.posYi, this.posXf, this.posYf);
        g2d.setStroke(oldStroke);      
    }
}
