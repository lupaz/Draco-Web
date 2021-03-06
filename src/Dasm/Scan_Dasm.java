/* The following code was generated by JFlex 1.4.3 on 11/9/18 4:16 PM */

package Dasm;

//import ManejoErrores.ListaErrores;
//import usac_web.Interfaz_Web;
import java_cup.runtime.Symbol;
import static draco_web.InterfazD.listaErrores;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 11/9/18 4:16 PM from the specification file
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
     0, 14,  8, 23, 15, 29, 27, 34,  8,  8,  8,  8, 33, 17, 40, 39, 
    38,  8, 25,  8,  8,  8,  8,  8,  8,  8,  8,  0,  0,  0,  0, 37, 
     0, 24, 35,  3,  4, 26,  5, 41,  8, 16,  8,  8, 19,  8, 28, 22, 
    42, 30, 36, 32, 20, 18, 21,  8,  8,  8, 31,  0,  0,  0,  0,  0, 
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
    "\1\10\24\2\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\2\1\0\20\2\1\17\1\20\1\21\1\2\1\22"+
    "\4\2\1\23\1\24\1\25\1\2\1\26\1\2\1\27"+
    "\4\2\1\30\1\2\1\31\1\32\1\33\3\2\1\34"+
    "\1\35\2\2\1\36\2\2\1\37\1\40\1\41\1\42"+
    "\1\2\1\43\1\44\21\2\1\45\1\2\1\46\2\2"+
    "\1\47\2\2\1\0\2\2\1\50\2\2\1\51\4\2"+
    "\1\0\10\2\1\0\1\52\1\53\4\2\1\54\1\2"+
    "\1\0\1\55\1\2\1\56\1\2\1\57\1\0\1\60"+
    "\1\61\3\0\1\62";

  private static int [] zzUnpackAction() {
    int [] result = new int[166];
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
    "\0\0\0\53\0\126\0\201\0\254\0\327\0\u0102\0\u012d"+
    "\0\126\0\126\0\126\0\126\0\126\0\u0158\0\u0183\0\u01ae"+
    "\0\u01d9\0\u0204\0\u022f\0\u025a\0\u0285\0\u02b0\0\u02db\0\u0306"+
    "\0\u0331\0\u035c\0\u0387\0\u03b2\0\u03dd\0\u0408\0\u0433\0\u045e"+
    "\0\u0489\0\126\0\126\0\126\0\126\0\126\0\126\0\u04b4"+
    "\0\u04df\0\u050a\0\u0535\0\u0560\0\u058b\0\u05b6\0\u05e1\0\u060c"+
    "\0\u0637\0\u0662\0\u068d\0\u06b8\0\u06e3\0\u070e\0\u0739\0\u0764"+
    "\0\u078f\0\u07ba\0\u07e5\0\u0810\0\u083b\0\u0102\0\u0866\0\u0891"+
    "\0\u08bc\0\u08e7\0\u04df\0\u0102\0\u0102\0\u0912\0\u0102\0\u093d"+
    "\0\u0102\0\u0968\0\u0993\0\u09be\0\u09e9\0\u0102\0\u0a14\0\u0102"+
    "\0\u0102\0\u0102\0\u0a3f\0\u0a6a\0\u0a95\0\u0102\0\u0102\0\u0ac0"+
    "\0\u0aeb\0\u0102\0\u0b16\0\u0b41\0\u0102\0\u0102\0\u0102\0\u0102"+
    "\0\u0b6c\0\u0102\0\u0b97\0\u0bc2\0\u0bed\0\u0c18\0\u0c43\0\u0c6e"+
    "\0\u0c99\0\u0cc4\0\u0cef\0\u0d1a\0\u0d45\0\u0d70\0\u0d9b\0\u0dc6"+
    "\0\u0df1\0\u0e1c\0\u0e47\0\u0e72\0\u0102\0\u0e9d\0\u0102\0\u0ec8"+
    "\0\u0ef3\0\u0102\0\u0f1e\0\u0f49\0\u0f74\0\u0f9f\0\u0fca\0\u0102"+
    "\0\u0ff5\0\u1020\0\u0102\0\u104b\0\u1076\0\u10a1\0\u10cc\0\u10f7"+
    "\0\u1122\0\u114d\0\u1178\0\u11a3\0\u11ce\0\u11f9\0\u1224\0\u124f"+
    "\0\u127a\0\u0102\0\u0102\0\u12a5\0\u12d0\0\u12fb\0\u1326\0\u0102"+
    "\0\u1351\0\u137c\0\u0102\0\u13a7\0\u0102\0\u13d2\0\u0102\0\u13fd"+
    "\0\u0102\0\u0102\0\u1428\0\u1453\0\u147e\0\126";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[166];
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
    "\1\25\1\7\1\26\1\7\1\27\1\30\1\7\1\31"+
    "\1\32\1\33\1\34\2\7\1\35\1\36\1\37\1\40"+
    "\1\41\12\42\1\43\40\42\54\0\1\44\54\0\1\45"+
    "\1\46\1\47\50\0\4\7\1\0\1\7\5\0\12\7"+
    "\1\50\22\7\3\0\4\7\1\0\1\7\5\0\35\7"+
    "\6\0\1\10\1\51\46\0\1\7\1\52\2\7\1\0"+
    "\1\7\5\0\16\7\1\53\16\7\3\0\4\7\1\0"+
    "\1\7\5\0\2\7\1\54\32\7\3\0\4\7\1\0"+
    "\1\7\5\0\4\7\1\55\3\7\1\56\24\7\3\0"+
    "\4\7\1\0\1\7\5\0\2\7\1\57\32\7\3\0"+
    "\4\7\1\0\1\7\5\0\14\7\1\60\20\7\3\0"+
    "\4\7\1\0\1\7\5\0\7\7\1\61\25\7\3\0"+
    "\4\7\1\0\1\7\5\0\12\7\1\62\22\7\3\0"+
    "\4\7\1\0\1\7\5\0\14\7\1\63\20\7\3\0"+
    "\4\7\1\0\1\7\5\0\4\7\1\64\30\7\3\0"+
    "\4\7\1\0\1\7\5\0\16\7\1\65\1\7\1\66"+
    "\14\7\3\0\4\7\1\0\1\7\5\0\4\7\1\67"+
    "\30\7\3\0\4\7\1\0\1\7\5\0\6\7\1\70"+
    "\5\7\1\71\20\7\3\0\4\7\1\0\1\7\5\0"+
    "\6\7\1\72\26\7\3\0\4\7\1\0\1\7\5\0"+
    "\6\7\1\73\26\7\3\0\4\7\1\0\1\7\5\0"+
    "\26\7\1\74\6\7\3\0\4\7\1\0\1\7\5\0"+
    "\26\7\1\75\6\7\3\0\4\7\1\0\1\7\5\0"+
    "\26\7\1\76\6\7\3\0\4\7\1\0\1\7\5\0"+
    "\10\7\1\77\24\7\3\0\4\7\1\0\1\7\5\0"+
    "\14\7\1\100\20\7\3\0\4\7\1\0\1\7\5\0"+
    "\10\7\1\101\24\7\3\0\4\7\1\0\1\7\5\0"+
    "\5\7\1\102\27\7\6\0\1\103\47\0\1\7\1\104"+
    "\2\7\1\0\1\7\5\0\35\7\3\0\1\7\1\105"+
    "\2\7\1\0\1\7\5\0\35\7\3\0\2\7\1\106"+
    "\1\7\1\0\1\7\5\0\7\7\1\107\25\7\3\0"+
    "\4\7\1\0\1\7\5\0\5\7\1\110\27\7\3\0"+
    "\1\7\1\111\2\7\1\0\1\7\5\0\35\7\3\0"+
    "\4\7\1\0\1\7\5\0\16\7\1\112\16\7\3\0"+
    "\4\7\1\0\1\7\5\0\14\7\1\113\20\7\3\0"+
    "\4\7\1\0\1\7\5\0\12\7\1\114\22\7\3\0"+
    "\4\7\1\0\1\7\5\0\5\7\1\115\27\7\3\0"+
    "\4\7\1\0\1\7\5\0\6\7\1\116\26\7\3\0"+
    "\4\7\1\0\1\7\5\0\16\7\1\117\16\7\3\0"+
    "\1\7\1\120\2\7\1\0\1\7\5\0\35\7\3\0"+
    "\4\7\1\0\1\7\5\0\21\7\1\121\1\122\12\7"+
    "\3\0\4\7\1\0\1\7\5\0\12\7\1\123\22\7"+
    "\3\0\4\7\1\0\1\7\5\0\26\7\1\124\6\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\125\26\7"+
    "\3\0\4\7\1\0\1\7\5\0\14\7\1\126\20\7"+
    "\3\0\4\7\1\0\1\7\5\0\14\7\1\127\20\7"+
    "\3\0\4\7\1\0\1\7\5\0\27\7\1\130\5\7"+
    "\3\0\4\7\1\0\1\7\5\0\2\7\1\131\32\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\132\26\7"+
    "\3\0\4\7\1\0\1\7\5\0\6\7\1\133\26\7"+
    "\3\0\4\7\1\0\1\7\5\0\2\7\1\134\32\7"+
    "\3\0\1\135\3\7\1\0\1\7\5\0\35\7\3\0"+
    "\2\7\1\136\1\7\1\0\1\7\5\0\35\7\3\0"+
    "\4\7\1\0\1\7\5\0\6\7\1\137\26\7\3\0"+
    "\4\7\1\0\1\7\5\0\14\7\1\140\20\7\3\0"+
    "\4\7\1\0\1\7\5\0\27\7\1\141\5\7\3\0"+
    "\4\7\1\0\1\7\5\0\5\7\1\142\27\7\3\0"+
    "\4\7\1\0\1\7\5\0\5\7\1\143\27\7\3\0"+
    "\1\144\3\7\1\0\1\7\5\0\35\7\3\0\1\7"+
    "\1\145\2\7\1\0\1\7\5\0\35\7\3\0\4\7"+
    "\1\0\1\7\5\0\2\7\1\146\32\7\3\0\4\7"+
    "\1\0\1\7\5\0\27\7\1\147\5\7\3\0\4\7"+
    "\1\0\1\7\5\0\2\7\1\150\24\7\1\151\5\7"+
    "\3\0\4\7\1\0\1\7\5\0\16\7\1\152\16\7"+
    "\3\0\4\7\1\0\1\7\5\0\27\7\1\153\5\7"+
    "\3\0\4\7\1\0\1\7\5\0\16\7\1\154\16\7"+
    "\3\0\4\7\1\0\1\7\5\0\5\7\1\155\25\7"+
    "\1\156\1\7\3\0\4\7\1\0\1\7\5\0\27\7"+
    "\1\157\5\7\3\0\4\7\1\0\1\7\5\0\6\7"+
    "\1\160\26\7\3\0\4\7\1\0\1\7\5\0\26\7"+
    "\1\161\6\7\3\0\4\7\1\0\1\7\5\0\16\7"+
    "\1\162\16\7\3\0\4\7\1\0\1\7\5\0\5\7"+
    "\1\163\25\7\1\164\1\7\3\0\2\7\1\165\1\7"+
    "\1\0\1\7\5\0\35\7\3\0\4\7\1\0\1\7"+
    "\5\0\2\7\1\166\32\7\3\0\4\7\1\0\1\7"+
    "\5\0\6\7\1\167\26\7\3\0\4\7\1\0\1\7"+
    "\5\0\5\7\1\170\25\7\1\171\1\7\3\0\4\7"+
    "\1\0\1\7\5\0\6\7\1\172\26\7\3\0\4\7"+
    "\1\0\1\7\5\0\10\7\1\173\24\7\3\0\4\7"+
    "\1\0\1\7\5\0\5\7\1\174\27\7\3\0\4\7"+
    "\1\0\1\7\2\0\1\175\2\0\35\7\3\0\4\7"+
    "\1\0\1\7\5\0\2\7\1\176\32\7\3\0\4\7"+
    "\1\0\1\7\5\0\12\7\1\177\22\7\3\0\4\7"+
    "\1\0\1\7\5\0\33\7\1\200\1\7\3\0\4\7"+
    "\1\0\1\7\5\0\10\7\1\201\24\7\3\0\4\7"+
    "\1\0\1\7\5\0\5\7\1\202\27\7\3\0\2\7"+
    "\1\203\1\7\1\0\1\7\5\0\35\7\3\0\4\7"+
    "\1\0\1\7\5\0\10\7\1\204\24\7\3\0\4\7"+
    "\1\0\1\7\5\0\5\7\1\205\27\7\3\0\1\206"+
    "\3\7\1\0\1\7\5\0\35\7\3\0\4\7\1\0"+
    "\1\7\5\0\10\7\1\207\24\7\52\0\1\210\3\0"+
    "\4\7\1\0\1\7\5\0\10\7\1\211\24\7\3\0"+
    "\4\7\1\0\1\7\5\0\6\7\1\212\26\7\3\0"+
    "\1\213\3\7\1\0\1\7\5\0\35\7\3\0\4\7"+
    "\1\0\1\7\5\0\10\7\1\214\24\7\3\0\1\215"+
    "\3\7\1\0\1\7\5\0\35\7\3\0\4\7\1\0"+
    "\1\7\5\0\10\7\1\216\24\7\3\0\4\7\1\0"+
    "\1\7\5\0\12\7\1\217\22\7\3\0\4\7\1\0"+
    "\1\7\5\0\25\7\1\220\7\7\26\0\1\221\27\0"+
    "\4\7\1\0\1\7\5\0\16\7\1\222\16\7\3\0"+
    "\4\7\1\0\1\7\5\0\14\7\1\223\20\7\3\0"+
    "\4\7\1\0\1\7\5\0\12\7\1\224\22\7\3\0"+
    "\4\7\1\0\1\7\5\0\25\7\1\225\7\7\3\0"+
    "\4\7\1\0\1\7\5\0\12\7\1\226\22\7\3\0"+
    "\4\7\1\0\1\7\5\0\25\7\1\227\7\7\3\0"+
    "\4\7\1\0\1\7\5\0\5\7\1\230\27\7\3\0"+
    "\4\7\1\0\1\7\5\0\12\7\1\231\22\7\24\0"+
    "\1\232\31\0\4\7\1\0\1\7\5\0\5\7\1\233"+
    "\27\7\3\0\4\7\1\0\1\7\5\0\12\7\1\234"+
    "\22\7\3\0\4\7\1\0\1\7\5\0\5\7\1\235"+
    "\27\7\3\0\4\7\1\0\1\7\5\0\12\7\1\236"+
    "\22\7\3\0\4\7\1\0\1\7\5\0\5\7\1\237"+
    "\27\7\32\0\1\240\23\0\4\7\1\0\1\7\5\0"+
    "\5\7\1\241\27\7\3\0\4\7\1\0\1\7\5\0"+
    "\5\7\1\242\27\7\34\0\1\243\21\0\1\244\67\0"+
    "\1\245\62\0\1\246\22\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5289];
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
    "\2\0\1\11\5\1\5\11\24\1\6\11\1\1\1\0"+
    "\123\1\1\0\12\1\1\0\10\1\1\0\10\1\1\0"+
    "\5\1\1\0\2\1\3\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[166];
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
        case 51: break;
        case 25: 
          { return new Symbol(sym.End, yyline,yycolumn,yytext());
          }
        case 52: break;
        case 7: 
          { return new Symbol(sym.DPUNTOS, yyline,yycolumn,yytext());
          }
        case 53: break;
        case 15: 
          { return new Symbol(sym.Lt, yyline, yycolumn,yytext());
          }
        case 54: break;
        case 3: 
          { return new Symbol(sym.enter, yyline, yycolumn,yytext());
          }
        case 55: break;
        case 24: 
          { return new Symbol(sym.Ret, yyline,yycolumn,yytext());
          }
        case 56: break;
        case 13: 
          { return new Symbol(sym.D, yyline, yycolumn,yytext());
          }
        case 57: break;
        case 29: 
          { return new Symbol(sym.Gte, yyline,yycolumn,yytext());
          }
        case 58: break;
        case 49: 
          { return new Symbol(sym.get_global, yyline,yycolumn,yytext());
          }
        case 59: break;
        case 31: 
          { return new Symbol(sym.Calc, yyline,yycolumn,yytext());
          }
        case 60: break;
        case 4: 
          { /*Espacios en blanco, ignorados*/
          }
        case 61: break;
        case 33: 
          { return new Symbol(sym.Mul, yyline,yycolumn,yytext());
          }
        case 62: break;
        case 12: 
          { return new Symbol(sym.C, yyline, yycolumn,yytext());
          }
        case 63: break;
        case 37: 
          { return new Symbol(sym.Br_if, yyline,yycolumn,yytext());
          }
        case 64: break;
        case 36: 
          { return new Symbol(sym.Call, yyline,yycolumn,yytext());
          }
        case 65: break;
        case 8: 
          { return new Symbol(sym.MENOS, yyline,yycolumn,yytext());
          }
        case 66: break;
        case 48: 
          { return new Symbol(sym.set_global, yyline,yycolumn,yytext());
          }
        case 67: break;
        case 43: 
          { return new Symbol(sym.quadrate, yyline, yycolumn,yytext());
          }
        case 68: break;
        case 39: 
          { return new Symbol(sym.point, yyline,yycolumn,yytext());
          }
        case 69: break;
        case 38: 
          { return new Symbol(sym.Print, yyline,yycolumn,yytext());
          }
        case 70: break;
        case 10: 
          { yybegin(YYINITIAL);
          }
        case 71: break;
        case 40: 
          { return new Symbol(sym.string, yycolumn,yyline,yytext());
          }
        case 72: break;
        case 41: 
          { return new Symbol(sym.Br__if, yyline,yycolumn,yytext());
          }
        case 73: break;
        case 35: 
          { return new Symbol(sym.oval, yyline,yycolumn,yytext());
          }
        case 74: break;
        case 45: 
          { return new Symbol(sym.set_local, yyline,yycolumn,yytext());
          }
        case 75: break;
        case 26: 
          { return new Symbol(sym.Eqz, yyline,yycolumn,yytext());
          }
        case 76: break;
        case 30: 
          { return new Symbol(sym.Not, yyline,yycolumn,yytext());
          }
        case 77: break;
        case 1: 
          { //Interfaz_Web.listaErrores.insertarError("Error Lexico", yytext(),(yycolumn+1)+"",(yyline+1)+"","El caracter no pertenece al lenguaje",Interfaz_Web.doc_actual);
                    String errLex = "Error léxico : '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+(yycolumn+1);
                    System.out.println(errLex);
          }
        case 78: break;
        case 32: 
          { return new Symbol(sym.Diff, yyline,yycolumn,yytext());
          }
        case 79: break;
        case 42: 
          { return new Symbol(sym.Function, yyline,yycolumn,yytext());
          }
        case 80: break;
        case 28: 
          { return new Symbol(sym.Lte, yyline,yycolumn,yytext());
          }
        case 81: break;
        case 17: 
          { return new Symbol(sym.Br, yyline,yycolumn,yytext());
          }
        case 82: break;
        case 23: 
          { return new Symbol(sym.Mod, yyline,yycolumn,yytext());
          }
        case 83: break;
        case 16: 
          { return new Symbol(sym.Gt, yyline, yycolumn,yytext());
          }
        case 84: break;
        case 5: 
          { /*Saltos de linea, ignorados*/
          }
        case 85: break;
        case 19: 
          { return new Symbol(sym.decim, yyline, yycolumn,yytext());
          }
        case 86: break;
        case 50: 
          { return new Symbol(sym.Pot, yyline,yycolumn,yytext());
          }
        case 87: break;
        case 44: 
          { return new Symbol(sym.tee_local, yyline,yycolumn,yytext());
          }
        case 88: break;
        case 21: 
          { return new Symbol(sym.And, yyline,yycolumn,yytext());
          }
        case 89: break;
        case 22: 
          { return new Symbol(sym.Div, yyline,yycolumn,yytext());
          }
        case 90: break;
        case 2: 
          { return new Symbol(sym.id, yyline, yycolumn,yytext());
          }
        case 91: break;
        case 27: 
          { return new Symbol(sym.Eqs, yyline,yycolumn,yytext());
          }
        case 92: break;
        case 46: 
          { return new Symbol(sym.get_local, yyline,yycolumn,yytext());
          }
        case 93: break;
        case 14: 
          { return new Symbol(sym.F, yyline, yycolumn,yytext());
          }
        case 94: break;
        case 47: 
          { return new Symbol(sym.tee_global, yyline,yycolumn,yytext());
          }
        case 95: break;
        case 34: 
          { return new Symbol(sym.line, yycolumn,yyline,yytext());
          }
        case 96: break;
        case 20: 
          { return new Symbol(sym.Add, yyline,yycolumn,yytext());
          }
        case 97: break;
        case 6: 
          { return new Symbol(sym.DOLAR, yyline,yycolumn,yytext());
          }
        case 98: break;
        case 18: 
          { return new Symbol(sym.Or, yyline,yycolumn,yytext());
          }
        case 99: break;
        case 9: 
          { 
          }
        case 100: break;
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
