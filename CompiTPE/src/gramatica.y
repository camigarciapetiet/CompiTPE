%{
import compiTPE;
%}

%token ID CTE CADENA IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK INT SINGLE REPEAT PRE ':=' '||' '&&' '<>' '==' '<=' '>='
%start programa

%left '||'
%left '&&'
%left '>' '<' '>=' '<=' '==' '<>'

%%

programa: ID bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';'
;

bloque_sentencias_declarativas	: sentencia_declarativa
								| bloque_sentencias_declarativas sentencia_declarativa
;

sentencia_declarativa	: declaracionDatos
						| declaracionFuncion
;

declaracionDatos : tipo factor ','
				 | tipo factor {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de factor")}
;

declaracionFuncion	: tipo FUNC ID parametro sentencias_declarativas_datos BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';'
					| tipo FUNC ID parametro sentencias_declarativas_datos BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';'
;

pre_condicion	: PRE '('condicion')' ',' CADENA
				| PRE condicion')' ',' CADENA {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion")}
				|PRE '('')' ',' CADENA {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'")}
				|PRE '('condicion ',' CADENA {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion")}
				|PRE '('condicion')'  CADENA {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'")}
				|PRE '('condicion')' ',' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','")}
;

parametro	: '('tipo ID')'
			| tipo ID')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo")}
			|'(' ID')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('")}
			|'('tipo ')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo")}
			|'('')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'")}
			|'('tipo ID {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID")}
;

retorno		: '('expresion')'
			| expresion')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion")}
			|'('')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": expresion esperada entre '(' ')'")}
			|'('expresion {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion")}
;

sentencias_declarativas_datos 	: declaracionDatos 
								| sentencias_declarativas_datos declaracionDatos 
;

bloque_sentencias_ejecutables	: sentencia_ejecutable
								| BEGIN conjunto_sentencia_ejecutable END
;

conjunto_sentencia_ejecutable	: sentencia_ejecutable
								| conjunto_sentencia_ejecutable sentencia_ejecutable
;

sentencia_ejecutable	: asignacion
						| mensaje_pantalla
						| clausula_seleccion_if
						| sentencia_control_repeat
;

asignacion: ID ':=' expresioncompuesta ';'
		  | ID  expresioncompuesta ';' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID")}
		  | ID ':=' expresioncompuesta {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ';' esperado despues de expresioncompuesta")}
;

expresioncompuesta	: '('expresion')'
					| expresion 
					| expresion')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion")}
					|'('expresion {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion")}
;

expresion	: expresion '+' termino 
			| expresion '-' termino
			| termino
;

termino		: termino '/' factor
			| termino '*' factor
			| factor
;

factor		: ID 
			| CTE
			| '-' CTE {//se hara una verificacion de rango en el analizador semantico ya que necesitamos sabes que tipo de CTE es (INT o SINGLE)}
			| ID parametro
;

tipo		: INT
			| SINGLE
;

clausula_seleccion_if	: IF '('condicion')' THEN bloque_sentencias_ejecutables ';' 
						| IF condicion')' THEN bloque_sentencias_ejecutables ';' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion")}
						| IF '('condicion THEN bloque_sentencias_ejecutables ';' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion")}
						| IF '('condicion')' THEN bloque_sentencias_ejecutables  {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ';' esperado despues de bloque de sentencias ejecutables")}
						| IF '('condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'
						| IF condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion")}
						| IF '('condicion THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion")}
						| IF '('condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF  {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ';' esperado despues de ENDIF")}
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

mensaje_pantalla	: PRINT '(' CADENA ')' ';'
					| PRINT  CADENA ')' ';' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena")}
					| PRINT '(' CADENA  ';' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena")}
					| PRINT '(' CADENA ')' {erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ';' esperado despues de ')'")}
;
	
sentencia_control_repeat	: REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' bloque_sentencias_ejecutables {chequeo semantico }
							| REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencia_ejecutable BREAK';' END ';'{chequeo semantico}
;		

condicion_repeat	: ID operador_logico ID
					| ID operador_logico CTE
;

%%

//hola