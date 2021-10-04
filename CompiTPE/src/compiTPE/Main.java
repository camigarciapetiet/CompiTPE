package compiTPE;

public class Main {

	public static void main(String[] args) {

		int matrizTransicionEstados[][] = {
		        {7,7,12,13,10,16,16,16,1,4,5,6,7,11,8,9,0,0,7,-1},
		        {-2,-2,-2,-2,-2,-2,-2,-2,2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},  //F=-2  error=-1
		        {2,2,2,2,2,2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},
		        {1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,18,17,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,19,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,20,-2,-2,-2,-2,-2,-2,-2,-2},
		        {7,7,7,-2,-2,-2,-2,-2,-2,-2,-2,-2,7,-2,-2,-2,-2,-2,7,-2},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,21,-1,-1,-1,-1,-1},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,22,-1,-1,-1,-1},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,23,-1,-1,-1,-1,-1,-1,-1,-1},
		        {11,11,11,11,11,11,11,11,11,11,11,11,11,24,11,11,11,-1,11,11},
		        {-2,-2,12,13,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,13,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,14,-2},
		        {-1,-1,15,-1,-1,15,15,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		        {-2,-2,15,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2}
		    };
		accionSemantica AS1= new AS1();
		accionSemantica AS2= new AS2();
		accionSemantica AS3= new AS3();
		accionSemantica AS4= new AS4();
		accionSemantica AS5= new AS5();
		accionSemantica AS6= new AS6();
		accionSemantica ASX= new ASX();
		accionSemantica ASZ= new ASZ();
		accionSemantica AS7 = new AS7();
		
		accionSemantica matrizAS[][]= {
				{AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,AS1,null,ASX,AS1,null},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS2,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2},
				{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,ASZ,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS2,AS2,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS2,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS2,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS2,AS2,AS2,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS2,AS3,AS3,AS3,AS3,AS3,AS2,AS3},
				{null,null,null,null,null,null,null,null,null,null,null,null,null,null,AS2,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,AS2,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null,null,AS2,null,null,null,null,null,null,null,null},
				{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,null,AS2,AS2},
				{AS4,AS4,AS2,AS2,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4},
				{AS5,AS5,AS2,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS2,AS5},
				{null,null,AS2,null,null,AS2,AS2,null,null,null,null,null,null,null,null,null,null,null,null,null},
				{AS5,AS5,AS2,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3},
				{AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3},
				{AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3},
				{AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3},
				{AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3},
				{AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3},
				{AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3},
				{AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7,AS7}
		};
		
		ErrorHandler e_h = new ErrorHandler();
        AnalizadorLexico a_lex = new AnalizadorLexico("palabras_predefinidas.txt", matrizTransicionEstados, matrizAS, e_h);
        //AnalizadorSintactico a_sint = new AnalizadorSintactico(a_lex);
        a_lex.setPrograma("testcases.txt");
        Parser newParser = new Parser(a_lex);
        newParser.run();
        for (String s: a_lex.erroresLex) {
        	System.out.println(s);
        }
        for (String s: newParser.erroresSint) {
        	System.out.println(s);
        }
        System.out.println("\nTABLA DE SIMBOLOS");
        System.out.println(a_lex.tabla_simbolos);
        System.out.println("\nPalabras Predefinidas");
        System.out.println(a_lex.palabras_predefinidas);
        System.out.println("termino");
		
	}

}
