/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Luis
 */
public class DibujarAST {
        
    private static int index;
        private static String cadenaDot;

        public DibujarAST() {
            index = 0;
        }

        public String generarDot(Nodo raiz) {
            cadenaDot = "digraph G {\n";
            cadenaDot+="nodo"+index+"[label=\""+raiz.toString()+"\"];\n";
            index += 1;
            recorreAST("nodo0", raiz);
            cadenaDot += "}";
            index = 0;
            return cadenaDot;
        }

        private void recorreAST(String nombreNodo,Nodo nodo) {
            nodo.Hijos.forEach((Nodo hijo) -> {
                if(hijo!=null){
                    String nomHijo = "nodo" + index;
                    cadenaDot += nomHijo + "[label=\"" + hijo.toString()+ "\"];\n";
                    cadenaDot+= nombreNodo+"->"+nomHijo+";\n";
                    index += 1;
                    recorreAST(nomHijo, hijo);
                }
            });
        }

        public void generarImg(Nodo raiz,String nombre) {
            if(raiz!=null){
                String tmp = generarDot(raiz);
                String archivo="C:\\Users\\Luis\\Desktop\\AST2\\"+nombre+".dot";
                String img="C:\\Users\\Luis\\Desktop\\AST2\\"+nombre+".png";
                String com1="dot -Tpng "+archivo+" -o "+img;
                FileWriter arch=null;
                PrintWriter  pw;
                try{
                    arch = new FileWriter(archivo);
                    pw = new PrintWriter(arch);
                    pw.println(tmp);
                }catch(Exception e){
                    System.err.println("Error al crear archivo dot: "+e);
                }finally{
                    try {
                        if (null !=arch){
                            arch.close();
                            Process p;
                            p = Runtime.getRuntime().exec(com1);
                            System.out.println("Imagen de AST creada...");
                        }       
                   } catch (Exception e2) {
                      System.err.println("Error al crear imagen ast: "+e2);
                   }
                } 
            }
            
        }

        /*private static string rendeer(String cadena) {
            cadena = cadena.Replace("\\", "\\\\");
            cadena = cadena.Replace("\"", "\\\"");
            return cadena;
        }*/

}
