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
    0,    0,    1,    2,    2,    4,    4,    4,    5,    5,
    9,    9,   10,    6,   11,    7,    7,   14,   14,   14,
   14,   14,   14,   12,   12,   12,   12,   12,   12,   16,
   13,   13,   13,   13,   18,   18,    3,    3,    3,   19,
   19,   19,   19,   24,   25,   20,   20,   20,   27,   28,
   28,   28,   28,   17,   17,   17,   29,   29,   29,   29,
   29,   29,   26,   26,   26,    8,    8,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   15,   15,   30,
   30,   30,   30,   30,   30,   30,   30,   21,   21,   21,
   23,   32,   32,   32,   31,   31,
};
final static short yylen[] = {                            2,
    6,    2,    1,    1,    2,    1,    1,    1,    3,    3,
    3,    1,    1,    5,    5,   12,   13,    8,    7,    7,
    7,    7,    7,    4,    3,    3,    3,    2,    3,    1,
    3,    2,    2,    2,    1,    4,    1,    2,    2,    1,
    1,    1,    1,    4,    1,    4,    3,    3,    1,    3,
    1,    2,    2,    3,    3,    1,    3,    3,    1,    3,
    3,    1,    1,    1,    2,    1,    1,    8,    7,    7,
   10,    9,    9,    7,    9,    5,    7,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    5,    4,    4,
   14,    2,    1,    2,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    3,    0,    0,    2,    0,   66,   67,    0,    0,
    4,    6,    7,    8,    0,   13,    0,   12,    0,    0,
    5,    0,    0,   10,    0,    0,    0,   49,    0,    0,
    0,    0,   37,   40,   41,   42,   43,    0,    0,    9,
   11,    0,    0,   39,    0,   64,    0,    0,    0,    0,
    0,   62,    0,   59,    0,    0,    0,    0,    0,   38,
    0,    0,    0,    0,    0,    0,    0,    0,   14,    0,
    0,   35,    0,   65,   80,   81,   82,   83,   84,   85,
   86,   87,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,   48,    0,    0,   52,   47,    0,   28,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   63,    0,   60,   57,   61,   58,   89,
   90,    0,    0,   46,   50,   26,    0,   27,    0,   25,
    0,    0,    0,    0,   76,    0,    0,    0,    0,   44,
   88,    0,   24,    0,    0,    0,   15,   36,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   77,    0,   70,    0,    0,    0,   74,    0,   69,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   68,
    0,    0,    0,    0,    0,    0,    0,   33,    0,    0,
   32,    0,   73,    0,   75,   72,   95,   96,    0,    0,
    0,    0,    0,   31,    0,    0,   71,    0,    0,    0,
    0,    0,    0,   16,    0,    0,   20,   21,   22,    0,
   23,   19,   17,    0,    0,    0,   18,   92,   94,    0,
   91,
};
final static short yydgoto[] = {                          3,
    4,   10,   32,   11,   12,   13,   14,   15,   17,   18,
   43,   67,  175,  146,   50,  129,   51,   71,   72,   34,
   35,   36,   37,   52,   53,   54,   38,   64,   55,   84,
  171,  226,
};
final static short yysindex[] = {                      -172,
  -22,    0,    0, -227,    0, -234,    0,    0, -189, -103,
    0,    0,    0,    0, -207,    0,  -13,    0,   13,  -81,
    0, -185,   48,    0, -234, -139,   20,    0,   82,  -34,
   64,  -70,    0,    0,    0,    0,    0,   60,  -12,    0,
    0, -163,   77,    0,    0,    0,  -68,   92, -165,   21,
   45,    0,   86,    0,   24,   71, -119, -113,   87,    0,
   84,   92,   37,   96,   67, -104, -227,  119,    0,  -81,
  -62,    0,   29,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -100,   92,   92,   92,   94,   92,   92,  113,
   -7,  123,    0,    0,  127,   53,    0,    0,  154,    0,
  -28,  156, -101, -139,  -54,  -68,  148,  -68,  -79,  -68,
   45,   24,   24,    0,  168,    0,    0,    0,    0,    0,
    0,  152,  -42,    0,    0,    0,    0,    0,  172,    0,
 -115,  178,  176,  -21,    0,  -36,  -68,  -32,  -30,    0,
    0,  196,    0,  199,   97,  -81,    0,    0,  208,  -68,
  230,   -2,  -68,  244,  -68,  245,   74,   88,   90,   98,
    0,   57,    0,  -68,  262,  102,    0,  103,    0,   49,
  275,    8,   35,   65,  294,   72,   90,  300,  104,    0,
  301,  309,    6,  114,  327,   15,  329,    0,   80,  106,
    0,  316,    0,  317,    0,    0,    0,    0,  336,  120,
  121,  -35,  122,    0,  319,  115,    0,  117,  323,  326,
  328,  -44,  330,    0,  331,  -52,    0,    0,    0,  332,
    0,    0,    0,  333,  -52,  118,    0,    0,    0,  334,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -40,    0,    0,    0,    0,    0,
    1,    0,    0,    0,  -33,    0,    0,    0,    0,    0,
    0,    0,  337,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  338,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    7,  -27,   -5,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   62,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  339,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  131,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  366,  335,  -45,   11,    0,    0,    0,   34,  380,  375,
    0,    0,  224,    0,  -24,    0,    3,   10,  324,    0,
    0,    0,    0,  134,    0,   63,    0,  342,  184,  234,
    0,  180,
};
final static int YYTABLESIZE=549;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         45,
   63,   63,   63,   63,   63,   57,   63,   56,  212,   56,
   56,   56,  128,   54,  221,   54,   54,   54,   63,   63,
   21,   63,   16,   73,  105,   56,   56,   65,   56,    6,
   25,   54,   54,  122,   54,   55,    5,   55,   55,   55,
   63,   78,    7,    8,   78,   24,    9,   79,  185,   16,
   79,  121,   49,   55,   55,  202,   55,   22,  201,   42,
   78,   83,   78,   63,   96,   89,   79,   19,   79,  109,
   88,    2,   66,   26,   82,  187,   81,   97,   44,   85,
   82,   86,   81,    1,    2,  145,  111,   85,   82,   86,
   81,   25,   74,  125,   82,   85,   81,   86,  101,   62,
  160,   68,   30,   58,   49,  188,   40,  100,   82,   49,
   81,   90,  191,   21,   85,  134,   86,  136,  138,  139,
  204,   48,   85,   62,   86,   87,   49,  172,   49,  174,
    7,    8,   49,  173,   49,   69,   49,  132,   49,   91,
   27,   28,   94,   92,   29,   93,  152,  186,   30,  115,
  117,  119,  102,    6,   98,    6,   31,  144,  104,  162,
  110,  176,  166,   20,  168,  131,    7,    8,    7,    8,
    9,  120,    9,  179,   27,   28,  189,   28,   29,  176,
   29,  137,   30,  123,   30,  124,   28,   70,   28,   29,
   31,   29,   31,   30,  126,   30,  130,   59,   70,  106,
  107,   31,   28,   31,   28,   29,  135,   29,  140,   30,
  141,   30,  143,  133,  220,  142,  224,   31,  147,   31,
   63,  116,  118,  211,   56,  150,  151,   56,  127,  153,
  154,  155,  156,   54,  148,   63,   63,   63,   63,   63,
   63,  149,   56,   56,   56,   56,   56,   56,   54,   54,
   54,   54,   54,   54,  157,   55,  158,    7,    8,  164,
  165,   78,  197,  198,   45,   46,  161,   79,  112,  113,
   55,   55,   55,   55,   55,   55,   78,   78,   78,   78,
   78,   78,   79,   79,   79,   79,   79,   79,  163,  108,
   75,   76,   77,   78,   79,   80,   75,   76,   77,   78,
   79,   80,  167,  169,   75,   76,   77,   78,   79,   80,
   75,   76,   77,   78,   79,   80,   45,   46,   29,  178,
  180,   45,   46,   99,   75,   76,   77,   78,   79,   80,
  170,   29,   29,  184,   61,   29,    7,    8,   45,   46,
   45,   46,   47,   33,   45,   46,   45,   46,   45,   46,
  114,   46,  190,   28,   28,   60,   29,   29,  193,  195,
   30,   30,  159,  177,  181,  182,  194,  196,   31,   31,
  200,  199,  203,  205,  206,  207,  208,  214,  209,  210,
  213,  217,  215,  216,  218,  230,  219,   39,  222,  223,
  227,  228,  231,   33,   23,   51,   53,   34,   93,   41,
  192,  103,   95,  183,  229,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   60,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   33,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   60,   33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   60,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  225,
    0,    0,    0,    0,    0,    0,    0,    0,  225,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   42,   43,   44,   45,   40,   47,   41,   44,   43,
   44,   45,   41,   41,   59,   43,   44,   45,   59,   60,
   10,   62,  257,   48,   70,   59,   60,   40,   62,  257,
   44,   59,   60,   41,   62,   41,   59,   43,   44,   45,
   38,   41,  270,  271,   44,   59,  274,   41,   41,  257,
   44,   59,   45,   59,   60,   41,   62,  265,   44,   26,
   60,   41,   62,   61,   62,   42,   60,  257,   62,   41,
   47,  257,   39,   61,   60,   41,   62,   41,   59,   43,
   60,   45,   62,  256,  257,  131,   84,   43,   60,   45,
   62,   44,  258,   41,   60,   43,   62,   45,   65,   40,
  146,  265,   41,   40,   45,   41,   59,   41,   60,   45,
   62,   41,   41,  103,   43,  106,   45,  108,  109,  110,
   41,   40,   43,   40,   45,   40,   45,   40,   45,   40,
  270,  271,   45,  158,   45,   59,   45,  104,   45,  259,
  256,  257,   59,  257,  260,   59,  137,  172,  264,   87,
   88,   89,  257,  257,   59,  257,  272,  273,   40,  150,
  261,  159,  153,  267,  155,  267,  270,  271,  270,  271,
  274,   59,  274,  164,  256,  257,  174,  257,  260,  177,
  260,  261,  264,   61,  264,   59,  257,  267,  257,  260,
  272,  260,  272,  264,   41,  264,   41,  268,  267,  262,
  263,  272,  257,  272,  257,  260,   59,  260,   41,  264,
   59,  264,   41,  268,  259,  258,  269,  272,   41,  272,
  261,   88,   89,  259,  259,  262,  263,  261,  257,  262,
  263,  262,  263,  261,   59,  276,  277,  278,  279,  280,
  281,  263,  276,  277,  278,  279,  280,  281,  276,  277,
  278,  279,  280,  281,   59,  261,   58,  270,  271,  262,
  263,  261,  257,  258,  257,  258,   59,  261,   85,   86,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,  276,  277,  278,  279,  280,  281,   59,  261,
  276,  277,  278,  279,  280,  281,  276,  277,  278,  279,
  280,  281,   59,   59,  276,  277,  278,  279,  280,  281,
  276,  277,  278,  279,  280,  281,  257,  258,  257,  263,
   59,  257,  258,  257,  276,  277,  278,  279,  280,  281,
  257,  270,  271,   59,  275,  274,  270,  271,  257,  258,
  257,  258,  261,   20,  257,  258,  257,  258,  257,  258,
  257,  258,   59,  257,  257,   32,  260,  260,   59,   59,
  264,  264,  266,  266,  263,  263,  263,   59,  272,  272,
   44,  258,   44,  268,   59,   59,   41,   59,  259,  259,
  259,   59,  268,  267,   59,  268,   59,   22,   59,   59,
   59,   59,   59,   70,   15,   59,   59,   59,  268,   25,
  177,   67,   61,  170,  225,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  105,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  131,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  145,  146,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  160,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  216,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  225,
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
"programa : nombre_ambito bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';'",
"programa : error ';'",
"nombre_ambito : ID",
"bloque_sentencias_declarativas : sentencia_declarativa",
"bloque_sentencias_declarativas : bloque_sentencias_declarativas sentencia_declarativa",
"sentencia_declarativa : declaracionDatos",
"sentencia_declarativa : declaracionNuevoTipo",
"sentencia_declarativa : declaracionFuncion",
"declaracionDatos : tipo conjunto_declaracion_variables ';'",
"declaracionDatos : ID conjunto_declaracion_variables ';'",
"conjunto_declaracion_variables : conjunto_declaracion_variables ',' nombre_declaracion",
"conjunto_declaracion_variables : nombre_declaracion",
"nombre_declaracion : ID",
"declaracionNuevoTipo : TYPEDEF ID '=' encabezado_funcion_typedef ';'",
"encabezado_funcion_typedef : tipo FUNC '(' tipo ')'",
"declaracionFuncion : tipo FUNC nombre_ambito parametro bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
"declaracionFuncion : tipo FUNC nombre_ambito parametro bloque_sentencias_declarativas BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';'",
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

