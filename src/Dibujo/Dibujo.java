/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dibujo;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Luis
 */
public class Dibujo extends JPanel{
    
    final ArrayList<trazo> trazos;
    private static final String punto="1";
    private static final String linea="2";
    private static final String cuadrado="3";
    private static final String ovalo="4";
    private static final String text="5";
    
    public Dibujo() {
        super();
        this.setBackground(java.awt.Color.WHITE);
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(500, 250));
        this.trazos = new ArrayList<>();
    }
    
    public void addPunto(int posX, int posY, String color,int diametro) {        
        trazos.add(  new trazo (new Punto(posX, posY, color, diametro),punto));
    } 
    
    public void addLinea(int posxi, int posyi,int posxf, int posyf, String color,int grosor) {        
        trazos.add(  new trazo (new Linea(posxi, posyi, posxf,posyf ,color,grosor),linea));
    } 
    
    public void addCuadrado(int posx, int posy, String color, int ancho,int alto) {        
        trazos.add(  new trazo (new Cuadrado(posx, posy, color, ancho,alto),cuadrado));
    } 
    
    public void addOvalo(int posx, int posy, String color, int ancho,int alto) {        
        trazos.add(  new trazo (new Ovalo(posx, posy, color, ancho,alto),ovalo));
    } 
    
    public void addTexto(int posx, int posy, String color, String texto) {        
        trazos.add(  new trazo (new Texto(posx, posy, color, texto),text));
    } 
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        for(final trazo t : trazos) {            
            switch(t.tipo){
                case punto:                    
                    ((Punto)t.trazo).paint(g2d);
                    break;
                case linea:
                    ((Linea)t.trazo).paint(g2d);
                    break;
                case cuadrado:
                    ((Cuadrado)t.trazo).paint(g2d);
                    break;
                case ovalo:
                    ((Ovalo)t.trazo).paint(g2d);
                    break;
                case text:
                    ((Texto)t.trazo).paint(g2d);
                    break;                
            }            
        }
    } 
}

/*


//Pintar punto
        if(this.drawPoint){
            g2d.setColor(java.awt.Color.decode(this.color_p));
            g2d.fillOval(this.x_p, this.y_p, this.diameter_p, this.diameter_p);
        }
        if(this.drawSquare){
            //Pintar cuadrado relleno
            g2d.setColor(java.awt.Color.decode(this.color_s));
            g2d.fillRect(this.x_s, this.y_s, this.width_s, this.height_s);
            //Pintar cuadrado vacio
            g2d.setColor(java.awt.Color.decode(this.color_s));
            g2d.drawRect(this.x_s +  this.width_s +  10, this.y_s, this.width_s, this.height_s);
        }
        if(this.drawOval){
            //Pintar óvalo relleno
            g2d.setColor(java.awt.Color.decode(this.color_o));
            g2d.fillOval(this.x_o, this.y_o, this.width_o, this.height_o);
            //Pintar óvalo vacio
            g2d.setColor(java.awt.Color.decode(this.color_o));
            g2d.drawOval(this.x_o +  this.width_o +  10, this.y_o, this.width_o, this.height_o);
        }
        if(this.drawText){
            //Pintar texto
            g2d.setColor(java.awt.Color.decode(this.color_t));
            g2d.drawString(this.text, this.x_t, this.y_t);
        }
        if(this.drawLine){
            //Pintar linea
            g2d.setColor(java.awt.Color.decode(this.color_l));
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(this.width_l));
            g2d.drawLine(this.x1_l, this.y1_l, this.x2_l, this.y2_l);
            g2d.setStroke(oldStroke);
        }
*/