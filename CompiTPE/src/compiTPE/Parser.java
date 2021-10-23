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
   11,   11,   11,   15,   15,   16,   16,    2,    2,   17,
   17,   17,   17,   17,   22,   18,   18,   24,   24,   24,
   24,   14,   14,   14,   25,   25,   25,   23,   23,   23,
   23,    7,    7,   20,   20,   20,   20,   20,   20,   20,
   13,   13,   26,   26,   26,   26,   26,   26,   26,   26,
   19,   19,   19,   21,   28,   28,   28,   27,   27,
};
final static short yylen[] = {                            2,
    6,    2,    1,    2,    1,    1,    1,    3,    3,    2,
    3,    1,    5,    5,   12,   13,    2,    8,    7,    7,
    7,    7,    7,    4,    3,    3,    3,    2,    3,    3,
    2,    2,    2,    1,    2,    1,    3,    1,    2,    1,
    1,    1,    1,    1,    5,    4,    3,    3,    1,    2,
    2,    3,    3,    1,    3,    3,    1,    1,    1,    2,
    5,    1,    1,    8,    7,    7,   10,    9,    9,    2,
    1,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    5,    4,    4,   14,    2,    1,    2,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    0,   62,   63,    0,    0,
    3,    5,    6,    7,    0,   17,   12,    0,    0,    0,
    4,    0,    0,    9,    0,    0,    0,    0,    0,    0,
    0,    0,   38,   40,   41,   42,   43,   44,    0,    8,
   11,    0,    0,   70,    0,   59,    0,    0,    0,    0,
   57,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   39,    0,    0,    0,    0,   13,    0,    0,    0,    0,
    0,   60,   50,    0,    0,   47,    0,    0,    0,   73,
   74,   75,   76,   77,   78,   79,   80,    0,    0,    0,
    0,    0,    1,    0,   28,    0,    0,    0,    0,    0,
   46,   48,    0,    0,    0,   55,   56,    0,    0,    0,
    0,   82,   83,    0,    0,   26,    0,   27,   25,    0,
    0,    0,   45,    0,    0,   36,    0,    0,   81,    0,
   24,    0,    0,    0,   14,   61,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   37,    0,   66,    0,
    0,    0,   65,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
   32,    0,    0,   31,    0,   69,    0,   68,   88,   89,
    0,    0,    0,    0,    0,   30,    0,    0,   67,    0,
    0,    0,    0,    0,    0,   15,    0,    0,   20,   21,
   22,    0,   23,   19,   16,    0,   86,    0,   18,   85,
    0,   87,   84,
};
final static short yydgoto[] = {                          3,
   10,   32,   11,   12,   13,   14,   15,   18,   43,   64,
  159,  134,   55,   56,    0,  125,  126,   34,   35,   36,
   37,   38,   51,   52,   53,   89,  155,  208,
};
final static short yysindex[] = {                      -118,
  -39,  -88,    0,    0,  -23, -233,    0,    0, -224, -122,
    0,    0,    0,    0, -177,    0,    0,   28,  -12,  126,
    0, -205,   38,    0, -198, -130,   22,   61,   63,  -35,
   35,  -38,    0,    0,    0,    0,    0,    0,  -13,    0,
    0, -197,   25,    0,   52,    0,   75,   85, -153,  117,
    0,   57,    8,   85,   23,   66,   78, -136, -125,   68,
    0,   54, -113,  -88,  119,    0, -130,   85,   94,  144,
  120,    0,    0,   85,   85,    0,   85,   85,   29,    0,
    0,    0,    0,    0,    0,    0,    0,  -95,   85,  116,
  -19,  129,    0,  170,    0,  -29,  184, -120, -130,  -16,
    0,    0,  195,    8,    8,    0,    0,  -51,  -32,  -51,
   66,    0,    0,  197,    3,    0,  224,    0,    0,  -76,
  225,  226,    0,  126,  -98,    0,  -51,  -90,    0,  210,
    0,  218,   88,  126,    0,    0,   93,  -51,  230,  -69,
  -51,  232,   41,   77,   81,  102,    0,   59,    0,  -51,
  264,   74,    0,   50,  287,    2,   36,   83,  288,  159,
   81,  292,   92,    0,  297,  -26,  114,  331,   16,  334,
    0,  167,  112,    0,  325,    0,  328,    0,    0,    0,
  347,  130,  132,  -36,  133,    0,  335,  125,    0,  128,
  337,  338,  340,  -45,  341,    0,  342,  107,    0,    0,
    0,  343,    0,    0,    0,  344,    0,  113,    0,    0,
  345,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -100,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -41,    0,    0,    0,    0,  346,
    0,    0,  -34,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  348,
  156,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -28,   -6,    0,    0,    0,    0,    0,
    7,    0,    0,    0,    0,    0,  -79,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  349,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  350,  -20,   20,    0,    0,    0,   32,  391,    0,    0,
  248,    0,  -31,   18,    0,  -48,    9,    0,    0,    0,
    0,    0,   -4,  363,  189,  257,    0,    0,
};
final static int YYTABLESIZE=414;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   58,   58,   58,   58,   58,   58,   54,  194,   54,   54,
   54,  118,   52,  203,   52,   52,   52,   58,   58,    4,
   58,  114,   79,   17,   54,   54,   62,   54,   33,   21,
   52,   52,   19,   52,   53,   16,   53,   53,   53,  113,
   61,   71,  168,   71,   71,   50,   49,   72,   26,   78,
   72,   39,   53,   53,   77,   53,  184,   42,   41,  183,
   71,  128,   71,   88,   50,   70,   72,   65,   72,  109,
   63,   25,  106,  107,   59,   87,  170,   86,  140,   17,
   44,   25,   87,   66,   86,   70,   24,   22,   87,  148,
   86,   67,  152,   96,   95,   87,   40,   86,  100,  133,
   48,  163,   54,  137,   72,   49,  111,   49,   74,   87,
   75,   86,  157,  146,   68,   76,  156,   21,   90,   49,
  158,   49,   91,  171,  169,   49,   93,   49,   33,   49,
  121,   92,   33,    5,    6,    5,    6,    1,    2,    7,
    8,   61,   33,   97,   20,   61,  120,    7,    8,    7,
    8,    9,  101,    9,   61,   10,   10,   73,   99,   74,
  103,   75,  160,  138,  139,  110,   10,    5,    6,   10,
   10,  141,  142,   10,  112,  172,   29,   29,  160,   27,
   28,    7,    8,   29,  102,    9,   74,   30,   75,  115,
   29,   29,  150,  151,   29,   31,  132,   57,   57,  174,
   57,   74,   57,   75,   27,   28,  207,  186,   29,   74,
  116,   75,   30,  202,   57,  124,  212,   27,   28,   58,
   31,   29,  193,   57,  119,   30,   54,  117,  127,   60,
  179,  180,   52,   31,   58,   58,   58,   58,   58,   58,
  122,   54,   54,   54,   54,   54,   54,   52,   52,   52,
   52,   52,   52,  123,   53,  129,    7,    8,   45,   46,
  130,   71,  104,  105,  131,  135,  136,   72,  143,   53,
   53,   53,   53,   53,   53,  144,   71,   71,   71,   71,
   71,   71,   72,   72,   72,   72,   72,   72,  149,  108,
  153,   80,   81,   82,   83,   84,   85,  154,   80,   81,
   82,   83,   84,   85,   80,   81,   82,   83,   84,   85,
   94,   80,   81,   82,   83,   84,   85,   45,   46,   45,
   46,  162,  164,    7,    8,   80,   81,   82,   83,   84,
   85,   45,   46,   45,   46,   47,  165,   45,   46,   45,
   46,   45,   46,   27,   28,  167,  173,   29,   27,   28,
  176,   30,   29,  145,  177,  178,   30,   27,   28,   31,
  147,   29,   27,   28,   31,   30,   29,  161,   27,   28,
   30,  181,   29,   31,  182,  206,   30,  185,   31,  187,
  211,   27,   28,  188,   31,   29,  189,  190,  191,   30,
  192,  195,  197,  196,  198,  199,  200,   31,  201,  204,
  205,  209,  210,  213,   49,   23,   51,   33,  175,   69,
  166,    0,    0,   98,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   40,   47,   41,   44,   43,   44,
   45,   41,   41,   59,   43,   44,   45,   59,   60,   59,
   62,   41,   54,  257,   59,   60,   40,   62,   20,   10,
   59,   60,  257,   62,   41,   59,   43,   44,   45,   59,
   32,   41,   41,   48,   44,   28,   45,   41,   61,   42,
   44,  257,   59,   60,   47,   62,   41,   26,  257,   44,
   60,  110,   62,   41,   47,   48,   60,  265,   62,   41,
   39,   44,   77,   78,   40,   60,   41,   62,  127,  257,
   59,   44,   60,   59,   62,   68,   59,  265,   60,  138,
   62,   40,  141,   62,   41,   60,   59,   62,   67,  120,
   40,  150,   40,  124,  258,   45,   89,   45,   43,   60,
   45,   62,  144,  134,   40,   59,   40,   98,   41,   45,
   40,   45,  259,   41,  156,   45,   59,   45,  120,   45,
   99,  257,  124,  256,  257,  256,  257,  256,  257,  270,
  271,  133,  134,  257,  267,  137,  267,  270,  271,  270,
  271,  274,   59,  274,  146,  256,  257,   41,   40,   43,
   41,   45,  145,  262,  263,  261,  267,  256,  257,  270,
  271,  262,  263,  274,   59,  158,  256,  257,  161,  256,
  257,  270,  271,  260,   41,  274,   43,  264,   45,   61,
  270,  271,  262,  263,  274,  272,  273,   42,   43,   41,
   45,   43,   47,   45,  256,  257,  198,   41,  260,   43,
   41,   45,  264,  259,   59,  267,  208,  256,  257,  261,
  272,  260,  259,  259,   41,  264,  261,  257,  261,  268,
  257,  258,  261,  272,  276,  277,  278,  279,  280,  281,
  257,  276,  277,  278,  279,  280,  281,  276,  277,  278,
  279,  280,  281,   59,  261,   59,  270,  271,  257,  258,
  258,  261,   74,   75,   41,   41,   41,  261,   59,  276,
  277,  278,  279,  280,  281,   58,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,   59,  261,
   59,  276,  277,  278,  279,  280,  281,  257,  276,  277,
  278,  279,  280,  281,  276,  277,  278,  279,  280,  281,
  257,  276,  277,  278,  279,  280,  281,  257,  258,  257,
  258,  263,   59,  270,  271,  276,  277,  278,  279,  280,
  281,  257,  258,  257,  258,  275,  263,  257,  258,  257,
  258,  257,  258,  256,  257,   59,   59,  260,  256,  257,
   59,  264,  260,  266,  263,   59,  264,  256,  257,  272,
  268,  260,  256,  257,  272,  264,  260,  266,  256,  257,
  264,  258,  260,  272,   44,  269,  264,   44,  272,  268,
  268,  256,  257,   59,  272,  260,   59,   41,  259,  264,
  259,  259,  268,   59,  267,   59,   59,  272,   59,   59,
   59,   59,   59,   59,   59,   15,   59,   59,  161,   47,
  154,   -1,   -1,   64,
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
"declaracionNuevoTipo : TYPEDEF ID '=' encabezado_funcion ';'",
"encabezado_funcion : tipo FUNC '(' tipo ')'",
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
"sentencia_ejecutable : invocacion_funcion",
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
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"factor : ID '(' tipo ID ')'",
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

