package compiTPE;

public abstract class accionSemantica {
	
	public abstract String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder token_actual, char caracter);
}

