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

programa	: nombre_programa bloque_sentencias_declarativas BEGIN conjunto_sentencia_ejecutable END ';' {desapilar_ambito(); this.reglas.add("Sentencia START programa"); this.raiz= new Nodo("Programa", $1, $4);}
			| error ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": error en el programa"); }
;

nombre_programa	: ID {apilar_ambito($1); set_campo($1,"uso","programa"); $$= new Nodo($1);}
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

declaracionNuevoTipo	: TYPEDEF nombre_funcion_typedef '=' encabezado_funcion_typedef ';'  {this.reglas.add("Declaracion TYPEDEF");}
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

retorno		: '('expresion')' {$$= $2;}
			| expresion')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion"); $$= $1;}
			|'('expresion {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion");$$= $2;}
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

invocacion_funcion	: nombre_invocacion '(' factor ')' {chequeoS_parametro_funcion($1, $2); $$=new Nodo("invocacion funcion", $1, $3);}
//Esto es tanto para typedef como funcion!
;

nombre_invocacion : ID {chequeoS_funcion_no_declarada($1); $$=new Nodo ($1);}
;

asignacion	: operador_asignacion ':=' expresioncompuesta ';' {chequeoS_diferentes_tipos($1, $3); this.reglas.add("Asignacion"); $$= new Nodo($2, $1, $3);}
		  	| operador_asignacion expresioncompuesta ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ':=' esperado despues de ID"); this.reglas.add("Asignacion"); $$= new Nodo(":=", $1, $2)}		  
;

operador_asignacion	: ID {chequeoS_variable_no_declarada($1); chequeoS_operador_valido($1); $$=$1;} //Esto ya no permite que se pongan funciones para asignar
;

expresioncompuesta	: '('expresion')' {$$= $2;}
					| expresion {$$= $1;}
					| expresion')' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de expresion"); $$= $1;}
					|'('expresion {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de expresion"); $$= $2;}
;

expresion	: expresion '+' termino {chequeoS_diferentes_tipos($0, $1, false); $$= new Nodo($2, $1, $3);}
			| expresion '-' termino {chequeoS_diferentes_tipos($0, $1, false); $$= new Nodo($2, $1, $3);}
			| termino {$$= $1;}
;

termino		: termino '/' factor {chequeoS_diferentes_tipos($0, $1, false); $$= new Nodo($2, $1, $3);}
			| termino '*' factor {chequeoS_diferentes_tipos($0, $1, false); $$= new Nodo($2, $1, $3);}
			| factor {$$= $1;}
			| termino '/' invocacion_funcion {chequeoS_diferentes_tipos($0, $1, false); $$= new Nodo($2, $1, $3);}
			| termino '*' invocacion_funcion {chequeoS_diferentes_tipos($0, $1, false); $$= new Nodo($2, $1, $3);}
			| invocacion_funcion {$$= $1;}
;

factor		: ID {this.reglas.add("factor ID"); chequeoS_variable_no_declarada($1); $$=new Nodo($1);}
			| CTE {this.reglas.add("Factor CTE"); $$=new Nodo($1);}
			| '-' CTE {reverificar_cte_negativa($1); $$=new Nodo("-"+$1);}
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
			| bloque_sentencias_ejecutables {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": 'THEN' esperado"); $$= new Nodo("THEN", $1, null);}
;

cuerpo_else : ELSE bloque_sentencias_ejecutables {$$= new Nodo("ELSE", $2, null);}
;

condicion	: expresion {$$=new Nodo("condicion", $1, null);}
			| condicion operador_logico expresion {chequeoS_diferentes_tipos($0, $1, false); $$= $1.setHijos($1, $3);}
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

mensaje_pantalla	: PRINT '(' CADENA ')' ';' {this.reglas.add("clausula PRINT"); $$= new Nodo($1, $3, null);}
					| PRINT  CADENA ')' ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": '(' esperado antes de cadena"); this.reglas.add("clausula PRINT"); $$= new Nodo($1, $2, null);}
					| PRINT '(' CADENA  ';' {this.erroresSint.add("Error en la linea "+ analizadorLexico.contadorLineas + ": ')' esperado despues de cadena"); this.reglas.add("clausula PRINT"); $$= new Nodo($1, $3, null);}
;
	
sentencia_control_repeat	: REPEAT '(' declaracion_repeat ')' BEGIN conjunto_sentencias_repeat END ';'  {this.reglas.add("Sentencia Ejecutable REPEAT - Chequeo Semantico"); new Nodo("Repeat", $3, $5);}
;		

conjunto_sentencias_repeat	: BREAK ';' {$$= new Nodo("BREAK");}
							| sentencia_ejecutable {$$= new Nodo("S", $1, null);}
							| sentencia_ejecutable conjunto_sentencias_repeat {$$= new Nodo("S", $1, $2);}
