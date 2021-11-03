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

programa	: nombre_programa bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';' {desapilar_ambito(); this.reglas.add("Sentencia START programa");}
			| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
;

nombre_programa	: ID {apilar_ambito($1); set_campo($1,"uso","programa");}
;

nombre_ambito	: ID {chequeoS_redeclaracion_funcion($1); apilar_ambito($1); set_campo($1,"uso","funcion"); setLastFuncType($1);} 
;

bloque_sentencias_declarativas	: sentencia_declarativa 
								| bloque_sentencias_declarativas sentencia_declarativa
;

sentencia_declarativa	: declaracionDatos
						| declaracionNuevoTipo
						| declaracionFuncion
;

declaracionDatos : tipo conjunto_declaracion_variables ';' {setPendingTypes($1.sval); this.reglas.add("Declaracion de datos");}
				 | nombre_typedef conjunto_declaracion_variables ';' {setPendingTypes($1.sval); this.reglas.add("Declaracion de datos TYPEDEF");} //Tema particular 23
;
nombre_typedef	: ID {chequeoS_typedef_existe($1);}
;

conjunto_declaracion_variables	: conjunto_declaracion_variables ',' nombre_declaracion
								| nombre_declaracion
;
	
nombre_declaracion	: ID {chequeoS_redeclaracion_variable($1); set_campo($1,"uso","variable"); addPendingTypeList($1.sval);}
;

declaracionNuevoTipo	: TYPEDEF nombre_funcion_typedef '=' encabezado_funcion_typedef ';' 
;

nombre_funcion_typedef	: ID {addPendingTypeList($1.sval); set_campo($1, "uso", "typedef"); setLastFuncType($1);} //uso typedef o uso funcion??
;

encabezado_funcion_typedef	: tipo FUNC '('tipo')' {setPendingTypes("funcion typedef"); setFuncType("tipo",$1.sval); setFuncType("parametro",$2.sval);}
;

declaracionFuncion	: cabeza_funcion bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {desapilar_ambito(); this.reglas.add("DECLARACION FUNCION");}
					| cabeza_funcion bloque_sentencias_declarativas BEGIN pre_condicion conjunto_sentencia_ejecutable RETURN retorno ';' END ';' {desapilar_ambito(); this.reglas.add("DECLARACION FUNCION Y PRE CONDICION");}
;

cabeza_funcion	: tipo FUNC nombre_ambito parametro {setFuncType("tipo",$1.sval); copiarTipoParametro($4,$3);}
;

pre_condicion	: PRE ':' '('condicion')' ',' CADENA ';' {this.reglas.add("pre-condicion");}
				| PRE ':' condicion')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de condicion"); this.reglas.add("pre-condicion");}
				| PRE ':' '('')' ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada entre '(' ')'"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion ',' CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de condicion"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion')'  CADENA ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ',' esperado despues de ')'"); this.reglas.add("pre-condicion");}
				| PRE ':' '('condicion')' ',' ';'{this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": cadena esperada despues de ','"); this.reglas.add("pre-condicion");}
;

parametro	: '('tipo nombre_parametro')' {setPendingTypes($2.sval);}
			| tipo ID')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de tipo");}
			|'(' ID')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo esperado despues de '('");}
			|'('tipo ')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ID esperado despues de tipo");}
			|'('')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": tipo e ID esperados entre '(' ')'");}
			|'('tipo ID {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de ID");}
;

nombre_parametro	: ID {set_campo($1,"uso", "parametro"); addPendingTypeList($1.sval);}
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

invocacion_funcion	: nombre_invocacion '(' factor ')' {chequeoS_parametro_funcion($1, $2);}
//Esto es tanto para typedef como funcion!
;

nombre_invocacion : ID {chequeoS_funcion_no_declarada($1);}
;

asignacion	: operador_asignacion ':=' expresioncompuesta ';' {chequeoS_diferentes_tipos($1, $3); this.reglas.add("Asignacion");}
		  	| operador_asignacion expresioncompuesta ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion");}		  
			| operador_asignacion ':=' ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": variable o constante faltante"); this.reglas.add("Asignacion");}
			| operador_asignacion ':=' '-' ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": variable o constante faltante"); this.reglas.add("Asignacion");}
;

operador_asignacion	: ID {chequeoS_variable_no_declarada($1);} //Esto ya no permite que se pongan funciones para asignar
;