//#line 167 "gramatica.y"
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
//#line 439 "Parser.java"
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
        yychar = yylex();  //get next token
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
case 37:
//#line 77 "gramatica.y"
{System.out.println("bloque de sentencias BEGIN-END");}
break;
case 46:
//#line 94 "gramatica.y"
{System.out.println("Asignacion");}
break;
case 47:
//#line 95 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); {System.out.println("Asignacion");}}
break;
case 50:
//#line 101 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 51:
//#line 102 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 58:
//#line 115 "gramatica.y"
{System.out.println("factor ID");}
break;
case 59:
//#line 116 "gramatica.y"
{System.out.println("Factor CTE");}
break;
case 62:
//#line 121 "gramatica.y"
{System.out.println("tipo INT");}
break;
case 63:
//#line 122 "gramatica.y"
{System.out.println("tipo SINGLE");}
break;
case 64:
//#line 125 "gramatica.y"
{System.out.println("clausula IF");}
break;
case 65:
//#line 126 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF");}
break;
case 66:
//#line 127 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("clausula IF");}
break;
case 67:
//#line 128 "gramatica.y"
{System.out.println("clausula IF-ELSE");}
break;
case 68:
//#line 129 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF-ELSE");}
break;
case 69:
//#line 130 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");System.out.println("clausula IF-ELSE");}
break;
case 70:
//#line 131 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en sentencia IF");}
break;
case 81:
//#line 149 "gramatica.y"
{System.out.println("clausula PRINT");}
break;
case 82:
//#line 150 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); System.out.println("clausula PRINT");}
break;
case 83:
//#line 151 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); System.out.println("clausula PRINT");}
break;
case 84:
//#line 154 "gramatica.y"
{System.out.println("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 88:
//#line 162 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
case 89:
//#line 163 "gramatica.y"
{System.out.println("Condicion_Repeat");}
break;
//#line 768 "Parser.java"
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
        yychar = yylex();        //get next character
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
