package compiTPE;

public class Nodo extends ParserVal implements Cloneable {

	public String nombre;
	public ParserVal izq;
	public ParserVal der;
	public String tipo;
	
	public Nodo(String s) {
		this.nombre=s;
		izq=null;
		der=null;
		tipo=null;
	}

	public Nodo(int s) {
		this.nombre=String.valueOf(s);
		izq=null;
		der=null;
		tipo=null;
	}

	public Nodo(String nombre, ParserVal izq, ParserVal der) {
		super();
		this.nombre = nombre;
		this.izq = izq;
		this.der = der;
		tipo=null;
	}

	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public String toString() {
		return nombre;
	}
	public ParserVal getIzq() {
		return izq;
	}
	public ParserVal getDer() {
		return der;
	}
	
	public Object clone(){
	    try{
	        return super.clone();
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	
	public boolean nodoAux() {
		if(nombre=="Programa" || nombre=="S" || nombre=="condicion" ||nombre=="cuerpo")
			return true;
		return false;
	}
	
	public boolean esHoja() {
		Nodo izq = null;
		Nodo der = null;
		try {
			izq = (Nodo)this.izq.obj; //Cast de OBJ a nodo
			der = (Nodo)this.der.obj; //Cast de OBJ a nodo
		} catch (Exception e) {}
		if (der==null && izq==null)
			return true;
		return false;
	}

	public boolean esRegistro() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getTipoHijoDer(Nodo n) {
		System.out.println(n);
		if (!n.esHoja()) {
			Nodo nododer=null;
			Nodo nodoizq=null;
			try {
				nododer = (Nodo)n.der.obj; //Cast de OBJ a nodo
				nodoizq = (Nodo)n.izq.obj; //Cast de OBJ a nodo
			} catch (Exception e) {}
			if(nododer!=null)
				return getTipoHijoDer(nododer);
			else if(nodoizq!=null)
				return getTipoHijoDer(nodoizq);
			/*else
				return "";*/
		}else {
			return n.nombre;
		}
			
	}
}
