package compiTPE;

public class StringHolder {
	public String valor;
	
	public void addValor(char ch) {
		this.valor = valor + ch;
	}
	
	public void resetValor() {
		this.valor = "";
	}
	
	public void set(String s) {
		this.valor=s;
	}
	
	public String toString() {
		return valor;
	}
}
