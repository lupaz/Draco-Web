/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dasm;

import Dplusplus.*;


/**
 *
 * @author Luis
 */
public class Cadena {
    //======================No terminales====> 
    public static final String 
    
    INICIO=         "INICIO",
            L_SENT =            "L_SENT",
            SENT =              "SENT",
            FUN=            "CUERPO2",
            VAL=             "VAL",
            LLAMADA=             "LLAMADA",
            PUNTO=             "PUNTO",        
            CUADRADO=             "CUADRADO",
            LINEA=            "LINEA",
            CADENA=           "CADENA",
            OVALO =           "OVALO",
            FUNCTION =        "FUNCTION",
            
            SET_LOCAL=           "SET_LOCAL",
            GET_LOCAL =           "GET_LOCAL",
            SET_GLOBAL =           "SET_GLOBAL",
            GET_GLOBAL =           "GET_GLOBAL",
            TEE_LOCAL =           "TEE_LOCAL",
            TEE_GLOBAL =           "TEE_GLOBAL",
            SALTO_SI =              "BR_IF",
            SALTO_SI2 =              "BR_IF2",
            SALTO =                 "BR"
            ;
            
    
    ///////////////////////////Terminales ------------------------------------->

    public static final String 
    
    numero =            "numero",           
    decimal  =          "decimal",
    id      =           "id",        
    Add =               "Add",
    Diff =              "Diff",
    Mul =               "Mul",
    Div =               "Div",
    Mod =               "Mod",            
    D =                 "D",
    C =                 "C",            
    F =                 "F",        
    call =              "call",
    calc =              "calc",        
    Function =          "Function",
            
            Label=       "Label",
            Print=       "Print",
            
            Eqz =           "Eqz",            
            Lt =            "Lt",
            Gt =            "Gt",
            Lte =           "Lte",
            Gte =           "Gte",
            And =           "And",
            Or =            "Or",
            Not=            "Not",            
            point =         "point",
            quadrate =      "quadrate",
            oval =          "oval",
            line =          "line",
            string =        "string"
            ;
   //posicion de cadenas por defecto; 
   
   public static final String
           ini_idex_bound = "4500",
           cadena_nula = "9522";             
    
}
