//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compiTPE;
import java.util.*;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short ENDIF=263;
public final static short PRINT=264;
public final static short FUNC=265;
public final static short RETURN=266;
public final static short BEGIN=267;
public final static short END=268;
public final static short BREAK=269;
public final static short INT=270;
public final static short SINGLE=271;
public final static short REPEAT=272;
public final static short PRE=273;
public final static short TYPEDEF=274;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    3,    3,    3,    4,    4,    4,
    8,    8,    5,    9,    6,    6,    6,   12,   12,   12,
   12,   12,   12,   10,   10,   10,   10,   10,   10,   11,
   11,   11,   11,   15,   15,   15,    2,    2,   16,   16,
   16,   16,   21,   17,   17,   23,   23,   23,   23,   14,
   14,   14,   24,   24,   24,   24,   24,   24,   22,   22,
   22,    7,    7,   19,   19,   19,   19,   19,   19,   13,
   13,   25,   25,   25,   25,   25,   25,   25,   25,   18,
   18,   18,   20,   27,   27,   27,   26,   26,
};
final static short yylen[] = {                            2,
    6,    2,    1,    2,    1,    1,    1,    3,    3,    2,
    3,    1,    5,    5,   12,   13,    2,    8,    7,    7,
    7,    7,    7,    4,    3,    3,    3,    2,    3,    3,
    2,    2,    2,    1,    3,    2,    1,    2,    1,    1,
    1,    1,    5,    4,    3,    3,    1,    2,    2,    3,
    3,    1,    3,    3,    1,    3,    3,    1,    1,    1,
    2,    1,    1,    8,    7,    7,   10,    9,    9,    1,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    5,
    4,    4,   14,    2,    1,    2,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    0,   62,   63,    0,    0,
    3,    5,    6,    7,    0,   17,   12,    0,    0,    0,
    4,    0,    0,    9,    0,    0,    0,    0,    0,    0,
    0,   37,   39,   40,   41,   42,    0,    8,   11,    0,
    0,    0,   60,    0,    0,    0,    0,   58,   55,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   38,    0,
    0,    0,    0,   13,    0,    0,    0,   61,   48,    0,
    0,   45,    0,    0,    0,   72,   73,   74,   75,   76,
   77,   78,   79,    0,    0,    0,    0,    0,    1,    0,
   28,    0,    0,    0,    0,   59,    0,   44,   46,    0,
    0,   56,   53,   57,   54,    0,    0,    0,    0,   81,
   82,    0,    0,   26,    0,   27,   25,    0,    0,    0,
    0,    0,    0,   34,    0,    0,   80,    0,   24,    0,
    0,    0,   14,   43,   36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   35,    0,   66,    0,    0,
    0,   65,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   64,    0,    0,    0,    0,    0,    0,   32,
    0,    0,   31,    0,   69,    0,   68,   87,   88,    0,
    0,    0,    0,    0,   30,    0,    0,   67,    0,    0,
    0,    0,    0,    0,   15,    0,    0,   20,   21,   22,
    0,   23,   19,   16,    0,    0,    0,   18,   84,   86,
    0,   83,
};
final static short yydgoto[] = {                          3,
   10,   31,   11,   12,   13,   14,   15,   18,   41,   62,
  158,  132,   53,   54,  123,  124,   33,   34,   35,   36,
   48,   49,   50,   51,   85,  154,  207,
};
final static short yysindex[] = {                       -67,
  -23, -184,    0,    0,  -15, -233,    0,    0, -211, -123,
    0,    0,    0,    0, -235,    0,    0,   38,  -12,   90,
    0, -205,   60,    0, -202,   -7,   61,   63,  -35,   31,
 -100,    0,    0,    0,    0,    0,  -13,    0,    0, -177,
   22,   53,    0,   75,   85, -158,  122,    0,    0,   57,
   33,   85,   23,   49,   68, -141, -132,   70,    0,   54,
 -118, -184,  103,    0,   87,  100,  141,    0,    0,   85,
   85,    0,   85,   85,   29,    0,    0,    0,    0,    0,
    0,    0,    0,  -62,   85,  146,   25,  147,    0,  171,
    0,  -29,  177, -121,   -7,    0,  181,    0,    0,   33,
   33,    0,    0,    0,    0,  -79,  -30,  -79,   49,    0,
    0,  167,  -17,    0,  188,    0,    0,  -77,  213,  197,
  202,   90,  -71,    0,  -79,    3,    0,  208,    0,  211,
  -63,   90,    0,    0,    0,  -47,  -79,  217,   94,  -79,
  230,   34,   77,   81,   89,    0,   35,    0,  -79,  289,
   74,    0,   50,  301,    2,   36,   83,  304,  157,   81,
  305,  102,    0,  307,   65,  109,  324,   16,  325,    0,
  189,  104,    0,  311,    0,  312,    0,    0,    0,  332,
  115,  116,  -36,  117,    0,  318,  110,    0,  112,  321,
  322,  323,  -45,  326,    0,  327,  -53,    0,    0,    0,
  328,    0,    0,    0,  329,  -53,  121,    0,    0,    0,
  331,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -101,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,  333,    0,    0,    0,
  -34,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  334,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -28,
   -6,    0,    0,    0,    0,    0,    0,    0,    7,    0,
    0,    0,    0,    0,  -95,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  335,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  123,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  336,   20,   13,    0,    0,    0,   42,  368,    0,    0,
  224,    0,  -32,   14,  -75,    9,    0,    0,    0,    0,
  278,   40,  351,  288,  243,    0,  191,
};
final static int YYTABLESIZE=398;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         59,
   59,   59,   59,   59,   56,   59,   52,  193,   52,   52,
   52,  116,   50,  202,   50,   50,   50,   59,   59,   75,
   59,   17,   21,   17,   52,   52,   60,   52,   32,   22,
   50,   50,  126,   50,   51,    4,   51,   51,   51,   59,
   47,   70,  167,   16,   70,   19,   46,   71,   26,  139,
   71,   37,   51,   51,   39,   51,  183,   47,   67,  182,
   70,  147,   70,   84,  151,  112,   71,   40,   71,  107,
   57,    5,    6,  162,   74,   83,  169,   82,   61,   73,
   64,   25,   83,  111,   82,    7,    8,   63,   83,    9,
   82,   70,   65,   71,   91,   83,   24,   82,  109,   68,
   45,   92,   52,   25,   97,   46,   21,   46,   86,   83,
  156,   82,  103,  105,   45,   72,  155,   87,   38,   46,
  157,   46,  168,  170,   88,   46,   32,   46,   89,   46,
   32,   46,    5,    6,    5,    6,  119,  131,   93,   59,
   32,  136,   95,   20,   59,  118,    7,    8,    7,    8,
    9,  145,    9,   59,   10,   10,   27,  159,   98,   28,
   29,   29,   69,   29,   70,   10,   71,   58,   10,   10,
  171,   30,   10,  159,   29,   29,  121,   27,   29,   27,
   28,   99,   28,   70,   29,   71,   29,  122,    1,    2,
  137,  138,   30,   27,   30,  130,   28,  173,  108,   70,
   29,   71,  144,   27,  110,  206,   28,  113,   30,   27,
   29,  114,   28,  201,  206,  205,   29,  117,   30,   59,
  146,  120,  192,   55,   30,  127,   52,  115,  129,  185,
  125,   70,   50,   71,   59,   59,   59,   59,   59,   59,
  128,   52,   52,   52,   52,   52,   52,   50,   50,   50,
   50,   50,   50,  133,   51,  134,    7,    8,   42,   43,
  135,   70,    7,    8,  140,  141,  142,   71,  143,   51,
   51,   51,   51,   51,   51,  148,   70,   70,   70,   70,
   70,   70,   71,   71,   71,   71,   71,   71,  152,  106,
  153,   76,   77,   78,   79,   80,   81,  161,   76,   77,
   78,   79,   80,   81,   76,   77,   78,   79,   80,   81,
   90,   76,   77,   78,   79,   80,   81,   42,   43,   42,
   43,  178,  179,    7,    8,   76,   77,   78,   79,   80,
   81,   42,   43,   42,   43,   44,  164,   42,   43,   42,
   43,   42,   43,   96,   43,   27,   27,  163,   28,   28,
  102,  104,   29,   29,  160,  149,  150,  100,  101,  166,
   30,   30,  172,  175,  176,  177,  180,  181,  184,  187,
  188,  186,  189,  190,  191,  194,  195,  196,  197,  198,
  199,  200,   23,  174,  203,  204,  208,  209,  211,  212,
   85,   47,   49,   33,   66,  165,  210,   94,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   40,   47,   41,   44,   43,   44,
   45,   41,   41,   59,   43,   44,   45,   59,   60,   52,
   62,  257,   10,  257,   59,   60,   40,   62,   20,  265,
   59,   60,  108,   62,   41,   59,   43,   44,   45,   31,
   27,   41,   41,   59,   44,  257,   45,   41,   61,  125,
   44,  257,   59,   60,  257,   62,   41,   44,   45,   44,
   60,  137,   62,   41,  140,   41,   60,   26,   62,   41,
   40,  256,  257,  149,   42,   60,   41,   62,   37,   47,
   59,   44,   60,   59,   62,  270,  271,  265,   60,  274,
   62,   43,   40,   45,   41,   60,   59,   62,   85,  258,
   40,   60,   40,   44,   65,   45,   94,   45,   41,   60,
  143,   62,   73,   74,   40,   59,   40,  259,   59,   45,
   40,   45,  155,   41,  257,   45,  118,   45,   59,   45,
  122,   45,  256,  257,  256,  257,   95,  118,  257,  131,
  132,  122,   40,  267,  136,  267,  270,  271,  270,  271,
  274,  132,  274,  145,  256,  257,  257,  144,   59,  260,
  256,  257,   41,  264,   43,  267,   45,  268,  270,  271,
  157,  272,  274,  160,  270,  271,  256,  257,  274,  257,
  260,   41,  260,   43,  264,   45,  264,  267,  256,  257,
  262,  263,  272,  257,  272,  273,  260,   41,  261,   43,
  264,   45,  266,  257,   59,  197,  260,   61,  272,  257,
  264,   41,  260,  259,  206,  269,  264,   41,  272,  261,
  268,   41,  259,  259,  272,   59,  261,  257,   41,   41,
  261,   43,  261,   45,  276,  277,  278,  279,  280,  281,
  258,  276,  277,  278,  279,  280,  281,  276,  277,  278,
  279,  280,  281,   41,  261,   59,  270,  271,  257,  258,
   59,  261,  270,  271,  262,  263,   59,  261,   58,  276,
  277,  278,  279,  280,  281,   59,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,   59,  261,
  257,  276,  277,  278,  279,  280,  281,  263,  276,  277,
  278,  279,  280,  281,  276,  277,  278,  279,  280,  281,
  257,  276,  277,  278,  279,  280,  281,  257,  258,  257,
  258,  257,  258,  270,  271,  276,  277,  278,  279,  280,
  281,  257,  258,  257,  258,  275,  263,  257,  258,  257,
  258,  257,  258,  257,  258,  257,  257,   59,  260,  260,
   73,   74,  264,  264,  266,  262,  263,   70,   71,   59,
  272,  272,   59,   59,  263,   59,  258,   44,   44,   59,
   59,  268,   41,  259,  259,  259,   59,  268,  267,   59,
   59,   59,   15,  160,   59,   59,   59,   59,  268,   59,
  268,   59,   59,   59,   44,  153,  206,   62,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"ID","CTE","CADENA","IF","THEN","ELSE","ENDIF",
