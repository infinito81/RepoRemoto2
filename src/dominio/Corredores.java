package dominio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import estaticos.Log;
import persistencia.Agente;
import persistencia.dao.model.TCategorias;
import persistencia.dao.model.TCorredores;
import persistencia.dao.model.TLecturas;
import util.Constantes;
import util.Estatica;



public class Corredores {
	private static Logger log = Log.getLogger(Corredores.class);
	
	public static int guardarCorredorNuevo(TCorredores corredor) {
		
		StringBuffer queryInsercion = new StringBuffer();
		queryInsercion.append("INSERT INTO corredores VALUES (");
		
		queryInsercion.append(corredor.getID_EVENTO() + ",");
		
		if (corredor.getID()!=null && corredor.getID() > 0){
			queryInsercion.append(corredor.getID() + ", ");
		} else {
			queryInsercion.append("NULL, ");	
		}		
		
		queryInsercion.append("'" + corredor.getNOMBRE() + "',");
		queryInsercion.append("'" + corredor.getAPELLIDOS() + "',");
		queryInsercion.append(corredor.getDNI()!=null ? "'" + corredor.getDNI() + "'," : "NULL,");
		queryInsercion.append(corredor.getEMAIL()!=null ? "'" + corredor.getEMAIL() + "'," : "NULL,");
		queryInsercion.append("'" + corredor.getSEXO() + "',");
		queryInsercion.append(corredor.getFECHA_NAC()!=null ? "'" + corredor.getFECHA_NAC() + "'," : "NULL,");
		queryInsercion.append("'" + corredor.getCATEGORIA() + "',");
		queryInsercion.append(corredor.getPOBLACION()!=null ? "'" + corredor.getPOBLACION() + "'," : "NULL,");
		queryInsercion.append(corredor.getCLUB()!=null ? "'" + corredor.getCLUB() + "'," : "NULL,");
		queryInsercion.append(corredor.getDORSAL()!=null ? "'" + corredor.getDORSAL() + "'," : "'-1',");
		queryInsercion.append(corredor.getIdCategoria() + ");");
		//queryInsercion.append("'-1');");
		
      	//System.out.println(queryInsercion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			agente.insert(queryInsercion.toString());
			return obtenerIdParticipante();
		} catch (Exception e) {
			System.out.println("FALLO AL INSERTAR UN CORREDOR " + corredor.getID() + "\n" + e.getMessage());
			return -1;
		}	
	}
	
	public static String getUltimoDorsal () {
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM corredores ORDER BY dorsal DESC;";
			System.out.println(query);
			resultSet = agente.select(query);
			return resultSet.getString("dorsal");
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR ULTIMO DORSAL");
			return "-1";
		}
	}
	
	public static int obtenerIdParticipante() {
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM corredores ORDER BY ID DESC;";
			//System.out.println(query);
			resultSet = agente.select(query);
			return resultSet.getInt("ID");
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR ULTIMO CORREDOR");
			return -1;
		}	
	}

	public static int setDorsal(int identificador, String sDorsal) {
		StringBuffer queryActualizacion = new StringBuffer();
		queryActualizacion.append("UPDATE corredores SET");

		queryActualizacion.append(" dorsal = '" + sDorsal + "' ");
		queryActualizacion.append(" WHERE id = " + identificador + ";");
		
		System.out.println(queryActualizacion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			agente.update(queryActualizacion.toString());
			return 1;
		} catch (Exception e) {
			System.out.println("FALLO AL ACTUALIZAR EL id corredor " + identificador);
			return -1;
		}		
	}

	public static ArrayList<TCorredores> getTodosCorredores () {
		ArrayList<TCorredores> corredores = new ArrayList<TCorredores>();
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM corredores ORDER BY ID ASC;";
			System.out.println(query);
			resultSet = agente.select(query);
			
			 while (resultSet.next()) {				
				TCorredores corredor = new TCorredores();
				corredor.setID(resultSet.getInt("id"));
				corredor.setNOMBRE(resultSet.getString("nombre"));
				corredor.setAPELLIDOS(resultSet.getString("apellidos"));
				corredor.setSEXO(resultSet.getString("sexo"));
				corredor.setDNI(resultSet.getString("dni"));
				corredor.setEMAIL(resultSet.getString("email"));
				corredor.setCATEGORIA(resultSet.getString("categoria"));
				corredor.setFECHA_NAC(resultSet.getString("fechaNac"));
				corredor.setPOBLACION(resultSet.getString("poblacion"));
				corredor.setCLUB(resultSet.getString("club"));
				corredor.setDORSAL(resultSet.getString("dorsal"));
				
				corredores.add(corredor);				
			}
			
			
			
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR ACTUALIZAR CORREDORES");			
		}	
		
		return corredores;
	}

	public static ArrayList<TCorredores> getTodosCorredoresArchivo() {
		ArrayList<TCorredores> corredoresArchivo = new ArrayList<TCorredores>();
		try{
			FileReader archivo = new FileReader ("C:/Users/6835938/Downloads/corredores.csv");
			BufferedReader buffer = new BufferedReader(archivo);
			String linea;
			while ((linea = buffer.readLine())!=null){
				StringTokenizer sToken = new StringTokenizer(linea, ",");
				TCorredores corredor = new TCorredores();
				while(sToken.hasMoreElements()){
					corredor.setID(Integer.parseInt(sToken.nextToken().trim()));
					corredor.setNOMBRE(sToken.nextToken().trim());
					corredor.setAPELLIDOS(sToken.nextToken().trim());
					corredor.setDNI(sToken.nextToken().trim());
					corredor.setEMAIL(sToken.nextToken().trim());
					corredor.setSEXO(sToken.nextToken().trim());
					corredor.setFECHA_NAC(sToken.nextToken().trim());
					corredor.setCATEGORIA(sToken.nextToken().trim());
					corredor.setPOBLACION(sToken.nextToken().trim());
					corredor.setCLUB(sToken.nextToken().trim());
					
					sToken.nextToken();
					
					corredoresArchivo.add(corredor);
				}
			}
			
			buffer.close();			
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		return corredoresArchivo;
	}
	
	public static String getCorredoresEvento(int evento) {
		
        String datos = "evento=" + evento;
        String [] strings = new String[]{"http://popularia.es/android/get_corredores_evento.php",datos};
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
			System.err.println("FALLO EN LA CONEXIÓN " + e.getMessage());
		}
		output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
        log.debug("CORREDORES SERVIDOR: " + output.toString());
        return responseOutput.toString();
	}
	
	public static void guardaCorredores (String s, int evento) {
        StringTokenizer st = new StringTokenizer(s, ";");
        int numCorredores = Integer.parseInt(st.nextToken());
        int j=0;
        for (j=0; j<numCorredores; j++){
            String idCorredor = st.nextToken();
            String nombreCorredor = st.nextToken();
            String apellidos = st.nextToken();
            String sexo = st.nextToken();
            String categoria = st.nextToken();
            String dorsal = st.nextToken();
            String idCategoria = st.nextToken();
            
            TCorredores corredor = new TCorredores();
            corredor.setID_EVENTO(new Integer(evento));
            corredor.setID(Integer.parseInt(idCorredor));
            corredor.setNOMBRE(nombreCorredor);
            corredor.setAPELLIDOS(apellidos);
            corredor.setSEXO(sexo);
            corredor.setCATEGORIA(categoria);
            corredor.setDORSAL(dorsal);
            corredor.setIdCategoria(Integer.parseInt(idCategoria));
            
            guardarCorredorNuevo(corredor);
            try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        System.out.println("Guardados " + j + " corredores de este evento");
	}

	public static TCategorias getCategoriaBD(int evento, int idCategoria) {
		TCategorias categoria = new TCategorias ();
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM Categorias WHERE idEvento = " + evento + " AND idCategoria = " + idCategoria + ";";
			//System.out.println(query);
			resultSet = agente.select(query);
			categoria.setIdCategoria(idCategoria);
			categoria.setIdEvento(evento);
			categoria.setDescCategoria(resultSet.getString("descCategoria"));
			categoria.setDistancia(resultSet.getInt("distancia"));
			categoria.setNumPasos(resultSet.getInt("numPasos"));
			categoria.setRetraso(resultSet.getInt("retraso"));

			return categoria;
		} catch (Exception e) {
			System.out.println("FALLO AL OBTENER LA CATEGORÍA DEL EVENTO " + evento + " Y CATEGORIA" + idCategoria);
			
		}		
		
		return null;
	}

	public static void borrarCorredoresEvento(int idEventoSeleccionado) {
		StringBuffer queryBorrado = new StringBuffer();
		queryBorrado.append("DELETE from Corredores WHERE");

		queryBorrado.append(" idEvento = '" + idEventoSeleccionado + "'; ");
		
		System.out.println(queryBorrado.toString());
		
		try {
			Agente agente = Agente.getAgente();
			int elementosBorrados = agente.delete(queryBorrado.toString());
			System.out.println("Borrados " + elementosBorrados + " corredores evento " + idEventoSeleccionado);
			
		} catch (Exception e) {
			System.out.println("FALLO AL BORRAR DORSALES DEL EVENTO " + idEventoSeleccionado);
			
		}	
		
	}

	public static String getUltimoPaso(TCorredores ultimoCorredor) {
		ArrayList<TLecturas> lecturasDorsal = Estatica.getInstance().getLecturasTotalesDorsales().get(Integer.parseInt(ultimoCorredor.getDORSAL()));
		
		if (ultimoCorredor.getDORSAL().equals("55"))
			System.out.println("prueba");
		
		long lecturaAnterior = 0;
		int i = 0;
		boolean ultimaLectura = false;
		for (TLecturas lectura: lecturasDorsal) {
			if (lectura.getTipoLectura().equals("RET")) {
				return "RET.";
			}
			if ((lectura.getFechaInsert() - lecturaAnterior) > Constantes.TIEMPO_ENTRE_LECTURAS_EN_MILIS) {
				lectura.setBuena(true);
				lecturaAnterior = lectura.getFechaInsert();
				i++;
				ultimaLectura = true;
			} else {
				lectura.setBuena(false);
				ultimaLectura = false;
			}
		}	
		
		if (ultimaLectura && i >= ultimoCorredor.gettCategoria().getNumPasos()) {
			return "META";
		} else if (ultimaLectura) {
			return "V" + i;
		}
		
		return "REPE";
	}



}
