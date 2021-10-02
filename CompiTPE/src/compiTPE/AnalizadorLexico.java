package compiTPE;
import java.util.*;
import java.io.*;

public class AnalizadorLexico {
	
	public String programa;
	public int codigoIndex;
	public int codigoIdentificador;
	public int codigoCTE;
	public int codigoCADENA;
	public int matrizTransicionEstados[][];
	public accionSemantica matrizAccionSemantica[][];
	public int contadorLineas;
	public Map<String, Integer> palabras_predefinidas; //palabras predefeinidas = Palabras Reservadas + Operadores de mas de 1 Caracter (asignacion, and, etc)
	public Map<String, HashMap<String, String>> tabla_simbolos;
	public ErrorHandler error_handler;
	public List<String> erroresLex;
	
	public AnalizadorLexico(String filename, int matriztransicionestados[][], accionSemantica matrizaccionsemantica[][], ErrorHandler error_handler) { // filename = TXT con las palabras predefinidas
		this.codigoIndex = 257;
		this.codigoIdentificador = this.codigoIndex;
		this.codigoIndex +=1;
		this.codigoCTE = this.codigoIndex;
		this.codigoIndex +=1;
		this.codigoCADENA = this.codigoIndex;
		this.codigoIndex +=1;
		this.contadorLineas = 1;
		this.palabras_predefinidas = new HashMap<String,Integer>();
		this.tabla_simbolos = new HashMap<String,HashMap<String,String>>();
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
		this.matrizAccionSemantica = matrizaccionsemantica;
		this.matrizTransicionEstados = matriztransicionestados;
		this.error_handler = error_handler;
		this.erroresLex= new ArrayList<String>();
	}
	
	private int convertirSimbolo(char ch) {
		if ((int) ch == 43) return 5; // +
		else if ((int) ch == 46) return 3; // .
		else if ((int) ch == 58) return 4; // :
		else if ((int) ch == 45) return 6; // -
		else if ((int) ch == 40) return 7; // (
		else if ((int) ch == 41) return 7; // )
		else if ((int) ch == 44) return 7; // ,
		else if ((int) ch == 59) return 7; // ;
		else if ((int) ch == 57) return 7; // : agregado por el TP2 tema 17
		else if ((int) ch == 42) return 7; // *
		else if ((int) ch == 47) return 8; // /
		else if ((int) ch == 60) return 9; // <
		else if ((int) ch == 62) return 10; // >
		else if ((int) ch == 61) return 11; // =
		else if ((int) ch == 95) return 12; // _
		else if ((int) ch == 37) return 13; // %
		else if ((int) ch == 38) return 14; // &
		else if ((int) ch == 124) return 15; // |
		else if ((int) ch == 32) return 16; // espacio
		else if ((int) ch == 9) return 16; // tab
		else if ((int) ch == 10) return 17; // ln
		else if ((int) ch == 13) return 17; // SALTO DE CARRO = ln berreta
		else if (ch == 'S') return 18; // S
		else if (Character.isDigit(ch)) return 2;
		else if (Character.isUpperCase(ch)) return 1;
		else if (Character.isLowerCase(ch)) return 0;
		else return 19;
	}
	
	@SuppressWarnings("resource")
	public void setPrograma(String filename) { // filename = TXT con el programa a compilar
		try {
			this.programa = new Scanner(new File(filename)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.programa = this.programa + " ";
	}
	
	public int getCodigoPP(String token) {
		int retorno = this.palabras_predefinidas.get(token);
		return retorno;
	}
	
	public int yylex(ParserVal yylval){
		StringHolder token_actual = new StringHolder();
		accionSemantica as;
		BooleanHolder eot = new BooleanHolder(false); //end of token
		char ch;
		int estado_actual = 0; // FILA de la matriz de transicion de estados y AS
		int index_simbolo; // COLUMNA de la matriz de transicion de estados y AS
		IntHolder tipo_token = new IntHolder(); //esto se asigna en las AS asi que tambien se manda por parametro
		
		if (!this.programa.isEmpty()) {
			while ((!eot.bool) && (!this.programa.isEmpty())) {
				ch = this.programa.charAt(0);
				System.out.println(ch);
				index_simbolo = this.convertirSimbolo(ch);
				as = this.matrizAccionSemantica[estado_actual][index_simbolo];
				if (as != null) {
					estado_actual = this.matrizTransicionEstados[estado_actual][index_simbolo]; // Estado de la siguiente iteracion
					as.ejecutar(this, eot, tipo_token, token_actual, ch); 
				}
				else {
					if (this.matrizTransicionEstados[estado_actual][index_simbolo] == -1) {
						this.error_handler.handle(this, eot, tipo_token, token_actual, ch);
					}
				}
				
				if (!eot.bool) {
					this.programa = this.programa.substring(1);
				}
				else {
					if (tipo_token.valor == 1) { //es una palabra predefinida
						yylval.sval = null; ///set(null);
						return this.getCodigoPP(token_actual.valor); //nll
					}
					else if (tipo_token.valor == 2) { //es un IDENTIFICADOR
						yylval.sval = token_actual.valor;
						return this.codigoIdentificador;//(token_actual.valor
					}
					else if (tipo_token.valor == 3) { //es una CTE
						yylval.sval = token_actual.valor;
						return this.codigoCTE;//token_actual.valor
					}
					else if (tipo_token.valor == 4) { //hay que devolver codigo ascii
						int ch_retorno = (int) token_actual.valor.charAt(0);
						yylval.sval = null;
						return ch_retorno;
					}		
					else if (tipo_token.valor == 5) {
						yylval.sval = token_actual.valor;
						return this.codigoCADENA; //token_actual.valor
					}
				}
			}
		}
		return 0; // En analisis sintactico: si esta funcion retorna null es porque no hay mas programa para compilar!
		//cuando llega al final retorna 0 porque no llego al eof
	}
}
