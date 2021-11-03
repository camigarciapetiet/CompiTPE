package compiTPE;

public class Nodo {

	String nombre;
	Nodo izq;
	Nodo der;
	
	public Nodo(String s) {
		this.nombre=s;
		izq=null;
		der=null;
	}

	public Nodo(String nombre, Nodo izq, Nodo der) {
		super();
		this.nombre = nombre;
		this.izq = izq;
		this.der = der;
	}
	
	public void setHijos(Nodo izq, Nodo der) {
		this.izq=izq;
		this.der=der;
	}
}
