package compiTPE;

public abstract class accionSemantica {
	
	public abstract String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena, char caracter);
}

