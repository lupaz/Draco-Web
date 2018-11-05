/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TraductorDpp;

import Dplusplus.Cadena;

/**
 *
 * @author Luis
 */

public class Generador {
    static String enter="\n";
    static int numEtq=0;
    
    //<editor-fold desc="Constructor estructuras">
    static String inicia_var_estr(String pos,String tipo){
        String res="";
        switch(tipo){   
            case Cadena.cadena:
                res += Cadena.get_local_0 + "\n";
                res += Cadena.get_local_calc + "\n";
                res += pos + "\n";
                res += Cadena.Add+"\n";
                res += "-1\n";
                res += Cadena.set_global_calc + "\n";
                break;
            case Cadena.arreglo:
                res += Cadena.get_local_0 + "\n";
                res += Cadena.get_local_calc + "\n";
                res += pos + "\n";
                res += Cadena.Add+"\n";
                res += Cadena.get_local_1+"\n";
                res += Cadena.set_global_calc + "\n";
                break;
            default:
                res += Cadena.get_local_0 + "\n";
                res += Cadena.get_local_calc + "\n";
                res += pos + "\n";
                res += Cadena.Add+"\n";
                res += "0\n";
                res += Cadena.set_global_calc + "\n";
        }
        return res;
    }
    
    static String almacenr_punt_arreglo_estr(){
        String res="";
        res+=Cadena.get_local_0+"\n";
        res+="1\n";
        res+=Cadena.Add+"\n";
        res+=Cadena.get_local_1+"\n";
        res+=Cadena.tee_local_calc+"\n";
        //hasta este punto se almaceno la pos libre del heap donde iniciara el arreglo
        //ahora recuperamos ese valor para lamacenar el no dim y las dims
        /*res+=Cadena.get_local_0+"\n";
        res+="1\n";
        res+=Cadena.Add+"\n";*/
        res+=Cadena.get_local_calc+"\n"; 
        return  res;
    }
    
    static String setear_dim_arreglo_est(String dim){
        String res="";        
        res+=dim;
        res+=Cadena.tee_global_calc+"\n";
        res+="1\n";
        res+=Cadena.Add+"\n";
        res+=Cadena.tee_local_1+"\n";
        return res;
    }
    
    static String setear_dir_itera_arreglo_est(){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+="2"+enter;
        res+=Cadena.Add+enter;
        return res;    
    }
    
    static String recuperar_dims_arreglo_est(String pos){
        String res="";
        res+=Cadena.get_local_0+"\n";
        res+="1\n";
        res+=Cadena.Add+"\n";
        res+=Cadena.get_local_calc+"\n";
        res+=pos+"\n";
        res+=Cadena.Add+"\n";
        res+=Cadena.get_global_calc+"\n";
        return res;
    }
    
    static String multiplicar_dims_areglo_est (int cant){
        String res="";
        for (int i = 0; i < cant; i++) {
            res+=Cadena.Mult+"\n";
        }
        //res+=Cadena.Add+"\n";
        //res+=Cadena.set_local_1+ "\n";
        return res;        
    }
    
    static String iniciar_vals_arreglo_est(String tipo){
        
        String res="";
        //en teoria aca en la cima del stack esta la posicion del primer valor libre del arreglo
        res+=Cadena.set_local_calc+"\n";  //con esto almaceno el tamano en la pos 2 del ambito
        
        switch(tipo){
            case Cadena.cadena:{
                String etq_inicio = generar_etq();
                String etq_fin = generar_etq();
                res += etq_inicio + " :\n";
                res += "-1\n";
                res += Cadena.tee_global_calc + "\n";
                res += "1\n";
                res += Cadena.Add + "\n";
                res += Cadena.tee_local_1 + "\n";
                //-Aca meteremos dos veces la posicion , una para hacer la resta y la otra para almacenarla
                res += Cadena.get_local_0 + enter;
                res += "2" + enter;
                res += Cadena.Add + enter;
                res += Cadena.get_local_0 + enter;
                res += "2" + enter;
                res += Cadena.Add + enter;
                res += Cadena.get_local_calc + "\n";
                res += "1\n";
                res += Cadena.Diff + "\n";
                res += Cadena.tee_local_calc + "\n";
                res += Cadena.get_local_calc + "\n";
                res += Cadena.br_if + etq_fin + "\n";
                res += Cadena.br + etq_inicio + "\n";
                res += etq_fin + " :\n";
                res += Cadena.set_local_1 + "\n";
                break;
            }
            default:
                String etq_inicio=generar_etq();
                String etq_fin = generar_etq();
                res+= etq_inicio+" :\n";
                res+="0\n";
                res+=Cadena.tee_global_calc+"\n";
                res+="1\n";
                res+=Cadena.Add+"\n";
                res+=Cadena.tee_local_1 +"\n";
                res += Cadena.get_local_0 + enter;
                res += "2" + enter;
                res += Cadena.Add + enter;
                res += Cadena.get_local_0 + enter;
                res += "2" + enter;
                res += Cadena.Add + enter;
                res += Cadena.get_local_calc + "\n";
                res+="1\n";
                res+=Cadena.Diff+"\n";
                res += Cadena.tee_local_calc + "\n";
                res += Cadena.get_local_calc + "\n";
                res+=Cadena.br_if+etq_fin+"\n";
                res+=Cadena.br+etq_inicio+"\n";
                res+=etq_fin+" :\n";
                res+=Cadena.set_local_1+"\n"; //esto para que deje vacia la pila, despues de 
        }     
        return res;
    }
    
