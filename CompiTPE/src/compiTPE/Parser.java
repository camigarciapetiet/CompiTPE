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
    6,   11,   10,   10,   12,    7,   13,   14,    8,    8,
   15,   17,   17,   17,   17,   17,   17,   18,   18,   18,
   18,   18,   18,   20,   16,   16,   16,   16,   22,   22,
    3,    3,    3,   23,   23,   23,   23,   28,   29,   24,
   24,   24,   24,   31,   32,   32,   32,   32,   21,   21,
   21,   33,   33,   33,   33,   33,   33,   30,   30,   30,
    9,    9,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   19,   19,   34,   34,   34,   34,   34,   34,
   34,   34,   25,   25,   25,   27,   36,   36,   36,   35,
   35,
};
final static short yylen[] = {                            2,
    6,    2,    1,    1,    1,    2,    1,    1,    1,    3,
    3,    1,    3,    1,    1,    5,    1,    5,    9,   10,
    4,    8,    7,    7,    7,    7,    7,    4,    3,    3,
    3,    2,    3,    1,    3,    2,    2,    2,    1,    4,
    1,    2,    2,    1,    1,    1,    1,    4,    1,    4,
    3,    3,    4,    1,    3,    1,    2,    2,    3,    3,
    1,    3,    3,    1,    3,    3,    1,    1,    1,    2,
    1,    1,    8,    7,    7,   10,    9,    9,    7,    9,
    5,    7,    1,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    5,    4,    4,   14,    2,    1,    2,    3,
    3,
};
final static short yydefred[] = {                         0,
    0,    3,    0,    0,    2,   12,   71,   72,    0,    0,
    5,    7,    8,    9,    0,    0,    0,   17,    0,    0,
    6,   15,    0,    0,   14,    0,    0,    0,    0,   54,
    0,    0,    0,    0,   41,   44,   45,   46,   47,    0,
    4,    0,   10,    0,   11,    0,    0,    0,   43,    0,
   69,    0,    0,    0,    0,    0,   67,    0,   64,    0,
    0,    0,    0,    0,   42,    0,    0,    0,    0,    0,
    0,   21,   13,    0,    0,    0,    0,   16,    0,    0,
   39,    0,   70,   85,   86,   87,   88,   89,   90,   91,
   92,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,   52,    0,    0,    0,   57,   51,    0,   32,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   68,    0,   65,   62,   66,
   63,   94,   95,    0,    0,   53,   50,   55,   30,    0,
   31,    0,   29,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   81,    0,    0,    0,    0,   48,   93,    0,
   28,    0,    0,    0,   37,    0,    0,   36,    0,   18,
   40,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   35,    0,    0,   82,    0,   75,
    0,    0,    0,   79,    0,   74,    0,    0,    0,    0,
    0,    0,    0,   19,    0,    0,    0,   73,    0,    0,
    0,    0,   24,   25,   26,    0,   27,   23,   20,   78,
    0,   80,   77,  100,  101,    0,   22,   76,    0,    0,
    0,    0,    0,   97,   99,    0,   96,
};
final static short yydgoto[] = {                          3,
    4,   10,   34,   42,   11,   12,   13,   14,   15,   24,
   16,   25,   19,   48,   17,  147,   76,   72,   55,  142,
   56,   80,   81,   36,   37,   38,   39,   57,   58,   59,
   40,   69,   60,   93,  198,  233,
};
final static short yysindex[] = {                      -226,
  -38,    0,    0, -192,    0,    0,    0,    0, -223, -153,
    0,    0,    0,    0, -191, -216, -192,    0,  -11,  -93,
    0,    0, -189,   -7,    0,   43, -123, -158,   12,    0,
   82,  -34,   48,  -80,    0,    0,    0,    0,    0,   60,
    0,  -12,    0, -216,    0, -111, -167,   42,    0,    0,
    0, -141,   92, -151,   21,  114,    0,   85,    0,   38,
   95, -109,  -92,  109,    0,   84,   92,   51,  119,   67,
  -85,    0,    0,  125,  -75,  -93,  147,    0,  -93,  -68,
    0,   29,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -71,   92,   92,   92,   94,   92,   92,  145,  -16,
  159,    0,    0,  -44,  163,  113,    0,    0,  189,    0,
  -28,  191,   88,   90,  -54, -158,  -55, -141,  183, -141,
  -91, -141,  114,   38,   38,    0,  226,    0,    0,    0,
    0,    0,    0,  210,   31,    0,    0,    0,    0,    0,
    0,  229,    0,    8,   35,   65,  272,  190,   90,  293,
  285,   91,    0,   -2, -141,   41,   58,    0,    0,  299,
    0,  319,   15,  320,    0,  314,   98,    0,  308,    0,
    0,  310, -141,  312,   99, -141,  315, -141,  316,  116,
  117,  118,  -35,  120,    0,  321,  110,    0,  121,    0,
 -141,  322,  122,    0,  123,    0,   49,  323,  324,  328,
  329,  -36,  330,    0,  331,  332,  130,    0,  333,  335,
  -50,  137,    0,    0,    0,  337,    0,    0,    0,    0,
  338,    0,    0,    0,    0,  357,    0,    0,  132,   96,
  341,   96,  133,    0,    0,  343,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -40,
    0,    0,    0,    0,    0,    1,    0,    0,    0,  -33,
    0,    0,    0,    0,    0,    0,    0,  344,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  345,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    7,  -27,   -5,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   62,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  346,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  138,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  390,   14,    0,   37,    0,    0,    0,   16,  392,
    0,  365,    0,    0,    0,  261,    0,    0,  -29,    0,
    6,   20,  140,    0,    0,    0,    0,  129,    0,  103,
    0,  347,  169,  214,    0,  180,
};
final static int YYTABLESIZE=413;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   68,   68,   68,   68,   68,   62,   68,   61,  202,   61,
   61,   61,  141,   60,  136,   60,   60,   60,   68,   68,
    5,   68,  217,   82,  134,   61,   61,   70,   61,    1,
    2,   60,   60,   18,   60,   59,   44,   59,   59,   59,
   22,   83,  133,   47,   83,   68,   21,   84,  162,   28,
   84,   43,   54,   59,   59,  183,   59,   71,  182,   75,
   83,   92,   83,   21,    6,   22,   84,   41,   84,  121,
   49,   68,  106,   23,   91,  164,   90,    7,    8,   98,
   91,    9,   90,  145,   97,  111,   44,   63,   91,  115,
   90,  107,  117,   95,   91,   94,   90,   77,  123,   67,
   78,   45,   34,    6,   54,  165,   83,  110,   91,   54,
   90,    7,    8,   20,  163,   30,    7,    8,   31,  148,
    9,   53,   32,   67,   96,   79,   54,  144,  104,  146,
   33,  150,   54,    6,   54,   99,   54,  152,   54,  154,
  156,  157,  103,   46,   29,   30,    7,    8,   31,  100,
    9,  166,   32,  138,  148,   95,   95,   94,   94,   35,
   33,   74,   29,   30,  101,   30,   31,  102,   31,  155,
   32,  112,   32,   65,  175,   79,   30,  108,   33,   31,
   33,   30,  113,   32,   31,   35,  116,   64,   32,  122,
  114,   33,  189,  118,  119,  193,   33,  195,  127,  129,
  131,   30,   30,  132,   31,   31,  224,  225,   32,   32,
  207,  149,  151,   83,   65,   35,   33,   33,   35,  135,
   68,  137,  216,  201,   61,  128,  130,   61,  140,  139,
  168,  143,   95,   60,   94,   68,   68,   68,   68,   68,
   68,  153,   61,   61,   61,   61,   61,   61,   60,   60,
   60,   60,   60,   60,   65,   59,   65,    7,    8,  173,
  174,   83,  124,  125,   50,   51,  158,   84,  159,  161,
   59,   59,   59,   59,   59,   59,   83,   83,   83,   83,
   83,   83,   84,   84,   84,   84,   84,   84,  160,  120,
   84,   85,   86,   87,   88,   89,   84,   85,   86,   87,
   88,   89,  176,  177,   84,   85,   86,   87,   88,   89,
   84,   85,   86,   87,   88,   89,   50,   51,   33,  178,
  179,   50,   51,  109,   84,   85,   86,   87,   88,   89,
  167,   33,   33,  170,   66,   33,    7,    8,   50,   51,
   50,   51,   52,  171,   50,   51,   50,   51,   50,   51,
  126,   51,   30,  172,  185,   31,   95,  180,   94,   32,
  191,  192,  181,  184,  231,  186,  187,   33,  188,  232,
  190,  232,  197,  194,  196,  199,  200,  205,  203,  204,
  208,  212,  213,  206,  209,  210,  214,  215,  218,  219,
  220,  222,  221,  223,  226,  227,  228,  229,  230,  234,
  236,  237,   56,   58,   38,   98,   27,   26,   73,  169,
  211,  235,  105,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   42,   43,   44,   45,   40,   47,   41,   44,   43,
   44,   45,   41,   41,   59,   43,   44,   45,   59,   60,
   59,   62,   59,   53,   41,   59,   60,   40,   62,  256,
  257,   59,   60,  257,   62,   41,   44,   43,   44,   45,
  257,   41,   59,   28,   44,   40,   10,   41,   41,   61,
   44,   59,   45,   59,   60,   41,   62,   42,   44,   46,
   60,   41,   62,   27,  257,  257,   60,  257,   62,   41,
   59,   66,   67,  265,   60,   41,   62,  270,  271,   42,
   60,  274,   62,  113,   47,   70,   44,   40,   60,   76,
   62,   41,   79,   43,   60,   45,   62,  265,   93,   40,
   59,   59,   41,  257,   45,   41,  258,   41,   60,   45,
   62,  270,  271,  267,  144,  257,  270,  271,  260,  114,
  274,   40,  264,   40,   40,  267,   45,   40,   45,   40,
  272,  116,   45,  257,   45,   41,   45,  118,   45,  120,
  121,  122,   59,  267,  256,  257,  270,  271,  260,  259,
  274,  146,  264,   41,  149,   43,   43,   45,   45,   20,
  272,  273,  256,  257,  257,  257,  260,   59,  260,  261,
  264,  257,  264,   34,  155,  267,  257,   59,  272,  260,
  272,  257,   58,  264,  260,   46,   40,  268,  264,  261,
  266,  272,  173,  262,  263,  176,  272,  178,   96,   97,
   98,  257,  257,   59,  260,  260,  257,  258,  264,  264,
  191,  266,  268,  258,   75,   76,  272,  272,   79,   61,
  261,   59,  259,  259,  259,   97,   98,  261,  257,   41,
   41,   41,   43,  261,   45,  276,  277,  278,  279,  280,
  281,   59,  276,  277,  278,  279,  280,  281,  276,  277,
  278,  279,  280,  281,  115,  261,  117,  270,  271,  262,
  263,  261,   94,   95,  257,  258,   41,  261,   59,   41,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,  258,  261,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,  262,  263,  276,  277,  278,  279,  280,  281,
  276,  277,  278,  279,  280,  281,  257,  258,  257,  262,
  263,  257,  258,  257,  276,  277,  278,  279,  280,  281,
   59,  270,  271,   41,  275,  274,  270,  271,  257,  258,
  257,  258,  261,   59,  257,  258,  257,  258,  257,  258,
  257,  258,  257,  263,   41,  260,   43,   59,   45,  264,
  262,  263,   44,   44,  269,  268,   59,  272,   59,  230,
   59,  232,  257,   59,   59,  259,  259,  268,  259,   59,
   59,   59,   59,  263,  263,  263,   59,   59,   59,   59,
   59,   59,  263,   59,  258,   59,   59,   41,  267,   59,
  268,   59,   59,   59,   59,  268,   17,   16,   44,  149,
  197,  232,   66,
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
"declaracionFuncion : cabeza_funcion bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
"declaracionFuncion : cabeza_funcion bloque_sentencias_declarativas BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
"cabeza_funcion : tipo FUNC nombre_ambito parametro",
"pre_condicion : PRE ':' '(' condicion ')' ',' CADENA ';'",
"pre_condicion : PRE ':' condicion ')' ',' CADENA ';'",
"pre_condicion : PRE ':' '(' ')' ',' CADENA ';'",
"pre_condicion : PRE ':' '(' condicion ',' CADENA ';'",
"pre_condicion : PRE ':' '(' condicion ')' CADENA ';'",
"pre_condicion : PRE ':' '(' condicion ')' ',' ';'",
"parametro : '(' tipo nombre_parametro ')'",
"parametro : tipo ID ')'",
"parametro : '(' ID ')'",
"parametro : '(' tipo ')'",
"parametro : '(' ')'",
"parametro : '(' tipo ID",
"nombre_parametro : ID",
"retorno : '(' expresion ')'",
"retorno : expresion ')'",
"retorno : '(' ')'",
"retorno : '(' expresion",
"bloque_sentencias_ejecutables : sentencia_ejecutable",
"bloque_sentencias_ejecutables : BEGIN conjunto_sentencia_ejecutable END ';'",
"conjunto_sentencia_ejecutable : sentencia_ejecutable",
"conjunto_sentencia_ejecutable : conjunto_sentencia_ejecutable sentencia_ejecutable",
"conjunto_sentencia_ejecutable : error ';'",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : mensaje_pantalla",
"sentencia_ejecutable : clausula_seleccion_if",
"sentencia_ejecutable : sentencia_control_repeat",
"invocacion_funcion : nombre_invocacion '(' factor ')'",
"nombre_invocacion : ID",
"asignacion : operador_asignacion \":=\" expresioncompuesta ';'",
"asignacion : operador_asignacion expresioncompuesta ';'",
"asignacion : operador_asignacion \":=\" ';'",
"asignacion : operador_asignacion \":=\" '-' ';'",
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

