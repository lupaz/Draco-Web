/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dplusplus;


/**
 *
 * @author Luis
 */
public class Cadena {
    //======================No terminales====> 
    public static final String 
    
    INICIO=         "INICIO",
            IMPORT =            "IMPORT",
            CUERPO=             "CUERPO",
            CUERPO2=            "CUERPO2",
            DA_VAR=             "DA_VAR",
            AS_VAR=             "AS_VAR",
            DA_ARR=             "DA_ARR",        
            AS_ARR=             "AS_ARR",
            AS_ARR2=             "AS_ARR2",
            AS_ARR3=             "AS_ARR3",
            DEC_EST=            "DEC_EST",
            ACC_EST =           "ACC_EST",
            ACC_MAT =           "ACC_MAT",
            ACC_MAT2 =           "ACC_MAT2",
            ACC_MAT3 =           "ACC_MAT3",
            ACC_MAT_EST =       "ACC_MAT_EST",
            AS_VAR_EST =        "AS_VAR_EST",
            AS_ARR_EST =        "AS_ARR_EST",
            STRUCT =            "STRUCT",
            DEC_MET=            "DEC_MET",
            DEC_FUN=            "DEC_FUN",
            PRINCIPAL =         "PRINCIPAL",
            RES =               "RES",
            RES2 =              "RES2",
            L_AS =              "L_AS",
            DIM =               "DIM",
            L_DEC =             "L_DEC",
            L_IDS =             "L_IDS",
            L_EXP =             "L_EXP",
            L_PAR =             "L_PAR",
            PAR   =             "PAR",
            DAS =               "DAS",
            RET =               "RET",
            
            LOG =               "LOG",
            REL =               "REL",
            ARIT =              "ARIT",
            
            
    SI =            "SI",
    SIN =            "SIN",
            SINO =              "SINO",
    SELECT =        "SELECT",
    PARA =          "PARA",
    MIENTRAS =      "MIENTRAS",
    DETENER =       "DETENER",
            CONTINUAR =         "CONTINUAR",
            OP =                "OP",
    IMPRIMIR =      "IMPRIMIR",
    RETORNAR =      "RETORNAR",
    
    LLAMADA =       "LLAMADA",
    
            PUNTO =             "PUNTO",
            CUADRADO =          "CUADRADO",
            OVALO =             "OVALO",
            CADENA =            "CADENA",
            LINEA =             "LINEA" ;
    
    ///////////////////////////Terminales ------------------------------------->

    public static final String 
    
    id =            "id",           
    mas  =          "mas",
    menos =         "menos",
    por =           "por",
    div =           "div",
    modulo =        "modulo",
    pot   =         "pot",        
    inc =           "inc",
    dec =           "dec",
            cad =           "cad",
            enter =         "enter",        
            bool =          "bool",
            decim =          "decim",
            caract =          "caract",
            
            cadena =        "cadena",
            entero =        "entero",
            decimal =        "decimal",
            booleano =        "booleano",
            caracter =        "caracter",
            nulo =            "nulo",

    trus =          "verdadero",
    fals =          "falso", 

        
    mayor  =        "mayor",
    menor  =        "menor",
    mayor_i  =      "mayor_i",        
    menor_i  =      "menor_i", 
    diferente  =    "diferente",
    igual  =        "igual",
    
    or =            "Or",
    and =           "And",
    not =           "Not",      
    error =         "error",
    var =           "var",
    vec =           "vec",
            
            
    pos =           "_pos_" ,
    fun =           "_func_",
            chtml = "chtml",
            documet ="documento",
            listo   = "listo",
            modificado = "modificado",
            clic = "click",
    ambito_g =      "Ambito_global",
    ambito_fun=     "Ambito_funcion",
    ambito_if =     "Ambito_if",
    ambito_for =    "Ambito_for",
    ambito_while =  "Ambito_while",
    ambito_select = "Ambito_select",
    ambito_else = "Ambito_else",
            
            
    ///////////////////ROLES
    var_primitiva   =   "var primitiva",
    metodo          =   "metodo",
    retorno         =   "retorno",
    parametro       =   "parametro",
    estructura      =   "estructura",
    arreglo         =   "arreglo",
            