"PRINT","FUNC","RETURN","BEGIN","END","BREAK","INT","SINGLE","REPEAT","PRE",
"TYPEDEF","\":=\"","\"||\"","\"&&\"","\"<>\"","\"==\"","\"<=\"","\">=\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';'",
"programa : error ';'",
"bloque_sentencias_declarativas : sentencia_declarativa",
"bloque_sentencias_declarativas : bloque_sentencias_declarativas sentencia_declarativa",
"sentencia_declarativa : declaracionDatos",
"sentencia_declarativa : declaracionNuevoTipo",
"sentencia_declarativa : declaracionFuncion",
"declaracionDatos : tipo conjunto_declaracion_variables ';'",
"declaracionDatos : ID conjunto_declaracion_variables ';'",
"declaracionDatos : tipo conjunto_declaracion_variables",
"conjunto_declaracion_variables : conjunto_declaracion_variables ',' ID",
"conjunto_declaracion_variables : ID",
"declaracionNuevoTipo : TYPEDEF ID '=' encabezado_funcion_typedef ';'",
"encabezado_funcion_typedef : tipo FUNC '(' tipo ')'",
"declaracionFuncion : tipo FUNC ID parametro bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
"declaracionFuncion : tipo FUNC ID parametro bloque_sentencias_declarativas BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
"declaracionFuncion : error ';'",
"pre_condicion : PRE ':' '(' condicion ')' ',' CADENA ';'",
"pre_condicion : PRE ':' condicion ')' ',' CADENA ';'",
"pre_condicion : PRE ':' '(' ')' ',' CADENA ';'",
"pre_condicion : PRE ':' '(' condicion ',' CADENA ';'",
"pre_condicion : PRE ':' '(' condicion ')' CADENA ';'",
"pre_condicion : PRE ':' '(' condicion ')' ',' ';'",
"parametro : '(' tipo ID ')'",
"parametro : tipo ID ')'",
"parametro : '(' ID ')'",
"parametro : '(' tipo ')'",
"parametro : '(' ')'",
"parametro : '(' tipo ID",
"retorno : '(' expresion ')'",
"retorno : expresion ')'",
"retorno : '(' ')'",
"retorno : '(' expresion",
"bloque_sentencias_ejecutables : sentencia_ejecutable",
"bloque_sentencias_ejecutables : BEGIN conjunto_sentencia_ejecutable END",
"bloque_sentencias_ejecutables : error ';'",
"conjunto_sentencia_ejecutable : sentencia_ejecutable",
"conjunto_sentencia_ejecutable : conjunto_sentencia_ejecutable sentencia_ejecutable",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : mensaje_pantalla",
"sentencia_ejecutable : clausula_seleccion_if",
"sentencia_ejecutable : sentencia_control_repeat",
"invocacion_funcion : ID '(' factor ')' ';'",
"asignacion : ID \":=\" expresioncompuesta ';'",
"asignacion : ID expresioncompuesta ';'",
"expresioncompuesta : '(' expresion ')'",
"expresioncompuesta : expresion",
"expresioncompuesta : expresion ')'",
"expresioncompuesta : '(' expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '/' factor",
"termino : termino '*' factor",
"termino : factor",
"termino : termino '/' invocacion_funcion",
"termino : termino '*' invocacion_funcion",
"termino : invocacion_funcion",
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"tipo : INT",
"tipo : SINGLE",
"clausula_seleccion_if : IF '(' condicion ')' THEN bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF condicion ')' THEN bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF '(' condicion THEN bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF '(' condicion ')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF condicion ')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF '(' condicion THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'",
"condicion : expresion",
"condicion : condicion operador_logico expresion",
"operador_logico : \"||\"",
"operador_logico : \"&&\"",
"operador_logico : \"<>\"",
"operador_logico : \"==\"",
"operador_logico : \"<=\"",
"operador_logico : \">=\"",
"operador_logico : '>'",
"operador_logico : '<'",
"mensaje_pantalla : PRINT '(' CADENA ')' ';'",
"mensaje_pantalla : PRINT CADENA ')' ';'",
"mensaje_pantalla : PRINT '(' CADENA ';'",
"sentencia_control_repeat : REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencias_repeat END ';'",
"conjunto_sentencias_repeat : BREAK ';'",
"conjunto_sentencias_repeat : sentencia_ejecutable",
"conjunto_sentencias_repeat : sentencia_ejecutable conjunto_sentencias_repeat",
"condicion_repeat : ID operador_logico ID",
"condicion_repeat : ID operador_logico CTE",
};

