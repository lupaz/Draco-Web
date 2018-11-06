/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EjecucionDASM;

import AST.Nodo;
import Dasm.Cadena;
import draco_web.LineasText;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

/**
 *
 * @author Luis
 */
public class InterpreteDas extends Thread{
    
    DefaultHighlighter.DefaultHighlightPainter normalPainter = new DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.decode("#FFFFFF"));
    DefaultHighlighter.DefaultHighlightPainter greenPainter = new DefaultHighlighter.DefaultHighlightPainter(java.awt.Color.decode("#7CBC67"));
    Stack<Double> pilaAux;
    Double stack[]= new Double[1000];
    Double heap[]= new Double[5000];
    
    JTable tstack;
    JTable theap;
    JTable tpila;
    
    boolean debug;
    boolean detener;
    boolean debugeando;
    
    Nodo raiz;
    String archivoActual;
    
    DefaultTableModel modelPila;
    DefaultTableModel modelStack;
    DefaultTableModel modelHeap;
    
    ArrayList<Integer> lineasDeb;
    TablaFunciones funciones;
    
    JTextArea consola;
    LineasText tmpL;
    int lineatmp=-1;
    
    public InterpreteDas(Nodo raiz,ArrayList<Integer> lineasDeb,String nombArchivo){
        this.pilaAux = new Stack<>();
        this.funciones = new TablaFunciones();
        this.raiz=raiz;
        this.lineasDeb=lineasDeb;
        this.debug=true;
        this.detener=false;
        this.archivoActual= nombArchivo;
        iniStack();
    }
    
    public InterpreteDas(Nodo raiz,String nombArchivo){
        this.pilaAux = new Stack<>();
        this.funciones = new TablaFunciones();
        this.raiz=raiz;
        this.debug=false;
        this.detener=false;
        this.archivoActual= nombArchivo;
        iniStack();
    }
    
    public void iniComponentes( JTable tstack, JTable theap, JTable tpila, JTextArea consola,LineasText tmpL){
        this.tpila = tpila;
        String cabPila[]={"Indice","Valor","Caracter"};
        modelPila= new DefaultTableModel();
        modelPila.setColumnIdentifiers(cabPila);
        tpila.setModel(modelPila);
        this.tstack= tstack;
        String cabStack[]={ "Indice","Valor"};
        modelStack= new DefaultTableModel();
        modelStack.setColumnIdentifiers(cabStack);
        tstack.setModel(modelStack);
        this.theap= theap;
        modelHeap= new DefaultTableModel();
        modelHeap.setColumnIdentifiers(cabPila);
        theap.setModel(modelHeap);
        this.consola =consola;
        this.tmpL=tmpL;
        cadenas_estaticas();
    }
    
    private void iniStack(){
        stack[0]=5.0; //puntero a la primera posicion libre del stack
        stack[1]=0.0; //puntero a la primera posicion libre del heap
        stack[2]=899.0; //
        stack[3]=0.0;
        stack[4]=0.0;
    }
        
    public void ejecutar(Nodo nodo,String et,boolean salto){  
        
        boolean ejecutar=true;
        if(salto){
            ejecutar=false;
        }
        for (Nodo Hijo : nodo.Hijos) {            
            if(ejecutar){
                String linea="0";
                if (Hijo.Term != null) {
                    switch (Hijo.Term.getNombre()) {
                        case Cadena.CADENA: {
                            //<editor-fold>
                            break;
                            //</editor-fold>                            
                        }
                        case Cadena.CUADRADO: {
                            //<editor-fold>
                            break;
                            //</editor-fold>
                        }
                        case Cadena.GET_GLOBAL: {    
                            //<editor-fold>
                            String tipo=Hijo.Hijos.get(0).Token.getNombre();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            if(tipo.equals(Cadena.calc)){
                                int punt= pilaAux.pop().intValue();
                                Double val = heap[punt];
                                pilaAux.push(val);
                            }else{ //es un numero
                                int pos = Integer.parseInt( Hijo.Hijos.get(0).Token.getValor()+"");
                                Double val=heap[pos];
                                pilaAux.push(val);
                            }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.GET_LOCAL: {
                           //<editor-fold>
                            String tipo=Hijo.Hijos.get(0).Token.getNombre();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            if(tipo.equals(Cadena.calc)){
                                int punt= pilaAux.pop().intValue();
                                Double val = stack[punt];
                                pilaAux.push(val);
                            }else{ //es un numero
                                int pos = Integer.parseInt( Hijo.Hijos.get(0).Token.getValor()+"");
                                Double val=stack[pos];
                                pilaAux.push(val);
                            }
                            break;
                           //</editor-fold>
                        }
                        case Cadena.LINEA: {
                            //<editor-fold>
                            break;
                            //</editor-fold>
                        }
                        case Cadena.LLAMADA: {
                            //<editor-fold>
                            String nombre=Hijo.Hijos.get(0).Token.getValor().toString();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            Funcion fun=funciones.retornaFuncion(nombre);
                            if(fun!=null){
                                ejecutar(fun.Cuerpo,"",false);
                            }else{
                                System.out.println("DASM: No se encontro la funcion ->"+nombre);
                                notificar("ERROR DASM: No se encontro la funcion ->"+nombre+"\n");
                                return;
                            }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.OVALO: {
                            //<editor-fold>
                            break;
                            //</editor-fold>
                        }
                        case Cadena.PUNTO: {
                            //<editor-fold>
                            break;
                            //</editor-fold>
                        }
                        case Cadena.SALTO: {
                            //<editor-fold>
                            String eti=Hijo.Hijos.get(0).Token.getValor().toString();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            ejecutar(nodo, eti,true);
                            //aca va el debugs
                            if (debug) {
                                if (lineadeb(linea) || debugeando) {
                                    pintarLinea(Integer.parseInt(linea), lineatmp);
                                    lineatmp= Integer.parseInt(linea);
                                    notificar("DEBUG ALERT: Se detuvo en la linea : " + linea +"  Archivo: "+archivoActual+"\n");
                                    llenarHeap();
                                    llenarPila();
                                    llenarStack();
                                    this.suspend(); // good practice                                    
                                }
                            }
                            return;
                            //</editor-fold>
                        }
                        case Cadena.SALTO_SI: {
                            //<editor-fold>
                            String eti=Hijo.Hijos.get(0).Token.getValor().toString();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            int val = pilaAux.pop().intValue();
                            if(val==0){
                                ejecutar(nodo,eti,true);
                                if (debug) {
                                    if (lineadeb(linea) || debugeando) {
                                        pintarLinea(Integer.parseInt(linea), lineatmp);
                                        lineatmp= Integer.parseInt(linea);
                                        notificar("DEBUG ALERT: Se detuvo en la linea : " + linea+"  Archivo: "+archivoActual+"\n");
                                        llenarHeap();
                                        llenarPila();
                                        llenarStack();
                                        this.suspend(); // good practice                                    
                                    }
                                }
                                return;
                            }                            
                            break;
                            //</editor-fold>
                        }
                        case Cadena.SALTO_SI2: {
                            //<editor-fold>
                            String eti=Hijo.Hijos.get(0).Token.getValor().toString();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            int val = pilaAux.peek().intValue();
                            if(val==0){
                                ejecutar(nodo,eti,true);
                                if (debug) {
                                    if (lineadeb(linea) || debugeando) {
                                        pintarLinea(Integer.parseInt(linea), lineatmp);
                                        lineatmp= Integer.parseInt(linea);
                                        notificar("DEBUG ALERT: Se detuvo en la linea : " + linea+"  Archivo: "+archivoActual+"\n");
                                        llenarHeap();
                                        llenarPila();
                                        llenarStack();
                                        this.suspend(); // good practice                                    
                                    }
                                }
                                return;
                            }                            
                            break;
                            //</editor-fold>
                        }
                        case Cadena.SET_GLOBAL: {
                            //<editor-fold>
                            String tipo=Hijo.Hijos.get(0).Token.getNombre();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            if(tipo.equals(Cadena.calc)){
                                Double valor = pilaAux.pop();
                                int punt= pilaAux.pop().intValue();
                                heap[punt]=valor;
                            }else{ //es un numero
                                Double valor = pilaAux.pop();
                                int pos = Integer.parseInt(Hijo.Hijos.get(0).Token.getValor()+"");
                                heap[pos]=valor;
                            }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.SET_LOCAL: {
                            //<editor-fold>
                            String tipo=Hijo.Hijos.get(0).Token.getNombre();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            if(tipo.equals(Cadena.calc)){
                                Double valor = pilaAux.pop();
                                int punt= pilaAux.pop().intValue();
                                stack[punt]=valor;
                            }else{ //es un numero
                                Double valor = pilaAux.pop();
                                int pos = Integer.parseInt(Hijo.Hijos.get(0).Token.getValor()+"");
                                stack[pos]=valor;
                            }
                            break;
                            //</editor-fold>
                        }                        
                        case Cadena.TEE_GLOBAL: {
                            //<editor-fold>
                            String tipo=Hijo.Hijos.get(0).Token.getNombre();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            if(tipo.equals(Cadena.calc)){
                                Double valor = pilaAux.pop();
                                int punt= pilaAux.peek().intValue();
                                heap[punt]=valor;
                            }else{ //es un numero
                                Double valor = pilaAux.peek();
                                int pos = Integer.parseInt(Hijo.Hijos.get(0).Token.getValor()+"");
                                heap[pos]=valor;
                            }
                            break;
                            //</editor-fold>
                        }                        
                        case Cadena.TEE_LOCAL: {
                            //<editor-fold>
                            String tipo=Hijo.Hijos.get(0).Token.getNombre();
                            linea = Hijo.Hijos.get(0).Token.getLinea();
                            if(tipo.equals(Cadena.calc)){
                                Double valor = pilaAux.pop();
                                int punt= pilaAux.peek().intValue();
                                stack[punt]=valor;
                            }else{ //es un numero
                                Double valor = pilaAux.peek();
                                int pos = Integer.parseInt(Hijo.Hijos.get(0).Token.getValor()+"");
                                stack[pos]=valor;
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                } else { //es una token
                    switch (Hijo.Token.getNombre()) {
                        case Cadena.Add: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            Double res= v1+v2;
                            pilaAux.push(res);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.And: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            break;
                            //</editor-fold>
                        }
                        case Cadena.C: { //insertare un -2
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            pilaAux.push(-2.0);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.D: { //insertare un -3
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            pilaAux.push(-3.0);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Diff: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            Double res= v1-v2;
                            pilaAux.push(res);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Div: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            if(v2 == 0.0){
                                notificar("DASM: Error operacion indefinida, 0 en el divisor."+"\n");
                                return;
                            }
                            Double res= v1/v2;
                            pilaAux.push(res);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.F: { //insertare un -4
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            pilaAux.push(-4.0);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Gt: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            if(v1>v2){
                                pilaAux.push(1.0);
                            }else{
                                pilaAux.push(0.0);
                            }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Gte: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            if(v1>=v2){
                                pilaAux.push(1.0);
                            }else{
                                pilaAux.push(0.0);
                            }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Label: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Lt: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            if(v1<v2){
                                pilaAux.push(1.0);
                            }else{
                                pilaAux.push(0.0);
                            }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Lte: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            if(v1<=v2){
                                pilaAux.push(1.0);
                            }else{
                                pilaAux.push(0.0);
                            }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Mod: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            if(v2 == 0.0){
                                System.out.println("DASM: Error operacion indefinida, 0 en el modulo.");
                                notificar("ERROR DASM: Error operacion indefinida, 0 en el modulo.\n");
                                return;
                            }
                            Double res= v1%v2;
                            pilaAux.push(res);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Mul: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            Double v2= pilaAux.pop();
                            Double v1 = pilaAux.pop();
                            Double res= v1*v2;
                            pilaAux.push(res);
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Not: {
                            //<editor-fold>
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Or: {
                            //<editor-fold>
                            linea = Hijo.Token.getLinea();
                            break;
                            //</editor-fold>
                        }
                        case Cadena.Print: {
                            //<editor-fold>
                            Double tipo = pilaAux.pop();
                            Double valor = pilaAux.pop();
                            linea = Hijo.Token.getLinea();                            
                        switch (tipo.intValue()){
                            case -2:
                                char car= (char)valor.intValue(); 
                                System.out.print(car); //esto lo debo imprimir en la consola de la app 
                                notificar(car+"");
                                break;
                            case -3:
                                System.out.print(valor.intValue()); //esto lo debo imprimir en la consola de la app
                                notificar(valor.intValue()+"");
                                break;
                            case -4:
                                System.out.print(valor);  //esto lo debo imprimir en la consola de la app
                                notificar(valor+"");
                                break;
                            default:
                                System.out.println("DASM : Error, formato invalido para Print.");
                                notificar("DASM : Error, formato invalido para Print.\n");
                                return;
                        }
                            break;
                            //</editor-fold>
                        }
                        case Cadena.decimal:
                        {
                            //<editor-fold>
                            String val= Hijo.Token.getValor().toString();
                            linea = Hijo.Token.getLinea();
                            pilaAux.push(Double.parseDouble(val));
                            break;
                            //</editor-fold>
                        }
                        case Cadena.numero:{
                            //<editor-fold>
                            String val= Hijo.Token.getValor().toString();
                            linea = Hijo.Token.getLinea();
                            pilaAux.push(Double.parseDouble(val));
                            break;
                            //</editor-fold>
                        }                          
                    }
                }
                //aca va el debugs
                if (debug) {
                    if (lineadeb(linea) || debugeando) {
                        pintarLinea(Integer.parseInt(linea), lineatmp);
                        lineatmp= Integer.parseInt(linea);
                        notificar("DEBUG ALERT: Se detuvo en la linea : " + linea+"  Archivo: "+archivoActual+"\n");
                        llenarHeap();
                        llenarPila();
                        llenarStack();
                        this.suspend(); // good practice                        
                    }
                }
                //para el stop
                if (detener) {
                    return;
                }
            }else{
                if (Hijo.Token != null) {
                    if (Hijo.Token.getNombre().equals(Cadena.Label)) {
                        String etq = Hijo.Token.getValor().toString();
                        if (et.equals(etq)){
                            ejecutar=true;
                        }
                    }
                }
            }
        }        
    }
    
    public void capturarFuns(Nodo nodo){
        if (nodo != null) {
            for (Nodo Hijo : nodo.Hijos.get(0).Hijos) {
                if (Hijo.Term != null) {
                    if (Hijo.Term.getNombre().equals(Cadena.FUNCTION)) {
                        String nombre = Hijo.Hijos.get(0).Token.getValor().toString();
                        Nodo body = Hijo.Hijos.get(1);
                        if (!funciones.existeFuncion(nombre)) {
                            Funcion fun = new Funcion(nombre, body);
                            funciones.insertar(nombre, fun);
                        } else {
                            String error = "DASM: La funcion dasm -> " + nombre + " ya fue agregada";
                            System.out.println(error);
                        }
                    }
                }
            }
        }
        
    }

    public void ejecutarPrincipal(){        
        Funcion principal=funciones.retornaFuncion("principal");
        if(principal!=null){
            ejecutar(principal.Cuerpo,"",false);            
        }else{
            notificar("DASM: No se encontro nigun metodo principal"+"\n");
        }                
    }
    
    private void llenarPila(){
        int tam=modelPila.getRowCount();
        for (int i = tam - 1; i >= 0; i--) {
            modelPila.removeRow(i);
        }
        
        for (int i = 0; i < pilaAux.size(); i++) {
            String row[]={i+"",retornarVal2(pilaAux.get(i)),retornarVal(pilaAux.get(i))};
            modelPila.addRow(row);
        }
        tpila.setModel(modelPila);
    }
    
    private void llenarStack(){
        int tam=modelStack.getRowCount();
        for (int i = tam - 1; i >= 0; i--) {
            modelStack.removeRow(i);
        }
        
        /*for (int i = 0; i < stack.length; i++) {
            String row[]={i+"",retornarVal2(stack[i])};
            modelStack.addRow(row);
        }*/
        int j=0;
        Double tmp=stack[j];
        while (tmp!=null) {            
            String row[]={j+"",retornarVal2(stack[j])};
            modelStack.addRow(row);          
            j++;
            tmp=stack[j];
        }
        
        j=899;
        tmp=stack[j];
        while (tmp!=null) {            
            String row[]={j+"",retornarVal2(stack[j])};
            modelStack.addRow(row);          
            j++;
            tmp=stack[j];
        }
        tstack.setModel(modelStack);
    }
    
    private void llenarHeap(){
       int tam=modelHeap.getRowCount();
        for (int i = tam - 1; i >= 0; i--) {
            modelHeap.removeRow(i);
        }        
        /*for (int i = 0; i < heap.length; i++) {
            String row[]={i+"",retornarVal2(heap[i]),retornarVal(heap[i])};
            modelHeap.addRow(row);
        }*/
        int j=0;
        Double tmp=heap[j];
        while (tmp!=null) {            
            String row[]={j+"",retornarVal2(heap[j]),retornarVal(heap[j])};
            modelHeap.addRow(row);            
            j++;
            tmp=heap[j];
        }
        theap.setModel(modelHeap);
        
    }
    
    private String retornarVal(Double x){
        if(esEntero(x)){
            int num=x.intValue();
            if(num>-1 && num<127){
                char val = (char)num;
                return val+"";
            }else{
                return num+"";
            }            
        }else{
            return x+"";
        }
        
    }
    
    private String retornarVal2(Double x){
        if(esEntero(x)){
            int num=x.intValue();           
            return num+"";            
        }else{
            return x+"";
        }
        
    }

    private boolean esEntero(Double x){        
        Double res = x%1;
        return  res==0.0;         
    }
    
    public void continuar(){
        debugeando=true;
        this.resume();               
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
    
    private  void notificar(String caracter){
        consola.append(caracter);
    }
    
    private boolean  lineadeb(String linea){
        int line = Integer.parseInt(linea);
        for (Integer integer : lineasDeb) {
            if(line==integer)
                return true;
        }
        return false;
    }
    
    @Override
    public void run(){
        if(raiz!=null){
            capturarFuns(raiz);
            ejecutar(raiz.Hijos.get(0),"",false);//ejecuta las senteicias globales            
            ejecutarPrincipal(); //inicia la ejecucion del metodo principal
        }  
    }
    
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
    
    private void cadenas_estaticas(){
        int index_heap = 4500;
        String indice_fuera_rango = "index out of range: ";
        String cad_nula = "null string: ";
        
        for (int i = 0; i <indice_fuera_rango.length(); i++) {
            heap[index_heap]= Double.parseDouble( (int)indice_fuera_rango.charAt(i)+"");
            index_heap++;
        }
        
        heap[index_heap]=0.0;
        index_heap++;       
        
        for (int i = 0; i <cad_nula.length(); i++) {
            heap[index_heap]= Double.parseDouble( (int)indice_fuera_rango.charAt(i)+"");
            index_heap++;
        }
        
        heap[index_heap]=0.0;
        index_heap++;         
        
    }
    
}
