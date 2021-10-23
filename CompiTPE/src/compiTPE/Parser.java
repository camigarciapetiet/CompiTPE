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
ParserVal yylval;//the 'lval' (result) I got from analizadorLexico.yylex(yylval)
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
   11,   11,   11,   15,   15,    2,    2,   16,   16,   16,
   16,   21,   17,   17,   23,   23,   23,   23,   14,   14,
   14,   24,   24,   24,   24,   24,   24,   22,   22,   22,
    7,    7,   19,   19,   19,   19,   19,   19,   19,   13,
   13,   25,   25,   25,   25,   25,   25,   25,   25,   18,
   18,   18,   20,   27,   27,   27,   26,   26,
};
final static short yylen[] = {                            2,
    6,    2,    1,    2,    1,    1,    1,    3,    3,    2,
    3,    1,    5,    5,   12,   13,    2,    8,    7,    7,
    7,    7,    7,    4,    3,    3,    3,    2,    3,    3,
    2,    2,    2,    1,    3,    1,    2,    1,    1,    1,
    1,    5,    4,    3,    3,    1,    2,    2,    3,    3,
    1,    3,    3,    1,    3,    3,    1,    1,    1,    2,
    1,    1,    8,    7,    7,   10,    9,    9,    2,    1,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    5,
    4,    4,   14,    2,    1,    2,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    0,   61,   62,    0,    0,
    3,    5,    6,    7,    0,   17,   12,    0,    0,    0,
    4,    0,    0,    9,    0,    0,    0,    0,    0,    0,
    0,    0,   36,   38,   39,   40,   41,    0,    8,   11,
    0,    0,   69,    0,   59,    0,    0,    0,    0,   57,
   54,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   37,    0,    0,    0,    0,   13,    0,    0,    0,   60,
   47,    0,    0,   44,    0,    0,    0,   72,   73,   74,
   75,   76,   77,   78,   79,    0,    0,    0,    0,    0,
    1,    0,   28,    0,    0,    0,    0,   58,    0,   43,
   45,    0,    0,   55,   52,   56,   53,    0,    0,    0,
    0,   81,   82,    0,    0,   26,    0,   27,   25,    0,
    0,    0,    0,    0,   34,    0,    0,   80,    0,   24,
    0,    0,    0,   14,   42,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   35,    0,   65,    0,    0,
    0,   64,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   63,    0,    0,    0,    0,    0,    0,   32,
    0,    0,   31,    0,   68,    0,   67,   87,   88,    0,
    0,    0,    0,    0,   30,    0,    0,   66,    0,    0,
    0,    0,    0,    0,   15,    0,    0,   20,   21,   22,
    0,   23,   19,   16,    0,   85,    0,   18,   84,    0,
   86,   83,
};
final static short yydgoto[] = {                          3,
   10,   32,   11,   12,   13,   14,   15,   18,   42,   64,
  158,  133,   55,   56,  124,  125,   34,   35,   36,   37,
   50,   51,   52,   53,   87,  154,  207,
};
final static short yysindex[] = {                      -152,
  -39, -116,    0,    0,  -10, -233,    0,    0, -227, -177,
    0,    0,    0,    0, -213,    0,    0,   22,   27,  125,
    0, -202,   28,    0, -195, -128,   15,   61,   63,  -35,
   52,  -76,    0,    0,    0,    0,    0,  -13,    0,    0,
 -158,   59,    0,   91,    0,   75,   85, -131,   30,    0,
    0,   80,   72,   85,   23,   39,  104, -107, -100,  102,
    0,   54,  -86, -116,  151,    0,   87,  138,  144,    0,
    0,   85,   85,    0,   85,   85,   29,    0,    0,    0,
    0,    0,    0,    0,    0,  -60,   85,  146,    9,  150,
    0,  172,    0,  -29,  180, -123, -128,    0,  184,    0,
    0,   72,   72,    0,    0,    0,    0,  -74,  -20,  -74,
   39,    0,    0,  170,   -2,    0,  220,    0,    0,  -94,
  222,  205,  125,  -98,    0,  -74,  -95,    0,  206,    0,
  209,  -57,  125,    0,    0,  -38,  -74,  210,  -68,  -74,
  217,   32,   77,   81,   92,    0,   35,    0,  -74,  232,
   60,    0,   50,  263,    2,   36,   83,  278,  161,   81,
  287,   84,    0,  291,  -82,   93,  309,   16,  311,    0,
  167,   95,    0,  300,    0,  301,    0,    0,    0,  327,
  112,  119,  -36,  124,    0,  316,  118,    0,  120,  331,
  332,  333,  -45,  334,    0,  335,  105,    0,    0,    0,
  336,    0,    0,    0,  337,    0,  116,    0,    0,  339,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -121,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,    0,    0,    0,  340,    0,
    0,    0,  -34,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  341,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -28,   -6,    0,    0,    0,    0,    0,    0,    0,
    7,    0,    0,    0,    0,    0,  -97,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  342,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  338,  -87,   13,    0,    0,    0,    3,  388,    0,    0,
  244,    0,  -32,   12,  -24,  234,    0,    0,    0,    0,
  141,   62,  359,  159,  253,    0,    0,
};
final static int YYTABLESIZE=441;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   58,   58,   58,   58,   58,   58,   51,  193,   51,   51,
   51,  118,   49,  202,   49,   49,   49,   58,   58,    4,
   58,   77,   21,   17,   51,   51,   62,   51,   41,   19,
   49,   49,  132,   49,   50,  136,   50,   50,   50,   49,
   63,   70,  167,   17,   70,  145,   48,   71,   16,  114,
   71,   22,   50,   50,   38,   50,  183,   49,   69,  182,
   70,   40,   70,   86,   94,   25,   71,  113,   71,  109,
   71,   25,   72,   43,   73,   85,  169,   84,    5,    6,
   24,   72,   85,   73,   84,  127,   39,   26,   85,   20,
   84,   59,    7,    8,   93,   85,    9,   84,  111,  121,
   47,  139,   54,    1,    2,   48,   65,   48,   21,   85,
  156,   84,  147,   76,   47,  151,  155,   66,   75,   48,
  157,   48,  168,  170,  162,   48,   70,   48,   99,   48,
   67,   48,    5,    6,   10,   10,  105,  107,   74,    5,
    6,    7,    8,  120,   88,   10,    7,    8,   10,   10,
    9,   89,   10,    7,    8,  159,   90,    9,   29,   29,
   91,   27,   28,  137,  138,   29,  140,  141,  171,   30,
   95,  159,   29,   29,  178,  179,   29,   31,  131,   27,
   28,   27,   28,   29,  101,   29,   72,   30,   73,   30,
   97,   60,  123,  149,  150,   31,  100,   31,   27,   28,
  110,  173,   29,   72,  112,   73,   30,  185,  144,   72,
  115,   73,  116,  201,   31,  104,  106,   27,   28,   58,
  119,   29,  192,   57,  122,   30,   51,  117,  128,  146,
  102,  103,   49,   31,   58,   58,   58,   58,   58,   58,
  126,   51,   51,   51,   51,   51,   51,   49,   49,   49,
   49,   49,   49,   33,   50,  129,    7,    8,   44,   45,
  130,   70,  134,  135,  142,   61,  143,   71,  148,   50,
   50,   50,   50,   50,   50,  152,   70,   70,   70,   70,
   70,   70,   71,   71,   71,   71,   71,   71,  153,  108,
  163,   78,   79,   80,   81,   82,   83,  161,   78,   79,
   80,   81,   82,   83,   78,   79,   80,   81,   82,   83,
   92,   78,   79,   80,   81,   82,   83,   44,   45,   44,
   45,  166,  164,    7,    8,   78,   79,   80,   81,   82,
   83,   44,   45,   44,   45,   46,  172,   44,   45,   44,
   45,   44,   45,   98,   45,  175,  176,   27,   28,  177,
  180,   29,  181,   33,  184,   30,   33,  160,  187,  188,
   27,   28,  186,   31,   29,   61,   33,  189,   30,   61,
  190,   27,   28,  205,  195,   29,   31,  191,   61,   30,
   27,   28,  194,  210,   29,  196,  197,   31,   30,  198,
  199,  200,  203,  204,  208,  209,   31,  212,   46,   48,
   33,   96,   23,  174,   68,  165,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  206,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  211,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   40,   47,   41,   44,   43,   44,
   45,   41,   41,   59,   43,   44,   45,   59,   60,   59,
   62,   54,   10,  257,   59,   60,   40,   62,   26,  257,
   59,   60,  120,   62,   41,  123,   43,   44,   45,   28,
   38,   41,   41,  257,   44,  133,   45,   41,   59,   41,
   44,  265,   59,   60,  257,   62,   41,   46,   47,   44,
   60,  257,   62,   41,   62,   44,   60,   59,   62,   41,
   41,   44,   43,   59,   45,   60,   41,   62,  256,  257,
   59,   43,   60,   45,   62,  110,   59,   61,   60,  267,
   62,   40,  270,  271,   41,   60,  274,   62,   87,   97,
   40,  126,   40,  256,  257,   45,  265,   45,   96,   60,
  143,   62,  137,   42,   40,  140,   40,   59,   47,   45,
   40,   45,  155,   41,  149,   45,  258,   45,   67,   45,
   40,   45,  256,  257,  256,  257,   75,   76,   59,  256,
  257,  270,  271,  267,   41,  267,  270,  271,  270,  271,
  274,  259,  274,  270,  271,  144,  257,  274,  256,  257,
   59,  256,  257,  262,  263,  260,  262,  263,  157,  264,
  257,  160,  270,  271,  257,  258,  274,  272,  273,  256,
  257,  256,  257,  260,   41,  260,   43,  264,   45,  264,
   40,  268,  267,  262,  263,  272,   59,  272,  256,  257,
  261,   41,  260,   43,   59,   45,  264,   41,  266,   43,
   61,   45,   41,  259,  272,   75,   76,  256,  257,  261,
   41,  260,  259,  259,   41,  264,  261,  257,   59,  268,
   72,   73,  261,  272,  276,  277,  278,  279,  280,  281,
  261,  276,  277,  278,  279,  280,  281,  276,  277,  278,
  279,  280,  281,   20,  261,  258,  270,  271,  257,  258,
   41,  261,   41,   59,   59,   32,   58,  261,   59,  276,
  277,  278,  279,  280,  281,   59,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,  257,  261,
   59,  276,  277,  278,  279,  280,  281,  263,  276,  277,
  278,  279,  280,  281,  276,  277,  278,  279,  280,  281,
  257,  276,  277,  278,  279,  280,  281,  257,  258,  257,
  258,   59,  263,  270,  271,  276,  277,  278,  279,  280,
  281,  257,  258,  257,  258,  275,   59,  257,  258,  257,
  258,  257,  258,  257,  258,   59,  263,  256,  257,   59,
  258,  260,   44,  120,   44,  264,  123,  266,   59,   59,
  256,  257,  268,  272,  260,  132,  133,   41,  264,  136,
  259,  256,  257,  269,   59,  260,  272,  259,  145,  264,
  256,  257,  259,  268,  260,  268,  267,  272,  264,   59,
   59,   59,   59,   59,   59,   59,  272,   59,   59,   59,
   59,   64,   15,  160,   46,  153,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  197,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  207,
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
"clausula_seleccion_if : error ';'",
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
"conjunto_sentencias_repeat : conjunto_sentencias_repeat sentencia_ejecutable",
"condicion_repeat : ID operador_logico ID",
"condicion_repeat : ID operador_logico CTE",
};

