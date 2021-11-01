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
   24,   24,   31,   32,   32,   32,   32,   21,   21,   21,
   33,   33,   33,   33,   33,   33,   30,   30,   30,    9,
    9,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   19,   19,   34,   34,   34,   34,   34,   34,   34,
   34,   25,   25,   25,   27,   36,   36,   36,   35,   35,
};
final static short yylen[] = {                            2,
    6,    2,    1,    1,    1,    2,    1,    1,    1,    3,
    3,    1,    3,    1,    1,    5,    1,    5,    9,   10,
    4,    8,    7,    7,    7,    7,    7,    4,    3,    3,
    3,    2,    3,    1,    3,    2,    2,    2,    1,    4,
    1,    2,    2,    1,    1,    1,    1,    4,    1,    4,
    3,    3,    1,    3,    1,    2,    2,    3,    3,    1,
    3,    3,    1,    3,    3,    1,    1,    1,    2,    1,
    1,    8,    7,    7,   10,    9,    9,    7,    9,    5,
    7,    1,    3,    1,    1,    1,    1,    1,    1,    1,
    1,    5,    4,    4,   14,    2,    1,    2,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    3,    0,    0,    2,   12,   70,   71,    0,    0,
    5,    7,    8,    9,    0,    0,    0,   17,    0,    0,
    6,   15,    0,    0,   14,    0,    0,    0,    0,   53,
    0,    0,    0,    0,   41,   44,   45,   46,   47,    0,
    4,    0,   10,    0,   11,    0,    0,    0,   43,    0,
   68,    0,    0,    0,    0,    0,   66,    0,   63,    0,
    0,    0,    0,    0,   42,    0,    0,    0,    0,    0,
    0,   21,   13,    0,    0,    0,    0,   16,    0,    0,
   39,    0,   69,   84,   85,   86,   87,   88,   89,   90,
   91,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,   52,    0,    0,   56,   51,    0,   32,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   67,    0,   64,   61,   65,   62,
   93,   94,    0,    0,   50,   54,   30,    0,   31,    0,
   29,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   80,    0,    0,    0,    0,   48,   92,    0,   28,    0,
    0,    0,   37,    0,    0,   36,    0,   18,   40,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   35,    0,    0,   81,    0,   74,    0,    0,
    0,   78,    0,   73,    0,    0,    0,    0,    0,    0,
    0,   19,    0,    0,    0,   72,    0,    0,    0,    0,
   24,   25,   26,    0,   27,   23,   20,   77,    0,   79,
   76,   99,  100,    0,   22,   75,    0,    0,    0,    0,
    0,   96,   98,    0,   95,
};
final static short yydgoto[] = {                          3,
    4,   10,   34,   42,   11,   12,   13,   14,   15,   24,
   16,   25,   19,   48,   17,  145,   76,   72,   55,  140,
   56,   80,   81,   36,   37,   38,   39,   57,   58,   59,
   40,   69,   60,   93,  196,  231,
};
final static short yysindex[] = {                      -192,
  -38,    0,    0, -186,    0,    0,    0,    0, -233, -112,
    0,    0,    0,    0, -191, -226, -186,    0,  -20,  -78,
    0,    0, -223,   -7,    0,   43,  -94, -124,   -9,    0,
   82,  -34,   28,  -67,    0,    0,    0,    0,    0,   60,
    0,  -12,    0, -226,    0, -116, -173,   39,    0,    0,
    0,  -58,   92, -164,   21,   75,    0,   64,    0,   54,
   66, -145, -131,   79,    0,   84,   92,   80,   83,   67,
 -103,    0,    0,  102,  -53,  -78,  129,    0,  -78, -111,
    0,   29,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -96,   92,   92,   92,   94,   92,   92,  112,  -16,
  113,    0,    0,  122,   91,    0,    0,  148,    0,  -28,
  155,   88,   90,   96, -124,  106,  -58,  159,  -58,  -69,
  -58,   75,   54,   54,    0,  194,    0,    0,    0,    0,
    0,    0,  183,    9,    0,    0,    0,    0,    0,  228,
    0,    8,   35,   65,  211,  123,   90,  248,  244,  -63,
    0,  -79,  -58,  -36,  -32,    0,    0,  245,    0,  276,
   15,  277,    0,  167,   63,    0,  275,    0,    0,  285,
  -58,  295,   -2,  -58,  296,  -58,  298,  101,  100,  105,
  -35,  108,    0,  302,   97,    0,  110,    0,  -58,  317,
  114,    0,  117,    0,   49,  322,  323,  324,  326,  -44,
  327,    0,  329,  330,  127,    0,  332,  333,  -25,  135,
    0,    0,    0,  335,    0,    0,    0,    0,  336,    0,
    0,    0,    0,  355,    0,    0,  130,  115,  339,  115,
  131,    0,    0,  341,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -40,
    0,    0,    0,    0,    0,    1,    0,    0,    0,  -33,
    0,    0,    0,    0,    0,    0,    0,  342,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  343,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    7,  -27,   -5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   62,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  344,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  136,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  388,   14,    0,   20,    0,    0,    0,   16,  390,
    0,  363,    0,    0,    0,  261,    0,    0,  -30,    0,
    6,   -4,  141,    0,    0,    0,    0,  125,    0,  -18,
    0,  345,  169,  214,    0,  180,
};
final static int YYTABLESIZE=411;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   67,   67,   67,   67,   67,   62,   67,   60,  200,   60,
   60,   60,  139,   58,  215,   58,   58,   58,   67,   67,
    5,   67,   82,   18,  133,   60,   60,   70,   60,   21,
   22,   58,   58,   41,   58,   59,   44,   59,   59,   59,
   28,   82,  132,   47,   82,   68,   21,   83,  160,   49,
   83,   43,   54,   59,   59,  181,   59,   71,  180,   75,
   82,   92,   82,    1,    2,   22,   83,   63,   83,  120,
    6,   68,  105,   23,   91,  162,   90,  126,  128,  130,
   91,  143,   90,    7,    8,  110,   44,    9,   91,  114,
   90,   77,  116,   83,   91,   98,   90,   78,  122,   67,
   97,   45,   34,   96,   54,  163,   99,  109,   91,   54,
   90,  161,  150,  100,  152,  154,  155,   94,  146,   95,
  106,   53,   94,   67,   95,  101,   54,  142,   54,  144,
  148,  136,   54,   94,   54,   95,   54,  102,   54,   29,
   30,  107,  103,   31,    6,    7,    8,   32,  173,  164,
  117,  118,  146,  111,   20,   33,   74,    7,    8,  112,
   35,    9,    6,  166,  121,   94,  187,   95,  115,  191,
  131,  193,   46,  134,   65,    7,    8,   29,   30,    9,
  135,   31,  171,  172,  205,   32,   35,   30,  137,   30,
   31,  153,   31,   33,   32,  141,   32,   79,   30,  170,
   64,   31,   33,   30,   33,   32,   31,  183,   79,   94,
   32,   95,  113,   33,  214,   65,   35,  151,   33,   35,
   67,  127,  129,  199,   61,  174,  175,   60,  138,  176,
  177,  222,  223,   58,  156,   67,   67,   67,   67,   67,
   67,  157,   60,   60,   60,   60,   60,   60,   58,   58,
   58,   58,   58,   58,   65,   59,   65,    7,    8,  189,
  190,   82,  123,  124,   50,   51,  158,   83,  159,  165,
   59,   59,   59,   59,   59,   59,   82,   82,   82,   82,
   82,   82,   83,   83,   83,   83,   83,   83,  168,  119,
   84,   85,   86,   87,   88,   89,   84,   85,   86,   87,
   88,   89,  169,  178,   84,   85,   86,   87,   88,   89,
   84,   85,   86,   87,   88,   89,   50,   51,   33,  179,
  182,   50,   51,  108,   84,   85,   86,   87,   88,   89,
  184,   33,   33,  185,   66,   33,    7,    8,   50,   51,
   50,   51,   52,  186,   50,   51,   50,   51,   50,   51,
  125,   51,   30,  188,  192,   31,  194,  195,  197,   32,
  202,  147,   30,  198,  203,   31,  201,   33,  230,   32,
  230,   30,  204,  149,   31,  206,  207,   33,   32,  208,
  210,  211,  212,  229,  213,  216,   33,  217,  218,  219,
  220,  221,  224,  225,  226,  227,  228,  232,  234,  235,
   55,   57,   38,   97,   27,   26,   73,  167,  209,  233,
  104,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   42,   43,   44,   45,   40,   47,   41,   44,   43,
   44,   45,   41,   41,   59,   43,   44,   45,   59,   60,
   59,   62,   53,  257,   41,   59,   60,   40,   62,   10,
  257,   59,   60,  257,   62,   41,   44,   43,   44,   45,
   61,   41,   59,   28,   44,   40,   27,   41,   41,   59,
   44,   59,   45,   59,   60,   41,   62,   42,   44,   46,
   60,   41,   62,  256,  257,  257,   60,   40,   62,   41,
  257,   66,   67,  265,   60,   41,   62,   96,   97,   98,
   60,  112,   62,  270,  271,   70,   44,  274,   60,   76,
   62,  265,   79,  258,   60,   42,   62,   59,   93,   40,
   47,   59,   41,   40,   45,   41,   41,   41,   60,   45,
   62,  142,  117,  259,  119,  120,  121,   43,  113,   45,
   41,   40,   43,   40,   45,  257,   45,   40,   45,   40,
  115,   41,   45,   43,   45,   45,   45,   59,   45,  256,
  257,   59,   59,  260,  257,  270,  271,  264,  153,  144,
  262,  263,  147,  257,  267,  272,  273,  270,  271,   58,
   20,  274,  257,   41,  261,   43,  171,   45,   40,  174,
   59,  176,  267,   61,   34,  270,  271,  256,  257,  274,
   59,  260,  262,  263,  189,  264,   46,  257,   41,  257,
  260,  261,  260,  272,  264,   41,  264,  267,  257,  263,
  268,  260,  272,  257,  272,  264,  260,   41,  267,   43,
  264,   45,  266,  272,  259,   75,   76,   59,  272,   79,
  261,   97,   98,  259,  259,  262,  263,  261,  257,  262,
  263,  257,  258,  261,   41,  276,  277,  278,  279,  280,
  281,   59,  276,  277,  278,  279,  280,  281,  276,  277,
  278,  279,  280,  281,  114,  261,  116,  270,  271,  262,
  263,  261,   94,   95,  257,  258,  258,  261,   41,   59,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,   41,  261,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,   59,   59,  276,  277,  278,  279,  280,  281,
  276,  277,  278,  279,  280,  281,  257,  258,  257,   44,
   44,  257,  258,  257,  276,  277,  278,  279,  280,  281,
  268,  270,  271,   59,  275,  274,  270,  271,  257,  258,
  257,  258,  261,   59,  257,  258,  257,  258,  257,  258,
  257,  258,  257,   59,   59,  260,   59,  257,  259,  264,
   59,  266,  257,  259,  268,  260,  259,  272,  228,  264,
  230,  257,  263,  268,  260,   59,  263,  272,  264,  263,
   59,   59,   59,  269,   59,   59,  272,   59,   59,  263,
   59,   59,  258,   59,   59,   41,  267,   59,  268,   59,
   59,   59,   59,  268,   17,   16,   44,  147,  195,  230,
   66,
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
		while (variable_ambito.lastIndexOf('.') != -1 && !variable_declarada)
		{	
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null) 
	            {
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
		//analizadorLexico.tabla_simbolos.remove(variable.sval); CREO QUE NO HAY QUE HACERLO.
		boolean cumple_rango = false;
		//rechequear rango
		if (cumple_rango)
		{
			analizadorLexico.tabla_simbolos.put(new_variable, new HashMap<String,String>());
			analizadorLexico.tabla_simbolos.get(new_variable).put("tipo", tipo);
			analizadorLexico.tabla_simbolos.get(new_variable).put("uso", "constante");
		}
		if (!cumple_rango)
		{
			//descartarlo y error
		}
		variable.sval = new_variable;
	}
	
	private void chequeoS_funcion_no_declarada (ParserVal funcion)
	{
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
	            	//agregar if uso == funcion?
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
	
	private boolean isTypedef(String id) // mal
	{
		if (analizadorLexico.tabla_simbolos.get(id).get("uso").compareTo("typedef") == 0)
		{
			return true;
		}
		return false;
	}
	
	
	private void chequeoS_diferentes_tipos(ParserVal id1, ParserVal id2)
	{
		String tipo_1 = getTipoVariable(id1.sval);
		String tipo_2 = getTipoVariable(id2.sval);
		
		
		if (getTipoVariable(tipo_1) != "nulo") //es typedef
			tipo_1 = getTipoVariable(tipo_1);
		if (getTipoVariable(tipo_2) != "nulo") //es typedef
			tipo_2 = getTipoVariable(tipo_2);
			
			
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
		analizadorLexico.tabla_simbolos.get(pegar.sval).put("tipo_parametro",tipo);
	}
	
	private String getTipoParametroFuncion(ParserVal funcion)
	{
			String variable_ambito = funcion.sval;
			while (variable_ambito.lastIndexOf('.') != -1)
			{	
				Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
		        while (iterator.hasNext()) 
		        {
		            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
		            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("tipo_parametro") != null)
		            {
		                return analizadorLexico.tabla_simbolos.get(entry.getKey()).get("tipo_parametro");
		            }
		        }
		        int ambito_index = variable_ambito.lastIndexOf('.');
				if (ambito_index > 0) {
					variable_ambito = variable_ambito.substring(0,ambito_index);
				}
			
			}
		
		return "nulo";
	}
	
	private void chequeoS_parametro_funcion(ParserVal funcion, ParserVal parametro)
	{
		String tipo_parametro_funcion = getTipoParametroFuncion(funcion);
		String tipo_parametro = getTipoVariable(parametro.sval);
		
		if (tipo_parametro_funcion.compareTo(tipo_parametro) == 0 && tipo_parametro_funcion != "nulo")
		{
			return;
		}
		System.out.println("error semantico: el parametro de " + funcion.sval + " debe ser de tipo " + tipo_parametro_funcion +" y no de tipo " + tipo_parametro); 
	}
