/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EjecucionDS;

/**
 *
 * @author Luis
 */
import DracoScript.Cadena;
import java.util.Stack;
import javax.swing.JTextArea;
import AST.Nodo;
import Dibujo.Dibujo;
import draco_web.InterfazD;
import static draco_web.InterfazD.leerArchivo;
import draco_web.LineasText;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
public class InterpreteDS extends Thread{
    DefaultHighlighter.DefaultHighlightPainter redPainter = new DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.decode("#FF5F60"));
    DefaultHighlighter.DefaultHighlightPainter normalPainter = new DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.decode("#FFFFFF"));
    DefaultHighlighter.DefaultHighlightPainter greenPainter = new DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.decode("#7CBC67"));
    
    public Stack<TablaSimbolos> pilaSimbols; // pila actual
    public TablaSimbolos Global; // TS global de la primera instancia
    TablaSimbolos cima; // TS que esta en la cima
    private  JTextArea consola;
    ArrayList<Integer> lineasDeb;
    
    boolean debug;
    boolean detener;
    public boolean debugeando=false; //servira par saber cuadno se esta dandon a la variable continua
    Nodo raiz;
    LineasText tmpL;
    Dibujo dibujo;
    JTabbedPane tabs;
    JLabel noLinea;
    JLabel noColumna;
    String archActual;
    int limite_it=500;
    int lineatmp=-1;
    public InterpreteDS(Nodo raiz,ArrayList<Integer> lineasDeb,String nombArchivo){
        this.raiz=raiz;
        this.lineasDeb=lineasDeb;
        this.debug=true;
        detener=false;
        this.archActual=nombArchivo;
        this.pilaSimbols =  new Stack<>();
        this.Global = new TablaSimbolos("Global", Cadena.ambito_g, true, false,false);
        pilaSimbols.push(this.Global);
        cima= pilaSimbols.peek();
    }
    
    public InterpreteDS(Nodo raiz,String nombArchivo){
        this.raiz=raiz;
        this.archActual=nombArchivo;
        this.pilaSimbols =  new Stack<>();
        this.Global = new TablaSimbolos("Global", Cadena.ambito_g, true, false,false);
        pilaSimbols.push(this.Global);
        cima= pilaSimbols.peek();
        this.debug=false;
        this.detener=false;
    }
    
    public retorno ejecutar(Nodo nodo) {
        retorno retur = new retorno(0, "correcto", "101", "101");//retorno cuando es de tipo vacio
        if (nodo != null) {
            String linea = "0";
            switch (nodo.Term.getNombre()) {
                case Cadena.INICIO: {           
                    File fichero;
                    String ruta;
                    for (Nodo Hijo : nodo.Hijos.get(0).Hijos.get(0).Hijos) {
                        String nomnbre = Hijo.Token.getValor().toString();
                        linea = Hijo.Token.getLinea();
                        String colum5 = Hijo.Token.getColumna();
                        ruta = InterfazD.rutaGenesis + nomnbre;
                        fichero = new File(ruta);
                        if (fichero.exists()) {
                        //<editor-fold>    
                            String texto = leerArchivo(ruta);
                            LineasText tmpL = new LineasText();
                            posicionPuntero(tmpL);
                            tmpL.text_pane.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if (e.getClickCount() == 2) {
                                        //int linea = tmpL.care
                                        int linea = Integer.parseInt(noLinea.getText());
                                        try {
                                            String text = tmpL.doc.getText(0, tmpL.doc.getLength());
                                            int start = 0;
                                            int end = 0;
                                            int line = 0;
                                            boolean even = true;
                                            while ((end = text.indexOf('\n', start)) >= 0) {
                                                line++;
                                                if (line == linea) {
                                                    if (tmpL.exiteLine(linea)) {
                                                        Object tmp = tmpL.hilite.addHighlight(start, end + 1, normalPainter);
                                                        tmpL.hilite.removeHighlight(tmpL.quitarline(linea));
                                                        tmpL.hilite.removeHighlight(tmp);
                                                        break;
                                                    } else {
                                                        tmpL.reflines.add(tmpL.hilite.addHighlight(start, end + 1, redPainter));
                                                        tmpL.lineasdeb.add(linea);
                                                        break;
                                                    }
                                                }
                                                start = end + 1;
                                            }
                                        } catch (BadLocationException ex) {
                                            System.out.println(ex.getMessage());
                                        }
                                        System.out.println("Se ha hecho doble click en la linea -> " + linea);
                                    }
                                }
                            });
                            tmpL.text_pane.setText(texto);
                            JPanel tmpP = new JPanel(new BorderLayout());
                            tmpP.add(tmpL, BorderLayout.WEST);
                            tmpP.add(tmpL.scrollPane, BorderLayout.CENTER);
                            tmpP.setPreferredSize(new Dimension(1, 1));
                            JScrollPane scrollPane = new JScrollPane();
                            scrollPane.setViewportView(tmpP);
                            tabs.addTab(nomnbre, scrollPane);
                            tabs.setSelectedIndex(tabs.getTabCount() - 1);                            
                        //</editor-fold>
                        }else{
                            String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + nomnbre + " L: " + linea + " C: " + colum5 + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);                           
                        }                        
                    }                    
                    //<editor-fold>
                    for (Nodo Hijo : nodo.Hijos.get(1).Hijos) {
                        ejecutar(Hijo);
                        //para el stop
                        if (detener) {
                            return retur;
                        }
                    }
                    //</editor-fold>
                    break;
                }
                case Cadena.DA_VAR: {
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    for (Nodo Hijo : nodo.Hijos.get(1).Hijos) {
                        String name5 = Hijo.Hijos.get(0).Token.getValor().toString();
                        String colum5 = Hijo.Hijos.get(0).Token.getColumna();
                        Simbolo tmp03 = existeVariable2(name5);
                        if (tmp03 == null) {//la variable no esxiste la puedo crear
                            if (Hijo.Hijos.get(1).Hijos.size() > 0) //es declaracion con asignacion/ hay que comprobar tipos XD
                            {
                                retorno ret = ejecutarEXP(Hijo.Hijos.get(1).Hijos.get(0));
                                if (!ret.tipo.equals(Cadena.error)) {
                                    tmp03 = new Simbolo(cima.Nivel, name5, ret.valor, ret.tipo, linea, colum5);
                                    cima.insertar(name5, tmp03);
                                }
                            } else // es solo declaracion 
                            {
                                tmp03 = new Simbolo(cima.Nivel, name5, Cadena.nulo, Cadena.nulo, linea, colum5);
                                cima.insertar(name5, tmp03);
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La variable ya se encuentra definida -> " + name5 + " L: " + linea + " C: " + colum5 + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                    }
                    break;
                    //</editor-fold>
                }
                case Cadena.AS_VAR: {
                    //<editor-fold>
                    String name7 = nodo.Hijos.get(0).Token.getValor().toString();
                    linea = (nodo.Hijos.get(0).Token.getLinea());
                    String colum7 = (nodo.Hijos.get(0).Token.getColumna());
                    Simbolo tmp05 = existeVariable2(name7);
                    if (tmp05 != null) {
                        retorno ret = ejecutarEXP(nodo.Hijos.get(1));// vamos a traer el valor de la expresion
                        if (!ret.tipo.equals(Cadena.error)) {
                            if (tmp05.Tipo.toLowerCase().equals(ret.tipo.toLowerCase()) || tmp05.Tipo.equals(Cadena.nulo)) {
                                tmp05.Valor = ret.valor;
                                tmp05.Tipo = ret.tipo;
                                System.out.println("Asignacion de variable:  " + tmp05.Nombre + " realizada.");
                            } else {//error incompatibilidad de tipos 
                                String error = "ERROR SEMANTICO: Incompatibilidad de tipos en la asignacion de la variable  -> " + name7 + " L: " + linea + " C: " + colum7 + " Clase: " + archActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                            }
                        }
                    } else {//error, variable no declarada
                        String error = "ERROR SEMANTICO: La variable no ha sido definida -> " + name7 + " L: " + linea + " C: " + colum7 + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                    //</editor-fold>                                        
                }
                case Cadena.DETENER:
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    String column1 = (nodo.Hijos.get(0).Token.getColumna() + 1) + "";
                    retorno r1 = new retorno("detener", "detener", linea, column1);
                    r1.detener = true;
                    return r1;
                //</editor-fold>                
                case Cadena.SI:
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    String column3 = nodo.Hijos.get(0).Token.getColumna();
                    retorno r3 = ejecutarEXP(nodo.Hijos.get(1));
                    if (r3.tipo.equals(Cadena.booleano)) {//se comprueba que sea un booleano
                        if (r3.valor.toString().equals("true")) {
                            TablaSimbolos tab = new TablaSimbolos(Cadena.sent, Cadena.ambito_if, cima.retorno, cima.detener, cima.continuar);
                            pilaSimbols.push(tab);
                            cima = tab;
                            for (Nodo hijo : nodo.Hijos.get(2).Hijos) {
                                retorno reto3 = ejecutar(hijo);
                                //aca preguntamos por lo return y break;                                
                                if (reto3.detener) {
                                    if (cima.detener) {//si permite detener, entoces vemos  si es el ultimo hoy hay mas tablas sobre el
                                        pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                        cima = pilaSimbols.peek();
                                        return reto3;
                                    } else {// no permite detener es un error
                                        String error = "ERROR SEMANTICO: La sentencia DETENER no es valida en el ambito que la envuelve." + " L: " + reto3.Linea + " C: " + reto3.Columna + " Clase: " + archActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                        return new retorno("error", Cadena.error, "0", "0");
                                    }
                                }
                            }
                            pilaSimbols.pop();
                            cima = pilaSimbols.peek();
                        } else {
                            for (Nodo hijo : nodo.Hijos.get(3).Hijos) {
                                if (hijo.Hijos.size() > 2) {//es un SINO SI                                      
                                    linea = hijo.Hijos.get(0).Token.getLinea();
                                    String column = (hijo.Hijos.get(0).Token.getColumna());
                                    retorno ret = ejecutarEXP(hijo.Hijos.get(1));
                                    if (ret.tipo.equals(Cadena.booleano)) {
                                        if (ret.valor.toString().equals("true")) {
                                            TablaSimbolos tab = new TablaSimbolos(Cadena.sent, Cadena.ambito_if_else, cima.retorno, cima.detener, cima.continuar);
                                            pilaSimbols.push(tab);
                                            cima = tab;
                                            for (Nodo sub_hijo : hijo.Hijos.get(2).Hijos) {
                                                retorno reto3 = ejecutar(sub_hijo);
                                                //aca preguntamos por lo return y break;                                                   
                                                if (reto3.detener) {
                                                    if (cima.detener) {//si permite detener, entoces vemos  si es el ultimo hoy hay mas tablas sobre el
                                                        pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                                        cima = pilaSimbols.peek();
                                                        return reto3;
                                                    } else {// no permite detener es un error
                                                        String error = "ERROR SEMANTICO: La sentencia DETENER no es valida en el ambito que la envuelve." + " L: " + reto3.Linea + " C: " + reto3.Columna + " Clase: " + archActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                        return new retorno("error", Cadena.error, "0", "0");
                                                    }
                                                }
                                            }
                                            pilaSimbols.pop();
                                            cima = pilaSimbols.peek();
                                            break;
                                        }
                                    } else if (ret.tipo.equals(Cadena.error)) {
                                        return ret;
                                    } else {
                                        String error = "ERROR SEMANTICO: Se debe evaluar una expresion booleana en la condicion del SINO SI -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                        break;
                                    }
                                } else { //es un SINO                                        
                                    TablaSimbolos tab = new TablaSimbolos(cima.Nivel, Cadena.ambito_else, cima.retorno, cima.detener, cima.continuar);
                                    pilaSimbols.push(tab);
                                    cima = tab;
                                    for (Nodo sub_hijo : hijo.Hijos.get(1).Hijos) {
                                        retorno reto3 = ejecutar(sub_hijo);
                                        //aca preguntamos por lo return y break;
                                        if (reto3.retorna) {
                                            if (cima.retorno) { //
                                                pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                                cima = pilaSimbols.peek();
                                                return reto3;
                                            } else { //error no permite retornos
                                                pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                                cima = pilaSimbols.peek();
                                                String error = "ERROR SEMANTICO: La sentencia RETORNO no es valida en el ambito que la envuelve." + " L: " + reto3.Linea + " C: " + reto3.Columna + " Clase: " + archActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                                return new retorno("error", Cadena.error, "0", "0");
                                            }
                                        }
                                        if (reto3.detener) {
                                            if (cima.detener) {//si permite detener, entoces vemos  si es el ultimo hoy hay mas tablas sobre el
                                                pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                                cima = pilaSimbols.peek();
                                                return reto3;
                                            } else {// no permite detener es un error
                                                pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                                cima = pilaSimbols.peek();
                                                String error = "ERROR SEMANTICO: La sentencia DETENER no es valida en el ambito que la envuelve." + " L: " + reto3.Linea + " C: " + reto3.Columna + " Clase: " + archActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                                return new retorno("error", Cadena.error, "0", "0");
                                            }
                                        }
                                        if (reto3.continua) {
                                            if (cima.continuar) {//si permite detener, entoces vemos  si es el ultimo hoy hay mas tablas sobre el                                        
                                                pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                                cima = pilaSimbols.peek();
                                                return reto3;
                                            } else {// no permite detener es un error
                                                pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                                cima = pilaSimbols.peek();
                                                String error = "ERROR SEMANTICO: La sentencia CONTINUAR no es valida en el ambito que la envuelve." + " L: " + reto3.Linea + " C: " + reto3.Columna + " Clase: " + archActual;
                                                InterfazD.listaErrores.add(error);
                                                System.out.println(error);
                                                return new retorno("error", Cadena.error, "0", "0");
                                            }
                                        }
                                    }
                                    pilaSimbols.pop();
                                    cima = pilaSimbols.peek();
                                    break;
                                }
                            }
                        }
                    } else if (r3.tipo.equals(Cadena.error)) {
                        return r3;
                    } else {
                        //no retorno una expresion booleana
                        String error = "ERROR SEMANTICO: Se debe evaluar una expresion booleana en la condicion del SI -> " + " L: " + linea + " C: " + column3 + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                //</editor-fold>                
                case Cadena.MIENTRAS:
                    //<editor-fold>
                    //evaluamos la condicion
                    linea = (nodo.Hijos.get(0).Token.getLinea());
                    String column7 = (nodo.Hijos.get(0).Token.getColumna());
                    retorno r7 = ejecutarEXP(nodo.Hijos.get(1));
                    if (r7.tipo.equals(Cadena.booleano)) {//aca todo va bien
                        int numero_it = 0;
                        while (true) {
                            if ((r7.valor.toString().equals("true")) && numero_it < limite_it) {
                                //agregamos la nueva tabla de simbolos
                                TablaSimbolos tab1 = new TablaSimbolos(Cadena.sent, Cadena.ambito_while, cima.retorno, true, true);
                                pilaSimbols.push(tab1);
                                cima = tab1;
                                for (Nodo hijo : nodo.Hijos.get(2).Hijos) {
                                    retorno reto3 = ejecutar(hijo);
                                    //aca preguntamos por lo return y break;
                                    if (reto3.detener) {
                                        pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                        cima = pilaSimbols.peek();
                                        reto3.detener = false;
                                        return reto3; //a no sigo con lo del if 
                                    }
                                }
                                //despues de evaluar las sentecnias sacamos la tabla 
                                pilaSimbols.pop();
                                cima = pilaSimbols.peek();
                            } else {
                                break;
                            }
                            //evaluamos la condicion de nuevo
                            numero_it++;
                            r7 = ejecutarEXP(nodo.Hijos.get(1)); //aca revalidamos la condicion    
                        }
                    } else if (r7.tipo.equals(Cadena.error)) {
                        return r7;
                    } else {
                        //no retorno una expresion booleana
                        String error = "ERROR SEMANTICO: Se debe evaluar una expresion booleana en la condicion del MIENTRAS -> " + " L: " + linea + " C: " + column7 + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                //</editor-fold>
                case Cadena.PARA:
                    //<editor-fold>
                    linea = (nodo.Hijos.get(0).Token.getLinea());
                    String column10 = (nodo.Hijos.get(0).Token.getColumna());
                    boolean declara = false;
                    if (nodo.Hijos.get(1).Hijos.size() < 3) { //aca es unicamente una asignacion                        
                        String name = nodo.Hijos.get(1).Hijos.get(0).Token.getValor().toString();
                        linea = (nodo.Hijos.get(1).Hijos.get(0).Token.getLinea());
                        String column = (nodo.Hijos.get(1).Hijos.get(0).Token.getColumna());
                        Simbolo sim = existeVariable2(name);
                        if (sim != null) {
                            if (sim.Tipo.equals(Cadena.number) || sim.Tipo.equals(Cadena.nulo) ) {
                                retorno ret = ejecutarEXP(nodo.Hijos.get(1).Hijos.get(1));
                                if (sim.Tipo.toLowerCase().equals(ret.tipo.toLowerCase()) || sim.Tipo.equals(Cadena.nulo) ) {
                                    sim.Valor = ret.valor;
                                    sim.Tipo = ret.tipo;
                                } else {
                                    String error = "ERROR SEMANTICO: Incompatibiidad de tipos en la asigacion del PARA -> " + name + " L: " + linea + " C: " + column + " Clase: " + archActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                    break;
                                }
                            } else {
                                String error = "ERROR SEMANTICO: La variable a asignar en el PARA no es de tipo numerico -> " + name + " L: " + linea + " C: " + column + " Clase: " + archActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                                break;
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La variable a asignar en el PARA no esta definida -> " + name + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                    } else {
                        String name = nodo.Hijos.get(1).Hijos.get(1).Token.getValor().toString();
                        linea = (nodo.Hijos.get(1).Hijos.get(1).Token.getLinea());
                        String column = (nodo.Hijos.get(1).Hijos.get(1).Token.getColumna());
                        Simbolo sim = existeVariable2(name);
                        if (sim == null) {
                            retorno ret = ejecutarEXP(nodo.Hijos.get(1).Hijos.get(2).Hijos.get(0));
                            if (ret.tipo.toLowerCase().equals(Cadena.number)) {
                                // creo una tabla de simbolos temporal para almacenar la variable del for
                                TablaSimbolos tab1 = new TablaSimbolos(Cadena.sent, Cadena.ambito_for + "_tmp", cima.retorno, true, true);
                                pilaSimbols.push(tab1);
                                cima = tab1;
                                sim = new Simbolo(cima.Nivel, name, ret.valor, ret.tipo, linea, column);
                                cima.insertar(name, sim);
                                declara = true;
                            } else {
                                String error = "ERROR SEMANTICO: Incompatibiidad de tipos en la Dec/asigacion del PARA -> " + name + " L: " + linea + " C: " + column + " Clase: " + archActual;
                                InterfazD.listaErrores.add(error);
                                System.out.println(error);
                                break;
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La variable a definir en el PARA ya esta definida -> " + name + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                    }
                    //si llego a este punto procedemos a ejecutar el cuerpo
                    retorno r10 = ejecutarEXP(nodo.Hijos.get(2));
                    // si se cumple prodecemos a crear el abito que dura este ciclo
                    if (r10.tipo.equals(Cadena.booleano)) {//aca todo va bien
                        while (true) {
                            if (r10.valor.toString().equals("true")) {
                                TablaSimbolos tab1 = new TablaSimbolos(cima.Nivel, Cadena.ambito_for, cima.retorno, true, true);
                                pilaSimbols.push(tab1);
                                cima = tab1;
                                // ejecutamos todas las sentencias internas 
                                for (Nodo hijo : nodo.Hijos.get(4).Hijos) {
                                    retorno reto3 = ejecutar(hijo);
                                    // aca preguntamos por lo return y break;                                    
                                    if (reto3.detener) {
                                        pilaSimbols.pop(); //sacamos la tabla de simbolos del if que se inserto
                                        cima = pilaSimbols.peek();
                                        if (declara) {
                                            pilaSimbols.pop();
                                            cima = pilaSimbols.peek();
                                        }
                                        return reto3; //a no sigo con lo del if 
                                    }
                                }
                                //sacamos la tabla del ambito que ya termino
                                pilaSimbols.pop();
                                cima = pilaSimbols.peek();
                            } else {
                                if (declara) {
                                    pilaSimbols.pop();
                                    cima = pilaSimbols.peek();
                                }
                                break;
                            }
                            //ejecutamos el amunto o decremento segun sea el caso.
                            if (ejecutarEXP(nodo.Hijos.get(3)).tipo.equals(Cadena.error)) {//si es un error nos salimos
                                if (declara) {
                                    pilaSimbols.pop();
                                    cima = pilaSimbols.peek();
                                }
                                break;
                            }
                            //re evaluamos la condicion
                            r10 = ejecutarEXP(nodo.Hijos.get(2));
                        }
                    } else if (r10.tipo.equals(Cadena.error)) {
                        if (declara) {
                            pilaSimbols.pop();
                            cima = pilaSimbols.peek();
                        }
                        return r10;
                    } else {//no retorno una expresion booleana
                        if (declara) {
                            pilaSimbols.pop();
                            cima = pilaSimbols.peek();
                        }
                        String error = "ERROR SEMANTICO: Se debe evaluar una expresion booleana en la condicion del PARA -> " + " L: " + linea + " C: " + column10 + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                //</editor-fold>
                case Cadena.IMPRIMIR:
                    //<editor-fold>
                    retorno r11 = ejecutarEXP(nodo.Hijos.get(1));
                    if (!r11.tipo.equals(Cadena.error)) {
                        //aca debo imprimir en la consola del programa
                        System.out.println("IMPRIMIR----> " + r11.valor.toString());
                        notificar(r11.valor.toString());
                    }
                    break;
                //</editor-fold>               
                case Cadena.OP:
                    ejecutarEXP(nodo);
                    break;
                case Cadena.PUNTO:{
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    String column= nodo.Hijos.get(0).Token.getColumna();
                    try {
                        Double tmp;
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(1)).valor + "");
                        int x = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(2)).valor + "");
                        int y = tmp.intValue();
                        retorno ret = ejecutarEXP(nodo.Hijos.get(3));
                        String color ;//="#FF545D";
                        if (ret.tipo.toLowerCase().equals(Cadena.cadena)) {
                            color = ret.valor + "";
                        } else {
                            String error = "ERROR SEMANTICO: El parametro color no es de tipo cadena, funcion PUNTO-> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(4)).valor + "");
                        int diametro = tmp.intValue();
                        if(x>-1 && y >-1 && diametro > -1){
                            dibujo.addPunto(x, y, color, diametro);
                        }else{
                            String error = "ERROR SEMANTICO: Parametro numericos negativos en la funcion PUNTO-> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }                        
                    } catch (NumberFormatException e) {
                        String error = "ERROR SEMANTICO: Parametros numericos incorrectos en la funcion PUNTO-> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                    //</editor-fold>
                }
                case Cadena.OVALO:{
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    String column= nodo.Hijos.get(0).Token.getColumna();
                    try {
                        Double tmp;
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(1)).valor + "");
                        int x = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(2)).valor + "");
                        int y = tmp.intValue();
                        retorno ret = ejecutarEXP(nodo.Hijos.get(3));
                        String color ;//="#FF545D";
                        if (ret.tipo.toLowerCase().equals(Cadena.cadena)) {
                            color = ret.valor + "";
                        } else {
                            String error = "ERROR SEMANTICO: El parametro color no es de tipo cadena, funcion OVALO -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(4)).valor + "");
                        int ancho = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(5)).valor + "");
                        int alto = tmp.intValue();
                        if(x>-1 && y >-1 && ancho>-1 && alto>-1){
                            dibujo.addOvalo(x, y, color, ancho, alto);
                        }else{
                            String error = "ERROR SEMANTICO: Parametro numericos negativos en la funcion OVALO -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }                        
                    } catch (NumberFormatException e) {
                        String error = "ERROR SEMANTICO: Parametros numericos incorrectos en la funcion OVALO -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                    //</editor-fold>
                }
                case Cadena.CUADRADO:{
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    String column= nodo.Hijos.get(0).Token.getColumna();
                    try {
                        Double tmp;
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(1)).valor + "");
                        int x = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(2)).valor + "");
                        int y = tmp.intValue();
                        retorno ret = ejecutarEXP(nodo.Hijos.get(3));
                        String color ;//="#FF545D";
                        if (ret.tipo.toLowerCase().equals(Cadena.cadena)) {
                            color = ret.valor + "";
                        } else {
                            String error = "ERROR SEMANTICO: El parametro color no es de tipo cadena, funcion CUADRADO -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(4)).valor + "");
                        int ancho = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(5)).valor + "");
                        int alto = tmp.intValue();                                                
                        if(x>-1 && y >-1 && ancho>-1 && alto>-1){
                            dibujo.addCuadrado(x, y, color, ancho,alto);
                        }else{
                            String error = "ERROR SEMANTICO: Parametro numericos negativos en la funcion CUADRADO -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }
                    } catch (NumberFormatException e) {
                        String error = "ERROR SEMANTICO: Parametros numericos incorrectos en la funcion CUADRADO -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                    //</editor-fold>
                }
                case Cadena.LINEA:{
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    String column= nodo.Hijos.get(0).Token.getColumna();
                    try {
                        Double tmp;
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(1)).valor + "");
                        int x = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(2)).valor + "");
                        int y = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(3)).valor + "");
                        int x2 = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(4)).valor + "");
                        int y2 = tmp.intValue();                        
                        retorno ret = ejecutarEXP(nodo.Hijos.get(5));
                        String color ;//="#FF545D";
                        if (ret.tipo.toLowerCase().equals(Cadena.cadena)) {
                            color = ret.valor + "";
                        } else {
                            String error = "ERROR SEMANTICO: El parametro color no es de tipo cadena, funcion LINEA -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(6)).valor + "");
                        int grosor= tmp.intValue();
                        if(x>-1 && y >-1 && x2>-1 && y2>-1 && grosor>-1){
                            dibujo.addLinea(x, y, x2,y2, color, grosor);
                        }else{
                            String error = "ERROR SEMANTICO: Parametro numericos negativos en la funcion LINEA -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }                        
                    } catch (NumberFormatException e) {
                        String error = "ERROR SEMANTICO: Parametros numericos incorrectos en la funcion LINEA -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                    //</editor-fold>
                }
                case Cadena.CADENA:{
                    //<editor-fold>
                    linea = nodo.Hijos.get(0).Token.getLinea();
                    String column= nodo.Hijos.get(0).Token.getColumna();
                    try {
                        Double tmp;
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(1)).valor + "");
                        int x = tmp.intValue();
                        tmp = Double.parseDouble(ejecutarEXP(nodo.Hijos.get(2)).valor + "");
                        int y = tmp.intValue();
                        retorno ret = ejecutarEXP(nodo.Hijos.get(3));
                        String color ;//="#FF545D";
                        if (ret.tipo.toLowerCase().equals(Cadena.cadena)) {
                            color = ret.valor + "";
                        } else {
                            String error = "ERROR SEMANTICO: El parametro color no es de tipo cadena, funcion CADENA-> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                        ret = ejecutarEXP(nodo.Hijos.get(4));
                        String cadena;//="#FF545D";
                        if (ret.tipo.toLowerCase().equals(Cadena.cadena)) {
                            cadena = ret.valor + "";
                        } else {
                            String error = "ERROR SEMANTICO: El parametro cadena no es de tipo cadena, funcion CADENA-> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            break;
                        }
                        
                        if(x>-1 && y >-1){
                            dibujo.addTexto(x, y, color,cadena);
                        }else{
                            String error = "ERROR SEMANTICO: Parametro numericos negativos en la funcion CADENA -> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                        }                        
                    } catch (NumberFormatException e) {
                        String error = "ERROR SEMANTICO: Parametros numericos incorrectos en la funcion CADENA-> " + " L: " + linea + " C: " + column + " Clase: " + archActual;
                        InterfazD.listaErrores.add(error);
                        System.out.println(error);
                    }
                    break;
                    //</editor-fold>
                }                    
            }
            //<editor-fold desc="Codigo debug">
            if (debug) {
                if (lineadeb(linea) || debugeando) {
                    pintarLinea(Integer.parseInt(linea), lineatmp);
                    lineatmp = Integer.parseInt(linea);
                    notificar("DEBUG ALERT: Se detuvo en la linea : " + linea);
                    this.suspend(); // good practice                                    
                }
            }

            //</editor-fold>
        }
        return retur;
    }
    
    public retorno ejecutarEXP(Nodo nodo) {
        switch (nodo.Term.getNombre()) {
            case Cadena.LOG:
                //<editor-fold>
                switch (nodo.Hijos.size()) {
                    case 3: // Puede ser un OR o AND
                        String line2 = (nodo.Hijos.get(1).Token.getLinea()) + "";
                        String column2 = (nodo.Hijos.get(1).Token.getLinea()) + "";
                        retorno ret2 = ejecutarEXP(nodo.Hijos.get(0));
                        retorno ret22 = ejecutarEXP(nodo.Hijos.get(2));
                        if (ret2.tipo.toLowerCase().equals(Cadena.booleano) && ret22.tipo.toLowerCase().equals(Cadena.booleano)) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "||":
                                    if (ret2.valor.toString().toLowerCase().equals("true")  || ret22.valor.toString().toLowerCase().equals("true") ) {
                                        return new retorno("true", Cadena.booleano, line2, column2);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line2, column2);
                                    }
                                case "&&":
                                    if ((ret2.valor.toString().toLowerCase().equals("true") ) && (ret22.valor.toString().toLowerCase().equals("true"))) {
                                        return new retorno("true", Cadena.booleano, line2, column2);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line2, column2);
                                    }
                            }
                        } else {
                            String error = "ERROR SEMANTICO: Una de las expresiones a evaluar en la condicion logica no es de tipo booleano -> EXP " + " L: " + line2 + " C: " + column2 + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            return new retorno("error", Cadena.error, line2, column2);
                        }
                        break;
                    case 1:
                        return ejecutarEXP(nodo.Hijos.get(0));
                }
                //</editor-fold>
                break;
            case Cadena.REL:
                //<editor-fold>
                switch (nodo.Hijos.size()) {
                    case 3:
                        String line3 = (nodo.Hijos.get(1).Token.getLinea()) + "";
                        String column3 = (nodo.Hijos.get(1).Token.getColumna() + 1) + "";
                        retorno ret3 = ejecutarEXP(nodo.Hijos.get(0));
                        retorno ret33 = ejecutarEXP(nodo.Hijos.get(2));

                        if (((ret3.tipo.toLowerCase().equals(Cadena.number)) && ret33.tipo.toLowerCase().equals(Cadena.number))) {
                            Double num1 = Double.parseDouble(ret3.valor.toString());
                            Double num2 = Double.parseDouble(ret33.valor.toString());
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":
                                    if (Objects.equals(num1, num2)) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case "!=":
                                    if (!Objects.equals(num1, num2)) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case ">":
                                    if (num1 > num2) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case "<":
                                    if (num1 < num2) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case ">=":
                                    if (num1 >= num2) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case "<=":
                                    if (num1 <= num2) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                            }
                        } else if ((ret3.tipo.toLowerCase().equals(Cadena.caracter) && ret33.tipo.toLowerCase().equals(Cadena.caracter))
                                || (ret3.tipo.toLowerCase().equals(Cadena.booleano) && ret33.tipo.toLowerCase().equals(Cadena.booleano))) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":
                                    if (ret3.valor.toString().equals(ret33.valor.toString())) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case "!=":
                                    if (!ret3.valor.toString().equals(ret33.valor.toString())) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                default:
                                    String error = "ERROR SEMANTICO: Operador invalido para comparar datos del mismo tipo -> " + nodo.Hijos.get(1).Token.getValor().toString() + " L: " + line3 + " C: " + column3 + " Clase: " + archActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                    return new retorno("error", Cadena.error, line3, column3);
                                // error
                                }

                        } else if ((ret3.tipo.toLowerCase().equals(Cadena.cadena) && ret33.tipo.toLowerCase().equals(Cadena.cadena))) {
                            String cad1 = ret3.valor + "";
                            String cad2 = ret33.valor + "";

                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":
                                    if (Objects.equals(ret3.valor, ret33.valor)) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case "!=":
                                    if (!Objects.equals(ret3.valor, ret33.valor)) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case ">":
                                    if (cad1.length() > cad2.length()) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case "<":
                                    if (cad1.length() < cad2.length()) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case ">=":
                                    if (cad1.length() >= cad2.length()) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                                case "<=":
                                    if (cad1.length() <= cad2.length()) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    } else {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }
                            }
                        } else if (ret3.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase()) || ret33.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase())
                                && (!ret3.tipo.toLowerCase().equals(Cadena.error.toLowerCase()) && !ret33.tipo.toLowerCase().equals(Cadena.error.toLowerCase()))) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "==":
                                    if (ret3.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase()) && ret33.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                        return new retorno("true", Cadena.booleano, line3, column3);
                                    }

                                    if (ret3.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                        if (ret33.valor.toString().toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                            return new retorno("true", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("false", Cadena.booleano, line3, column3);
                                        }
                                    } else {
                                        if (ret3.valor.toString().toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                            return new retorno("true", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("false", Cadena.booleano, line3, column3);
                                        }
                                    }
                                case "!=":
                                    if (ret3.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase()) && ret33.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                        return new retorno("false", Cadena.booleano, line3, column3);
                                    }

                                    if (ret3.tipo.toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                        if (!ret33.valor.toString().toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                            return new retorno("true", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("false", Cadena.booleano, line3, column3);
                                        }
                                    } else {
                                        if (!ret3.valor.toString().toLowerCase().equals(Cadena.nulo.toLowerCase())) {
                                            return new retorno("true", Cadena.booleano, line3, column3);
                                        } else {
                                            return new retorno("false", Cadena.booleano, line3, column3);
                                        }
                                    }
                                default:
                                    String error = "ERROR SEMANTICO: Operador invalido para comparar datos con NULO -> " + nodo.Hijos.get(1).Token.getValor().toString() + " L: " + line3 + " C: " + column3 + " Clase: " + archActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                    return new retorno("error", Cadena.error, line3, column3);
                                // error
                                }
                        } else {
                            String error = "ERROR SEMANTICO: Expresiones no validas en la operacion relacional REL -> " + nodo.Hijos.get(1).Token.getValor().toString() + " L: " + line3 + " C: " + column3 + " Clase: " + archActual;
                            InterfazD.listaErrores.add(error);
                            System.out.println(error);
                            return new retorno("error", Cadena.error, line3, column3);
                        }
                        break;
                    case 1:
                        return ejecutarEXP(nodo.Hijos.get(0));
                }

                //</editor-fold>
                break;
            case Cadena.ARIT:
                //<editor-fold>
                switch (nodo.Hijos.size()) {
                    case 3:
                        //<editor-fold>
                        String line4 = (nodo.Hijos.get(1).Token.getLinea());
                        String column4 = (nodo.Hijos.get(1).Token.getLinea());
                        retorno ret4 = ejecutarEXP(nodo.Hijos.get(0));
                        retorno ret44 = ejecutarEXP(nodo.Hijos.get(2));
                        if (!ret4.tipo.equals(Cadena.error) && !ret44.tipo.equals(Cadena.error)) {
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
//======================================SUMA==================================================================>
                                case "+": {
                                    //<editor-fold>
                                    switch (ret4.tipo) {
                                        case Cadena.number: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (!isInteger(ret4.valor + "") || !isInteger(ret44.valor + "")) {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        Double num2 = Double.parseDouble(ret44.valor + "");
                                                        num1 += num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {//ambos son enteros
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        int num2 = Integer.parseInt(ret44.valor + "");
                                                        num1 += num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if (isInteger(ret4.valor + "")) {
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            num1 += 1;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            num1 += 1.0;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);
                                                    if (isInteger(ret4.valor + "")) {
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        num1 += charmel;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        num1 += Double.parseDouble(charmel + "");
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.cadena: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.booleano: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (isInteger(ret44.valor + "")) {
                                                        int num1 = Integer.parseInt(ret44.valor + "");
                                                        if (ret4.valor.equals(Cadena.trus)) {
                                                            num1 += 1;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret44.valor + "");
                                                        if (ret4.valor.equals(Cadena.trus)) {
                                                            num1 += 1.0;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if (ret4.valor.equals(Cadena.trus) || ret44.valor.equals(Cadena.trus)) {
                                                        return new retorno(Cadena.trus, Cadena.booleano, line4, column4);
                                                    }
                                                    return new retorno(Cadena.fals, Cadena.booleano, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                case Cadena.cadena: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.caracter: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    int charmel = (int) (ret4.valor + "").charAt(0);
                                                    if (isInteger(ret44.valor + "")) {
                                                        int num1 = Integer.parseInt(ret44.valor + "");
                                                        num1 += charmel;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret44.valor + "");
                                                        num1 += Double.parseDouble(charmel + "");
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                case Cadena.cadena: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.cadena: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                case Cadena.cadena: {
                                                    String cad = ret4.valor + "" + ret44.valor;
                                                    return new retorno(cad, Cadena.cadena, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '+' " + ret4.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                    //</editor-fold>    
                                }
//======================================RESTA=================================================================>
                                case "-": {
                                    //<editor-fold>
                                    switch (ret4.tipo) {
                                        case Cadena.number: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (!isInteger(ret4.valor + "") || !isInteger(ret44.valor + "")) {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        Double num2 = Double.parseDouble(ret44.valor + "");
                                                        num1 -= num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {//ambos son enteros
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        int num2 = Integer.parseInt(ret44.valor + "");
                                                        num1 -= num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if (isInteger(ret4.valor + "")) {
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            num1 -= 1;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            num1 -= 1.0;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);
                                                    if (isInteger(ret4.valor + "")) {
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        num1 -= charmel;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        num1 -= Double.parseDouble(charmel + "");
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.booleano: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (isInteger(ret44.valor + "")) {
                                                        int num1 = Integer.parseInt(ret44.valor + "");
                                                        if (ret4.valor.equals(Cadena.trus)) {
                                                            num1 = 1 - num1;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret44.valor + "");
                                                        if (ret4.valor.equals(Cadena.trus)) {
                                                            num1 = 1.0 - num1;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if (ret4.valor.equals(Cadena.trus)) {
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            return new retorno(0, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(1, Cadena.number, line4, column4);
                                                    } else {
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            return new retorno(-1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(0, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);
                                                    if (ret4.valor.equals(Cadena.trus)) {
                                                        int num = 1 - charmel;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    } else {
                                                        int num = 0 - charmel;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.caracter: {
                                            //<editor-fold>
                                            int charmel = (int) (ret4.valor + "").charAt(0);
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (isInteger(ret44.valor + "")) {
                                                        int num = Integer.parseInt(ret44.valor + "");
                                                        num = charmel - num;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    double num = Double.parseDouble(ret44.valor + "");
                                                    num = Double.parseDouble(charmel + "") - num;
                                                    return new retorno(num, Cadena.number, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    if (ret44.valor.equals(Cadena.trus)) {
                                                        charmel -= 1;
                                                        return new retorno(charmel, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(charmel, Cadena.number, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel2 = (int) (ret44.valor + "").charAt(0);
                                                    charmel -= charmel2;
                                                    return new retorno(charmel, Cadena.number, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '-' " + ret4.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                    //</editor-fold> 
                                }
//======================================MULTI==================================================================>
                                case "*": {
                                    //<editor-fold>
                                    switch (ret4.tipo) {
                                        case Cadena.number: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (!isInteger(ret4.valor + "") || !isInteger(ret44.valor + "")) {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        Double num2 = Double.parseDouble(ret44.valor + "");
                                                        num1 *= num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {//ambos son enteros
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        int num2 = Integer.parseInt(ret44.valor + "");
                                                        num1 *= num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if (isInteger(ret4.valor + "")) {
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            num1 *= 1;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(0, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        if (ret44.valor.equals(Cadena.trus)) {
                                                            num1 *= 1.0;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(0, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);
                                                    if (isInteger(ret4.valor + "")) {
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        num1 *= charmel;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        num1 *= Double.parseDouble(charmel + "");
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.booleano: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (isInteger(ret44.valor + "")) {
                                                        int num1 = Integer.parseInt(ret44.valor + "");
                                                        if (ret4.valor.equals(Cadena.trus)) {
                                                            num1 *= 1;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(0, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret44.valor + "");
                                                        if (ret4.valor.equals(Cadena.trus)) {
                                                            num1 *= 1.0;
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        }
                                                        return new retorno(0, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if (ret4.valor.equals(Cadena.trus) && ret44.valor.equals(Cadena.trus)) {
                                                        return new retorno(Cadena.trus, Cadena.booleano, line4, column4);
                                                    }
                                                    return new retorno(Cadena.fals, Cadena.booleano, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);
                                                    if (ret4.valor.equals(Cadena.trus)) {
                                                        return new retorno(charmel, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.caracter: {
                                            //<editor-fold>
                                            int charmel = (int) (ret4.valor + "").charAt(0);
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (isInteger(ret44.valor + "")) {
                                                        int num1 = Integer.parseInt(ret44.valor + "");
                                                        num1 *= charmel;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } else {
                                                        Double num1 = Double.parseDouble(ret44.valor + "");
                                                        num1 *= Double.parseDouble(charmel + "");
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if (ret44.valor.equals(Cadena.trus)) {
                                                        return new retorno(charmel, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel2 = (int) (ret44.valor + "").charAt(0);
                                                    charmel *= charmel2;
                                                    return new retorno(charmel, Cadena.number, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '*' " + ret4.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                    //</editor-fold> 
                                }
//======================================DIVI==================================================================>
                                case "/": {
                                    //<editor-fold>
                                    switch (ret4.tipo) {
                                        case Cadena.number: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    
                                                    int num1 = Integer.parseInt(ret4.valor + "");
                                                    int num2 = Integer.parseInt(ret44.valor + "");
                                                    if (num2 != 0) {
                                                        num1 /= num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                    
                                                }
                                                case Cadena.booleano: {                                                    
                                                    if(ret44.valor.equals(Cadena.trus)){
                                                        return new retorno(ret4.valor, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);                                                    
                                                    if(charmel!= 0){
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        num1 /= Double.parseDouble(charmel + "");
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);                                                    
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.booleano: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if(ret4.valor.equals(Cadena.trus)){
                                                        Double num = Double.parseDouble(ret44.valor+"");
                                                        num=1/num;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    if(ret44.valor.equals(Cadena.trus)){                                                        
                                                        if(ret4.valor.equals(Cadena.trus)){
                                                            return new retorno(1, Cadena.number, line4, column4);
                                                        }                                               
                                                        return new retorno(0, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);
                                                    if(ret4.valor.equals(Cadena.trus)){
                                                        Double num=1/ Double.parseDouble(charmel+"");
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.caracter: {
                                            //<editor-fold>
                                            int charmel = (int) (ret4.valor + "").charAt(0);
                                            switch (ret44.tipo) {
                                                case Cadena.number: {                                                    
                                                    Double num = Double.parseDouble(ret44.valor + "");
                                                    if (num != 0) {
                                                        num = Double.parseDouble(charmel + "") / num;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    if(ret44.valor.equals(Cadena.trus)){
                                                        return new retorno(charmel, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel2 = (int) (ret44.valor + "").charAt(0);
                                                    if(charmel2!=0){
                                                        Double num= Double.parseDouble(charmel+"")/Double.parseDouble(charmel2+"");
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '/' " + ret4.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                    //</editor-fold> 
                                }
//======================================MODULO==================================================================>
                                case "%": {
                                    //<editor-fold>
                                    switch (ret4.tipo) {
                                        case Cadena.number: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    int num1 = Integer.parseInt(ret4.valor + "");
                                                    int num2 = Integer.parseInt(ret44.valor + "");
                                                    if (num2 != 0) {
                                                        num1 %= num2;
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    if(ret44.valor.equals(Cadena.trus)){
                                                        Double num = Double.parseDouble(ret4.valor+"");
                                                        num= num%1;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);                                                    
                                                    if(charmel!= 0){
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        num1 %= Double.parseDouble(charmel + "");
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '%' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.booleano: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if(ret4.valor.equals(Cadena.trus)){
                                                        Double num = Double.parseDouble(ret44.valor+"");
                                                        num=1%num;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    if(ret44.valor.equals(Cadena.trus)){                                                        
                                                        if(ret4.valor.equals(Cadena.trus)){
                                                            return new retorno(0, Cadena.number, line4, column4);
                                                        }                                                        
                                                        return new retorno(0, Cadena.number, line4, column4);
                                                    }
                                                    
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);
                                                    if(ret4.valor.equals(Cadena.trus)){
                                                        if( charmel!=0){
                                                            return new retorno(1, Cadena.number, line4, column4);
                                                        }
                                                        String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                        return new retorno("error", Cadena.error, line4, column4);                                                        
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '%' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.caracter: {
                                            //<editor-fold>
                                            int charmel = (int)(ret4.valor + "").charAt(0);
                                            switch (ret44.tipo) {                                                
                                                case Cadena.number: {
                                                    Double num = Double.parseDouble(ret44.valor + "");
                                                    if (num != 0) {
                                                        num = Double.parseDouble(charmel + "") / num;
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    if(ret44.valor.equals(Cadena.trus)){
                                                        return new retorno(charmel, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    int charmel2 = (int) (ret44.valor + "").charAt(0);
                                                    if(charmel2!=0){
                                                        Double num= Double.parseDouble(charmel+"")%Double.parseDouble(charmel2+"");
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    }
                                                    String error = "ERROR SEMANTICO: No es posible dividir un cantidad sobre '0'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '%' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '%' " + ret4.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                    //</editor-fold>
                                }
//======================================POT==================================================================>
                                case "^":{
                                    //<editor-fold>
                                    switch (ret4.tipo) {
                                        case Cadena.number: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if (!isInteger(ret4.valor + "") || !isInteger(ret44.valor + "")) {
                                                        Double num1 = Double.parseDouble(ret4.valor + "");
                                                        Double num2 = Double.parseDouble(ret44.valor + "");
                                                        try {
                                                            num1 = Math.pow(num1, num2);
                                                            return new retorno(num1, Cadena.number, line4, column4);
                                                        } catch (Exception e){
                                                            String error = "ERROR SEMANTICO: Se exsedio la capacidad de un entero en la operacion '^'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                            InterfazD.listaErrores.add(error);
                                                            System.out.println(error);
                                                            return new retorno("error", Cadena.error, line4, column4);
                                                        }                                                        
                                                    } else {//ambos son enteros
                                                        int num1 = Integer.parseInt(ret4.valor + "");
                                                        int num2 = Integer.parseInt(ret44.valor + "");
                                                        try {
                                                            Double num = Math.pow(num1, num2) ;
                                                            return new retorno(num, Cadena.number, line4, column4);
                                                        } catch (Exception e){
                                                            String error = "ERROR SEMANTICO: Se exsedio la capacidad de un entero en la operacion '^'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                            InterfazD.listaErrores.add(error);
                                                            System.out.println(error);
                                                            return new retorno("error", Cadena.error, line4, column4);
                                                        }
                                                    }
                                                }
                                                case Cadena.booleano: {                                                    
                                                    if(ret44.valor.equals(Cadena.trus)){
                                                        return new retorno(ret4.valor, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(1, Cadena.number, line4, column4);    
                                                }
                                                case Cadena.caracter: {
                                                    int charmel = (int) (ret44.valor + "").charAt(0);  
                                                    Double num1 = Double.parseDouble(ret4.valor+"");
                                                    try {
                                                        num1 = Math.pow(num1, Double.parseDouble(charmel + ""));
                                                        return new retorno(num1, Cadena.number, line4, column4);
                                                    } catch (NumberFormatException e) {
                                                        String error = "ERROR SEMANTICO: Se exsedio la capacidad de un entero en la operacion '^'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                        return new retorno("error", Cadena.error, line4, column4);
                                                    }
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.booleano: {
                                            //<editor-fold>
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    if(ret4.valor.equals(Cadena.trus)){
                                                        return new retorno(1, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                case Cadena.booleano: {
                                                    if(ret4.valor.equals(Cadena.trus)){
                                                        return new retorno(1, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                case Cadena.caracter: {
                                                    if(ret4.valor.equals(Cadena.trus)){
                                                        return new retorno(1, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(0, Cadena.number, line4, column4);
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        case Cadena.caracter: {
                                            //<editor-fold>
                                            int charmel = (int)(ret4.valor + "").charAt(0);
                                            switch (ret44.tipo) {
                                                case Cadena.number: {
                                                    Double num= Double.parseDouble(ret44.valor+"");
                                                    try {
                                                        num = Math.pow( Double.parseDouble(charmel+"") , num);
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    } catch (NumberFormatException e) {
                                                        String error = "ERROR SEMANTICO: Se exsedio la capacidad de un entero en la operacion '^'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                        return new retorno("error", Cadena.error, line4, column4);
                                                    }
                                                }
                                                case Cadena.booleano: {
                                                    if(ret44.valor.equals(Cadena.trus)){
                                                        return new retorno(ret4.valor, Cadena.number, line4, column4);
                                                    }
                                                    return new retorno(1, Cadena.number, line4, column4);
                                                }
                                                case Cadena.caracter:{
                                                   int charmel2 = (int) (ret44.valor + "").charAt(0);
                                                   try {
                                                        Double num = Math.pow( Double.parseDouble(charmel+"") ,Double.parseDouble(charmel2+""));
                                                        return new retorno(num, Cadena.number, line4, column4);
                                                    } catch (NumberFormatException e) {
                                                        String error = "ERROR SEMANTICO: Se exsedio la capacidad de un entero en la operacion '^'" + " L: " + line4 + " C: " + column4 + " Clase: " + archActual;
                                                        InterfazD.listaErrores.add(error);
                                                        System.out.println(error);
                                                        return new retorno("error", Cadena.error, line4, column4);
                                                    }
                                                }
                                                default:
                                                    String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' " + ret44.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                                    InterfazD.listaErrores.add(error);
                                                    System.out.println(error);
                                                    return new retorno("error", Cadena.error, line4, column4);
                                            }
                                            //</editor-fold>
                                        }
                                        default:
                                            String error = "ERROR SEMANTICO: Tipo no valido en la operacion Aritmetica -> '^' " + ret4.tipo + " L: " + line4 + " C: " + column4 + " Archivo: " + archActual;
                                            InterfazD.listaErrores.add(error);
                                            System.out.println(error);
                                            return new retorno("error", Cadena.error, line4, column4);
                                    }
                                    //</editor-fold>
                                }
                            }
                        } else {
                            if (ret4.tipo.equals(Cadena.error)) {
                                return ret4;
                            } else {
                                return ret44;
                            }
                        }
                        //</editor-fold>                        
                    case 2:
                        //<editor-fold>
                        //es la negacion logica
                        if (nodo.Hijos.get(0).Token != null) {                            
                            if (nodo.Hijos.get(0).Token.getNombre().equals(Cadena.not)) {
                                retorno ret = ejecutarEXP(nodo.Hijos.get(1));
                                if (ret.tipo.toLowerCase().equals(Cadena.booleano)) {
                                    if (ret.valor.toString().toLowerCase().equals("true")) {
                                        ret.valor = "false";
                                        return ret;
                                    } else {
                                        ret.valor = "true";
                                        return ret;
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: La expresion a negar logicamente, no es de tipo booleano ->  " + ret.tipo + " L: " + ret.Linea + " C: " + ret.Columna + " Clase: " + archActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                    return new retorno("error", Cadena.error, "0", "0");
                                }
                            } //es la negacion aritmetica
                            else {
                                retorno ret = ejecutarEXP(nodo.Hijos.get(1));
                                if (ret.tipo.toLowerCase().equals(Cadena.number)) {
                                    if ( isInteger(ret.valor.toString()) )//es un entero
                                    {
                                        int num = Integer.parseInt(ret.valor.toString());
                                        ret.valor = (num * -1);
                                        return ret;
                                    } else// es un decimal
                                    {
                                        double num = Double.parseDouble(ret.valor.toString());
                                        ret.valor = (num * - 1.0);
                                        return ret;
                                    }
                                } else {
                                    String error = "ERROR SEMANTICO: La expresion a negar aritmeticamente, no es de tipo numerico ->  " + ret.tipo + " L: " + ret.Linea + " C: " + ret.Columna + " Clase: " + archActual;
                                    InterfazD.listaErrores.add(error);
                                    System.out.println(error);
                                    return new retorno("error", Cadena.error, "0", "0");
                                }
                            }
                        } // es una expresion                        
                        break;
                    //</editor-fold>
                    //========================Son las funciones especiales==============================
                    case 1:
                        //<editor-fold>
                        //aca son todos van todos los nodos terminales 
                        if (nodo.Hijos.get(0).Token != null) {
                            switch (nodo.Hijos.get(0).Token.getNombre()) {
                                case Cadena.id:
                                    Simbolo tmp00 = existeVariable2(nodo.Hijos.get(0).Token.getValor().toString());
                                    if (tmp00 != null) { //aca no se por que no estaba retornando los objetos
                                        //if(tmp00.TipoObjeto.equals("")){
                                        retorno rt = new retorno(tmp00.Valor, tmp00.Tipo, tmp00.Linea, tmp00.Columna);
                                        return rt;
                                    } else { // variable no existe
                                        String error = "ERROR SEMANTICO: La variable a acceder no esta definida ->  " + nodo.Hijos.get(0).Token.getValor().toString() + " L: " + (nodo.Hijos.get(0).Token.getLinea()) + " C: " + (nodo.Hijos.get(0).Token.getColumna() + 1) + " Clase: " + archActual;
                                        InterfazD.listaErrores.add(error);
                                        System.out.println(error);
                                        return new retorno("error", Cadena.error, "0", "0");
                                    }
                                case Cadena.cadena:
                                    return new retorno(nodo.Hijos.get(0).Token.getValor(), Cadena.cadena, (nodo.Hijos.get(0).Token.getLinea()), (nodo.Hijos.get(0).Token.getColumna()));
                                case Cadena.number:
                                    return new retorno(nodo.Hijos.get(0).Token.getValor(), Cadena.number, (nodo.Hijos.get(0).Token.getLinea()) , (nodo.Hijos.get(0).Token.getColumna()));                               
                                case Cadena.caracter:
                                    return new retorno(nodo.Hijos.get(0).Token.getValor(), Cadena.caracter, (nodo.Hijos.get(0).Token.getLinea()), (nodo.Hijos.get(0).Token.getColumna()));                                
                                case Cadena.booleano:
                                    return new retorno(nodo.Hijos.get(0).Token.getValor(), Cadena.booleano, (nodo.Hijos.get(0).Token.getLinea()), (nodo.Hijos.get(0).Token.getColumna()));
                                case Cadena.nulo:
                                    return new retorno(Cadena.nulo, Cadena.nulo, (nodo.Hijos.get(0).Token.getLinea()), (nodo.Hijos.get(0).Token.getColumna()));
                            }
                        } //resto de nodos de funncion
                        else {
                            return ejecutarEXP(nodo.Hijos.get(0));
                        }
                        break;
                    //</editor-fold>
                    }
                //</editor-fold>
                break;
            //RESTO DE FUNCIONES
            //<editor-fold>
            case Cadena.OP://esto lo tengo que pasar a las instrucciones de cuerpo
                //<editor-fold>
                //es un id
                if (nodo.Hijos.get(0).Token != null) {
                    String name = nodo.Hijos.get(0).Token.getValor().toString();
                    String line = (nodo.Hijos.get(0).Token.getLinea()) + "";
                    String column = (nodo.Hijos.get(0).Token.getColumna() + 1) + "";
                    Simbolo tmp01 = existeVariable2(name);
                    if (tmp01 != null) {
                        if (tmp01.Tipo.toLowerCase().equals(Cadena.number)){
                            switch (nodo.Hijos.get(1).Token.getValor().toString()) {
                                case "++":
                                    if (isInteger(tmp01.Valor.toString())) {
                                        int num = Integer.parseInt(tmp01.Valor.toString()) + 1;
                                        tmp01.Valor = num;
                                        return new retorno(num, Cadena.number, line, column);
                                    } else {
                                        double num = Double.parseDouble(tmp01.Valor.toString()) + 1;
                                        tmp01.Valor = num;
                                        return new retorno(num, Cadena.number, line, column);
                                    }
                                case "--":
                                     if (isInteger(tmp01.Valor.toString())) {
                                        int num = Integer.parseInt(tmp01.Valor.toString())-1;
                                        tmp01.Valor = num;
                                        return new retorno(num, Cadena.number, line, column);
                                    } else {
                                        double num = Double.parseDouble(tmp01.Valor.toString())-1;
                                        tmp01.Valor = num;
                                        return new retorno(num, Cadena.number, line, column);
                                    }
                            }
                        } else {
                            String error = "ERROR SEMANTICO: La variable a inc/dec debe ser de tipo numerico ->  " + name + " L: " + line + " C: " + column;
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
                //</editor-fold>
                break;
        }
        return new retorno(1, "entero", "01001", "01001");
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

    private  void notificar(String mensaje){
        consola.append(mensaje+"\n");
    }
    
    public boolean isInteger(String numero){
    try{
        Integer.parseInt(numero);
        return true;
    }catch(NumberFormatException e){
        return false;
    }
}
    
    private Simbolo existeVariable2(String id) //declara/asigna local
    { //metodo para obtener el valor de las variables
        if(!cima.Nivel.equals("Global")){
            String nombre = cima.Nivel;
            for (int j = pilaSimbols.size() - 1; j > -1; j--) {
                if (pilaSimbols.get(j).Nivel.equals(nombre)) {
                    if (pilaSimbols.get(j).existeSimbolo(id)) {
                        Simbolo sim =pilaSimbols.get(j).retornaSimbolo(id);
                        return sim;
                    }
                }
            }
        }
        return Global.retornaSimbolo(id);
    }
    
    private boolean  lineadeb(String linea){
        int line = Integer.parseInt(linea);
        for (Integer integer : lineasDeb) {
            if(line==integer)
                return true;
        }
        return false;
    }
    
    private void posicionPuntero(LineasText lines){
        lines.text_pane.addCaretListener(new CaretListener(){          
            @Override
            public void caretUpdate(CaretEvent e) {
                int pos = e.getDot();
		int fila = 1, columna=0;
		int ultimalinea=-1;
		String text =lines.text_pane.getText().replaceAll("\r","");
				
		for(int i=0;i<pos;i++){
                    if(text.charAt(i)==10){
                        fila++;
                        ultimalinea=i;
                    }
                }		
		columna=pos-ultimalinea;                
                noLinea.setText(fila +"");
                noColumna.setText(columna+"");
            }
        });
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
    
    
    public void iniciar_Coponentes(JTextArea consola, LineasText ltmp, Dibujo dibujo, JTabbedPane pane,JLabel line, JLabel colum){
        this.consola=consola;
        this.tmpL=ltmp;
        this.dibujo=dibujo;
        this.tabs=pane;
        this.noLinea=line;
        this.noColumna = colum;
    }
    
    @Override
    public void run(){        
        ejecutar(raiz);
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
