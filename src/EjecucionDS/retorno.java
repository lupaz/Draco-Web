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
public class retorno {
     public Object valor;
        public String tipo;
        public Boolean retorna;
        public Boolean detener;
        public Boolean continua;
        public String Linea;
        public String Columna;
        public retorno(Object valor, String tipo, String linea, String columna)
        {
            this.valor = valor;
            this.tipo = tipo;
            this.Linea = linea;
            this.Columna = columna;
            this.detener = false;
            this.retorna = false;
            this.continua = false;
        }
}
