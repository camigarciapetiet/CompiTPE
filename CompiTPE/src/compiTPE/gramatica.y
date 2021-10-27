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

programa	: ID bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';' {System.out.println("Sentencia START programa");}
			| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
;


bloque_sentencias_declarativas	: sentencia_declarativa 
								| bloque_sentencias_declarativas sentencia_declarativa
;

sentencia_declarativa	: declaracionDatos
						| declaracionNuevoTipo
						| declaracionFuncion
;

declaracionDatos : tipo conjunto_declaracion_variables ';' {System.out.println("Declaracion de datos");}
				 | ID conjunto_declaracion_variables ';' {System.out.println("Declaracion de datos TYPEDEF");} //Tema particular 23
				 | tipo conjunto_declaracion_variables {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor");}
;

conjunto_declaracion_variables	: conjunto_declaracion_variables ',' ID
								| ID
;

declaracionNuevoTipo	: TYPEDEF ID '=' encabezado_funcion_typedef ';' {System.out.println("Declaracion TYPEDEF");}
;

encabezado_funcion_typedef	: tipo FUNC '('tipo')'
;

declaracionFuncion	: tipo FUNC ID parametro bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {System.out.println("DECLARACION FUNCION");}
					| tipo FUNC ID parametro bloque_sentencias_declarativas BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {System.out.println("DECLARACION FUNCION Y PRE CONDICION");}
					| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en la declaracion de la funcion");}
;

pre_condicion	: PRE ':' '('condicion')' ',' CADENA ';' {System.out.println("pre-condicion");}
				| PRE ':' condicion')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("pre-condicion");}
				| PRE ':' '('')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); System.out.println("pre-condicion");}
				| PRE ':' '('condicion ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("pre-condicion");}
				| PRE ':' '('condicion')'  CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); System.out.println("pre-condicion");}
				| PRE ':' '('condicion')' ',' ';'{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); System.out.println("pre-condicion");}
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

bloque_sentencias_ejecutables	: sentencia_ejecutable
								| BEGIN conjunto_sentencia_ejecutable END {System.out.println("bloque de sentencias BEGIN-END");}
								| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en bloque de sentencias ejecutables");}
;

conjunto_sentencia_ejecutable	: sentencia_ejecutable
								| conjunto_sentencia_ejecutable sentencia_ejecutable
;

sentencia_ejecutable	: asignacion
						| mensaje_pantalla
						| clausula_seleccion_if
						| sentencia_control_repeat
;

invocacion_funcion	: ID '(' factor ')' ';'
;

asignacion: ID ':=' expresioncompuesta ';' {System.out.println("Asignacion");}
		  | ID expresioncompuesta ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); System.out.println("Asignacion");}		  
;

expresioncompuesta	: '('expresion')'
					| expresion 
					| expresion')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
					|'('expresion {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
;

expresion	: expresion '+' termino 
			| expresion '-' termino
			| termino
;

termino		: termino '/' factor
			| termino '*' factor
			| factor
			| termino '/' invocacion_funcion
			| termino '*' invocacion_funcion
			| invocacion_funcion
;

factor		: ID {System.out.println("factor ID");}
			| CTE {System.out.println("Factor CTE");}
			| '-' CTE // {System.out.println("Constante negativa, se debe modificar la tabla de simbolos indicando el signo y rechequear limite}; {se hara una verificacion de rango en el analizador semantico ya que necesitamos sabes que tipo de CTE es (INT o SINGLE)}
			//| ID '('tipo ID')' No me acuerdo que era
;

tipo		: INT {System.out.println("tipo INT");}
			| SINGLE {System.out.println("tipo SINGLE");}
;

clausula_seleccion_if	: IF '('condicion')' THEN bloque_sentencias_ejecutables ENDIF ';' {System.out.println("clausula IF");}
						| IF condicion')' THEN bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF");}
						| IF '('condicion THEN bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); System.out.println("clausula IF");}
						| IF '('condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'{System.out.println("clausula IF-ELSE");}
						| IF condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); System.out.println("clausula IF-ELSE");}
						| IF '('condicion THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");System.out.println("clausula IF-ELSE");}
;


condicion	: expresion 
			| condicion operador_logico expresion 
;

operador_logico	: '||'
				| '&&' 
				| '<>'
				| '=='
				| '<='
				| '>='
				| '>'
				| '<'
;

mensaje_pantalla	: PRINT '(' CADENA ')' ';' {System.out.println("clausula PRINT");}
					| PRINT  CADENA ')' ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); System.out.println("clausula PRINT");}
					| PRINT '(' CADENA  ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); System.out.println("clausula PRINT");}
;
	
sentencia_control_repeat	: REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencias_repeat END ';'  {System.out.println("Sentencia Ejecutable REPEAT - Chequeo Semantico");}
;		

conjunto_sentencias_repeat	: BREAK ';'
							| sentencia_ejecutable
							| sentencia_ejecutable conjunto_sentencias_repeat
;

condicion_repeat	: ID operador_logico ID {System.out.println("Condicion_Repeat");}
					| ID operador_logico CTE {System.out.println("Condicion_Repeat");}
;

%%
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