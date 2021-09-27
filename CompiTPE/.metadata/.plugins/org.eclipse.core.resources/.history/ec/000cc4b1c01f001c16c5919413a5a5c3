%{
import compiTPE;
%}

%token ID CTE IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK INT SINGLE REPEAT PRE := || && <> == <= >=

%%

// COSAS SACADAS DE LAS FILMINAS QUE SE USAN



declaracionFuncion	: tipo FUNC ID '('parametro')' sentencia_declarativa_funcion BEGIN sentencia_ejecutable_funcion RETURN '('retorno')' ';' END ';'
					| tipo FUNC ID '('parametro')' sentencia_declarativa_funcion BEGIN pre_condicion sentencia_ejecutable_funcion RETURN '('retorno')' ';' END ';'
;	// falta para representar el cuerpo pero no se si iria por ejemplo saltos de linea y todo eso?

pre_condicion	: PRE '('condicion')' ',' cadena // DUDA DE LAS CADENAS TAMBIEN
;

parametro	: tipo ID
;

retorno		: expresioncompuesta
;

sentencia_declarativa_funcion 	: declaracionDatos sentencia_declarativa_funcion //PUEDE NO TENER DECLARACIONES?
								| declaracionDatos
;

declaracionDatos : tipo factor ';'
;

sentencia_ejecutable_funcion	: asignacion sentencia_ejecutable_funcion
								| asignacion
								| sentencia_ejecutable sentencia_ejecutable_funcion
								| sentencia_ejecutable  // esto serian IF ELSE WHILE etc
;

bloque_de_sentencias	: sentencia_ejecutable // preguntar si puede tener IFs anidados
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


tipo		: INT ','
			| SINGLE ','
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

mensaje_pantalla	: PRINT '(' cadena ')' ';'
;

cadena	: ID // las CADENAS son identificadores? como asegurarse que siempre tengan los % % ya que nuestro AL guarda cadenas con los % pero existen IDs sin %.
;
	
	
sentencia_control_repeat	: REPEAT '('asignacion_int ';' condicion_repeat ';' CTE ')' bloque_de_sentencias // Como hacer que las CTE y los ID sean enteros y las cte entre 1 - 2 - 3 - 4 y que la condicion compare con i si o si?
							| REPEAT '('asignacion_int ';' condicion_repeat ';' CTE ')' BEGIN conjunto_sentencia_ejecutable BREAK ';' END // si pongo bloque de sentencias no puedo poner el break adentro, no?
;		

condicion_repeat	:
;

// Ver de no tener duplicacion de terminos ejemplo bloque_de_sentencias + conjunto_sentencia_ejecutable y sentencia_ejecutable_funcion!!! El primero esta siendo usado en if y repeat y el otro en funciones, pero creo que hace lo mismo
// falta: 
//	PRECEDENCIA DE LOS OPERADORES
//	LA PARTE INICIAL DEL PROGRAMA seria como programa : nombre bloque_sentencias o algo asi!
