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
    0,    0,    1,    4,    2,    2,    5,    5,    5,    6,
    6,   11,   10,   10,   12,    7,   13,   14,    8,   15,
   19,   19,   19,   19,   18,   18,   18,   18,   18,   18,
   22,   16,   16,   17,   23,   23,   23,   25,   25,   25,
    3,    3,   26,   26,   26,   26,   31,   32,   27,   27,
   34,   35,   35,   35,   35,   24,   24,   24,   36,   36,
   36,   36,   36,   36,   33,   33,   33,    9,    9,   29,
   29,   29,   37,   37,   38,   38,   39,   20,   20,   40,
   40,   40,   40,   40,   40,   40,   40,   28,   28,   28,
   21,   30,   42,   42,   42,   41,   43,   44,   45,   48,
   46,   47,   49,
};
final static short yylen[] = {                            2,
    6,    2,    1,    1,    1,    2,    1,    1,    1,    3,
    3,    1,    3,    1,    1,    5,    1,    5,    8,    4,
    8,    7,    7,    7,    4,    3,    3,    3,    2,    3,
    1,    1,    2,    2,    3,    2,    2,    1,    4,    2,
    1,    2,    1,    1,    1,    1,    4,    1,    4,    3,
    1,    3,    1,    2,    2,    3,    3,    1,    3,    3,
    1,    3,    3,    1,    1,    1,    2,    1,    1,    7,
    6,    6,    1,    2,    2,    1,    2,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    5,    4,    4,
    1,    8,    2,    1,    2,    3,    1,    3,    3,    1,
    3,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    3,    0,    0,    2,   12,   68,   69,    0,    0,
    5,    7,    8,    9,    0,    0,    0,   17,    0,    0,
    6,   15,    0,    0,   14,    0,    0,    0,   51,    0,
    0,    0,    0,    0,   43,   44,   45,   46,    0,    4,
    0,   10,    0,   11,    0,    0,    0,    0,   66,    0,
    0,    0,    0,   64,    0,   61,    0,   91,    0,    0,
    0,    0,   42,    0,    0,    0,    0,    0,    0,   20,
   13,    0,   32,    0,    0,    0,   16,    0,   67,   80,
   81,   82,   83,   84,   85,   86,   87,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  100,    0,    0,   97,
    0,    1,    0,    0,   54,   50,    0,   29,    0,    0,
    0,    0,    0,   33,    0,    0,    0,    0,    0,   76,
   38,    0,    0,    0,    0,    0,    0,   65,    0,   62,
   59,   63,   60,   90,    0,   89,    0,    0,    0,   49,
   52,   27,    0,   28,    0,   26,    0,    0,    0,   34,
    0,    0,    0,   40,   75,    0,    0,    0,    0,   74,
    0,   47,   88,    0,  103,   96,    0,    0,  102,   99,
   25,    0,    0,    0,   36,    0,   18,    0,    0,   72,
   77,   71,    0,    0,    0,    0,    0,    0,    0,    0,
   35,   19,   39,   70,   93,   95,    0,   98,    0,    0,
    0,    0,    0,   92,   23,    0,   24,   22,   21,
};
final static short yydgoto[] = {                          3,
    4,   10,   33,   41,   11,   12,   13,   14,   15,   24,
   16,   25,   19,   47,   17,   74,  113,   70,   75,   52,
   60,  145,  150,   53,  120,   34,   35,   36,   37,   38,
   54,   55,   56,   39,   67,   57,  122,  123,  160,   89,
   98,  185,   99,  166,  100,  167,  170,  101,  168,
};
final static short yysindex[] = {                       -97,
  -44,    0,    0, -151,    0,    0,    0,    0, -222, -182,
    0,    0,    0,    0, -196, -199, -151,    0,    7,  -71,
    0,    0, -187,  -19,    0,   36, -125,  -98,    0,  -29,
  -34,   37, -181,  -71,    0,    0,    0,    0,  -27,    0,
  -26,    0, -199,    0,  -93, -171,   48,    0,    0,  -24,
 -162,  128,   -7,    0,   72,    0,   44,    0, -149,   86,
 -127,   76,    0,   -6,  -24,   70,   84,   52, -110,    0,
    0,  103,    0,  -89,  -71,  141,    0,  116,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -139,  -24,  -24,
  -24,   -1,  -24,  -24,   25,  109,    0,  157,  145,    0,
  149,    0,  147,  142,    0,    0,  170,    0,  -31,  171,
   -3,   20,  155,    0,  -98,  156, -102,  -71, -139,    0,
    0,  -45,  -49,  -41,   -7,   44,   44,    0,  178,    0,
    0,    0,    0,    0,  176,    0,  -18,    2,    3,    0,
    0,    0,    0,    0,  219,    0,  -24,  134,  -24,    0,
  154,    6,  229,    0,    0,   13,    8,  216, -102,    0,
  221,    0,    0, -116,    0,    0,  223,  140,    0,    0,
    0,  122,  241,  162,    0,  227,    0,  228,  242,    0,
    0,    0,  244, -116,   39,    3,  -24, -149,  -36, -149,
    0,    0,    0,    0,    0,    0,  245,    0,   -7,  247,
 -149,  249,  252,    0,    0,  253,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -219,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -40,    0,    0,
    0,    0,   64,    0,    0,    0,  -14,    0,    0,    0,
    0,    0,    0,    0,    0,  254,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  267,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   66,    0,   90,   12,   38,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   26,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  268,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   62,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  273,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  316,   -2,    0,   14,    0,    0,    0,   22,  318,
    0,  292,    0,    0,    0,    0,    0,    0,    0,  -33,
  -50,    0,    0,  -13, -105,  -55,    0,    0,    0,    0,
   98,    0,    9,    0,  274,  118,  -60,    0,    0,  169,
    0,  164,    0,    0,    0,    0,  153,    0,    0,
};
final static int YYTABLESIZE=421;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   65,   65,   65,   65,   65,   59,   65,  201,   95,  144,
   50,  155,   65,   68,    5,   51,   78,   51,   65,   65,
   51,   65,  121,   21,   43,   66,   58,  124,   58,   58,
   58,   63,  121,   65,   18,   90,  147,   91,   51,   42,
   21,   51,   73,   51,   58,   58,   41,   58,   41,   46,
   66,  104,   56,  181,   56,   56,   56,   22,  157,  149,
   22,  121,   69,  121,   51,  135,   31,   28,   23,   40,
   56,   56,  114,   56,    6,  125,   61,  148,   57,   43,
   57,   57,   57,  134,   20,   94,   62,    7,    8,  109,
   93,    9,  108,   76,   44,   79,   57,   57,  151,   57,
  129,  131,  133,  121,   78,    6,   77,   78,  184,   58,
  105,   92,   90,  172,   91,  156,  116,   29,    7,    8,
   30,  117,    9,   78,   31,   78,   96,  118,  184,   97,
   79,    6,   32,   79,  102,  174,  153,  200,  202,  203,
   29,   45,  106,   30,    7,    8,  110,   31,    9,   79,
  206,   79,  183,  116,   29,   32,  119,   30,    1,    2,
  111,   31,  189,   29,  118,  188,   30,  136,   88,   32,
   31,    7,    8,  199,  173,   87,  112,   86,   32,   72,
  115,   87,  141,   86,   90,   29,   91,   87,   30,   86,
  130,  132,   31,   87,  175,   86,   90,  137,   91,   87,
   32,   86,  191,  138,   90,  140,   91,  126,  127,  139,
  142,  146,  159,  152,  154,   65,   65,  158,  162,   65,
   65,  161,   58,   65,   58,  143,   65,   48,   49,   48,
   49,   65,   48,   49,  163,   65,   65,   65,   65,   65,
   65,   58,   58,    7,    8,   58,   58,   64,  164,   58,
   48,   49,   58,   48,   49,  128,   49,   58,  165,  171,
  169,   58,   58,   58,   58,   58,   58,   56,   56,  177,
  179,   56,   56,  176,  180,   56,   48,   49,   56,  182,
  178,  186,   30,   56,  190,  192,  193,   56,   56,   56,
   56,   56,   56,   57,   57,   30,   30,   57,   57,   30,
  194,   57,  195,  204,   57,  205,  197,  207,  107,   57,
  208,  209,   53,   57,   57,   57,   57,   57,   57,   78,
   78,    7,    8,   78,   78,   55,   37,   78,   73,   94,
   78,  101,   27,   26,   71,   78,  187,  103,  198,   78,
   78,   78,   78,   78,   78,   79,   79,  196,    0,   79,
   79,    0,    0,   79,    0,    0,   79,    0,    0,    0,
    0,   79,    0,    0,    0,   79,   79,   79,   79,   79,
   79,  116,   29,    0,    0,   30,  117,    0,    0,   31,
    0,    0,  118,    0,    0,    0,    0,   32,    0,    0,
    0,   80,   81,   82,   83,   84,   85,   80,   81,   82,
   83,   84,   85,   80,   81,   82,   83,   84,   85,   80,
   81,   82,   83,   84,   85,   80,   81,   82,   83,   84,
   85,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   42,   43,   44,   45,   40,   47,   44,   59,   41,
   40,  117,   40,   40,   59,   45,   50,   45,   59,   60,
   45,   62,   78,   10,   44,   39,   41,   88,   43,   44,
   45,   34,   88,   40,  257,   43,   40,   45,   45,   59,
   27,   45,   45,   45,   59,   60,  266,   62,  268,   28,
   64,   65,   41,  159,   43,   44,   45,  257,  119,   40,
  257,  117,   41,  119,   45,   41,   41,   61,  265,  257,
   59,   60,   75,   62,  257,   89,   40,  111,   41,   44,
   43,   44,   45,   59,  267,   42,  268,  270,  271,   68,
   47,  274,   41,  265,   59,  258,   59,   60,  112,   62,
   92,   93,   94,  159,   41,  257,   59,   44,  164,  259,
   41,   40,   43,  147,   45,  118,  256,  257,  270,  271,
  260,  261,  274,   60,  264,   62,   41,  267,  184,  257,
   41,  257,  272,   44,   59,  149,  115,  188,  189,  190,
  257,  267,   59,  260,  270,  271,  257,  264,  274,   60,
  201,   62,  269,  256,  257,  272,   41,  260,  256,  257,
   58,  264,   41,  257,  267,   44,  260,   59,   41,  272,
  264,  270,  271,  187,   41,   60,  266,   62,  272,  273,
   40,   60,   41,   62,   43,  257,   45,   60,  260,   62,
   93,   94,  264,   60,   41,   62,   43,   41,   45,   60,
  272,   62,   41,   59,   43,   59,   45,   90,   91,   61,
   41,   41,  262,   59,   59,  256,  257,  263,   41,  260,
  261,  263,  259,  264,  259,  257,  267,  257,  258,  257,
  258,  272,  257,  258,   59,  276,  277,  278,  279,  280,
  281,  256,  257,  270,  271,  260,  261,  275,  267,  264,
  257,  258,  267,  257,  258,  257,  258,  272,  257,   41,
  258,  276,  277,  278,  279,  280,  281,  256,  257,   41,
  263,  260,  261,  268,   59,  264,  257,  258,  267,   59,
  268,   59,  257,  272,   44,   59,   59,  276,  277,  278,
  279,  280,  281,  256,  257,  270,  271,  260,  261,  274,
   59,  264,   59,   59,  267,   59,  268,   59,  257,  272,
   59,   59,   59,  276,  277,  278,  279,  280,  281,  256,
  257,  270,  271,  260,  261,   59,   59,  264,  263,  268,
  267,   59,   17,   16,   43,  272,  168,   64,  186,  276,
  277,  278,  279,  280,  281,  256,  257,  184,   -1,  260,
  261,   -1,   -1,  264,   -1,   -1,  267,   -1,   -1,   -1,
   -1,  272,   -1,   -1,   -1,  276,  277,  278,  279,  280,
  281,  256,  257,   -1,   -1,  260,  261,   -1,   -1,  264,
   -1,   -1,  267,   -1,   -1,   -1,   -1,  272,   -1,   -1,
   -1,  276,  277,  278,  279,  280,  281,  276,  277,  278,
  279,  280,  281,  276,  277,  278,  279,  280,  281,  276,
  277,  278,  279,  280,  281,  276,  277,  278,  279,  280,
  281,
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
"programa : nombre_programa bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';'",
"programa : error ';'",
"nombre_programa : ID",
"nombre_ambito : ID",
"bloque_sentencias_declarativas : sentencia_declarativa",
"bloque_sentencias_declarativas : bloque_sentencias_declarativas sentencia_declarativa",
"sentencia_declarativa : declaracionDatos",
"sentencia_declarativa : declaracionNuevoTipo",
"sentencia_declarativa : declaracionFuncion",
"declaracionDatos : tipo conjunto_declaracion_variables ';'",
"declaracionDatos : nombre_typedef conjunto_declaracion_variables ';'",
"nombre_typedef : ID",
"conjunto_declaracion_variables : conjunto_declaracion_variables ',' nombre_declaracion",
"conjunto_declaracion_variables : nombre_declaracion",
"nombre_declaracion : ID",
"declaracionNuevoTipo : TYPEDEF nombre_funcion_typedef '=' encabezado_funcion_typedef ';'",
"nombre_funcion_typedef : ID",
"encabezado_funcion_typedef : tipo FUNC '(' tipo ')'",
"declaracionFuncion : cabeza_funcion bloque_sentencias_declarativas BEGIN cuerpo_funcion cuerpo_retorno ';' END ';'",
"cabeza_funcion : tipo FUNC nombre_ambito parametro",
"pre_condicion : PRE ':' '(' condicion ')' ',' cadena_cuerpo ';'",
"pre_condicion : PRE ':' condicion ')' ',' cadena_cuerpo ';'",
"pre_condicion : PRE ':' '(' condicion ',' cadena_cuerpo ';'",
"pre_condicion : PRE ':' '(' condicion ')' cadena_cuerpo ';'",
"parametro : '(' tipo nombre_parametro ')'",
"parametro : tipo ID ')'",
"parametro : '(' ID ')'",
"parametro : '(' tipo ')'",
"parametro : '(' ')'",
"parametro : '(' tipo ID",
"nombre_parametro : ID",
"cuerpo_funcion : conjunto_sentencia_ejecutable",
"cuerpo_funcion : pre_condicion conjunto_sentencia_ejecutable",
"cuerpo_retorno : RETURN retorno",
"retorno : '(' expresion ')'",
"retorno : expresion ')'",
"retorno : '(' expresion",
"bloque_sentencias_ejecutables : sentencia_ejecutable",
"bloque_sentencias_ejecutables : BEGIN conjunto_sentencia_ejecutable END ';'",
"bloque_sentencias_ejecutables : error ';'",
"conjunto_sentencia_ejecutable : sentencia_ejecutable",
"conjunto_sentencia_ejecutable : sentencia_ejecutable conjunto_sentencia_ejecutable",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : mensaje_pantalla",
"sentencia_ejecutable : clausula_seleccion_if",
"sentencia_ejecutable : sentencia_control_repeat",
"invocacion_funcion : nombre_invocacion '(' factor ')'",
"nombre_invocacion : ID",
"asignacion : operador_asignacion \":=\" expresioncompuesta ';'",
"asignacion : operador_asignacion expresioncompuesta ';'",
"operador_asignacion : ID",
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
"clausula_seleccion_if : IF '(' condicion ')' cuerpo_if ENDIF ';'",
"clausula_seleccion_if : IF condicion ')' cuerpo_if ENDIF ';'",
"clausula_seleccion_if : IF '(' condicion cuerpo_if ENDIF ';'",
"cuerpo_if : cuerpo_then",
"cuerpo_if : cuerpo_then cuerpo_else",
"cuerpo_then : THEN bloque_sentencias_ejecutables",
"cuerpo_then : bloque_sentencias_ejecutables",
"cuerpo_else : ELSE bloque_sentencias_ejecutables",
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
"mensaje_pantalla : PRINT '(' cadena_cuerpo ')' ';'",
"mensaje_pantalla : PRINT cadena_cuerpo ')' ';'",
"mensaje_pantalla : PRINT '(' cadena_cuerpo ';'",
"cadena_cuerpo : CADENA",
"sentencia_control_repeat : REPEAT '(' declaracion_repeat ')' BEGIN conjunto_sentencias_repeat END ';'",
"conjunto_sentencias_repeat : BREAK ';'",
"conjunto_sentencias_repeat : sentencia_ejecutable",
"conjunto_sentencias_repeat : sentencia_ejecutable conjunto_sentencias_repeat",
"declaracion_repeat : nodo_asignacion ';' nodo_condicion",
"nodo_asignacion : asignacion_repeat",
"nodo_condicion : condicion_repeat ';' constante_repeat",
"asignacion_repeat : variable_repeat '=' constante_repeat",
"variable_repeat : ID",
"condicion_repeat : id_repeat operador_logico expresion",
"constante_repeat : CTE",
"id_repeat : ID",
};

