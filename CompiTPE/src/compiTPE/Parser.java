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
    5,    9,    6,    6,    6,   13,   13,   13,   13,   13,
   13,   10,   10,   10,   10,   10,   10,   12,   12,   12,
   12,   11,   11,   16,   16,    2,    2,   17,   17,   17,
   17,   18,   18,   18,   22,   22,   22,   22,   15,   15,
   15,   23,   23,   23,    8,    8,    8,    8,    7,    7,
   20,   20,   20,   20,   20,   20,   14,   14,   24,   24,
   24,   24,   24,   24,   24,   24,   19,   19,   19,   21,
   21,   25,   25,
};
final static short yylen[] = {                            2,
    6,    2,    1,    2,    1,    1,    1,    3,    3,    2,
    5,    5,   12,   13,    2,    6,    5,    5,    5,    5,
    5,    4,    3,    3,    3,    2,    3,    3,    2,    2,
    2,    1,    2,    1,    3,    1,    2,    1,    1,    1,
    1,    4,    3,    2,    3,    1,    2,    2,    3,    3,
    1,    3,    3,    1,    1,    1,    2,    5,    1,    1,
    7,    6,    6,   10,    9,    9,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    5,    4,    4,   11,
   16,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    2,    0,    0,   59,   60,    0,    0,
    3,    5,    6,    7,    0,   15,    0,   56,    0,    0,
    0,    0,    4,    0,    0,    0,   57,    9,    0,    0,
    0,    0,    0,    0,    0,   36,   38,   39,   40,   41,
    0,    8,    0,    0,    0,   44,    0,    0,   54,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   37,
    0,    0,    0,    0,    0,   11,    0,    0,   47,    0,
    0,   43,    0,    0,    0,   69,   70,   71,   72,   73,
   74,   75,   76,    0,    0,    0,    0,    0,    1,    0,
   26,    0,    0,   32,    0,    0,   58,    0,   42,   45,
    0,    0,   52,   53,    0,    0,    0,    0,   78,   79,
    0,    0,   24,    0,   25,   23,    0,   33,    0,    0,
    0,   34,    0,    0,   77,    0,   22,    0,    0,    0,
   12,    0,    0,   63,    0,    0,   62,    0,    0,    0,
    0,    0,   35,    0,    0,   61,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   30,    0,    0,   29,    0,
   66,    0,   65,   82,   83,    0,   18,   19,   20,    0,
   17,   28,    0,    0,   64,    0,   16,   13,    0,    0,
   80,   14,    0,    0,    0,    0,   81,
};
final static short yydgoto[] = {                          3,
   10,   35,   11,   12,   13,   14,   15,   49,   45,   63,
   96,  154,  130,   54,   55,  121,  122,   37,   38,   39,
   40,   51,   52,   85,  149,
};
final static short yysindex[] = {                      -198,
  -36, -150,    0,    0,    3,   84,    0,    0, -191, -104,
    0,    0,    0,    0,  -40,    0,   32,    0, -189,   22,
   31,  -51,    0, -153,   79, -159,    0,    0, -159,   50,
   68,   -4,  -28,   86,   91,    0,    0,    0,    0,    0,
  -20,    0, -115, -120,   89,    0,   28,   84,    0,   52,
   96,    4,   84,   36,  -21,  123,  -86,  -83,  122,    0,
   61,  -75, -171,  143,  145,    0,  129,   98,    0,   84,
   84,    0,   84,   84,   29,    0,    0,    0,    0,    0,
    0,    0,    0,  -68,   84,  137,   -7,  133,    0,  158,
    0,  -33,  162,    0,   84,  -81,    0, -159,    0,    0,
    4,    4,    0,    0,   93,  -54,   93,  -21,    0,    0,
  152,  -44,    0,  181,    0,    0,  -95,    0,  182,  -51,
  -43,    0,   93,  -10,    0,  173,    0,   70,  110,  -51,
    0,  121,   93,    0,   21,   93,    0,  -23,   -1,   43,
   82,  130,    0,  -22,   93,    0,  -15,   57,  199,  215,
   16,  216,   30,  202,  159,   82,  204,   13,  230, -127,
   33,   39,   71,  -31,   85,    0,  167,   88,    0,  252,
    0,  293,    0,    0,    0,  313,    0,    0,    0,   99,
    0,    0,  302,   94,    0,  141,    0,    0,  305,  -51,
    0,    0,  -77,  309,  101,  312,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,  -99,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   50,
    0,  -34,    0,    0,    1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  314,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -12,   -6,    0,    0,    0,    0,    0,   23,    0,    0,
    0,    0,    0, -111,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  316,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  135,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0, -103,  362,   -8,    0,    0,   53,   59,    0,    0,
    0,  223,    0,  -38,  298,   11,    8,    0,    0,    0,
    0,  333,   65,  236,    0,
};
final static int YYTABLESIZE=454;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   55,   55,   55,   55,   19,   55,   51,  115,   51,   51,
   51,   57,  180,  129,   75,  134,  132,   55,   55,   61,
   55,   70,    4,   71,   51,   51,  142,   51,   49,   36,
   49,   49,   49,  111,   50,   53,   50,   50,   50,  150,
   19,   67,   60,   19,   67,   74,   49,   49,  137,   49,
   73,  110,   50,   50,   94,   50,  164,    1,    2,  163,
   67,   16,   67,   68,   20,   21,   68,   48,   27,  106,
  166,   26,   19,   25,   19,   83,   84,   82,   43,  146,
   28,   44,   68,  152,   68,    6,  193,  118,   83,  140,
   82,   29,   69,   62,   70,   83,   71,   82,    7,    8,
  151,   91,   83,   41,   82,    5,    6,   48,   46,  139,
    7,    8,   19,   92,   19,   95,   83,  124,   82,    7,
    8,  153,   42,    9,   36,   58,   19,   36,   19,  174,
  175,  103,  104,  135,  101,  102,   60,   36,  100,   60,
   70,   64,   71,  144,   65,   27,  147,   66,   95,   60,
  119,    5,    6,   25,   72,  158,   10,   10,   27,   27,
   30,   31,   22,   86,   32,    7,    8,   10,   33,    9,
   10,   10,   87,   88,   10,    6,   34,  128,   30,   31,
   89,   93,   32,   97,   98,  117,   33,   99,    7,    8,
  143,  194,  107,  112,   34,  109,  191,   36,  113,  169,
   60,   70,  116,   71,   30,   31,  123,  182,   32,   70,
  125,   71,   33,  126,   55,   55,   17,   18,  133,   55,
   34,  127,  131,  114,   24,   55,   51,  179,   55,   55,
   56,  138,   55,  148,   55,   55,   55,   55,   55,   55,
  157,   51,   51,   51,   51,   51,   51,  159,   49,    7,
    8,  136,   17,   18,   50,   17,   18,  161,  162,  165,
  168,   67,  171,   49,   49,   49,   49,   49,   49,   50,
   50,   50,   50,   50,   50,  172,   67,   67,   67,   67,
   67,   67,  145,   68,   17,   18,   17,   18,  173,  105,
  176,   76,   77,   78,   79,   80,   81,  177,   68,   68,
   68,   68,   68,   68,   76,   77,   78,   79,   80,   81,
  184,   76,   77,   78,   79,   80,   81,   90,   76,   77,
   78,   79,   80,   81,   17,   18,   17,   18,   50,  178,
    7,    8,   76,   77,   78,   79,   80,   81,   17,   18,
   17,   18,   47,  181,   50,   68,   30,   31,   30,   31,
   32,  185,   32,  186,   33,  183,   33,  187,   59,  120,
  188,  189,   34,  192,   34,   30,   31,  195,  196,   32,
  197,   23,   48,   33,   31,  141,   30,   31,  170,   67,
   32,   34,  108,  160,   33,   30,   31,    0,  143,   32,
   21,   21,   34,   33,   21,  156,   30,   31,   21,    0,
   32,   34,    0,    0,   33,    0,   21,  190,    0,    0,
    0,    0,   34,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  155,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  167,    0,    0,  155,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   45,   47,   41,   41,   43,   44,
   45,   40,   44,  117,   53,   59,  120,   59,   60,   40,
   62,   43,   59,   45,   59,   60,  130,   62,   41,   22,
   43,   44,   45,   41,   41,   40,   43,   44,   45,   41,
   45,   41,   35,   45,   44,   42,   59,   60,   59,   62,
   47,   59,   59,   60,   63,   62,   41,  256,  257,   44,
   60,   59,   62,   41,    6,  257,   44,   40,  258,   41,
   41,   40,   45,   15,   45,   60,   41,   62,   26,   59,
   59,   29,   60,   41,   62,  257,  190,   96,   60,  128,
   62,   61,   41,   41,   43,   60,   45,   62,  270,  271,
  139,   41,   60,  257,   62,  256,  257,   40,   59,   40,
  270,  271,   45,   61,   45,   63,   60,  107,   62,  270,
  271,   40,   44,  274,  117,   40,   45,  120,   45,  257,
  258,   73,   74,  123,   70,   71,  129,  130,   41,  132,
   43,  257,   45,  133,  265,  257,  136,   59,   96,  142,
   98,  256,  257,   95,   59,  145,  256,  257,  270,  271,
  256,  257,  267,   41,  260,  270,  271,  267,  264,  274,
  270,  271,  259,  257,  274,  257,  272,  273,  256,  257,
   59,  257,  260,   41,   40,  267,  264,   59,  270,  271,
  268,  269,  261,   61,  272,   59,  186,  190,   41,   41,
  193,   43,   41,   45,  256,  257,  261,   41,  260,   43,
   59,   45,  264,  258,  256,  257,  257,  258,  262,  261,
  272,   41,   41,  257,  265,  267,  261,  259,  270,  271,
  259,   59,  274,  257,  276,  277,  278,  279,  280,  281,
  263,  276,  277,  278,  279,  280,  281,  263,  261,  270,
  271,  262,  257,  258,  261,  257,  258,   59,   44,   44,
   59,  261,   59,  276,  277,  278,  279,  280,  281,  276,
  277,  278,  279,  280,  281,  263,  276,  277,  278,  279,
  280,  281,  262,  261,  257,  258,  257,  258,   59,  261,
  258,  276,  277,  278,  279,  280,  281,  259,  276,  277,
  278,  279,  280,  281,  276,  277,  278,  279,  280,  281,
   59,  276,  277,  278,  279,  280,  281,  257,  276,  277,
  278,  279,  280,  281,  257,  258,  257,  258,   31,  259,
  270,  271,  276,  277,  278,  279,  280,  281,  257,  258,
  257,  258,  275,  259,   47,   48,  256,  257,  256,  257,
  260,   59,  260,   41,  264,  268,  264,  259,  268,  267,
   59,  268,  272,   59,  272,  256,  257,   59,  268,  260,
   59,   10,   59,  264,   59,  266,  256,  257,  156,   47,
  260,  272,   85,  148,  264,  256,  257,   -1,  268,  260,
  256,  257,  272,  264,  260,  266,  256,  257,  264,   -1,
  260,  272,   -1,   -1,  264,   -1,  272,  267,   -1,   -1,
   -1,   -1,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  141,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  153,   -1,   -1,  156,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
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
"pre_condicion : PRE '(' condicion ')' ',' CADENA",
"pre_condicion : PRE condicion ')' ',' CADENA",
"pre_condicion : PRE '(' ')' ',' CADENA",
"pre_condicion : PRE '(' condicion ',' CADENA",
"pre_condicion : PRE '(' condicion ')' CADENA",
"pre_condicion : PRE '(' condicion ')' ','",
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
"asignacion : error ';'",
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
//#line 435 "Parser.java"
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
case 10:
//#line 32 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
break;
case 17:
//#line 47 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion");}
break;
case 18:
//#line 48 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'");}
break;
case 19:
//#line 49 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");}
break;
case 20:
//#line 50 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'");}
break;
case 21:
//#line 51 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','");}
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
case 43:
//#line 87 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID");}
break;
case 47:
//#line 93 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 48:
//#line 94 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 62:
//#line 118 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion");}
break;
case 63:
//#line 119 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");}
break;
case 65:
//#line 121 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion");}
break;
case 66:
//#line 122 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");}
break;
case 78:
//#line 141 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena");}
break;
case 79:
//#line 142 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena");}
break;
//#line 676 "Parser.java"
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
