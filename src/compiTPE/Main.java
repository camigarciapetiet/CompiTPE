package compiTPE;

public class Main {

	public static void main(String[] args) {
		accionSemantica[] a= new accionSemantica[9];
		AS1 as1=new AS1();
		a[1]=as1;
		System.out.println(a[1].ejecutar("", 'c'));
	}

}
