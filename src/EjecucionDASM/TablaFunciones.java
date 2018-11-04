/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EjecucionDASM;

import java.util.HashMap;

/**
 *
 * @author Luis
 */
public class TablaFunciones {
    HashMap t;
    public TablaFunciones(){
        t= new HashMap();
    }
        
    public void insertar(String nombre,Funcion funcion){
            t.put(nombre, funcion);
            System.out.println("Se inserto una nueva funcion DASM: "+nombre); 
    }
    
    public Funcion retornaFuncion(String nombre){
        return (Funcion)t.get(nombre);  
    }
        
    public boolean existeFuncion(String Nombre){ // esta comprobacion hacerla en la analizador para hacer la validacion ahi 
        return retornaFuncion(Nombre)!=null;
    }
}