//#line 218 "gramatica.y"
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	public List<String> pendingTypeList;
	public String lastFuncType;
	public ParserVal raiz;
	public ArrayList<ParserVal> listaFunc;
	public List<String> erroresSem;
	public ParserVal aux_i; //para mantener constancia del repeat
	public ParserVal aux_m; // para mantener constancia del repeat
	
	private int yylex() 
	{
		return analizadorLexico.yylex(yylval);
	}
	
	public Parser(AnalizadorLexico a_lex)
	{
		this.analizadorLexico = a_lex;
		this.erroresSint = new ArrayList<String>();
		this.reglas = new ArrayList<String>();
		this.pendingTypeList = new ArrayList<String>();
		this.listaFunc= new ArrayList<ParserVal>();
		this.erroresSem = new ArrayList<String>();
	}
	
	public void yyerror(String error)
	{
		this.erroresSem.add("error : "+error+" no identificado");
	}
	
	private void set_campo (ParserVal identificador, String campo, String contenido) 
	{
		analizadorLexico.tabla_simbolos.get(identificador.sval).put(campo,contenido);
	}
	private void apilar_ambito(ParserVal ambito)
	{
		int nuevo_ambito_index = ambito.sval.indexOf('.');
		String nuevo_ambito = "";
		if (nuevo_ambito_index > 0) { //desapilar recursion de ambitos en los nombres de variables en el primer .
			nuevo_ambito = ambito.sval.substring(0,nuevo_ambito_index);
		}
		else {
			nuevo_ambito = ambito.sval;
		}
		analizadorLexico.ambito = analizadorLexico.ambito + "." + nuevo_ambito;
	}
	
	private void desapilar_ambito()
	{
		int separacion = analizadorLexico.ambito.lastIndexOf('.');
		if (separacion >= 0) 
		{
			analizadorLexico.ambito = analizadorLexico.ambito.substring(0,separacion);
		}
	}
	
	private void chequeoS_variable_no_declarada(ParserVal variable)
	{
		//Averigua si la variable esta declarada y en el alcance del ambito donde se la utiliza
		String var = getEntradaValidaTS(variable);
		
		if (var != null) 
		{
			if (isFuncion(var))
			{
				chequeoS_funcion_no_declarada(variable);
				return;
			}

			try {
				if (analizadorLexico.tabla_simbolos.get(var).get("uso").compareTo("variable") == 0 || analizadorLexico.tabla_simbolos.get(var).get("uso").compareTo("parametro") == 0)
				{
					return;
				}
				this.erroresSem.add("error semantico: variable " + variable.sval + " no declarada");
			} catch (Exception e) {}
		}
		this.erroresSem.add("error semantico: variable " + variable.sval + " no declarada");
		return;
	
	}
	
	private void chequeoS_redeclaracion_variable(ParserVal variable)
	{
		// buscar en la tabla de simbolos si esta (no hace falta por campo uso sino que no pertenezcan al mismo ambito y listo)
		// pero si hay que verificar que 'uso' no sea null porque sino, no esta declarada.
		
		String var = getEntradaValidaTS(variable);
		if (var != null)
		{
			try {
				if (analizadorLexico.tabla_simbolos.get(var).get("uso").compareTo("variable") == 0)
				{
					this.erroresSem.add("error semantico: variable " + variable.sval + " ya declarada");
					return;
				}
			} catch (Exception e) {}
		}
		return;
	}
	
	private void reverificar_cte_negativa (ParserVal variable)
	{
		String new_variable = "-" + variable.sval;
		String var = getEntradaValidaTS(variable);
		String tipo = analizadorLexico.tabla_simbolos.get(var).get("tipo");
		boolean fuera_de_rango = false;
		if (tipo.compareTo("INT") == 0)
		{
			long aux= Long.parseLong(new_variable);
			int limite_sup= (int) (Math.pow(2, 15)-1);
			int limite_inf= (int) (-Math.pow(2, 15));
			if (aux>limite_sup || aux<limite_inf) {
				fuera_de_rango = true;
			}
		}
		else if (tipo.compareTo("SINGLE") == 0)
		{
			double base;
			double exp=1.0;
			String[] cadena_dividida= new_variable.split("S", 0);
			base= Double.parseDouble(cadena_dividida[0]);
			if(cadena_dividida.length>1){						//Si tiene S
				exp=Double.parseDouble(cadena_dividida[1]);
			}
			double valor= Math.pow(base, exp);
			if (valor == 0 && base != 0) {
				analizadorLexico.erroresLex.add("Error en la linea "+ analizadorLexico.contadorLineas + ": constante fuera de rango");
				fuera_de_rango = true;
			}
			
			double lim1= Math.pow(1.17549435, -38); // 1.17549435S-38
			double lim2= Math.pow(3.40282347, 38);// 3.40282347S+38
			double lim3= Math.pow(-3.40282347, 38);//-3.40282347S+38 
			double lim4= Math.pow(-1.17549435, -38);//-1.17549435S-38
			if(valor<lim1 || valor>lim2) {
				if(valor<lim3 || valor>lim4) {
					if(valor!=0) {
						fuera_de_rango = true;
				}}
			}else if(valor<lim3 || valor>lim4) {
				if(valor<lim1 || valor>lim2) {
					if(valor!=0) {
						fuera_de_rango = true;
					}
				}
			}
		}
		
		if (!fuera_de_rango)
		{
			analizadorLexico.tabla_simbolos.put(new_variable, new HashMap<String,String>());
			analizadorLexico.tabla_simbolos.get(new_variable).put("tipo", tipo);
			analizadorLexico.tabla_simbolos.get(new_variable).put("uso", "constante");
		}
		if (fuera_de_rango)
		{
			analizadorLexico.erroresLex.add("error semantico: variable negativa fuera de rango");
		}
		variable.sval = new_variable;
	}
	
	private void chequeoS_funcion_no_declarada (ParserVal funcion)
	{
		String func = getEntradaValidaTS(funcion);
		
		if (func != null) 
		{
			boolean isTypeDef = isTypeDef(getTipoVariable(funcion.sval));
			if (isTypeDef)
			{
				funcion.sval = getTipoVariable(funcion.sval);
				chequeoS_typedef_existe(funcion);
				return;
			}
			try {
				if (analizadorLexico.tabla_simbolos.get(func).get("uso").compareTo("funcion") == 0)
				{
					return;
				}
				this.erroresSem.add("error semantico: funcion " + funcion.sval + " no declarada");
			} catch (Exception e) {}
		}
		this.erroresSem.add("error semantico: funcion " + funcion.sval + " no declarada");
		return;
	}
	
	private void chequeoS_redeclaracion_funcion (ParserVal funcion)
	{
		boolean funcion_redeclarada = false;
		// buscar en la tabla de simbolos si esta (no hace falta por campo uso sino que no pertenezcan al mismo ambito y listo)
		// pero si hay que verificar que 'uso' no sea null porque sino, no esta declarada.

			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(funcion.sval) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null)  
	            {
	            	if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("funcion") == 0)
	                	funcion_redeclarada = true;
	            }
	        }
		
		if (funcion_redeclarada)
		{
			this.erroresSem.add("error semantico: funcion " + funcion.sval + " ya declarada");
		}	
	}
	
	private void addPendingTypeList(String identificador)
	{
		pendingTypeList.add(identificador);
	}
	
	private void setPendingTypes(String tipo)
	{
	    for(String identificador : pendingTypeList) {
     		analizadorLexico.tabla_simbolos.get(identificador).put("tipo",tipo);
    	}
	    pendingTypeList.clear();
	}
	
	public String getTipoVariable(String id)
	{
		//Esta funcion recibe tanto constantes como identificadores, por lo que hace falta buscarlo a secas el loop de ambito/alcance
		try {
		if (analizadorLexico.tabla_simbolos.get(id).get("uso") != null)
		{
			return analizadorLexico.tabla_simbolos.get(id).get("tipo");
		}
		}
		catch (Exception e) {}

		String variable_ambito = id;
		while (variable_ambito.lastIndexOf('.') != -1)
		{	
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null)
	            {//Se compara uso ya que la variable debe estar declarada para que tenga tipo
	                return analizadorLexico.tabla_simbolos.get(entry.getKey()).get("tipo");
	            }
	        }
	        int ambito_index = variable_ambito.lastIndexOf('.');
			if (ambito_index > 0) {
				variable_ambito = variable_ambito.substring(0,ambito_index);
			}
		
		}
		
		return "nulo";
	}

	private void chequeoS_diferentes_tipos(ParserVal id1, ParserVal id2, boolean condicion_repeat)
	{
		
		
		String tipo_1 = getTipoVariable(id1.sval);
		String tipo_2 = getTipoVariable(id2.sval);
		
		
		
		if (getTipoVariable(tipo_1) != "nulo") //es typedef
		{
			tipo_1 = getTipoVariable(tipo_1);
			if (!isFuncion(id2.sval))
				this.erroresSem.add("error semantico: " + id2.sval +"("+tipo_2+ ") debe ser una funcion de retorno " + tipo_1); 
		}
		if (getTipoVariable(tipo_2) != "nulo") //es typedef
		{
			tipo_2 = getTipoVariable(tipo_2);
		}
		
		if (condicion_repeat == true) // CONDICION REPEAT DEBEN SER DE TIPO ENTERO
		{
			if (tipo_1.compareTo("INT") != 0 || tipo_2.compareTo("INT") != 0)
			{
				this.erroresSem.add("error semantico: en una sentencia repeat, los datos de condicion deben ser de tipo entero (INT)");
				return;
			}
		}
		
		if (tipo_1.compareTo(tipo_2) == 0 && tipo_1 != "nulo")
		{
			return;
		}

		this.erroresSem.add("error semantico: " + id1.sval +"("+tipo_1+ ") y " + id2.sval +"("+tipo_2+") son de tipos incompatibles para la operacion."); 
	}
	
	public String getEntradaValidaTS(String entrada)
	{
		//Esta funcion devuelve una referencia a la tabla de simbolos valida (de existir) de una entrada (en el alcance de su ambito), tanto ID como CTE.
		String iterador_entrada = entrada;
		boolean no_quedan_ambitos = false;
		while (!no_quedan_ambitos)
		{
			try {
				String entrada_actual_uso = analizadorLexico.tabla_simbolos.get(iterador_entrada).get("uso");
				if (entrada_actual_uso != null)
					{return iterador_entrada;}
			}
			catch (Exception e) { }
			
			int ambito_index = iterador_entrada.lastIndexOf('.');
			if (ambito_index > 0) {
				iterador_entrada = iterador_entrada.substring(0,ambito_index);
			} else {no_quedan_ambitos = true;}
		}
		return null;
	}
	
	private void setLastFuncType (ParserVal func) //Tanto typedef como funcion
	{
		lastFuncType = func.sval;
	}
	
	private void setFuncType (String campo, String contenido)
	{
		analizadorLexico.tabla_simbolos.get(lastFuncType).put(campo,contenido);
	}
	
	private void copiarTipoParametro (ParserVal copiar, ParserVal pegar)
	{
		String tipo = getTipoVariable(copiar.sval);
		analizadorLexico.tabla_simbolos.get(pegar.sval).put("parametro",tipo);
	}
	
	public boolean isTypeDef(String func)
	{
		try {
			if (analizadorLexico.tabla_simbolos.get(func).get("tipo") != null)
				return true;
		} catch (Exception e) {}
		return false;
	}
	
	private boolean isFuncion(String id)
	{
		if (analizadorLexico.tabla_simbolos.get(id).get("uso") != null)
		{
			if (analizadorLexico.tabla_simbolos.get(id).get("uso").compareTo("funcion") == 0)
				return true;
		}
		return false;
	}
	
	private void chequeoS_parametro_funcion(ParserVal funcion, ParserVal parametro)
	{
		String func = getEntradaValidaTS(funcion);
		String par = getEntradaValidaTS(parametro);
		
		try {
			String tipo_par_func = analizadorLexico.tabla_simbolos.get(func).get("tipo_parametro");
			String tipo_par = analizadorLexico.tabla_simbolos.get(par).get("tipo");
			if (isTypeDef(tipo_par_func)) {
				tipo_par_func = analizadorLexico.tabla_simbolos.get(tipo_par_func).get("tipo");
			}
			
			if (tipo_par_func.compareTo(tipo_par) == 0 && tipo_par_func != null)
			{
				return;
			}
			this.erroresSem.add("error semantico: el parametro de " + funcion.sval + " debe ser de tipo " + tipo_par_func +" y no de tipo " + tipo_par); 
		} catch (Exception e) { }
		return;
		
	}
	
	private void chequeoS_typedef_existe(ParserVal var)
	{
		String typedef = getEntradaValidaTS(var);
		try
		{
			String uso_typedef = analizadorLexico.tabla_simbolos.get(typedef).get("uso");
			if (uso_typedef.compareTo("typedef") == 0)
				return;
		} catch (Exception e){}
		this.erroresSem.add("error semantico: no existe un typedef de nombre " + var.sval); 
		return;
	}
	
	private void chequeoS_repeat_tipo_entero(ParserVal var, String tipo)
	{
		String variable = getEntradaValidaTS(var);
		try
		{
			String tipo_variable = analizadorLexico.tabla_simbolos.get(variable).get("tipo");
			if (tipo_variable.compareTo(tipo) == 0)
				return;
		} catch (Exception e){}
		this.erroresSem.add("error semantico: la variable o constante deberia ser de tipo entero");
		return;
		
	}

	private void chequeoS_operador_valido(ParserVal op)
	{
		String operador = getEntradaValidaTS(op);
		try
		{
			String uso_operador = analizadorLexico.tabla_simbolos.get(operador).get("uso");
			if (uso_operador.compareTo("variable") == 0)
				return;
		} catch (Exception e){}
		this.erroresSem.add("error semantico: operador de asignacion invalido, " + op.sval + " no es una variable.");
		return;
	}
	
	private String getEntradaValidaTS(ParserVal entrada)
	{
		//Esta funcion devuelve una referencia a la tabla de simbolos valida (de existir) de una entrada (en el alcance de su ambito), tanto ID como CTE.
		String iterador_entrada = entrada.sval;
		boolean no_quedan_ambitos = false;
		while (!no_quedan_ambitos)
		{
			try {
				String entrada_actual_uso = analizadorLexico.tabla_simbolos.get(iterador_entrada).get("uso");
				if (entrada_actual_uso != null)
					{return iterador_entrada;}
			}
			catch (Exception e) { }
			
			int ambito_index = iterador_entrada.lastIndexOf('.');
			if (ambito_index > 0) {
				iterador_entrada = iterador_entrada.substring(0,ambito_index);
			} else {no_quedan_ambitos = true;}
		}
		return null;
	}
	
	private void chequeoS_repeat_set_i (ParserVal i)
	{
		aux_i = i;
	}
	
	private void chequeoS_repeat_check_i (ParserVal i)
	{
		if (aux_i.sval.compareTo(i.sval) != 0)
		{
			System.out.println("CHECK:" + i.sval);
			this.erroresSem.add("error semantico: en una sentencia repeat se debe comparar usando la variable de control");
		}
		return;
	}
	
	public void imprimirArbol(ParserVal nodo){
        
        Nodo clon=null;
		try {
		clon = (Nodo)nodo.obj;
		}catch (Exception e) {
		
		}
		if(clon!=null){
			if(!clon.nodoAux()){
        		System.out.println(clon);
        	}
        	imprimirArbol(clon.getIzq());
        	imprimirArbol(clon.getDer());
        } else{
        	return;
        }
	}
	
	
	
	
