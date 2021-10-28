package compiTPE;

public class ASXI extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token,
			StringHolder token_actual, char caracter) {
		token_actual.addValor(caracter);
		if((int) caracter == 13 || (int) caracter == 10) {
			analizadorLexico.contadorLineas++;
		}
		return null;
	}

}
