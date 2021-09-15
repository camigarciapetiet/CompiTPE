package compiTPE;

import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class Main {
	
	public static int convertAscii(char ch) {
		return ((int) ch);
	}
	
	public static void cargarPalabrasReservadas(Map<String,Integer> palabras_reservadas, int codigo) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader( "palabras_reservadas.txt"));
			String line = reader.readLine();
			while (line != null) {
				palabras_reservadas.put(line, codigo);
				codigo +=1;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		accionSemantica[] a = new accionSemantica[9];
		AS1 as1=new AS1();
		a[1]=as1;
		System.out.println(a[1].ejecutar("", 'c'));
		
		// Cargar MAPA PALABRAS_RESERVADAS
		int indice_Codigo = 257;
		Map<String, Integer> palabras_reservadas = new HashMap<String,Integer>();
		cargarPalabrasReservadas(palabras_reservadas, indice_Codigo);
		palabras_reservadas.entrySet().forEach(entry -> {
		    System.out.println(entry.getKey() + " " + entry.getValue());
		});
		//
		
		// Leer CARACTER POR CARACTER de un txt
		File file = new File("testcases.txt");
	       try (FileReader fr = new FileReader(file))
	        {
	    	   int content;
	           while ((content = fr.read()) != -1) {
	               System.out.print((char) content); //ACA VA LO Q HACEMOS CON CADA CHAR
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	       
		
		int matrizTransicionEstados[][] = {
		        {7,7,12,13,10,-2,-2,-2,1,4,5,6,7,11,8,9,-2,0,0,7,-1},
		        {-2,-2,-2,-2,-2,-2,-2,-2,2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,3,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {7,7,7,-2,-2,-2,-2,-2,-2,-2,-2,-2,7,-2,-2,-2,-2,-2,-2,7,-2},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2,-1,-1,-1,-1,-1,-1},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2,-1,-1,-1,-1,-1},
		        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		        {11,11,11,11,11,11,11,11,11,11,11,11,11,-2,11,11,11,11,11,11,11},
		        {-2,-2,12,13,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2},
		        {-2,-2,13,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,14,-2},
		        {-1,-1,15,-1,-1,15,15,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
		        {-2,-2,15,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2}
		    };
		
		accionSemantica matrizAS[][];
		
		
	}

}
