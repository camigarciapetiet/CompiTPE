package compiTPE;

import java.util.*;

public class AS3 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena, char caracter) {
		eot.bool = true;
		if (analizadorLexico.palabras_predefinidas.get(cadena.valor) != null){
			tipo_token.valor = 1;
		}
		else {
			if (cadena.valor.length() > 22) {
				while (cadena.valor.length() > 22) { //Borro caracteres hata que sea long 22.
					cadena.valor = cadena.valor.substring(0, cadena.valor.length()-1);
				}
				analizadorLexico.erroresLex.add("Warning en la linea "+ analizadorLexico.contadorLineas + ": identificador fuera de rango, se renombro a: " + cadena.valor);
			}
			tipo_token.valor = 2;

			
			Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
	        boolean isKeyPresent = false;
	        while (iterator.hasNext()) {
	            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
	            if (cadena.valor == entry.getKey()) {
	                isKeyPresent = true;
	                return null;
	            }
	        }
	        
	        
	        if (isKeyPresent == false) {
	        	analizadorLexico.tabla_simbolos.put(cadena.valor, new HashMap<String,String>()); //Por ahora no guardamos ningun atributo!
	        	analizadorLexico.tabla_simbolos.get(cadena.valor).put("linea",String.valueOf(analizadorLexico.contadorLineas)); //EJEMPLO
	        }
			
		}
		return null;
	}

}