    static String inicar_EST(){
        return "a";
    }
    //</editor-fold>
    
    //<editor-fold desc="declaracion con asignacion global de arreglo">
    
    static String inicio_dec_arr(String pos){
        String res="";
        res+=Cadena.get_local_2+enter;
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_1+enter;
        res+=Cadena.set_local_calc+enter;
        //hasta este punto ya dejamos el valor del heap donde inicia el arreglo en la pos de la variable
        res+=Cadena.get_local_1+enter;
        return res;
    }
    
    static String setear_dim_dec_arr(String dim){
        String res="";        
        res+=dim;
        res+=Cadena.tee_global_calc+"\n";
        res+="1\n";
        res+=Cadena.Add+"\n";
        res+=Cadena.tee_local_1+"\n";
        return res;
    } //esto siempre deja en pila auz el valor donde inician los valores del arreglo
    
    static String recuperar_dims_dec_arr(String pos, String no_dim){    
        String res="";        
        res+=Cadena.get_local_2+enter; //esto servirar para rcuperar los valores de las dimensiones
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+=no_dim+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter;
        return res;
    }
    
    static String iniciar_vals_dec_arr_estr( String pos, String tipo){
        String ret="";
        ret+=Cadena.tee_local_1000+enter;
        
        ret += Cadena.Add + enter;
        ret += Cadena.set_local_1 + enter;
        ret += Cadena.get_local_2 + enter;
        ret += pos + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_calc + enter;
        ret += Cadena.tee_local_2000 + enter;
        ret += Cadena.get_global_calc + enter;
        ret += Cadena.get_local_2000 + enter;
        ret += Cadena.Add + enter;
        ret += "1" + enter;
        ret += Cadena.Add + enter;

        String et_ini = generar_etq();
        String et_fin = generar_etq();

        ret += et_ini + " :\n";
        ret += Cadena.Call + Cadena.inicio_ + tipo + enter;
        ret += Cadena.get_local_0 + enter;
        ret += "0" + enter;//la pos 0 es donde se alamcena el valor de incio de la estructura
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_calc + enter; //recupero el valor (la posicion inical de la estructura)
        ret += Cadena.tee_global_calc + enter;
        ret += "1" + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_1000 + "\n";
        ret += "1\n";
        ret += Cadena.Diff + "\n";
        ret+=Cadena.tee_local_1000+enter;
        ret += Cadena.br_if + et_fin + "\n";
        ret += Cadena.br + et_ini + "\n";
        ret += et_fin + " :\n";
        ret+= "0"+enter; //esto para que deje vacia la pila, despues de
        ret+=Cadena.Mult+enter;
        ret+=Cadena.tee_local_1000+enter;
        ret += Cadena.set_local_2000+enter;  
        return ret;
    }
    
    static String iniciar_vals_dec_arr_var( String pos, String tipo){
        String ret = "";
        ret += Cadena.tee_local_1000 + enter;

        ret += Cadena.Add + enter;
        ret += Cadena.set_local_1 + enter; //reservamos la memoria directamente
        //ahora tenemos que ubicarnos de nuevo en la primera pos libre del arreglo
        ret += Cadena.get_local_2 + enter;
        ret += pos + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_calc + enter;
        ret += Cadena.tee_local_2000 + enter; //almaceno el incio del arreglo
        ret += Cadena.get_global_calc + enter;//obtengo el numero de dims
        ret += Cadena.get_local_2000 + enter; // obtengo la pos inicial nuevamente
        ret += Cadena.Add + enter; //le sumo el numero de dims
        ret += "1" + enter; //
        ret += Cadena.Add + enter; //sumamos uno , esto ya nos deja en la 1ra pos libre del arreglo 

        String et_ini = generar_etq();
        String et_fin = generar_etq();

        ret += et_ini + " :\n";
        //----------------------------
        if(tipo.equals(Cadena.cadena)){
            ret+= "-1"+enter;
        }else{
            ret+= "0"+enter;
        }
        //-----------------------------
        ret += Cadena.tee_global_calc + enter;
        ret += "1" + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_1000 + "\n";
        ret += "1\n";
        ret += Cadena.Diff + "\n";
        ret += Cadena.tee_local_1000 + enter;
        ret += Cadena.br_if + et_fin + "\n";
        ret += Cadena.br + et_ini + "\n";
        ret += et_fin + " :\n";
        ret += "0" + enter; //esto para que deje vacia la pila, despues de
        ret += Cadena.Mult + enter;
        ret += Cadena.tee_local_1000 + enter;
        ret += Cadena.set_local_2000 + enter;
        return ret;
    }
    