//#line 919 "Parser.java"
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
{desapilar_ambito(); this.reglas.add("Sentencia START programa"); this.raiz= new ParserVal(new Nodo("Programa", val_peek(5), val_peek(2)));}
break;
case 2:
//#line 16 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
break;
case 3:
//#line 19 "gramatica.y"
{apilar_ambito(val_peek(0)); set_campo(val_peek(0),"uso","programa"); yyval.obj= new Nodo(val_peek(0).sval);}
break;
case 4:
//#line 22 "gramatica.y"
{chequeoS_redeclaracion_funcion(val_peek(0)); apilar_ambito(val_peek(0)); set_campo(val_peek(0),"uso","funcion"); setLastFuncType(val_peek(0)); yyval.sval=val_peek(0).sval;}
break;
case 10:
//#line 34 "gramatica.y"
{setPendingTypes(val_peek(2).sval); this.reglas.add("Declaracion de datos");}
break;
case 11:
//#line 35 "gramatica.y"
{setPendingTypes(val_peek(2).sval); this.reglas.add("Declaracion de datos TYPEDEF");}
break;
case 12:
//#line 37 "gramatica.y"
{chequeoS_typedef_existe(val_peek(0));}
break;
case 15:
//#line 44 "gramatica.y"
{chequeoS_redeclaracion_variable(val_peek(0)); set_campo(val_peek(0),"uso","variable"); addPendingTypeList(val_peek(0).sval);}
break;
case 16:
//#line 47 "gramatica.y"
{this.reglas.add("Declaracion TYPEDEF");}
break;
case 17:
//#line 50 "gramatica.y"
{addPendingTypeList(val_peek(0).sval); set_campo(val_peek(0), "uso", "typedef"); setLastFuncType(val_peek(0));}
break;
case 18:
//#line 53 "gramatica.y"
{setPendingTypes("funcion typedef"); setFuncType("tipo",val_peek(4).sval); setFuncType("parametro",val_peek(3).sval);}
break;
case 19:
//#line 56 "gramatica.y"
{desapilar_ambito(); yyval.obj=new Nodo(val_peek(7).sval, val_peek(4), val_peek(3)); listaFunc.add(yyval);}
break;
case 20:
//#line 59 "gramatica.y"
{setFuncType("tipo",val_peek(3).sval); copiarTipoParametro(val_peek(0),val_peek(1)); yyval.sval=val_peek(1).sval;}
break;
case 21:
//#line 62 "gramatica.y"
{this.reglas.add("pre-condicion"); yyval.obj=new Nodo("PRE", val_peek(4), val_peek(1));}
break;
case 22:
//#line 63 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("pre-condicion");yyval.obj=new Nodo("PRE", val_peek(4), val_peek(1));}
break;
case 23:
//#line 64 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("pre-condicion");yyval.obj=new Nodo("PRE", val_peek(3), val_peek(1));}
break;
case 24:
//#line 65 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); this.reglas.add("pre-condicion");yyval.obj=new Nodo("PRE", val_peek(3), val_peek(1));}
break;
case 25:
//#line 68 "gramatica.y"
{setPendingTypes(val_peek(2).sval);}
break;
case 26:
//#line 69 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 27:
//#line 70 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 28:
//#line 71 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 29:
//#line 72 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 30:
//#line 73 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 31:
//#line 76 "gramatica.y"
{set_campo(val_peek(0),"uso", "parametro"); addPendingTypeList(val_peek(0).sval);}
break;
case 32:
//#line 79 "gramatica.y"
{this.reglas.add("DECLARACION FUNCION"); yyval.obj= new Nodo("cuerpo", null, val_peek(0));}
break;
case 33:
//#line 80 "gramatica.y"
{this.reglas.add("DECLARACION FUNCION Y PRE CONDICION"); yyval.obj= new Nodo("cuerpo", val_peek(1), val_peek(0));}
break;
case 34:
//#line 82 "gramatica.y"
{ yyval.obj=new Nodo("RETURN", val_peek(0), null);}
break;
case 35:
//#line 85 "gramatica.y"
{yyval= val_peek(1);}
break;
case 36:
//#line 86 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion"); yyval= val_peek(1);}
break;
case 37:
//#line 87 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");yyval= val_peek(0);}
break;
case 38:
//#line 90 "gramatica.y"
{yyval.obj= new Nodo("S", val_peek(0), null);}
break;
case 39:
//#line 91 "gramatica.y"
{this.reglas.add("bloque de sentencias BEGIN-END"); yyval= val_peek(2);}
break;
case 40:
//#line 92 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en bloque de sentencias ejecutables");}
break;
case 41:
//#line 95 "gramatica.y"
{yyval.obj= new Nodo("S", val_peek(0), null);}
break;
case 42:
//#line 96 "gramatica.y"
{yyval.obj= new Nodo("S", val_peek(1), val_peek(0));}
break;
case 43:
//#line 99 "gramatica.y"
{yyval= val_peek(0);}
break;
case 44:
//#line 100 "gramatica.y"
{yyval= val_peek(0);}
break;
case 45:
//#line 101 "gramatica.y"
{yyval= val_peek(0);}
break;
case 46:
//#line 102 "gramatica.y"
{yyval= val_peek(0);}
break;
case 47:
//#line 105 "gramatica.y"
{chequeoS_parametro_funcion(val_peek(3), val_peek(2)); yyval.obj=new Nodo("invocacion funcion", val_peek(3), val_peek(1));}
break;
case 48:
//#line 109 "gramatica.y"
{chequeoS_funcion_no_declarada(val_peek(0)); yyval.obj=new Nodo (val_peek(0).sval);}
break;
case 49:
//#line 112 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(1), false); this.reglas.add("Asignacion"); yyval.obj= new Nodo(":=", val_peek(3), val_peek(1));}
break;
case 50:
//#line 113 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion"); yyval.obj= new Nodo(":=", val_peek(2), val_peek(1));}
break;
case 51:
//#line 116 "gramatica.y"
{chequeoS_variable_no_declarada(val_peek(0)); chequeoS_operador_valido(val_peek(0)); yyval.obj= new Nodo(val_peek(0).sval);}
break;
case 52:
//#line 119 "gramatica.y"
{yyval= val_peek(1);}
break;
case 53:
//#line 120 "gramatica.y"
{yyval= val_peek(0);}
break;
case 54:
//#line 121 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion"); yyval= val_peek(1);}
break;
case 55:
//#line 122 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion"); yyval= val_peek(0);}
break;
case 56:
//#line 125 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false); yyval.obj= new Nodo("+", val_peek(2), val_peek(0));}
break;
case 57:
//#line 126 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false); yyval.obj= new Nodo("-", val_peek(2), val_peek(0));}
break;
case 58:
//#line 127 "gramatica.y"
{yyval= val_peek(0);}
break;
case 59:
//#line 130 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false); yyval.obj= new Nodo("/", val_peek(2), val_peek(0));}
break;
case 60:
//#line 131 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false); yyval.obj= new Nodo("*", val_peek(2), val_peek(0));}
break;
case 61:
//#line 132 "gramatica.y"
{yyval= val_peek(0);}
break;
case 62:
//#line 133 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false); yyval.obj= new Nodo("/", val_peek(2), val_peek(0));}
break;
case 63:
//#line 134 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false); yyval.obj= new Nodo("*", val_peek(2), val_peek(0));}
break;
case 64:
//#line 135 "gramatica.y"
{yyval= val_peek(0);}
break;
case 65:
//#line 138 "gramatica.y"
{this.reglas.add("factor ID"); chequeoS_variable_no_declarada(val_peek(0)); yyval.obj=new Nodo(val_peek(0).sval);}
break;
case 66:
//#line 139 "gramatica.y"
{this.reglas.add("Factor CTE"); yyval.obj=new Nodo(val_peek(0).sval);}
break;
case 67:
//#line 140 "gramatica.y"
{yyval.obj=new Nodo("-"+val_peek(1).sval);reverificar_cte_negativa(val_peek(1));  }
break;
case 68:
//#line 143 "gramatica.y"
{this.reglas.add("tipo INT");}
break;
case 69:
//#line 144 "gramatica.y"
{this.reglas.add("tipo SINGLE");}
break;
case 70:
//#line 147 "gramatica.y"
{yyval.obj= new Nodo("IF", val_peek(4), val_peek(2));}
break;
case 71:
//#line 148 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF"); yyval.obj= new Nodo("IF", val_peek(4), val_peek(2));}
break;
case 72:
//#line 149 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("clausula IF"); yyval.obj= new Nodo("IF", val_peek(3), val_peek(2));}
break;
case 73:
//#line 152 "gramatica.y"
{yyval.obj= new Nodo("cuerpo", val_peek(0), null);}
break;
case 74:
//#line 153 "gramatica.y"
{yyval.obj= new Nodo("cuerpo", val_peek(1), val_peek(0));}
break;
case 75:
//#line 156 "gramatica.y"
{yyval.obj= new Nodo("THEN", val_peek(0), null);}
break;
case 76:
//#line 157 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); yyval.obj= new Nodo("THEN", val_peek(0), null);}
break;
case 77:
//#line 160 "gramatica.y"
{yyval.obj= new Nodo("ELSE", val_peek(0), null);}
break;
case 78:
//#line 163 "gramatica.y"
{yyval.obj=new Nodo("condicion", val_peek(0), null);}
break;
case 79:
//#line 164 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false); yyval.obj= new Nodo(val_peek(1).sval, val_peek(2), val_peek(0));}
break;
case 80:
//#line 167 "gramatica.y"
{yyval.sval="||";}
break;
case 81:
//#line 168 "gramatica.y"
{yyval.sval="&&";}
break;
case 82:
//#line 169 "gramatica.y"
{yyval.sval="<>";}
break;
case 83:
//#line 170 "gramatica.y"
{yyval.sval="==";}
break;
case 84:
//#line 171 "gramatica.y"
{yyval.sval="<=";}
break;
case 85:
//#line 172 "gramatica.y"
{yyval.sval=">=";}
break;
case 86:
//#line 173 "gramatica.y"
{yyval.sval=">";}
break;
case 87:
//#line 174 "gramatica.y"
{yyval.sval="<";}
break;
case 88:
//#line 177 "gramatica.y"
{this.reglas.add("clausula PRINT"); yyval.obj= new Nodo("PRINT",val_peek(2), null);}
break;
case 89:
//#line 178 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); this.reglas.add("clausula PRINT"); yyval.obj= new Nodo("PRINT", val_peek(2), null);}
break;
case 90:
//#line 179 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); this.reglas.add("clausula PRINT"); yyval.obj= new Nodo("PRINT", val_peek(1), null);}
break;
case 91:
//#line 182 "gramatica.y"
{yyval.obj=new Nodo(val_peek(0).sval);}
break;
case 92:
//#line 185 "gramatica.y"
{this.reglas.add("Sentencia Ejecutable REPEAT - Chequeo Semantico"); yyval.obj=new Nodo("Repeat", val_peek(5), val_peek(2));}
break;
case 93:
//#line 188 "gramatica.y"
{yyval.obj= new Nodo("S", val_peek(1), null);}
break;
case 94:
//#line 189 "gramatica.y"
{yyval.obj= new Nodo("S", val_peek(0), null);}
break;
case 95:
//#line 190 "gramatica.y"
{yyval.obj= new Nodo("S", val_peek(1), val_peek(0));}
break;
case 96:
//#line 193 "gramatica.y"
{chequeoS_repeat_check_i(val_peek(2)); yyval.obj=new Nodo("declaracion_repeat", val_peek(2), val_peek(0));}
break;
case 97:
//#line 196 "gramatica.y"
{ yyval.obj=new Nodo("asignacion_repeat", val_peek(0),null);}
break;
case 98:
//#line 199 "gramatica.y"
{yyval.obj=new Nodo("condicion repeat", val_peek(2), val_peek(0));}
break;
case 99:
//#line 202 "gramatica.y"
{ chequeoS_repeat_tipo_entero(val_peek(2),"INT"); chequeoS_repeat_tipo_entero(val_peek(1), "INT"); yyval.obj=new Nodo("=", val_peek(2), val_peek(0));}
break;
case 100:
//#line 205 "gramatica.y"
{chequeoS_variable_no_declarada(val_peek(0)); chequeoS_operador_valido(val_peek(0));yyval.obj=new Nodo(val_peek(0).sval);}
break;
case 101:
//#line 208 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), true); this.reglas.add("Condicion_Repeat"); chequeoS_repeat_set_i(val_peek(2)); yyval.obj=new Nodo(val_peek(1).sval, val_peek(2), val_peek(0));}
break;
case 102:
//#line 211 "gramatica.y"
{yyval.obj=new Nodo(val_peek(0).sval);}
break;
case 103:
//#line 214 "gramatica.y"
{yyval.obj=new Nodo(val_peek(0).sval);}
break;
//#line 1452 "Parser.java"
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