;

declaracion_repeat :  asignacion_repeat ';' condicion_repeat ';' CTE {chequeoS_repeat_check_i($1);}
;

asignacion_repeat : variable_repeat '=' CTE { chequeoS_repeat_tipo_entero($1,"INT"); chequeoS_repeat_tipo_entero($2, "INT");}
;

variable_repeat : ID {chequeoS_variable_no_declarada($1); chequeoS_operador_valido($1);}
;

condicion_repeat	: ID operador_logico expresion {chequeoS_diferentes_tipos($0, $2, true); this.reglas.add("Condicion_Repeat"); chequeoS_repeat_set_i($2);}
;

%%
	public AnalizadorLexico analizadorLexico;
	public List<String> erroresSint;
	public List<String> reglas;
	public List<String> pendingTypeList;
	public String lastFuncType;
	public Nodo raiz;
	
	public ParserVal aux_i; //para mantener constancia del repeat
	public ParserVal aux_m; // para mantener constancia del repeat
	
	public ParserVal aux_i; //para mantener constancia del repeat
	public ParserVal aux_m; // para mantener constancia del repeat
	
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
		String var = getEntradaValidaTS(variable);
		
		if (var != null) 
		{
			if (isFuncion(var))
			{
				chequeoS_funcion_no_declarada(variable);
				return;
			}

			try {
				if (analizadorLexico.tabla_simbolos.get(var).get("uso").compareTo("variable") == 0)
				{
					return;
				}
				System.out.println("error semantico: variable " + variable.sval + " no declarada");
			} catch (Exception e) {}
		}
		System.out.println("error semantico: variable " + variable.sval + " no declarada");
		return;
	
	}
	
	private void chequeoS_redeclaracion_variable(ParserVal variable)
	{
		// buscar en la tabla de simbolos si esta (no hace falta por campo uso sino que no pertenezcan al mismo ambito y listo)
		// pero si hay que verificar que 'uso' no sea null porque sino, no esta declarada.
		
		String var = getEntradaValidaTS(variable);
		if (var != null)
		{
			try {
				if (analizadorLexico.tabla_simbolos.get(var).get("uso").compareTo("variable") == 0)
				{
					System.out.println("error semantico: variable " + variable.sval + " ya declarada");
					return;
				}
			} catch (Exception e) {}
		}
		return;
	}
	
	private void reverificar_cte_negativa (ParserVal variable)
	{
		System.out.println("var negativa: " + variable.sval);
		String new_variable = "-" + variable.sval;
		String var = getEntradaValidaTS(variable);
		String tipo = analizadorLexico.tabla_simbolos.get(var).get("tipo");
		boolean fuera_de_rango = false;
		if (tipo.compareTo("INT") == 0)
		{
			long aux= Long.parseLong(new_variable);
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
			
			System.out.println(valor);
			double lim1= Math.pow(1.17549435, -38); // 1.17549435S-38
			double lim2= Math.pow(3.40282347, 38);// 3.40282347S+38
			double lim3= Math.pow(-3.40282347, 38);//-3.40282347S+38 
			double lim4= Math.pow(-1.17549435, -38);//-1.17549435S-38
			if(valor<lim1 || valor>lim2) {
				if(valor<lim3 || valor>lim4) {
					if(valor!=0) {
						fuera_de_rango = true;
							System.out.println("fuera de rangaso");
				}}
			}else if(valor<lim3 || valor>lim4) {
				if(valor<lim1 || valor>lim2) {
					if(valor!=0) {
						fuera_de_rango = true;
					}
				}
			}
		}
		
		if (!fuera_de_rango)
		{
			analizadorLexico.tabla_simbolos.put(new_variable, new HashMap<String,String>());
			analizadorLexico.tabla_simbolos.get(new_variable).put("tipo", tipo);
			analizadorLexico.tabla_simbolos.get(new_variable).put("uso", "constante");
		}
		if (fuera_de_rango)
		{
			System.out.println("error semantico: variable negativa fuera de rango");
		}
		variable.sval = new_variable;
	}
	
	private void chequeoS_funcion_no_declarada (ParserVal funcion)
	{
		String func = getEntradaValidaTS(funcion);
		
		if (func != null) 
		{
			boolean isTypeDef = isTypeDef(getTipoVariable(funcion.sval));
			if (isTypeDef)
			{
				funcion.sval = getTipoVariable(funcion.sval);
				chequeoS_typedef_existe(funcion);
				return;
			}
			try {
				if (analizadorLexico.tabla_simbolos.get(func).get("uso").compareTo("funcion") == 0)
				{
					return;
				}
				System.out.println("error semantico: funcion " + funcion.sval + " no declarada");
			} catch (Exception e) {}
		}
		System.out.println("error semantico: funcion " + funcion.sval + " no declarada");
		return;
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

	private void chequeoS_diferentes_tipos(ParserVal id1, ParserVal id2, boolean condicion_repeat)
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
		
		if (condicion_repeat == true) // CONDICION REPEAT DEBEN SER DE TIPO ENTERO
		{
			if (tipo_1.compareTo("INT") != 0 || tipo_2.compareTo("INT") != 0)
			{
				System.out.println("error semantico: en una sentencia repeat, los datos de condicion deben ser de tipo entero (INT)");
				return;
			}
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
	
	private boolean isTypeDef(String func)
	{
		try {
			if (analizadorLexico.tabla_simbolos.get(func).get("tipo") != null)
				return true;
		} catch (Exception e) {}
		return false;
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
	
	private void chequeoS_parametro_funcion(ParserVal funcion, ParserVal parametro)
	{
		String func = getEntradaValidaTS(funcion);
		String par = getEntradaValidaTS(parametro);
		
		try {
			String tipo_par_func = analizadorLexico.tabla_simbolos.get(func).get("tipo_parametro");
			String tipo_par = analizadorLexico.tabla_simbolos.get(par).get("tipo");
			if (isTypeDef(tipo_par_func)) {
				tipo_par_func = analizadorLexico.tabla_simbolos.get(tipo_par_func).get("tipo");
			}
			
			if (tipo_par_func.compareTo(tipo_par) == 0 && tipo_par_func != null)
			{
				return;
			}
			System.out.println("error semantico: el parametro de " + funcion.sval + " debe ser de tipo " + tipo_par_func +" y no de tipo " + tipo_par); 
		} catch (Exception e) { }
		return;
		
	}
	
	private void chequeoS_typedef_existe(ParserVal var)
	{
		String typedef = getEntradaValidaTS(var);
		try
		{
			String uso_typedef = analizadorLexico.tabla_simbolos.get(typedef).get("uso");
			if (uso_typedef.compareTo("typedef") == 0)
				return;
		} catch (Exception e){}
		System.out.println("error semantico: no existe un typedef de nombre " + var.sval); 
		return;
	}
	
	private void chequeoS_repeat_tipo_entero(ParserVal var, String tipo)
	{
		String variable = getEntradaValidaTS(var);
		try
		{
			String tipo_variable = analizadorLexico.tabla_simbolos.get(variable).get("tipo");
			if (tipo_variable.compareTo(tipo) == 0)
				return;
		} catch (Exception e){}
		System.out.println("error semantico: la variable o constante deberia ser de tipo entero");
		return;
		
	}

	private void chequeoS_operador_valido(ParserVal op)
	{
		String operador = getEntradaValidaTS(op);
		try
		{
			String uso_operador = analizadorLexico.tabla_simbolos.get(operador).get("uso");
			if (uso_operador.compareTo("variable") == 0)
				return;
		} catch (Exception e){}
		System.out.println("error semantico: operador de asignacion invalido, " + op.sval + " no es una variable.");
		return;
	}
	
	private String getEntradaValidaTS(ParserVal entrada)
	{
		//Esta funcion devuelve una referencia a la tabla de simbolos valida (de existir) de una entrada (en el alcance de su ambito), tanto ID como CTE.
		String iterador_entrada = entrada.sval;
		boolean no_quedan_ambitos = false;
		while (!no_quedan_ambitos)
		{
			try {
				String entrada_actual_uso = analizadorLexico.tabla_simbolos.get(iterador_entrada).get("uso");
				if (entrada_actual_uso != null)
					{return iterador_entrada;}
			}
			catch (Exception e) { }
			
			int ambito_index = iterador_entrada.lastIndexOf('.');
			if (ambito_index > 0) {
				iterador_entrada = iterador_entrada.substring(0,ambito_index);
			} else {no_quedan_ambitos = true;}
		}
		return null;
	}
	
	private void chequeoS_repeat_set_i (ParserVal i)
	{
		aux_i = i;
	}
	
	private void chequeoS_repeat_check_i (ParserVal i)
	{
		if (aux_i.sval.compareTo(i.sval) != 0)
		{
			System.out.println("error semantico: en una sentencia repeat se debe comparar usando la variable de control");
		}
		return;
	}
	
	public void imprimirArbol(Nodo nodo){
        if(nodo == null)
            return;
        
        System.out.println(nodo.nombre + " ");
        System.out.print("    ");
        imprimirArbol(nodo.izq);
        System.out.print("    ");
        imprimirArbol(nodo.der);
	}