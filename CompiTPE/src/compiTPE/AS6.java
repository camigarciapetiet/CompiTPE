package compiTPE;

public class AS6 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena,
			char caracter) 
	{
		eot.bool = true;
		tipo_token.valor = 4;
		return null;
	}

}
