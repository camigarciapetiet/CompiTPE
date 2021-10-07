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
    5,    9,    6,    6,    6,   13,   13,   13,   13,   13,
   13,   10,   10,   10,   10,   10,   10,   12,   12,   12,
   12,   11,   11,   16,   16,    2,    2,   17,   17,   17,
   17,   18,   18,   22,   22,   22,   22,   15,   15,   15,
   23,   23,   23,    8,    8,    8,    8,    7,    7,   20,
   20,   20,   20,   20,   20,   14,   14,   24,   24,   24,
   24,   24,   24,   24,   24,   19,   19,   19,   21,   21,
   25,   25,
};
final static short yylen[] = {                            2,
    6,    2,    1,    2,    1,    1,    1,    3,    3,    2,
    5,    5,   12,   13,    2,    8,    7,    7,    7,    7,
    7,    4,    3,    3,    3,    2,    3,    3,    2,    2,
    2,    1,    2,    1,    3,    1,    2,    1,    1,    1,
    1,    4,    3,    3,    1,    2,    2,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    5,    1,    1,    7,
    6,    6,   10,    9,    9,    1,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    5,    4,    4,   11,   16,
    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    0,   58,   59,    0,    0,
    3,    5,    6,    7,    0,   15,    0,   55,    0,    0,
    0,    0,    4,    0,    0,    0,   56,    9,    0,    0,
    0,    0,    0,    0,   36,   38,   39,   40,   41,    0,
    8,    0,    0,    0,    0,    0,   53,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   37,    0,    0,
    0,    0,    0,   11,    0,    0,   46,    0,    0,   43,
    0,    0,    0,   68,   69,   70,   71,   72,   73,   74,
   75,    0,    0,    0,    0,    0,    1,    0,   26,    0,
    0,   32,    0,    0,   57,    0,   42,   44,    0,    0,
   51,   52,    0,    0,    0,    0,   77,   78,    0,    0,
   24,    0,   25,   23,    0,   33,    0,    0,    0,   34,
    0,    0,   76,    0,   22,    0,    0,    0,   12,    0,
    0,   62,    0,    0,   61,    0,    0,    0,    0,   35,
    0,    0,   60,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   30,    0,    0,   29,    0,   65,    0,   64,   81,   82,
    0,    0,    0,    0,    0,   28,    0,    0,   63,    0,
    0,    0,    0,    0,    0,   13,    0,    0,   79,   18,
   19,   20,    0,   21,   17,   14,    0,   16,    0,    0,
    0,   80,
};
final static short yydgoto[] = {                          3,
   10,   34,   11,   12,   13,   14,   15,   47,   44,   61,
   94,  150,  128,   52,   53,  119,  120,   36,   37,   38,
   39,   49,   50,   83,  146,
};
final static short yysindex[] = {                      -233,
  -42,  -99,    0,    0,  -32,   84,    0,    0, -227, -205,
    0,    0,    0,    0,  -40,    0,    3,    0, -224,   34,
   -2,  105,    0, -157,   60, -121,    0,    0, -121,   68,
   -4,  -28,   66,  -79,    0,    0,    0,    0,    0,  -20,
    0, -143, -147,   67,   28,   84,    0,  121,   71,   96,
   84,   36,   54,   91, -124, -117,   86,    0,   61, -104,
   93,  120,  125,    0,  117,  218,    0,   84,   84,    0,
   84,   84,   29,    0,    0,    0,    0,    0,    0,    0,
    0,  -82,   84,  138,   33,  123,    0,  159,    0,  -33,
  166,    0,   84,  -62,    0, -121,    0,    0,   96,   96,
    0,    0,  -61,  -51,  -61,   54,    0,    0,  154,  -35,
    0,  181,    0,    0, -178,    0,  191,  105,  -43,    0,
  -61,  -10,    0,  175,    0,  183,  -74,  105,    0,   87,
  -61,    0,   21,  -61,    0,   -9,   70,   82,   88,    0,
   -5,  -61,    0,   -3,   57,  217,   -1,   43,   30,  230,
  331,   82,  232,   35,  252, -102,   72,  285,   16,  314,
    0,  337,   98,    0,  308,    0,  309,    0,    0,    0,
  332,  116,  122,  -31,  124,    0,  320,  118,    0,   89,
  325,  326,  328,  -45,  329,    0,  330,  105,    0,    0,
    0,    0,  333,    0,    0,    0, -148,    0,  334,  126,
  336,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,  -97,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  338,    0,  -34,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  339,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -12,   -6,
    0,    0,    0,    0,    0,   23,    0,    0,    0,    0,
    0,  100,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  340,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,   13,  380,  -39,    0,    0,  108,   75,    0,    0,
    0,  239,    0,  -36,   42,    2,   24,    0,    0,    0,
    0,  351,  119,  255,    0,
};
final static int YYTABLESIZE=400;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   54,   54,   54,   54,   19,   54,   50,  113,   50,   50,
   50,   55,  184,  194,   73,  132,    4,   54,   54,   59,
   54,   92,    1,    2,   50,   50,   16,   50,   48,   21,
   48,   48,   48,   27,   49,   51,   49,   49,   49,  158,
   19,   66,   26,   19,   66,   35,   48,   48,  135,   48,
    5,    6,   49,   49,  116,   49,  174,   58,   29,  173,
   66,   22,   66,   67,    7,    8,   67,   46,    9,  104,
  161,   48,   19,  109,   19,   81,   82,   80,   30,  143,
   20,   31,   67,  160,   67,   32,   48,   66,   81,   25,
   80,  108,   28,   33,  126,   81,   68,   80,   69,   40,
  148,   89,   81,   41,   80,   56,  122,   46,   30,  147,
  159,   31,   19,   62,   19,   32,   81,   63,   80,  140,
  199,  149,  133,   33,  106,   64,   19,  127,   19,   70,
  130,   84,  141,   42,   85,  144,   43,   72,   35,   86,
  139,   35,   71,  154,   87,  101,  102,   60,    7,    8,
   58,   35,   91,   58,  169,  170,    5,    6,   10,   10,
   95,   67,   58,   68,   96,   69,   90,   25,   93,   10,
    7,    8,   10,   10,    9,   97,   10,   30,  105,  151,
   31,  189,   30,  110,   32,   31,   99,  100,   57,   32,
  162,  138,   33,  151,    6,   30,  107,   33,   31,  111,
  197,   93,   32,  117,  115,  118,  114,    7,    8,  121,
   33,   35,  123,  193,   54,   54,   17,   18,  131,   54,
   58,  125,  124,  112,   24,   54,   50,  183,   54,   54,
   54,  129,   54,  136,   54,   54,   54,   54,   54,   54,
  137,   50,   50,   50,   50,   50,   50,  145,   48,    7,
    8,  134,   17,   18,   49,   17,   18,  153,   98,  155,
   68,   66,   69,   48,   48,   48,   48,   48,   48,   49,
   49,   49,   49,   49,   49,  157,   66,   66,   66,   66,
   66,   66,  142,   67,   17,   18,   17,   18,  163,  103,
  166,   74,   75,   76,   77,   78,   79,  167,   67,   67,
   67,   67,   67,   67,   74,   75,   76,   77,   78,   79,
  168,   74,   75,   76,   77,   78,   79,   88,   74,   75,
   76,   77,   78,   79,   17,   18,   17,   18,  172,  171,
    7,    8,   74,   75,   76,   77,   78,   79,   17,   18,
   17,   18,   45,   30,   30,   30,   31,   31,   31,    6,
   32,   32,   32,  152,  140,  188,   27,  175,   33,   33,
   33,   30,    7,    8,   31,  177,  178,  179,   32,   27,
   27,  164,  180,   68,  181,   69,   33,  176,  186,   68,
  182,   69,  185,  190,  191,  187,  192,  195,  196,   23,
  165,  198,  200,  201,  202,   65,   45,   47,   31,  156,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   45,   47,   41,   41,   43,   44,
   45,   40,   44,   59,   51,   59,   59,   59,   60,   40,
   62,   61,  256,  257,   59,   60,   59,   62,   41,  257,
   43,   44,   45,  258,   41,   40,   43,   44,   45,   41,
   45,   41,   40,   45,   44,   22,   59,   60,   59,   62,
  256,  257,   59,   60,   94,   62,   41,   34,   61,   44,
   60,  267,   62,   41,  270,  271,   44,   40,  274,   41,
   41,   30,   45,   41,   45,   60,   41,   62,  257,   59,
    6,  260,   60,   41,   62,  264,   45,   46,   60,   15,
   62,   59,   59,  272,  273,   60,   43,   62,   45,  257,
  137,   41,   60,   44,   62,   40,  105,   40,  257,   40,
  147,  260,   45,  257,   45,  264,   60,  265,   62,  268,
  269,   40,  121,  272,   83,   59,   45,  115,   45,   59,
  118,   41,  131,   26,  259,  134,   29,   42,  115,  257,
  128,  118,   47,  142,   59,   71,   72,   40,  270,  271,
  127,  128,  257,  130,  257,  258,  256,  257,  256,  257,
   41,   41,  139,   43,   40,   45,   59,   93,   61,  267,
  270,  271,  270,  271,  274,   59,  274,  257,  261,  138,
  260,  180,  257,   61,  264,  260,   68,   69,  268,  264,
  149,  266,  272,  152,  257,  257,   59,  272,  260,   41,
  188,   94,  264,   96,  267,  267,   41,  270,  271,  261,
  272,  188,   59,  259,  256,  257,  257,  258,  262,  261,
  197,   41,  258,  257,  265,  267,  261,  259,  270,  271,
  259,   41,  274,   59,  276,  277,  278,  279,  280,  281,
   58,  276,  277,  278,  279,  280,  281,  257,  261,  270,
  271,  262,  257,  258,  261,  257,  258,  263,   41,  263,
   43,  261,   45,  276,  277,  278,  279,  280,  281,  276,
  277,  278,  279,  280,  281,   59,  276,  277,  278,  279,
  280,  281,  262,  261,  257,  258,  257,  258,   59,  261,
   59,  276,  277,  278,  279,  280,  281,  263,  276,  277,
  278,  279,  280,  281,  276,  277,  278,  279,  280,  281,
   59,  276,  277,  278,  279,  280,  281,  257,  276,  277,
  278,  279,  280,  281,  257,  258,  257,  258,   44,  258,
  270,  271,  276,  277,  278,  279,  280,  281,  257,  258,
  257,  258,  275,  257,  257,  257,  260,  260,  260,  257,
  264,  264,  264,  266,  268,  267,  257,   44,  272,  272,
  272,  257,  270,  271,  260,  268,   59,   59,  264,  270,
  271,   41,   41,   43,  259,   45,  272,   41,   59,   43,
  259,   45,  259,   59,   59,  268,   59,   59,   59,   10,
  152,   59,   59,  268,   59,   45,   59,   59,   59,  145,
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
"declaracionDatos : tipo factor ','",
"declaracionDatos : ID factor ';'",
"declaracionDatos : tipo factor",
"declaracionNuevoTipo : TYPEDEF ID '=' encabezado_funcion ';'",
"encabezado_funcion : tipo FUNC '(' tipo ')'",
"declaracionFuncion : tipo FUNC ID parametro sentencias_declarativas_datos BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
"declaracionFuncion : tipo FUNC ID parametro sentencias_declarativas_datos BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
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
"sentencias_declarativas_datos : declaracionDatos",
"sentencias_declarativas_datos : sentencias_declarativas_datos declaracionDatos",
"bloque_sentencias_ejecutables : sentencia_ejecutable",
"bloque_sentencias_ejecutables : BEGIN conjunto_sentencia_ejecutable END",
"conjunto_sentencia_ejecutable : sentencia_ejecutable",
"conjunto_sentencia_ejecutable : conjunto_sentencia_ejecutable sentencia_ejecutable",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : mensaje_pantalla",
"sentencia_ejecutable : clausula_seleccion_if",
"sentencia_ejecutable : sentencia_control_repeat",
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
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"factor : ID '(' tipo ID ')'",
"tipo : INT",
"tipo : SINGLE",
"clausula_seleccion_if : IF '(' condicion ')' THEN bloque_sentencias_ejecutables ';'",
"clausula_seleccion_if : IF condicion ')' THEN bloque_sentencias_ejecutables ';'",
"clausula_seleccion_if : IF '(' condicion THEN bloque_sentencias_ejecutables ';'",
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
"sentencia_control_repeat : REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' bloque_sentencias_ejecutables",
"sentencia_control_repeat : REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencia_ejecutable BREAK ';' END ';'",
"condicion_repeat : ID operador_logico ID",
"condicion_repeat : ID operador_logico CTE",
};

