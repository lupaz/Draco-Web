/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author Luis
 */
public class Term {
       String Nombre;
       public Term(String Nombre){
           this.Nombre=Nombre;
       }
       
       public String getNombre(){
           return Nombre;
       }
}
