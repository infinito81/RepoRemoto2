package util;

public class FormatoCadenas {

	public static String formateaParaPHP(String cadena) {
		cadena = cadena.replaceAll("�", "-q");
		cadena = cadena.replaceAll("�", "-w");
		cadena = cadena.replaceAll("�", "-x");
		cadena = cadena.replaceAll("�", "-y");
		cadena = cadena.replaceAll("�", "-z");
		cadena = cadena.replaceAll(" ", "-e");
		
		return cadena;
	}

}
