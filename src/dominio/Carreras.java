package dominio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import estaticos.Log;
import persistencia.Agente;
import persistencia.dao.model.TCarreras;
import persistencia.dao.model.TCategorias;
import persistencia.dao.model.TEventos;
import persistencia.dao.model.TLecturas;
import persistencia.dao.model.TResultados;
import util.FormatoFecha;
import util.UtilFicheros;

public class Carreras {
	private static Logger log = Log.getLogger(Carreras.class);

	public static int guardarTiempoInicial(int idEvento, int idCarrera, long tiempoInicial) {
		StringBuffer queryActualizacion = new StringBuffer();
		queryActualizacion.append("UPDATE carreras SET");

		queryActualizacion.append(" inicio = " + tiempoInicial + " ");
		queryActualizacion.append(" WHERE idEvento = " + idEvento + " AND idCarrera = " + idCarrera + ";");
		
		System.out.println(queryActualizacion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			agente.update(queryActualizacion.toString());
			return 1;
		} catch (Exception e) {
			System.out.println("FALLO AL ACTUALIZAR el tiempo inicial del idCarrera: " + idCarrera + " y evento " + idEvento);
			return -1;
		}		
	}
	
	public static TCarreras getCarreraPorId (int idEvento, int idCarrera){
		TCarreras carrera = new TCarreras ();
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM carreras WHERE idEvento = " + idEvento + " AND idCarrera = " + idCarrera + ";";
			System.out.println(query);
			resultSet = agente.select(query);
			carrera.setID_CARRERA(resultSet.getInt("idCarrera"));
			carrera.setHORA(resultSet.getString("hora"));
			carrera.setCATEGORIA(resultSet.getString("categoria"));
			carrera.setINICIO(resultSet.getLong("inicio"));

			return carrera;
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR CARRERA " + idCarrera);
			return null;
		}			
	}
	
	public static int guardarCarrera(TCarreras carrera) {
		StringBuffer queryActualizacion = new StringBuffer();
		queryActualizacion.append("INSERT INTO carreras VALUES (");
		queryActualizacion.append(carrera.getID_EVENTO());
		queryActualizacion.append("," + carrera.getID_CARRERA());
		queryActualizacion.append(",'" + carrera.getHORA() + "'");
		queryActualizacion.append(",'" + carrera.getCATEGORIA() + "'");
		if (carrera.getINICIO() > 0 )
			queryActualizacion.append("," + carrera.getINICIO());
		else 
			queryActualizacion.append(",NULL");
		queryActualizacion.append(");");
		
		System.out.println(queryActualizacion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			int resultado = agente.insert(queryActualizacion.toString());
			return resultado;
		} catch (Exception e) {
			System.out.println("FALLO AL INSERTAR la carrera: " + carrera.getID_CARRERA());
			return -1;
		}		
	}
	
	/*
	 * devuelve nombre evento, numCategorias x categorías (idCategoria, descCategoria, numPasos)
	 */
	public static String getDatosEvento(int evento){
	       String datos = "evento=" + evento;
	        String [] strings = new String[]{"http://popularia.es/android/get_datos_generales_evento.php",datos};
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
				
			}
			output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
	        log.debug("GET LECTURAS SERVIDOR " + output.toString());
	        return responseOutput.toString();		
	}
	
	
	public static String getClasificaciones(int evento, int categoria){
	       String datos = "evento=" + evento + "&categoria=" + categoria + "&carrera=1";
	        String [] strings = new String[]{"http://popularia.es/android/get_clasificaciones.php",datos};
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
				
				System.out.println("http://popularia.es/android/get_clasificaciones.php?" + datos);

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
				
			}
			output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
	        log.debug("GET LECTURAS SERVIDOR " + output.toString());
	        return responseOutput.toString();		
	}

	public static long getTiempoInicial(int idEventoSeleccionado, int idCarrera) {
		
		return 0;
	}

	public static HashMap <String, TCategorias> procesaDatosEvento(String datosCarrera, int idEventoSeleccionado, int idCarreraSeleccionada) {
		 StringTokenizer st = new StringTokenizer(datosCarrera, ";");
		 
		 String nombreCarrera = st.nextToken();
		 System.out.println("nombre carrera: " + nombreCarrera);
		 
        HashMap <String, TCategorias> hmCategorias = new HashMap <String, TCategorias>();
        TCategorias auxiliar = null;
        while(st.hasMoreTokens()) {
        	
        	
            String idEvento = st.nextToken();
            String idCategoria = st.nextToken();
            String descCategoria = st.nextToken();
            String numPasos = st.nextToken();
            String distancia = st.nextToken();
            String retraso = st.nextToken();
            String idCarrera = st.nextToken();
            
            auxiliar = new TCategorias();
			auxiliar.setIdEvento(Integer.parseInt(idEvento));
			auxiliar.setIdCategoria(Integer.parseInt(idCategoria));
			auxiliar.setDescCategoria(descCategoria);
			auxiliar.setNumPasos(Integer.parseInt(numPasos));
			auxiliar.setDistancia(Integer.parseInt(distancia));
			auxiliar.setRetraso(Integer.parseInt(retraso));
			auxiliar.setIdCarrera(Integer.parseInt(idCarrera));
			
			
			
			if (idCarreraSeleccionada == auxiliar.getIdCarrera()) {
	            hmCategorias.put(idCategoria, auxiliar);           
	            guardaCategoria(idEvento, idCategoria, numPasos, descCategoria, distancia, retraso, idCarrera);				
			}

        }
        
        return hmCategorias;
		
	}
	
	public static void generaHTMLResultadosIndividuales(TEventos evento, String lineasClasificaciones, TCategorias categoria) {
		String stringHTML = UtilFicheros.leeFichero("./res/resultados/result.html");
		
		
		StringTokenizer st = new StringTokenizer(lineasClasificaciones, ";");
		
		while(st.hasMoreTokens()) {
            String posicion = st.nextToken();
            String dorsal = st.nextToken();
            String idCorredor = st.nextToken();
            String nombreApellidos = st.nextToken();
            String tiempo = st.nextToken();
            String ritmoKMH = st.nextToken();
            String ritmoMinKM = st.nextToken();
            
            String stringResultado = stringHTML.replace("#DORSAL#", dorsal);
            stringResultado = stringResultado.replace("#TIEMPO#", tiempo);
            
            if (ritmoKMH.equals("-")) {
            	stringResultado = stringResultado.replace("#RITMO#", ritmoKMH);
            } else {
            	if (evento.getIdEvento() != 85 && evento.getIdEvento() != 143 && evento.getIdEvento() != 145)
            		stringResultado = stringResultado.replace("#RITMO#", ritmoKMH.substring(0, ritmoKMH.lastIndexOf(" ")));
            	else
            		stringResultado = stringResultado.replace("#RITMO#", ritmoMinKM.substring(0, ritmoMinKM.lastIndexOf(" ")));
            }
            
            stringResultado = stringResultado.replace("#NOMBRE_CORREDOR#", nombreApellidos);
            stringResultado = stringResultado.replace("#POSICION#", posicion);
            stringResultado = stringResultado.replace("#CATEGORIA#", categoria.getDescCategoria());
            stringResultado = stringResultado.replace("#NOMBRE_CARRERA#", evento.getNombreEvento());
            stringResultado = stringResultado.replace("#DISTANCIA#", categoria.getDistancia() + " M");
            
            if (evento.getIdEvento() != 85 && evento.getIdEvento() != 143 && evento.getIdEvento() != 145)
            	stringResultado = stringResultado.replace("#UNIDADES_RITMO#", "KM/H");
            else	
            	stringResultado = stringResultado.replace("#UNIDADES_RITMO#", "MIN/KM");
            
            //para caracteres raros
            stringResultado = stringResultado.replace("Á", "&Aacute;");
            stringResultado = stringResultado.replace("É", "&Eacute;");
            stringResultado = stringResultado.replace("Í", "&Iacute;");
            stringResultado = stringResultado.replace("Ó", "&Oacute;");
            stringResultado = stringResultado.replace("Ú", "&Uacute;");
            stringResultado = stringResultado.replace("Ñ", "&Ntilde;");
            
            stringResultado = stringResultado.replace("á", "&aacute;");
            stringResultado = stringResultado.replace("é", "&eacute;");
            stringResultado = stringResultado.replace("í", "&iacute;");
            stringResultado = stringResultado.replace("ó", "&oacute;");
            stringResultado = stringResultado.replace("ú", "&uacute;");
            
            UtilFicheros.generaFichero("./res/resultados/resultado_" + dorsal + "_" + evento.getIdEvento() + ".html", stringResultado);
            System.out.println("Generado fichero: resultado_" + dorsal + "_" + evento.getIdEvento() + ".html");
            
            TResultados resultado = new TResultados();
            resultado.setID_EVENTO(evento.getIdEvento() + "");
            resultado.setID_CARRERA(1);
            resultado.setPOS_CAT(Integer.parseInt(posicion));
            resultado.setPOS_GEN(Integer.parseInt(posicion));
            resultado.setTIEMPO_FORMATEADO(tiempo);
            resultado.setID_CORREDOR(Integer.parseInt(idCorredor));
            if (evento.getIdEvento() != 85 && evento.getIdEvento() != 143 && evento.getIdEvento() != 145)
            	resultado.setRITMO(ritmoKMH.substring(0, ritmoKMH.lastIndexOf(" ")));
           
            Resultados.guardaResultado(resultado);
		}
		
		
	}
	
	public static void guardaCategoria(String idEvento, String idCategoria, String numPasos, String descCategoria,  String distancia, String retraso, String idCarrera) {
		
		StringBuffer queryInsercion = new StringBuffer();
		queryInsercion.append("INSERT INTO Categorias VALUES (");
		queryInsercion.append( idEvento + ",");
		queryInsercion.append(idCategoria + ",");
		queryInsercion.append(numPasos + ",'");
		queryInsercion.append(descCategoria + "',");		
		queryInsercion.append(distancia + ",");
		queryInsercion.append(retraso + ",");
		queryInsercion.append(idCarrera);
		queryInsercion.append(");");
		
				
      	System.out.println(queryInsercion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			agente.insert(queryInsercion.toString());
		} catch (Exception e) {
			System.out.println("FALLO AL INSERTAR UNA CATEGORIA: Evento --> " + idEvento + " idCategoria --> " + idCategoria + " descCategoria --> " + descCategoria);
		}		
	}
	
	public static HashMap <String, TCategorias> recuperaCategoriasEventoBd(String idEvento) {
		TCategorias categoria = null;
		ResultSet resultSet = null;
		HashMap <String, TCategorias> hmCategorias = new HashMap <String, TCategorias>();
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM Categorias WHERE idEvento = " + idEvento + " and numPasos=1;";
			
			System.out.println(query);
			resultSet = agente.select(query);
			
			while (resultSet.next()) {
				categoria = new TCategorias ();
				categoria.setIdEvento(resultSet.getInt("idEvento"));
				categoria.setIdCategoria(resultSet.getInt("idCategoria"));
				categoria.setDescCategoria(resultSet.getString("descCategoria"));
				categoria.setDistancia(resultSet.getInt("distancia"));
				categoria.setNumPasos(resultSet.getInt("numPasos"));
				categoria.setRetraso(resultSet.getInt("retraso"));
				hmCategorias.put(categoria.getIdCategoria()+"", categoria);
			}
			return hmCategorias;
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR CATEGORIAS EVENTO  " + idEvento);
			return null;
		}		
	}



}
