package DracoScript;

//import ManejoErrores.ListaErrores;
//import usac_web.Interfaz_Web;
import java_cup.runtime.Symbol;
import static draco_web.InterfazD.listaErrores;
import draco_web.Pintar;

%%

%{

    //Código de usuario
    String cadena= "";
    String charm = "";
    public String nomArch ="";
    public Pintar pintar;
    int inicio=0;
%}

%cup
%class Scan_DS
%public
%line
%char
%column
%full
%state CADENA,COMENTARIO1,COMENTARIO2,CHARMEL
%ignorecase

//comentarios 
COM1    = "$$"
COM2    = "$*"
F_COM2  = "*$"
CHA     = "'"

//expresiones

ENTERO  = [0-9]+
DECIMAL = [0-9]+["."][0-9]+   
ID      = [A-Za-zñÑ_][_0-9A-Za-zñÑ]*
SPACE   = [\ \r\t\f\t]
ENTER   = [\ \n]

%%

<YYINITIAL> {

    //simbolos
    "("     {return new Symbol(sym.PAR_A, yyline,yycolumn,yytext()); }
    ")"     {return new Symbol(sym.PAR_C, yyline,yycolumn,yytext()); }
    "{"     {return new Symbol(sym.LLAV_A, yyline,yycolumn,yytext()); }
    "}"     {return new Symbol(sym.LLAV_C, yyline,yycolumn,yytext()); }
    ","     {return new Symbol(sym.COMA, yyline,yycolumn,yytext()); }
    ";"     {return new Symbol(sym.PYCOMA, yyline,yycolumn,yytext()); }
    ":=:"     {return new Symbol(sym.ASIG, yyline,yycolumn,yytext()); }

    //Operadores aritmeticos
    "+"     {return new Symbol(sym.MAS, yyline,yycolumn,yytext()); }
    "-"     {return new Symbol(sym.MENOS, yyline,yycolumn,yytext()); }
    "/"     {return new Symbol(sym.DIV, yyline,yycolumn,yytext()); }
    "*"     {return new Symbol(sym.POR, yyline,yycolumn,yytext()); }
    "^"     {return new Symbol(sym.POT, yyline,yycolumn,yytext()); }
    "%"     {return new Symbol(sym.MOD, yyline,yycolumn,yytext()); }
    "++"    {return new Symbol(sym.INC, yyline,yycolumn,yytext()); }
    "--"    {return new Symbol(sym.DEC, yyline,yycolumn,yytext()); }


    //Operadores Logicos

    "||"    {return new Symbol(sym.OR, yyline,yycolumn,yytext()); }
    "&&"    {return new Symbol(sym.AND, yyline,yycolumn,yytext()); }
    "!"     {return new Symbol(sym.NOT, yyline,yycolumn,yytext()); }

    //Operadores relacionales
    ">"     { return new Symbol(sym.MAYOR,yyline, yycolumn,yytext()); }
    "<"     { return new Symbol(sym.MENOR, yyline,yycolumn,yytext()); }
    ">="    { return new Symbol(sym.MAYOR_I, yyline,yycolumn,yytext()); }
    "<="    { return new Symbol(sym.MENOR_I, yyline,yycolumn,yytext()); }
    "=="    { return new Symbol(sym.IGUAL, yyline,yycolumn,yytext()); }
    "!="    { return new Symbol(sym.DIFERENTE, yyline,yycolumn,yytext()); }

    //palabras reservadas

    "RunMultDasm"   { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.run, yyline,yycolumn,yytext()); }  
    "var"           { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.var, yyline,yycolumn,yytext()); }
    "nulo"          { pintar.pintaAmari(yychar,yylength()); return new Symbol(sym.nulo, yyline,yycolumn,yytext()); }
    "true"          { pintar.pintaVerde(yychar,yylength()); return new Symbol(sym.bool, yyline, yycolumn,yytext());}
    "false"         { pintar.pintaVerde(yychar,yylength()); return new Symbol(sym.bool, yyline, yycolumn,yytext());}
 
    "if not"        { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.si_no, yyline,yycolumn,yytext()); }
    "if"            { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.si, yyline,yycolumn,yytext()); }
    "elif"          { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.sinosi, yyline,yycolumn,yytext()); }
    "for"           { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.para, yyline,yycolumn,yytext()); }
    "while"         { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.mientras, yyline,yycolumn,yytext()); }
    "smash"         { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.detener, yyline,yycolumn,yytext()); }  
    "print"         { pintar.pintaAzul(yychar,yylength());  return new Symbol(sym.imprimir, yyline,yycolumn,yytext()); }
    
    
    "point"         { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.punto, yyline,yycolumn,yytext()); }
    "quadrate"      { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.cuadrado, yyline, yycolumn,yytext()); }
    "oval"          { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.ovalo, yyline,yycolumn,yytext()); }
    "line"          { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.linea, yycolumn,yyline,yytext()); }
    "string"        { pintar.pintaAzul(yychar,yylength()); return new Symbol(sym.cadena, yycolumn,yyline,yytext()); }
    
    //exp regulares
    {ENTERO}        { pintar.pintaMora(yychar,yylength()); return new Symbol(sym.number, yyline, yycolumn,yytext());}
    {DECIMAL}       { pintar.pintaMora(yychar,yylength()); return new Symbol(sym.number, yyline, yycolumn,yytext());}
    {ID}            { pintar.pintaRojo(yychar,yylength()); return new Symbol(sym.id, yyline, yycolumn,yytext());}
    
    
    //estados 
    [\"]        { inicio=yychar; yybegin(CADENA); /*cadena="";*/ }
    {COM2}      { inicio=yychar; yybegin(COMENTARIO2); }
    {COM1}       { inicio= yychar; yybegin(COMENTARIO1); }
    {CHA}        { inicio=yychar; yybegin(CHARMEL);}
    
    {SPACE}     { /*Espacios en blanco, ignorados*/ }
    {ENTER}     { /*Saltos de linea, ignorados*/}

    //errores lexicos 

    .           { 
                    //Interfaz_Web.listaErrores.insertarError("Error Lexico", yytext(),(yycolumn+1)+"",(yyline+1)+"","El caracter no pertenece al lenguaje",Interfaz_Web.doc_actual);
                    String error = "ERROR LEXICO: Caracter invalido ->  " + yytext() + " L: " + (yyline+1) + " C: " + (yycolumn+1) + " Archivo" + nomArch;
                    listaErrores.add(error);
                    System.out.println(error); 
                }

}
//