//#line 182 "gramatica.y"
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	
	
	private void set_uso (ParserVal identificador, String uso) 
	{
		//chequear si existe?
		analizadorLexico.tabla_simbolos.get(identificador.sval).put("uso",uso);
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
	private int yylex() 
	{
		return analizadorLexico.yylex(yylval);
	}
	
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
			System.out.println("error: variable " + variable.sval + " no declarada:");
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
	
	private void chequeoS_cte_negativa (ParserVal variable)
	{
		//eliminar variable.sval de la tabla de simbolos
		variable.sval = "-" + variable.sval;
		//rechequear rango
		//si cumple rango insertar en la tabla de simbolos
		
		//si no cumple descartarlo y error
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
		System.out.println("error: funcion " + funcion.sval + " no declarada");
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
//#line 629 "Parser.java"
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
{chequeoS_redeclaracion_funcion(val_peek(0)); apilar_ambito(val_peek(0)); set_uso(val_peek(0),"funcion");}
break;
case 9:
//#line 31 "gramatica.y"
{this.reglas.add("Declaracion de datos");}
break;
case 10:
//#line 32 "gramatica.y"
{this.reglas.add("Declaracion de datos TYPEDEF");}
break;
case 13:
//#line 39 "gramatica.y"
{chequeoS_redeclaracion_variable(val_peek(0)); set_uso(val_peek(0),"variable");}
break;
case 14:
//#line 42 "gramatica.y"
{this.reglas.add("Declaracion TYPEDEF");}
break;
case 15:
//#line 45 "gramatica.y"
{System.out.println(val_peek(4).sval);}
break;
case 16:
//#line 48 "gramatica.y"
{desapilar_ambito(); this.reglas.add("DECLARACION FUNCION");}
break;
case 17:
//#line 49 "gramatica.y"
{desapilar_ambito(); this.reglas.add("DECLARACION FUNCION Y PRE CONDICION");}
break;
case 18:
//#line 52 "gramatica.y"
{this.reglas.add("pre-condicion");}
break;
case 19:
//#line 53 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("pre-condicion");}
break;
case 20:
//#line 54 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); this.reglas.add("pre-condicion");}
break;
case 21:
//#line 55 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("pre-condicion");}
break;
case 22:
//#line 56 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); this.reglas.add("pre-condicion");}
break;
case 23:
//#line 57 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); this.reglas.add("pre-condicion");}
break;
case 25:
//#line 61 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
break;
case 26:
//#line 62 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
break;
case 27:
//#line 63 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
break;
case 28:
//#line 64 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
break;
case 29:
//#line 65 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
break;
case 30:
//#line 68 "gramatica.y"
{set_uso(val_peek(0),"parametro");}
break;
case 32:
//#line 72 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 33:
//#line 73 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
break;
case 34:
//#line 74 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 36:
//#line 78 "gramatica.y"
{this.reglas.add("bloque de sentencias BEGIN-END");}
break;
case 39:
//#line 83 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error de sentencias ejecutables");}
break;
case 45:
//#line 96 "gramatica.y"
{chequeoS_funcion_no_declarada(val_peek(0));}
break;
case 46:
//#line 99 "gramatica.y"
{this.reglas.add("Asignacion");}
break;
case 47:
//#line 100 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion");}
break;
case 48:
//#line 101 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": variable o constante faltante"); this.reglas.add("Asignacion");}
break;
case 49:
//#line 104 "gramatica.y"
{chequeoS_variable_no_declarada(val_peek(0));}
break;
case 52:
//#line 109 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
break;
case 53:
//#line 110 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
break;
case 63:
//#line 126 "gramatica.y"
{this.reglas.add("factor ID"); chequeoS_variable_no_declarada(val_peek(0));}
break;
case 64:
//#line 127 "gramatica.y"
{this.reglas.add("Factor CTE");}
break;
case 65:
//#line 128 "gramatica.y"
{chequeoS_cte_negativa(val_peek(1));}
break;
case 66:
//#line 132 "gramatica.y"
{this.reglas.add("tipo INT");}
break;
case 67:
//#line 133 "gramatica.y"
{this.reglas.add("tipo SINGLE");}
break;
case 68:
//#line 136 "gramatica.y"
{this.reglas.add("clausula IF");}
break;
case 69:
//#line 137 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF");}
break;
case 70:
//#line 138 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("clausula IF");}
break;
case 71:
//#line 139 "gramatica.y"
{this.reglas.add("clausula IF-ELSE");}
break;
case 72:
//#line 140 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF-ELSE");}
break;
case 73:
//#line 141 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");this.reglas.add("clausula IF-ELSE");}
break;
case 74:
//#line 142 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado");  this.reglas.add("clausula IF");}
break;
case 75:
//#line 143 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); this.reglas.add("clausula IF-ELSE");}
break;
case 76:
//#line 144 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante");  this.reglas.add("clausula IF");}
break;
case 77:
//#line 145 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante"); this.reglas.add("clausula IF-ELSE");}
break;
case 88:
//#line 164 "gramatica.y"
{this.reglas.add("clausula PRINT");}
break;
case 89:
//#line 165 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); this.reglas.add("clausula PRINT");}
break;
case 90:
//#line 166 "gramatica.y"
{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); this.reglas.add("clausula PRINT");}
break;
case 91:
//#line 169 "gramatica.y"
{this.reglas.add("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
break;
case 95:
//#line 177 "gramatica.y"
{this.reglas.add("Condicion_Repeat");}
break;
case 96:
//#line 178 "gramatica.y"
{this.reglas.add("Condicion_Repeat");}
break;
//#line 998 "Parser.java"
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
