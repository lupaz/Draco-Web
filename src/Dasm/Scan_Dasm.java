/* The following code was generated by JFlex 1.4.3 on 11/4/18 3:49 AM */

package Dasm;

//import ManejoErrores.ListaErrores;
//import usac_web.Interfaz_Web;
import java_cup.runtime.Symbol;
import static draco_web.InterfazD.listaErrores;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 11/4/18 3:49 AM from the specification file
 * <tt>src/Dasm/lexico.jflex</tt>
 */
public class Scan_Dasm implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int COMENTARIO1 = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = {
     0,  0,  0,  0,  0,  0,  0,  0,  0,  9, 10,  0,  9,  9,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     9,  0,  0,  0, 11,  2,  0,  0,  0,  0,  0,  0,  0, 13,  7,  1, 
     6,  6,  6,  6,  6,  6,  6,  6,  6,  6, 12,  0,  0,  0,  0,  0, 
     0, 14,  8, 23, 15, 27, 25, 31,  8,  8,  8,  8, 30, 17, 38, 37, 
    36,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  0,  0,  0,  0, 35, 
     0, 24, 33,  3,  4, 32,  5, 39,  8, 16,  8,  8, 19,  8, 26, 22, 
    41, 28, 34, 40, 20, 18, 21,  8,  8,  8, 29,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 
     0,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0
  };

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\3\1\2\2\1\3\1\4\1\5\1\6\1\7"+
    "\1\10\23\2\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\2\1\0\15\2\1\17\1\20\1\21\1\2\1\22"+
    "\6\2\1\23\1\24\1\25\1\2\1\26\1\2\1\27"+
    "\5\2\1\30\1\31\1\2\1\32\1\33\2\2\1\34"+
    "\4\2\1\35\1\36\1\37\1\40\1\2\1\41\1\42"+
    "\15\2\1\43\1\2\1\44\5\2\1\45\4\2\1\46"+
    "\2\2\1\47\14\2\1\50\1\51\4\2\1\52\1\2"+
    "\1\53\1\2\1\54\1\2\1\55\1\56\1\57";

  private static int [] zzUnpackAction() {
    int [] result = new int[152];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\52\0\124\0\176\0\250\0\322\0\374\0\u0126"+
    "\0\124\0\124\0\124\0\124\0\124\0\u0150\0\u017a\0\u01a4"+
    "\0\u01ce\0\u01f8\0\u0222\0\u024c\0\u0276\0\u02a0\0\u02ca\0\u02f4"+
    "\0\u031e\0\u0348\0\u0372\0\u039c\0\u03c6\0\u03f0\0\u041a\0\u0444"+
    "\0\124\0\124\0\124\0\124\0\124\0\124\0\u046e\0\u0498"+
    "\0\u04c2\0\u04ec\0\u0516\0\u0540\0\u056a\0\u0594\0\u05be\0\u05e8"+
    "\0\u0612\0\u063c\0\u0666\0\u0690\0\u06ba\0\u06e4\0\u070e\0\u0738"+
    "\0\u0762\0\374\0\u078c\0\u07b6\0\u07e0\0\u080a\0\u0834\0\u085e"+
    "\0\u0498\0\374\0\374\0\u0888\0\374\0\u08b2\0\374\0\u08dc"+
    "\0\u0906\0\u0930\0\u095a\0\u0984\0\374\0\374\0\u09ae\0\374"+
    "\0\374\0\u09d8\0\u0a02\0\374\0\u0a2c\0\u0a56\0\u0a80\0\u0aaa"+
    "\0\374\0\374\0\374\0\374\0\u0ad4\0\374\0\374\0\u0afe"+
    "\0\u0b28\0\u0b52\0\u0b7c\0\u0ba6\0\u0bd0\0\u0bfa\0\u0c24\0\u0c4e"+
    "\0\u0c78\0\u0ca2\0\u0ccc\0\u0cf6\0\374\0\u0d20\0\374\0\u0d4a"+
    "\0\u0d74\0\u0d9e\0\u0dc8\0\u0df2\0\374\0\u0e1c\0\u0e46\0\u0e70"+
    "\0\u0e9a\0\374\0\u0ec4\0\u0eee\0\374\0\u0f18\0\u0f42\0\u0f6c"+
    "\0\u0f96\0\u0fc0\0\u0fea\0\u1014\0\u103e\0\u1068\0\u1092\0\u10bc"+
    "\0\u10e6\0\374\0\374\0\u1110\0\u113a\0\u1164\0\u118e\0\374"+
    "\0\u11b8\0\374\0\u11e2\0\374\0\u120c\0\374\0\374\0\374";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[152];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\2\7\1\10\1\3\1\7"+
    "\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\7"+
    "\1\20\1\7\1\21\1\22\1\7\1\23\1\24\1\7"+
    "\1\25\1\7\1\26\1\27\1\7\1\30\1\31\1\7"+
    "\1\32\2\7\1\33\1\34\1\35\1\36\1\37\1\40"+
    "\12\41\1\42\37\41\53\0\1\43\53\0\1\44\1\45"+
    "\1\46\47\0\4\7\1\0\1\7\5\0\12\7\1\47"+
    "\21\7\3\0\4\7\1\0\1\7\5\0\34\7\6\0"+
    "\1\10\1\50\45\0\1\7\1\51\2\7\1\0\1\7"+
    "\5\0\14\7\1\52\17\7\3\0\4\7\1\0\1\7"+
    "\5\0\2\7\1\53\31\7\3\0\4\7\1\0\1\7"+
    "\5\0\4\7\1\54\3\7\1\55\23\7\3\0\4\7"+
    "\1\0\1\7\5\0\2\7\1\56\31\7\3\0\4\7"+
    "\1\0\1\7\5\0\22\7\1\57\11\7\3\0\4\7"+
    "\1\0\1\7\5\0\7\7\1\60\24\7\3\0\4\7"+
    "\1\0\1\7\5\0\12\7\1\61\21\7\3\0\4\7"+
    "\1\0\1\7\5\0\4\7\1\62\27\7\3\0\4\7"+
    "\1\0\1\7\5\0\14\7\1\63\1\7\1\64\15\7"+
    "\3\0\4\7\1\0\1\7\5\0\4\7\1\65\27\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\66\25\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\67\25\7"+
    "\3\0\4\7\1\0\1\7\5\0\24\7\1\70\7\7"+
    "\3\0\4\7\1\0\1\7\5\0\24\7\1\71\7\7"+
    "\3\0\4\7\1\0\1\7\5\0\24\7\1\72\7\7"+
    "\3\0\4\7\1\0\1\7\5\0\10\7\1\73\23\7"+
    "\3\0\4\7\1\0\1\7\5\0\22\7\1\74\11\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\75\13\7"+
    "\1\76\11\7\3\0\4\7\1\0\1\7\5\0\10\7"+
    "\1\77\23\7\3\0\4\7\1\0\1\7\5\0\5\7"+
    "\1\100\26\7\6\0\1\101\46\0\1\7\1\102\2\7"+
    "\1\0\1\7\5\0\34\7\3\0\1\7\1\103\2\7"+
    "\1\0\1\7\5\0\34\7\3\0\2\7\1\104\1\7"+
    "\1\0\1\7\5\0\7\7\1\105\24\7\3\0\4\7"+
    "\1\0\1\7\5\0\5\7\1\106\26\7\3\0\1\7"+
    "\1\107\2\7\1\0\1\7\5\0\34\7\3\0\4\7"+
    "\1\0\1\7\5\0\14\7\1\110\17\7\3\0\4\7"+
    "\1\0\1\7\5\0\22\7\1\111\11\7\3\0\4\7"+
    "\1\0\1\7\5\0\12\7\1\112\21\7\3\0\4\7"+
    "\1\0\1\7\5\0\5\7\1\113\26\7\3\0\4\7"+
    "\1\0\1\7\5\0\14\7\1\114\17\7\3\0\1\7"+
    "\1\115\2\7\1\0\1\7\5\0\34\7\3\0\4\7"+
    "\1\0\1\7\5\0\17\7\1\116\14\7\3\0\4\7"+
    "\1\0\1\7\5\0\12\7\1\117\21\7\3\0\4\7"+
    "\1\0\1\7\5\0\22\7\1\120\11\7\3\0\4\7"+
    "\1\0\1\7\5\0\22\7\1\121\11\7\3\0\4\7"+
    "\1\0\1\7\5\0\25\7\1\122\6\7\3\0\4\7"+
    "\1\0\1\7\5\0\2\7\1\123\31\7\3\0\4\7"+
    "\1\0\1\7\5\0\6\7\1\124\25\7\3\0\4\7"+
    "\1\0\1\7\5\0\6\7\1\125\25\7\3\0\4\7"+
    "\1\0\1\7\5\0\24\7\1\126\7\7\3\0\4\7"+
    "\1\0\1\7\5\0\6\7\1\127\25\7\3\0\4\7"+
    "\1\0\1\7\5\0\2\7\1\130\31\7\3\0\1\131"+
    "\3\7\1\0\1\7\5\0\34\7\3\0\2\7\1\132"+
    "\1\7\1\0\1\7\5\0\34\7\3\0\4\7\1\0"+
    "\1\7\5\0\6\7\1\133\25\7\3\0\4\7\1\0"+
    "\1\7\5\0\22\7\1\134\11\7\3\0\4\7\1\0"+
    "\1\7\5\0\25\7\1\135\6\7\3\0\4\7\1\0"+
    "\1\7\5\0\5\7\1\136\26\7\3\0\4\7\1\0"+
    "\1\7\5\0\5\7\1\137\26\7\3\0\1\140\3\7"+
    "\1\0\1\7\5\0\34\7\3\0\1\7\1\141\2\7"+
    "\1\0\1\7\5\0\34\7\3\0\4\7\1\0\1\7"+
    "\5\0\2\7\1\142\22\7\1\143\6\7\3\0\4\7"+
    "\1\0\1\7\5\0\14\7\1\144\17\7\3\0\4\7"+
    "\1\0\1\7\5\0\25\7\1\145\6\7\3\0\4\7"+
    "\1\0\1\7\5\0\2\7\1\146\31\7\3\0\4\7"+
    "\1\0\1\7\5\0\25\7\1\147\6\7\3\0\4\7"+
    "\1\0\1\7\5\0\14\7\1\150\17\7\3\0\4\7"+
    "\1\0\1\7\5\0\5\7\1\151\23\7\1\152\2\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\153\25\7"+
    "\3\0\4\7\1\0\1\7\5\0\24\7\1\154\7\7"+
    "\3\0\2\7\1\155\1\7\1\0\1\7\5\0\34\7"+
    "\3\0\4\7\1\0\1\7\5\0\2\7\1\156\31\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\157\25\7"+
    "\3\0\4\7\1\0\1\7\5\0\5\7\1\160\23\7"+
    "\1\161\2\7\3\0\4\7\1\0\1\7\5\0\14\7"+
    "\1\162\17\7\3\0\4\7\1\0\1\7\5\0\5\7"+
    "\1\163\23\7\1\164\2\7\3\0\4\7\1\0\1\7"+
    "\5\0\6\7\1\165\25\7\3\0\4\7\1\0\1\7"+
    "\5\0\10\7\1\166\23\7\3\0\4\7\1\0\1\7"+
    "\5\0\5\7\1\167\26\7\3\0\4\7\1\0\1\7"+
    "\5\0\2\7\1\170\31\7\3\0\4\7\1\0\1\7"+
    "\5\0\12\7\1\171\21\7\3\0\2\7\1\172\1\7"+
    "\1\0\1\7\5\0\34\7\3\0\4\7\1\0\1\7"+
    "\5\0\10\7\1\173\23\7\3\0\4\7\1\0\1\7"+
    "\5\0\5\7\1\174\26\7\3\0\4\7\1\0\1\7"+
    "\5\0\31\7\1\175\2\7\3\0\4\7\1\0\1\7"+
    "\5\0\10\7\1\176\23\7\3\0\4\7\1\0\1\7"+
    "\5\0\5\7\1\177\26\7\3\0\1\200\3\7\1\0"+
    "\1\7\5\0\34\7\3\0\4\7\1\0\1\7\5\0"+
    "\10\7\1\201\23\7\3\0\4\7\1\0\1\7\5\0"+
    "\10\7\1\202\23\7\3\0\4\7\1\0\1\7\5\0"+
    "\6\7\1\203\25\7\3\0\1\204\3\7\1\0\1\7"+
    "\5\0\34\7\3\0\4\7\1\0\1\7\5\0\10\7"+
    "\1\205\23\7\3\0\1\206\3\7\1\0\1\7\5\0"+
    "\34\7\3\0\4\7\1\0\1\7\5\0\10\7\1\207"+
    "\23\7\3\0\4\7\1\0\1\7\5\0\12\7\1\210"+
    "\21\7\3\0\4\7\1\0\1\7\5\0\23\7\1\211"+
    "\10\7\3\0\4\7\1\0\1\7\5\0\14\7\1\212"+
    "\17\7\3\0\4\7\1\0\1\7\5\0\22\7\1\213"+
    "\11\7\3\0\4\7\1\0\1\7\5\0\12\7\1\214"+
    "\21\7\3\0\4\7\1\0\1\7\5\0\23\7\1\215"+
    "\10\7\3\0\4\7\1\0\1\7\5\0\12\7\1\216"+
    "\21\7\3\0\4\7\1\0\1\7\5\0\23\7\1\217"+
    "\10\7\3\0\4\7\1\0\1\7\5\0\5\7\1\220"+
    "\26\7\3\0\4\7\1\0\1\7\5\0\12\7\1\221"+
    "\21\7\3\0\4\7\1\0\1\7\5\0\5\7\1\222"+
    "\26\7\3\0\4\7\1\0\1\7\5\0\12\7\1\223"+
    "\21\7\3\0\4\7\1\0\1\7\5\0\5\7\1\224"+
    "\26\7\3\0\4\7\1\0\1\7\5\0\12\7\1\225"+
    "\21\7\3\0\4\7\1\0\1\7\5\0\5\7\1\226"+
    "\26\7\3\0\4\7\1\0\1\7\5\0\5\7\1\227"+
    "\26\7\3\0\4\7\1\0\1\7\5\0\5\7\1\230"+
    "\26\7";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4662];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\5\1\5\11\23\1\6\11\1\1\1\0"+
    "\160\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[152];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */

    //Código de usuario
    public String nomArch ="";


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Scan_Dasm(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Scan_Dasm(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      yychar+= zzMarkedPosL-zzStartRead;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 11: 
          { yybegin(COMENTARIO1);
          }
        case 48: break;
        case 24: 
          { return new Symbol(sym.End, yyline,yycolumn,yytext());
          }
        case 49: break;
        case 7: 
          { return new Symbol(sym.DPUNTOS, yyline,yycolumn,yytext());
          }
        case 50: break;
        case 15: 
          { return new Symbol(sym.Lt, yyline, yycolumn,yytext());
          }
        case 51: break;
        case 3: 
          { return new Symbol(sym.enter, yyline, yycolumn,yytext());
          }
        case 52: break;
        case 13: 
          { return new Symbol(sym.D, yyline, yycolumn,yytext());
          }
        case 53: break;
        case 27: 
          { return new Symbol(sym.Gte, yyline,yycolumn,yytext());
          }
        case 54: break;
        case 46: 
          { return new Symbol(sym.get_global, yyline,yycolumn,yytext());
          }
        case 55: break;
        case 29: 
          { return new Symbol(sym.Calc, yyline,yycolumn,yytext());
          }
        case 56: break;
        case 4: 
          { /*Espacios en blanco, ignorados*/
          }
        case 57: break;
        case 31: 
          { return new Symbol(sym.Mul, yyline,yycolumn,yytext());
          }
        case 58: break;
        case 12: 
          { return new Symbol(sym.C, yyline, yycolumn,yytext());
          }
        case 59: break;
        case 35: 
          { return new Symbol(sym.Br_if, yyline,yycolumn,yytext());
          }
        case 60: break;
        case 34: 
          { return new Symbol(sym.Call, yyline,yycolumn,yytext());
          }
        case 61: break;
        case 8: 
          { return new Symbol(sym.MENOS, yyline,yycolumn,yytext());
          }
        case 62: break;
        case 47: 
          { return new Symbol(sym.set_global, yyline,yycolumn,yytext());
          }
        case 63: break;
        case 41: 
          { return new Symbol(sym.quadrate, yyline, yycolumn,yytext());
          }
        case 64: break;
        case 37: 
          { return new Symbol(sym.point, yyline,yycolumn,yytext());
          }
        case 65: break;
        case 36: 
          { return new Symbol(sym.Print, yyline,yycolumn,yytext());
          }
        case 66: break;
        case 10: 
          { yybegin(YYINITIAL);
          }
        case 67: break;
        case 39: 
          { return new Symbol(sym.string, yycolumn,yyline,yytext());
          }
        case 68: break;
        case 38: 
          { return new Symbol(sym.Br__if, yyline,yycolumn,yytext());
          }
        case 69: break;
        case 33: 
          { return new Symbol(sym.oval, yyline,yycolumn,yytext());
          }
        case 70: break;
        case 44: 
          { return new Symbol(sym.set_local, yyline,yycolumn,yytext());
          }
        case 71: break;
        case 25: 
          { return new Symbol(sym.Eqz, yyline,yycolumn,yytext());
          }
        case 72: break;
        case 28: 
          { return new Symbol(sym.Not, yyline,yycolumn,yytext());
          }
        case 73: break;
        case 1: 
          { //Interfaz_Web.listaErrores.insertarError("Error Lexico", yytext(),(yycolumn+1)+"",(yyline+1)+"","El caracter no pertenece al lenguaje",Interfaz_Web.doc_actual);
                    String errLex = "Error léxico : '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+(yycolumn+1);
                    System.out.println(errLex);
          }
        case 74: break;
        case 30: 
          { return new Symbol(sym.Diff, yyline,yycolumn,yytext());
          }
        case 75: break;
        case 40: 
          { return new Symbol(sym.Function, yyline,yycolumn,yytext());
          }
        case 76: break;
        case 26: 
          { return new Symbol(sym.Lte, yyline,yycolumn,yytext());
          }
        case 77: break;
        case 17: 
          { return new Symbol(sym.Br, yyline,yycolumn,yytext());
          }
        case 78: break;
        case 23: 
          { return new Symbol(sym.Mod, yyline,yycolumn,yytext());
          }
        case 79: break;
        case 16: 
          { return new Symbol(sym.Gt, yyline, yycolumn,yytext());
          }
        case 80: break;
        case 5: 
          { /*Saltos de linea, ignorados*/
          }
        case 81: break;
        case 19: 
          { return new Symbol(sym.decim, yyline, yycolumn,yytext());
          }
        case 82: break;
        case 42: 
          { return new Symbol(sym.tee_local, yyline,yycolumn,yytext());
          }
        case 83: break;
        case 21: 
          { return new Symbol(sym.And, yyline,yycolumn,yytext());
          }
        case 84: break;
        case 22: 
          { return new Symbol(sym.Div, yyline,yycolumn,yytext());
          }
        case 85: break;
        case 2: 
          { return new Symbol(sym.id, yyline, yycolumn,yytext());
          }
        case 86: break;
        case 43: 
          { return new Symbol(sym.get_local, yyline,yycolumn,yytext());
          }
        case 87: break;
        case 14: 
          { return new Symbol(sym.F, yyline, yycolumn,yytext());
          }
        case 88: break;
        case 45: 
          { return new Symbol(sym.tee_global, yyline,yycolumn,yytext());
          }
        case 89: break;
        case 32: 
          { return new Symbol(sym.line, yycolumn,yyline,yytext());
          }
        case 90: break;
        case 20: 
          { return new Symbol(sym.Add, yyline,yycolumn,yytext());
          }
        case 91: break;
        case 6: 
          { return new Symbol(sym.DOLAR, yyline,yycolumn,yytext());
          }
        case 92: break;
        case 18: 
          { return new Symbol(sym.Or, yyline,yycolumn,yytext());
          }
        case 93: break;
        case 9: 
          { 
          }
        case 94: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { return new java_cup.runtime.Symbol(sym.EOF); }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}