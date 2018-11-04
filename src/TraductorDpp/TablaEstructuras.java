/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TraductorDpp;

import java.util.HashMap;

/**
 *
 * @author Luis
 */
public class TablaEstructuras {
    HashMap t;
    public TablaEstructuras(){
        t= new HashMap();
    }
        
    public void insertar(String nombre,Estructura estructura){
            t.put(nombre, estructura);
            System.out.println("Se inserto una nueva estructura: "+nombre); 
    }
    
    public Estructura retornaEstructura(String nombre){
        return (Estructura)t.get(nombre);  
    }
        
    public boolean existeEstructura(String Nombre){ // esta comprobacion hacerla en la analizador para hacer la validacion ahi 
        return retornaEstructura(Nombre)!=null;
    }
}
