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
        public ArrayList<Parametro> Parametros;
        public Nodo Cuerpo;
        public String key; // aca vamos a generar la llave de la funcion para el polimorfismo.

        public Funcion(String tipo, String nombre, Nodo cuerpo){
            this.Nombre= nombre;
            this.Cuerpo= cuerpo;
            this.Tipo = tipo;
            Parametros = new ArrayList<>();
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
}
