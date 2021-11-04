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
   37,   39,   38,
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
    1,    1,    5,    4,    4,    8,    2,    1,    2,    5,
    3,    1,    3,
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
  102,    0,    0,    0,    1,   52,    0,    0,    0,   57,
   51,    0,   32,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   68,    0,
   65,   62,   66,   63,   94,   95,    0,    0,    0,    0,
   53,   50,   55,   30,    0,   31,    0,   29,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   81,    0,    0,
    0,    0,   48,   93,    0,    0,    0,  101,   28,    0,
    0,    0,   37,    0,    0,   36,    0,   18,   40,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   35,    0,    0,   82,
    0,   75,    0,    0,    0,   79,    0,   74,   97,   99,
    0,    0,  100,    0,    0,    0,    0,    0,   19,    0,
    0,    0,   73,    0,    0,   96,   24,   25,   26,    0,
   27,   23,   20,   78,    0,   80,   77,   22,   76,
};
final static short yydgoto[] = {                          3,
    4,   10,   34,   42,   11,   12,   13,   14,   15,   24,
   16,   25,   19,   48,   17,  152,   76,   72,   55,  147,
   56,   80,   81,   36,   37,   38,   39,   57,   58,   59,
   40,   69,   60,   93,  102,  190,  103,  167,  104,
};
final static short yysindex[] = {                      -191,
  -25,    0,    0, -227,    0,    0,    0,    0, -232,  -93,
    0,    0,    0,    0, -197, -220, -227,    0,   12,  -67,
    0,    0, -177,  -13,    0,   34,  -88, -155,   27,    0,
   82,  -34,   50,   96,    0,    0,    0,    0,    0,   60,
    0,  -12,    0, -220,    0,  -97, -163,   45,    0,    0,
    0, -110,   92, -151,   21,   56,    0,   72,    0,   32,
   77, -133, -126,   89,    0,   84,   92,   53,  102,   67,
 -119,    0,    0,  110,  -53,  -67,  115,    0,  -67,  -92,
    0,   29,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -81,   92,   92,   92,   94,   92,   92,  133,  -18,
    0,  132,  143,  126,    0,    0,  -38,  149,   80,    0,
    0,  169,    0,  -28,  185,   88,   90,   97, -155,   98,
 -110,  173, -110,  -66, -110,   56,   32,   32,    0,  194,
    0,    0,    0,    0,    0,    0,  183,  -10,   10,   -3,
    0,    0,    0,    0,    0,    0,  222,    0,    8,   35,
   65,  205,   91,   90,  228,  230,   57,    0,  -78, -110,
  -63,   -2,    0,    0,  114,   49,  262,    0,    0,  287,
   15,  290,    0,   99,   76,    0,  300,    0,    0,  306,
 -110,  308,   41, -110,  313, -110,  314,  316,  114,  108,
   92,  119,  121,  122,  -35,  123,    0,  320,  116,    0,
  124,    0, -110,  326,  125,    0,  127,    0,    0,    0,
  330,   56,    0,  332,  333,  334,  -44,  335,    0,  336,
  337,  134,    0,  339,  340,    0,    0,    0,    0,  341,
    0,    0,    0,    0,  342,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -40,
    0,    0,    0,    0,    0,    1,    0,    0,    0,  -33,
    0,    0,    0,    0,    0,    0,    0,  343,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  344,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    7,  -27,   -5,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  345,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  137,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  347,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  390,    6,    0,   61,    0,    0,    0,   22,  392,
    0,  365,    0,    0,    0,  256,    0,    0,  -29,    0,
   79,   28,   38,    0,    0,    0,    0,   68,    0,  120,
    0,  346,  128,  245,    0,  224,    0,    0,    0,
};
final static int YYTABLESIZE=413;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   68,   68,   68,   68,   68,   62,   68,   61,  217,   61,
   61,   61,  146,   60,  231,   60,   60,   60,   68,   68,
  141,   68,  137,   82,   18,   61,   61,   70,   61,    6,
   44,   60,   60,    5,   60,   59,   22,   59,   59,   59,
  136,   83,    7,    8,   83,   43,    9,   84,  170,   47,
   84,   75,   54,   59,   59,  195,   59,   35,  194,   22,
   83,   92,   83,   71,    1,    2,   84,   23,   84,  124,
   21,   65,   28,   98,   91,  172,   90,   44,   97,   41,
   91,  118,   90,   35,  120,   49,  150,   21,   91,   63,
   90,  114,   45,  110,   91,   95,   90,   94,   95,   67,
   94,   77,   34,   78,   54,  173,   83,  113,   91,   54,
   90,   96,   65,   35,    7,    8,   35,   99,   68,  171,
  143,   53,   95,   67,   94,  100,   54,  149,  107,  151,
  101,  176,   54,   95,   54,   94,   54,  115,   54,  197,
  155,   95,  106,   94,   68,  109,   30,  105,  157,   31,
  159,  161,  162,   32,  119,   65,   79,   65,   29,   30,
  111,   33,   31,    6,  131,  133,   32,  116,    6,  121,
  122,  126,  138,   20,   33,   74,    7,    8,   46,  125,
    9,    7,    8,  181,  182,    9,  140,  183,   29,   30,
   30,  135,   31,   31,  160,  153,   32,   32,  184,  185,
   79,  139,  189,   30,   33,   33,   31,  142,  201,  144,
   32,  205,  117,  207,  230,  130,  132,  134,   33,   83,
   68,  127,  128,  216,   61,  148,  189,   61,  145,  174,
  222,  158,  153,   60,  163,   68,   68,   68,   68,   68,
   68,  164,   61,   61,   61,   61,   61,   61,   60,   60,
   60,   60,   60,   60,  168,   59,  165,    7,    8,  186,
  187,   83,  169,  175,   50,   51,  166,   84,  178,  212,
   59,   59,   59,   59,   59,   59,   83,   83,   83,   83,
   83,   83,   84,   84,   84,   84,   84,   84,  179,  123,
   84,   85,   86,   87,   88,   89,   84,   85,   86,   87,
   88,   89,  203,  204,   84,   85,   86,   87,   88,   89,
   84,   85,   86,   87,   88,   89,   50,   51,   33,  180,
  192,   50,   51,  112,   84,   85,   86,   87,   88,   89,
  193,   33,   33,  196,   66,   33,    7,    8,   50,   51,
   50,   51,   52,  198,   50,   51,   50,   51,   50,   51,
  129,   51,   30,   30,   30,   31,   31,   31,  199,   32,
   32,   32,  154,   64,  200,  156,  202,   33,   33,   33,
   30,  206,  208,   31,  209,  211,  213,   32,  219,  214,
  215,  218,  188,  220,  223,   33,  221,  224,  226,  225,
  227,  228,  229,  232,  233,  234,  235,  236,  237,  238,
  239,   56,   58,   38,   98,  103,   27,   26,   73,  177,
  191,  108,  210,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   42,   43,   44,   45,   40,   47,   41,   44,   43,
   44,   45,   41,   41,   59,   43,   44,   45,   59,   60,
   59,   62,   41,   53,  257,   59,   60,   40,   62,  257,
   44,   59,   60,   59,   62,   41,  257,   43,   44,   45,
   59,   41,  270,  271,   44,   59,  274,   41,   41,   28,
   44,   46,   45,   59,   60,   41,   62,   20,   44,  257,
   60,   41,   62,   42,  256,  257,   60,  265,   62,   41,
   10,   34,   61,   42,   60,   41,   62,   44,   47,  257,
   60,   76,   62,   46,   79,   59,  116,   27,   60,   40,
   62,   70,   59,   41,   60,   43,   62,   45,   43,   40,
   45,  265,   41,   59,   45,   41,  258,   41,   60,   45,
   62,   40,   75,   76,  270,  271,   79,   41,   40,  149,
   41,   40,   43,   40,   45,  259,   45,   40,   45,   40,
  257,   41,   45,   43,   45,   45,   45,  257,   45,   41,
  119,   43,   59,   45,   66,   67,  257,   59,  121,  260,
  123,  124,  125,  264,   40,  118,  267,  120,  256,  257,
   59,  272,  260,  257,   97,   98,  264,   58,  257,  262,
  263,   93,   41,  267,  272,  273,  270,  271,  267,  261,
  274,  270,  271,  262,  263,  274,   61,  160,  256,  257,
  257,   59,  260,  260,  261,  117,  264,  264,  262,  263,
  267,   59,  165,  257,  272,  272,  260,   59,  181,   41,
  264,  184,  266,  186,  259,   96,   97,   98,  272,  258,
  261,   94,   95,  259,  259,   41,  189,  261,  257,  151,
  203,   59,  154,  261,   41,  276,  277,  278,  279,  280,
  281,   59,  276,  277,  278,  279,  280,  281,  276,  277,
  278,  279,  280,  281,  258,  261,  267,  270,  271,  262,
  263,  261,   41,   59,  257,  258,  257,  261,   41,  191,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,   59,  261,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,  262,  263,  276,  277,  278,  279,  280,  281,
  276,  277,  278,  279,  280,  281,  257,  258,  257,  263,
   59,  257,  258,  257,  276,  277,  278,  279,  280,  281,
   44,  270,  271,   44,  275,  274,  270,  271,  257,  258,
  257,  258,  261,  268,  257,  258,  257,  258,  257,  258,
  257,  258,  257,  257,  257,  260,  260,  260,   59,  264,
  264,  264,  266,  268,   59,  268,   59,  272,  272,  272,
  257,   59,   59,  260,   59,  268,  258,  264,   59,  259,
  259,  259,  269,  268,   59,  272,  263,  263,   59,  263,
   59,   59,   59,   59,   59,   59,  263,   59,   59,   59,
   59,   59,   59,   59,  268,   59,   17,   16,   44,  154,
  166,   66,  189,
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
"sentencia_control_repeat : REPEAT '(' declaracion_repeat ')' BEGIN conjunto_sentencias_repeat END ';'",
"conjunto_sentencias_repeat : BREAK ';'",
"conjunto_sentencias_repeat : sentencia_ejecutable",
"conjunto_sentencias_repeat : sentencia_ejecutable conjunto_sentencias_repeat",
"declaracion_repeat : asignacion_repeat ';' condicion_repeat ';' CTE",
"asignacion_repeat : variable_repeat '=' CTE",
"variable_repeat : ID",
"condicion_repeat : ID operador_logico expresion",
};

