package compiTPE;

public class Nodo extends ParserVal {

	public String nombre;
	public Object izq;
	public Object der;
	
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

	public Nodo(String nombre, Object izq, Object der) {
		super();
		this.nombre = nombre;
		this.izq = izq;
		this.der = der;
	}
		
	public void setHijos(Object izq, Object der) {
		this.izq=izq;
		this.der=der;
	}
}
