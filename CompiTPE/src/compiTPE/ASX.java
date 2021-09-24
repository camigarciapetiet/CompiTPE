package compiTPE;

public class ASX extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena, char caracter) {
		if ((int) caracter == 10) {
			analizadorLexico.contadorLineas += 1;
		}
		return null;
	}

}
