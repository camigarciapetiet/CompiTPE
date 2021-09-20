package compiTPE;

public class AS6 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena,
			char caracter) 
	{
		eot = true;
		tipo_token = 4;
		return null;
	}

}
