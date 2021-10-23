package compiTPE;

public class AS9 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token,
			StringHolder token_actual, char caracter) {
		
		eot.bool=true;
		token_actual.addValor(caracter);
		tipo_token.valor=1;
		analizadorLexico.programa = analizadorLexico.programa.substring(1);
		return null;
	}

}
