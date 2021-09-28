package compiTPE;

public class AnalizadorSintactico {
	public AnalizadorLexico analizadorLexico;
	
	public AnalizadorSintactico (AnalizadorLexico analizadorLexico) {
		this.analizadorLexico = analizadorLexico;
	}
	
	
	public void consumirTokens(String path_programa) {
		this.analizadorLexico.setPrograma(path_programa);
		StringHolder yylval=new StringHolder();
		int iterador_tokens = this.analizadorLexico.yylex(yylval); //Busca el primer elemento o null si el archivo es vacio y no tiene que hacer nada
		while (iterador_tokens != 0) {
			//hacer cosas
			System.out.println("[" + yylval + ", " + iterador_tokens + "]");
			
			// al final conseguimos el proximo token para la proxima iteracion
			iterador_tokens = this.analizadorLexico.yylex(yylval);
		}
	}

}