//#line 202 "gramatica.y"
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	public List<String> pendingTypeList;
	public String lastFuncType;
	
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
		String var = getEntradaValidaTS(variable);
		
		if (var != null) 
		{
			if (isFuncion(var))
			{
				chequeoS_funcion_no_declarada(variable);
				return;
			}

			try {
				if (analizadorLexico.tabla_simbolos.get(var).get("uso").compareTo("variable") == 0)
				{
					return;
				}
				System.out.println("error semantico: variable " + variable.sval + " no declarada");
			} catch (Exception e) {}
		}
		System.out.println("error semantico: variable " + variable.sval + " no declarada");
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
					System.out.println("error semantico: variable " + variable.sval + " ya declarada");
					return;
				}
			} catch (Exception e) {}
		}
		return;
	}
	
	private void reverificar_cte_negativa (ParserVal variable)
	{
		System.out.println("var negativa: " + variable.sval);
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
			
			System.out.println(valor);
			double lim1= Math.pow(1.17549435, -38); // 1.17549435S-38
			double lim2= Math.pow(3.40282347, 38);// 3.40282347S+38
			double lim3= Math.pow(-3.40282347, 38);//-3.40282347S+38 
			double lim4= Math.pow(-1.17549435, -38);//-1.17549435S-38
			System.out.println("lim 1: " + lim1 + " lim2: " + lim2 + " lim3: " + lim3 + " lim4: " + lim4);
			if(valor<lim1 || valor>lim2) {
				if(valor<lim3 || valor>lim4) {
					if(valor!=0) {
						fuera_de_rango = true;
							System.out.println("fuera de rangaso");
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
			System.out.println("error semantico: variable negativa fuera de rango");
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
				System.out.println("error semantico: funcion " + funcion.sval + " no declarada");
			} catch (Exception e) {}
		}
		System.out.println("error semantico: funcion " + funcion.sval + " no declarada");
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

	private void chequeoS_diferentes_tipos(ParserVal id1, ParserVal id2, boolean condicion_repeat)
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
		
		if (condicion_repeat == true) // CONDICION REPEAT DEBEN SER DE TIPO ENTERO
		{
			if (tipo_1.compareTo("INT") != 0 || tipo_2.compareTo("INT") != 0)
			{
				System.out.println("error semantico: en una sentencia repeat, los datos de condicion deben ser de tipo entero (INT)");
				return;
			}
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
	
	private boolean isTypeDef(String func)
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
			System.out.println("error semantico: el parametro de " + funcion.sval + " debe ser de tipo " + tipo_par_func +" y no de tipo " + tipo_par); 
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
		System.out.println("error semantico: no existe un typedef de nombre " + var.sval); 
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
		System.out.println("error semantico: la variable o constante deberia ser de tipo entero");
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
		System.out.println("error semantico: operador de asignacion invalido, " + op.sval + " no es una variable.");
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
			System.out.println("error semantico: en una sentencia repeat se debe comparar usando la variable de control");
		}
		return;
	}
//#line 877 "Parser.java"
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
{chequeoS_diferentes_tipos(val_peek(3), val_peek(1), false); this.reglas.add("Asignacion");}
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
{chequeoS_variable_no_declarada(val_peek(0)); chequeoS_operador_valido(val_peek(0));}
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
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false);}
break;
case 60:
//#line 127 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false);}
break;
case 62:
//#line 131 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false);}
break;
case 63:
//#line 132 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false);}
break;
case 65:
//#line 134 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false);}
break;
case 66:
//#line 135 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false);}
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
{chequeoS_diferentes_tipos(val_peek(3), val_peek(2), false);}
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
{chequeoS_repeat_check_i(val_peek(4));}
break;
case 101:
//#line 192 "gramatica.y"
{ chequeoS_repeat_tipo_entero(val_peek(2),"INT"); chequeoS_repeat_tipo_entero(val_peek(1), "INT");}
break;
case 102:
//#line 195 "gramatica.y"
{chequeoS_variable_no_declarada(val_peek(0)); chequeoS_operador_valido(val_peek(0));}
break;
case 103:
//#line 198 "gramatica.y"
{chequeoS_diferentes_tipos(val_peek(3), val_peek(1), true); this.reglas.add("Condicion_Repeat"); chequeoS_repeat_set_i(val_peek(1));}
break;
//#line 1306 "Parser.java"
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
