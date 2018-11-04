/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author Luis
 */
public class Nodo {
    
    public Term Term;
    public Token Token;
    public ArrayList<Nodo> Hijos= new ArrayList<Nodo>();

    public Nodo(String Nombre, Object valor,int fila, int columna) { // Cosntructor para cuando es un token
        Token= new Token(Nombre, valor, fila, columna);
        Term=null;
    }
    
    public Nodo(String nombre){ // Constructor para cuando es un TERM (No terminal)
        Term = new Term(nombre);
        Token=null;
    }

    @Override
    public String toString() {
        if(Term!=null){
            return Term.Nombre;
        }else if(Token!=null){
            String tmp=(String)Token.valor; 
            return Token.Nombre+" : "+tmp.replace("\"","");
        }else{
            return "Null";
        }
         
    }
    
    
    
}