//#line 163 "gramatica.y"
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	
	public Parser(AnalizadorLexico a_lex)
	{
		this.analizadorLexico = a_lex;
		this.erroresSint = new ArrayList<String>();
	}
	
	public void yyerror(String error)
	{
		System.out.println(error);
	}
//#line 434 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = analizadorLexico.yylex(yylval);  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 15 "gramatica.y"
{System.out.println("Sentencia START programa");}
break;
case 2:
//#line 16 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
break;
case 8:
//#line 29 "gramatica.y"
{System.out.println("Declaracion de datos");}
break;
case 9:
//#line 30 "gramatica.y"
{System.out.println("Declaracion de datos TYPEDEF");}
break;
case 10:
//#line 31 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
break;
case 13:
//#line 38 "gramatica.y"
{System.out.println("Declaracion TYPEDEF");}
break;
case 15:
//#line 44 "gramatica.y"
{System.out.println("DECLARACION FUNCION");}
break;
case 16:
//#line 45 "gramatica.y"
{System.out.println("DECLARACION FUNCION Y PRE CONDICION");}
break;
case 17:
//#line 46 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en la declaracion de la funcion");}
break;
case 18:
//#line 49 "gramatica.y"
{System.out.println("pre-condicion");}
break;
case 19:
//#line 50 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("pre-condicion");}
break;
case 20:
//#line 51 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); System.out.println("pre-condicion");}
break;
case 21:
//#line 52 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("pre-condicion");}
break;
case 22:
//#line 53 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); System.out.println("pre-condicion");}
break;
case 23:
//#line 54 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); System.out.println("pre-condicion");}
break;
case 25:
//#line 58 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 26:
//#line 59 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 27:
//#line 60 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 28:
//#line 61 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 29:
//#line 62 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 31:
//#line 66 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 32:
//#line 67 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
break;
case 33:
//#line 68 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 35:
//#line 72 "gramatica.y"
{System.out.println("bloque de sentencias BEGIN-END");}
break;
case 36:
//#line 73 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en bloque de sentencias ejecutables");}
break;
case 44:
//#line 89 "gramatica.y"
{System.out.println("Asignacion");}
break;
case 45:
//#line 90 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); System.out.println("Asignacion");}
break;
case 48:
//#line 95 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 49:
//#line 96 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 59:
//#line 112 "gramatica.y"
{System.out.println("factor ID");}
break;
case 60:
//#line 113 "gramatica.y"
{System.out.println("Factor CTE");}
break;
case 62:
//#line 118 "gramatica.y"
{System.out.println("tipo INT");}
break;
case 63:
//#line 119 "gramatica.y"
{System.out.println("tipo SINGLE");}
break;
case 64:
//#line 122 "gramatica.y"
{System.out.println("clausula IF");}
break;
case 65:
//#line 123 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF");}
break;
case 66:
//#line 124 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("clausula IF");}
break;
case 67:
//#line 125 "gramatica.y"
{System.out.println("clausula IF-ELSE");}
break;
case 68:
//#line 126 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF-ELSE");}
break;
case 69:
//#line 127 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");System.out.println("clausula IF-ELSE");}
break;
case 80:
//#line 145 "gramatica.y"
{System.out.println("clausula PRINT");}
break;
case 81:
//#line 146 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); System.out.println("clausula PRINT");}
break;
case 82:
//#line 147 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); System.out.println("clausula PRINT");}
break;
case 83:
//#line 150 "gramatica.y"
{System.out.println("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 87:
//#line 158 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
case 88:
//#line 159 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
//#line 763 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = analizadorLexico.yylex(yylval);        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
