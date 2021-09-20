package compiTPE;

import java.util.*;

public class AS3 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena, char caracter) {
		eot = true;
		if (analizadorLexico.palabras_predefinidas.get(cadena) != null){
			tipo_token = 1;
		}
		else {
			if (cadena.length() < 22) {
				//aca hace la marca de warning si se pasa del rango ???
			}
			tipo_token = 2;

			
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        boolean isKeyPresent = false;
	        while (iterator.hasNext()) {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (cadena == entry.getKey()) {
	                isKeyPresent = true;
	                return null;
	            }
	        }
	        
	        
	        if (isKeyPresent == false) {
	        	analizadorLexico.tabla_simbolos.put(cadena, new HashMap<String,String>()); //Por ahora no guardamos ningun atributo!
	        	analizadorLexico.tabla_simbolos.get(cadena).put("linea",String.valueOf(analizadorLexico.contadorLineas)); //EJEMPLO
	        }
			
		}
		return null;
	}

}