//#line 748 "Parser.java"
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
{System.out.println("Chequear que " + val_peek(0).sval + " sea typedef");}
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
//#line 107 "gramatica.y"
{chequeoS_funcion_no_declarada(val_peek(0));}
break;
case 50:
//#line 110 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(1)); this.reglas.add("Asignacion");}
break;
case 51:
//#line 111 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion");}
break;
case 52:
//#line 112 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": variable o constante faltante"); this.reglas.add("Asignacion");}
break;
case 53:
//#line 115 "gramatica.y"
{chequeoS_variable_no_declarada(val_peek(0));}
break;
case 56:
//#line 120 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 57:
//#line 121 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 58:
//#line 124 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 59:
//#line 125 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 61:
//#line 129 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 62:
//#line 130 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 64:
//#line 132 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 65:
//#line 133 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 67:
//#line 137 "gramatica.y"
{this.reglas.add("factor ID"); chequeoS_variable_no_declarada(val_peek(0));}
break;
case 68:
//#line 138 "gramatica.y"
{this.reglas.add("Factor CTE");}
break;
case 69:
//#line 139 "gramatica.y"
{reverificar_cte_negativa(val_peek(1));}
break;
case 70:
//#line 144 "gramatica.y"
{this.reglas.add("tipo INT");}
break;
case 71:
//#line 145 "gramatica.y"
{this.reglas.add("tipo SINGLE");}
break;
case 72:
//#line 148 "gramatica.y"
{this.reglas.add("clausula IF");}
break;
case 73:
//#line 149 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF");}
break;
case 74:
//#line 150 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("clausula IF");}
break;
case 75:
//#line 151 "gramatica.y"
{this.reglas.add("clausula IF-ELSE");}
break;
case 76:
//#line 152 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF-ELSE");}
break;
case 77:
//#line 153 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");this.reglas.add("clausula IF-ELSE");}
break;
case 78:
//#line 154 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado");  this.reglas.add("clausula IF");}
break;
case 79:
//#line 155 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); this.reglas.add("clausula IF-ELSE");}
break;
case 80:
//#line 156 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante");  this.reglas.add("clausula IF");}
break;
case 81:
//#line 157 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante"); this.reglas.add("clausula IF-ELSE");}
break;
case 83:
//#line 163 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2));}
break;
case 92:
//#line 176 "gramatica.y"
{this.reglas.add("clausula PRINT");}
break;
case 93:
//#line 177 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); this.reglas.add("clausula PRINT");}
break;
case 94:
//#line 178 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); this.reglas.add("clausula PRINT");}
break;
case 95:
//#line 181 "gramatica.y"
{this.reglas.add("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 99:
//#line 189 "gramatica.y"
{System.out.println("Comparacion: " + val_peek(3).sval + " " + val_peek(2).sval); this.reglas.add("Condicion_Repeat");}
break;
case 100:
//#line 190 "gramatica.y"
{this.reglas.add("Condicion_Repeat");}
break;
//#line 1165 "Parser.java"
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
