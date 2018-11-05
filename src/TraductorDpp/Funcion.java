/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TraductorDpp;

import AST.Nodo;
import java.util.ArrayList;

/**
 *
 * @author Luis
 */
public class Funcion {
        public String Nombre;
        public String Tipo;
        public ArrayList<Simbolo> variables;
        public Nodo Cuerpo;
        public ArrayList<Parametro> Parametros;
        public String key; // aca vamos a generar la llave de la funcion para el polimorfismo.

        public Funcion(String tipo, String nombre,Nodo cuerpo){
            this.Nombre= nombre;
            this.Tipo = tipo;
            this.Cuerpo=cuerpo;
            Parametros = new ArrayList<>();
            variables = new ArrayList<>();
        }

        public int numPars()
        {
            return Parametros.size();
        }

        public void addParametro(String  nombre, String tipo) //Vamos a enviar lo parametros de esta forma tipo,nombre
        {
            Parametros.add(new Parametro(nombre,tipo));
        }

        public Boolean exixtePar(String nombre){
            for(Parametro par : Parametros) {
                if(par.getNombre().equals(nombre)){
                    return true;
                }
            }
            return false;
        }
        
        public void addVariable(Simbolo sim) //Vamos a enviar lo parametros de esta forma tipo,nombre
        {
            variables.add(sim);
        }
        
        public Simbolo getVariable(String nombre){
            for(Simbolo sim : variables) {
                if(sim.nombre.equals(nombre)){
                    return sim;
                }
            }
            return null;
        }
        public Boolean exixteVar(String nombre){
            for(Simbolo sim : variables) {
                if(sim.nombre.equals(nombre)){
                    return true;
                }
            }
            return false;
        }
}
