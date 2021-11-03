%{
package compiTPE;
import java.util.*;
%}

%token ID CTE CADENA IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK INT SINGLE REPEAT PRE TYPEDEF ':=' '||' '&&' '<>' '==' '<=' '>='
%start programa

%left '||'
%left '&&'
%left '>' '<' '>=' '<=' '==' '<>'

%%

programa	: ID bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';' {this.reglas.add("Sentencia START programa");}
			| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
;


bloque_sentencias_declarativas	: sentencia_declarativa 
								| bloque_sentencias_declarativas sentencia_declarativa
;

sentencia_declarativa	: declaracionDatos
						| declaracionNuevoTipo
						| declaracionFuncion
;

declaracionDatos : tipo conjunto_declaracion_variables ';' {this.reglas.add("Declaracion de datos");}
				 | ID conjunto_declaracion_variables ';' {this.reglas.add("Declaracion de datos TYPEDEF");} //Tema particular 23
				 | tipo conjunto_declaracion_variables {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
;

conjunto_declaracion_variables	: conjunto_declaracion_variables ',' ID
								| ID
;

declaracionNuevoTipo	: TYPEDEF ID '=' encabezado_funcion_typedef ';' {this.reglas.add("Declaracion TYPEDEF");}
;

encabezado_funcion_typedef	: tipo FUNC '('tipo')'
;

declaracionFuncion	: tipo FUNC ID parametro bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {this.reglas.add("DECLARACION FUNCION");}
					| tipo FUNC ID parametro bloque_sentencias_declarativas BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {this.reglas.add("DECLARACION FUNCION Y PRE CONDICION");}
;

pre_condicion	: PRE ':' '('condicion')' ',' CADENA ';' {this.reglas.add("pre-condicion");}
				| PRE ':' condicion')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("pre-condicion");}
				| PRE ':' '('')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion')'  CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion')' ',' ';'{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); this.reglas.add("pre-condicion");}
;

parametro	: '('tipo ID')'
			| tipo ID')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
			|'(' ID')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
			|'('tipo ')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
			|'('')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
			|'('tipo ID {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
;

retorno		: '('expresion')'
			| expresion')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
			|'('')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
			|'('expresion {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
;

bloque_sentencias_ejecutables	: sentencia_ejecutable {$$= new Nodo("S", $1, null);}
								| BEGIN conjunto_sentencia_ejecutable END ';' {this.reglas.add("bloque de sentencias BEGIN-END"); $$= $2;}
								| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en bloque de sentencias ejecutables");}
;

conjunto_sentencia_ejecutable	: sentencia_ejecutable {$$= new Nodo("S", $1, null);}
								| sentencia_ejecutable conjunto_sentencia_ejecutable  {$$= new Nodo("S", $1, $2);}
;

sentencia_ejecutable	: asignacion {$$= $1;}
						| mensaje_pantalla {$$= $1;}
						| clausula_seleccion_if {$$= $1;}
						| sentencia_control_repeat {$$= $1;}
;

invocacion_funcion	: ID '(' factor ')' ';'
;

asignacion	: ID ':=' expresioncompuesta ';' {this.reglas.add("Asignacion"); $$= new Nodo($2, $1, $3);}
		  	| ID expresioncompuesta ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion"); $$= new Nodo(":=", $1, $2)}		  
;

expresioncompuesta	: '('expresion')' {$$= $2;}
					| expresion {$$= $1;}
					| expresion')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion"); $$= $1;}
					|'('expresion {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion"); $$= $2;}
;

expresion	: expresion '+' termino {$$= new Nodo($2, $1, $3);}
			| expresion '-' termino {$$= new Nodo($2, $1, $3);}
			| termino {$$= $1;}
;

termino		: termino '/' factor {$$= new Nodo($2, $1, $3);}
			| termino '*' factor {$$= new Nodo($2, $1, $3);}
			| factor {$$= $1;}
			| termino '/' invocacion_funcion {$$= new Nodo($2, $1, $3);}
			| termino '*' invocacion_funcion {$$= new Nodo($2, $1, $3);}
			| invocacion_funcion {$$= $1;}
;

factor		: ID {this.reglas.add("factor ID"); $$=new Nodo($1);}
			| CTE {this.reglas.add("Factor CTE"); $$=new Nodo($1);}
			| '-' CTE // {this.reglas.add("Constante negativa, se debe modificar la tabla de simbolos indicando el signo y rechequear limite}; {se hara una verificacion de rango en el analizador semantico ya que necesitamos sabes que tipo de CTE es (INT o SINGLE)}
;

tipo		: INT {this.reglas.add("tipo INT");}
			| SINGLE {this.reglas.add("tipo SINGLE");}
;

clausula_seleccion_if : IF '('condicion')' cuerpo_if ENDIF ';' {$$= new Nodo("IF", $3, $5);}
					  | IF condicion')'  cuerpo_if ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF"); $$= new Nodo("IF", $2, $4);}
					  | IF '('condicion  cuerpo_if ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("clausula IF"); $$= new Nodo("IF", $3, $4);}
;

cuerpo_if : cuerpo_then {$$= new Nodo("cuerpo", $1, null);}
		  | cuerpo_then cuerpo_else  {$$= new Nodo("cuerpo", $1, $2);}
;

cuerpo_then : THEN bloque_sentencias_ejecutables {$$= new Nodo("THEN", $2, null);}
			| bloque_sentencias {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); $$= new Nodo("THEN", $1, null);}
;

cuerpo_else : ELSE bloque_sentencias_ejecutables {$$= new Nodo("ELSE", $2, null);}
;

condicion	: expresion {$$=new Nodo("condicion", $1, null);}
			| condicion operador_logico expresion {$$= $1.setHijos($1, $3);}
;

operador_logico	: '||' {$$= new Nodo($1);}
				| '&&' {$$= new Nodo($1);}
				| '<>' {$$= new Nodo($1);}
				| '==' {$$= new Nodo($1);}
				| '<=' {$$= new Nodo($1);}
				| '>=' {$$= new Nodo($1);}
				| '>' {$$= new Nodo($1);}
				| '<' {$$= new Nodo($1);}
;

mensaje_pantalla	: PRINT '(' CADENA ')' ';' {this.reglas.add("clausula PRINT");}
					| PRINT  CADENA ')' ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); this.reglas.add("clausula PRINT");}
					| PRINT '(' CADENA  ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); this.reglas.add("clausula PRINT");}
;
	
sentencia_control_repeat	: REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencias_repeat END ';'  {this.reglas.add("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
;		

conjunto_sentencias_repeat	: BREAK ';'
							| sentencia_ejecutable
							| sentencia_ejecutable conjunto_sentencias_repeat
;

condicion_repeat	: ID operador_logico ID {this.reglas.add("Condicion_Repeat");}
					| ID operador_logico CTE {this.reglas.add("Condicion_Repeat");}
;

%%
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	public Nodo raiz;
	
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