//#line 194 "gramatica.y"
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	public List<String> pendingTypeList;
	public String lastFuncType;
	
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
	}
	
	public void yyerror(String error)
	{
		System.out.println("error : "+error);
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
		String variable_ambito = variable.sval;
		boolean variable_declarada = false;
		if (isFuncion(variable.sval))
		{
			chequeoS_funcion_no_declarada(variable);
			return;
		}
		while (variable_ambito.lastIndexOf('.') != -1 && !variable_declarada)
		{	
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null) 
	            {
	            	if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("variable") == 0)
	                	variable_declarada = true;
	            }
	        }
	        
	        int ambito_index = variable_ambito.lastIndexOf('.');
			if (ambito_index > 0) {
				variable_ambito = variable_ambito.substring(0,ambito_index);
			}
		
		}
		
		if (!variable_declarada) 
		{
			System.out.println("error semantico: variable " + variable.sval + " no declarada.");
		}
	}
	
	private void chequeoS_redeclaracion_variable(ParserVal variable)
	{
		// buscar en la tabla de simbolos si esta (no hace falta por campo uso sino que no pertenezcan al mismo ambito y listo)
		// pero si hay que verificar que 'uso' no sea null porque sino, no esta declarada.
		boolean variable_redeclarada = false;

			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(variable.sval) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null)  
	            {
	            	if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("variable") == 0)
	                	variable_redeclarada = true;
	            }
	        }
		
		if (variable_redeclarada)
		{
			System.out.println("error semantico: variable " + variable.sval + " ya declarada");
		}
	}
	
	private void reverificar_cte_negativa (ParserVal variable)
	{
		String new_variable = "-" + variable.sval;
		String tipo = analizadorLexico.tabla_simbolos.get(variable.sval).get("tipo");
		boolean fuera_de_rango = false;
		long aux= Long.parseLong(new_variable);
		if (tipo.compareTo("INT") == 0)
		{
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
		//rechequear rango
		
		
		if (!fuera_de_rango)
		{
			analizadorLexico.tabla_simbolos.put(new_variable, new HashMap<String,String>());
			analizadorLexico.tabla_simbolos.get(new_variable).put("tipo", tipo);
			analizadorLexico.tabla_simbolos.get(new_variable).put("uso", "constante");
		}
		if (fuera_de_rango)
		{
			//descartarlo y error
		}
		variable.sval = new_variable;
	}
	
	private void chequeoS_funcion_no_declarada (ParserVal funcion)
	{
		
		boolean isTypeDef = isTypeDef(getTipoVariable(funcion.sval));
		if (isTypeDef)
		{
			funcion.sval = getTipoVariable(funcion.sval);
			chequeoS_typedef_existe(funcion);
			return;
		}
		
		
		String funcion_ambito = funcion.sval;
		boolean funcion_declarada = false;
		while (funcion_ambito.lastIndexOf('.') != -1 && !funcion_declarada)
		{	
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(funcion_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null) 
	            {
	            	if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("funcion") == 0)
	                	funcion_declarada = true;
	            }
	        }
	        
	        int ambito_index = funcion_ambito.lastIndexOf('.');
			if (ambito_index > 0) 
			{
				funcion_ambito = funcion_ambito.substring(0,ambito_index);
			}
		}
		
		if (!funcion_declarada)
		{
		System.out.println("error semantico: funcion " + funcion.sval + " no declarada");
		}
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
			System.out.println("error semantico: funcion " + funcion.sval + " ya declarada");
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
	
	private String getTipoVariable(String id)
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

	
	private boolean isFuncion(String id)
	{
		if (analizadorLexico.tabla_simbolos.get(id).get("uso") != null)
		{
			if (analizadorLexico.tabla_simbolos.get(id).get("uso").compareTo("funcion") == 0)
				return true;
		}
		return false;
		
	}
	private void chequeoS_diferentes_tipos(ParserVal id1, ParserVal id2)
	{
		String tipo_1 = getTipoVariable(id1.sval);
		String tipo_2 = getTipoVariable(id2.sval);
		
		
		if (getTipoVariable(tipo_1) != "nulo") //es typedef
		{
			tipo_1 = getTipoVariable(tipo_1);
			if (!isFuncion(id2.sval))
				System.out.println("error semantico: " + id2.sval +"("+tipo_2+ ") debe ser una funcion de retorno " + tipo_1); 
		}
		if (getTipoVariable(tipo_2) != "nulo") //es typedef
		{
			tipo_2 = getTipoVariable(tipo_2);
		}
		
		if (tipo_1.compareTo(tipo_2) == 0 && tipo_1 != "nulo")
		{
			return;
		}

		System.out.println("error semantico: " + id1.sval +"("+tipo_1+ ") y " + id2.sval +"("+tipo_2+") son de tipos incompatibles para la operacion."); 
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
	
	private String getTipoParametroFuncion(String funcion)
	{
			String variable_ambito = funcion;
			while (variable_ambito.lastIndexOf('.') != -1)
			{	
				Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
		        while (iterator.hasNext()) 
		        {
		            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
		            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("parametro") != null)
		            {
		                return analizadorLexico.tabla_simbolos.get(entry.getKey()).get("parametro");
		            }
		        }
		        int ambito_index = variable_ambito.lastIndexOf('.');
				if (ambito_index > 0) {
					variable_ambito = variable_ambito.substring(0,ambito_index);
				}
			
			}
		
		return "nulo";
	}
	
	private boolean isTypeDef(String func)
	{
		try {
			if (analizadorLexico.tabla_simbolos.get(func).get("tipo") != null)
				return true;
		} catch (Exception e) {}
		return false;
		
	}
	
	private void chequeoS_parametro_funcion(ParserVal funcion, ParserVal parametro)
	{
		String tipo_parametro_funcion = getTipoParametroFuncion(funcion.sval);
		String tipo_parametro = getTipoVariable(parametro.sval);
		
		if (isTypeDef(getTipoVariable(funcion.sval)))
		{
			tipo_parametro_funcion = getTipoParametroFuncion(getTipoVariable(funcion.sval));
		}
		
		if (tipo_parametro_funcion.compareTo(tipo_parametro) == 0 && tipo_parametro_funcion != "nulo")
		{
			return;
		}
		System.out.println("error semantico: el parametro de " + funcion.sval + " debe ser de tipo " + tipo_parametro_funcion +" y no de tipo " + tipo_parametro); 
	}
	
	private void chequeoS_typedef_existe(ParserVal typedef)
	{
		String variable_ambito = typedef.sval;
		while (variable_ambito.lastIndexOf('.') != -1)
		{	
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null)
	            {//Se compara uso ya que la variable debe estar declarada para que tenga tipo
	            		try {
	            		if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("typedef") == 0)
	                		return;
	            		}
	            		catch(Exception e) {}
	            }
	        }
	        int ambito_index = variable_ambito.lastIndexOf('.');
			if (ambito_index > 0) {
				variable_ambito = variable_ambito.substring(0,ambito_index);
			}
		}
		System.out.println("error semantico: no existe un typedef de nombre " + typedef.sval); 
	}
