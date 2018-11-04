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
public class Token {
    String Nombre; // Nombre del terminal o token
    Object valor; // su valor puede variar lo dejaremos como objet para hacer casteos;
    int linea;  // posicion donde esta la fila
    int columna; // posicion donde esta la columna
    
    public Token(String Nombre, Object valor,int linea, int columna){
        this.Nombre=Nombre;
        this.valor=valor;
        this.linea=linea;
        this.columna=columna;
    }

    public String getColumna() {
        return columna+"";
    }
   
    public String getLinea() {
        return linea+"";
    }

    public String getNombre() {
        return Nombre;
    }

    public Object getValor() {
        return valor;
    }
  
    
}
