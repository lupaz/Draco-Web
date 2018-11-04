/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EjecucionDASM;

import AST.Nodo;
import TraductorDpp.Parametro;
import java.util.ArrayList;

/**
 *
 * @author Luis
 */
public class Funcion {
        public String Nombre;       
        public Nodo Cuerpo;
       
        public Funcion(String nombre, Nodo cuerpo){
            this.Nombre= nombre;
            this.Cuerpo= cuerpo;
        }
}
