%{
import compiTPE;
%}

%token ID CTE CADENA IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK INT SINGLE REPEAT PRE ":=" "||" "&&" "<>" "==" "<=" ">="
%start programa

%left "||"
%left "&&"
%left '>' '<' ">=" "<=" "==" "<>"
%left '+' '-'
%left '*' '/'

%%

programa: ID sentencia_declarativa_funcion BEGIN bloque_sentencias_declarativas END
;

bloque_sentencias_declarativas	: declaracionFuncion bloque_sentencias_declarativas
								| declaracionFuncion
								| bloque_de_sentencias
;

declaracionFuncion	: tipo FUNC ID '('parametro')' sentencia_declarativa_funcion BEGIN conjunto_sentencia_ejecutable RETURN '('retorno')' ';' END ';'
					| tipo FUNC ID '('parametro')' sentencia_declarativa_funcion BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN '('retorno')' ';' END ';'
;

pre_condicion	: PRE '('condicion')' ',' CADENA
;

parametro	: tipo ID
;

retorno		: expresioncompuesta
;

sentencia_declarativa_funcion 	: declaracionDatos sentencia_declarativa_funcion
								| declaracionDatos
;

declaracionDatos : tipo factor ','
;

bloque_de_sentencias	: sentencia_ejecutable
						| BEGIN conjunto_sentencia_ejecutable END
;

conjunto_sentencia_ejecutable	: sentencia_ejecutable
								| sentencia_ejecutable sentencia_ejecutable
;

sentencia_ejecutable	: asignacion
						| clausula_seleccion_if
						| mensaje_pantalla
						| sentencia_control_repeat
;

asignacion: ID ":=" expresioncompuesta ';'
;

expresioncompuesta	: '('expresion')'
					| expresion
					| expresioncompuesta '+' expresioncompuesta
					| expresioncompuesta '-' expresioncompuesta
					| expresioncompuesta '*' expresioncompuesta
					| expresioncompuesta '/' expresioncompuesta
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
			| '-' CTE {chequear rango}
			| ID '('parametro')'
;


tipo		: INT
			| SINGLE
;

clausula_seleccion_if	: IF '('condicion')' THEN bloque_de_sentencias ELSE bloque_de_sentencias ENDIF ';'
						| IF '('condicion')' THEN bloque_de_sentencias ';'
;

condicion	: condicion operador_logico condicion
			| expresion
;

operador_logico	: "||" 
				| "&&" 
				| "<>" 
				| "=="
				| "<="
				| ">="
				| '>'
				| '<'
;

mensaje_pantalla	: PRINT '(' CADENA ')' ';'
;
	
sentencia_control_repeat	: REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' bloque_de_sentencias {chequeo semantico }
							| REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencia_ejecutable BREAK ';' END {chequeo semantico}
;		

condicion_repeat	: ID operador_logico ID
					| ID operador_logico CTE
;
