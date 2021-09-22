package compiTPE;

public class AnalizadorSintactico {
	public AnalizadorLexico analizadorLexico;
	
	public AnalizadorSintactico (AnalizadorLexico analizadorLexico) {
		this.analizadorLexico = analizadorLexico;
	}
	
	
	public void consumirTokens(String path_programa) {
		this.analizadorLexico.setPrograma(path_programa);
		Tuple<String,Integer> iterador_tokens = this.analizadorLexico.getToken(); //Busca el primer elemento o null si el archivo es vacio y no tiene que hacer nada
		while (iterador_tokens != null) {
			//hacer cosas
			System.out.println("[" + iterador_tokens.x + ", " + iterador_tokens.y + "]");
			
			// al final conseguimos el proximo token para la proxima iteracion
			this.analizadorLexico.getToken();
		}
	}

}