//#line 858 "Parser.java"
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
{desapilar_ambito(); this.reglas.add("Sentencia START programa");}
break;
case 2:
//#line 16 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
break;
case 3:
//#line 19 "gramatica.y"
{apilar_ambito(val_peek(0)); set_campo(val_peek(0),"uso","programa");}
break;
case 4:
//#line 22 "gramatica.y"
{chequeoS_redeclaracion_funcion(val_peek(0)); apilar_ambito(val_peek(0)); set_campo(val_peek(0),"uso","funcion"); setLastFuncType(val_peek(0));}
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
{desapilar_ambito(); this.reglas.add("DECLARACION FUNCION");}
break;
case 20:
//#line 57 "gramatica.y"
{desapilar_ambito(); this.reglas.add("DECLARACION FUNCION Y PRE CONDICION");}
break;
case 21:
//#line 60 "gramatica.y"
{setFuncType("tipo",val_peek(3).sval); copiarTipoParametro(val_peek(0),val_peek(1));}
break;
case 22:
//#line 63 "gramatica.y"
{this.reglas.add("pre-condicion");}
break;
case 23:
//#line 64 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("pre-condicion");}
break;
case 24:
//#line 65 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); this.reglas.add("pre-condicion");}
break;
case 25:
//#line 66 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("pre-condicion");}
break;
case 26:
//#line 67 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); this.reglas.add("pre-condicion");}
break;
case 27:
//#line 68 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); this.reglas.add("pre-condicion");}
break;
case 28:
//#line 71 "gramatica.y"
{setPendingTypes(val_peek(2).sval);}
break;
case 29:
//#line 72 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 30:
//#line 73 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 31:
//#line 74 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 32:
//#line 75 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 33:
//#line 76 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 34:
//#line 79 "gramatica.y"
{set_campo(val_peek(0),"uso", "parametro"); addPendingTypeList(val_peek(0).sval);}
break;
case 36:
//#line 83 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 37:
//#line 84 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
break;
case 38:
//#line 85 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 40:
//#line 89 "gramatica.y"
{this.reglas.add("bloque de sentencias BEGIN-END");}
break;
case 43:
//#line 94 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error de sentencias ejecutables");}
break;
case 48:
//#line 104 "gramatica.y"
{chequeoS_parametro_funcion(val_peek(3), val_peek(2));}
break;
case 49:
//#line 108 "gramatica.y"
{chequeoS_funcion_no_declarada(val_peek(0));}
break;
case 50:
//#line 111 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(1)); this.reglas.add("Asignacion");}
break;
case 51:
//#line 112 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion");}
break;
case 52:
//#line 113 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": variable o constante faltante"); this.reglas.add("Asignacion");}
break;
case 53:
//#line 114 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": variable o constante faltante"); this.reglas.add("Asignacion");}
break;
case 54:
//#line 117 "gramatica.y"
{chequeoS_variable_no_declarada(val_peek(0));}
break;
case 57:
//#line 122 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 58:
//#line 123 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 59:
//#line 126 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 60:
//#line 127 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 62:
//#line 131 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 63:
//#line 132 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 65:
//#line 134 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 66:
//#line 135 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 68:
//#line 139 "gramatica.y"
{this.reglas.add("factor ID"); chequeoS_variable_no_declarada(val_peek(0));}
break;
case 69:
//#line 140 "gramatica.y"
{this.reglas.add("Factor CTE");}
break;
case 70:
//#line 141 "gramatica.y"
{reverificar_cte_negativa(val_peek(1));}
break;
case 71:
//#line 144 "gramatica.y"
{this.reglas.add("tipo INT");}
break;
case 72:
//#line 145 "gramatica.y"
{this.reglas.add("tipo SINGLE");}
break;
case 73:
//#line 148 "gramatica.y"
{this.reglas.add("clausula IF");}
break;
case 74:
//#line 149 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF");}
break;
case 75:
//#line 150 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("clausula IF");}
break;
case 76:
//#line 151 "gramatica.y"
{this.reglas.add("clausula IF-ELSE");}
break;
case 77:
//#line 152 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF-ELSE");}
break;
case 78:
//#line 153 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");this.reglas.add("clausula IF-ELSE");}
break;
case 79:
//#line 154 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado");  this.reglas.add("clausula IF");}
break;
case 80:
//#line 155 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); this.reglas.add("clausula IF-ELSE");}
break;
case 81:
//#line 156 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante");  this.reglas.add("clausula IF");}
break;
case 82:
//#line 157 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante"); this.reglas.add("clausula IF-ELSE");}
break;
case 84:
//#line 163 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 93:
//#line 176 "gramatica.y"
{this.reglas.add("clausula PRINT");}
break;
case 94:
//#line 177 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); this.reglas.add("clausula PRINT");}
break;
case 95:
//#line 178 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); this.reglas.add("clausula PRINT");}
break;
case 96:
//#line 181 "gramatica.y"
{this.reglas.add("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 100:
//#line 189 "gramatica.y"
{System.out.println("Comparacion: " + val_peek(3).sval + " " + val_peek(2).sval); this.reglas.add("Condicion_Repeat");}
break;
case 101:
//#line 190 "gramatica.y"
{this.reglas.add("Condicion_Repeat");}
break;
//#line 1279 "Parser.java"
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
