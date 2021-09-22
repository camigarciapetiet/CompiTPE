package compiTPE;

public class AS5 extends accionSemantica {

	@Override
	public String ejecutar(AnalizadorLexico analizadorLexico, boolean eot, int tipo_token, String cadena,
			char caracter) {
		// TODO Auto-generated method stub
		eot=true;
		tipo_token=3;
		double base;
		double exp=1.0;
		String[] cadena_dividida= cadena.split("S", 0);
		base= Double.parseDouble(cadena_dividida[0]);
		if(cadena_dividida.length>1){						//Si tiene S
			exp=Double.parseDouble(cadena_dividida[1]);
		}
		double valor= Math.pow(base, exp);
		double lim1= Math.pow(1.17549435, -38); // 1.17549435S-38
		double lim2= Math.pow(3.40282347, 38);// 3.40282347S+38
		double lim3= Math.pow(-3.40282347, 38);//-3.40282347S+38 
		double lim4= Math.pow(-1.17549435, -38);//-1.17549435S-38
		if(valor<lim1 || valor>lim2 || valor<lim3 || valor>lim4) {
			if(valor!=0) {
				//warning
			}
		}
		/*if(analizadorLexico.tabla_sibolos.) {//si no pertenece a la tabla de simbolos
			analizadorLexico.tabla_simbolos.put(cadena, new HashMap<String,String>());//dar de alta en la tabla si no esta
		}*/
		return null;
	}

	/*public static void main(String[] args) {
		System.out.println(Double.parseDouble("-1"));
	}*/
}
