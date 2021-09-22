package compiTPE;

public class AS2 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena,
			char caracter) 
	{
		cadena = cadena + caracter;
		return null;
	}

}
