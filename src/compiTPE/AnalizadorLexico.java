package compiTPE;
import java.io.*;
import java.util.Scanner;

import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {
	
	public String programa;
	public int codigoIndex;
	public int codigoIdentificador;
	public int codigoCTE;
	public int matrizTransicionEstados[][];
	public accionSemantica matrizAccionSemantica[][];
	public int contadorLineas;
	public Map<String, Integer> palabras_predefinidas; //palabras predefeinidas = Palabras Reservadas + Operadores de mas de 1 Caracter (asignacion, and, etc)
	//TABLA SIMBOLOS. No alcanza con key + value // puede ser con LISTA // puede ser con Map<String,Dictionary<String,String>>
	public ErrorHandler error_handler;
	
	public AnalizadorLexico(String filename, int matriztransicionestados[][], accionSemantica matrizaccionsemantica[][]) { // filename = TXT con las palabras predefinidas
		this.codigoIndex = 257;
		this.contadorLineas = 1;
		this.palabras_predefinidas = new HashMap<String,Integer>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {
				this.palabras_predefinidas.put(line, this.codigoIndex);
				this.codigoIndex +=1;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.codigoIdentificador = this.codigoIndex;
		this.codigoIndex +=1;
		this.codigoCTE = this.codigoIndex;
		this.codigoIndex +=1;
		this.matrizAccionSemantica = matrizaccionsemantica;
		this.matrizTransicionEstados = matriztransicionestados;
	}
	
	private int convertirSimbolo(char ch) {
		int retorno = 0;
		// CASE IF MAS GRANDE Q UNA CASA
		// PRIMERO LOS OPERADORES, NL, TAB, ESPACIO
		// DESPUES DIGITOS
		// DESPUES LA S MAYUSCULA
		// DESPUES LETRAS MAYUSCULAS
		// DESPUES LETRAS MINUSCULAS
		
		return retorno;	
	}
	
	public void setPrograma(String filename) { // filename = TXT con el programa a compilar
		try {
			this.programa = new Scanner(new File(filename)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int getCodigoPP(String token) {
		int retorno = this.palabras_predefinidas.get(token);
		return retorno;
	}
	
	public Tuple<String,Integer> getToken(){
		String token_actual = "";
		accionSemantica as;
		boolean eot = false; //end of token
		char ch;
		int estado_actual = 0;
		int index_simbolo;
		int tipo_token = 0; //esto se asigna en las AS asi que tambien se manda por parametro
		
		if (!this.programa.isEmpty()) {
			while (!eot) {
				ch = this.programa.charAt(0);
				index_simbolo = this.convertirSimbolo(ch);
				as = this.matrizAccionSemantica[estado_actual][index_simbolo];
				if (as != null) {
					estado_actual = this.matrizTransicionEstados[estado_actual][index_simbolo]; // Estado de la siguiente iteracion
					as.ejecutar(this, eot, tipo_token, token_actual, ch); //faltan los PARAMETROS, considerar mandar eot y que se vuelva true en las acciones semanticas donde termina el token
				}
				else {
					if (this.matrizTransicionEstados[estado_actual][index_simbolo] == -1) {
						//error handling
					}
				}
				
				if (!eot) {
					this.programa = this.programa.substring(1);
				}
				else {
					if (tipo_token == 1) { //es una palabra predefinida
						return new Tuple<String,Integer>(null,this.getCodigoPP(token_actual));
					}
					else if (tipo_token == 2) { //es un IDENTIFICADOR 
						return new Tuple<String,Integer>(token_actual,this.codigoIdentificador);
					}
					else if (tipo_token == 3) { //es una CTE
						return new Tuple<String,Integer>(token_actual,this.codigoCTE);
					}
					else if (tipo_token == 4) { //hay que devolver codigo ascii
						int ch_retorno = (int) token_actual.charAt(0);
						return new Tuple<String,Integer>(null,ch_retorno);
					}		
				}
			}
		}
		return null; // En analisis sintactico: si esta funciona retorna null es porque no hay mas programa para compilar!
	}
}
