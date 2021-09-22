package compiTPE;

public class AS4 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena,
			char caracter) {
		// TODO Auto-generated method stub
		eot= true;
		tipo_token=3;
		int aux= Integer.parseInt(cadena);
		int limite_sup= (int) (Math.pow(2, 15)-1);
		int limite_inf= (int) (-Math.pow(2, 15));
		if (aux>limite_sup || aux<limite_inf) {
			//warning
		}
		//alta en tabla de simbolos si no esta
		return null;
	}

}
