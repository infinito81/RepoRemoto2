package dominio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import estaticos.Log;
import persistencia.Agente;
import persistencia.dao.model.TCorredores;
import persistencia.dao.model.TLecturas;
import persistencia.dao.model.TResultados;
import util.Estatica;
import util.FormatoFecha;

public class Lecturas {
	
	private static Logger log = Log.getLogger(Lecturas.class);
	
	public static int guardaLectura(int idEvento, int idCarrera, String tipoLectura, String sDorsal, String tag, String tiempoFinal, long fechaInsert) {
		TLecturas lectura = new TLecturas();
		int resultado = -1;
		
		StringBuffer queryInsercion = new StringBuffer();
		queryInsercion.append("INSERT INTO LecturaDorsal VALUES (");
		queryInsercion.append( " NULL,");
		queryInsercion.append( idEvento + ",");
		queryInsercion.append(idCarrera + ",'");
		queryInsercion.append(tipoLectura + "',");
		queryInsercion.append(tag != null ? "'" + tag + "'," : "NULL,");
		queryInsercion.append(sDorsal != null ? sDorsal + "," : "NULL,");
		
		queryInsercion.append(fechaInsert <= 0 ? Calendar.getInstance().getTimeInMillis() + ",'" : fechaInsert + ",'");
		queryInsercion.append(tiempoFinal);
		queryInsercion.append("');");		
		
		fechaInsert = fechaInsert <= 0 ? Calendar.getInstance().getTimeInMillis() : fechaInsert;
		
      	//System.out.println(queryInsercion.toString()); 
      	//System.out.println("INSERTO dorsal " + sDorsal + ". Tiempo: " + tiempoFinal + ". FechaInMillis: " + fechaInsert);
      	log.debug(queryInsercion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			resultado=agente.insert(queryInsercion.toString());
			System.out.println("Resultado inserción lectura: " + resultado);
			
		} catch (Exception e) {
			System.out.println("FALLO AL INSERTAR UNA LECTURA: dORSAL --> " + sDorsal + "Evento --> " + idEvento + " tiempo final --> " + tiempoFinal);
		}		
		
		return resultado;
	}
	
