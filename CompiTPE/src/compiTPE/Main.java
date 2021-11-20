package compiTPE;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		int matrizTransicionEstados[][] = {
		        {7,7,12,13,10,-2,-2,-2,1,4,5,6,7,11,8,9,0,0,7,-1},
		        {-2,-2,-2,-2,-2,-2,-2,-2,2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},  //F=-2  error=-1
		        {2,2,2,2,2,2,2,2,3,2,2,2,2,2,2,2,2,2,2,2},
		        {1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {7,7,7,-2,-2,-2,-2,-2,-2,-2,-2,-2,7,-2,-2,-2,-2,-2,7,-2},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2,-1,-1,-1,-1,-1},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2,-1,-1,-1,-1},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {11,11,11,11,11,11,11,11,11,11,11,11,11,-2,11,11,11,-1,11,11},
		        {-2,-2,12,13,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,13,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,14,-2},
		        {-1,-1,15,-1,-1,15,15,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		        {-2,-2,15,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2}
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
		accionSemantica AS8 = new AS8();
		accionSemantica AS9 = new AS9();
		accionSemantica AS10 = new AS10();
		accionSemantica ASXI = new ASXI();
		
		accionSemantica matrizAS[][]= {
				{AS1,AS1,AS1,AS1,AS1,AS10,AS10,AS10,AS1,AS1,AS1,AS1,AS1,AS7,AS1,AS1,null,ASX,AS1,null},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS2,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,ASXI,ASXI,AS2,AS2},
				{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,ASZ,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS9,AS9,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS9,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS9,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS2,AS2,AS2,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS3,AS2,AS3,AS3,AS3,AS3,AS3,AS2,AS3},
				{null,null,null,null,null,null,null,null,null,null,null,null,null,null,AS9,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,AS9,null,null,null,null},
				{AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS9,AS6,AS6,AS6,AS6,AS6,AS6,AS6,AS6},
				{AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS2,AS8,AS2,AS2,AS2,null,AS2,AS2},
				{AS4,AS4,AS2,AS2,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4,AS4},
				{AS5,AS5,AS2,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS2,AS5},
				{null,null,AS2,null,null,AS2,AS2,null,null,null,null,null,null,null,null,null,null,null,null,null},
				{AS5,AS5,AS2,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5,AS5}
		};
		
		ErrorHandler e_h = new ErrorHandler();
		System.out.println("Ingrese el nombre del programa (en formato txt) que quiera compilar:");
		Scanner sc=new Scanner(System.in); 
		String programa = sc.nextLine();
		sc.close();
		programa = programa + ".txt";
		boolean program_exists = false;
		String programtext = "";
		try {
			programtext = new Scanner(new File(programa)).useDelimiter("\\Z").next();
			program_exists = true;
		} catch (Exception e) {
			System.out.println("No se encontro el archivo buscado");
			program_exists = false;
		}
		if (program_exists) {
			AnalizadorLexico a_lex = new AnalizadorLexico("palabras_predefinidas.txt", matrizTransicionEstados, matrizAS, e_h);
	        a_lex.setPrograma(programtext);
	        Parser newParser = new Parser(a_lex);
	        newParser.run();
	        
	        System.out.println("\n\nINFORME TOKENS");
	        for (String s: a_lex.informeTokens) {
	        	System.out.print(s+ ", ");
	        }
	        System.out.println("\n\nINFORME REGLAS");
	        for (String s: newParser.reglas) {
	        	System.out.print(s + ", ");
	        }
	        System.out.println("\n\nERRORES LEXICOS");
	        for (String s: a_lex.erroresLex) {
	        	System.out.println(s);
	        }
	        System.out.println("\n\nERRORES SINTACTICOS");
	        for (String s: newParser.erroresSint) {
	        	System.out.println(s);
	        }
	        System.out.println("\n\n\nTABLA DE SIMBOLOS");
	        System.out.println(a_lex.tabla_simbolos);
	        System.out.println("\nPalabras Predefinidas");
	        System.out.println(a_lex.palabras_predefinidas);

//	        if (a_lex.erroresLex.isEmpty() && newParser.erroresSint.isEmpty())
//	        {
	        	CodeGenerator cg_assembler = new CodeGenerator(newParser.raiz, newParser);
	        	cg_assembler.run();
//	        }
//	        else {
//	        	System.out.println("Se han encontrado errores lexicos y/o sintacticos en la compilación del programa, por lo que no ha sido compilado.");
//  		        System.out.println("\n\nERRORES LEXICOS");
//		        for (String s: a_lex.erroresLex) {
//		        	System.out.println(s);
//		        }
//		        System.out.println("\n\nERRORES SINTACTICOS");
//		        for (String s: newParser.erroresSint) {
//		        	System.out.println(s);
//		        }
//		        System.out.println("\n\n\nTABLA DE SIMBOLOS");
//		        System.out.println(a_lex.tabla_simbolos);
//		        System.out.println("\nPalabras Predefinidas");
//		        System.out.println(a_lex.palabras_predefinidas);
//	        }
		}
		
	}

}