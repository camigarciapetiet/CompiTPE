package compiTPE;

public class AS2 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena,
			char caracter) 
	{
		cadena.addValor(caracter);
		return null;
	}

}