    //la parte de asignacion:
    
    static String recuperar_pos_ini_arr(String pos){        
        String res="";
        
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_1+enter;
        
        //inicia la recuperación
        res+=Cadena.get_local_2+enter;
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+=Cadena.get_global_calc+enter; //con esto obtengo el numero de dims
        //recuepero de nuevo el puntero y
        res+=Cadena.get_local_2+enter;
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+= Cadena.Add+enter; //le sumamos el numero de dim a la pos
        res+="1"+enter;
        res+=Cadena.Add+enter;   //con esto ya dejo la dirccion de memoria de la primera pos libre del arreglo      
        return res;
    }
    
    static  String asignar_val_arr(String val){
        String res="";        
        res+=val;
        res+= Cadena.tee_global_calc+enter;
        res+= "1"+enter;
        res+= Cadena.Add+enter;        
        return  res;
    }
    
    static String finalizar_asignacion_arr(){
        String res="";
        res+=Cadena.set_local_1+enter;
        return  res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="declaracion con asignacion local de arreglo">
    
    static String inicio_dec_arr_loc(String pos){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_1+enter;
        res+=Cadena.set_local_calc+enter;
        //hasta este punto ya dejamos el valor del heap donde inicia el arreglo en la pos de la variable
        res+=Cadena.get_local_1+enter;
        return res;
    }
    
    static String setear_dim_dec_arr_loc(String dim){
        String res="";        
        res+=dim;
        res+=Cadena.tee_global_calc+"\n";
        res+="1\n";
        res+=Cadena.Add+"\n";
        res+=Cadena.tee_local_1+"\n";
        return res;
    } //esto siempre deja en pila auz el valor donde inician los valores del arreglo
    
    static String recuperar_dims_dec_arr_loc(String pos, String no_dim){    
        String res="";        
        res+=Cadena.get_local_0+enter; //esto servirar para rcuperar los valores de las dimensiones
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+=no_dim+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter;
        return res;
    }
    
    static String iniciar_vals_dec_arr_estr_loc( String pos, String tipo,String ambito){
        String ret = "";
        ret += Cadena.tee_local_1000 + enter;

        ret += Cadena.Add + enter;
        ret += Cadena.set_local_1 + enter;
        ret += Cadena.get_local_0 + enter;
        ret += pos + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_calc + enter;
        ret += Cadena.tee_local_2000 + enter;
        ret += Cadena.get_global_calc + enter;
        ret += Cadena.get_local_2000 + enter;
        ret += Cadena.Add + enter;
        ret += "1" + enter;
        ret += Cadena.Add + enter;

        String et_ini = generar_etq();
        String et_fin = generar_etq();

        ret += et_ini + " :\n";
        //hacemos el cambio de ambito
        ret += Cadena.get_local_0 + enter;
        ret += ambito + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.set_local_0;
        //----------------------------
        ret += Cadena.Call + Cadena.inicio_ + tipo + enter;
        ret += Cadena.get_local_0 + enter;
        ret += "0" + enter;//la pos 0 es donde se alamcena el valor de incio de la estructura
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_calc + enter; //recupero el valor (la posicion inical de la estructura)
        // regresamos el ambito
        ret += Cadena.get_local_0 + enter;
        ret += ambito + enter;
        ret += Cadena.Diff + enter;
        ret += Cadena.set_local_0 + enter;
        //-----------------------------
        ret += Cadena.tee_global_calc + enter;
        ret += "1" + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_1000 + "\n";
        ret += "1\n";
        ret += Cadena.Diff + "\n";
        ret += Cadena.tee_local_1000 + enter;
        ret += Cadena.br_if + et_fin + "\n";
        ret += Cadena.br + et_ini + "\n";
        ret += et_fin + " :\n";
        ret += "0" + enter; //esto para que deje vacia la pila, despues de
        ret += Cadena.Mult + enter;
        ret += Cadena.tee_local_1000 + enter;
        ret += Cadena.set_local_2000 + enter;
        return ret;
    }
    
    //se multiplican
    
    static String iniciar_vals_dec_arr_var_loc( String pos, String tipo){
        String ret = "";
        ret += Cadena.tee_local_1000 + enter;

        ret += Cadena.Add + enter;
        ret += Cadena.set_local_1 + enter; //reservamos la memoria directamente
        //ahora tenemos que ubicarnos de nuevo en la primera pos libre del arreglo
        ret += Cadena.get_local_0 + enter;
        ret += pos + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_calc + enter;
        ret += Cadena.tee_local_2000 + enter; //almaceno el incio del arreglo
        ret += Cadena.get_global_calc + enter;//obtengo el numero de dims
        ret += Cadena.get_local_2000 + enter; // obtengo la pos inicial nuevamente
        ret += Cadena.Add + enter; //le sumo el numero de dims
        ret += "1" + enter; //
        ret += Cadena.Add + enter; //sumamos uno , esto ya nos deja en la 1ra pos libre del arreglo 

        String et_ini = generar_etq();
        String et_fin = generar_etq();

        ret += et_ini + " :\n";
        //----------------------------
        if(tipo.equals(Cadena.cadena)){
            ret+= "-1"+enter;
        }else{
            ret+= "0"+enter;
        }
        //-----------------------------
        ret += Cadena.tee_global_calc + enter;
        ret += "1" + enter;
        ret += Cadena.Add + enter;
        ret += Cadena.get_local_1000 + "\n";
        ret += "1\n";
        ret += Cadena.Diff + "\n";
        ret += Cadena.tee_local_1000 + enter;
        ret += Cadena.br_if + et_fin + "\n";
        ret += Cadena.br + et_ini + "\n";
        ret += et_fin + " :\n";
        ret += "0" + enter; //esto para que deje vacia la pila, despues de
        ret += Cadena.Mult + enter;
        ret += Cadena.tee_local_1000 + enter;
        ret += Cadena.set_local_2000 + enter;
        return ret;
    }
    
    //la parte de asignacion:
    
    static String recuperar_pos_ini_arr_loc(String pos){        
        String res="";
        
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_1+enter;
        
        //inicia la recuperación
        res+=Cadena.get_local_0+enter;
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+=Cadena.get_global_calc+enter; //con esto obtengo el numero de dims
        //recuepero de nuevo el puntero y
        res+=Cadena.get_local_0+enter;
        res+=pos+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+= Cadena.Add+enter; //le sumamos el numero de dim a la pos
        res+="1"+enter;
        res+=Cadena.Add+enter;   //con esto ya dejo la dirccion de memoria de la primera pos libre del arreglo      
        return res;
    }
    
    static  String asignar_val_arr_loc(String val){
        String res="";        
        res+=val;
        res+= Cadena.tee_global_calc+enter;
        res+= "1"+enter;
        res+= Cadena.Add+enter;        
        return  res;
    }
    
    static String finalizar_asignacion_arr_loc(){
        String res="";
        res+=Cadena.set_local_1+enter;
        return  res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="declaracion y asignacion global y local de variables primitivas">
    static String declara_asigna_var_global(String pos, String val){        
        String res="";
        res += Cadena.get_local_2 + enter;
        res += pos + enter;
        res += Cadena.Add + enter;
        res += val ;
        res += Cadena.set_local_calc + enter;
        return res;        
    }
    
    static String declara_asigna_var_local(String pos, String val){        
        String res="";
        res += Cadena.get_local_0 + enter;
        res += pos + enter;
        res += Cadena.Add + enter;
        res += val ;
        res += Cadena.set_local_calc + enter;
        return res;        
    }
    
    static String declara_var_global(String pos, String tipo){    
        String res="";
        switch(tipo){        
            case Cadena.cadena:
                res+=Cadena.get_local_2+enter;
                res+=pos+enter;
                res+=Cadena.Add+enter;
                res+="-1"+enter;
                res+=Cadena.set_local_calc+enter;
                break;
            default:
                res+=Cadena.get_local_2+enter;
                res+=pos+enter;
                res+=Cadena.Add+enter;
                res+="0"+enter;
                res+=Cadena.set_local_calc+enter;
        }
        return res;
    } 
    
   static String declara_var_local(String pos, String tipo){    
        String res="";
        switch(tipo){        
            case Cadena.cadena:
                res+=Cadena.get_local_0+enter;
                res+=pos+enter;
                res+=Cadena.Add+enter;
                res+="-1"+enter;
                res+=Cadena.set_local_calc+enter;
                break;
            default:
                res+=Cadena.get_local_0+enter;
                res+=pos+enter;
                res+=Cadena.Add+enter;
                res+="0"+enter;
                res+=Cadena.set_local_calc+enter;
        }
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Declaracion y Asigacion de estructuras locales y globales">
    static String declara_struct_global(String pos , String tipo){
        String res="";
        res += Cadena.get_local_2 + enter;
        res += pos + enter;
        res += Cadena.Add + enter;
        res += Cadena.Call+Cadena.inicio_+tipo+enter; //no hay cambio de ambito por que es global
        res += Cadena.get_local_0 + enter;
        res += "0" + enter;//la pos 0 es donde se alamcena el valor de incio de la estructura
        res += Cadena.Add + enter;
        res += Cadena.get_local_calc + enter; //recupero el valor (la posicion inical de la estructura)
        res += Cadena.set_local_calc+enter;
        return res;
    }
     
    static String declara_struct_local(String pos , String tipo,String ambito){
        String res="";
        res += Cadena.get_local_0 + enter;
        res += pos + enter;
        res += Cadena.Add + enter;
        //hacemos el cambio de ambito
        res+= Cadena.get_local_0+enter;
        res+= ambito+enter;
        res+= Cadena.Add+enter;
        res+= Cadena.set_local_0+enter;
        res += Cadena.Call+Cadena.inicio_+tipo+enter; //si hay cambio de ambito por que es local
        res += Cadena.get_local_0 + enter;
        res += "0" + enter;//la pos 0 es donde se alamcena el valor de incio de la estructura
        res += Cadena.Add + enter;
        res += Cadena.get_local_calc + enter; //recupero el valor (la posicion inical de la estructura)
        //regresamos el ambito anterior
        res+= Cadena.get_local_0+enter;
        res+= ambito+enter;
        res+= Cadena.Diff+enter;
        res+= Cadena.set_local_0+enter;
        //por ultimo asignamos
        res += Cadena.set_local_calc+enter;
        return res;
    }
     
    //</editor-fold>
    
    //<editor-fold desc="acceso a arreglos locales">
    
    static String comprobarIndice_local(String pos_atrib, String pos_arr ,String pos_ambit,String indice, String et_error){
        String res="";        
        res+=indice; //colocamos el valor del indice en la pila
        res+="-1"+enter;
        res+=Cadena.Gt+enter;
        res+=Cadena.br_if+et_error+enter;
        res+=indice;
        //ahora vamos a recuperar el valor de la dimension para la segunda comprobación
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=pos_atrib+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter;
        //luego de recuperar el valor de la dim comprovamos
        res+=Cadena.Lt+enter;
        res+=Cadena.br_if+et_error+enter; 
        return res;
    }
    
    static String linealizar_arreglo_1dim(String indice){
        String res="";
        res+=indice;        
        return res;
    }
    
    static String linealizar_arreglo_Ndim_local(String indice,String no_dim,String pos_arr ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=no_dim+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //recuperamos el valor de la dim
        res+=Cadena.Mult+enter;
        res+=indice;
        res+=Cadena.Add+enter;        
        return res;
    }
    
    static String recuperar_val_Arreglo_local(String no_dims,String pos_arr ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        return res;
    }
    
    static String recuperar_dir_Arreglo_local(String no_dims,String pos_arr ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        return res;
    }
    
    static String recuperar_val_est_Arreglo_local(String no_dims,String pos_arr ,String pos_ambit, String pos_est){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        //luego de obtener el valor, procedo a obtener el atributo del struct
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        return res;
    }
    
    static String recuperar_dir_est_Arreglo_local(String no_dims,String pos_arr ,String pos_ambit, String pos_est){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        //luego de obtener el valor, procedo a obtener el atributo del struct
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        return res;
    }
    
    //</editor-fold>
      
    //<editor-fold desc="acceso a arreglos gobales">
        
    static String comprobarIndice_global(String pos_atrib, String pos_arr ,String indice, String et_error){
        String res="";        
        res+=indice; //colocamos el valor del indice en la pila
        res+="-1"+enter;
        res+=Cadena.Gt+enter;
        res+=Cadena.br_if+et_error+enter;
        res+=indice;
        //ahora vamos a recuperar el valor de la dimension para la segunda comprobación
        res+=Cadena.get_local_2+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=pos_atrib+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter;
        //luego de recuperar el valor de la dim comprovamos
        res+=Cadena.Lt+enter;
        res+=Cadena.br_if+et_error+enter; 
        return res;
    }
    
    static String linealizar_arreglo_Ndim_global(String indice,String no_dim,String pos_arr){
        String res="";
        res+=Cadena.get_local_2+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=no_dim+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //recuperamos el valor de la dim
        res+=Cadena.Mult+enter;
        res+=indice;
        res+=Cadena.Add+enter;        
        return res;
    }
    
    static String recuperar_val_Arreglo_global(String no_dims,String pos_arr){
        String res="";
        res+=Cadena.get_local_2+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        return res;
    }
    
    static String recuperar_dir_Arreglo_global(String no_dims,String pos_arr){
        String res="";
        res+=Cadena.get_local_2+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        return res;
    }
    
    static String recuperar_val_est_Arreglo_global(String no_dims,String pos_arr,String pos_est){
        String res="";
        res+=Cadena.get_local_2+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        //luego de obtener el valor, procedo a obtener el atributo del struct
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        return res;
    }
    
    static String recuperar_dir_est_Arreglo_global(String no_dims,String pos_arr,String pos_est){
        String res="";
        res+=Cadena.get_local_2+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        //luego de obtener el valor, procedo a obtener el atributo del struct
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="acceso a arreglos de estructuras locales">
    
    static String comprobarIndice_est_local(String pos_est,String pos_atrib, String pos_arr ,String pos_ambit,String indice, String et_error){
        String res="";        
        res+=indice; //colocamos el valor del indice en la pila
        res+="-1"+enter;
        res+=Cadena.Gt+enter;
        res+=Cadena.br_if+et_error+enter;
        res+=indice;
        //ahora vamos a recuperar el valor de la dimension para la segunda comprobación
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=pos_atrib+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter;
        //luego de recuperar el valor de la dim comprovamos
        res+=Cadena.Lt+enter;
        res+=Cadena.br_if+et_error+enter; 
        return res;
    }
    
    static String linealizar_arreglo_est_Ndim_local(String indice,String no_dim,String pos_est,String pos_arr ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=no_dim+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //recuperamos el valor de la dim
        res+=Cadena.Mult+enter;
        res+=indice;
        res+=Cadena.Add+enter;        
        return res;
    }
    
    static String recuperar_val_Arreglo_est_local(String no_dims,String pos_est,String pos_arr ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        return res;
    }
    
    static String recuperar_dir_Arreglo_est_local(String no_dims,String pos_est,String pos_arr ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="acceso a arreglos de estrcuturas gobales">
        
    static String comprobarIndice_est_global(String pos_atrib,String pos_est, String pos_arr ,String indice, String et_error){
        String res="";        
        res+=indice; //colocamos el valor del indice en la pila
        res+="-1"+enter;
        res+=Cadena.Gt+enter;
        res+=Cadena.br_if+et_error+enter;
        res+=indice;
        //ahora vamos a recuperar el valor de la dimension para la segunda comprobación
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=pos_atrib+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter;
        //luego de recuperar el valor de la dim comprovamos
        res+=Cadena.Lt+enter;
        res+=Cadena.br_if+et_error+enter; 
        return res;
    }
    
    static String linealizar_arreglo_est_Ndim_global(String indice,String no_dim,String pos_est,String pos_arr){
        String res="";
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+=no_dim+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //recuperamos el valor de la dim
        res+=Cadena.Mult+enter;
        res+=indice;
        res+=Cadena.Add+enter;        
        return res;
    }
    
    static String recuperar_val_Arreglo_est_global(String no_dims,String pos_est,String pos_arr){
        String res="";
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc; //esto obtiene el valor y lo deja en pila
        return res;
    }
    
    static String recuperar_dir_Arreglo_est_global(String no_dims,String pos_est,String pos_arr){
        String res="";
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="acceso a arreglo de arreglo de estructuras loc_gob">
    
    static String comprobarIndice_Arr_arr_loc_gob(String pos_atrib,String indice, String et_error){
        String res="";        
        res+=indice; //colocamos el valor del indice en la pila
        res+="-1"+enter;
        res+=Cadena.Gt+enter;
        res+=Cadena.br_if+et_error+enter;
        res+=indice;
        //ahora vamos a recuperar el valor de la dimension para la segunda comprobación
        res+=Cadena.get_local_1000+enter; //ya tenemos la posicion incial del arr en local 100
        res+=pos_atrib+enter; //sumanmos 
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter;
        //luego de recuperar el valor de la dim comprovamos
        res+=Cadena.Lt+enter;
        res+=Cadena.br_if+et_error+enter; 
        return res;
    }

    static String linealizar_Arr_arr_Ndim_loc_gob(String indice,String no_dim){
        String res="";
        res+=Cadena.get_local_1000+enter;       
        res+=no_dim+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //recuperamos el valor de la dim
        res+=Cadena.Mult+enter;
        res+=indice;
        res+=Cadena.Add+enter;        
        return res;
    }
    
    static String recuperar_val_Arr_arr_loc_gob(String no_dims){
        String res="";
        res+=Cadena.get_local_1000+enter;
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        return res;
    }
    
    static String recuperar_dir_Arr_arr_loc_gob(String no_dims){
        String res="";
        res+=Cadena.get_local_1000+enter;
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        return res;
    }
    
    //esto es parte del acceso al arreglo inicial
    static String recuperar_val_est_Arr_arr_local(String no_dims,String pos_arr ,String pos_ambit, String pos_est){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        //luego de obtener el valor, procedo a obtener el atributo del struct
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        res+=Cadena.set_local_1000+enter;
        return res;
    }
    
    static String recuperar_val_est_Arr_arr_global(String no_dims,String pos_arr,String pos_est){
        String res="";
        res+=Cadena.get_local_2+enter;
        res+=pos_arr+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter; //con esto optengo la pos del heap dond inicia el arreglo
        res+= no_dims+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.Add+enter; //esto suma la linealizacion que quedo en pila
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        //luego de obtener el valor, procedo a obtener el atributo del struct
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //esto obtiene el valor y lo deja en pila
        res+=Cadena.set_local_1000+enter;
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Acceso a atributo de estrcutura local">
    static String recuperar_val_est_local(String pos_est,String pos_var ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_var+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap donde esta la var  
        return res;
    }
    
    static String recuperar_dir_est_local(String pos_est,String pos_var ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_var+enter;
        res+=Cadena.Add+enter;  
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Acceso a atributo de estrcutura global">
    static String recuperar_val_est_global(String pos_est,String pos_var){
        String res="";
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_var+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_global_calc+enter; //con esto optengo la pos del heap donde esta la var
        return res;
    }
    
    static String recuperar_dir_est_global(String pos_est,String pos_var){
        String res="";
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_est+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        //--------------------
        res+=pos_var+enter;
        res+=Cadena.Add+enter;
        return res;
    }
    //</editor-fold>
    
    //<editor-fold desc="Acceso a variable local">
    static String recuperar_val_var_local(String pos_var ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_var+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        return res;
    }
    
    static String recuperar_dir_var_local(String pos_var ,String pos_ambit){
        String res="";
        res+=Cadena.get_local_0+enter;
        res+=pos_ambit+enter;
        res+=Cadena.Diff+enter;
        //----parte estrcutura
        res+=pos_var+enter;
        res+=Cadena.Add+enter;
        return res;
    }    
    //</editor-fold>
    
    //<editor-fold desc="Acceso a varaible global">
    static String recuperar_val_var_global(String pos_var){
        String res="";
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_var+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        return res;
    }
    
    static String recuperar_dir_var_global(String pos_var){
        String res="";
        res+=Cadena.get_local_2+enter;
        //----parte estrcutura
        res+=pos_var+enter;
        res+=Cadena.Add+enter;
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Asignacion de variables loc_glob">
    static String asignar_var_glob_loc_stack(String valor){
        String res="";        
        res+=valor;
        res+=Cadena.set_local_calc+enter;        
        return res;
    }
    
    static String asignar_var_glob_loc_heap(String val){
        String res="";        
        res+=val;
        res+=Cadena.set_global_calc+enter;        
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="defincion de una cadena"> 
     
    static String inicio_cadena(){
        String res= "";
        res+= Cadena.get_local_1+enter;
        res+=Cadena.get_local_1+enter;
        return res;
    }
    
    static String insertar_caracter(String car){
        String res= "";
        res+= car+enter;
        res+=Cadena.tee_global_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.tee_local_1+enter;
        return res;
    }
    
    static String fin_cadena(){
        String res= "";
        res+="0"+enter;
        res+=Cadena.tee_global_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_1+enter;        
        return res;
    }
    
    /// para las cadenas por defecto    
        
    //</editor-fold>
    
    //<editor-fold desc="declaracion y llamada funcion printer">
    
    static String funcion_print(){
        String res="";
        
        String et_sig1=generar_etq();
        String et_sig2=generar_etq();
        String et_end= generar_etq();
        String et_num = generar_etq();
        String et_dec = generar_etq();
        String et_cad = generar_etq();
        String ini_cad= generar_etq();
        String fin_cad= generar_etq();
        String et_null= generar_etq();
        
        res+=Cadena.Function+"$printer"+enter;
        res+=Cadena.get_local_0+enter;
        res+="1"+enter;
        res+= Cadena.Add+enter;
        res+= Cadena.get_local_calc+enter; //con esto recupero el tipo de impresion;
        // Enviare de parametros, -2 para cadena, -3 para entero, -4 para decimal
        res+="2"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.br_if+et_cad+enter;
        res+=Cadena.br + et_sig1+enter;
        res+=et_cad+" :"+enter;
        //aca incio el ciclo de impresion
        res+=Cadena.get_local_0+enter;
        res+=Cadena.get_local_calc+enter;
        res+=Cadena.tee_local_1000+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.br_if+et_null+enter;
        //----ciclo--
        res+=ini_cad+" :"+enter;
        res+=Cadena.get_local_1000+enter;
        res+=Cadena.get_global_calc+enter;
        res+=Cadena._C+enter;
        res+=Cadena.Print+enter;
        res+=Cadena.get_local_1000+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.tee_local_1000+enter;
        res+=Cadena.get_global_calc+enter;
        res+=Cadena.br_if+fin_cad+enter;
        res+=Cadena.br +ini_cad+enter;
        res+=fin_cad + " :"+enter;        
        res+="10"+enter;
        res+=Cadena._C+enter;
        res+=Cadena.Print+enter;
        //--------------------------
        res+=Cadena.br+et_end+enter;
        res+=et_null+" :"+enter;       
        res+="110"+enter; //n
        res+=Cadena._C+enter;
        res+=Cadena.Print+enter;        
        res+="117"+enter; //u
        res+=Cadena._C+enter;
        res+=Cadena.Print+enter;        
        res+="108"+enter; //l
        res+=Cadena._C+enter;
        res+=Cadena.Print+enter;        
        res+="108"+enter; //l
        res+=Cadena._C+enter;
        res+=Cadena.Print+enter;        
        res+="10"+enter;
        res+=Cadena._C+enter;
        res+=Cadena.Print+enter;
        //------------------------
        res+=Cadena.br+et_end+enter;
        res+=et_sig1+" :"+enter;
        //si no es cadena comprobamos si es entero
        res+=Cadena.get_local_0+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+="3"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.br_if +et_num+enter;
        res+=Cadena.br + et_sig2+enter;
        res+= et_num+" :"+enter;        
        res+=Cadena.get_local_0+enter;
        res+=Cadena.get_local_calc+enter;
        res+=Cadena._D+enter;
        res+=Cadena.Print+enter;
        res+=Cadena.br+et_end+enter;
        res+=et_sig2+" :"+enter;        
        res+=Cadena.get_local_0+enter;
        res+=Cadena.get_local_calc+enter;
        res+= Cadena._F+enter;
        res+=Cadena.Print+enter;
        res+=et_end+" :"+enter; 
        res+=Cadena.End+enter;
        return  res;
    }
    
    static  String llamada_print(String tam_ambito,String cod_exp,String tipo){
        String res=""; //tipo puede ser -2 para caracter, -3 para entero y -4 para decimal
        //paso del primer parametro
        res+= Cadena.get_local_0+enter;
        res+=tam_ambito+enter;
        res+=Cadena.Add+enter;
        res+=cod_exp+enter;
        res+=Cadena.set_local_calc+enter;
        //paso del segundo parametro
        res+=Cadena.get_local_0+enter;
        res+=tam_ambito+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=tipo+enter;
        res+=Cadena.set_local_calc+enter;
        res+=Cadena.get_local_0+enter;
        res+=tam_ambito+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_0+enter;
        res+=Cadena.Call +"$printer"+enter;
        res+=Cadena.get_local_0+enter;
        res+=tam_ambito+enter;
        res+=Cadena.Diff+enter;
        res+=Cadena.set_local_0+enter;
        
        return res;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="declaracion y llamada funcion concat">
    static String funcion_concat(){
        String res="";
        
        String et_err=generar_etq();
        String et_cad1= generar_etq();
        String et_segunda=generar_etq();
        String et_cad2= generar_etq();
        String et_final= generar_etq();
        String et_end  =generar_etq();
        
        res+= Cadena.Function + "$concat"+enter;
        
        res+=Cadena.get_local_0+enter;
        res+=Cadena.get_local_1+enter;
        res+=Cadena.set_local_calc+enter; //en este punto dejamos el incio de la nueva cadena en la pos 0 de la fun
        
        res+=Cadena.get_local_0 +enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.br_if +et_err+enter;
        res+=Cadena.get_local_0+enter;
        res+="2"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+= "1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.br_if + et_err+enter;
        res+=et_cad1+" :"+enter;
        res+=Cadena.get_local_1+enter;
        res+=Cadena.get_local_0+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+=Cadena.get_global_calc+enter;
        res+=Cadena.br__if+et_segunda+enter;
        //seteamos el valor en la nueva posicion
        res+=Cadena.tee_global_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_1+enter;
        res+=Cadena.get_local_0+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_0+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;   
        res+=Cadena.get_local_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_calc+enter;
        res+=Cadena.br +et_cad1+enter;
        // seguimos con la segunda cad
        res+=et_segunda+" :"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_1+enter;
        //con lo anterior en terio limpiamos la pila 
        res+=et_cad2+" :"+enter;
        res+=Cadena.get_local_1+enter;
        res+=Cadena.get_local_0+enter;
        res+="2"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_calc+enter;
        res+=Cadena.get_global_calc+enter;
        res+=Cadena.br__if+et_final+enter;
        //seteamos el valor en la nueva posicion
        res+=Cadena.tee_global_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_1+enter;
        res+=Cadena.get_local_0+enter;
        res+="2"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.get_local_0+enter;
        res+="2"+enter;
        res+=Cadena.Add+enter;   
        res+=Cadena.get_local_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_calc+enter;
        res+=Cadena.br +et_cad2+enter;
        res+=et_final+" :"+enter;
        res+=Cadena.tee_global_calc+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_1+enter;
        res+=Cadena.br+et_end+enter;
        //si una de las cadenas es nula, retornamos nulo tambien
        res+=et_err+" :"+enter;
        res+=Cadena.get_local_0+enter;
        res+="-1"+enter;
        res+=Cadena.set_local_calc+enter;
        res+=et_end+" :"+enter;
        res+=Cadena.End+enter;
        return res;    
    }
    
    static String llamada_concat(String ambito_actul, String cod_exp1,String cod_exp2){    
        String res="";
        //pasamos el primer parametro
        res+=Cadena.get_local_0+enter;
        res+=ambito_actul+enter;
        res+=Cadena.Add+enter;
        res+="1"+enter;
        res+=Cadena.Add+enter;
        res+=cod_exp1;
        res+=Cadena.set_local_calc+enter;
        //pasamos el segundo parametro;
        res+=Cadena.get_local_0+enter;
        res+=ambito_actul+enter;
        res+=Cadena.Add+enter;
        res+="2"+enter;
        res+=Cadena.Add+enter;
        res+=cod_exp2;
        res+=Cadena.set_local_calc+enter;
        //hacemos el cambio de ambito y la llamda
        res+=Cadena.get_local_0+enter;
        res+=ambito_actul+enter;
        res+=Cadena.Add+enter;
        res+=Cadena.set_local_0+enter;
        res+=Cadena.Call+"$concat"+enter;
        //vamos a dejar el puntero de la nueva cadena en la pila
        res+=Cadena.get_local_0+enter;
        res+=Cadena.get_local_calc+enter;
        //regresamos el ambito
        res+=Cadena.get_local_0+enter;
        res+=ambito_actul+enter;
        res+=Cadena.Diff+enter;
        res+=Cadena.set_local_0+enter;
        return  res;
    }
    //</editor-fold>
    
    static String generar_etq(){
        return "L"+numEtq++;
    }
    
    static String aumentar_heap(String cantidad){
        return "get_local 1\n"+
                cantidad+"\n"+
                "add\n"+
                "set_local 1\n";
    }
    
}
