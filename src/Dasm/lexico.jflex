package Dasm;

//import ManejoErrores.ListaErrores;
//import usac_web.Interfaz_Web;
import java_cup.runtime.Symbol;
import static draco_web.InterfazD.listaErrores;

%%

%{

    //Código de usuario
    public String nomArch ="";
%}

%cup
%class Scan_Dasm
%public
%line
%char
%column
%full
%state COMENTARIO1


//comentarios 
COM1    = "//"
C       = "%c"
D       = "%d"
F       = "%f"

//expresiones

ENTERO  = [0-9]+
DECIMAL = [0-9]+["."][0-9]+   
ID      = [A-Za-zñÑ_][_0-9A-Za-zñÑ]*
SPACE   = [\ \r\t\f\t]
ENTER   = [\ \n]

%%

<YYINITIAL> {

    //simbolos
    
    "$"     {return new Symbol(sym.DOLAR, yyline,yycolumn,yytext()); }
    ":"     {return new Symbol(sym.DPUNTOS, yyline,yycolumn,yytext()); }
    "-"     {return new Symbol(sym.MENOS, yyline,yycolumn,yytext());}

    //Operadores aritmeticos


    //Operadores Logicos


    //Operadores relacionales


    //palabras reservadas

    "Add"           {  return new Symbol(sym.Add, yyline,yycolumn,yytext()); } 
    "Diff"          {  return new Symbol(sym.Diff, yyline,yycolumn,yytext()); }
    "Mult"          {  return new Symbol(sym.Mul, yyline,yycolumn,yytext()); } 
    "Div"           {  return new Symbol(sym.Div, yyline,yycolumn,yytext()); }
    "Mod"           {  return new Symbol(sym.Mod, yyline,yycolumn,yytext()); }
    "Call"          {  return new Symbol(sym.Call, yyline,yycolumn,yytext()); }
    "Ret"           {  return new Symbol(sym.Ret, yyline,yycolumn,yytext()); }
    "calc"          {  return new Symbol(sym.Calc, yyline,yycolumn,yytext()); }    
    "Function"      {  return new Symbol(sym.Function, yyline,yycolumn,yytext()); } 
    "End"           {  return new Symbol(sym.End, yyline,yycolumn,yytext()); }
    "Eqz"           {  return new Symbol(sym.Eqz, yyline,yycolumn,yytext()); }
    "Eqs"           {  return new Symbol(sym.Eqs, yyline,yycolumn,yytext()); }
    "Lt"            {  return new Symbol(sym.Lt, yyline, yycolumn,yytext());}
    "Gt"            {  return new Symbol(sym.Gt, yyline, yycolumn,yytext());}
 
    "Lte"           {  return new Symbol(sym.Lte, yyline,yycolumn,yytext()); } 
    "Gte"           {  return new Symbol(sym.Gte, yyline,yycolumn,yytext()); }
    "br_if"         {  return new Symbol(sym.Br_if, yyline,yycolumn,yytext()); }
    "br__if"        {  return new Symbol(sym.Br__if, yyline,yycolumn,yytext()); }
    "br"            {  return new Symbol(sym.Br, yyline,yycolumn,yytext()); }
    "Print"         {  return new Symbol(sym.Print, yyline,yycolumn,yytext()); }     
   

    "And"           {  return new Symbol(sym.And, yyline,yycolumn,yytext()); }
    "Or"            {  return new Symbol(sym.Or, yyline,yycolumn,yytext()); }
    "Not"           {  return new Symbol(sym.Not, yyline,yycolumn,yytext()); }

    "get_local"     {  return new Symbol(sym.get_local, yyline,yycolumn,yytext()); }
    "set_local"     {  return new Symbol(sym.set_local, yyline,yycolumn,yytext()); } 
    "get_global"    {  return new Symbol(sym.get_global, yyline,yycolumn,yytext()); }
    "set_global"    {  return new Symbol(sym.set_global, yyline,yycolumn,yytext()); }
    "tee_local"     {  return new Symbol(sym.tee_local, yyline,yycolumn,yytext()); }
    "tee_global"    {  return new Symbol(sym.tee_global, yyline,yycolumn,yytext()); }
    

    
    "point"         {  return new Symbol(sym.point, yyline,yycolumn,yytext()); }
    "quadrate"      {  return new Symbol(sym.quadrate, yyline, yycolumn,yytext()); }
    "oval"          {  return new Symbol(sym.oval, yyline,yycolumn,yytext()); }
    "line"          {  return new Symbol(sym.line, yycolumn,yyline,yytext()); }
    "string"        {  return new Symbol(sym.string, yycolumn,yyline,yytext()); }
    "Call_$potencia" {  return new Symbol(sym.Pot, yyline,yycolumn,yytext()); }

    //exp regulares
    {ENTERO}        {  return new Symbol(sym.enter, yyline, yycolumn,yytext());}
    {DECIMAL}       {  return new Symbol(sym.decim, yyline, yycolumn,yytext());}
    {C}             {  return new Symbol(sym.C, yyline, yycolumn,yytext());}    
    {D}             {  return new Symbol(sym.D, yyline, yycolumn,yytext());}
    {F}             {  return new Symbol(sym.F, yyline, yycolumn,yytext());}
    {ID}            {  return new Symbol(sym.id, yyline, yycolumn,yytext());}
    
    
    //estados 
    {COM1}       { yybegin(COMENTARIO1); }
    
    {SPACE}     { /*Espacios en blanco, ignorados*/ }
    {ENTER}     { /*Saltos de linea, ignorados*/}

    //errores lexicos 

    .           { 
                    //Interfaz_Web.listaErrores.insertarError("Error Lexico", yytext(),(yycolumn+1)+"",(yyline+1)+"","El caracter no pertenece al lenguaje",Interfaz_Web.doc_actual);
                    String errLex = "Error léxico : '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+(yycolumn+1);
                    System.out.println(errLex); 
                }

}

<COMENTARIO1>{
    [\n]    { yybegin(YYINITIAL); }
    .       { }
}

