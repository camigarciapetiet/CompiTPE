package compiTPE;

public class AS10 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token,
			StringHolder token_actual, char caracter) {
		eot.bool=true;
		token_actual.valor="";
		token_actual.addValor(caracter);
		tipo_token.valor=4;
		analizadorLexico.programa = analizadorLexico.programa.substring(1);
		return null;
	}

}
