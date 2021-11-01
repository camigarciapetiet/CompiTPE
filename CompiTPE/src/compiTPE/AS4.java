package compiTPE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AS4 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena,
			char caracter) {
		// TODO Auto-generated method stub
		eot.bool = true;
		tipo_token.valor=3;
		boolean fuera_de_rango = false;
		long aux= Long.parseLong(cadena.valor);
		int limite_sup= (int) (Math.pow(2, 15)-1);
		int limite_inf= (int) (-Math.pow(2, 15));
		if (aux>limite_sup || aux<limite_inf) {
			analizadorLexico.erroresLex.add("Error en la linea "+ analizadorLexico.contadorLineas + ": constante fuera de rango, se descartara.");
			fuera_de_rango = true;
		}
		
		if (fuera_de_rango) {
			eot.bool = false;
			cadena.resetValor();
			analizadorLexico.programa = " " + analizadorLexico.programa;
			analizadorLexico.estado_actual=0;
		}
		else {
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
	        	analizadorLexico.tabla_simbolos.put(cadena.valor, new HashMap<String,String>());
	        	//analizadorLexico.tabla_simbolos.get(cadena.valor).put("linea",String.valueOf(analizadorLexico.contadorLineas)); 
	        	
	        	analizadorLexico.tabla_simbolos.get(cadena.valor).put("tipo","INT");
	        	analizadorLexico.tabla_simbolos.get(cadena.valor).put("uso","constante"); 

	        }
		}
		return null;
	}

}
