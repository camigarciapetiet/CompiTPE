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
    8,    8,    5,    9,    6,    6,   12,   12,   12,   12,
   12,   12,   10,   10,   10,   10,   10,   10,   11,   11,
   11,   11,   15,   15,   15,    2,    2,   16,   16,   16,
   16,   21,   17,   17,   17,   23,   23,   23,   23,   14,
   14,   14,   24,   24,   24,   24,   24,   24,   22,   22,
   22,    7,    7,   19,   19,   19,   19,   19,   19,   19,
   19,   19,   19,   13,   13,   25,   25,   25,   25,   25,
   25,   25,   25,   18,   18,   18,   20,   27,   27,   27,
   26,   26,
};
final static short yylen[] = {                            2,
    6,    2,    1,    2,    1,    1,    1,    3,    3,    2,
    3,    1,    5,    5,   12,   13,    8,    7,    7,    7,
    7,    7,    4,    3,    3,    3,    2,    3,    3,    2,
    2,    2,    1,    4,    2,    1,    2,    1,    1,    1,
    1,    5,    4,    3,    3,    3,    1,    2,    2,    3,
    3,    1,    3,    3,    1,    3,    3,    1,    1,    1,
    2,    1,    1,    8,    7,    7,   10,    9,    9,    7,
    9,    5,    7,    1,    3,    1,    1,    1,    1,    1,
    1,    1,    1,    5,    4,    4,   14,    2,    1,    2,
    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,   62,   63,    0,    0,    3,
    5,    6,    7,    0,   12,    0,    0,    0,    4,    0,
    0,    9,    0,    0,    0,    0,    0,    0,    0,   36,
   38,   39,   40,   41,    0,    8,   11,    0,    0,    0,
   60,    0,    0,    0,    0,   58,   55,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   37,    0,    0,
    0,    0,   13,    0,   45,    0,    0,   61,   48,    0,
    0,   44,    0,    0,    0,    0,    0,   33,    0,   76,
   77,   78,   79,   80,   81,   82,   83,    0,    0,    0,
    0,    0,    1,    0,   27,    0,    0,    0,    0,   59,
    0,   43,   46,    0,    0,   56,   53,   57,   54,   35,
    0,    0,    0,    0,    0,    0,    0,   85,   86,    0,
    0,   25,    0,   26,   24,    0,    0,    0,    0,    0,
   72,    0,    0,    0,    0,   84,    0,   23,    0,    0,
    0,   14,   42,   34,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   73,    0,   66,    0,
    0,    0,   70,    0,   65,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   64,    0,    0,    0,    0,
    0,    0,    0,   31,    0,    0,   30,    0,   69,    0,
   71,   68,   91,   92,    0,    0,    0,    0,    0,   29,
    0,    0,   67,    0,    0,    0,    0,    0,    0,   15,
    0,    0,   19,   20,   21,    0,   22,   18,   16,    0,
    0,    0,   17,   88,   90,    0,   87,
};
final static short yydgoto[] = {                          3,
    9,   29,   10,   11,   12,   13,   14,   16,   39,   61,
  171,  141,   52,   53,   77,   78,   31,   32,   33,   34,
   46,   47,   48,   49,   89,  167,  222,
};
final static short yysindex[] = {                      -139,
  -30, -125,    0,    0, -224,    0,    0, -221, -199,    0,
    0,    0,    0, -235,    0,   15,  -15,  109,    0, -208,
   48,    0, -205, -132,   61,   76,  -35,   42,  -51,    0,
    0,    0,    0,    0,  -13,    0,    0, -200,   43,   59,
    0,   63,   81, -170,  135,    0,    0,   46,   39, -100,
   81,   23,   91,   70, -135, -126,   89,    0,   54,  -84,
 -125,  141,    0,   90,    0,  116,  154,    0,    0,   81,
   81,    0,   81,   81,  132,  109, -104,    0,   29,    0,
    0,    0,    0,    0,    0,    0,    0,  -67,   81,  144,
   25,  146,    0,  170,    0,  -29,  175, -177, -132,    0,
  178,    0,    0,   39,   39,    0,    0,    0,    0,    0,
  -42, -100,  166, -100,  -74, -100,   91,    0,    0,  182,
  -24,    0,  188,    0,    0,  -68,  213,  202,  210,   13,
    0,  -61, -100,  -31,    4,    0,  230,    0,  233,   92,
  109,    0,    0,    0,  239, -100,  273,   60, -100,  276,
 -100,  285,   94,   83,   85,   93,    0,   98,    0, -100,
  303,  105,    0,  107,    0,   50,  312,    2,   36,   88,
  318,  167,   85,  320,  117,    0,  323,  324,  -95,  126,
  341,   16,  342,    0,  333,  119,    0,  329,    0,  330,
    0,    0,    0,    0,  349,  133,  134,  -36,  136,    0,
  332,  128,    0,  127,  338,  339,  340,  -45,  343,    0,
  344,  103,    0,    0,    0,  345,    0,    0,    0,  346,
  103,  138,    0,    0,    0,  348,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -120,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,  350,    0,    0,    0,  -34,    0,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  351,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -28,   -6,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    7,    0,    0,    0,
    0,    0, -105,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  352,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  140,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  353,  -26,   11,    0,    0,    0,   20,  386,    0,    0,
  228,    0,  -27,   -2,   28,   44,    0,    0,    0,    0,
  190,   40,  370,  284,  247,    0,  194,
};
final static int YYTABLESIZE=415;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         59,
   59,   59,   59,   59,   55,   59,   52,  208,   52,   52,
   52,  124,   50,  217,   50,   50,   50,   59,   59,   19,
   59,   15,   45,   79,   52,   52,   59,   52,    4,   20,
   50,   50,   15,   50,   51,   17,   51,   51,   51,   45,
   67,   74,  181,   38,   74,   24,   44,   75,   35,  111,
   75,   37,   51,   51,   60,   51,  198,    5,   23,  197,
   74,   30,   74,   88,   62,  120,   75,   18,   75,  115,
    6,    7,   58,   22,    8,   87,  183,   86,   96,    5,
   74,   56,   87,  119,   86,   73,  117,   68,   87,  126,
   86,   23,    6,    7,   95,   87,    8,   86,   64,  140,
   43,   63,   43,  101,   72,   44,   36,   44,   19,   87,
   90,   86,  107,  109,  156,   51,    1,    2,  127,   30,
   44,   65,  168,   91,  170,   44,  169,   44,  184,   44,
   92,    5,   44,   70,   44,   71,   10,    6,    7,  130,
  182,  132,  134,  135,    6,    7,   10,   93,    8,   10,
   10,   28,  172,   10,   58,   75,   25,  112,  113,   26,
  148,  193,  194,   27,   28,   28,   76,  185,   28,   30,
  172,   28,   97,  158,  102,   69,  162,   70,  164,   71,
   99,   75,   25,   58,   30,   26,  133,  175,   25,   27,
  110,   26,   76,  116,  103,   27,   70,   28,   71,   58,
  146,  147,  118,   28,  139,   25,  121,  187,   26,   70,
  122,   71,   27,  216,   25,  125,   57,   26,  128,   59,
   28,   27,  207,   54,  131,  129,   52,  123,  138,   28,
  149,  150,   50,  137,   59,   59,   59,   59,   59,   59,
  136,   52,   52,   52,   52,   52,   52,   50,   50,   50,
   50,   50,   50,  142,   51,  221,    6,    7,   40,   41,
  143,   74,  106,  108,  221,  151,  152,   75,  144,   51,
   51,   51,   51,   51,   51,  145,   74,   74,   74,   74,
   74,   74,   75,   75,   75,   75,   75,   75,  153,  114,
  154,   80,   81,   82,   83,   84,   85,  157,   80,   81,
   82,   83,   84,   85,   80,   81,   82,   83,   84,   85,
   94,   80,   81,   82,   83,   84,   85,   40,   41,   40,
   41,  160,  161,    6,    7,   80,   81,   82,   83,   84,
   85,  159,   40,   41,  163,   42,   50,   40,   41,   40,
   41,   40,   41,  165,   40,   41,  100,   41,   25,   25,
  166,   26,   26,  104,  105,   27,   27,  155,  173,   25,
  174,  176,   26,   28,   28,   25,   27,  177,   26,  178,
  180,  220,   27,  200,   28,   70,  186,   71,  189,  190,
   28,  191,  192,  195,  196,  199,  201,  202,  203,  204,
  210,  205,  206,  212,  209,  211,  213,  214,  215,   21,
  188,  218,  219,  223,  224,  226,  227,   89,   47,   49,
   32,   66,  179,   98,  225,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   40,   47,   41,   44,   43,   44,
   45,   41,   41,   59,   43,   44,   45,   59,   60,    9,
   62,  257,   25,   51,   59,   60,   40,   62,   59,  265,
   59,   60,  257,   62,   41,  257,   43,   44,   45,   42,
   43,   41,   41,   24,   44,   61,   45,   41,  257,   76,
   44,  257,   59,   60,   35,   62,   41,  257,   44,   44,
   60,   18,   62,   41,  265,   41,   60,  267,   62,   41,
  270,  271,   29,   59,  274,   60,   41,   62,   59,  257,
   42,   40,   60,   59,   62,   47,   89,  258,   60,  267,
   62,   44,  270,  271,   41,   60,  274,   62,   40,  126,
   40,   59,   40,   64,   59,   45,   59,   45,   98,   60,
   41,   62,   73,   74,  141,   40,  256,  257,   99,   76,
   45,   59,   40,  259,   40,   45,  154,   45,   41,   45,
  257,  257,   45,   43,   45,   45,  257,  270,  271,  112,
  168,  114,  115,  116,  270,  271,  267,   59,  274,  270,
  271,  257,  155,  274,  111,  256,  257,  262,  263,  260,
  133,  257,  258,  264,  270,  271,  267,  170,  274,  126,
  173,  272,  257,  146,   59,   41,  149,   43,  151,   45,
   40,  256,  257,  140,  141,  260,  261,  160,  257,  264,
   59,  260,  267,  261,   41,  264,   43,  272,   45,  156,
  262,  263,   59,  272,  273,  257,   61,   41,  260,   43,
   41,   45,  264,  259,  257,   41,  268,  260,   41,  261,
  272,  264,  259,  259,   59,  268,  261,  257,   41,  272,
  262,  263,  261,  258,  276,  277,  278,  279,  280,  281,
   59,  276,  277,  278,  279,  280,  281,  276,  277,  278,
  279,  280,  281,   41,  261,  212,  270,  271,  257,  258,
   59,  261,   73,   74,  221,  262,  263,  261,   59,  276,
  277,  278,  279,  280,  281,  263,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,   59,  261,
   58,  276,  277,  278,  279,  280,  281,   59,  276,  277,
  278,  279,  280,  281,  276,  277,  278,  279,  280,  281,
  257,  276,  277,  278,  279,  280,  281,  257,  258,  257,
  258,  262,  263,  270,  271,  276,  277,  278,  279,  280,
  281,   59,  257,  258,   59,  275,  261,  257,  258,  257,
  258,  257,  258,   59,  257,  258,  257,  258,  257,  257,
  257,  260,  260,   70,   71,  264,  264,  266,  266,  257,
  263,   59,  260,  272,  272,  257,  264,  263,  260,  263,
   59,  269,  264,   41,  272,   43,   59,   45,   59,  263,
  272,   59,   59,  258,   44,   44,  268,   59,   59,   41,
   59,  259,  259,  267,  259,  268,   59,   59,   59,   14,
  173,   59,   59,   59,   59,  268,   59,  268,   59,   59,
   59,   42,  166,   61,  221,
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
"bloque_sentencias_ejecutables : BEGIN conjunto_sentencia_ejecutable END ';'",
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
"asignacion : ID \":=\" ';'",
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
"clausula_seleccion_if : IF '(' condicion ')' bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF '(' condicion ')' bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF THEN bloque_sentencias_ejecutables ENDIF ';'",
"clausula_seleccion_if : IF THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'",
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

