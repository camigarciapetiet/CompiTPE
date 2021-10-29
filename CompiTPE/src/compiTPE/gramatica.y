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

programa	: nombre_ambito bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';' {desapilar_ambito(); this.reglas.add("Sentencia START programa");}
			| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
;

nombre_ambito	: ID {chequeoS_redeclaracion_funcion($1); apilar_ambito($1); set_uso($1,"funcion");} 
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
;

conjunto_declaracion_variables	: conjunto_declaracion_variables ',' nombre_declaracion
								| nombre_declaracion
;

nombre_declaracion	: ID {chequeoS_redeclaracion_variable($1); set_uso($1,"variable");}
;

declaracionNuevoTipo	: TYPEDEF ID '=' encabezado_funcion_typedef ';' {this.reglas.add("Declaracion TYPEDEF");}
;

encabezado_funcion_typedef	: tipo FUNC '('tipo')' {System.out.println($1.sval);}
;

declaracionFuncion	: tipo FUNC nombre_ambito parametro bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {desapilar_ambito(); this.reglas.add("DECLARACION FUNCION");}
					| tipo FUNC nombre_ambito parametro bloque_sentencias_declarativas BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {desapilar_ambito(); this.reglas.add("DECLARACION FUNCION Y PRE CONDICION");}
;

pre_condicion	: PRE ':' '('condicion')' ',' CADENA ';' {this.reglas.add("pre-condicion");}
				| PRE ':' condicion')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("pre-condicion");}
				| PRE ':' '('')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion')'  CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion')' ',' ';'{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); this.reglas.add("pre-condicion");}
;

parametro	: '('tipo nombre_parametro')'
			| tipo ID')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
			|'(' ID')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
			|'('tipo ')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
			|'('')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
			|'('tipo ID {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
;

nombre_parametro	: ID {set_uso($1,"parametro");}
;

retorno		: '('expresion')'
			| expresion')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
			|'('')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'");}
			|'('expresion {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
;

bloque_sentencias_ejecutables	: sentencia_ejecutable
								| BEGIN conjunto_sentencia_ejecutable END ';' {this.reglas.add("bloque de sentencias BEGIN-END");}
;

conjunto_sentencia_ejecutable	: sentencia_ejecutable
								| conjunto_sentencia_ejecutable sentencia_ejecutable
								| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error de sentencias ejecutables");}
								
;

sentencia_ejecutable	: asignacion
						| mensaje_pantalla
						| clausula_seleccion_if
						| sentencia_control_repeat
;

invocacion_funcion	: nombre_invocacion '(' factor ')'
;

nombre_invocacion : ID {chequeoS_funcion_no_declarada($1);}
;

asignacion	: operador_asignacion ':=' expresioncompuesta ';' {this.reglas.add("Asignacion");}
		  	| operador_asignacion expresioncompuesta ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion");}		  
			| operador_asignacion ':=' ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": variable o constante faltante"); this.reglas.add("Asignacion");}
;

operador_asignacion	: ID {chequeoS_variable_no_declarada($1);}
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

factor		: ID {this.reglas.add("factor ID"); chequeoS_variable_no_declarada($1);}
			| CTE {this.reglas.add("Factor CTE");}
			| '-' CTE {chequeoS_cte_negativa($1);}
		// {this.reglas.add("Constante negativa, se debe modificar la tabla de simbolos indicando el signo y rechequear limite}; {se hara una verificacion de rango en el analizador semantico ya que necesitamos sabes que tipo de CTE es (INT o SINGLE)}
;

tipo		: INT {this.reglas.add("tipo INT");}
			| SINGLE {this.reglas.add("tipo SINGLE");}
;

clausula_seleccion_if	: IF '('condicion')' THEN bloque_sentencias_ejecutables ENDIF ';' {this.reglas.add("clausula IF");}
						| IF condicion')' THEN bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF");}
						| IF '('condicion THEN bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("clausula IF");}
						| IF '('condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'{this.reglas.add("clausula IF-ELSE");}
						| IF condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("clausula IF-ELSE");}
						| IF '('condicion THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion");this.reglas.add("clausula IF-ELSE");}
						| IF '('condicion')' bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado");  this.reglas.add("clausula IF");}
						| IF '('condicion')'  bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); this.reglas.add("clausula IF-ELSE");}
						| IF THEN bloque_sentencias_ejecutables ENDIF ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante");  this.reglas.add("clausula IF");}
						| IF THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": condicion faltante"); this.reglas.add("clausula IF-ELSE");}

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
			System.out.println("error: variable " + variable.sval + " no declarada.");
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