package DracoScript;

import Dplusplus.*;

/**
 *
 * @author esvux
 */

public class GeneradorDeCompiladores {
    
    public static void main(String[] args) {
        generarCompilador();
    }
    
    private static void generarCompilador(){
        try {
            String ruta = "src/DracoScript/"; //ruta donde tenemos los archivos con extension .jflex y .cup
            String opcFlex[] = {ruta + "lexico.jflex", "-d", ruta};
            JFlex.Main.generate(opcFlex);
            String opcCUP[] = {"-destdir", ruta, "-parser", "Par_DS", ruta + "sintactico.cup"};
            java_cup.Main.main(opcCUP);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
