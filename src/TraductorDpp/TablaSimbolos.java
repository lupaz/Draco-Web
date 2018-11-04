/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TraductorDpp;;

import java.util.HashMap;

/**
 *
 * @author Luis
 */
public class TablaSimbolos {
    
    public String Nivel;
    public String tipo;
    public boolean retorno;
    public boolean  detener;
    public boolean  continuar;
    public String etq_ini;
    public String etq_fin;
    public int tamanio=0;
    
    public HashMap t;
    
    public TablaSimbolos(String nivel, String tipo,boolean retorno, boolean detener, boolean  continuar){
        t= new HashMap();
        this.tipo=tipo;
        this.Nivel=nivel;
        this.retorno=retorno;
        this.detener=detener;
        this.continuar = continuar;
    }
        
    public void insertar(String nombre,Simbolo simbolo){
        t.put(nombre,simbolo);
        System.out.println("Se inserto una nueva variable: "+nombre+" Nivel : "+Nivel);
    }
    
    public Simbolo retornaSimbolo(String nombre){
        return (Simbolo)t.get(nombre);
    }
    
    public boolean existeSimbolo(String Nombre){ 
        return retornaSimbolo(Nombre)!=null;
    }
            
}
