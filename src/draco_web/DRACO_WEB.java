/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draco_web;

import AST.DibujarAST;
import AST.Nodo;
import Dibujo.Dibujo;
import Dplusplus.Par_Dplus;
import Dplusplus.Scan_Dplus;

import java.awt.GraphicsConfiguration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import static java.lang.System.gc;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Luis
 */
public class DRACO_WEB {

    /**
     * @param args the command line arguments
     */
    static GraphicsConfiguration gc;
    public static void main(String[] args) {
        // TODO code application logic here
        //Nodo raiz=leer_Dplus(InterfazD.rutaGenesis+"pruebaD.dpp");
        ///DibujarAST dibuja = new  DibujarAST();
        //dibuja.generarImg(raiz,"draco");                
        //System.out.println("aads"+cad);
        
        
        JFrame frame = new JFrame(gc);
        frame.setTitle("Welcome to the jungle XD");
        frame.setSize(600, 600);
        frame.setLocation(200, 200);
   
        Dibujo dibujo = new Dibujo();
        //dibujo.drawLineSwing(5, 10, 20, 20, "#FF5733",10);
        //dibujo.repaint();
        
        int val = Integer.valueOf("FF5733",16);
        System.out.println(val);
                
        String hexColor = String.format("#%06X", (0xFFFFFF & val));                
        dibujo.addPunto(15, 15, hexColor, 20);        
        dibujo.addPunto(15, 15,"#80FF33", 70);
        dibujo.addCuadrado(50, 80, "#FFC300", 70, 90);
        dibujo.addLinea(40, 80,70, 120,"#52A7E9", 4);
        dibujo.addLinea(40, 80,70, 120,"#52A7E9", 4);
        dibujo.addOvalo(180, 150,"#8D52E9", 120,90);
        dibujo.addTexto(300,150,"#8D52E9","Callese viejo lesbiano :v");
        
        //dibujo.repaint();
        
        
        //dibujo.drawPointSwing(50, 50, "#FF5733", 10);
        //dibujo.repaint();
        
        frame.add(dibujo);        
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
    
    public static Nodo leer_Dplus(String ruta){
        String texto=leerArchivo(ruta);
        try {    
            System.out.println("Inicia el Analisis de D++...\n");            
            Scan_Dplus scan = new Scan_Dplus(new BufferedReader( new StringReader(texto)));
            Par_Dplus parser = new Par_Dplus(scan);
            parser.parse();
            System.out.println("Finaliza el Analisis de D++...");
            return  parser.raiz;
        } catch (Exception ex) {
            ex.printStackTrace();
            return  null;
        }
    }
    
    
    public static  String leerArchivo(String ruta){
        String texto=""; 
        try  {
              BufferedReader bf = new BufferedReader(new FileReader(ruta));
              String bflinea;
              while((bflinea=bf.readLine())!=null ){
              texto+=bflinea+"\n";
              }
              bf.close();
          } catch (Exception e) {
              System.err.println("Lectura Incorrecta de archivo");
          }       
        return  texto;
    }
    
    
    
    
}
