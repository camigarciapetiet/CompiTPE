%{
import compiTPE;
%}

%left '||'
%left '&&'
%left '>' '<' '>=' '<=' '==' '<>'
%left '+' '-'
%left '*' '/'

// si se deja a los operadores en el %left, hay que sacarlos de los tokens
%token ID CTE IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK INT SINGLE REPEAT := || && <> == <= >=

%%

// COSAS SACADAS DE LAS FILMINAS QUE SE USAN

asignacion: ID := expresioncompuesta ';'
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
			| ID '('parametro')' //2da sentencia ejecutable del tpe
;


SENTENCIAS DE DECLARACION:

declaracionDatos : tipo factor ';'
;

tipo		: INT ','
			| SINGLE ','
;

--hacer las declaraciones de los temas particulares--: TEMA 7 Y 17

declaracionFuncion : tipo FUNC ID '('parametro')' sentencia_declarativa_funcion BEGIN sentencia_ejecutable_funcion RETURN '('retorno')' ';' END ';'
;// falta para representar el cuerpo pero no se si iria por ejemplo saltos de linea y todo eso?

parametro	: tipo ID
;

retorno		: expresioncompuesta
;

sentencia_declarativa_funcion 	: declaracionDatos sentencia_declarativa_funcion //PUEDE NO TENER DECLARACIONES? (poner en el informe)
								| declaracionDatos
;

sentencia_ejecutable_funcion	: asignacion sentencia_ejecutable_funcion
								| asignacion
								| sentencia_ejecutable sentencia_ejecutable_funcion
								| sentencia_ejecutable  // esto serian IF ELSE WHILE etc
;

SENTENCIAS EJECUTABLES:

sentencia_ejecutable	: asignacion
						| clausula_seleccion_if
						| mensaje_pantalla
;

clausula_seleccion_if	: IF '('condicion')' THEN bloque_de_sentencias ELSE bloque_de_sentencias ENDIF ';'
						| IF '('condicion')' THEN bloque_de_sentencias ';'
;

condicion	: condicion operador_logico condicion
			| expresion
;

operador_logico	: || 
				| && 
				| <> 
				| == 
				| <= 
				| >=
				| '>'
				| '<'
;

bloque_de_sentencias	: sentencia_ejecutable // preguntar si puede tener IFs anidados
						| BEGIN conjunto_sentencia_ejecutable END
;

conjunto_sentencia_ejecutable	: sentencia_ejecutable
								| sentencia_ejecutable sentencia_ejecutable
;

mensaje_pantalla	: PRINT '(' cadena ')' ';'
;

cadena	: ID // las CADENAS son identificadores? como asegurarse que siempre tengan los % % ya que nuestro AL guarda cadenas con los % pero existen IDs sin %.
;


falta: 
	PRECEDENCIA DE LOS OPERADORES
	DUDAS
	TEMA PARTICULAR 7
	TEMA PARTICULAR 17
