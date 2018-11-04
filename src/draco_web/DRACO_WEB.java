/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draco_web;

import AST.DibujarAST;
import AST.Nodo;
import Dplusplus.Par_Dplus;
import Dplusplus.Scan_Dplus;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 *
 * @author Luis
 */
public class DRACO_WEB {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        //Nodo raiz=leer_Dplus(InterfazD.rutaGenesis+"pruebaD.dpp");
        ///DibujarAST dibuja = new  DibujarAST();
        //dibuja.generarImg(raiz,"draco");        
        String cad="h";
        
        System.out.println("valor char-> "+ (int)cad.charAt(0)); 
        //System.out.println("aads"+cad);
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