//#line 167 "gramatica.y"
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	
	public Parser(AnalizadorLexico a_lex)
	{
		this.analizadorLexico = a_lex;
		this.erroresSint = new ArrayList<String>();
		this.reglas = new ArrayList<String>();
	}
	
	public void yyerror(String error)
	{
		System.out.println(error);
	}
//#line 449 "Parser.java"
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
{this.reglas.add("Sentencia START programa");}
break;
case 2:
//#line 16 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
break;
case 8:
//#line 29 "gramatica.y"
{this.reglas.add("Declaracion de datos");}
break;
case 9:
//#line 30 "gramatica.y"
{this.reglas.add("Declaracion de datos TYPEDEF");}
break;
case 10:
//#line 31 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
break;
case 13:
//#line 38 "gramatica.y"
{this.reglas.add("Declaracion TYPEDEF");}
break;
case 15:
//#line 44 "gramatica.y"
{this.reglas.add("DECLARACION FUNCION");}
break;
case 16:
//#line 45 "gramatica.y"
{this.reglas.add("DECLARACION FUNCION Y PRE CONDICION");}
break;
case 17:
//#line 48 "gramatica.y"
{this.reglas.add("pre-condicion");}
break;
case 18:
//#line 49 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("pre-condicion");}
break;
case 19:
//#line 50 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); this.reglas.add("pre-condicion");}
break;
case 20:
//#line 51 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("pre-condicion");}
break;
case 21:
//#line 52 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); this.reglas.add("pre-condicion");}
break;
case 22:
//#line 53 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); this.reglas.add("pre-condicion");}
break;
case 24:
//#line 57 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 25:
//#line 58 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 26:
//#line 59 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 27:
//#line 60 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 28:
//#line 61 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 30:
//#line 65 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 31:
//#line 66 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
break;
case 32:
//#line 67 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 34:
//#line 71 "gramatica.y"
{this.reglas.add("bloque de sentencias BEGIN-END");}
break;
case 35:
//#line 72 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en bloque de sentencias ejecutables");}
break;
case 43:
//#line 88 "gramatica.y"
{this.reglas.add("Asignacion");}
break;
case 44:
//#line 89 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion");}
break;
case 45:
//#line 90 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'variable o constante faltante"); this.reglas.add("Asignacion");}
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
{this.reglas.add("factor ID");}
break;
case 60:
//#line 113 "gramatica.y"
{this.reglas.add("Factor CTE");}
break;
case 62:
//#line 117 "gramatica.y"
{this.reglas.add("tipo INT");}
break;
case 63:
//#line 118 "gramatica.y"
{this.reglas.add("tipo SINGLE");}
break;
case 64:
//#line 121 "gramatica.y"
{this.reglas.add("clausula IF");}
break;
case 65:
//#line 122 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF");}
break;
case 66:
//#line 123 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("clausula IF");}
break;
case 67:
//#line 124 "gramatica.y"
{this.reglas.add("clausula IF-ELSE");}
break;
case 68:
//#line 125 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF-ELSE");}
break;
case 69:
//#line 126 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");this.reglas.add("clausula IF-ELSE");}
break;
case 70:
//#line 127 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado");  this.reglas.add("clausula IF");}
break;
case 71:
//#line 128 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); this.reglas.add("clausula IF-ELSE");}
break;
case 72:
//#line 129 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante");  this.reglas.add("clausula IF");}
break;
case 73:
//#line 130 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante"); this.reglas.add("clausula IF-ELSE");}
break;
case 84:
//#line 149 "gramatica.y"
{this.reglas.add("clausula PRINT");}
break;
case 85:
//#line 150 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); this.reglas.add("clausula PRINT");}
break;
case 86:
//#line 151 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); this.reglas.add("clausula PRINT");}
break;
case 87:
//#line 154 "gramatica.y"
{this.reglas.add("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 91:
//#line 162 "gramatica.y"
{this.reglas.add("Condicion_Repeat");}
break;
case 92:
//#line 163 "gramatica.y"
{this.reglas.add("Condicion_Repeat");}
break;
//#line 794 "Parser.java"
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
