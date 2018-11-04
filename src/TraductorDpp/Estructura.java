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
public class Estructura {
    public String nombre;
    public ArrayList<Simbolo> atributos;
    
    public Estructura(String nombre){
            this.nombre= nombre;
            atributos = new ArrayList<>();
        }
    
        public int numAtrs()
        {
            return atributos.size();
        }

        public void addAtributo(Simbolo sim) //Vamos a enviar lo parametros de esta forma tipo,nombre
        {
            atributos.add(sim);
        }
        
        public Simbolo getAtributo(String nombre){
            for(Simbolo sim : atributos) {
                if(sim.nombre.equals(nombre)){
                    return sim;
                }
            }
            return null;
        }
        public Boolean exixteAtr(String nombre){
            for(Simbolo sim : atributos) {
                if(sim.nombre.equals(nombre)){
                    return true;
                }
            }
            return false;
        }
}
