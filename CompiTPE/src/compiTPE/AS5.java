package compiTPE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AS5 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, BooleanHolder eot, IntHolder tipo_token, StringHolder cadena,
			char caracter) {
		// TODO Auto-generated method stub
		eot.bool=true;
		boolean fuera_de_rango = false;
		tipo_token.valor=3;
		double base;
		double exp=1.0;
		String[] cadena_dividida= cadena.valor.split("S", 0);
		base= Double.parseDouble(cadena_dividida[0]);
		if(cadena_dividida.length>1){						//Si tiene S
			exp=Double.parseDouble(cadena_dividida[1]);
		}
		double valor= Math.pow(base, Math.pow(10,exp));
		if (valor == 0 && base != 0) {
			analizadorLexico.erroresLex.add("Error en la linea "+ analizadorLexico.contadorLineas + ": constante fuera de rango");
			fuera_de_rango = true;
			
		}
		double lim1= Math.pow(1.17549435, Math.pow(10,-38)); // 1.17549435S-38
		double lim2= Math.pow(3.40282347, Math.pow(10,38));// 3.40282347S+38
		double lim3= Math.pow(-3.40282347,Math.pow(10,38));//-3.40282347S+38 
		double lim4= Math.pow(-1.17549435,Math.pow(10,-38));//-1.17549435S-38
		if(valor<lim1 || valor>lim2) {
			if(valor<lim3 || valor>lim4) {
				if(valor!=0) {
					fuera_de_rango = true;
					analizadorLexico.erroresLex.add("Error en la linea "+ analizadorLexico.contadorLineas + ": constante fuera de rango, se descartara.");
			}}
		}else if(valor<lim3 || valor>lim4) {
			if(valor<lim1 || valor>lim2) {
				if(valor!=0) {

					analizadorLexico.erroresLex.add("Error en la linea "+ analizadorLexico.contadorLineas + ": constante fuera de rango, se descartara.");					
				}
			}
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
	        	analizadorLexico.tabla_simbolos.put(cadena.valor, new HashMap<String,String>()); //Por ahora no guardamos ningun atributo!
	        	analizadorLexico.tabla_simbolos.get(cadena.valor).put("linea",String.valueOf(analizadorLexico.contadorLineas)); //EJEMPLO
	        }
		}
		return null;
	}
}
