package compiTPE;


public class AS7 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena, char caracter) {
		eot.bool = false;
		cadena.resetValor();
		
		return null;
	}

}