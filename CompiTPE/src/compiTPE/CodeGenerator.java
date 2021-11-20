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
	private LinkedList<String> aux;
	private ArrayList<Nodo> listFunciones;
	
	public CodeGenerator(Nodo raizArbol, Parser analizadorSintactico)
	{
		this.raiz = raizArbol;
		this.analizador = analizadorSintactico;
		this.pilaLabels = new LinkedList<Integer>();
		this.contLabels = 0;
		this.listFunciones = analizadorSintactico.listFunciones;
	}
	
	public void run() throws IOException 
	{	
		this.assembler_code = ".MODEL small\n.STACK 200h\n"; //REVISAR cambiar tama;o y stack
		declareData();
		compile();		
		setAux(); //Inserta todas la auxiliares necesarias usadas en el programa
		save();
		
	}
	
	private String getNextAux(String tipo)
	{
		int number = aux.size()+1;
		analizador.analizadorLexico.tabla_simbolos.put("@aux"+number, new HashMap<String,String>());
		analizador.analizadorLexico.tabla_simbolos.get("@aux"+number).put("tipo", tipo);
		analizador.analizadorLexico.tabla_simbolos.get("@aux"+number).put("uso", "auxiliar");
		if (tipo.compareTo("SINGLE") == 0){
			aux.add("@aux"+number+" DW ?\n");
		} else if (tipo.compareTo("INT") == 0) {
			aux.add("@aux"+number+" DD ?\n");
		}
		
		return ("aux"+number);
	}
	
	private void setAux()
	{
		String auxs = "";
		for (int i = 0; i < aux.size(); i++) {
			auxs = auxs + aux.get(i);
		}
		this.assembler_code.replaceAll(";FLAG PARA DECLARAR AUXILIARES\n", auxs);
	}
	
	private void declareData()
	{
		this.assembler_code = assembler_code + ".DATA\n";
		//recorrer la tabla de simbolos y declarar todo lo que haga falta
		for (Map.Entry<String, HashMap<String,String>> entry : analizador.analizadorLexico.tabla_simbolos.entrySet()) {
			try {
			    if (analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("variable") == 0 || 
			    	analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("parametro") == 0 )
			    {
				    String tipo = analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("tipo");
				    if (tipo.compareTo("INT") == 0)
				    {
				    	this.assembler_code = assembler_code + entry.getKey() + " DW ?\n";
				    } else if (tipo.compareTo("SINGLE") == 0) {
				    	this.assembler_code = assembler_code + entry.getKey() + " DD ?\n";
				    } else { //Es TYPEDEF
				    	this.assembler_code = assembler_code + entry.getKey() + " DD ?\n";
				    } 
			    }
			    if (analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("cadena") == 0) //una cadena
			    {
			    	this.assembler_code = assembler_code + entry.getKey().replace(" ","") + " db \"" + entry.getKey().replace(" ", "") + "\", 0 \n";
			    }
			    if (analizador.analizadorLexico.tabla_simbolos.get(entry.getKey()).get("uso").compareTo("programa") == 0) //nombre del programa / label MAIN
			    {
			    	this.programa_filename = entry.getKey();
			    }
			} catch (Exception e) {}

		}
		
		this.assembler_code = assembler_code + ";FLAG PARA DECLARAR AUXILIARES\n";
	}
	
	
	private void genError() throws IOException {
		this.assembler_code = assembler_code + "invoke MessageBox, NULL, addr FIN, NULL, MB_OK\n"; //fin del programa
		this.assembler_code = assembler_code + "EXIT:\n invoke ExitProcess, 0\n";
		this.assembler_code = assembler_code + "DIV_CERO:\n invoke MessageBox, NULL, addr CERO, NULL, MB_OK\n JMP EXIT\n";
		this.assembler_code = assembler_code + "OVERFLOW_ERROR:\n invoke MessageBox, NULL, addr OVERFLOW_ERROR, NULL, MB_OK\n JMP EXIT\n";
		this.assembler_code = assembler_code + "ERROR_REC_MUTUA:\n invoke MessageBox, NULL, addr ERROR_REC_MUTUA, NULL, MB_OK\n JMP EXIT\n";


	}
	private void setADD(Nodo nodo) throws IOException {
		if (!nodo.izq.esHoja()) {
			generateCode(nodo.izq);
		}
		if(!nodo.der.esHoja()) {
			generateCode(nodo.der);
		}
		String tipoOP = "INT"; //PENDIENTE: agregar campo a los nodos que mantenga los tipos de las variables, para cuando se reemplacen los nodos por auxiliares y tal.
		//PENDIENTE: Podria ser sacar el tipo del nodo hoja del subarbol derecho antes de generar codigo que deberia corresponder a una entrada de la tabla de simbolos con tipo.
		
		
		// CHEQUEO OVERFLOW
		if (tipoOP.compareTo("INT") == 0) {
			String registroAux = "BX"; //16	 bits
			assembler_code =  assembler_code + "MOV " + registroAux + ", " + nodo.der.nombre + "\n";
			assembler_code =  assembler_code + "CMP " + registroAux + ", "  + nodo.izq.nombre + "\n";
			registroAux = "";
		} 	
		this.assembler_code = assembler_code + "JO OVERFLOW_ERROR\n";
		
		/*else if (tipoOP.compareTo("SINGLE") == 0) { NO HACIA FALTA CHEQUEO DE OVERFLOW SINGLE
			assembler_code =  assembler_code + "FLDZ \n";
			assemblera_code =  assembler_code + "FCOM " + "_"+nodo.der.nombre.replace(".", "_");
		}*/
		
		
		if (tipoOP.compareTo("INT") == 0) {

			if (nodo.izq.esRegistro() && nodo.der.esRegistro()) {//Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code = assembler_code + "ADD " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
				nodo.der.nombre = ""; //vacio el registro ya que no lo necesito (para futuras comparaciones)
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
				nodo.nombre = aux;
			}
			else {
				if (nodo.izq.esRegistro()) {
					assembler_code =  assembler_code + "ADD " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
					nodo.nombre = nodo.izq.nombre;
					
					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
					nodo.nombre = aux;
				}
				else {
					if (nodo.der.esRegistro()){
						String aux = "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.der.nombre + "\n";
						assembler_code =  assembler_code + "MOV " + nodo.der.nombre + ", " + nodo.izq.nombre + "\n"; // der.nombre es REG
						assembler_code =  assembler_code + "ADD " + nodo.izq.nombre + ", " + aux + "\n";
						
						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code =  assembler_code + "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;
					}
					else {
						assembler_code =  assembler_code + "MOV " + "AX, " + nodo.izq.nombre + "\n";
						nodo.izq.nombre = "AX";
						assembler_code =  assembler_code + "ADD " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (nodo.der.nombre.contains(".") && nodo.izq.nombre.contains(".")) {
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code = assembler_code +"FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FADD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (nodo.der.nombre.contains(".")) {
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code = assembler_code + "FLD " + nodo.izq.nombre + "\n";
	                    this.assembler_code = assembler_code + "FADD " + "_" + nodo.der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (nodo.izq.nombre.contains(".")) {
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code = assembler_code + "FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
	    	                this.assembler_code = assembler_code + "FADD " + "_" +  nodo.der.nombre.replace(".", "_") + "\n";
	    	                this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else {
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code = assembler_code +"FLD " +nodo.izq.nombre+ "\n";
	                        this.assembler_code = assembler_code +"FADD " + "_" + nodo.der.nombre.replace(".", "_")+ "\n";
	                        this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	    }

	
	private void setSUB(Nodo nodo) throws IOException {
		if (!nodo.izq.esHoja()) {
			generateCode(nodo.izq);
		}
		if(!nodo.der.esHoja()) {
			generateCode(nodo.der);
		}
		String tipoOP = "INT"; //agregar campo a los nodos que mantenga los tipos de las variables, para cuando se reemplacen los nodos por auxiliares y tal.
		
		if (tipoOP.compareTo("INT") == 0) {
			if (nodo.izq.esRegistro() && nodo.der.esRegistro()) { //Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code = assembler_code + "SUB " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
				nodo.izq.nombre = "";
				nodo.der.nombre = ""; //vacio el registro ya que no lo necesito (para futuras comparaciones)
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
				nodo.nombre = aux;
			}
			else {
				if (nodo.izq.esRegistro()) {
					assembler_code =  assembler_code + "SUB " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";

					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
					nodo.nombre = aux;
				}
				else {
					if (nodo.der.esRegistro()){
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.der.nombre + "\n";
						assembler_code =  assembler_code + "MOV " + nodo.der.nombre + ", " + nodo.izq.nombre + "\n";
						assembler_code =  assembler_code + "SUB " + nodo.izq.nombre + ", " + aux + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code =  assembler_code + "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;
					} else {
						assembler_code =  assembler_code + "MOV " + "AX, " + nodo.izq.nombre + "\n";
						nodo.izq.nombre = "AX";
						assembler_code =  assembler_code + "SUB " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
						
						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (nodo.der.nombre.contains(".") && nodo.izq.nombre.contains(".")) {
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code = assembler_code +"FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FSUB " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (nodo.der.nombre.contains(".")) {
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code = assembler_code + "FLD " + nodo.izq.nombre + "\n";
	                    this.assembler_code = assembler_code + "FSUB " + "_" + nodo.der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (nodo.izq.nombre.contains(".")) {
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code = assembler_code + "FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
	    	                this.assembler_code = assembler_code + "FSUB " + "_" +  nodo.der.nombre.replace(".", "_") + "\n";
	    	                this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else {
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code = assembler_code +"FLD " +nodo.izq.nombre+ "\n";
	                        this.assembler_code = assembler_code +"FSUB " + "_" + nodo.der.nombre.replace(".", "_")+ "\n";
	                        this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	}
	
	private void setIMUL(Nodo nodo) throws IOException {
		if (!nodo.izq.esHoja()) {
			generateCode(nodo.izq);
		}
		if(!nodo.der.esHoja()) {
			generateCode(nodo.der);
		}
		String tipoOP = "INT"; //PENDIENTE: agregar campo a los nodos que mantenga los tipos de las variables, para cuando se reemplacen los nodos por auxiliares y tal.
		if (tipoOP.compareTo("INT") == 0) {
			if (nodo.izq.esRegistro() && nodo.der.esRegistro()) { //Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code = assembler_code + "IMUL " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
				nodo.nombre = aux;
			}
			else {
				if (nodo.izq.esRegistro()) {
					assembler_code =  assembler_code + "IMUL " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
					nodo.nombre = nodo.izq.nombre;
					
					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
					nodo.nombre = aux;
				}
				else {
					if (nodo.der.esRegistro()){
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.der.nombre + "\n";
						assembler_code =  assembler_code + "MOV " + nodo.der.nombre + ", " + nodo.izq.nombre + "\n";
						assembler_code =  assembler_code + "IMUL " + nodo.izq.nombre + ", " + aux + "\n";
						
						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code =  assembler_code + "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;	
					}else {
						assembler_code =  assembler_code + "MOV " + "AX, " + nodo.izq.nombre + "\n";
						nodo.izq.nombre = "AX";
						assembler_code =  assembler_code + "IMUL " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (nodo.der.nombre.contains(".") && nodo.izq.nombre.contains(".")) {
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code = assembler_code +"FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FIMUL " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (nodo.der.nombre.contains(".")) {
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code = assembler_code + "FLD " + nodo.izq.nombre + "\n";
	                    this.assembler_code = assembler_code + "FIMUL " + "_" + nodo.der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (nodo.izq.nombre.contains(".")) {
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code = assembler_code + "FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
	    	                this.assembler_code = assembler_code + "FIMUL" + "_" +  nodo.der.nombre.replace(".", "_") + "\n";
	    	                this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else {
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code = assembler_code +"FLD " +nodo.izq.nombre+ "\n";
	                        this.assembler_code = assembler_code +"FIMUL " + "_" + nodo.der.nombre.replace(".", "_")+ "\n";
	                        this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	}
	
	private void setIDIV(Nodo nodo) throws IOException {
		if (!nodo.izq.esHoja()) {
			generateCode(nodo.izq);
		}
		if(!nodo.der.esHoja()) {
			generateCode(nodo.der);
		}
		String tipoOP = "INT"; //PENDIENTE: agregar campo a los nodos que mantenga los tipos de las variables, para cuando se reemplacen los nodos por auxiliares y tal.
		
		//CHEQUEO IDIVISION POR CERO:
		if (tipoOP.compareTo("INT") == 0) {
			String registroAux = "BX"; //16	 bits
			assembler_code =  assembler_code + "MOV " + registroAux + ", " + nodo.der.nombre + "\n";
			assembler_code =  assembler_code + "CMP " + registroAux + ", "  + "0\n";
			registroAux = "";
		} else if (tipoOP.compareTo("SINGLE") == 0) {
			assembler_code =  assembler_code + "FLDZ \n";
			assembler_code =  assembler_code + "FCOM " + "_"+nodo.der.nombre.replace(".", "_");
		}
		this.assembler_code = assembler_code + "JE DIV_CERO\n";
		
		if (tipoOP.compareTo("INT") == 0) {
			if (nodo.izq.esRegistro() && nodo.der.esRegistro()) { //Si solo usamos un registro y el resto auxiliares no hace falta la 2da condicion
				assembler_code = assembler_code + "IDIV " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
				
				//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
				String aux= "@"+ getNextAux(tipoOP);
				assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
				nodo.nombre = aux;
			}
			else {
				if (nodo.izq.esRegistro()) {
					assembler_code =  assembler_code + "IDIV " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";
					
					//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
					String aux= "@"+ getNextAux(tipoOP);
					assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
					nodo.nombre = aux;
				}
				else {
					if (nodo.der.esRegistro()){
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.der.nombre + "\n";
						assembler_code =  assembler_code + "MOV " + nodo.der.nombre + ", " + nodo.izq.nombre + "\n";
						assembler_code =  assembler_code + "IDIV " + nodo.izq.nombre + ", " + aux + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						assembler_code =  assembler_code + "MOV " + aux + ", " + "AX" + "\n";
						nodo.nombre = aux;
					}else {
						assembler_code =  assembler_code + "MOV " + "AX, " + nodo.izq.nombre + "\n";
						nodo.izq.nombre = "AX";
						assembler_code =  assembler_code + "IDIV " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n";

						//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
						String aux= "@"+ getNextAux(tipoOP);
						assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
						nodo.nombre = aux;
					}
				}
			}
		}
		else if (tipoOP.compareTo("SINGLE") == 0) {
            if (nodo.der.nombre.contains(".") && nodo.izq.nombre.contains(".")) { //Los DOS son CTE
                String aux = "@"+ this.getNextAux(tipoOP);
                this.assembler_code = assembler_code +"FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FIDIV " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
                this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
                nodo.nombre = aux;
	            } else {
	                if (nodo.der.nombre.contains(".")) { //DER es CTE
	                    String aux = "@"+ this.getNextAux(tipoOP);
	                    this.assembler_code = assembler_code + "FLD " + nodo.izq.nombre + "\n";
	                    this.assembler_code = assembler_code + "FIDIV " + "_" + nodo.der.nombre.replace(".", "_")+ "\n";
	                    this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	                    nodo.nombre = aux;
	                } else {
	                	if (nodo.izq.nombre.contains(".")) { //IZQ es CTE
	    	                String aux = "@"+ this.getNextAux(tipoOP);
	    	                this.assembler_code = assembler_code + "FLD " + "_" + nodo.izq.nombre.replace(".", "_")+ "\n";
	    	                this.assembler_code = assembler_code + "FIDIV " + nodo.der.nombre.replace(".", "_") + "\n";
	    	                this.assembler_code = assembler_code + "FSTP " + aux + "\n";
	    	                nodo.nombre = aux;
	                    } else { //Las dos son VARIABLES
	                        String aux = "@"+ this.getNextAux(tipoOP);
	                        this.assembler_code = assembler_code +"FLD " +nodo.izq.nombre+ "\n";
	                        this.assembler_code = assembler_code +"FIDIV " + nodo.der.nombre+ "\n";
	                        this.assembler_code = assembler_code +"FSTP " + aux+ "\n";
	                        nodo.nombre =aux;
	                    }
	                }
	            }
	        }
	}
	
	private void setASG(Nodo nodo) throws IOException {
		if(!nodo.der.esHoja()) {
			generateCode(nodo.der);
		}
		String tipoOP = "INT"; //PENDIENTE
		if (tipoOP.compareTo("INT") == 0) {
			this.assembler_code = assembler_code + "MOV " + nodo.izq.nombre + ", " + nodo.der.nombre + "\n"; 
		}
		else if (tipoOP.compareTo("SINGLE") == 0) { //IZQ siempre es una variable
			if (nodo.der.nombre.contains(".")) { //tiene parte exponencial
				this.assembler_code = assembler_code + "FLD _" + nodo.der.nombre.replace(".","_") + "\n"; 
				this.assembler_code = assembler_code + "FSTP " + nodo.izq.nombre + "\n"; 
			}
			else {
				this.assembler_code = assembler_code + "FLD " + nodo.der.nombre + "\n"; 
				this.assembler_code = assembler_code + "FSTP " + nodo.izq.nombre + "\n"; 	
			}
		} else if (analizador.isTypeDef(analizador.getTipoVariable(nodo.izq.nombre))){
			String auxreg32 = "EAX";
			this.assembler_code = assembler_code + "MOV " + auxreg32 + ", " + nodo.der.nombre + "\n"; 
			this.assembler_code = assembler_code + "MOV " + nodo.izq.nombre + ", " + auxreg32 + "\n"; 

		}
	}
	
	private void setPRINT(Nodo nodo) throws IOException
	{
		this.assembler_code = assembler_code + "invoke MessageBox, NULL, addr, " + nodo.nombre.replace(" ", "") + ", addr "+nodo.nombre.replace(" ", "")+", MB_OK\n";	
	}
	
	private void setIF(Nodo nodo) throws IOException { 
		this.setCMP(nodo.izq);
		if (nodo.der.der.nombre.compareTo("ELSE") == 0){
			this.setCuerpoIfElse(nodo.der.izq);
			this.setElse(nodo.der.der);
		}
		else {
			this.setCuerpoIf(nodo.der.izq);
		}
	}
	
	private void setCuerpoIf(Nodo nodo) throws IOException { //SIN ELSE
		generateCode(nodo);
		nodo.nombre = ""; //Libero su valor por si ocupa un REG
		this.assembler_code = assembler_code + "Label" + this.pilaLabels.pollLast() + ": \n"; //desapilo el label generado en CMP y lo inserto.
	}
	
	private void setCuerpoIfElse(Nodo nodo) throws IOException { 
		generateCode(nodo);
		nodo.nombre = ""; //Libero su valor por si ocupa un REG
		contLabels++;
		this.pilaLabels.addLast(this.contLabels);	
		this.assembler_code = assembler_code + "JMP Label" + contLabels + "\n"; //Salto para omitir el ELSE
	}
	
	private void setElse(Nodo nodo) throws IOException
	{
		String elseLabel = "Label" + this.pilaLabels.pollLast() +":\n"; //Desapilo y guardo el salto hacia fuera del else
		this.assembler_code = assembler_code + "Label" + this.pilaLabels.pollLast()+":\n"; //Desapilo el salto que tenia si CMP era falso y genero el ELSE
		this.generateCode(nodo); //genero el codigo dentro de ELSE
		this.assembler_code = assembler_code + elseLabel; 
	}
	
	private void setCMP(Nodo nodo) throws IOException {
		if (!nodo.izq.esHoja()) {
			generateCode(nodo.izq);
		}
		if(!nodo.der.esHoja()) {
			generateCode(nodo.der);
		}
		this.contLabels++;
		this.pilaLabels.addLast(this.contLabels);
		String registro = "AX";
		String tipoOP = "INT"; //PENDIENTE
		if (tipoOP.compareTo("INT") == 0) {
			if (nodo.der.esRegistro()){ //Como guardare la comparacion en un registro a la izquierda, si mi derecha es un registro debo liberarlo para no pisarlo
				String aux="@"+ getNextAux(tipoOP);
				assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.der.nombre + "\n";
				nodo.der.nombre = aux;
			}
			this.assembler_code = assembler_code + "MOV " + registro + ", " + nodo.izq.nombre + "\n";
			nodo.izq.nombre = registro;
			switch (nodo.nombre) {
				case "<": {
					this.assembler_code = "CMP " +  nodo.izq.nombre + ", " + nodo.der.nombre + "\n" + "JL Label"+contLabels + "\n";
					break;
				}
				case "<=": {
					this.assembler_code = "CMP " +  nodo.izq.nombre + ", " + nodo.der.nombre + "\n" + "JLE Label"+contLabels + "\n";
					break;
				}
				case ">": {
					this.assembler_code = "CMP " +  nodo.izq.nombre + ", " + nodo.der.nombre + "\n" + "JG Label"+contLabels + "\n";
					break;
				}
				case ">=": {
					this.assembler_code = "CMP " +  nodo.izq.nombre + ", " + nodo.der.nombre + "\n" + "JGE Label"+contLabels + "\n";
					break;
				}
				case "!=": {
					this.assembler_code = "CMP " +  nodo.izq.nombre + ", " + nodo.der.nombre + "\n" + "JNE Label"+contLabels + "\n";
					break;
				}
				case "==": {
					this.assembler_code = "CMP " +  nodo.izq.nombre + ", " + nodo.der.nombre + "\n" + "JE Label"+contLabels + "\n";
					break;
				}
			}
		}
		if (tipoOP.compareTo("SINGLE") == 0) {
			switch (nodo.nombre) {
				case "<": {
					if (nodo.der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP _"+nodo.der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JL Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP "+nodo.der.nombre+"\n FSTSW AX\n SAHF \n JL Label" +contLabels+"\n";
					}
					break;
				}
				case "<=": {
					if (nodo.der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP _"+nodo.der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JLE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP "+nodo.der.nombre+"\n FSTSW AX\n SAHF \n JLE Label" +contLabels+"\n";
					}
					break;
				}
				case ">": {
					if (nodo.der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP _"+nodo.der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JG Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP "+nodo.der.nombre+"\n FSTSW AX\n SAHF \n JG Label" +contLabels+"\n";
					}
					break;
				}
				case ">=": {
					if (nodo.der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP _"+nodo.der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JGE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP "+nodo.der.nombre+"\n FSTSW AX\n SAHF \n JGE Label" +contLabels+"\n";
					}
					break;
				}
				case "!=": {
					if (nodo.der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP _"+nodo.der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JNE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP "+nodo.der.nombre+"\n FSTSW AX\n SAHF \n JNE Label" +contLabels+"\n";
					}
					break;
				}
				case "==": {
					if (nodo.der.nombre.contains(".")) { 
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP _"+nodo.der.nombre.replace(".","_")+"\n FSTSW AX\n SAHF \n JE Label" +contLabels+"\n";
					} else {
						this.assembler_code = "FLD "+nodo.izq.nombre+"\n FCOMP "+nodo.der.nombre+"\n FSTSW AX\n SAHF \n JE Label" +contLabels+"\n";
					}
					break;
				}
			}
		}
	}
	

	private void setSENT(Nodo nodo) throws IOException //Sentencia (subarbol) cualquiera
	{
		this.generateCode(nodo.izq);
		if (nodo.der != null) {
			this.generateCode(nodo.der);
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
		//PENDIENTE: Agregar chequeo semantico recursion mutua... wtf? tema f
		if (nodo.der.izq != null) { //Es una expresion
			this.generateCode(nodo.der);
		}
		
		//Chequeo REC MUTUA

				
				
				
		//parametro: Buscamos getParametro asociado al nombre de la funcion y le asignamos el valor del hijo derecho
		String funcion = nodo.izq.nombre;
		String parametro = getParametro(nodo.izq.nombre);
		this.assembler_code = assembler_code + "MOV " + parametro + ", " + nodo.der.nombre + "\n";
		if (analizador.isTypeDef(analizador.getTipoVariable(funcion))) {
			//funcion realmente es la variable typedef almacenando la direccion de memoria de la funcion, al ponerla en [] funciona como puntero a la func
			this.assembler_code = assembler_code + "CALL [" + funcion + "]\n";
		} else {
			//SI ES FUNC NORMAL directamente salto a la label habiendo asignado el parametro previamente
			this.assembler_code = assembler_code + "CALL " + funcion + "\n";
		}
		nodo.nombre = "@"+this.aux.getLast(); //resultado de la invocacion, es decir el RETURN.
	}
	
	private void setREPEAT(Nodo nodo)
	{
		// PENDIENTE
	}
	
	private void setRETURN(Nodo nodo) throws IOException
	{
		this.generateCode(nodo.izq);
		//Al trabajar con variables auxiliares, el resultado de las operaciones aritmeticas queda en una variable auxiliar.
		String tipoOP = "INT"; //PENDIENTE
		String aux= "@"+ getNextAux(tipoOP);
		assembler_code =  assembler_code + "MOV " + aux + ", " + nodo.izq.nombre + "\n";
		nodo.nombre = aux;
	}
	
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
			case "PRINT": {setPRINT(nodo); break;}
			case "REPEAT": {setREPEAT(nodo); break;}
			case "RETURN": {setRETURN(nodo); break;}
		}
	}
	
	private void compile() throws IOException
	{
		this.assembler_code = assembler_code + ".CODE\n";

		// Las funciones:
        for (int i = 0; i < listFunciones.size(); i++) {
        	Nodo nodo = listFunciones.get(i);
   			this.assembler_code = assembler_code + nodo.nombre +":\n";
   			boolean precondicion = false;
   			if (nodo.izq.izq.nombre.compareTo("precondicion") == 0) //izq es cuerpo, izq izq puede ser precondicion
   			{
   				precondicion = true;
   				this.setCMP(nodo.izq.izq); //Apilara un Label y escribira la instruccion de salto
   				this.generateCode(nodo.izq.der);
   			} else {
   				generateCode(nodo.izq.der); //derecha del cuerpo, si no tiene precondicion nodo.izq.izq es null
   			}
   			if (nodo.der != null) {
   				generateCode(nodo.der); //Return
   			}
   			this.assembler_code = assembler_code + "ret\n";
   			if (precondicion)
   			{
   				this.assembler_code = assembler_code + "Label"+this.pilaLabels.pollLast()+":\n"; //Desapilara un label
   				this.setPRINT(nodo.izq.der); //mensaje
   				this.assembler_code = assembler_code + "ret\n";
   			}
        }

		this.assembler_code = assembler_code + programa_filename +":\n";
		generateCode(this.raiz.izq); //Izquierda del nombre del programa deberia ser S de sentencia.
		if (this.raiz.der != null) {
			generateCode(this.raiz.der); // Derecha del nombre del programa no existe.
		}
		this.assembler_code = assembler_code + "END " + programa_filename + "\n";
		
        //Los errores:
        this.genError();
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