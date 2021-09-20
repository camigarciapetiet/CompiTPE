package compiTPE;

import java.util.HashMap;

public class AS3 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena, char caracter) {
		eot = true;
		if (analizadorLexico.palabras_predefinidas.get(cadena) != null){
			tipo_token = 1;
		}
		else {
			if (cadena.length() < 22) {
				//aca hace la marca de warning si se pasa del rango
			}
			tipo_token = 2;
			//Aca va la parte de buscar en la tabla de simbolos si esta o no
			// www.geeksforgeeks.org/how-to-check-if-a-key-exists-in-a-hashmap-in-java/
			
			//SI NO ESTA:
			analizadorLexico.tabla_simbolos.put(cadena, new HashMap<String,String>()); //Por ahora no guardamos ningun atributo!
		}
		return null;
	}

}
