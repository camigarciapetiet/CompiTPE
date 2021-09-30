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
;

declaracionFuncion	: tipo FUNC ID parametro sentencias_declarativas_datos BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';'
					| tipo FUNC ID parametro sentencias_declarativas_datos BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';'
;

pre_condicion	: PRE '('condicion')' ',' CADENA
;

parametro	: '('tipo ID')'
;

retorno		: '('expresion')'
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
;

expresioncompuesta	: '('expresion')'
					| expresion 
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
			| ID parametro
;

tipo		: INT
			| SINGLE
;

clausula_seleccion_if	: IF '('condicion')' THEN bloque_sentencias_ejecutables ';' 
						| IF '('condicion')' THEN bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables ENDIF ';'
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
;
	
sentencia_control_repeat	: REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' bloque_sentencias_ejecutables {chequeo semantico }
							| REPEAT '(' ID '=' CTE ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencia_ejecutable BREAK';' END ';'{chequeo semantico}
;		

condicion_repeat	: ID operador_logico ID
					| ID operador_logico CTE
;

%%

