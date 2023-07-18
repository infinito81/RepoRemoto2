package dominio;

import java.net.HttpURLConnection;
import java.util.logging.Logger;

import estaticos.Dorsales;
import util.EnviosHTTP;

public class ResultadosAsincrona extends Thread {
	


	int lector;
	int evento;
	int carrera;
	String tagLeido;
	String tiempoFinal;
	long timeInsert;
	String dorsal;
	String tipoLectura;
		
	public ResultadosAsincrona(int lector, int evento, int carrera, String tipoLectura, String tagLeido, String dorsal, String tiempoFinal, long timeInsert) {
		super();
		this.evento = evento;
		this.carrera = carrera;
		this.tagLeido = tagLeido;
		this.tiempoFinal = tiempoFinal;
		this.lector = lector;
		this.dorsal = dorsal;
		this.timeInsert = timeInsert;
		this.tipoLectura = tipoLectura;
	}

	
	public ResultadosAsincrona(int evento, int carrera) {
		super();
		this.evento = evento;
		this.carrera = carrera;
	}


	@Override
	public void run() {
		//System.out.println("envío resultado de lector " + lector + ": " + tagLeido);
		
		//dorsal = Dorsales.hashUhfTagDorsales.get(tagLeido);	
		
		if (dorsal != null) {
			enviaResultado(dorsal);
		} else {
			System.out.println("no envío. lectura a null");
		}
		
		//Resultados.guardarResultado(carrera, dorsal, tiempoFinal);	
	}	
	
	public void enviaResultado (String dorsal) {
		StringBuffer stb = new StringBuffer();
		stb.append("http://popularia.es/registro_lectura_get.php?action=agregar");
		stb.append("&evento=");
		stb.append(evento);
		stb.append("&carrera=");
		stb.append(carrera);
		stb.append("&tipo=");
		stb.append(tipoLectura);
		stb.append("&tag=");
		stb.append(tagLeido);
		stb.append("&dorsal=");
		stb.append(dorsal);
		stb.append("&tiempo_final=");
		stb.append(tiempoFinal);
		stb.append("&fecha_insert=");
		stb.append(timeInsert);		
		
		System.out.println(stb.toString());
		try {
			String respuesta = EnviosHTTP.sendGet(stb.toString());
			if (respuesta.contains("CORRECTO")) respuesta = "CORRECTO";
			if (respuesta.contains("FALLO")) respuesta = "FALLO";
			//System.out.println(respuesta);
		} catch (Exception e) {

		}
	}


	public void enviarInicioCarrera(long tiempoInicial) {
		StringBuffer stb = new StringBuffer();
		stb.append("http://popularia.es/android/resultados_get.php?");
		stb.append("action=inicializar");
		stb.append("&evento=");
		stb.append(evento);
		stb.append("&carrera=");
		stb.append(carrera);	
		stb.append("&marca_inicio="); 
		stb.append(tiempoInicial);
		
		System.out.println(stb.toString());
		try {
			String respuesta = EnviosHTTP.sendGet(stb.toString());
			if (respuesta.contains("CORRECTO")) respuesta = "CORRECTO";
			if (respuesta.contains("FALLO")) respuesta = "FALLO";
			System.out.println(respuesta);
		} catch (Exception e) {

		}		
		
	}
}
