/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EjecucionDS;

/**
 *
 * @author Luis
 */
public class Simbolo {
     
     public String Ambito;
        public String Nombre;
        public Object Valor;
        public String Tipo;
        public String Linea;
        public String Columna;

       
        public Simbolo(String ambito,String nombre,Object valor, String tipo,String linea,String columna){
            this.Ambito =ambito;
            this.Nombre=nombre;
            this.Tipo=tipo;
            this.Valor=valor;
            this.Linea=linea;
            this.Columna =columna;
        }
}
