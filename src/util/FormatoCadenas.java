package util;

public class FormatoCadenas {

	public static String formateaParaPHP(String cadena) {
		cadena = cadena.replaceAll("Á", "-q");
		cadena = cadena.replaceAll("É", "-w");
		cadena = cadena.replaceAll("Í", "-x");
		cadena = cadena.replaceAll("Ó", "-y");
		cadena = cadena.replaceAll("Ú", "-z");
		cadena = cadena.replaceAll(" ", "-e");
		
		return cadena;
	}

}
