package compiTPE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class CodeGenerator {

	Parser analizador;
	Nodo raiz; // acceso al codigo intermedio
	String programa_filename;
	String assembler_code;
	
	private LinkedList<Integer> pilaLabels;
	private int contLabels;
	private LinkedList<String> auxvars;
	private ArrayList<ParserVal> listFunciones;
	
	public CodeGenerator(Nodo raizArbol, Parser analizadorSintactico)
	{
		this.raiz = raizArbol;
		this.analizador = analizadorSintactico;
		this.pilaLabels = new LinkedList<Integer>();
		this.contLabels = 0;
		this.listFunciones = analizadorSintactico.listaFunc;
		this.auxvars = new LinkedList<String>();
	}
	
	public void run() throws IOException 
	{	
		this.assembler_code = ".386\r\n"
				+ ".MODEL  Flat,StdCall\r\n"
				+ "OPTION  CaseMap:None\r\n"
				+ "include \\masm32\\include\\windows.inc\r\n"
				+ "include \\masm32\\include\\kernel32.inc\r\n"
				+ "include \\masm32\\include\\user32.inc\r\n"
				+ "includelib \\masm32\\lib\\kernel32.lib\r\n"
				+ "includelib \\masm32\\lib\\user32.lib\n.STACK 200h\n"; //REVISAR cambiar tama;o y stack
		declareData();
		compile();		
		setAux(); //Inserta todas la auxiliares necesarias usadas en el programa
		save();
		
	}
	
	private String getNextAux(String tipo)
	{
		int number = auxvars.size()+1;
		analizador.analizadorLexico.tabla_simbolos.put("@aux"+number, new HashMap<String,String>());
		analizador.analizadorLexico.tabla_simbolos.get("@aux"+number).put("tipo", tipo);
		analizador.analizadorLexico.tabla_simbolos.get("@aux"+number).put("uso", "auxiliar");
		if (tipo.compareTo("INT") == 0){
			auxvars.add("@aux"+number+" DW ?\n");
		} else if (tipo.compareTo("SINGLE") == 0) {
			auxvars.add("@aux"+number+" DD ?\n");
		}else if (tipo.compareTo("cadena") == 0) {
			auxvars.add("@aux"+number+" DB \"empty\", 0\n");
		}
		
		return ("aux"+number);
	}
	
	private void setAux()
	{
		String auxs = "";
		for (int i = 0; i < auxvars.size(); i++) {
			auxs = auxs + auxvars.get(i);
		}
		this.assembler_code = this.assembler_code.replaceAll(";FLAG PARA DECLARAR AUXILIARES", auxs);
	}
	
	private void declareData()
    {
        this.assembler_code =this.assembler_code+ ".DATA\n";
        //recorrer la tabla de simbolos y declarar todo lo que haga falta
        for (Map.Entry<String, HashMap<String,String>> entry : analizador.analizadorLexico.tabla_simbolos.entrySet()) {
            try {
                if (analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("variable") == 0 || 
                    analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("parametro") == 0 )
                {
                    String tipo = analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("tipo");
                    if (tipo.compareTo("INT") == 0)
                    {
                        this.assembler_code =this.assembler_code+ entry.getKey().replace(".", "@") + " DW ?\n";
                    } else if (tipo.compareTo("SINGLE") == 0) {
                        this.assembler_code =this.assembler_code+ entry.getKey().replace(".", "@") + " DD ?\n";
                    } else { //Es TYPEDEF
                        this.assembler_code =this.assembler_code+ entry.getKey().replace(".", "@") + " DD ?\n";
                    } 
                }
                if (analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("cadena") == 0) //una cadena
                {
                    this.assembler_code =this.assembler_code+ entry.getKey().replace(" ","") + " db \"" + entry.getKey().replace(" ", "") + "\", 0 \n";
                }
                if (analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("programa") == 0) //nombre del programa / label MAIN
                {
                    this.programa_filename = entry.getKey();
                }

            } catch (Exception e) {} 
}
        this.assembler_code = this.assembler_code + "recfuncion db \"none\", 0\n"; //PARA CHEQUEO RECURSION MUTUA

        this.assembler_code =this.assembler_code+ ";FLAG PARA DECLARAR AUXILIARES\n";
    }
	
	
	private void genError() throws IOException {
		this.assembler_code =this.assembler_code+ "EXIT:\n invoke ExitProcess, 0\n";
		this.assembler_code =this.assembler_code+ "DIV_CERO:\n invoke MessageBox, NULL, addr DIV_CERO, NULL, MB_OK\n JMP EXIT\n";
		this.assembler_code =this.assembler_code+ "OVERFLOW_ERROR:\n invoke MessageBox, NULL, addr OVERFLOW_ERROR, NULL, MB_OK\n JMP EXIT\n";
		this.assembler_code =this.assembler_code+ "ERROR_REC_MUTUA:\n invoke MessageBox, NULL, addr ERROR_REC_MUTUA, NULL, MB_OK\n JMP EXIT\n";


	}
	private void setADD(Nodo nodo) throws IOException {
		
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}

		if (!izq.esHoja()) {
			generateCode(izq);
		}
		if(!der.esHoja()) {
			generateCode(der);
		}
		
		String tipoOP = analizador.analizadorLexico.tabla_simbolos.get(analizador.getEntradaValidaTS(nodo.getTipoHijoDer(nodo))).get("tipo"); //PENDIENTE
 //PENDIENTE: agregar campo a los nodos que mantenga los tipos de las variables, para cuando se reemplacen los nodos por auxiliares y tal.
		//PENDIENTE: Podria ser sacar el tipo del nodo hoja del subarbol derecho antes de generar codigo que deberia corresponder a una entrada de la tabla de simbolos con tipo.
		//String tipoOP = "INT";
		
		// CHEQUEO OVERFLOW
		if (tipoOP.compareTo("INT") == 0) {
			String registroAux = "BX"; //16	 bits
			assembler_code = this.assembler_code+ "MOV " + registroAux + ", " + der.nombre.replace(".", "@") + "\n";
			assembler_code = this.assembler_code+ "CMP " + registroAux + ", "  + izq.nombre.replace(".", "@") + "\n";
			registroAux = "";
		} 	
		this.assembler_code =this.assembler_code+ "JO OVERFLOW_ERROR\n";
		
		/*else if (tipoOP.compareTo("SINGLE") == 0) { NO HACIA FALTA CHEQUEO DE OVERFLOW SINGLE
			assembler_code = this.assembler_code+ "FLDZ \n";
			assemblera_code = this.assembler_code+ "FCOM " + "_"+der.nombre.replace(".", "@").replace(".", "_");
		}*/
		
		
		if (tipoOP.compareTo("INT") == 0) {

			if (izq.esRegistro() && der.esRegistro()) {//Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code =this.assembler_code+ "ADD " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";
				der.nombre = ""; //vacio el registro ya que no lo necesito (para futuras comparaciones)
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
				nodo.nombre = aux;
			}
			else {
				if (izq.esRegistro()) {
					assembler_code = this.assembler_code+ "ADD " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";
					nodo.nombre = izq.nombre.replace(".", "@");
					
					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
					nodo.nombre = aux;
				}
				else {
					if (der.esRegistro()){
						String aux = "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + der.nombre.replace(".", "@") + "\n";
						assembler_code = this.assembler_code+ "MOV " + der.nombre.replace(".", "@") + ", " + izq.nombre.replace(".", "@") + "\n"; // der.nombre.replace(".", "@") es REG
						assembler_code = this.assembler_code+ "ADD " + izq.nombre.replace(".", "@") + ", " + aux + "\n";
						
						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;
					}
					else {
						assembler_code = this.assembler_code+ "MOV " + "AX, " + izq.nombre.replace(".", "@") + "\n";
						izq.nombre = "AX";
						assembler_code = this.assembler_code+ "ADD " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (der.nombre.contains(".") && izq.nombre.contains(".")) {
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code =this.assembler_code+"FLD " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FADD " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (der.nombre.contains(".")) { //DER ES EXPONENCIAL
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code =this.assembler_code+ "FLD " + izq.nombre.replace(".", "@") + "\n";
	                    this.assembler_code =this.assembler_code+ "FADD " + "_" + der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (izq.nombre.contains(".")) {
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code =this.assembler_code+ "FLD " + "_" + izq.nombre.replace(".", "_")+ "\n";
	    	                this.assembler_code =this.assembler_code+ "FADD " +  der.nombre.replace(".", "@") + "\n";
	    	                this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else {
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code =this.assembler_code+"FLD " +izq.nombre.replace(".", "@")+ "\n";
	                        this.assembler_code =this.assembler_code+"FADD "+ der.nombre.replace(".", "@")+ "\n";
	                        this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	    }

	
	private void setSUB(Nodo nodo) throws IOException {
		
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		if (!izq.esHoja()) {
			generateCode(izq);
		}
		if(!der.esHoja()) {
			generateCode(der);
		}
		String tipoOP = "INT"; //agregar campo a los nodos que mantenga los tipos de las variables, para cuando se reemplacen los nodos por auxiliares y tal.
		
		if (tipoOP.compareTo("INT") == 0) {
			if (izq.esRegistro() && der.esRegistro()) { //Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code =this.assembler_code+ "SUB " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";
				izq.nombre = "";
				der.nombre = ""; //vacio el registro ya que no lo necesito (para futuras comparaciones)
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
				nodo.nombre = aux;
			}
			else {
				if (izq.esRegistro()) {
					assembler_code = this.assembler_code+ "SUB " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";

					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
					nodo.nombre = aux;
				}
				else {
					if (der.esRegistro()){
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + der.nombre.replace(".", "@") + "\n";
						assembler_code = this.assembler_code+ "MOV " + der.nombre.replace(".", "@") + ", " + izq.nombre.replace(".", "@") + "\n";
						assembler_code = this.assembler_code+ "SUB " + izq.nombre.replace(".", "@") + ", " + aux + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;
					} else {
						assembler_code = this.assembler_code+ "MOV " + "AX, " + izq.nombre.replace(".", "@") + "\n";
						izq.nombre= "AX";
						assembler_code = this.assembler_code+ "SUB " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";
						
						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (der.nombre.contains(".") && izq.nombre.contains(".")) {
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code =this.assembler_code+"FLD " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FSUB " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (der.nombre.contains(".")) {
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code =this.assembler_code+ "FLD " + izq.nombre.replace(".", "@") + "\n";
	                    this.assembler_code =this.assembler_code+ "FSUB " + "_" + der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (izq.nombre.contains(".")) {
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code =this.assembler_code+ "FLD " + "_" + izq.nombre.replace(".", "_") + "\n";
	    	                this.assembler_code =this.assembler_code+ "FSUB " +  der.nombre.replace(".", "@") + "\n";
	    	                this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else {
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code =this.assembler_code+"FLD " +izq.nombre.replace(".", "@") + "\n";
	                        this.assembler_code =this.assembler_code+"FSUB " + der.nombre.replace(".", "@") + "\n";
	                        this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	}
	
	private void setIMUL(Nodo nodo) throws IOException {
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		if (!izq.esHoja()) {
			generateCode(izq);
		}
		if(!der.esHoja()) {
			generateCode(der);
		}
		String tipoOP = analizador.analizadorLexico.tabla_simbolos.get(analizador.getEntradaValidaTS(nodo.getTipoHijoDer(nodo))).get("tipo"); //PENDIENTE
 //PENDIENTE: agregar campo a los nodos que mantenga los tipos de las variables, para cuando se reemplacen los nodos por auxiliares y tal.
		//String tipoOP = "INT";
		if (tipoOP.compareTo("INT") == 0) {
			if (izq.esRegistro() && der.esRegistro()) { //Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code =this.assembler_code+ "IMUL " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
				nodo.nombre = aux;
			}
			else {
				if (izq.esRegistro()) {
					assembler_code = this.assembler_code+ "IMUL " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";
					nodo.nombre = izq.nombre.replace(".", "@");
					
					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
					nodo.nombre = aux;
				}
				else {
					if (der.esRegistro()){
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + der.nombre.replace(".", "@") + "\n";
						assembler_code = this.assembler_code+ "MOV " + der.nombre.replace(".", "@") + ", " + izq.nombre.replace(".", "@") + "\n";
						assembler_code = this.assembler_code+ "IMUL " + izq.nombre.replace(".", "@") + ", " + aux + "\n";
						
						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;	
					}else {
						assembler_code = this.assembler_code+ "MOV " + "AX, " + izq.nombre.replace(".", "@") + "\n";
						izq.nombre = "AX";
						assembler_code = this.assembler_code+ "IMUL " + izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (der.nombre.contains(".") && izq.nombre.contains(".")) {
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code =this.assembler_code+"FLD " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FIMUL " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (der.nombre.contains(".")) {
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code =this.assembler_code+ "FLD " + izq.nombre.replace(".", "@") + "\n";
	                    this.assembler_code =this.assembler_code+ "FIMUL " + "_" + der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (izq.nombre.contains(".")) {
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code =this.assembler_code+ "FLD " + "_" + izq.nombre.replace(".", "_")+ "\n";
	    	                this.assembler_code =this.assembler_code+ "FIMUL" + der.nombre.replace(".", "@") + "\n";
	    	                this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else {
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code =this.assembler_code+"FLD " +izq.nombre.replace(".", "@")+ "\n";
	                        this.assembler_code =this.assembler_code+"FIMUL "+ der.nombre.replace(".", "@")+ "\n";
	                        this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	}
	
	private void setIDIV(Nodo nodo) throws IOException {
		
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		if (!izq.esHoja()) {
			generateCode(izq);
		}
		if(!der.esHoja()) {
			generateCode(der);
		}
		String tipoOP = analizador.analizadorLexico.tabla_simbolos.get(analizador.getEntradaValidaTS(nodo.getTipoHijoDer(nodo))).get("tipo"); 
		//String tipoOP = "INT";
		//CHEQUEO IDIVISION POR CERO:
		if (tipoOP.compareTo("INT") == 0) {
			String registroAux = "BX"; //16	 bits
			assembler_code = this.assembler_code+ "MOV " + registroAux + ", " + der.nombre.replace(".", "@") + "\n";
			assembler_code = this.assembler_code+ "CMP " + registroAux + ", "  + "0\n";
			registroAux = "";
		} else if (tipoOP.compareTo("SINGLE") == 0) {
			assembler_code = this.assembler_code+ "FLDZ \n";
			if (der.nombre.contains("."))
			{
				assembler_code = this.assembler_code+ "FCOM " + "_"+der.nombre.replace(".", "_");
			} else
				assembler_code = this.assembler_code+ "FCOM " + "_"+der.nombre.replace(".", "@");
		}
		this.assembler_code =this.assembler_code+ "JE DIV_CERO\n";
		
		if (tipoOP.compareTo("INT") == 0) {
			if (izq.esRegistro() && der.esRegistro()) { //Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code = this.assembler_code + "MOV AX," + izq.nombre.replace(".", "@" + "\n");
				izq.nombre = "AX";
				assembler_code =this.assembler_code+ "IDIV "+ der.nombre.replace(".", "@") + "\n";
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
				nodo.nombre = aux;
			}
			else {
				if (izq.esRegistro()) {
					assembler_code = this.assembler_code + "MOV AX," + izq.nombre.replace(".", "@" + "\n");
					izq.nombre = "AX";
					assembler_code = this.assembler_code+ "IDIV " + der.nombre.replace(".", "@") + "\n";
					
					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
					nodo.nombre = aux;
				}
				else {
					if (der.esRegistro()){
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + der.nombre.replace(".", "@") + "\n";
						assembler_code = this.assembler_code+ "MOV " + der.nombre.replace(".", "@") + ", " + izq.nombre.replace(".", "@") + "\n";
						assembler_code = this.assembler_code + "MOV AX," + izq.nombre.replace(".", "@" + "\n");
						izq.nombre = "AX";
						assembler_code = this.assembler_code+ "IDIV " + aux + "\n";
						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;
					}else { //izq es una variable o un inmediato. No puede ser inmediato!
						if (analizador.analizadorLexico.tabla_simbolos.get(der.nombre).get("uso").compareTo("constante") == 0) {
							String auxINM = "@" + getNextAux(tipoOP);
							assembler_code = this.assembler_code + "MOV "+auxINM+", "+der.nombre+"\n";
							der.nombre = auxINM;
						}
						
						assembler_code = this.assembler_code+ "MOV " + "AX, " + izq.nombre.replace(".", "@") + "\n";
						izq.nombre = "AX";
						assembler_code = this.assembler_code+ "IDIV "+der.nombre.replace(".", "@") + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (der.nombre.contains(".") && izq.nombre.contains(".")) { //Los DOS son CTE
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code =this.assembler_code+"FLD " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FIDIV " + "_" + izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (der.nombre.contains(".")) { //DER es CTE
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code =this.assembler_code+ "FLD " + izq.nombre.replace(".", "@") + "\n";
	                    this.assembler_code =this.assembler_code+ "FIDIV " + "_" + der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (izq.nombre.contains(".")) { //IZQ es CTE
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code =this.assembler_code+ "FLD " + "_" + izq.nombre.replace(".", "_")+ "\n";
	    	                this.assembler_code =this.assembler_code+ "FIDIV " + der.nombre.replace(".", "@") + "\n";
	    	                this.assembler_code =this.assembler_code+ "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else { //Las dos son VARIABLES
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code =this.assembler_code+"FLD " +izq.nombre.replace(".", "@")+ "\n";
	                        this.assembler_code =this.assembler_code+"FIDIV " + der.nombre.replace(".", "@")+ "\n";
	                        this.assembler_code =this.assembler_code+"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	}
	
	private void setASG(Nodo nodo) throws IOException {
		
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		if(!der.esHoja()) {
			generateCode(der);
		}
		
		String tipoOP = analizador.analizadorLexico.tabla_simbolos.get(analizador.getEntradaValidaTS(nodo.getTipoHijoDer(nodo))).get("tipo"); //PENDIENTE
//PENDIENTE
		//String tipoOP = "INT";
		if (tipoOP.compareTo("INT") == 0) {
			this.assembler_code =this.assembler_code+ "MOV AX, " + der.nombre.replace(".", "@") + "\n"; 
			this.assembler_code =this.assembler_code+ "MOV " + izq.nombre.replace(".", "@") + ", AX\n"; 

		}
		else if (tipoOP.compareTo("SINGLE") == 0) { //IZQ siempre es una variable
			if (der.nombre.contains(".")) { //tiene parte exponencial
				this.assembler_code =this.assembler_code+ "FLD _" + der.nombre.replace(".","_") + "\n"; 
				this.assembler_code =this.assembler_code+ "FSTP " + izq.nombre.replace(".", "@") + "\n"; 
			}
			else {
				this.assembler_code =this.assembler_code+ "FLD " + der.nombre.replace(".", "@") + "\n"; 
				this.assembler_code =this.assembler_code+ "FSTP " + izq.nombre.replace(".", "@") + "\n"; 	
			}
		} else if (analizador.isTypeDef(analizador.getTipoVariable(izq.nombre.replace(".", "@")))){
			String auxreg32 = "EAX";
			this.assembler_code =this.assembler_code+ "MOV " + auxreg32 + ", " + der.nombre.replace(".", "@") + "\n"; 
			this.assembler_code =this.assembler_code+ "MOV " + izq.nombre.replace(".", "@") + ", " + auxreg32 + "\n"; 

		}
	}
	
	private void setPRINT(Nodo nodo, boolean precondicion) throws IOException
	{
		Nodo izq = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo

		} catch (Exception e) {}
		if (precondicion)
		{
			this.assembler_code =this.assembler_code+ "invoke MessageBox, NULL, addr " + nodo.nombre.replace(" ", "") + ", addr "+nodo.nombre.replace(" ", "")+", MB_OK\n";	
		}
		else {
			this.assembler_code =this.assembler_code+ "invoke MessageBox, NULL, addr " + izq.nombre.replace(" ", "") + ", addr "+izq.nombre.replace(" ", "")+", MB_OK\n";	

		}

	}
	
	private void setIF(Nodo nodo) throws IOException { 
		Nodo izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
		Nodo der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo

		this.setCMP(izq);
		Nodo nodo_else = null;
		
		try {
			nodo_else = (Nodo)((Nodo)der.der.obj).izq.obj;
		} catch (Exception e) {}
		
		 if (nodo_else != null) {
			if (nodo_else.nombre.compareTo("ELSE") == 0){
				this.setCuerpoIfElse((Nodo)((Nodo)der.izq.obj).izq.obj); //der.izq es THEn, der.izq.izq es S
				System.out.println(nodo_else);
				this.setElse(nodo_else); //der.der es ELSE, der.der.izq es S
			}
		}
		else {
			this.setCuerpoIf((Nodo)((Nodo)der.izq.obj).izq.obj); // der.izq es THEN, der.izq.izq es S
		}
	}
	
	private void setCuerpoIf(Nodo nodo) throws IOException { //SIN ELSE
		generateCode(nodo);
		nodo.nombre = ""; //Libero su valor por si ocupa un REG
		this.assembler_code =this.assembler_code+ "Label" + this.pilaLabels.pollLast() + ": \n"; //desapilo el label generado en CMP y lo inserto.
	}
	
	private void setCuerpoIfElse(Nodo nodo) throws IOException { 
		generateCode(nodo);
		nodo.nombre = ""; //Libero su valor por si ocupa un REG
		contLabels++;
		this.pilaLabels.addLast(this.contLabels);	
		this.assembler_code =this.assembler_code+ "JMP Label" + contLabels + "\n"; //Salto para omitir el ELSE
	}
	
	private void setElse(Nodo nodo) throws IOException
	{
		String elseLabel = "Label" + this.pilaLabels.pollLast() +":\n"; //Desapilo y guardo el salto hacia fuera del else
		this.assembler_code =this.assembler_code+ "Label" + this.pilaLabels.pollLast()+":\n"; //Desapilo el salto que tenia si CMP era falso y genero el ELSE
		this.generateCode(nodo); //genero el codigo dentro de ELSE
		this.assembler_code =this.assembler_code+ elseLabel; 
	}
	
	private void setCMP(Nodo nodo) throws IOException {
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		if (!izq.esHoja()) {
			generateCode(izq);
		}
		if(!der.esHoja()) {
			generateCode(der);
		}
		this.contLabels++;
		this.pilaLabels.addLast(this.contLabels);
		String registro = "AX";
		String tipoOP = analizador.analizadorLexico.tabla_simbolos.get(analizador.getEntradaValidaTS(nodo.getTipoHijoDer(nodo))).get("tipo");
		if (tipoOP.compareTo("INT") == 0) {
			if (der.esRegistro()){ //Como guardare la comparacion en un registro a la izquierda, si mi derecha es un registro debo liberarlo para no pisarlo
				String aux="@"+ getNextAux(tipoOP);
				assembler_code = this.assembler_code+ "MOV " + aux + ", " + der.nombre.replace(".", "@") + "\n";
				der.nombre = aux;
			}
			this.assembler_code =this.assembler_code+ "MOV " + registro + ", " + izq.nombre.replace(".", "@") + "\n";
			izq.nombre = registro;
			switch (nodo.nombre) {
				case "<": {
					this.assembler_code = this.assembler_code + "CMP " +  izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n" + "JAE Label"+contLabels + "\n";
					break;
				}
				case "<=": {
					this.assembler_code = this.assembler_code + "CMP " +  izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n" + "JA Label"+contLabels + "\n";
					break;
				}
				case ">": {
					this.assembler_code = "CMP " +  izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n" + "JLE Label"+contLabels + "\n";
					break;
				}
				case ">=": {
					this.assembler_code = "CMP " +  izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n" + "JL Label"+contLabels + "\n";
					break;
				}
				case "<>": {
					this.assembler_code = "CMP " +  izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n" + "JE Label"+contLabels + "\n";
					break;
				}
				case "==": {
					this.assembler_code = "CMP " +  izq.nombre.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n" + "JNE Label"+contLabels + "\n";
					break;
				}
			}
		}
		if (tipoOP.compareTo("SINGLE") == 0) {
			switch (nodo.nombre) {
				case "<": {
					if (der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP _"+der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JL Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP "+der.nombre.replace(".", "@")+"\n FSTSW AX\n SAHF \n JL Label" +contLabels+"\n";
					}
					break;
				}
				case "<=": {
					if (der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP _"+der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JLE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP "+der.nombre.replace(".", "@")+"\n FSTSW AX\n SAHF \n JLE Label" +contLabels+"\n";
					}
					break;
				}
				case ">": {
					if (der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP _"+der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JG Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP "+der.nombre.replace(".", "@")+"\n FSTSW AX\n SAHF \n JG Label" +contLabels+"\n";
					}
					break;
				}
				case ">=": {
					if (der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP _"+der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JGE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP "+der.nombre.replace(".", "@")+"\n FSTSW AX\n SAHF \n JGE Label" +contLabels+"\n";
					}
					break;
				}
				case "!=": {
					if (der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP _"+der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JNE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP "+der.nombre.replace(".", "@")+"\n FSTSW AX\n SAHF \n JNE Label" +contLabels+"\n";
					}
					break;
				}
				case "==": {
					if (der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP _"+der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+izq.nombre.replace(".", "@")+"\n FCOMP "+der.nombre.replace(".", "@")+"\n FSTSW AX\n SAHF \n JE Label" +contLabels+"\n";
					}
					break;
				}
			}
		}
	}
	

	private void setSENT(Nodo nodo) throws IOException //Sentencia (subarbol) cualquiera
	{
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		if (izq != null) {
			this.generateCode(izq);
		}
		if (der != null) {
			this.generateCode(der);
		}
	}
	
	private String getParametro(String funcion) {
		//obtener LA variable de TIPO PARAMETRO que sea del formato ???.(ambito).funcion
		String ambito = funcion.substring(funcion.indexOf(".")) + "." + funcion.substring(0,funcion.indexOf("."));
		
		Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizador.analizadorLexico.tabla_simbolos.entrySet().iterator();
        while (iterator.hasNext()) 
        {
            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
            if (entry.getKey().endsWith(ambito)) //tiene que terminar con el ambito, ya que si solo lo contiene podriamos estar accediendo a una subfuncion con el ambito mas largo  
            {
            	if (analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("parametro") == 0)
                	return entry.getKey();
            }
        }
        return null; //nunca deberia llegar aca
	}
	

	
	private void setINV(Nodo nodo) throws IOException //INC
	{
		
		Nodo izq = null;
		Nodo der = null;
		Nodo nodo_expresion = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
			nodo_expresion =(Nodo)der.izq.obj;
		} catch (Exception e) {}
		
		//PENDIENTE: Revisar
		//Chequeo REC MUTUA
		this.assembler_code = assembler_code + "CMP recfuncion, \"" + izq.nombre.replace(".", "@")+ "\" \n";
		this.assembler_code = assembler_code + "JE ERROR_REC_MUTUA\n";
		this.assembler_code = assembler_code + "MOV recfuncion, \"" + izq.nombre.replace(".", "@")+"\" \n";
		
		if (nodo_expresion != null) { //Es una expresion
			this.generateCode(der);
		}
		

		//parametro: Buscamos getParametro asociado al nombre de la funcion y le asignamos el valor del hijo derecho
		String funcion = izq.nombre.replace(".", "@");
		String parametro = getParametro(izq.nombre);
		this.assembler_code =this.assembler_code+ "MOV " + parametro.replace(".", "@") + ", " + der.nombre.replace(".", "@") + "\n";
		if (analizador.isTypeDef(analizador.getTipoVariable(funcion))) {
			//funcion realmente es la variable typedef almacenando la direccion de memoria de la funcion, al ponerla en [] funciona como puntero a la func
			this.assembler_code =this.assembler_code+ "CALL [" + funcion + "]\n";
		} else {
			//SI ES FUNC NORMAL directamente salto a la label habiendo asignado el parametro previamente
			this.assembler_code =this.assembler_code+ "CALL " + funcion + "\n";
		}
		
		//CHEQUEO REC MUTUA
		this.assembler_code = assembler_code + "MOV recfuncion, \"empty\" \n"; //"DESAPILAR" FUNCION ACTUAL
		
		
		nodo.nombre = "@aux"+this.auxvars.size(); //resultado de la invocacion, es decir el RETURN.
	}
	
	private void setREPEAT(Nodo nodo)throws IOException
	{
		// PENDIENTE
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		contLabels++;
		this.pilaLabels.addLast(this.contLabels);	
		this.assembler_code =this.assembler_code+ "Label" + contLabels + ":\n"; 
		Nodo varControl =  (Nodo)((Nodo)((Nodo)izq.izq.obj).izq.obj).izq.obj;
		this.generateCode(izq); //genero codigo de la condicion
		
		String cmpLabel= "Label" + this.pilaLabels.pollLast();
		String repeatLabel = "Label" + this.pilaLabels.pollLast() ; //Desapilo y guardo el salto el inicio del repeat
		//agregar label en caso que no se cumpla la condicion

		
		this.generateCode(der);
		
		
		Nodo aumento= (Nodo)((Nodo)izq.der.obj).der.obj;
		this.assembler_code = this.assembler_code + "ADD " + varControl.nombre.replace(".", "@") +", " + aumento.nombre + "\n";
		//primero label con jump al repeat y despues label en caso que no se cumpla mas la sentencia del repeat
		this.assembler_code =this.assembler_code+ "JMP " + repeatLabel + "\n"; //PENDIENTE agregar el tipo de jump
		this.assembler_code =this.assembler_code+ cmpLabel + ":\n"; 

	}
	
	private void setRETURN(Nodo nodo) throws IOException
	{
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		String tipoOP = analizador.analizadorLexico.tabla_simbolos.get(analizador.getEntradaValidaTS(nodo.getTipoHijoDer(izq))).get("tipo"); //PENDIENTE
		this.generateCode(izq);
		//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
		String aux= "@"+ getNextAux(tipoOP);
		assembler_code = this.assembler_code+ "MOV " + aux + ", " + izq.nombre.replace(".", "@") + "\n";
		nodo.nombre = aux;
	}
	
	private void setCondicion(Nodo nodo) throws IOException {
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		this.generateCode(izq);
		nodo.nombre = izq.nombre.replace(".", "@");
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void setDeclaracionRepeat(Nodo nodo) throws IOException {
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		this.generateCode(izq);
		this.generateCode(der);
		
	}
	
	private void setCondicionRepeat(Nodo nodo) throws IOException {
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		this.setCMP(izq);
		
	}
	
	private void setAsignacionRepeat(Nodo nodo) throws IOException {
		Nodo izq = null;
		try {
			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		
		this.generateCode(izq);
	}



	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void generateCode(Nodo nodo) throws IOException
	{
		switch(nodo.nombre){
			case "+": {setADD(nodo); break;}
			case "-": {setSUB(nodo); break;}
			case "/": {setIDIV(nodo); break;}
			case "*": {setIMUL(nodo); break;}
			case ":=": {setASG(nodo); break;}
			case "invocacion funcion": {setINV(nodo); break;}
			case "S": {setSENT(nodo); break;}
			case "IF": {setIF(nodo); break;}
			case "PRINT": {setPRINT(nodo,false); break;}
			case "Repeat": {setREPEAT(nodo); break;}
			case "RETURN": {setRETURN(nodo); break;}
			case "condicion": {setCondicion(nodo); break;}
			case "declaracion_repeat": {setDeclaracionRepeat(nodo); break;}
			case "condicion repeat": {setCondicionRepeat(nodo); break;}
			case "asignacion_repeat": {setAsignacionRepeat(nodo); break;}
		}
	}
	
	private void compile() throws IOException
	{
		this.assembler_code =this.assembler_code+ ".CODE\n";
        //Los errores:
        this.genError();
        
		// Las funciones:
        for (int i = 0; i < listFunciones.size(); i++) {
        	Nodo nodo = (Nodo) listFunciones.get(i).obj;
    		Nodo izq = null;
    		Nodo der = null;
    		try {
    			izq = (Nodo)nodo.izq.obj; //Cast de OBJ a nodo
    			der = (Nodo)nodo.der.obj; //Cast de OBJ a nodo
    		} catch (Exception e) {}
   			this.assembler_code =this.assembler_code+ nodo.nombre.replace(".", "@") +":\n";
   			boolean precondicion = false;
   			Nodo nodo_precondicion = (Nodo) izq.izq.obj;
   			if (nodo_precondicion != null)
   			{
   				if (nodo_precondicion.nombre.compareTo("PRE") == 0) //izq es precondicion, der es cuerpo de la funcion con precondicion
   	   			{
   					precondicion=true;
   	   				this.setCMP((Nodo) nodo_precondicion.izq.obj); //Apilara un Label y escribira la instruccion de salto
   	   				this.generateCode((Nodo) izq.der.obj);
   	   			} else {
   	   				generateCode((Nodo) izq.der.obj); //derecha del cuerpo, si no tiene precondicion nodo.izq.izq es null
   	   			}
   			}
   			
   			if (der != null) {
   				generateCode(der); //Return
   			}
   			this.assembler_code =this.assembler_code+ "ret\n";
   			if (precondicion)
   			{
   				this.assembler_code =this.assembler_code+ "Label"+this.pilaLabels.pollLast()+":\n"; //Desapilara un label
   				//this.setPRINT((Nodo)izq.der.obj); //mensaje NO, este es el CUERPO de la funcion.
   				Nodo print = (Nodo)nodo_precondicion.der.obj;
   				this.setPRINT(print,true);
   				this.assembler_code =this.assembler_code+ "ret\n";
   			}
        }

		this.assembler_code =this.assembler_code+ programa_filename +":\n";
		generateCode((Nodo) this.raiz.izq.obj); //Izquierda del nombre del programa deberia ser S de sentencia.
		if (this.raiz.der != null) {
			generateCode((Nodo) this.raiz.der.obj); // Derecha del nombre del programa no existe.
		}
		this.assembler_code =this.assembler_code+ "END " + programa_filename + "\n";
		this.assembler_code =this.assembler_code+ "invoke MessageBox, NULL, addr FIN, NULL, MB_OK\n"; //fin del programa


	}
	
	private void save() 
	{
		System.out.println(this.assembler_code);
		PrintWriter out;
		try {
			out = new PrintWriter(programa_filename +".txt");
			out.println(assembler_code);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}