<CADENA> {
        [\"] { String tmp=cadena;/*+"\"";*/ cadena=""; pintar.pintaNara(inicio,tmp.length()+2); yybegin(YYINITIAL);  return new Symbol(sym.cad,yyline,yycolumn,tmp); }
        [\n] {String tmp=cadena; cadena="";
                String error="ERROR LEXICO: Cadena invalida, se esperaba cierre de cadena. ->  " + tmp + " L: " + (yyline+1) + " C: " + (yycolumn+1) + " Archivo" + nomArch;
                listaErrores.add(error);
                System.out.println(error);
                yybegin(YYINITIAL);
            }
        [^\"] { cadena+=yytext();}
}

<COMENTARIO1>{
    [\n]    { String tmp=cadena; cadena=""; pintar.pintaGris(inicio,tmp.length()+2); yybegin(YYINITIAL); }
    .       { cadena+=yytext(); }
}

<COMENTARIO2>{
    {F_COM2}    {  String tmp=cadena; cadena=""; pintar.pintaGris(inicio,tmp.length()+4); yybegin(YYINITIAL); }
    [\n]        {  cadena+=yytext(); }  
    .           { cadena+=yytext();  }
}

<CHARMEL>{
    ['] {   String tmp=charm; charm=""; 
            if(tmp.length() > 1){  
                String error="ERROR LEXICO: Caracter invalido, solo se permite un caracter. ->  " + tmp + " L: " + (yyline+1) + " C: " + (yycolumn+1) + " Archivo" + nomArch;
                listaErrores.add(error);
                System.out.println(error);               
                yybegin(YYINITIAL);
            }else{
                yybegin(YYINITIAL);
                pintar.pintaNara(inicio,tmp.length()+2);
                return new Symbol(sym.carac,yyline,yycolumn,tmp); 
            } 
        }
    [\n]
            {
                String tmp=charm; charm="";
                String error="ERROR LEXICO: Caracter invalido, se esperaba cierre de caracter. ->  " + tmp + " L: " + (yyline+1) + " C: " + (yycolumn+1) + " Archivo" + nomArch;
                listaErrores.add(error);
                System.out.println(error);
                yybegin(YYINITIAL);
            }
    [^'] { charm+=yytext(); }
}