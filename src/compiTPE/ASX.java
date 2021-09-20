package compiTPE;

public class ASX extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena,
			char caracter) {
		//eot = false; No es EOT ya que no se tiene que mandar nada al analisis sintactico, simplemente no se suma a la cadena.
		analizadorLexico.contadorLineas += 1;
		return null;
	}

}