    ////////////////////Tipo Codigo Generado
    codigo_estr         =   "//-----------------Codigo: Constructor de estructura-------------------\n",        
    codigo_da_pri       =   "//-----------------Codigo: Declaracion y Asignacion Primitiva----------\n",
    codigo_d_pri        =   "//-----------------Codigo: Declaracion de Primitiva--------------------\n",
    codigo_da_arr_est   =   "//-----------------Codigo: Declaracion y Asignacion Arreglo Est--------\n",
    codigo_d_arr_est    =   "//-----------------Codigo: Declaracion de Arreglo Estrcutura-----------\n",
    codigo_da_arr_pri   =   "//-----------------Codigo: Declaracion y Asignacion Arreglo Pri--------\n",
    codigo_d_arr_pri    =   "//-----------------Codigo: Declaracion de Arreglo Primitiva------------\n",
    codigo_da_est       =   "//-----------------Codigo: Declaracion y Asignacion Estrcutura---------\n",
    codigo_d_est        =   "//-----------------Codigo: Declaracion de Estrcutura-------------------\n",
            
                   
    codigo_da_pri_loc       =   "//-----------------Codigo: Declaracion y Asignacion Primitiva local ----------\n",
    codigo_d_pri_loc        =   "//-----------------Codigo: Declaracion de Primitiva local --------------------\n",
    codigo_da_arr_est_loc   =   "//-----------------Codigo: Declaracion y Asignacion Arreglo Est local --------\n",
    codigo_d_arr_est_loc    =   "//-----------------Codigo: Declaracion de Arreglo Estrcutura local -----------\n",
    codigo_da_arr_pri_loc   =   "//-----------------Codigo: Declaracion y Asignacion Arreglo Pri local --------\n",
    codigo_d_arr_pri_loc    =   "//-----------------Codigo: Declaracion de Arreglo Primitiva local ------------\n",
    codigo_da_est_loc       =   "//-----------------Codigo: Declaracion y Asignacion Estrcutura local ---------\n",
    codigo_d_est_loc        =   "//-----------------Codigo: Declaracion de Estrcutura local -------------------\n",        
            
    codigo_a_var_est_glo        =   "//-----------------Codigo: Asignacion variable de EST Global-------------------\n",        
    codigo_a_var_glo            =   "//-----------------Codigo: Asignacion de varibale Global ----------------------\n",        
    codigo_a_arr_glo            =   "//-----------------Codigo: Asignacion de Arreglo Global -----------------------\n",
    codigo_a_arr_arr_glo        =   "//-----------------Codigo: Asignacion de Arr de Arr Global --------------------\n",
    codigo_a_var_arr_glo        =   "//-----------------Codigo: Asignacion de var de Arr Global --------------------\n",
    codigo_a_arr_est_glo        =   "//-----------------Codigo: Asignacion de Arreglo EST Global -------------------\n",
    
    codigo_a_var_est_loc        =   "//-----------------Codigo: Asignacion variable de EST Local -------------------\n",        
    codigo_a_var_loc            =   "//-----------------Codigo: Asignacion de varibale Local  ----------------------\n",        
    codigo_a_arr_loc            =   "//-----------------Codigo: Asignacion de Arreglo Local  -----------------------\n",
    codigo_a_arr_arr_loc        =   "//-----------------Codigo: Asignacion de Arr de Arr Local  --------------------\n",
    codigo_a_var_arr_loc        =   "//-----------------Codigo: Asignacion de var de Arr Local  --------------------\n",
    codigo_a_arr_est_loc        =   "//-----------------Codigo: Asignacion de Arreglo EST Local  -------------------\n",
    //////////////// Reservadas DASM
            
            get_local   = "get_local ",
            set_local = "set_local ",
            tee_local = "tee_local ",
            
            get_local_calc = "get_local $calc",
            set_local_calc = "set_local $calc",
            tee_local_calc = "tee_local $calc",
     
            get_local_0 = "get_local 0",      
            set_local_0 = "set_local 0",
            tee_local_0 = "tee_local 0",
            
            
            get_local_1 = "get_local 1",
            set_local_1 = "set_local 1",
            tee_local_1 = "tee_local 1",
            
            get_local_2 = "get_local 2",
            set_local_2 = "set_local 2",
            tee_local_2 = "tee_local 2",
            
            get_local_1000 = "get_local 3",
            set_local_1000 = "set_local 3",
            tee_local_1000 = "tee_local 3",
            
            get_local_2000 = "get_local 4",
            set_local_2000 = "set_local 4",
            tee_local_2000 = "tee_local 4",
            
            get_global = "get_global ",
            set_global = "set_global ",
            tee_global = "tee_global ",
            
            get_global_calc = "get_global $calc",
            set_global_calc = "set_global $calc",
            tee_global_calc = "tee_global $calc",
            
            br_if = "br_if ",
            br__if = "br__if ",
            br = "br ",
            
            Add = "Add",
            Diff = "Diff",
            Mult = "Mult",
            Div = "Div",
            Mod = "Mod",
            
            Function = "Function ",
            Call_pot = "Call_$potencia", 
            End = "End",
            Print = "Print",
            _C="%c",
            _D="%d",
            _F="%f",
            
            point ="$point",
            quadrate = "$quadrate",
            oval = "$oval",
            line = "$line",
            string = "$string",
            
            Call ="Call ",
                    
            
            Eqz = "Eqz",
            Eqs = "Eqs",
            Lt = "Lt",
            Gt = "Gt",
            Lte = "Lte",
            inicio_ ="$Inicio_",
            Gte = "Gte"
            
            
            
            ;
}