	public static TLecturas anyadirHashMapDinamico (int idEvento, int idCarrera, String tipoLectura, String sDorsal, String tag, String tiempoFinal, long fechaInsert) {
		TLecturas tLectura = null;
		//Lo añado al hashmap dinámico
		try {

			Integer dorsal = Integer.parseInt(sDorsal);
			
			tLectura = new TLecturas();
			tLectura.setIdEvento(idEvento);
			tLectura.setIdCarrera(idCarrera);
			tLectura.setTipoLectura(tipoLectura);
			tLectura.setTagLeido(tag);
			tLectura.setDorsalLeido(dorsal);
			tLectura.setFechaInsert(fechaInsert <= 0 ? Calendar.getInstance().getTimeInMillis() : fechaInsert);
			tLectura.setTiempoFinal(tiempoFinal);
			
			if (Estatica.getInstance().getLecturasTotalesDorsales().containsKey(dorsal)) {
				Estatica.getInstance().getLecturasTotalesDorsales().get(dorsal).add(tLectura);
			} else {
				ArrayList<TLecturas> listaLecturas = new ArrayList<TLecturas>();
				listaLecturas.add(tLectura);
				Estatica.getInstance().getLecturasTotalesDorsales().put(dorsal, listaLecturas);
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}	
		
		return tLectura;
	}
	
	public static TLecturas anyadirHashMapDinamico (TLecturas lectura) {
		//TLecturas tLectura = null;
		//Lo añado al hashmap dinámico
		try {

			Integer dorsal = lectura.getDorsalLeido();
			
//			tLectura = new TLecturas();
//			tLectura.setIdEvento(idEvento);
//			tLectura.setIdCarrera(idCarrera);
//			tLectura.setTipoLectura(tipoLectura);
//			tLectura.setTagLeido(tag);
//			tLectura.setDorsalLeido(dorsal);
//			tLectura.setFechaInsert(fechaInsert <= 0 ? Calendar.getInstance().getTimeInMillis() : fechaInsert);
//			tLectura.setTiempoFinal(tiempoFinal);
			
			if (Estatica.getInstance().getLecturasTotalesDorsales().containsKey(dorsal)) {
				Estatica.getInstance().getLecturasTotalesDorsales().get(dorsal).add(lectura);
			} else {
				ArrayList<TLecturas> listaLecturas = new ArrayList<TLecturas>();
				listaLecturas.add(lectura);
				Estatica.getInstance().getLecturasTotalesDorsales().put(dorsal, listaLecturas);
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}	
		
		return lectura;
	}

	public static ArrayList<TLecturas> recuperarLecturas (int idEvento, int idCarrera) {
		ArrayList<TLecturas> lecturas = new ArrayList<TLecturas>();
		
		TLecturas auxiliar = null;
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM LecturaDorsal WHERE idEvento = " + idEvento + " AND idCarrera = " + idCarrera + " and dorsalLeido is not null order by fechaInsert;";
			System.out.println(query);
			resultSet = agente.select(query);
			
			
			while (resultSet!=null && resultSet.next()){
				auxiliar = new TLecturas();
				auxiliar.setIdEvento(idEvento);
				auxiliar.setIdCarrera(idCarrera);
				auxiliar.setTipoLectura(resultSet.getString("tipoLectura"));
				auxiliar.setTagLeido(resultSet.getString("tagLeido"));
				auxiliar.setDorsalLeido(resultSet.getInt("dorsalLeido"));
				auxiliar.setFechaInsert(resultSet.getLong("fechaInsert"));
				auxiliar.setTiempoFinal(resultSet.getString("tiempoFinal"));
				
				lecturas.add(auxiliar);
			}
			
			return lecturas;
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR LECTURAS DEL EVENTO " + idEvento + " y carrera " + idCarrera);
			return null;
		}			
	}
	
	public static String getLecturasServidor(int evento, int idCarreraSeleccionada){
	       String datos = "evento=" + evento + "&carrera=" + idCarreraSeleccionada;
	        String [] strings = new String[]{"http://popularia.es/android/get_lecturas_evento.php",datos};
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
				
				while ((line = br.readLine()) != null) {
					responseOutput.append(line);
				}
				br.close();
				System.out.println("output===============" + responseOutput.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
			output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
	        log.debug("GET LECTURAS SERVIDOR " + output.toString());
	        return responseOutput.toString();		
	}
	
	public static ArrayList<TLecturas> procesaLecturas (String s, int evento) {
        StringTokenizer st = new StringTokenizer(s, ";");
        ArrayList<TLecturas> lLecturas = new ArrayList<TLecturas>();
        TLecturas auxiliar = null;
        while(st.hasMoreTokens()) {
        	
        	
        	
            String idCarrera = st.nextToken();
            String tipoLectura = st.nextToken();
            String tagLeido = st.nextToken();
            String dorsalLeido = st.nextToken();
            String fechaInsert = st.nextToken();
            long longFechaInsert = FormatoFecha.stringDateToLong(fechaInsert);
            String tiempoFinal = st.nextToken();
            
                      
            auxiliar = new TLecturas();
			auxiliar.setIdEvento(evento);
			auxiliar.setIdCarrera(Integer.parseInt(idCarrera));
			auxiliar.setTipoLectura(tipoLectura);
			auxiliar.setTagLeido(tagLeido);
			auxiliar.setDorsalLeido(Integer.parseInt(dorsalLeido));
			auxiliar.setFechaInsert(longFechaInsert);
			auxiliar.setTiempoFinal(tiempoFinal);
			
			
            lLecturas.add(auxiliar);            
            
            //guardaLectura(evento, Integer.parseInt(idCarrera), tipoLectura, dorsalLeido, tagLeido, tiempoFinal, longFechaInsert);
        }
        
        return lLecturas;
	}
	
	
}
