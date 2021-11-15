package compiTPE;

public class Nodo extends ParserVal {

	public String nombre;
	public ParserVal izq;
	public ParserVal der;
	
	public Nodo(String s) {
		this.nombre=s;
		izq=null;
		der=null;
	}

	public Nodo(int s) {
		this.nombre=String.valueOf(s);
		izq=null;
		der=null;
	}

	public Nodo(String nombre, ParserVal izq, ParserVal der) {
		super();
		this.nombre = nombre;
		this.izq = izq;
		this.der = der;
	}
	
	public String toString() {
		return nombre;
	}
}
