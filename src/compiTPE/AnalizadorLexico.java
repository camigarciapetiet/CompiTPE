package compiTPE;
import java.io.*;
import java.util.Scanner;

import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {
	
	String programa;
	int codigoIndex;
	int codigoIdentificador;
	int codigoCTE;
	int matrizTransicionEstados[][];
	accionSemantica matrizAccionSemantica[][];
	int contadorLineas;
	Map<String, Integer> palabras_predefinidas; //palabras predefeinidas = Palabras Reservadas + Operadores de mas de 1 Caracter (asignacion, and, etc)
	//TABLA SIMBOLOS. No alcanza con key + value
	
	
	public AnalizadorLexico(String filename) { // filename = TXT con las palabras predefinidas
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
		
		// MATRIZ ACCIONES SEMANTICAS Y TRANSICION ESTADOS. PODRIAMOS PASARLA COMO PARAMETRO EN VEZ DE HACERLO ACA POR TEMAS DE ESCALABILIDAD!
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
	
	public int getCodigoTS(String token){
		int retorno = 0;
		//retorno = this.tabla_simbolos[token]; o algo asi ////////// CREO Q NO LO VAMOS A USAR
		return retorno;
	}
	
	public int getCodigoPP(String token) {
		int retorno = this.palabras_predefinidas.get(token);
		return retorno;
	}
	
	public Pair<String,Integer> getToken(){
		String token_actual = "";
		accionSemantica as;
		boolean eot = false; //end of token
		char ch;
		int estado_actual = 0;
		int index_simbolo;
		int tipo_token; //esto se asigna en las AS asi que tambien se manda por parametro
		
		if (!this.programa.isEmpty()) {
			while (!eot) {
				ch = this.programa.charAt(0);
				index_simbolo = this.convertirSimbolo(ch);
				as = this.matrizAccionSemantica[estado_actual][index_simbolo];
				estado_actual = this.matrizTransicionEstados[estado_actual][index_simbolo]; // Estado de la siguiente iteracion
				as.ejecutar(this, eot, tipo_token, token_actual, ch); //faltan los PARAMETROS, considerar mandar eot y que se vuelva true en las acciones semanticas donde termina el token
				if (!eot) {
					this.programa = this.programa.substring(1);
				}
				else {
					if (tipo_token == 1) { //es una palabra predefinida
						return new Pair<String,Integer>(null,this.getCodigoPP(token_actual));
					}
					else if (tipo_token == 2) { //es un IDENTIFICADOR 
						return new Pair<String,Integer>(token_actual,this.codigoIdentificador);
					}
					else if (tipo_token == 3) { //es una CTE
						return new Pair<String,Integer>(token_actual,this.codigoCTE);
					}
					else if (tipo_token == 4) { //hay que devolver codigo ascii
						return new Pair<String,Integer>(null,(int) token_actual);
					}		
				}
			}
		}
	}
}