//#line 154 "gramatica.y"
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
//#line 425 "Parser.java"
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
case 8:
//#line 30 "gramatica.y"
{System.out.println("Declaracion de datos");}
break;
case 9:
//#line 31 "gramatica.y"
{System.out.println("Declaracion de datos");}
break;
case 10:
//#line 32 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
break;
case 11:
//#line 35 "gramatica.y"
{System.out.println("Declaracion TYPEDEF");}
break;
case 13:
//#line 41 "gramatica.y"
{System.out.println("DECLARACION FUNCION");}
break;
case 14:
//#line 42 "gramatica.y"
{System.out.println("DECLARACION FUNCION Y PRE CONDICION");}
break;
case 16:
//#line 46 "gramatica.y"
{System.out.println("pre-condicion");}
break;
case 17:
//#line 47 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("pre-condicion");}
break;
case 18:
//#line 48 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); System.out.println("pre-condicion");}
break;
case 19:
//#line 49 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("pre-condicion");}
break;
case 20:
//#line 50 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); System.out.println("pre-condicion");}
break;
case 21:
//#line 51 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); System.out.println("pre-condicion");}
break;
case 23:
//#line 55 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 24:
//#line 56 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 25:
//#line 57 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 26:
//#line 58 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 27:
//#line 59 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 29:
//#line 63 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 30:
//#line 64 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
break;
case 31:
//#line 65 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 35:
//#line 73 "gramatica.y"
{System.out.println("bloque de sentencias BEGIN-END");}
break;
case 42:
//#line 86 "gramatica.y"
{System.out.println("Asignacion");}
break;
case 43:
//#line 87 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); {System.out.println("Asignacion");}}
break;
case 46:
//#line 93 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 47:
//#line 94 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 54:
//#line 107 "gramatica.y"
{System.out.println("factor ID");}
break;
case 55:
//#line 108 "gramatica.y"
{System.out.println("Factor CTE");}
break;
case 58:
//#line 113 "gramatica.y"
{System.out.println("tipo INT");}
break;
case 59:
//#line 114 "gramatica.y"
{System.out.println("tipo SINGLE");}
break;
case 60:
//#line 117 "gramatica.y"
{System.out.println("clausula IF");}
break;
case 61:
//#line 118 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF");}
break;
case 62:
//#line 119 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("clausula IF");}
break;
case 63:
//#line 120 "gramatica.y"
{System.out.println("clausula IF-ELSE");}
break;
case 64:
//#line 121 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF-ELSE");}
break;
case 65:
//#line 122 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");System.out.println("clausula IF-ELSE");}
break;
case 76:
//#line 140 "gramatica.y"
{System.out.println("clausula PRINT");}
break;
case 77:
//#line 141 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); System.out.println("clausula PRINT");}
break;
case 78:
//#line 142 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); System.out.println("clausula PRINT");}
break;
case 79:
//#line 145 "gramatica.y"
{System.out.println("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 80:
//#line 146 "gramatica.y"
{System.out.println("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 81:
//#line 149 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
case 82:
//#line 150 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
//#line 746 "Parser.java"
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