//#line 165 "gramatica.y"
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
//#line 444 "Parser.java"
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
//#line 30 "gramatica.y"
{System.out.println("Declaracion de datos");}
break;
case 9:
//#line 31 "gramatica.y"
{System.out.println("Declaracion de datos TYPEDEF");}
break;
case 10:
//#line 32 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
break;
case 13:
//#line 39 "gramatica.y"
{System.out.println("Declaracion TYPEDEF");}
break;
case 15:
//#line 45 "gramatica.y"
{System.out.println("DECLARACION FUNCION");}
break;
case 16:
//#line 46 "gramatica.y"
{System.out.println("DECLARACION FUNCION Y PRE CONDICION");}
break;
case 17:
//#line 47 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en la declaracion de la funcion");}
break;
case 18:
//#line 50 "gramatica.y"
{System.out.println("pre-condicion");}
break;
case 19:
//#line 51 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("pre-condicion");}
break;
case 20:
//#line 52 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); System.out.println("pre-condicion");}
break;
case 21:
//#line 53 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("pre-condicion");}
break;
case 22:
//#line 54 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); System.out.println("pre-condicion");}
break;
case 23:
//#line 55 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); System.out.println("pre-condicion");}
break;
case 25:
//#line 59 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 26:
//#line 60 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 27:
//#line 61 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 28:
//#line 62 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 29:
//#line 63 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 31:
//#line 67 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 32:
//#line 68 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
break;
case 33:
//#line 69 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 35:
//#line 73 "gramatica.y"
{System.out.println("bloque de sentencias BEGIN-END");}
break;
case 43:
//#line 89 "gramatica.y"
{System.out.println("Asignacion");}
break;
case 44:
//#line 90 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); {System.out.println("Asignacion");}}
break;
case 47:
//#line 96 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 48:
//#line 97 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 58:
//#line 113 "gramatica.y"
{System.out.println("factor ID");}
break;
case 59:
//#line 114 "gramatica.y"
{System.out.println("Factor CTE");}
break;
case 61:
//#line 119 "gramatica.y"
{System.out.println("tipo INT");}
break;
case 62:
//#line 120 "gramatica.y"
{System.out.println("tipo SINGLE");}
break;
case 63:
//#line 123 "gramatica.y"
{System.out.println("clausula IF");}
break;
case 64:
//#line 124 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF");}
break;
case 65:
//#line 125 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("clausula IF");}
break;
case 66:
//#line 126 "gramatica.y"
{System.out.println("clausula IF-ELSE");}
break;
case 67:
//#line 127 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF-ELSE");}
break;
case 68:
//#line 128 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");System.out.println("clausula IF-ELSE");}
break;
case 69:
//#line 129 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en sentencia IF");}
break;
case 80:
//#line 147 "gramatica.y"
{System.out.println("clausula PRINT");}
break;
case 81:
//#line 148 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); System.out.println("clausula PRINT");}
break;
case 82:
//#line 149 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); System.out.println("clausula PRINT");}
break;
case 83:
//#line 152 "gramatica.y"
{System.out.println("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 87:
//#line 160 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
case 88:
//#line 161 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
//#line 773 "Parser.java"
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
