package compiTPE;

public class ErrorHandler {
	public void handle(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder token_actual, char ch) { //Solo sirve para nuestro automata
		if (token_actual.valor == ":") { //El automata estaba en el estado 10, cuya unica salida es el simbolo =
			eot.bool = true;
			token_actual.valor += "=";
			tipo_token.valor = 1;
		}
		else if (token_actual.valor == "&") { //El automata estaba en el estado 8, cuya unica salida es el simbolo &
			eot.bool = true;
			token_actual.valor += "&";
			tipo_token.valor = 1;

		}
		else if (token_actual.valor == "|") { //El automata estaba en el estado 9, cuya unica salida es el simbolo |
			eot.bool = true;
			token_actual.valor += "|";
			tipo_token.valor = 1;

		}
		else if (token_actual.valor == "=") { //El automata estaba en el estado 6, cuya unica salida es el simbolo =
			eot.bool = true;
			token_actual.valor += "=";
			tipo_token.valor = 1;

		}
		else if (token_actual.valor.endsWith("S")) {
			eot.bool = true;
			token_actual.valor.substring(0, token_actual.valor.length()-1);//Directamente le sacamos la parte exponencial y seguimos con el siguiente token
			tipo_token.valor = 3;
		}
	}
}
