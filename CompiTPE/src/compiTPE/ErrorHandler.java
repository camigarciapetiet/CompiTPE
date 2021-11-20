package compiTPE;

public class ErrorHandler {
	public void handle(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder token_actual, char ch) { //Solo sirve para nuestro automata
		System.out.println("Error con: " + token_actual.valor + " al leer: "+ ch);
		try {
			if (token_actual.valor == "&") { //El automata estaba en el estado 8, cuya unica salida es el simbolo &
				eot.bool = true;
				token_actual.valor += "&";
				tipo_token.valor = 1;
				analizadorLexico.warningsLex.add("Warning en la linea "+ analizadorLexico.contadorLineas + ": '&' esperado despues de '&'");
			}
			else if (token_actual.valor == "|") { //El automata estaba en el estado 9, cuya unica salida es el simbolo |
				eot.bool = true;
				token_actual.valor += "|";
				tipo_token.valor = 1;
				analizadorLexico.warningsLex.add("Warning en la linea "+ analizadorLexico.contadorLineas + ": '|' esperado despues de '|'");
			}
			else if (token_actual.valor.endsWith("S")) {
				eot.bool = true;
				token_actual.valor.substring(0, token_actual.valor.length()-1);//Directamente le sacamos la parte exponencial y seguimos con el siguiente token
				tipo_token.valor = 3;
				analizadorLexico.warningsLex.add("Warning en la linea "+ analizadorLexico.contadorLineas + ": double esperado luego de 'S'");
			}
			else if (((int) ch == 13) || ((int) ch == 10)) { //El automata estaba en el estado 11 y leyo un nl, lo reemplazamos por un espacio.
				ch = ' ';
				analizadorLexico.warningsLex.add("Warning en la linea "+ analizadorLexico.contadorLineas + ": ln inesperado dentro de una cadena de caracteres");
			}
		}
		catch(Exception e)
		{}
	}
}
