package compiTPE;

public class ErrorHandler {
	public void handle(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena, char ch) { //Solo sirve para nuestro automata
		if (cadena == ":") { //El automata estaba en el estado 10, cuya unica salida es el simbolo =
			eot = true;
			cadena += "=";
		}
		else if (cadena == "&") { //El automata estaba en el estado 8, cuya unica salida es el simbolo &
			eot = true;
			cadena += "&";
		}
		else if (cadena == "|") { //El automata estaba en el estado 9, cuya unica salida es el simbolo |
			eot = true;
			cadena += "|";
		}
		else if (cadena == "=") { //El automata estaba en el estado 6, cuya unica salida es el simbolo =
			eot = true;
			cadena += "=";
		}
		else if (cadena.endsWith("S")) {
			eot = true;
			cadena.substring(0, cadena.length()-1);//Directamente le sacamos la parte exponencial y seguimos con el siguiente token
		}
	}
}
