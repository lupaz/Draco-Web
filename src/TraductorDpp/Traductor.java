/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TraductorDpp;


import AST.Nodo;
import Dplusplus.*;

import java.util.ArrayList;
import java.util.Stack;
import draco_web.InterfazD;
import static draco_web.InterfazD.listaErrores;
import draco_web.LineasText;
import draco_web.Pintar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;


/**
 *
 * @author Luis
 */
public class Traductor extends Thread{
    
    DefaultHighlighter.DefaultHighlightPainter normalPainter = new DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.decode("#FFFFFF"));
    DefaultHighlighter.DefaultHighlightPainter greenPainter = new DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.decode("#7CBC67"));
    
    public Stack<TablaSimbolos> pilaSimbols; // pila actual
    public TablaSimbolos Global; // TS global de la primera instancia
    public TablaFunciones Funciones; // TB funciones actual
    public TablaEstructuras Estructuras;
    TablaSimbolos cima; // TS que esta en la cima
    ArrayList<Integer> lineasDeb;
    boolean debug;
    boolean detener;
    public boolean debugeando=false; //servira par saber cuadno se esta dandon a la variable continua
    Nodo raiz;
    String archivoActual;
    public JTextArea consola;
    public JTextArea dasm;
    public JTextArea errores;
    public JTable tablaS;
    private DefaultTableModel modelo;
    
    LineasText tmpL;
    int lineatmp=-1;
    public String codigo_generado="//---------------------- Funciones Nativas -------------------------\n";
    
    public Traductor(Nodo raiz,ArrayList<Integer> lineasDeb,String nombArchivo){
        this.raiz=raiz;
        this.lineasDeb=lineasDeb;
        this.debug=true;
        detener=false;
        this.archivoActual= nombArchivo;
        this.pilaSimbols =  new Stack<>();
        this.Funciones = new TablaFunciones();
        this.Estructuras = new TablaEstructuras();
        this.Global = new TablaSimbolos("Global", Cadena.ambito_g, true, false,false);
        pilaSimbols.push(this.Global);
        cima= pilaSimbols.peek();
        codigo_generado+=Generador.funcion_print();
        codigo_generado+=Generador.funcion_concat();
    }
    
    public Traductor(Nodo raiz,String nombArchivo){
        this.raiz=raiz;
        this.debug=false;
        detener=false;
        this.archivoActual= nombArchivo;
        this.pilaSimbols =  new Stack<>();
        this.Funciones = new TablaFunciones();
        this.Estructuras = new TablaEstructuras();
        this.Global = new TablaSimbolos("Global", Cadena.ambito_g, true, false,false);
        pilaSimbols.push(this.Global);
        cima= pilaSimbols.peek();
        codigo_generado+=Generador.funcion_print();
        codigo_generado+=Generador.funcion_concat();
    }
    
    public retorno comprobarExp(Nodo nodo){
        switch (nodo.Term.getNombre()) {
            case Cadena.LOG:
                // <editor-fold defaultstate="collapsed">
                switch (nodo.Hijos.size()) {
                    case 4: // cuando es un SI_SINO simplificado
                        String line = (nodo.Hijos.get(1).Token.getLinea() + 1) + "";
                        String column = (nodo.Hijos.get(1).Token.getColumna() + 1) + "";
                        retorno ret = comprobarExp(nodo.Hijos.get(0));
                        if (ret.tipo.equals(Cadena.booleano)) {
                            if (ret.valor.toString().equals("verdadero") || ret.valor.toString().equals("'verdadero'")) {
                                return comprobarExp(nodo.Hijos.get(2));
                            } else {
                                return comprobarExp(nodo.Hijos.get(3));
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La condicion del SI-SINO simple no retorno un booleano -> COND " + " L: " + line + " C: " + column + " Archivo: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            return new retorno("error", Cadena.error, line, column);
                        }
                    case 3: // Puede ser un OR o AND
                        String line2 = (nodo.Hijos.get(1).Token.getLinea() + 1) + "";
                        String column2 = (nodo.Hijos.get(1).Token.getColumna() + 1) + "";
                        retorno ret2 = comprobarExp(nodo.Hijos.get(0));
                        retorno ret22 = comprobarExp(nodo.Hijos.get(2));
                        if (ret2.tipo.equals(Cadena.booleano) && ret22.tipo.equals(Cadena.booleano)) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()){
                                case "||":
                                    if (ret2.valor.toString().equals("verdadero") || ret2.valor.toString().equals("'verdadero'") || ret22.valor.toString().equals("verdadero") || ret22.valor.toString().equals("'verdadero'")) {
                                        return new retorno("verdadero", Cadena.booleano, line2, column2);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line2, column2);
                                    }
                                case "&&":
                                    if ((ret2.valor.toString().equals("verdadero") || ret2.valor.toString().equals("'verdadero'")) && (ret22.valor.toString().equals("verdadero") || ret22.valor.toString().equals("'verdadero'"))) {
                                        return new retorno("verdadero", Cadena.booleano, line2, column2);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line2, column2);
                                    }
                            }
                        } else {
                            String error = "ERROR SEMANTICO: Una de las expresiones a evaluar en la condicion logica no es de tipo booleano -> EXP " + " L: " + line2 + " C: " + column2 + " Archivo: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            return new retorno("error", Cadena.error, line2, column2);
                        }
                        break;
                    case 1:
                        return comprobarExp(nodo.Hijos.get(0));
                }
                // </editor-fold>
                break;
            case Cadena.REL:
                // <editor-fold defaultstate="collapsed">
                switch (nodo.Hijos.size()) {
                    case 3:
                        String line3 = (nodo.Hijos.get(1).Token.getLinea() + 1) + "";
                        String column3 = (nodo.Hijos.get(1).Token.getColumna() + 1) + "";
                        retorno ret3 = comprobarExp(nodo.Hijos.get(0));
                        retorno ret33 = comprobarExp(nodo.Hijos.get(2));
                        
                        if (((ret3.tipo.equals(Cadena.entero) || ret3.tipo.equals(Cadena.decimal))
                                && (ret33.tipo.equals(Cadena.entero) || ret33.tipo.equals(Cadena.decimal)))) {
                            Double num1 = Double.parseDouble(ret3.valor.toString());
                            Double num2 = Double.parseDouble(ret33.valor.toString());
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":
                                    if (num1 == num2) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                                case "!=":
                                    if (num1 != num2) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                                case ">":
                                    if (num1 > num2) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                                case "<":
                                    if (num1 < num2) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                                case ">=":
                                    if (num1 >= num2) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                                case "<=":
                                    if (num1 <= num2) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                            }
                        }
                        //aca se comparan 2 cadenas
                        else if (ret3.tipo.equals(Cadena.cadena) && ret33.tipo.equals(Cadena.cadena)) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":

                                    return new retorno("falso", Cadena.booleano, line3, column3);
                                case "!=":

                                    return new retorno("falso", Cadena.booleano, line3, column3);

                                case ">":

                                    return new retorno("falso", Cadena.booleano, line3, column3);
                                case "<":

                                    return new retorno("falso", Cadena.booleano, line3, column3);

                                case ">=":

                                    return new retorno("falso", Cadena.booleano, line3, column3);

                                case "<=":

                                    return new retorno("falso", Cadena.booleano, line3, column3);
                            }
                        }
                        else if ( (ret3.tipo.equals(Cadena.caracter) && ret33.tipo.equals(Cadena.caract))
                                || (ret3.tipo.equals(Cadena.booleano) && ret33.tipo.equals(Cadena.booleano)) ) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":
                                    if (ret3.valor.toString().equals(ret33.valor.toString())) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                                case "!=":
                                    if (!ret3.valor.toString().equals(ret33.valor.toString())) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }
                                default:
                                    String error = "ERROR SEMANTICO: Operador invalido para comparar datos del mismo tipo -> " + nodo.Hijos.get(1).Token.getValor().toString() + " L: " + line3 + " C: " + column3 + " Archivo: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                    return new retorno("error", Cadena.error, line3, column3);
                                // error
                                }
                        } else if (ret3.tipo.equals(Cadena.nulo) || ret33.tipo.equals(Cadena.nulo)
                                && (!ret3.tipo.equals(Cadena.error) && !ret33.tipo.equals(Cadena.error))) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":
                                    if (ret3.tipo.equals(Cadena.nulo) && ret33.tipo.equals(Cadena.nulo)) {
                                        return new retorno("verdadero", Cadena.booleano, line3, column3);
                                    }

                                    if (ret3.tipo.equals(Cadena.nulo)) {
                                        if (ret33.valor.toString().equals(Cadena.nulo)) {
                                            return new retorno("verdadero", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("falso", Cadena.booleano, line3, column3);
                                        }
                                    } else {
                                        if (ret3.valor.toString().equals(Cadena.nulo)) {
                                            return new retorno("verdadero", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("falso", Cadena.booleano, line3, column3);
                                        }
                                    }
                                case "!=":
                                    if (ret3.tipo.equals(Cadena.nulo) && ret33.tipo.equals(Cadena.nulo)) {
                                        return new retorno("falso", Cadena.booleano, line3, column3);
                                    }

                                    if (ret3.tipo.equals(Cadena.nulo)) {
                                        if (!ret33.valor.toString().equals(Cadena.nulo)) {
                                            return new retorno("verdadero", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("falso", Cadena.booleano, line3, column3);
                                        }
                                    } else {
                                        if (!ret3.valor.toString().equals(Cadena.nulo)) {
                                            return new retorno("verdadero", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("falso", Cadena.booleano, line3, column3);
                                        }
                                    }
                                default:
                                    String error = "ERROR SEMANTICO: Operador invalido para comparar datos con NULO -> " + nodo.Hijos.get(1).Token.getValor().toString() + " L: " + line3 + " C: " + column3 + " Archivo: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                    return new retorno("error", Cadena.error, line3, column3);
                                // error
                                }
                        } else {
                            String error = "ERROR SEMANTICO: Expresiones no validas en la operacion relacional REL -> " + nodo.Hijos.get(1).Token.getValor().toString() + " L: " + line3 + " C: " + column3 + " Archivo: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            return new retorno("error", Cadena.error, line3, column3);
                        }
                        break;
                    case 1:
                        return comprobarExp(nodo.Hijos.get(0));
                }

                // </editor-fold>
                break;
            case Cadena.ARIT:
                // <editor-fold defaultstate="collapsed">
                switch (nodo.Hijos.size()) {
                    case 3:
                        // <editor-fold defaultstate="collapsed">
                        String line4 = (nodo.Hijos.get(1).Token.getLinea() + 1) + "";
                        String column4 = (nodo.Hijos.get(1).Token.getLinea() + 1) + "";
                        retorno ret4 = comprobarExp(nodo.Hijos.get(0));
                        retorno ret44 = comprobarExp(nodo.Hijos.get(2));
                        if (!ret4.tipo.equals(Cadena.error) && !ret44.tipo.equals(Cadena.error)) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
//======================================SUMA==================================================================>
                                case "+":
                                    // <editor-fold defaultstate="collapsed">
                                    switch(ret4.tipo){
                                        case Cadena.entero:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                    
                                                case Cadena.decimal:
                                              
                                                case Cadena.booleano:

                                                case Cadena.caracter:

                                                case Cadena.cadena:

                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                            // si llego aca fue una operacion valida   
                                        case Cadena.decimal:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.booleano:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.caracter:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.cadena:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' "+ret4.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                // </editor-fold>
//======================================RESTA=================================================================>
                                case "-":
                                // <editor-fold defaultstate="collapsed">
                                    switch(ret4.tipo){
                                        case Cadena.entero:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                    
                                                case Cadena.decimal:
                                              
                                                case Cadena.booleano:

                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                            // si llego aca fue una operacion valida   
                                        case Cadena.decimal:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.booleano:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.caracter:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.cadena:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' "+ret4.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                // </editor-fold>
//======================================MULTI==================================================================>
                                case "*":
                                // <editor-fold defaultstate="collapsed">
                                    switch(ret4.tipo){
                                        case Cadena.entero:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                    
                                                case Cadena.decimal:
                                              
                                                case Cadena.booleano:

                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                            // si llego aca fue una operacion valida   
                                        case Cadena.decimal:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.booleano:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.caracter:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.cadena:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' "+ret4.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                // </editor-fold>
//======================================DIVI==================================================================>
                                case "/":
                                    // <editor-fold defaultstate="collapsed">
                                    switch(ret4.tipo){
                                        case Cadena.entero:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                    
                                                case Cadena.decimal:
                                              
                                                case Cadena.booleano:

                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                            // si llego aca fue una operacion valida   
                                        case Cadena.decimal:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.booleano:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.caracter:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.cadena:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret4.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                // </editor-fold>
//======================================MODULO==================================================================>
                                case "%":
                                    // <editor-fold defaultstate="collapsed">
                                    switch(ret4.tipo){
                                        case Cadena.entero:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                    
                                                case Cadena.decimal:
                                              
                                                case Cadena.booleano:

                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                            // si llego aca fue una operacion valida   
                                        case Cadena.decimal:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.booleano:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.caracter:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.cadena:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' "+ret4.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                // </editor-fold>
//======================================POT==================================================================>
                                case "^":
                                    // <editor-fold defaultstate="collapsed">
                                    //validamos que sean entero con entero
                                    switch(ret4.tipo){
                                        case Cadena.entero:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                    
                                                case Cadena.decimal:
                                              
                                                case Cadena.booleano:

                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                            // si llego aca fue una operacion valida   
                                        case Cadena.decimal:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.booleano:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.caracter:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        case Cadena.cadena:
                                            switch(ret4.tipo){
                                                case Cadena.entero:
                                                case Cadena.decimal:
                                                case Cadena.booleano:
                                                case Cadena.caracter:
                                                case Cadena.cadena:
                                                    
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' "+ret44.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);    
                                            }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' "+ret4.tipo+ " L: " + line4 + " C: " + column4 + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                // </editor-fold>
                                }
                        } else {
                            if (ret4.tipo.equals(Cadena.error)) {
                                return ret4;
                            } else {
                                return ret44;
                            }
                        }
                        // </editor-fold>
                        break;
                    case 2:
                        // <editor-fold defaultstate="collapsed">
                        if (nodo.Hijos.get(0).Token != null){
                            switch(nodo.Hijos.get(0).Token.getValor().toString()){
                                case "-":
                                    retorno ret = comprobarExp(nodo.Hijos.get(1));
                                    if (ret.tipo.equals(Cadena.entero) || ret.tipo.equals(Cadena.decimal)) {
                                        if (ret.tipo.equals(Cadena.entero))//es un entero
                                        {
                                            
                                        } else// es un decimal
                                        {
                                           
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: La expresion a negar aritmeticamente, no es de tipo numerico ->  " + ret.tipo + " L: " + ret.Linea + " C: " + ret.Columna + " Archivo: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                        return new retorno("error", Cadena.error, "0", "0");
                                    }
                                case "!":
                                    retorno ret2 = comprobarExp(nodo.Hijos.get(1));
                                    if (ret2.tipo.equals(Cadena.booleano)) {
                                       
                                        
                                        
                                    } else {
                                        String error = "ERROR SEMANTICO: La expresion a negar logicamente, no es de tipo booleano ->  " + ret2.tipo + " L: " + ret2.Linea + " C: " + ret2.Columna + " Archivo: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                        return new retorno("error", Cadena.error, "0", "0");
                                    }
                                default:      
                            }
                        } // es una expresion ++ y --
                        else {
                            retorno ret = comprobarExp(nodo.Hijos.get(0));
                            if (ret.tipo.equals(Cadena.entero) || ret.tipo.equals(Cadena.entero)) {
                                switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                    case "++":
                                        if (ret.tipo.equals(Cadena.entero)) {
                                           

                                        } else {
                                            
                                            return ret;
                                        }
                                    case "--":
                                        if (ret.tipo.equals(Cadena.entero)) {
                                           
                                        } else {
                                            
                                        }
                                }
                            } else {
                                String error = "ERROR SEMANTICO: El valor  a inc/dec debe ser de tipo numerico ->  " + ret.tipo + " L: " + ret.Linea + " C: " + ret.Columna + " Archivo: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                                return new retorno("error", Cadena.error, "0", "0");
                            }
                        }
                        break;
                    // </editor-fold>
                    //========================Son las funciones especiales==============================
                    case 1:
                        // <editor-fold defaultstate="collapsed">
                        //aca son todos van todos los nodos terminales 
                        if (nodo.Hijos.get(0).Token != null) {
                            switch (nodo.Hijos.get(0).Token.getNombre()){                               
                                    case Cadena.id:
                                    {
                                        //<editor-fold>
                                        String nombre = nodo.Hijos.get(0).Token.getValor().toString();
                                        String linea =  nodo.Hijos.get(0).Token.getLinea();
                                        String columna = nodo.Hijos.get(0).Token.getColumna();                                        
                                        Simbolo sim = existeVariable2(nombre);                                        
                                        if(sim!=null){ //es una variable local
                                            //<editor-fold>
                                            retorno ret= new retorno("", sim.tipo,linea, columna);
                                            ret.tipoDato=sim.TipoObjeto;
                                            ret.cod_generado=Generador.recuperar_val_var_local(sim.posicion,sim.profundidad+"");
                                            return  ret;
                                            //</editor-fold>
                                        }else{ //puede que sea una var global                                            
                                            sim= existeVariable3(nombre);                                            
                                            if(sim!=null){ //es una variable global
                                            //<editor-fold>
                                            retorno ret= new retorno("", sim.tipo,linea, columna);
                                            ret.tipoDato=sim.TipoObjeto;
                                            ret.cod_generado=Generador.recuperar_val_var_global(sim.posicion);
                                            return  ret;
                                            //</editor-fold>
                                            }else{ //la varibale no existe
                                                String error = "ERROR SEMANTICO: La variable a acceder no esta definida ->  " +nombre+ " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                                return new retorno("error", Cadena.error, "0", "0");
                                            }
                                        }
                                        //</editor-fold>
                                    }                                    
                                case Cadena.decimal:
                                {
                                    String linea =  nodo.Hijos.get(0).Token.getLinea();
                                    String columna = nodo.Hijos.get(0).Token.getColumna();
                                    retorno ret= new retorno("",Cadena.decimal,linea, columna);                                   
                                    ret.cod_generado=nodo.Hijos.get(0).Token.getValor().toString()+"\n";
                                    return  ret;                                    
                                }                                    
                                case Cadena.entero:
                                {
                                    String linea =  nodo.Hijos.get(0).Token.getLinea();
                                    String columna = nodo.Hijos.get(0).Token.getColumna();
                                    retorno ret= new retorno("",Cadena.entero,linea, columna);                                   
                                    ret.cod_generado=nodo.Hijos.get(0).Token.getValor().toString()+"\n";
                                    return  ret;
                                }                                    
                                case Cadena.booleano:
                                {
                                    String val = nodo.Hijos.get(0).Token.getValor().toString();
                                    String linea =  nodo.Hijos.get(0).Token.getLinea();
                                    String columna = nodo.Hijos.get(0).Token.getColumna();
                                    retorno ret= new retorno("",Cadena.booleano,linea, columna);                                    
                                    if(val.equals(Cadena.trus)){
                                        ret.cod_generado="1"+"\n";
                                    }else{
                                        ret.cod_generado="0"+"\n";
                                    }
                                    return  ret;
                                }                                    
                                case Cadena.caracter:
                                {
                                    String charmel = nodo.Hijos.get(0).Token.getValor().toString();
                                    String linea =  nodo.Hijos.get(0).Token.getLinea();
                                    String columna = nodo.Hijos.get(0).Token.getColumna();
                                    retorno ret= new retorno("",Cadena.caracter,linea, columna);                                   
                                    ret.cod_generado=((int)charmel.charAt(0))+"\n";
                                    return  ret;
                                }
                                case Cadena.cadena:
                                {
                                    String cad = nodo.Hijos.get(0).Token.getValor().toString();
                                    String linea =  nodo.Hijos.get(0).Token.getLinea();
                                    String columna = nodo.Hijos.get(0).Token.getColumna();
                                    retorno ret= new retorno("",Cadena.cadena,linea, columna);                                   
                                    ret.cod_generado=Generador.inicio_cadena();                                    
                                    for (int i = 0; i <cad.length(); i++) {
                                        ret.cod_generado+=Generador.insertar_caracter((int)cad.charAt(i)+"");
                                    }
                                    ret.cod_generado+=Generador.fin_cadena();
                                    return  ret;
                                }
                                case Cadena.nulo:
                                {
                                    String linea =  nodo.Hijos.get(0).Token.getLinea();
                                    String columna = nodo.Hijos.get(0).Token.getColumna();
                                    retorno ret= new retorno("",Cadena.nulo,linea, columna);                                   
                                    ret.cod_generado="-1"+"\n";
                                    return  ret;
                                }                                 
                            }
                        } //resto de nodos de funncion
                        else {
                            return comprobarExp(nodo.Hijos.get(0));
                        }
                        break;
                    // </editor-fold>
                    }
                // </editor-fold>
                break;
            //RESTO DE FUNCIONES
            case Cadena.OP://esto lo tengo que pasar a las instrucciones de cuerpo
                // <editor-fold defaultstate="collapsed">
                //es un id
                if (nodo.Hijos.get(0).Token != null) {
                    String name = nodo.Hijos.get(0).Token.getValor().toString();
                    String line = (nodo.Hijos.get(0).Token.getLinea() + 1) + "";
                    String column = (nodo.Hijos.get(0).Token.getColumna() + 1) + "";
                    Simbolo tmp01 = existeVariable2(name);
                    if (tmp01 != null) {
                        if (tmp01.TipoObjeto.equals("")) {
                            if (tmp01.tipo.equals(Cadena.entero) || tmp01.tipo.equals(Cadena.decimal)) {
                                switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                    case "++":
                                        if (tmp01.tipo.equals(Cadena.entero)) {

                                            return new retorno("", Cadena.entero, line, column);
                                        } else {

                                            return new retorno("", Cadena.decimal, line, column);
                                        }
                                    case "--":
                                        if (tmp01.tipo.equals(Cadena.entero)) {
                                           
                                            return new retorno("", Cadena.entero, line, column);
                                        } else {
                                           
                                            return new retorno("", Cadena.decimal, line, column);
                                        }
                                }
                            } else {
                                String error = "ERROR SEMANTICO: La variable a inc/dec debe ser de tipo numerico ->  " + name + " L: " + line + " C: " + column;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                                return new retorno("error", Cadena.error, "0", "0");
                            }

                        } else {
                            String error = "ERROR SEMANTICO: La variable a acceder no es primitiva ->  " + name + " L: " + line + " C: " + column;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            return new retorno("error", Cadena.error, "0", "0");
                        }
                    } else { // variable no existe
                        String error = "ERROR SEMANTICO: La variable a acceder no esta definida ->  " + name + " L: " + line + " C: " + column;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                        return new retorno("error", Cadena.error, "0", "0");
                    }
                }
                // </editor-fold>
                break;
            case Cadena.LLAMADA:              
                //<editor-fold>
                break;
                //</editor-fold>
            case Cadena.ACC_EST:{ // acceso tipo est.nombre
                //<editor-fold>
                String codigo_dasm="";
                String nombre= nodo.Hijos.get(0).Token.getValor().toString();
                String linea= nodo.Hijos.get(0).Token.getLinea();
                String columna = nodo.Hijos.get(0).Token.getColumna();                
                Simbolo sim = existeVariable2(nombre);
                if(sim!=null){ //es una variable local 
                    //<editor-fold>
                        if(sim.TipoObjeto.equals(Cadena.estructura)){ //comprobamos que sea  una estrcutura
                            Estructura est= Estructuras.retornaEstructura(sim.tipo);                            
                            if(est!=null){                                
                                String nombre2=  nodo.Hijos.get(1).Token.getValor().toString();
                                columna = nodo.Hijos.get(1).Token.getColumna();
                                Simbolo sim2= est.getAtributo(nombre2); 
                                if(sim2!=null){                                                                       
                                    codigo_dasm+=Generador.recuperar_val_est_local(sim.posicion, sim2.posicion ,sim.profundidad+"");
                                    retorno ret = new retorno("", sim2.tipo, linea, columna);
                                    ret.tipoDato =sim2.TipoObjeto;
                                    ret.cod_generado = codigo_dasm;
                                    return ret;
                                }else{
                                    String error = "ERROR SEMANTICO: El atributo al que desea acceder no es parte de la estrcutura definida -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }                                
                            }else{
                                String error = "ERROR SEMANTICO: El tipo de estructura de la variable no esta definido  -> " + sim.tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                           
                        }else{
                            String error = "ERROR SEMANTICO: La variable a acceder no es una estrcutura -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                        
                        }                    
                    //</editor-fold>
                }else{ 
                    sim = existeVariable3(nombre);                    
                    if(sim!=null){ //es una variable global 
                        //<editor-fold>
                        if (sim.TipoObjeto.equals(Cadena.estructura)) { //comprobamos que sea  una estrcutura
                            Estructura est = Estructuras.retornaEstructura(sim.tipo);
                            if (est != null) {
                                String nombre2 = nodo.Hijos.get(1).Token.getValor().toString();
                                columna = nodo.Hijos.get(1).Token.getColumna();
                                Simbolo sim2 = est.getAtributo(nombre2);
                                if (sim2 != null) {
                                    codigo_dasm+=Generador.recuperar_val_est_global(sim.posicion, sim2.posicion);
                                    retorno ret = new retorno("", sim2.tipo, linea, columna);
                                    ret.tipoDato =sim2.TipoObjeto;
                                    ret.cod_generado = codigo_dasm;
                                    return ret;
                                } else {
                                    String error = "ERROR SEMANTICO: El atributo al que desea acceder no es parte de la estrcutura definida -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else {
                                String error = "ERROR SEMANTICO: El tipo de estructura de la variable no esta definido  -> " + sim.tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La variable a acceder no es una estrcutura -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        //</editor-fold>
                    }else{
                        String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }                                       
                }
                break;                
                //</editor-fold>
            }
            case Cadena.ACC_MAT_EST:{ //acceso tipo est.edad[2]
                //<editor-fold>
                String codigo_dasm="";
                String nombre= nodo.Hijos.get(0).Token.getValor().toString();
                String linea= nodo.Hijos.get(0).Token.getLinea();
                String columna = nodo.Hijos.get(0).Token.getColumna();                
                Simbolo sim = existeVariable2(nombre);
                if(sim!=null){ //es una variable local 
                    //<editor-fold>
                        if(sim.TipoObjeto.equals(Cadena.estructura)){ //comprobamos que sea  una estrcutura
                            Estructura est= Estructuras.retornaEstructura(sim.tipo);                            
                            if(est!=null){                                
                                String nombre2=  nodo.Hijos.get(1).Token.getValor().toString();
                                columna = nodo.Hijos.get(1).Token.getColumna();
                                Simbolo sim2= est.getAtributo(nombre2); 
                                if(sim2!=null){                                    
                                    if(sim2.TipoObjeto.equals(Cadena.arreglo)){
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        capturarEXP(val_dims, nodo.Hijos.get(2));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            if (sim2.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                                String et_error = Generador.generar_etq();
                                                String et_correcto = Generador.generar_etq();
                                                //codigo para comprobar los indices en ejecucion
                                                for (int i = 0; i < val_dims.size(); i++) {
                                                    codigo_dasm += Generador.comprobarIndice_est_local(sim.posicion, (i + 1) + "", sim2.posicion, sim.profundidad + "", val_dims.get(i).cod_generado, et_error);
                                                }
                                                // codigo para linealizar    
                                                for (int i = 0; i < val_dims.size(); i++) {
                                                    retorno ret = val_dims.get(i);
                                                    if (i == 0) {
                                                        codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                    } else {
                                                        codigo_dasm += Generador.linealizar_arreglo_est_Ndim_local(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion, sim2.posicion, sim.profundidad + "");
                                                    }
                                                }
                                                //fin del arreglo
                                                codigo_dasm += Generador.recuperar_val_Arreglo_est_local(sim2.numDims + "", sim.posicion, sim2.posicion, sim.profundidad + "");
                                                
                                                codigo_dasm += Cadena.br + et_correcto + "\n";
                                                //ahora lo del error 
                                                codigo_dasm += et_error + ":\n";                                                
                                                String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                String cad2 = generarCodCad(sim2.nombre + "\n");
                                                //llamo a concat
                                                String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                codigo_dasm += "//------------------llamada a print ----------------\n";
                                                codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                //llamo al funcion print
                                                codigo_dasm += "//----------------------------------------------------\n";
                                                codigo_dasm += et_correcto + ":\n";
                                                retorno ret = new retorno("", sim2.tipo, linea, columna);
                                                ret.cod_generado = codigo_dasm;
                                                return ret;
                                            } else {
                                                String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    }else{
                                        String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }                                                                       
                                }else{
                                    String error = "ERROR SEMANTICO: El atributo al que desea acceder no es parte de la estrcutura definida -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }                                
                            }else{
                                String error = "ERROR SEMANTICO: El tipo de estructura de la variable no esta definido  -> " + sim.tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                           
                        }else{
                            String error = "ERROR SEMANTICO: La variable a acceder no es una estrcutura -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                        
                        }                    
                    //</editor-fold>
                }else{ 
                    sim = existeVariable3(nombre);                    
                    if(sim!=null){ //es una variable global 
                        //<editor-fold>
                        if (sim.TipoObjeto.equals(Cadena.estructura)) { //comprobamos que sea  una estrcutura
                            Estructura est = Estructuras.retornaEstructura(sim.tipo);
                            if (est != null) {
                                String nombre2 = nodo.Hijos.get(1).Token.getValor().toString();
                                columna = nodo.Hijos.get(1).Token.getColumna();
                                Simbolo sim2 = est.getAtributo(nombre2);
                                if (sim2 != null) {
                                    if (sim2.TipoObjeto.equals(Cadena.arreglo)) {
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        capturarEXP(val_dims, nodo.Hijos.get(2));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            if (sim2.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                                String et_error = Generador.generar_etq();
                                                String et_correcto = Generador.generar_etq();
                                                //codigo para comprobar los indices en ejecucion
                                                for (int i = 0; i < val_dims.size(); i++) {
                                                    codigo_dasm += Generador.comprobarIndice_est_global((i + 1) + "", sim.posicion, sim2.posicion, val_dims.get(i).cod_generado, et_error);
                                                }
                                                // codigo para linealizar    
                                                for (int i = 0; i < val_dims.size(); i++) {
                                                    retorno ret = val_dims.get(i);
                                                    if (i == 0) {
                                                        codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                    } else {
                                                        codigo_dasm += Generador.linealizar_arreglo_est_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion, sim2.posicion);
                                                    }
                                                }
                                                //fin del arreglo
                                                codigo_dasm += Generador.recuperar_val_Arreglo_est_global(sim2.numDims + "", sim.posicion, sim2.posicion);
                                                //ahora lo del error 
                                                codigo_dasm += Cadena.br + et_correcto + "\n";
                                                codigo_dasm += et_error + ":\n";
                                                String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                String cad2 = generarCodCad(sim2.nombre + "\n");
                                                //llamo a concat
                                                String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                codigo_dasm += "//------------------llamada a print ----------------\n";
                                                codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                //llamo al funcion print
                                                codigo_dasm += "//----------------------------------------------------\n";
                                                codigo_dasm += et_correcto + ":\n";
                                                retorno ret = new retorno("", sim2.tipo, linea, columna);
                                                ret.cod_generado = codigo_dasm;
                                                return ret;
                                            } else {
                                                String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: El atributo al que desea acceder no es parte de la estrcutura definida -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else {
                                String error = "ERROR SEMANTICO: El tipo de estructura de la variable no esta definido  -> " + sim.tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La variable a acceder no es una estrcutura -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        //</editor-fold>
                    }else{
                        String error = "ERROR SEMANTICO: La variable no se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }                                       
                }                
                break;
                //</editor-fold>
            }                    
            case Cadena.ACC_MAT:{ //acceso tipo mat[2][4]
                //<editor-fold>
                String codigo_dasm="";
                String nombre= nodo.Hijos.get(0).Token.getValor().toString();
                String linea= nodo.Hijos.get(0).Token.getLinea();
                String columna = nodo.Hijos.get(0).Token.getColumna();                
                Simbolo sim = existeVariable2(nombre);
                if(sim!=null){ //es una variable local 
                    //<editor-fold>
                        if(sim.TipoObjeto.equals(Cadena.arreglo)){ //comprobamos que sea  un arreglo
                            ArrayList<retorno> val_dims = new ArrayList<>();
                            capturarEXP(val_dims, nodo.Hijos.get(1));                            
                            if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                    String et_error = Generador.generar_etq();
                                    String et_correcto = Generador.generar_etq();
                                    //codigo para comprobar los indices en ejecucion
                                    for (int i = 0; i < val_dims.size(); i++) {
                                        codigo_dasm += Generador.comprobarIndice_local((i + 1) + "", sim.posicion, sim.profundidad + "", val_dims.get(i).cod_generado, et_error);
                                    }
                                    // codigo para linealizar    
                                    for (int i = 0; i < val_dims.size(); i++) {
                                        retorno ret = val_dims.get(i);
                                        if (i == 0) {
                                            codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                        } else {
                                            codigo_dasm += Generador.linealizar_arreglo_Ndim_local(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion, sim.profundidad + "");
                                        }
                                    }
                                    //fin del arreglo
                                    codigo_dasm += Generador.recuperar_val_Arreglo_local(sim.numDims + "", sim.posicion, sim.profundidad + "");
                                    //ahora lo del error 
                                    codigo_dasm += Cadena.br + et_correcto + "\n";
                                    codigo_dasm += et_error + ":\n";
                                    String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                    String cad2 = generarCodCad(sim.nombre + "\n");
                                    //llamo a concat
                                    String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                    codigo_dasm += "//------------------llamada a print ----------------\n";
                                    codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                    //llamo al funcion print
                                    codigo_dasm += "//----------------------------------------------------\n";
                                    codigo_dasm += et_correcto + ":\n";
                                    retorno ret = new retorno("", sim.tipo, linea, columna);
                                    ret.cod_generado = codigo_dasm;
                                    return ret;
                                } else {
                                    String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else {
                                String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                             
                        }else{
                            String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                        
                        }                    
                    //</editor-fold>
                }else{ 
                    sim = existeVariable3(nombre);                    
                    if(sim!=null){ //es una variable global 
                        //<editor-fold>
                        if (sim.TipoObjeto.equals(Cadena.arreglo)) { //comprobamos que sea  un arreglo
                            ArrayList<retorno> val_dims = new ArrayList<>();
                            capturarEXP(val_dims, nodo.Hijos.get(1));
                            if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                    String et_error = Generador.generar_etq();
                                    String et_correcto = Generador.generar_etq();
                                    //codigo para comprobar los indices en ejecucion
                                    for (int i = 0; i < val_dims.size(); i++) {
                                        codigo_dasm += Generador.comprobarIndice_global((i + 1) + "", sim.posicion, val_dims.get(i).cod_generado, et_error);
                                    }
                                    // codigo para linealizar    
                                    for (int i = 0; i < val_dims.size(); i++) {
                                        retorno ret = val_dims.get(i);
                                        if (i == 0) {
                                            codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                        } else {
                                            codigo_dasm += Generador.linealizar_arreglo_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion);
                                        }
                                    }
                                    //fin del arreglo
                                    codigo_dasm += Generador.recuperar_val_Arreglo_global(sim.numDims + "", sim.posicion);
                                    //ahora lo del error 
                                    codigo_dasm += Cadena.br + et_correcto + "\n";
                                    codigo_dasm += et_error + ":\n";
                                    String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                    String cad2 = generarCodCad(sim.nombre + "\n");
                                    //llamo a concat
                                    String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                    codigo_dasm += "//------------------llamada a print ----------------\n";
                                    codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                    //llamo al funcion print
                                    codigo_dasm += "//----------------------------------------------------\n";
                                    codigo_dasm += et_correcto + ":\n";
                                    retorno ret = new retorno("", sim.tipo, linea, columna);
                                    ret.cod_generado = codigo_dasm;
                                    return ret;
                                } else {
                                    String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            }else{
                                String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            } 
                        } else {
                            String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        //</editor-fold>
                    }else{
                        String error = "ERROR SEMANTICO: La variable no se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }                                       
                }                
                break;
                //</editor-fold>
            }                
            case Cadena.ACC_MAT2: {// acceso tipo arr[0][2].nombre :)
                //<editor-fold> 
                String codigo_dasm="";
                String nombre= nodo.Hijos.get(0).Token.getValor().toString();
                String linea= nodo.Hijos.get(0).Token.getLinea();
                String columna = nodo.Hijos.get(0).Token.getColumna();                
                Simbolo sim = existeVariable2(nombre);
                if(sim!=null){ //es una variable local 
                    //<editor-fold>
                        if(sim.TipoObjeto.equals(Cadena.arreglo)){ //comprobamos que sea  un arreglo
                            Estructura est= Estructuras.retornaEstructura(sim.tipo);
                            if(est!=null){
                                String nombre2= nodo.Hijos.get(2).Token.getValor().toString();
                                columna = nodo.Hijos.get(2).Token.getColumna();
                                Simbolo sim2= est.getAtributo(nombre2);                                
                                if(sim2!=null){
                                    ArrayList<retorno> val_dims = new ArrayList<>();
                                    capturarEXP(val_dims, nodo.Hijos.get(1));
                                    if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                        if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                            String et_error = Generador.generar_etq();
                                            String et_correcto = Generador.generar_etq();
                                            //codigo para comprobar los indices en ejecucion
                                            for (int i = 0; i < val_dims.size(); i++) {
                                                codigo_dasm += Generador.comprobarIndice_local((i + 1) + "", sim.posicion, sim.profundidad + "", val_dims.get(i).cod_generado, et_error);
                                            }
                                            // codigo para linealizar    
                                            for (int i = 0; i < val_dims.size(); i++) {
                                                retorno ret = val_dims.get(i);
                                                if (i == 0) {
                                                    codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                } else {
                                                    codigo_dasm += Generador.linealizar_arreglo_Ndim_local(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion, sim.profundidad + "");
                                                }
                                            }
                                            //fin del arreglo
                                            codigo_dasm += Generador.recuperar_val_est_Arreglo_local(sim.numDims + "", sim.posicion, sim.profundidad + "", sim2.posicion);
                                            //ahora lo del error 
                                            codigo_dasm += Cadena.br + et_correcto + "\n";
                                            codigo_dasm += et_error + ":\n";
                                            String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                String cad2 = generarCodCad(sim.nombre + "\n");
                                                //llamo a concat
                                                String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                codigo_dasm += "//------------------llamada a print ----------------\n";
                                                codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                //llamo al funcion print
                                                codigo_dasm += "//----------------------------------------------------\n";
                                            codigo_dasm += et_correcto + ":\n";
                                            retorno ret = new retorno("", sim2.tipo, linea, columna);
                                            ret.tipoDato = sim2.TipoObjeto;
                                            ret.cod_generado = codigo_dasm;
                                            return ret;
                                        } else {
                                            String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    }else{
                                        String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                }else{
                                    String error = "ERROR SEMANTICO: El atributo de estructura al que desea acceder no esta definido -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            }else{
                                String error = "ERROR SEMANTICO: El arreglo al que inteta  acceder no es de estrcuturas -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                                                        
                        }else{
                            String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                        
                        }                    
                    //</editor-fold>
                }else{ 
                    sim = existeVariable3(nombre);                    
                    if(sim!=null){ //es una variable global 
                        //<editor-fold>
                        if(sim.TipoObjeto.equals(Cadena.arreglo)){ //comprobamos que sea  un arreglo
                            Estructura est= Estructuras.retornaEstructura(sim.tipo);
                            if(est!=null){
                                String nombre2= nodo.Hijos.get(2).Token.getValor().toString();
                                columna = nodo.Hijos.get(2).Token.getColumna();
                                Simbolo sim2= est.getAtributo(nombre2);                                
                                if(sim2!=null){
                                    ArrayList<retorno> val_dims = new ArrayList<>();
                                    capturarEXP(val_dims, nodo.Hijos.get(1));
                                    if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                        if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                            String et_error = Generador.generar_etq();
                                            String et_correcto = Generador.generar_etq();
                                            //codigo para comprobar los indices en ejecucion
                                            for (int i = 0; i < val_dims.size(); i++) {
                                                codigo_dasm += Generador.comprobarIndice_global((i + 1) + "", sim.posicion, val_dims.get(i).cod_generado, et_error);
                                            }
                                            // codigo para linealizar    
                                            for (int i = 0; i < val_dims.size(); i++) {
                                                retorno ret = val_dims.get(i);
                                                if (i == 0) {
                                                    codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                } else {
                                                    codigo_dasm += Generador.linealizar_arreglo_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion);
                                                }
                                            }
                                            //fin del arreglo
                                            codigo_dasm += Generador.recuperar_val_est_Arreglo_global(sim.numDims + "", sim.posicion, sim2.posicion);
                                            //ahora lo del error 
                                            codigo_dasm += Cadena.br + et_correcto + "\n";
                                            codigo_dasm += et_error + ":\n";
                                            String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                String cad2 = generarCodCad(sim.nombre + "\n");
                                                //llamo a concat
                                                String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                codigo_dasm += "//------------------llamada a print ----------------\n";
                                                codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                //llamo al funcion print
                                                codigo_dasm += "//----------------------------------------------------\n";
                                            codigo_dasm += et_correcto + ":\n";
                                            retorno ret = new retorno("", sim2.tipo, linea, columna);
                                            ret.tipoDato = sim2.TipoObjeto;
                                            ret.cod_generado = codigo_dasm;
                                            return ret;
                                        } else {
                                            String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    }else{
                                        String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                }else{
                                    String error = "ERROR SEMANTICO: El atributo de estructura al que desea acceder no esta definido -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            }else{
                                String error = "ERROR SEMANTICO: El arreglo al que inteta  acceder no es de estrcuturas -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                                                        
                        }else{
                            String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                        
                        }
                        //</editor-fold>
                    }else{
                        String error = "ERROR SEMANTICO: La variable no se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }                                       
                }     
                break;            
                //</editor-fold>
            }
            case Cadena.ACC_MAT3:{ // acceso tipo arr[1][3].edad[5] :)
                //<editor-fold>
                String codigo_dasm="";
                String nombre= nodo.Hijos.get(0).Token.getValor().toString();
                String linea= nodo.Hijos.get(0).Token.getLinea();
                String columna = nodo.Hijos.get(0).Token.getColumna();                
                Simbolo sim = existeVariable2(nombre);
                if(sim!=null){ //es una variable local 
                    //<editor-fold>
                        if(sim.TipoObjeto.equals(Cadena.arreglo)){ //comprobamos que sea  un arreglo
                            Estructura est= Estructuras.retornaEstructura(sim.tipo);
                            if(est!=null){
                                String nombre2= nodo.Hijos.get(2).Token.getValor().toString();
                                columna = nodo.Hijos.get(2).Token.getColumna();
                                Simbolo sim2= est.getAtributo(nombre2);                                
                                if(sim2!=null){                                    
                                    if(sim2.TipoObjeto.equals(Cadena.arreglo)){
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        capturarEXP(val_dims, nodo.Hijos.get(1));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                                ArrayList<retorno> val_dims2 = new ArrayList<>();
                                                capturarEXP(val_dims2, nodo.Hijos.get(3));
                                                if (validarTipos(val_dims2) && val_dims2.get(0).tipo.equals(Cadena.entero)) {
                                                    if (sim2.numDims == val_dims2.size()) {
                                                        String et_error = Generador.generar_etq();
                                                        String et_correcto = Generador.generar_etq();
                                                        //codigo para comprobar los indices en ejecucion
                                                        for (int i = 0; i < val_dims.size(); i++) {
                                                            codigo_dasm += Generador.comprobarIndice_local((i + 1) + "", sim.posicion, sim.profundidad + "", val_dims.get(i).cod_generado, et_error);
                                                        }
                                                        // codigo para linealizar    
                                                        for (int i = 0; i < val_dims.size(); i++) {
                                                            retorno ret = val_dims.get(i);
                                                            if (i == 0) {
                                                                codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                            } else {
                                                                codigo_dasm += Generador.linealizar_arreglo_Ndim_local(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion, sim.profundidad + "");
                                                            }
                                                        }
                                                        //fin del 1er arreglo
                                                        codigo_dasm += Generador.recuperar_val_est_Arr_arr_local(sim.numDims + "", sim.posicion, sim.profundidad + "", sim2.posicion);

                                                        //ahora hacemos el acceso del 2do arreglo                                                
                                                        for (int i = 0; i < val_dims2.size(); i++) {
                                                            codigo_dasm += Generador.comprobarIndice_Arr_arr_loc_gob((i + 1) + "", val_dims2.get(i).cod_generado, et_error);
                                                        }
                                                        // codigo para linealizar    
                                                        for (int i = 0; i < val_dims2.size(); i++) {
                                                            if (i == 0) {
                                                                codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims2.get(i).cod_generado);
                                                            } else {
                                                                codigo_dasm += Generador.linealizar_Arr_arr_Ndim_loc_gob(val_dims2.get(i).cod_generado, (i + 1) + "");
                                                            }
                                                        }
                                                        codigo_dasm += Generador.recuperar_val_Arr_arr_loc_gob(sim2.numDims + "");
                                                        //ahora lo del error 
                                                        codigo_dasm += Cadena.br + et_correcto + "\n";
                                                        codigo_dasm += et_error + ":\n";
                                                        String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                            String cad2 = generarCodCad(sim.nombre+"|"+sim2.nombre+ "\n");
                                                            //llamo a concat
                                                            String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                            codigo_dasm += "//------------------llamada a print ----------------\n";
                                                            codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                            //llamo al funcion print
                                                            codigo_dasm += "//----------------------------------------------------\n";
                                                        codigo_dasm += et_correcto + ":\n";
                                                        retorno ret = new retorno("", sim2.tipo, linea, columna);
                                                        ret.cod_generado = codigo_dasm;
                                                        return ret;
                                                    } else {
                                                        String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo de la estructura a acceder, es incorrecto  -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                    }
                                                } else {
                                                    String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }
                                            } else {
                                                String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    }else{
                                        String error = "ERROR SEMANTICO: El atributo de la estructura a acceder no es de tipo arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);                                    
                                    }
                                }else{
                                    String error = "ERROR SEMANTICO: El atributo de estructura al que desea acceder no esta definido -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            }else{
                                String error = "ERROR SEMANTICO: El arreglo al que inteta  acceder no es de estrcuturas -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                                                        
                        }else{
                            String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                        
                        }                    
                    //</editor-fold>
                }else{ 
                    sim = existeVariable3(nombre);                    
                    if(sim!=null){ //es una variable global 
                        //<editor-fold>
                        if(sim.TipoObjeto.equals(Cadena.arreglo)){ //comprobamos que sea  un arreglo
                            Estructura est= Estructuras.retornaEstructura(sim.tipo);
                            if(est!=null){
                                String nombre2= nodo.Hijos.get(2).Token.getValor().toString();
                                columna = nodo.Hijos.get(2).Token.getColumna();
                                Simbolo sim2= est.getAtributo(nombre2);                                
                                if(sim2!=null){
                                    if(sim2.TipoObjeto.equals(Cadena.arreglo)){
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        capturarEXP(val_dims, nodo.Hijos.get(1));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones 
                                                ArrayList<retorno> val_dims2 = new ArrayList<>();
                                                capturarEXP(val_dims2, nodo.Hijos.get(3));
                                                if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                                    if (sim2.numDims == val_dims2.size()) {
                                                        String et_error = Generador.generar_etq();
                                                        String et_correcto = Generador.generar_etq();
                                                        //codigo para comprobar los indices en ejecucion
                                                        for (int i = 0; i < val_dims.size(); i++) {
                                                            codigo_dasm += Generador.comprobarIndice_global((i + 1) + "", sim.posicion, val_dims.get(i).cod_generado, et_error);
                                                        }
                                                        // codigo para linealizar    
                                                        for (int i = 0; i < val_dims.size(); i++) {
                                                            retorno ret = val_dims.get(i);
                                                            if (i == 0) {
                                                                codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                            } else {
                                                                codigo_dasm += Generador.linealizar_arreglo_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion);
                                                            }
                                                        }
                                                        //fin del arreglo
                                                        codigo_dasm += Generador.recuperar_val_est_Arr_arr_global(sim.numDims + "", sim.posicion, sim2.posicion);

                                                        //ahora hacemos el acceso del 2do arreglo                                                
                                                        for (int i = 0; i < val_dims2.size(); i++) {
                                                            codigo_dasm += Generador.comprobarIndice_Arr_arr_loc_gob((i + 1) + "", val_dims2.get(i).cod_generado, et_error);
                                                        }
                                                        // codigo para linealizar    
                                                        for (int i = 0; i < val_dims2.size(); i++) {
                                                            if (i == 0) {
                                                                codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims2.get(i).cod_generado);
                                                            } else {
                                                                codigo_dasm += Generador.linealizar_Arr_arr_Ndim_loc_gob(val_dims.get(i).cod_generado, (i + 1) + "");
                                                            }
                                                        }
                                                        codigo_dasm += Generador.recuperar_val_Arr_arr_loc_gob(sim2.numDims + "");
                                                        //ahora lo del error 
                                                        codigo_dasm += Cadena.br + et_correcto + "\n";
                                                        codigo_dasm += et_error + ":\n";
                                                        String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                            String cad2 = generarCodCad(sim.nombre+"|"+sim2.nombre+ "\n");
                                                            //llamo a concat
                                                            String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                            codigo_dasm += "//------------------llamada a print ----------------\n";
                                                            codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                            //llamo al funcion print
                                                            codigo_dasm += "//----------------------------------------------------\n";
                                                        codigo_dasm += et_correcto + ":\n";
                                                        retorno ret = new retorno("", sim2.tipo, linea, columna);
                                                        ret.cod_generado = codigo_dasm;
                                                        return ret;
                                                    } else {
                                                        String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo de la estructura a acceder, es incorrecto  -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                    }
                                                } else {
                                                    String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }
                                            } else {
                                                String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }                                        
                                    }else{
                                        String error = "ERROR SEMANTICO: El atributo de la estructura a acceder no es de tipo arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);                                         
                                    }  
                                }else{
                                    String error = "ERROR SEMANTICO: El atributo de estructura al que desea acceder no esta definido -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            }else{
                                String error = "ERROR SEMANTICO: El arreglo al que inteta  acceder no es de estrcuturas -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                                                        
                        }else{
                            String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                        
                        }
                        //</editor-fold>
                    }else{
                        String error = "ERROR SEMANTICO: La variable No se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }                                       
                }     
                break; 
                //</editor-fold>
            }
        }        
        return new retorno(Cadena.error,Cadena.error,"01001","01001");
    }
    
    public void traducir(Nodo nodo){ 
        switch (nodo.Term.getNombre()){        
//===================================Asignacion de una Estructura ==============================                
            case Cadena.AS_VAR_EST:
                // <editor-fold defaultstate="collapsed">
                
                // </editor-fold>
                break;
//===================================Asignacion de una Estructura ==============================                
            case Cadena.AS_ARR_EST:
                // <editor-fold defaultstate="collapsed">
                
                // </editor-fold>
                break;                
//===================================Asignacion de un Arreglo ==================================                
            case Cadena.AS_ARR:
                // <editor-fold defaultstate="collapsed">
                
                // </editor-fold>
                break;
//===================================Continuar =================================================
            case Cadena.CONTINUAR:
                // <editor-fold defaultstate="collapsed">
                
                // </editor-fold>
                break;                
//===================================Declaracioon y Asignacion de Arreglo ======================                
            case Cadena.DA_ARR:
                // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//===================================Declaracioon de una estructura =============================
            case Cadena.DEC_EST:
                // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//===================================Definicion de una funcion ==================================            
            case Cadena.DEC_FUN:
                // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//===================================Definicion de un metodo ====================================                 
            case Cadena.DEC_MET:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;                
//=================================== Sentencia Detener ====================================                 
            case Cadena.DETENER:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//=================================== Sentencia imprimir ====================================                 
            case Cadena.IMPRIMIR:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;                
//=================================== Inicio de la ejecucion ====================================                 
            case Cadena.INICIO:
                // <editor-fold defaultstate="collapsed">
                //comprobamos el import
                if(nodo.Hijos.get(0).Hijos.size()>0){
                    for (Nodo hijo : nodo.Hijos.get(0).Hijos) {
                        String ruta=InterfazD.rutaGenesis+hijo.Token.getValor().toString();
                        File fichero = new File(ruta);
                        if (fichero.exists()) {
                            String texto = leerArchivo(ruta);
                            try {
                                Scan_Dplus scanner = new Scan_Dplus(new BufferedReader(new StringReader(texto)));
                                LineasText tmpL = new LineasText();
                                tmpL.text_pane.setText(texto);
                                scanner.pintar = new Pintar(tmpL.sc, tmpL.doc);
                                scanner.nomArch = hijo.Token.getValor().toString();
                                Par_Dplus parser = new Par_Dplus(scanner);
                                parser.nombreArch = hijo.Token.getValor().toString();
                                parser.parse();
                                if (notificarErrrores()) {
                                    notificar("AVISO : Existen errores Lexicos/Sintacticos, ver consola de errores.");
                                    listaErrores.clear();
                                }
                                //
                                String tmp=archivoActual;
                                archivoActual= hijo.Token.getValor().toString();
                                traducir(parser.raiz); //                               
                                archivoActual=tmp; //restablecemos el archivo que estaba antes
                                if (notificarErrrores()) {
                                    notificar("AVISO : Existen errores Lexicos/Sintacticos, ver consola de errores.");
                                    listaErrores.clear();
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            String error = "ERROR SEMANTICO: El archivo que desa importar no existe -> " + hijo.Token.getValor().toString() +" L: " + hijo.Token.getLinea() + " C: " + hijo.Token.getColumna() + " Archivo: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                    }
                }
                //capturamos el tamaño del ambito global
                cima.tamanio=0; //en teoria no ocupa nada de espacio en el heap lo global//calcularTamano(nodo.Hijos.get(1));
                //Capturamos y traducimos las estrcuturas y las funciones
                capturarEstrcuts(nodo.Hijos.get(1));
                //ahora capturamos las variables globales
                capturarGlobales(nodo.Hijos.get(1));
                // </editor-fold>
                break;
//=================================== funcion nativa linea ====================================                 
            case Cadena.LINEA:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//=================================== Llamada a metodos o funciones ====================================                 
            case Cadena.LLAMADA:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;                
//=================================== Sentencia Mietras ====================================                 
            case Cadena.MIENTRAS:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//=================================== Sentencia incremento/decremento===================================                 
            case Cadena.OP:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//=================================== Funcion nativa Ovalo ====================================                 
            case Cadena.OVALO:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//=================================== Sentencia PARA ====================================                 
            case Cadena.PARA:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;      
//=================================== Metodo principal ====================================                 
            case Cadena.PRINCIPAL:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//=================================== Funcion nativa PUNTO ====================================                 
            case Cadena.PUNTO:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;
//=================================== Sentencia retornar ====================================                 
            case Cadena.RETORNAR:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;            
//=================================== Sentencia  ================================================                 
            case Cadena.SELECT:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;                
//=================================== Sentencia SI ====================================                 
            case Cadena.SI:
                 // <editor-fold defaultstate="collapsed">

                // </editor-fold>
                break;                               
        }
    }
   
    private void capturarEstrcuts(Nodo nodo){
        for (Nodo Hijo : nodo.Hijos) {
            if(Hijo!=null){
                switch (Hijo.Term.getNombre()) {
                    case Cadena.STRUCT: {
                        int pos = 0;
                        //Creamos la estrcutura
                        String Codigo_dasm = "";
                        String nombre = Hijo.Hijos.get(0).Token.getValor().toString();
                        String linea = Hijo.Hijos.get(0).Token.getLinea();
                        String columna = Hijo.Hijos.get(0).Token.getColumna();
                        //insertamos el encabezado a la tabla sims
                        if (!Estructuras.existeEstructura(nombre)) {
                            Estructura Est = new Estructura(nombre);
                            Simbolo estr = new Simbolo("e_" + nombre, Hijo.Hijos.get(1).Hijos.size() + "", Cadena.estructura, nombre, "-", linea, columna);
                            cima.insertar(estr.nombre, estr);
                            //<editor-fold desc="generacion DASM">
                            Codigo_dasm += Cadena.codigo_estr;
                            Codigo_dasm += Cadena.Function + Cadena.inicio_ + nombre + "\n";
                            Codigo_dasm += Cadena.get_local_0 + "\n";
                            Codigo_dasm += Cadena.get_local_1 + "\n";
                            Codigo_dasm += Cadena.set_local_calc + "\n"; //dejamos en la pos 0 el valor donde inicia la estructura
                            Codigo_dasm += Cadena.get_local_1 + "\n"; //recuperamos el puntero del heap y le sumamos el tamaño del struct
                            Codigo_dasm += Hijo.Hijos.get(1).Hijos.size() + "\n";
                            Codigo_dasm += Cadena.Add + "\n";
                            Codigo_dasm += Cadena.set_local_1 + "\n"; //dejamos el puntero del heap ya actualizado 
                            //</editor-fold>
                            for (Nodo sub_hijo : Hijo.Hijos.get(1).Hijos) {
                                switch (sub_hijo.Term.getNombre()) {
                                    case Cadena.DA_VAR: {
                                        //< editor-fold>
                                        String tipo = sub_hijo.Hijos.get(0).Token.getNombre();
                                        linea = sub_hijo.Hijos.get(0).Token.getLinea();
                                        columna = sub_hijo.Hijos.get(0).Token.getColumna();
                                        if (!tipo.equals(Cadena.id)) {
                                            tipo = sub_hijo.Hijos.get(0).Token.getValor().toString();
                                            if (sub_hijo.Hijos.get(1).Hijos.size() > 1) { // es mas de una variable                                        
                                                for (Nodo nieto : sub_hijo.Hijos.get(1).Hijos) {
                                                    String name = nieto.Token.getValor().toString();
                                                    linea = nieto.Token.getLinea();
                                                    columna = nieto.Token.getColumna();
                                                    if (!Est.exixteAtr(name)) { //validamos que no exista 
                                                        Simbolo sim = new Simbolo(name, retornarTam(tipo), Cadena.var_primitiva, tipo, pos + "", linea, columna);
                                                        cima.insertar(nombre + "_" + name, sim);
                                                        Est.atributos.add(sim);
                                                        //<editor-fold desc="generacion DASM">
                                                        Codigo_dasm += Generador.inicia_var_estr(pos + "", tipo);
                                                        //</editor-fold>
                                                        pos++;
                                                    } else {
                                                        String error = "ERROR SEMANTICO: Ya se encuntra definio un atributo en la estructura, con el mismo nombre -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                    }
                                                }
                                            } else { //es una sola variable
                                                String name = sub_hijo.Hijos.get(1).Hijos.get(0).Token.getValor().toString();
                                                linea = sub_hijo.Hijos.get(1).Hijos.get(0).Token.getLinea();
                                                columna = sub_hijo.Hijos.get(1).Hijos.get(0).Token.getColumna();
                                                if (!Est.exixteAtr(name)) { //validamos que no exista 
                                                    Simbolo sim = new Simbolo(name, retornarTam(tipo), Cadena.var_primitiva, tipo, pos + "", linea, columna);
                                                    cima.insertar(nombre + "_" + name, sim);
                                                    Est.atributos.add(sim);
                                                    //<editor-fold desc="generacion DASM">
                                                    Codigo_dasm += Generador.inicia_var_estr(pos + "", tipo);
                                                    //</editor-fold>
                                                    pos++;
                                                } else {
                                                    String error = "ERROR SEMANTICO: Ya se encuntra definio un atributo en la estructura, con el mismo nombre -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }
                                            }
                                        } else {
                                            tipo = sub_hijo.Hijos.get(0).Token.getValor().toString();
                                            String error = "ERROR SEMANTICO: No se permite declarar estructuras dentro de estructuras -> " + tipo + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                        break;
                                    }
                                    //</editor-fold>
                                    case Cadena.DA_ARR: {
                                        //<editor-fold>
                                        //vamos a decir que en la edd solo se declaran arreglos estaticos
                                        String tipo = sub_hijo.Hijos.get(0).Token.getNombre();
                                        linea = sub_hijo.Hijos.get(0).Token.getLinea();
                                        columna = sub_hijo.Hijos.get(0).Token.getColumna();
                                        if (!tipo.equals(Cadena.id)) {
                                            tipo = sub_hijo.Hijos.get(0).Token.getValor().toString();
                                            String name = sub_hijo.Hijos.get(1).Token.getValor().toString();
                                            linea = sub_hijo.Hijos.get(1).Token.getLinea();
                                            columna = sub_hijo.Hijos.get(1).Token.getColumna();
                                            if (!Est.exixteAtr(name)) { //validamos que no exista 
                                                Simbolo sim = new Simbolo(name, retornarTam(tipo), Cadena.arreglo, tipo, pos + "", linea, columna);
                                                sim.TipoObjeto = Cadena.arreglo;
                                                sim.numDims = sub_hijo.Hijos.get(2).Hijos.size();
                                                String no_dim = sim.numDims + "";
                                                ArrayList<retorno> val_dims = new ArrayList<>();
                                                capturarEXP(val_dims, sub_hijo.Hijos.get(2));
                                                if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                                    cima.insertar(nombre + "_" + name, sim);
                                                    Est.atributos.add(sim);
                                                    //<editor-fold desc="generacion DASM">
                                                    Codigo_dasm += Generador.inicia_var_estr(pos + "", Cadena.arreglo);
                                                    Codigo_dasm += Generador.almacenr_punt_arreglo_estr();
                                                    Codigo_dasm += Generador.setear_dim_arreglo_est(no_dim);
                                                    //Almacenamos los valores de las dimensiones 
                                                    for (retorno val_dim : val_dims) {
                                                        Codigo_dasm += Generador.setear_dim_arreglo_est(val_dim.cod_generado);
                                                    }
                                                    Codigo_dasm += Generador.setear_dir_itera_arreglo_est();
                                                    //ahora alamcenamos los valores de las dimensiones en el heap para multiplicarlas
                                                    for (int i = 1; i < val_dims.size() + 1; i++) {
                                                        Codigo_dasm += Generador.recuperar_dims_arreglo_est(i + "");
                                                    }
                                                    Codigo_dasm += Generador.multiplicar_dims_areglo_est(val_dims.size() - 1);
                                                    Codigo_dasm += Generador.iniciar_vals_arreglo_est(tipo);
                                                    //</editor-fold>
                                                    pos++;
                                                } else {
                                                    String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }
                                            } else {
                                                String error = "ERROR SEMANTICO: Ya se encuntra definio un atributo en la estructura, con el mismo nombre -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            tipo = sub_hijo.Hijos.get(0).Token.getValor().toString();
                                            String error = "ERROR SEMANTICO: No se permite declarar arrglos de estructuras dentro de estructuras -> " + tipo + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                        break;
                                    }
                                    //</editor-fold>
                                }
                                //aca validar el debug
                                //<editor-fold desc="Codigo debug">
                                if (debug) {
                                    if (lineadeb(linea) || debugeando) {
                                        pintarLinea(Integer.parseInt(linea), lineatmp);
                                        lineatmp = Integer.parseInt(linea);
                                        notificar("DEBUG ALERT: Se detuvo en la linea : " + linea);
                                        llenar_TS();
                                        imprimir_DASM(Codigo_dasm);
                                        this.suspend(); // good practice                                    
                                    }
                                }
                                //para el stop
                                if (detener) {
                                    codigo_generado += Codigo_dasm;
                                    return;
                                }
                                //</editor-fold>
                            }
                            Codigo_dasm += Cadena.End + "\n";
                            Estructuras.insertar(nombre, Est);
                            codigo_generado += Codigo_dasm;
                        } else {
                            String error = "ERROR SEMANTICO: Ya se encuentra definida un estrcutura con el mismo nombre -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void capturarGlobales(Nodo nodo){
        int pos = 0;
        for (Nodo hijo : nodo.Hijos){
            String linea="0";
            String Codigo_dasm="";
            if(hijo!=null){
                switch (hijo.Term.getNombre()) {
                    case Cadena.DA_VAR: {
                        //<editor-fold>
                        String tipo = hijo.Hijos.get(0).Token.getValor().toString();
                        linea = hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();
                        if (hijo.Hijos.get(1).Hijos.size() > 1) { // es una lista de variables
                            //<editor-fold>
                            if (hijo.Hijos.get(2).Hijos.size() > 0) { //declaracion con asignacion,  
                                if (hijo.Hijos.get(0).Token.getNombre().equals(Cadena.id)) { //lista de estructuras, solo se asigna la ultima
                                    //<editor-fold>
                                    if (Estructuras.existeEstructura(tipo)) { //validamos que este definido el tipo de struct
                                        int num_hijos = hijo.Hijos.get(1).Hijos.size();
                                        for (int i = 0; i < num_hijos - 1; i++) {
                                            String nombre = hijo.Hijos.get(1).Hijos.get(i).Token.getValor().toString();
                                            columna = hijo.Hijos.get(1).Hijos.get(i).Token.getColumna();
                                            if (!Global.existeSimbolo(nombre)) {
                                                Simbolo sim = new Simbolo(nombre, retornarTam(tipo), Cadena.estructura, tipo, pos + "", linea, columna);
                                                sim.TipoObjeto=Cadena.estructura;
                                                cima.insertar(nombre, sim);
                                                Codigo_dasm += Cadena.codigo_d_est;
                                                Codigo_dasm += Generador.declara_struct_global(pos + "", tipo);
                                                pos++;
                                            } else {
                                                String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        }
                                        //asignamos la ultima
                                        String nombre = hijo.Hijos.get(1).Hijos.get(num_hijos - 1).Token.getValor().toString();
                                        columna = hijo.Hijos.get(1).Hijos.get(num_hijos - 1).Token.getColumna();
                                        if (!Global.existeSimbolo(nombre)) {
                                            retorno ret = comprobarExp(hijo.Hijos.get(2).Hijos.get(0));
                                            if (ret.tipo.equals(tipo)) {
                                                Simbolo sim = new Simbolo(nombre, retornarTam(tipo), Cadena.estructura, tipo, pos + "", linea, columna);
                                                sim.TipoObjeto=Cadena.estructura;
                                                cima.insertar(nombre, sim);
                                                //aca va el codigo generado
                                                //<editor-fold desc="codigo dasm generado">
                                                Codigo_dasm += Cadena.codigo_da_est;
                                                Codigo_dasm += Generador.declara_asigna_var_global(pos + "", ret.cod_generado);
                                                pos++;
                                                //</editor-fold>
                                            } else {
                                                String error = "ERROR SEMANTICO: La estrcutura a asignar no es de del mismo  tipo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: El tipo de estructura a instanciar no esta definida -> " + tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                    //</editor-fold>
                                } else {//es una lista de primitivas, solo se asigna la ultima
                                    //<editor-fold>
                                    int num_hijos = hijo.Hijos.get(1).Hijos.size();
                                    for (int i = 0; i < num_hijos - 1; i++) {
                                        String nombre = hijo.Hijos.get(1).Hijos.get(i).Token.getValor().toString();
                                        columna = hijo.Hijos.get(1).Hijos.get(i).Token.getColumna();
                                        if (!Global.existeSimbolo(nombre)) {
                                            Simbolo sim = new Simbolo(nombre, retornarTam(tipo), Cadena.var_primitiva, tipo, pos + "", linea, columna);
                                            cima.insertar(sim.nombre, sim);
                                            Codigo_dasm += Cadena.codigo_d_pri;
                                            Codigo_dasm += Generador.declara_var_global(pos + "", tipo);
                                            pos++;
                                        } else {
                                            String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    }
                                    //asignamos la ultima
                                    String nombre = hijo.Hijos.get(1).Hijos.get(num_hijos - 1).Token.getValor().toString();
                                    columna = hijo.Hijos.get(1).Hijos.get(num_hijos - 1).Token.getColumna();
                                    if (!Global.existeSimbolo(nombre)) {
                                        retorno ret = comprobarExp(hijo.Hijos.get(2).Hijos.get(0));
                                        if (ret.tipo.equals(tipo)) {
                                            Simbolo sim = new Simbolo(nombre, retornarTam(tipo), Cadena.var_primitiva, tipo, pos + "", linea, columna);
                                            cima.insertar(nombre, sim);
                                            //aca va el codigo generado
                                            //<editor-fold desc="codigo dasm generado"> 
                                            Codigo_dasm += Cadena.codigo_da_pri;
                                            Codigo_dasm += Generador.declara_asigna_var_global(pos + "", ret.cod_generado);
                                            pos++;
                                            //</editor-fold>
                                        } else {
                                            String error = "ERROR SEMANTICO: La estrcutura a asignar no es de del mismo  tipo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                    //</editor-fold>
                                }
                            } else { //solo de claraccion
                                if (hijo.Hijos.get(0).Token.getNombre().equals(Cadena.id)) { //lista de estructuras, no se asigna niguna
                                    //<editor-fold>
                                    if (Estructuras.existeEstructura(tipo)) { //validamos que este definido el tipo de struct
                                        int num_hijos = hijo.Hijos.get(1).Hijos.size();
                                        for (int i = 0; i < num_hijos; i++) {
                                            String nombre = hijo.Hijos.get(1).Hijos.get(i).Token.getValor().toString();
                                            columna = hijo.Hijos.get(1).Hijos.get(i).Token.getColumna();
                                            if (!Global.existeSimbolo(nombre)) {
                                                Simbolo sim = new Simbolo(nombre, retornarTam(tipo), Cadena.estructura, tipo, pos + "", linea, columna);
                                                cima.insertar(nombre, sim);
                                                sim.TipoObjeto=Cadena.estructura;
                                                Codigo_dasm += Cadena.codigo_d_est;
                                                Codigo_dasm += Generador.declara_struct_global(pos + "", tipo);
                                                pos++;
                                            } else {
                                                String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: El tipo de estructura a instanciar no esta definida -> " + tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                    //</editor-fold>
                                } else { //es una lista de primitivas, no se asigna niguna
                                    //<editor-fold>
                                    int num_hijos = hijo.Hijos.get(1).Hijos.size();
                                    for (int i = 0; i < num_hijos; i++) {
                                        String nombre = hijo.Hijos.get(1).Hijos.get(i).Token.getValor().toString();
                                        columna = hijo.Hijos.get(1).Hijos.get(i).Token.getColumna();
                                        if (!Global.existeSimbolo(nombre)) {
                                            Simbolo sim = new Simbolo(nombre, retornarTam(tipo), Cadena.var_primitiva, tipo, pos + "", linea, columna);
                                            cima.insertar(sim.nombre, sim);
                                            Codigo_dasm += Cadena.codigo_d_pri;
                                            Codigo_dasm += Generador.declara_var_global(pos + "", tipo);
                                            pos++;
                                        } else {
                                            String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    }
                                    //</editor-fold>
                                }
                            }
                            //</editor-fold>
                        } else { // es una sola variable
                            //<editor-fold>
                            String nombre = hijo.Hijos.get(1).Hijos.get(0).Token.getValor().toString();
                            Simbolo sim = existeVariable3(nombre);
                            if (sim == null) {
                                if (hijo.Hijos.get(2).Hijos.size() > 0) { // es una declaracion con asignacion                                
                                    if (hijo.Hijos.get(0).Token.getNombre().equals(Cadena.id)) {//es una estructura                                    
                                        //<editor-fold>
                                        if (Estructuras.existeEstructura(tipo)) {
                                            retorno ret = comprobarExp(hijo.Hijos.get(2).Hijos.get(0));
                                            if (ret.tipo.equals(tipo)) {
                                                sim = new Simbolo(nombre, retornarTam(tipo), Cadena.estructura, tipo, pos + "", linea, columna);
                                                sim.TipoObjeto=Cadena.estructura;
                                                cima.insertar(nombre, sim);
                                                //aca va el codigo generado
                                                //<editor-fold desc="codigo dasm generado">
                                                Codigo_dasm += Cadena.codigo_da_est;
                                                Codigo_dasm += Generador.declara_asigna_var_global(pos + "", ret.cod_generado);
                                                //</editor-fold>
                                                pos++;
                                            } else {
                                                String error = "ERROR SEMANTICO: La estrcutura a asignar no es de del mismo  tipo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: El tipo de estructura a instanciar no esta definida -> " + tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                        //</editor-fold>
                                    } else { //es una variable primitiva
                                        //<editor-fold>
                                        retorno ret = comprobarExp(hijo.Hijos.get(2).Hijos.get(0));
                                        if (ret.tipo.equals(tipo)) {
                                            sim = new Simbolo(nombre, retornarTam(tipo), Cadena.var_primitiva, tipo, pos + "", linea, columna);
                                            cima.insertar(nombre, sim);
                                            //<editor-fold desc="codigo dasm generado">
                                            Codigo_dasm += Cadena.codigo_da_pri;
                                            Codigo_dasm += Generador.declara_asigna_var_global(pos + "", ret.cod_generado);
                                            //</editor-fold>
                                            pos++;
                                        } else {
                                            String error = "ERROR SEMANTICO: Incompatibilidad de tipos en la asignacion de la variable  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                        //</editor-fold>
                                    }
                                } else { //es solo una declaracion                                            
                                    if (hijo.Hijos.get(0).Token.getNombre().equals(Cadena.id)) {//es una estrcutura
                                        //<editor-fold>
                                        if (Estructuras.existeEstructura(tipo)) {
                                            sim = new Simbolo(nombre, retornarTam(tipo), Cadena.estructura, tipo, pos + "", linea, columna);
                                            sim.TipoObjeto=Cadena.estructura;
                                            cima.insertar(nombre, sim);                                            
                                            Codigo_dasm += Cadena.codigo_d_est;
                                            Codigo_dasm += Generador.declara_struct_global(pos + "", tipo);
                                            pos++;
                                            //inicialicar la var
                                        } else {
                                            String error = "ERROR SEMANTICO: La estructura a instanciar no esta definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                        //</editor-fold>
                                    } else { //es una variable primitiva                                                 
                                        //<editor-fold>
                                        sim = new Simbolo(nombre, retornarTam(tipo), Cadena.var_primitiva, tipo, pos + "", linea, columna);
                                        cima.insertar(nombre, sim);
                                        Codigo_dasm += Cadena.codigo_d_pri;
                                        Codigo_dasm += Generador.declara_var_global(pos + "", tipo);
                                        pos++;
                                        //</editor-fold>
                                    }
                                }
                            } else {
                                String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                            //</editor-fold>
                        }
                        break;
                        //</editor-fold>
                    }
                    case Cadena.DA_ARR: {
                        //<editor-fold>
                        String tipo = hijo.Hijos.get(0).Token.getNombre();
                        linea = hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();
                        String name = hijo.Hijos.get(1).Token.getValor().toString();
                        //validamos que no este definido
                        if (!Global.existeSimbolo(name)) {
                            if (tipo.equals(Cadena.id)) { //es un arreglo de estructuras
                                tipo = hijo.Hijos.get(0).Token.getValor().toString();
                                columna = hijo.Hijos.get(1).Token.getColumna();
                                if (Estructuras.existeEstructura(tipo)) {
                                    if (hijo.Hijos.get(3).Hijos.size() > 0) { //es declaracion con asignacion
                                        //<editor-fold>
                                        Simbolo sim = new Simbolo(name, retornarTam(tipo), Cadena.arreglo, tipo, pos + "", linea, columna);
                                        sim.TipoObjeto = Cadena.arreglo;
                                        sim.numDims = hijo.Hijos.get(2).Hijos.size();
                                        String no_dim = sim.numDims + "";
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        ArrayList<retorno> valores = new ArrayList<>();
                                        ArrayList<Integer> dimens = new ArrayList<>();
                                        capturarEXP(val_dims, hijo.Hijos.get(2));
                                        capturarEXP(valores, hijo.Hijos.get(3).Hijos.get(0));
                                        capturarDims(dimens, hijo.Hijos.get(3).Hijos.get(0));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            if (validarTipos(valores)) { //validamos el mismo tipo de los valores
                                                String tip = ((retorno) valores.get(0)).tipo;
                                                if (tip.equals(tipo)) {
                                                    if (sim.numDims == dimens.size()){                                                        
                                                        //insertamos el simbolo
                                                        cima.insertar(name, sim);
                                                        //<editor-fold>
                                                        Codigo_dasm += Cadena.codigo_da_arr_est;
                                                        Codigo_dasm += Generador.inicio_dec_arr(pos + "");
                                                        Codigo_dasm += Generador.setear_dim_dec_arr(no_dim+"\n");
                                                        //Almacenamos los valores de las dimensiones 
                                                        for (retorno val_dim : val_dims) {
                                                            Codigo_dasm += Generador.setear_dim_dec_arr(val_dim.cod_generado);
                                                        }
                                                        //alamcenamos los valores de las dimensiones en el heap para multiplicarlas
                                                        for (int i = 1; i < val_dims.size() + 1; i++) {
                                                            Codigo_dasm += Generador.recuperar_dims_dec_arr(pos + "", i + "");
                                                        }
                                                        Codigo_dasm += Generador.multiplicar_dims_areglo_est(val_dims.size() - 1);
                                                        Codigo_dasm += Generador.recuperar_pos_ini_arr(pos + "");
                                                        for (retorno valore : valores) {
                                                            Codigo_dasm += Generador.asignar_val_arr(valore.cod_generado);
                                                        }
                                                        Codigo_dasm += Generador.finalizar_asignacion_arr();
                                                        //</editor-fold>
                                                        pos++;
                                                    } else {
                                                        String error = "ERROR SEMANTICO: Dimnensiones incorrectas en la DEC y ASIG  del arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                    }
                                                } else {
                                                    String error = "ERROR SEMANTICO: Incopatibilidad de tipos en la DEC y ASIG  del arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }
                                            } else {
                                                String error = "ERROR SEMANTICO: Todos los valores a asignar deben ser del mismo tipo , arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                        //</editor-fold>
                                    } else { //es solo la declaración de un arreglo de estructuras
                                        //<editor-fold>                                    
                                        Simbolo sim = new Simbolo(name, retornarTam(tipo), Cadena.arreglo, tipo, pos + "", linea, columna);
                                        sim.TipoObjeto = Cadena.arreglo;
                                        sim.numDims = hijo.Hijos.get(2).Hijos.size();
                                        String no_dim = sim.numDims + "";
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        capturarEXP(val_dims, hijo.Hijos.get(2));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            cima.insertar(sim.nombre, sim);
                                            //<editor-fold desc="generacion DASM">
                                            Codigo_dasm += Cadena.codigo_d_arr_est;
                                            Codigo_dasm += Generador.inicio_dec_arr(pos + "");
                                            Codigo_dasm += Generador.setear_dim_dec_arr(no_dim+"\n");
                                            //Almacenamos los valores de las dimensiones 
                                            for (retorno val_dim : val_dims) {
                                                Codigo_dasm += Generador.setear_dim_dec_arr(val_dim.cod_generado);
                                            }
                                            //alamcenamos los valores de las dimensiones en el heap para multiplicarlas
                                            for (int i = 1; i < val_dims.size() + 1; i++) {
                                                Codigo_dasm += Generador.recuperar_dims_dec_arr(pos + "", i + "");
                                            }
                                            Codigo_dasm += Generador.multiplicar_dims_areglo_est(val_dims.size() - 1);
                                            Codigo_dasm += Generador.iniciar_vals_dec_arr_estr(pos + "", tipo);
                                            //</editor-fold>
                                            pos++;
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                        //</editor-fold>
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: El tipo de estrcutara del arreglo a declar no esta definido -> " + tipo + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else { //arreglo de primitivos
                                tipo = hijo.Hijos.get(0).Token.getValor().toString();
                                columna = hijo.Hijos.get(1).Token.getColumna();
                                if (hijo.Hijos.get(3).Hijos.size() > 0) { //es declaracion con asignacion de arreglo de primitiovos
                                    //<editor-fold>
                                    Simbolo sim = new Simbolo(name, retornarTam(tipo), Cadena.arreglo, tipo, pos + "", linea, columna);
                                    sim.TipoObjeto = Cadena.arreglo;
                                    sim.numDims = hijo.Hijos.get(2).Hijos.size();
                                    String no_dim = sim.numDims + "";
                                    ArrayList<retorno> val_dims = new ArrayList<>();
                                    ArrayList<retorno> valores = new ArrayList<>();
                                    ArrayList<Integer> dimens = new ArrayList<>();
                                    capturarEXP(val_dims, hijo.Hijos.get(2));
                                    capturarEXP(valores, hijo.Hijos.get(3).Hijos.get(0));
                                    capturarDims(dimens, hijo.Hijos.get(3).Hijos.get(0));
                                    if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                        if (validarTipos(valores)) { //validamos el mismo tipo de los valores
                                            String tip = ((retorno) valores.get(0)).tipo;
                                            if (tip.equals(tipo)){
                                                if (sim.numDims == dimens.size()) {
                                                    //insrtamos el simbolo
                                                    cima.insertar(name, sim);
                                                    //<editor-fold>
                                                    Codigo_dasm += Cadena.codigo_da_arr_pri;
                                                    Codigo_dasm += Generador.inicio_dec_arr(pos + "");
                                                    Codigo_dasm += Generador.setear_dim_dec_arr(no_dim+"\n");
                                                    //Almacenamos los valores de las dimensiones 
                                                    for (retorno val_dim : val_dims) {
                                                        Codigo_dasm += Generador.setear_dim_dec_arr(val_dim.cod_generado);
                                                    }
                                                    //alamcenamos los valores de las dimensiones en el heap para multiplicarlas
                                                    for (int i = 1; i < val_dims.size() + 1; i++) {
                                                        Codigo_dasm += Generador.recuperar_dims_dec_arr(pos + "", i + "");
                                                    }
                                                    Codigo_dasm += Generador.multiplicar_dims_areglo_est(val_dims.size() - 1);
                                                    Codigo_dasm += Generador.recuperar_pos_ini_arr(pos + "");
                                                    for (retorno valore : valores) {
                                                        Codigo_dasm += Generador.asignar_val_arr(valore.cod_generado);
                                                    }
                                                    Codigo_dasm += Generador.finalizar_asignacion_arr();
                                                    //</editor-fold>
                                                    pos++;
                                                } else {
                                                    String error = "ERROR SEMANTICO: Dimnensiones incorrectas en la DEC y ASIG  del arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }
                                            } else {
                                                String error = "ERROR SEMANTICO: Incopatibilidad de tipos en la DEC y ASIG  del arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores a asignar deben ser del mismo tipo , arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                    //</editor-fold>
                                } else { // es solo declaracion sin asignacion de primitivos
                                    //<editor-fold>
                                    Simbolo sim = new Simbolo(name, retornarTam(tipo), Cadena.arreglo, tipo, pos + "", linea, columna);
                                    sim.TipoObjeto = Cadena.arreglo;
                                    sim.numDims = hijo.Hijos.get(2).Hijos.size();
                                    String no_dim = sim.numDims + "";
                                    ArrayList<retorno> val_dims = new ArrayList<>();
                                    capturarEXP(val_dims, hijo.Hijos.get(2));
                                    if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                        cima.insertar(sim.nombre, sim);
                                        //<editor-fold desc="generacion DASM">
                                        Codigo_dasm += Cadena.codigo_d_arr_pri;
                                        Codigo_dasm += Generador.inicio_dec_arr(pos + "");
                                        Codigo_dasm += Generador.setear_dim_dec_arr(no_dim+"\n");
                                        //Almacenamos los valores de las dimensiones 
                                        for (retorno val_dim : val_dims) {
                                            Codigo_dasm += Generador.setear_dim_dec_arr(val_dim.cod_generado);
                                        }
                                        //alamcenamos los valores de las dimensiones en el heap para multiplicarlas
                                        for (int i = 1; i < val_dims.size() + 1; i++) {
                                            Codigo_dasm += Generador.recuperar_dims_dec_arr(pos + "", i + "");
                                        }
                                        Codigo_dasm += Generador.multiplicar_dims_areglo_est(val_dims.size() - 1);
                                        Codigo_dasm += Generador.iniciar_vals_arreglo_est(tipo);
                                        //</editor-fold>
                                        pos++;
                                    } else {
                                        String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                    //</editor-fold>
                                }
                            }
                        } else {
                            String error = "ERROR SEMANTICO: Ya se encuntra definio un arreglo, con el mismo nombre -> " + name + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        break;
                        //</editor-fold>
                    }
                    case Cadena.AS_VAR: {
                        //<editor-fold>
                        String nombre = hijo.Hijos.get(0).Token.getValor().toString();
                        linea = hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();
                        Simbolo sim = existeVariable3(nombre);
                        if (sim != null) { //es una variable global
                            //<editor-fold>                                                     
                            retorno ret=comprobarExp(hijo.Hijos.get(1));     
                            if(sim.tipo.equals(ret.tipo)){
                                Codigo_dasm += Cadena.codigo_a_var_glo;
                                Codigo_dasm += Generador.recuperar_dir_var_global(sim.posicion);
                                Codigo_dasm += Generador.asignar_var_glob_loc_stack(ret.cod_generado);
                            }else{
                                String error = "ERROR SEMANTICO:Imcompatibilidad de tipos al asignar la varaible ->  " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }                                                        
                            //</editor-fold>
                        } else { //la varibale no existe
                            String error = "ERROR SEMANTICO: La variable a acceder no esta definida ->  " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        break;
                        //</editor-fold>
                    }
                    case Cadena.AS_ARR: {
                        //<editor-fold>
                        String nombre= hijo.Hijos.get(0).Token.getValor().toString();
                        linea= hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();                
                        Simbolo sim = existeVariable3(nombre);
                        if(sim!=null){ //es una variable global 
                        //<editor-fold>
                            if (sim.TipoObjeto.equals(Cadena.arreglo)) { //comprobamos que sea  un arreglo
                                ArrayList<retorno> val_dims = new ArrayList<>();
                                capturarEXP(val_dims, hijo.Hijos.get(1));
                                if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                    if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                        retorno ret = comprobarExp(hijo.Hijos.get(2));
                                        if(ret.tipo.equals(sim.tipo)){
                                            String et_error = Generador.generar_etq();
                                            String et_correcto = Generador.generar_etq();
                                            Codigo_dasm+=Cadena.codigo_a_arr_glo;
                                            //codigo para comprobar los indices en ejecucion
                                            for (int i = 0; i < val_dims.size(); i++) {
                                                Codigo_dasm += Generador.comprobarIndice_global((i + 1) + "", sim.posicion, val_dims.get(i).cod_generado, et_error);
                                            }
                                            // codigo para linealizar    
                                            for (int i = 0; i < val_dims.size(); i++) {
                                                if (i == 0) {
                                                    Codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                } else {
                                                    Codigo_dasm += Generador.linealizar_arreglo_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion);
                                                }
                                            }
                                            //fin del arreglo
                                            Codigo_dasm += Generador.recuperar_dir_Arreglo_global(sim.numDims + "", sim.posicion);
                                            Codigo_dasm += Generador.asignar_var_glob_loc_heap(ret.cod_generado);
                                            //ahora lo del error 
                                            Codigo_dasm += Cadena.br + et_correcto + "\n";
                                            Codigo_dasm += et_error + ":\n";
                                            String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                String cad2 = generarCodCad(sim.nombre + "\n");
                                                //llamo a concat
                                                String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                Codigo_dasm += "//------------------llamada a print ----------------\n";
                                                Codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                //llamo al funcion print
                                                Codigo_dasm += "//----------------------------------------------------\n";
                                            Codigo_dasm += et_correcto + ":\n";
                                        }else{
                                            String error = "ERROR SEMANTICO: Imcompatibilidad de tipos al asignar el arreglo ->  " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                }else{
                                    String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }                                
                            } else {
                                String error = "ERROR SEMANTICO: La variable a asignar no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                            //</editor-fold>
                        } else {
                            String error = "ERROR SEMANTICO: La variable no se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        break;
                        //</editor-fold>
                    }
                    case Cadena.AS_ARR2:
                    {
                        //<editor-fold>
                        String nombre= hijo.Hijos.get(0).Token.getValor().toString();
                        linea = hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();
                        Simbolo sim = existeVariable3(nombre);
                        if (sim != null) { //es una variable global 
                            //<editor-fold>
                            if (sim.TipoObjeto.equals(Cadena.arreglo)) { //comprobamos que sea  un arreglo
                                Estructura est = Estructuras.retornaEstructura(sim.tipo);
                                if (est != null) {
                                    String nombre2 = hijo.Hijos.get(2).Token.getValor().toString();
                                    columna = hijo.Hijos.get(2).Token.getColumna();
                                    Simbolo sim2 = est.getAtributo(nombre2);
                                    if (sim2 != null) {
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        capturarEXP(val_dims, hijo.Hijos.get(1));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones
                                                retorno ret = comprobarExp(hijo.Hijos.get(3));                                                
                                                if(ret.tipo.equals(sim2.tipo)){
                                                    String et_error = Generador.generar_etq();
                                                    String et_correcto = Generador.generar_etq();
                                                    Codigo_dasm+=Cadena.codigo_a_var_arr_glo;
                                                    //codigo para comprobar los indices en ejecucion
                                                    for (int i = 0; i < val_dims.size(); i++) {
                                                        Codigo_dasm += Generador.comprobarIndice_global((i + 1) + "", sim.posicion, val_dims.get(i).cod_generado, et_error);
                                                    }
                                                    // codigo para linealizar    
                                                    for (int i = 0; i < val_dims.size(); i++) {
                                                        if (i == 0) {
                                                            Codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                        } else {
                                                            Codigo_dasm += Generador.linealizar_arreglo_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion);
                                                        }
                                                    }
                                                    //fin del arreglo
                                                    Codigo_dasm += Generador.recuperar_dir_est_Arreglo_global(sim.numDims + "", sim.posicion, sim2.posicion);
                                                    Codigo_dasm += Generador.asignar_var_glob_loc_heap(ret.cod_generado);
                                                    //ahora lo del error 
                                                    Codigo_dasm += Cadena.br + et_correcto + "\n";
                                                    Codigo_dasm += et_error + ":\n";
                                                    String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                        String cad2 = generarCodCad(sim.nombre + "\n");
                                                        //llamo a concat
                                                        String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                        Codigo_dasm += "//------------------llamada a print ----------------\n";
                                                        Codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                        //llamo al funcion print
                                                        Codigo_dasm += "//----------------------------------------------------\n";
                                                    Codigo_dasm += et_correcto + ":\n";
                                                }else{
                                                    String error = "ERROR SEMANTICO: Imcompatibilidad de tipos al asignar la variable de estrcutura ->  " + nombre2 + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }                                                
                                            } else {
                                                String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: El atributo de estructura al que desea acceder no esta definido -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: El arreglo al que inteta  acceder no es de estrcuturas -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else {
                                String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                            //</editor-fold>
                        } else {
                            String error = "ERROR SEMANTICO: La variable no se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        
                        //</editor-fold>
                        break;
                    }
                    case Cadena.AS_ARR3:{
                        //<editor-fold>
                        String nombre= hijo.Hijos.get(0).Token.getValor().toString();
                        linea = hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();
                        Simbolo sim = existeVariable3(nombre);
                        if(sim!=null){ //es una variable global 
                        //<editor-fold>
                            if (sim.TipoObjeto.equals(Cadena.arreglo)) { //comprobamos que sea  un arreglo
                                Estructura est = Estructuras.retornaEstructura(sim.tipo);
                                if (est != null) {
                                    String nombre2 = hijo.Hijos.get(2).Token.getValor().toString();
                                    columna = hijo.Hijos.get(2).Token.getColumna();
                                    Simbolo sim2 = est.getAtributo(nombre2);
                                    if (sim2 != null) {
                                        if (sim2.TipoObjeto.equals(Cadena.arreglo)) {
                                            ArrayList<retorno> val_dims = new ArrayList<>();
                                            capturarEXP(val_dims, hijo.Hijos.get(1));
                                            if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                                if (sim.numDims == val_dims.size()) { // comprobamos el numero de dimensiones 
                                                    ArrayList<retorno> val_dims2 = new ArrayList<>();
                                                    capturarEXP(val_dims2, hijo.Hijos.get(3));
                                                    if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                                        if (sim2.numDims == val_dims2.size()) {
                                                            retorno ret= comprobarExp(hijo.Hijos.get(4));                                                          
                                                            if(ret.tipo.equals(sim2.tipo)){
                                                                String et_error = Generador.generar_etq();
                                                                String et_correcto = Generador.generar_etq();
                                                                Codigo_dasm+=Cadena.codigo_a_arr_arr_glo;
                                                                //codigo para comprobar los indices en ejecucion
                                                                for (int i = 0; i < val_dims.size(); i++) {
                                                                    Codigo_dasm += Generador.comprobarIndice_global((i + 1) + "", sim.posicion, val_dims.get(i).cod_generado, et_error);
                                                                }
                                                                // codigo para linealizar    
                                                                for (int i = 0; i < val_dims.size(); i++) {
                                                                    if (i == 0) {
                                                                        Codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                                    } else {
                                                                        Codigo_dasm += Generador.linealizar_arreglo_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion);
                                                                    }
                                                                }
                                                                //fin del arreglo
                                                                Codigo_dasm += Generador.recuperar_val_est_Arr_arr_global(sim.numDims + "", sim.posicion, sim2.posicion);

                                                                //ahora hacemos el acceso del 2do arreglo                                                
                                                                for (int i = 0; i < val_dims2.size(); i++) {
                                                                    Codigo_dasm += Generador.comprobarIndice_Arr_arr_loc_gob((i + 1) + "", val_dims2.get(i).cod_generado, et_error);
                                                                }
                                                                // codigo para linealizar    
                                                                for (int i = 0; i < val_dims2.size(); i++) {
                                                                    if (i == 0) {
                                                                        Codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims2.get(i).cod_generado);
                                                                    } else {
                                                                        Codigo_dasm += Generador.linealizar_Arr_arr_Ndim_loc_gob(val_dims.get(i).cod_generado, (i + 1) + "");
                                                                    }
                                                                }
                                                                Codigo_dasm += Generador.recuperar_dir_Arr_arr_loc_gob(sim2.numDims + "");
                                                                Codigo_dasm+=Generador.asignar_var_glob_loc_heap(ret.cod_generado);
                                                                //ahora lo del error                                                                 
                                                                Codigo_dasm += Cadena.br + et_correcto + "\n";
                                                                Codigo_dasm += et_error + ":\n";
                                                                String cad1 = Dasm.Cadena.ini_idex_bound + "\n";
                                                                    String cad2 = generarCodCad(sim.nombre+"|"+sim2.nombre + "\n");
                                                                    //llamo a concat
                                                                    String expr = Generador.llamada_concat(cima.tamanio + "", cad1, cad2);
                                                                    Codigo_dasm += "//------------------llamada a print ----------------\n";
                                                                    Codigo_dasm += Generador.llamada_print(cima.tamanio + "", expr, "-2");
                                                                    //llamo al funcion print
                                                                    Codigo_dasm += "//----------------------------------------------------\n";
                                                                Codigo_dasm += et_correcto + ":\n";
                                                            }else{
                                                                String error = "ERROR SEMANTICO: Imcompatibilidad de tipos al asignar el arreglo de estrcutura ->  " + nombre2 + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                                InterfazD.listaErrores.add(error);
                                                                System.out.println(error);
                                                            } 
                                                        } else {
                                                            String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo de la estructura a acceder, es incorrecto  -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                            InterfazD.listaErrores.add(error);
                                                            System.out.println(error);
                                                        }
                                                    } else {
                                                        String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                    }
                                                } else {
                                                    String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }
                                            } else {
                                                String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: El atributo de la estructura a acceder no es de tipo arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: El atributo de estructura al que desea acceder no esta definido -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: El arreglo al que inteta  acceder no es de estrcuturas -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else {
                                String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                            //</editor-fold>
                        } else {
                            String error = "ERROR SEMANTICO: La variable No se encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        //</editor-fold>
                        break;
                    }  
                    case Cadena.AS_ARR_EST:{
                        //<editor-fold>
                        String nombre= hijo.Hijos.get(0).Token.getValor().toString();
                        linea = hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();
                        Simbolo sim = existeVariable3(nombre);
                        if (sim.TipoObjeto.equals(Cadena.estructura)) { //comprobamos que sea  una estrcutura
                            Estructura est = Estructuras.retornaEstructura(sim.tipo);
                            if (est != null) {
                                String nombre2 = hijo.Hijos.get(1).Token.getValor().toString();
                                columna = hijo.Hijos.get(1).Token.getColumna();
                                Simbolo sim2 = est.getAtributo(nombre2);
                                if (sim2 != null) {
                                    if (sim2.TipoObjeto.equals(Cadena.arreglo)) {
                                        ArrayList<retorno> val_dims = new ArrayList<>();
                                        capturarEXP(val_dims, hijo.Hijos.get(2));
                                        if (validarTipos(val_dims) && val_dims.get(0).tipo.equals(Cadena.entero)) {
                                            if (sim2.numDims == val_dims.size()) { // comprobamos el numero de dimensiones                                                
                                                retorno ret  = comprobarExp(hijo.Hijos.get(3));                                                
                                                if(ret.tipo.equals(sim2.tipo)){
                                                    String et_error = Generador.generar_etq();
                                                    String et_correcto = Generador.generar_etq();
                                                    Codigo_dasm+=Cadena.codigo_a_arr_est_glo;
                                                    //codigo para comprobar los indices en ejecucion
                                                    for (int i = 0; i < val_dims.size(); i++) {
                                                        Codigo_dasm += Generador.comprobarIndice_est_global((i + 1) + "", sim.posicion, sim2.posicion, val_dims.get(i).cod_generado, et_error);
                                                    }
                                                    // codigo para linealizar    
                                                    for (int i = 0; i < val_dims.size(); i++) {
                                                        if (i == 0) {
                                                            Codigo_dasm += Generador.linealizar_arreglo_1dim(val_dims.get(i).cod_generado);
                                                        } else {
                                                            Codigo_dasm += Generador.linealizar_arreglo_est_Ndim_global(val_dims.get(i).cod_generado, (i + 1) + "", sim.posicion, sim2.posicion);
                                                        }
                                                    }
                                                    //fin del arreglo
                                                    Codigo_dasm += Generador.recuperar_dir_Arreglo_est_global(sim2.numDims + "", sim.posicion, sim2.posicion);
                                                    Codigo_dasm+= Generador.asignar_var_glob_loc_heap(ret.cod_generado);
                                                    //ahora lo del error 
                                                    Codigo_dasm += Cadena.br + et_correcto + "\n";
                                                    Codigo_dasm += et_error + ":\n";
                                                    String cad1 =Dasm.Cadena.ini_idex_bound+"\n";
                                                    String cad2=generarCodCad(sim2.nombre+"\n");
                                                    //llamo a concat
                                                    String expr= Generador.llamada_concat(cima.tamanio+"", cad1, cad2);
                                                    Codigo_dasm += "//------------------llamada a print ----------------\n";
                                                    Codigo_dasm+= Generador.llamada_print(cima.tamanio+"", expr,"-2");
                                                    //llamo al funcion print
                                                    Codigo_dasm+="//----------------------------------------------------\n";
                                                    Codigo_dasm += et_correcto + ":\n";
                                                }else{
                                                    String error = "ERROR SEMANTICO: Imcompatibilidad de tipos al asignar el arreglo de estrcutura ->  " + nombre2 + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                }                                                
                                            } else {
                                                String error = "ERROR SEMANTICO: El numero de dimensiones del arreglo a acceder, es incorrecto  -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                            }
                                        } else {
                                            String error = "ERROR SEMANTICO: Todos los valores de dimension deben ser de tipo entero, arreglo -> " + nombre + " L: " + linea + " C: " + columna + " Archivo: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }
                                    } else {
                                        String error = "ERROR SEMANTICO: La variable a acceder no es de tipo arreglo -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: El atributo al que desea acceder no es parte de la estrcutura definida -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else {
                                String error = "ERROR SEMANTICO: El tipo de estructura de la variable no esta definido  -> " + sim.tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La variable a acceder no es una estrcutura -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        //</editor-fold>
                        break;
                    }
                    case Cadena.AS_VAR_EST:{
                        //<editor-fold>
                        String nombre= hijo.Hijos.get(0).Token.getValor().toString();
                        linea= hijo.Hijos.get(0).Token.getLinea();
                        String columna = hijo.Hijos.get(0).Token.getColumna();                
                        Simbolo sim = existeVariable3(nombre);
                        if (sim != null) { //es una variable global 
                            //<editor-fold>
                            if (sim.TipoObjeto.equals(Cadena.estructura)) { //comprobamos que sea  una estrcutura
                                Estructura est = Estructuras.retornaEstructura(sim.tipo);
                                if (est != null) {
                                    String nombre2 = hijo.Hijos.get(1).Token.getValor().toString();
                                    columna = hijo.Hijos.get(1).Token.getColumna();
                                    Simbolo sim2 = est.getAtributo(nombre2);
                                    if (sim2 != null) {
                                        retorno ret = comprobarExp(hijo.Hijos.get(2));
                                        if(ret.tipo.equals(sim2.tipo)){
                                            Codigo_dasm+=Cadena.codigo_a_var_est_glo;
                                            Codigo_dasm += Generador.recuperar_dir_est_global(sim.posicion, sim2.posicion);
                                            Codigo_dasm += Generador.asignar_var_glob_loc_heap(ret.cod_generado);
                                        }else{
                                            String error = "ERROR SEMANTICO: Incompatibilidad de tipos en la asignacion del atributo de estructura -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                        }                                        
                                    } else {
                                        String error = "ERROR SEMANTICO: El atributo al que desea acceder no es parte de la estrcutura definida -> " + nombre2 + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: El tipo de estructura de la variable no esta definido  -> " + sim.tipo + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                }
                            } else {
                                String error = "ERROR SEMANTICO: La variable a acceder no es una estrcutura -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                            //</editor-fold>
                        } else {
                            String error = "ERROR SEMANTICO: La variable a acceder no encuentra definida -> " + nombre + " L: " + linea + " C: " + columna + " Clase: " + archivoActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                        //</editor-fold>
                        break;
                    }    
                }
                //<editor-fold desc="Codigo debug">            
                if (debug) {
                    if (lineadeb(linea) || debugeando) {
                        pintarLinea(Integer.parseInt(linea), lineatmp);
                        lineatmp = Integer.parseInt(linea);
                        notificar("DEBUG ALERT: Se detuvo en la linea : " + linea);
                        llenar_TS();
                        imprimir_DASM(Codigo_dasm);
                        this.suspend(); // good practice                                    
                    }
                }
                //para el stop
                if (detener) {
                    codigo_generado += Codigo_dasm;
                    return;
                }
                //</editor-fold>
                codigo_generado += Codigo_dasm;
            }            
        }
    }
    
    private Simbolo existeVariable(String id)// para acceso a variables ->  locales y globales
    {
        String nombre = cima.Nivel;
        for (int j = pilaSimbols.size() - 1; j > -1; j--) {
            if (pilaSimbols.get(j).Nivel.equals(nombre)) {
                if (pilaSimbols.get(j).existeSimbolo(id)) {
                    return pilaSimbols.get(j).retornaSimbolo(id);
                }
            }
        }
        return Global.retornaSimbolo(id);
    }

    private Simbolo existeVariable2(String id) //declara/asigna local
    { //metodo para obtener el valor de las variables
        if(!cima.Nivel.equals("Global")){
            String nombre = cima.Nivel;
            int profundidad=0;
            for (int j = pilaSimbols.size() - 1; j > -1; j--) {
                if (pilaSimbols.get(j).Nivel.equals(nombre)) {
                    if (pilaSimbols.get(j).existeSimbolo(id)) {
                        Simbolo sim =pilaSimbols.get(j).retornaSimbolo(id);
                        if(j != pilaSimbols.size() - 1) // si la variable se encuntra fuera de la cima, sumo el tamanio de ambito
                            sim.profundidad=profundidad+pilaSimbols.get(j).tamanio;                    
                        return sim;
                    }else{
                        if(j != pilaSimbols.size() - 1) //no resto el valor de la cima
                            profundidad+=pilaSimbols.get(j).tamanio;                    
                    }
                }
            }
        }
        return null;
    }

    private Simbolo existeVariable3(String id) //declara o asigna gloabal
    { //metodo para obtener el valor de las variables
        return Global.retornaSimbolo(id);
    }
    
    public static  String leerArchivo(String ruta){
        String texto=""; 
        try  {
              BufferedReader bf = new BufferedReader(new FileReader(ruta));
              String bflinea;
              while((bflinea=bf.readLine())!=null ){
              texto+=bflinea+"\n";
              }
              bf.close();
          } catch (Exception e) {
              System.err.println("Lectura Incorrecta de archivo");
          }
        
        return  texto;
    }
    
    private void capturarEXP(ArrayList<retorno> vals, Nodo nodo) {
            switch (nodo.Term.getNombre()) { 
                case Cadena.L_AS:
                    for(Nodo hijo : nodo.Hijos)
                    {
                        capturarEXP(vals, hijo);
                    }
                    break;
                case Cadena.L_EXP:
                    for (Nodo hijo : nodo.Hijos)
                    {
                        capturarEXP(vals, hijo);
                    }
                    break;
                case Cadena.DIM:
                    for(Nodo hijo : nodo.Hijos)
                    {
                        capturarEXP(vals, hijo);
                    }
                    break;
                case Cadena.LOG:
                    retorno ret = comprobarExp(nodo);
                    vals.add(ret);
                    break;
            }

        }
    
    private void capturarDims(ArrayList<Integer> dims, Nodo nodo) {
        if (nodo.Term.getNombre().equals(Cadena.L_EXP)) {
            dims.add(nodo.Hijos.size());
        } else {
            dims.add(nodo.Hijos.size());
            capturarDims(dims, nodo.Hijos.get(0));
        }
    }
    
    private  void notificar(String mensaje){
        consola.append(mensaje+"\n");
    }
    
    private  void agregarDasm(String mensaje){
        consola.append(mensaje+"\n");
    }
    
    private boolean notificarErrrores(){
        if(!listaErrores.isEmpty()){
            for (String Error : listaErrores) {
                errores.append(Error+"\n");
            }
            return true;
        }
        return false;
    }
    
    private String retornarTam(String tipo){
        switch(tipo){
            case Cadena.entero:
                return "4";
            case Cadena.decimal:
                return "8";
            case Cadena.cadena:
                return "1";
            case Cadena.caracter:
                return "1";
            case Cadena.booleano:
                return "1";
            default:
                return "1";
        }    
    }
    
    private boolean  lineadeb(String linea){
        int line = Integer.parseInt(linea);
        for (Integer integer : lineasDeb) {
            if(line==integer)
                return true;
        }
        return false;
    }
    
    private int calcularTamano(Nodo nodo){
        int tama=0;
        
        for (Nodo Hijo : nodo.Hijos) {
            switch(Hijo.Term.getNombre()){
                case Cadena.DA_VAR:
                    tama++;
                    break;
                case Cadena.DA_ARR:
                    tama++;
                    break;
            }
        }        
        return tama;
    }
    
    private  void llenar_TS(){
        int tam=modelo.getRowCount();
        for (int i = tam - 1; i >= 0; i--) {
            modelo.removeRow(i);
        }

        for (Object value : cima.t.values()) {
            Simbolo sim=(Simbolo)value;
            String row[] ={sim.nombre,sim.tamano,sim.rol,sim.tipo,sim.posicion,sim.linea,sim.columna};
            modelo.addRow(row);
        }        
        tablaS.setModel(modelo);
    }
    
    private void imprimir_DASM(String cod_dasm){
        dasm.setText(cod_dasm);
    }
    
    private boolean validarTipos(ArrayList<retorno> vals) {
            String tipo_base="";
            String tipo_sig;
            for (int i = 0; i < vals.size(); i++)
            {
                if (i == 0)
                {
                    retorno ret = vals.get(i);
                    tipo_base = ret.tipo;
                }
                else {
                    retorno ret = vals.get(i);
                    tipo_sig=ret.tipo;
                    if (!tipo_base.equals(tipo_sig)) {
                        return false;
                    }
                }
            }
            return true;
        }
    
    public void continuar(){
        this.resume();
        debugeando=true;
    }
    
    public void siguientePunto(){
        this.resume();
        debugeando=false;
    }
    
    public void detener(){
        detener=true;
        this.resume();
        this.interrupt();
    }
    
    public void setConsolas(JTextArea errores,JTextArea consola,JTextArea dasm,JTable tabla,LineasText tmpL){
        this.errores=errores;
        this.consola=consola;
        this.dasm=dasm;
        this.tablaS=tabla;
        this.tmpL=tmpL;
        String encabezados[]={"Nombre","Tamaño","Rol","Tipo","Posicion","Linea","Columna"};
        modelo= new DefaultTableModel();
        modelo.setColumnIdentifiers(encabezados);
        tablaS.setModel(modelo);
    }
    
    
    private String generarCodCad(String cad){
        String cod_generado="";
        cod_generado = Generador.inicio_cadena();
        for (int i = 0; i < cad.length(); i++) {
            cod_generado += Generador.insertar_caracter((int) cad.charAt(i) + "");
        }
        cod_generado += Generador.fin_cadena();
        return cod_generado;
    }
 
    @Override
    public void run(){        
        traducir(raiz);
    }
    //exite variable cuando se declara solo s busca en la tablas del mismo nivel
    //cuando se asignar una variable se busca tanto en las del mismo nivel como la global
    //cuando se accede a una variable se accede a las del mismo nivel o la global
    
    private void pintarLinea(int linea,int lineatmp){
    
        if (lineatmp != -1) {
            try {
                String text = tmpL.doc.getText(0, tmpL.doc.getLength());
                int start = 0;
                int end = 0;
                int line = 0;
                boolean even = true;
                while ((end = text.indexOf('\n', start)) >= 0) {
                    line++;
                    if (line == lineatmp) {
                        if (tmpL.exiteLine2(lineatmp)) {
                            Object tmp = tmpL.hilite.addHighlight(start, end + 1, normalPainter);
                            tmpL.hilite.removeHighlight(tmpL.quitarline2(lineatmp));
                            tmpL.hilite.removeHighlight(tmp);
                            break;
                        }
                    }
                    start = end + 1;
                }
            } catch (BadLocationException ex) {
                System.out.println(ex.getMessage());
            }
            
            try {
                String text = tmpL.doc.getText(0, tmpL.doc.getLength());
                int start = 0;
                int end = 0;
                int line = 0;
                boolean even = true;
                while ((end = text.indexOf('\n', start)) >= 0) {
                    line++;
                    if (line == linea) {
                        if (!tmpL.exiteLine2(linea)) {
                            tmpL.reflines2.add(tmpL.hilite.addHighlight(start, end + 1, greenPainter));
                            tmpL.lineasdeb2.add(linea);
                            break;
                        }
                    }
                    start = end + 1;
                }
            } catch (BadLocationException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}