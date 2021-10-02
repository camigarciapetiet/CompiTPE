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

boolean yydebug = true;        //do I want debug output?
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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    4,    4,    5,    5,   11,
   11,   11,   11,   11,   11,    8,    8,    8,    8,    8,
    8,   10,   10,   10,   10,    9,    9,   14,   14,    2,
    2,   15,   15,   15,   15,   16,   16,   20,   20,   20,
   20,   13,   13,   13,   21,   21,   21,    7,    7,    7,
    7,    6,    6,   18,   18,   18,   18,   18,   18,   12,
   12,   22,   22,   22,   22,   22,   22,   22,   22,   17,
   17,   17,   19,   19,   23,   23,
};
final static short yylen[] = {                            2,
    6,    1,    2,    1,    1,    3,    2,   12,   13,    6,
    5,    5,    5,    5,    5,    4,    3,    3,    3,    2,
    3,    3,    2,    2,    2,    1,    2,    1,    3,    1,
    2,    1,    1,    1,    1,    4,    3,    3,    1,    2,
    2,    3,    3,    1,    3,    3,    1,    1,    1,    2,
    5,    1,    1,    7,    6,    6,   10,    9,    9,    1,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    5,
    4,    4,   11,   16,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,   52,   53,    0,    2,    4,    5,    0,    0,
    3,    0,   49,    0,    0,    0,    0,    0,    0,    0,
    0,   30,   32,   33,   34,   35,    0,    0,   50,    6,
    0,    0,   47,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   31,    0,    0,    0,    0,    0,    0,
   40,    0,    0,   37,    0,    0,    0,   62,   63,   64,
   65,   66,   67,   68,   69,    0,    0,    0,    0,    0,
    1,    0,    0,   20,    0,    0,   26,    0,    0,   36,
   38,    0,    0,   45,   46,    0,    0,    0,    0,   71,
   72,    0,    0,   51,   18,    0,   19,   17,    0,   27,
    0,    0,   28,    0,    0,   70,    0,   16,    0,    0,
    0,    0,    0,   56,    0,    0,   55,    0,    0,    0,
    0,    0,   29,    0,    0,   54,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   24,    0,    0,   23,    0,
   59,    0,   58,   75,   76,    0,   12,   13,   14,    0,
   11,   22,    0,    0,   57,    0,   10,    8,    0,    0,
   73,    9,    0,    0,    0,    0,   74,
};
final static short yydgoto[] = {                          2,
    5,   21,    6,    7,    8,    9,   33,   48,   79,  134,
  111,   38,   39,  102,  103,   23,   24,   25,   26,   35,
   36,   67,  129,
};
final static short yysindex[] = {                      -202,
 -221,    0,    0,    0,  -77,    0,    0,    0,  -40, -130,
    0,   17,    0, -187, -181,   36,   49,   61,    2,   44,
 -124,    0,    0,    0,    0,    0, -221,   38,    0,    0,
   63,   76,    0,   28,   34,  -12,   76,   19,   62,   57,
 -157, -144,   58,    0, -126,   54, -122, -221,   87,  134,
    0,   76,   76,    0,   76,   76,    5,    0,    0,    0,
    0,    0,    0,    0,    0, -106,   76,   98,  -14,   97,
    0,  120,  126,    0,  -33,  129,    0,   76,  -59,    0,
    0,  -12,  -12,    0,    0,  -91,  -78,  -91,   62,    0,
    0,  142,  -73,    0,    0,  161,    0,    0, -110,    0,
 -130,  -47,    0,  -91,  -30,    0,  146,    0,   69,  -86,
 -130,  -69,  -91,    0,    3,  -91,    0,  -38,    6,   25,
   71,  -68,    0,  -35,  -91,    0,  -32,   37,  164,  218,
   12,  242,   74,  234,  294,   71,  251,   59,  271, -194,
   83,   84,   85,   14,   88,    0,  295,   80,    0,  290,
    0,  291,    0,    0,    0,  310,    0,    0,    0,   93,
    0,    0,  296,   86,    0,  -51,    0,    0,  297, -130,
    0,    0, -104,  298,   90,  300,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,   75,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  301,    0,  -34,    0,    0,   -8,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  302,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -28,  -21,    0,    0,    0,    0,    0,   -1,    0,
    0,    0,    0,    0,    0, -146,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  303,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -50,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  -11,  348,   -4,    0,   64,   67,    0,    0,  227,
    0,  -23,   51,   16,   27,    0,    0,    0,    0,  333,
   99,  237,    0,
};
final static int YYTABLESIZE=365;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   48,   48,   48,   48,   15,   48,   44,   97,   44,   44,
   44,  114,   42,   57,   42,   42,   42,   48,   48,   43,
   48,   43,   43,   43,   44,   44,   92,   44,  117,   56,
   42,   42,   60,   42,   55,   60,   22,   43,   43,   61,
   43,   41,   61,   77,   91,   87,  130,   44,    3,    4,
   15,   60,  144,   60,    1,  143,   27,  160,   61,   66,
   61,  126,  154,  155,   65,  132,   64,   34,   51,   28,
   52,   65,   53,   64,  100,   16,   29,   46,   65,   30,
   64,   34,   50,   42,   65,  120,   64,  110,   32,  112,
   45,   47,   54,   15,   74,  131,   65,   68,   64,  122,
   37,   69,   32,  105,   52,   15,   53,   15,  119,   75,
  133,   78,   70,   15,  146,   15,   71,   89,   15,  115,
   15,   84,   85,   21,   21,   22,   17,   22,  124,   18,
   72,  127,   17,   19,   76,   18,   44,   22,   44,   19,
  138,   20,   78,   43,   16,   80,   17,   20,   44,   18,
   82,   83,   17,   19,   88,   18,   90,   93,  173,   19,
   94,   20,  109,  123,  174,   17,   95,   20,   18,   98,
   17,  135,   19,   18,   81,  101,   52,   19,   53,  121,
   20,  171,  104,  147,  107,   20,  135,   17,   17,   10,
   18,   18,    3,    4,   19,   19,   22,  136,  123,   44,
  106,  108,   20,   20,  118,   17,   15,   99,   18,   15,
    3,    4,   19,   15,  113,  170,   12,   13,  128,   48,
   20,   15,  141,   96,   14,   48,   44,  137,   48,   48,
  139,  116,   42,   48,   48,   48,   48,   48,   48,   43,
   44,   44,   44,   44,   44,   44,   42,   42,   42,   42,
   42,   42,   60,   43,   43,   43,   43,   43,   43,   61,
   40,  142,   12,   13,  125,   86,   60,   60,   60,   60,
   60,   60,  159,   61,   61,   61,   61,   61,   61,   58,
   59,   60,   61,   62,   63,  145,   58,   59,   60,   61,
   62,   63,  148,   58,   59,   60,   61,   62,   63,   58,
   59,   60,   61,   62,   63,   12,   13,    3,    4,  151,
   73,   58,   59,   60,   61,   62,   63,   12,   13,   12,
   13,  152,   31,    3,    4,   12,   13,   12,   13,  153,
   12,   13,   12,   13,  149,  162,   52,   52,   53,   53,
  156,    7,  157,  158,    7,    7,  161,  163,  164,  165,
  166,  167,   11,  169,  168,  172,  175,  176,  177,   39,
   41,   25,  150,   49,  140,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   45,   47,   41,   41,   43,   44,
   45,   59,   41,   37,   43,   44,   45,   59,   60,   41,
   62,   43,   44,   45,   59,   60,   41,   62,   59,   42,
   59,   60,   41,   62,   47,   44,   10,   59,   60,   41,
   62,   40,   44,   48,   59,   41,   41,   21,  270,  271,
   45,   60,   41,   62,  257,   44,   40,   44,   60,   41,
   62,   59,  257,  258,   60,   41,   62,   17,   41,  257,
   43,   60,   45,   62,   79,    9,  258,   40,   60,   44,
   62,   31,   32,   40,   60,  109,   62,   99,   40,  101,
   27,   28,   59,   45,   41,  119,   60,   41,   62,  111,
   40,  259,   40,   88,   43,   45,   45,   45,   40,   46,
   40,   48,  257,   45,   41,   45,   59,   67,   45,  104,
   45,   55,   56,  270,  271,   99,  257,  101,  113,  260,
  257,  116,  257,  264,  257,  260,  110,  111,  112,  264,
  125,  272,   79,  268,   78,   59,  257,  272,  122,  260,
   52,   53,  257,  264,  261,  260,   59,   61,  170,  264,
   41,  272,  273,  268,  269,  257,   41,  272,  260,   41,
  257,  121,  264,  260,   41,  267,   43,  264,   45,  266,
  272,  166,  261,  133,  258,  272,  136,  257,  257,  267,
  260,  260,  270,  271,  264,  264,  170,  266,  268,  173,
   59,   41,  272,  272,   59,  257,  257,  267,  260,  260,
  270,  271,  264,  264,  262,  267,  257,  258,  257,  261,
  272,  272,   59,  257,  265,  267,  261,  263,  270,  271,
  263,  262,  261,  275,  276,  277,  278,  279,  280,  261,
  275,  276,  277,  278,  279,  280,  275,  276,  277,  278,
  279,  280,  261,  275,  276,  277,  278,  279,  280,  261,
  259,   44,  257,  258,  262,  261,  275,  276,  277,  278,
  279,  280,  259,  275,  276,  277,  278,  279,  280,  275,
  276,  277,  278,  279,  280,   44,  275,  276,  277,  278,
  279,  280,   59,  275,  276,  277,  278,  279,  280,  275,
  276,  277,  278,  279,  280,  257,  258,  270,  271,   59,
  257,  275,  276,  277,  278,  279,  280,  257,  258,  257,
  258,  263,  274,  270,  271,  257,  258,  257,  258,   59,
  257,  258,  257,  258,   41,   41,   43,   43,   45,   45,
  258,  267,  259,  259,  270,  271,  259,  268,   59,   59,
   41,  259,    5,  268,   59,   59,   59,  268,   59,   59,
   59,   59,  136,   31,  128,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
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
"\":=\"","\"||\"","\"&&\"","\"<>\"","\"==\"","\"<=\"","\">=\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';'",
"bloque_sentencias_declarativas : sentencia_declarativa",
"bloque_sentencias_declarativas : bloque_sentencias_declarativas sentencia_declarativa",
"sentencia_declarativa : declaracionDatos",
"sentencia_declarativa : declaracionFuncion",
"declaracionDatos : tipo factor ','",
"declaracionDatos : tipo factor",
"declaracionFuncion : tipo FUNC ID parametro sentencias_declarativas_datos BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
"declaracionFuncion : tipo FUNC ID parametro sentencias_declarativas_datos BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
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

//#line 143 "gramatica.y"
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
//#line 401 "Parser.java"
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
case 7:
//#line 29 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
break;
case 11:
//#line 37 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion");}
break;
case 12:
//#line 38 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'");}
break;
case 13:
//#line 39 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");}
break;
case 14:
//#line 40 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'");}
break;
case 15:
//#line 41 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','");}
break;
case 17:
//#line 45 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 18:
//#line 46 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 19:
//#line 47 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 20:
//#line 48 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 21:
//#line 49 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 23:
//#line 53 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 24:
//#line 54 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
break;
case 25:
//#line 55 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 37:
//#line 77 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID");}
break;
case 40:
//#line 82 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 41:
//#line 83 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 55:
//#line 107 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion");}
break;
case 56:
//#line 108 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");}
break;
case 58:
//#line 110 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion");}
break;
case 59:
//#line 111 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");}
break;
case 71:
//#line 130 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena");}
break;
case 72:
//#line 131 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena");}
break;
//#line 642 "Parser.java"
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
        yychar = this.analizadorLexico.yylex(yylval);        //get next character
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
