package compiTPE;

public class AS1 extends accionSemantica {

	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena,
			char caracter) 
	{
		cadena.resetValor();
		cadena.addValor(caracter);
		return null;
	}


}
