package dominio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import estaticos.Log;

import persistencia.Agente;

import persistencia.dao.model.TEventos;

public class Eventos {

	private static Logger log = Log.getLogger(Eventos.class);
	
	public static int guardarEvento(TEventos evento) {
		StringBuffer queryActualizacion = new StringBuffer();
		queryActualizacion.append("INSERT INTO eventos VALUES (");

		queryActualizacion.append(evento.getIdEvento());
		queryActualizacion.append(",'" + evento.getNombreEvento() + "'");
		queryActualizacion.append(",'" + evento.getFechaEvento() + "'");
		
		queryActualizacion.append(");");
		
		System.out.println(queryActualizacion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			int resultado = agente.insert(queryActualizacion.toString());
			return resultado;
		} catch (Exception e) {
			System.out.println("FALLO AL INSERTAR el evento: " + evento.getIdEvento());
			return -1;
		}		
	}
	
	
	public static ArrayList<TEventos> getTodosEventos (){
		ArrayList<TEventos> lEventos = new ArrayList<TEventos> ();
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM eventos ORDER BY fechaEvento DESC;";
			System.out.println(query);
			resultSet = agente.select(query);
			
			while (resultSet.next()) {
				TEventos evento = new TEventos();
				evento.setIdEvento(resultSet.getInt("idEvento"));
				evento.setFechaEvento(resultSet.getString("fechaEvento"));
				evento.setNombreEvento(resultSet.getString("nombreEvento"));
				lEventos.add(evento);				
			}

		} catch (Exception e) {
			System.out.println("FALLO AL TODOS LOS EVENTOS ");
			return null;
		}	
		
		return lEventos;
	}

	
	public static TEventos getEvento (int idEvento){
		TEventos evento = null;
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM eventos where idEvento = " + idEvento + ";";
			//System.out.println(query);
			resultSet = agente.select(query);
			
			while (resultSet.next()) {
				evento = new TEventos();
				evento.setIdEvento(resultSet.getInt("idEvento"));
				evento.setFechaEvento(resultSet.getString("fechaEvento"));
				evento.setNombreEvento(resultSet.getString("nombreEvento"));
								
			}

		} catch (Exception e) {
			System.out.println("FALLO AL TODOS LOS EVENTOS ");
			return null;
		}	
		
		return evento;
	}
	
	public static String dameCarrerasServidor(int eventoSeleccionado) {
        String datos = "password=rocioysergio";
        String [] strings = new String[]{"http://popularia.es/android/get_carreras_solo.php",datos};
        HttpURLConnection con = null;
		
        String data = strings[1];

        StringBuilder output = new StringBuilder();
        StringBuilder responseOutput= new StringBuilder();
        try {
        	URL url = new URL(strings[0]);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("USER-AGENT", "Mozilla/5.0");
			con.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

			con.setDoOutput(true);
			DataOutputStream dStream = new DataOutputStream(con.getOutputStream());
			dStream.writeBytes(data);
			dStream.flush();
			dStream.close();
			int responseCode = con.getResponseCode();
			output = new StringBuilder("Request URL " + url);
			output.append(System.getProperty("line.separator") + "Request Parameters " + data);
			output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
			output.append(System.getProperty("line.separator") + "Type " + "POST");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
			String line = "";
			responseOutput = new StringBuilder();
			System.out.println("output===============" + br);
			while ((line = br.readLine()) != null) {
				responseOutput.append(line);
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
        log.debug("ENVIOS_SALIDA" + output.toString());
        return responseOutput.toString();
	}	
}
