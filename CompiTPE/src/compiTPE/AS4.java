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
		int aux= Integer.parseInt(cadena.valor);
		int limite_sup= (int) (Math.pow(2, 15)-1);
		int limite_inf= (int) (-Math.pow(2, 15));
		if (aux>limite_sup || aux<limite_inf) {
			//warning
		}
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
		return null;
	}

}
