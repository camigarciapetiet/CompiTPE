package compiTPE;

import java.util.*;

public class AS8 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token,
			StringHolder token_actual, char caracter) {
		eot.bool=true;
		tipo_token.valor=5;
		analizadorLexico.programa = analizadorLexico.programa.substring(1);
		
		//busca si esta en la tabla de simbolos
		Iterator<Map.Entry<String, HashMap<String,String>>>iterator = analizadorLexico.tabla_simbolos.entrySet().iterator();
        boolean isKeyPresent = false;
        while (iterator.hasNext()) {
            Map.Entry<String, HashMap<String,String>> entry = iterator.next();
            if (token_actual.valor == entry.getKey()) {
                isKeyPresent = true;
                return null;
            }
        }
        
        //si no esta la agrega
        if (isKeyPresent == false) {
        	analizadorLexico.tabla_simbolos.put(token_actual.valor, new HashMap<String,String>()); //Por ahora no guardamos ningun atributo!
        	//analizadorLexico.tabla_simbolos.get(token_actual.valor).put("linea",String.valueOf(analizadorLexico.contadorLineas)); //EJEMPLO
        	analizadorLexico.tabla_simbolos.get(token_actual.valor).put("uso","cadena"); 

        }
		return null;
	}

}
