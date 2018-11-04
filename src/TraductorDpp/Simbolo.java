/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TraductorDpp;

import java.util.ArrayList;

/**
 *
 * @author Luis
 */
public class Simbolo {
         
        public String nombre;
        public String tamano;
        public String rol;
        public String tipo;
        public String TipoObjeto="";
        public String posicion;
        public String linea;
        public String columna;
        public int numDims=0;
        public int profundidad=0; //servira para acceso a variables locales,cunado estan en diferente ambito 
        
        public Simbolo(String nombre,String tamano, String rol, String tipo,String posicion,String linea,String columna){
            this.nombre=nombre;
            this.tamano=tamano;
            this.rol=rol;
            this.tipo=tipo;
            this.posicion = posicion;
            this.linea=linea;
            this.columna =columna;
        }
}