expresioncompuesta	: '('expresion')'
					| expresion 
					| expresion')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion");}
					|'('expresion {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");}
;

expresion	: expresion '+' termino {chequeoS_diferentes_tipos($0, $1);}
			| expresion '-' termino {chequeoS_diferentes_tipos($0, $1);}
			| termino
;

termino		: termino '/' factor {chequeoS_diferentes_tipos($0, $1);}
			| termino '*' factor {chequeoS_diferentes_tipos($0, $1);}
			| factor
			| termino '/' invocacion_funcion {chequeoS_diferentes_tipos($0, $1);}
			| termino '*' invocacion_funcion {chequeoS_diferentes_tipos($0, $1);}
			| invocacion_funcion
;

factor		: ID {this.reglas.add("factor ID"); chequeoS_variable_no_declarada($1);}
			| CTE {this.reglas.add("Factor CTE");}
			| '-' CTE {reverificar_cte_negativa($1);}
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
			| condicion operador_logico expresion {chequeoS_diferentes_tipos($0, $1);}
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

condicion_repeat	: ID operador_logico ID {System.out.println("Comparacion: " + $0.sval + " " + $1.sval); this.reglas.add("Condicion_Repeat");}
					| ID operador_logico CTE {this.reglas.add("Condicion_Repeat");}
;

%%
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	public List<String> pendingTypeList;
	public String lastFuncType;
	
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
		String variable_ambito = variable.sval;
		boolean variable_declarada = false;
		if (isFuncion(variable.sval))
		{
			chequeoS_funcion_no_declarada(variable);
			return;
		}
		while (variable_ambito.lastIndexOf('.') != -1 && !variable_declarada)
		{	
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null) 
	            {
	            	if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("variable") == 0)
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
			System.out.println("error semantico: variable " + variable.sval + " no declarada.");
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
	            	if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("variable") == 0)
	                	variable_redeclarada = true;
	            }
	        }
		
		if (variable_redeclarada)
		{
			System.out.println("error semantico: variable " + variable.sval + " ya declarada");
		}
	}
	
	private void reverificar_cte_negativa (ParserVal variable)
	{
		String new_variable = "-" + variable.sval;
		String tipo = analizadorLexico.tabla_simbolos.get(variable.sval).get("tipo");
		boolean fuera_de_rango = false;
		long aux= Long.parseLong(new_variable);
		if (tipo.compareTo("INT") == 0)
		{
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
			double lim1= Math.pow(1.17549435, -38); // 1.17549435S-38
			double lim2= Math.pow(3.40282347, 38);// 3.40282347S+38
			double lim3= Math.pow(-3.40282347, 38);//-3.40282347S+38 
			double lim4= Math.pow(-1.17549435, -38);//-1.17549435S-38
			if(valor<lim1 || valor>lim2) {
				if(valor<lim3 || valor>lim4) {
					if(valor!=0) {
						fuera_de_rango = true;
				}}
			}else if(valor<lim3 || valor>lim4) {
				if(valor<lim1 || valor>lim2) {
					if(valor!=0) {
						fuera_de_rango = true;
					}
				}
			}
		}
		//rechequear rango
		
		
		if (!fuera_de_rango)
		{
			analizadorLexico.tabla_simbolos.put(new_variable, new HashMap<String,String>());
			analizadorLexico.tabla_simbolos.get(new_variable).put("tipo", tipo);
			analizadorLexico.tabla_simbolos.get(new_variable).put("uso", "constante");
		}
		if (fuera_de_rango)
		{
			//descartarlo y error
		}
		variable.sval = new_variable;
	}
	
	private void chequeoS_funcion_no_declarada (ParserVal funcion)
	{
		
		boolean isTypeDef = isTypeDef(getTipoVariable(funcion.sval));
		if (isTypeDef)
		{
			funcion.sval = getTipoVariable(funcion.sval);
			chequeoS_typedef_existe(funcion);
			return;
		}
		
		
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
	            	if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("funcion") == 0)
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
		System.out.println("error semantico: funcion " + funcion.sval + " no declarada");
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

	
	private boolean isFuncion(String id)
	{
		if (analizadorLexico.tabla_simbolos.get(id).get("uso") != null)
		{
			if (analizadorLexico.tabla_simbolos.get(id).get("uso").compareTo("funcion") == 0)
				return true;
		}
		return false;
		
	}
	private void chequeoS_diferentes_tipos(ParserVal id1, ParserVal id2)
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
	
	private String getTipoParametroFuncion(String funcion)
	{
			String variable_ambito = funcion;
			while (variable_ambito.lastIndexOf('.') != -1)
			{	
				Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
		        while (iterator.hasNext()) 
		        {
		            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
		            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("parametro") != null)
		            {
		                return analizadorLexico.tabla_simbolos.get(entry.getKey()).get("parametro");
		            }
		        }
		        int ambito_index = variable_ambito.lastIndexOf('.');
				if (ambito_index > 0) {
					variable_ambito = variable_ambito.substring(0,ambito_index);
				}
			
			}
		
		return "nulo";
	}
	
	private boolean isTypeDef(String func)
	{
		try {
			if (analizadorLexico.tabla_simbolos.get(func).get("tipo") != null)
				return true;
		} catch (Exception e) {}
		return false;
		
	}
	
	private void chequeoS_parametro_funcion(ParserVal funcion, ParserVal parametro)
	{
		String tipo_parametro_funcion = getTipoParametroFuncion(funcion.sval);
		String tipo_parametro = getTipoVariable(parametro.sval);
		
		if (isTypeDef(getTipoVariable(funcion.sval)))
		{
			tipo_parametro_funcion = getTipoParametroFuncion(getTipoVariable(funcion.sval));
		}
		
		if (tipo_parametro_funcion.compareTo(tipo_parametro) == 0 && tipo_parametro_funcion != "nulo")
		{
			return;
		}
		System.out.println("error semantico: el parametro de " + funcion.sval + " debe ser de tipo " + tipo_parametro_funcion +" y no de tipo " + tipo_parametro); 
	}
	
	private void chequeoS_typedef_existe(ParserVal typedef)
	{
		String variable_ambito = typedef.sval;
		while (variable_ambito.lastIndexOf('.') != -1)
		{	
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        while (iterator.hasNext()) 
	        {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (entry.getKey().compareTo(variable_ambito) == 0 && analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso") != null)
	            {//Se compara uso ya que la variable debe estar declarada para que tenga tipo
	            		try {
	            		if (analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("typedef") == 0)
	                		return;
	            		}
	            		catch(Exception e) {}
	            }
	        }
	        int ambito_index = variable_ambito.lastIndexOf('.');
			if (ambito_index > 0) {
				variable_ambito = variable_ambito.substring(0,ambito_index);
			}
		}
		System.out.println("error semantico: no existe un typedef de nombre " + typedef.sval); 
	}
