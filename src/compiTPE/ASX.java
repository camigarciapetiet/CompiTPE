package compiTPE;

public class ASX extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena, char caracter) {
		if ((int) caracter == 10) {
			analizadorLexico.contadorLineas += 1;
		}
		return null;
	}

}
