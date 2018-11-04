/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draco_web;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Luis
 */
public class Pintar { 
    public StyleContext sc;
    public DefaultStyledDocument doc ;

    public Pintar(StyleContext sc, DefaultStyledDocument doc) {
        this.sc=sc;
        this.doc=doc;
    }
    
    public void insertar(String texto){
        try {
            doc.insertString(0,texto,null);
        }catch (Exception ex) {
            System.out.println("ERROr: no se pudo establecer estilo de documento");
        }
   
   }
   
    public void pintaRojo(int posini, int posfin) {
        Style rojo = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(rojo, Color.decode("#FF4040"));
        doc.setCharacterAttributes(posini, posfin, rojo, false);

    }

    public void pintaVerde(int posini, int posfin) {
        Style verde = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(verde, Color.decode("#03CC5E"));
        doc.setCharacterAttributes(posini, posfin, verde, false);
    }

    public void pintaAzul(int posini, int posfin) {
        Style azul = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(azul, Color.blue);
        doc.setCharacterAttributes(posini, posfin, azul, false);

    }

    public void pintaGris(int posini, int posfin) {
        Style gris = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(gris, Color.decode("#898989"));
        doc.setCharacterAttributes(posini, posfin, gris, false);

    }

    public void pintaAmari(int posini, int posfin) {
        Style cafe = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(cafe, Color.decode("#DED823"));
        doc.setCharacterAttributes(posini, posfin, cafe, false);
    }

    public void pintaMora(int posini, int posfin) {
        Style mora = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(mora, Color.decode("#8633FF"));
        doc.setCharacterAttributes(posini, posfin, mora, false);
    }

    public void pintaNara(int posini, int posfin) {
        Style nara = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(nara, Color.decode("#FF8700"));
        doc.setCharacterAttributes(posini, posfin, nara, false);
    }

    
}
