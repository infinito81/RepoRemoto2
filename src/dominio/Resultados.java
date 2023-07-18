package dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import estaticos.Log;
import persistencia.Agente;
import persistencia.dao.model.TCategorias;
import persistencia.dao.model.TCorredores;
import persistencia.dao.model.TLecturas;
import persistencia.dao.model.TResultados;
import util.Constantes;
import util.EnviosHTTP;
import util.Estatica;
import util.FormatoFecha;

public class Resultados {
	
	private static Logger log = Log.getLogger(Resultados.class);
	
	public static TCorredores guardarResultado(int idCarrera, String sDorsal, long tiempoFinal) {
		TCorredores corredor = getIdPorDorsal(sDorsal);
		
		if (corredor != null){
			StringBuffer queryInsercion = new StringBuffer();
			queryInsercion.append("INSERT INTO resultados VALUES (");
			queryInsercion.append( " NULL,");
			queryInsercion.append(idCarrera + ",");
			queryInsercion.append(corredor.getID() + ",");
			queryInsercion.append(tiempoFinal + ",");
			queryInsercion.append( " NULL,");
			queryInsercion.append( " NULL,");
			queryInsercion.append( " NULL,");
			queryInsercion.append( " NULL");
			queryInsercion.append(");");
			
					
	      	System.out.println(queryInsercion.toString());
			
			try {
				Agente agente = Agente.getAgente();
				agente.insert(queryInsercion.toString());
				return corredor;
			} catch (Exception e) {
				System.out.println("FALLO AL INSERTAR UN RESULTADO: Corredor --> " + corredor.getID() + "Carrera --> " + idCarrera + " tiempo final --> " + tiempoFinal);
				return null;
			}	
		} else {
			return null;
		}
	}
	
	public static int guardaResultado(TResultados resultado) {
		
		StringBuffer queryInsercion = new StringBuffer();
		queryInsercion.append("INSERT INTO resultados VALUES (");
		
		if (resultado.getID_RESULTADO()!=null && resultado.getID_RESULTADO() > 0){
			queryInsercion.append(resultado.getID_RESULTADO() + ", ");
		} else {
			queryInsercion.append("NULL, ");	
		}
		
		queryInsercion.append("'" + resultado.getID_EVENTO() + "',");
		queryInsercion.append("'" + resultado.getID_CARRERA() + "',");
		queryInsercion.append("'" + resultado.getID_CORREDOR() + "',");
		queryInsercion.append("'" + resultado.getTIEMPO() + "',");
		queryInsercion.append(resultado.getPOS_CAT()!=null ? "'" + resultado.getPOS_CAT() + "'," : "NULL,");
		queryInsercion.append(resultado.getPOS_GEN()!=null ? "'" + resultado.getPOS_GEN() + "'," : "NULL,");
		queryInsercion.append(resultado.getTIEMPO_FORMATEADO(true)!=null ? "'" + resultado.getTIEMPO_FORMATEADO(true) + "'," : "NULL,");
		queryInsercion.append(resultado.getRITMO()!=null ? "'" + resultado.getRITMO() + "'" : "NULL");
		queryInsercion.append(");");

		
      	System.out.println(queryInsercion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			int i = agente.insert(queryInsercion.toString());
			return i;
		} catch (Exception e) {
			System.out.println("FALLO AL INSERTAR UN RESULTADO " + resultado.toString());
			return -1;
		}	
	}
	
	
	private static TCorredores getIdPorDorsal (String sDorsal){
		TCorredores corredor = new TCorredores ();
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM corredores WHERE dorsal = '" + sDorsal + "';";
			System.out.println(query);
			resultSet = agente.select(query);
			corredor.setID(resultSet.getInt("id"));
			corredor.setDORSAL(resultSet.getString("dorsal"));
			corredor.setNOMBRE(resultSet.getString("nombre"));
			corredor.setAPELLIDOS(resultSet.getString("apellidos"));
			return corredor;
		} catch (Exception e) {
			System.out.println("DORSAL NO ENCONTRADO " + sDorsal);
			return null;
		}			
	}
	
	public static TCorredores getIdPorDorsal (int evento, String sDorsal){
		TCorredores corredor = new TCorredores ();
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM corredores WHERE idEvento = " + evento + " AND dorsal = " + sDorsal + ";";
			//System.out.println(query);
			
			resultSet = agente.select(query);
			corredor.setID(resultSet.getInt("id"));
			corredor.setDORSAL(resultSet.getString("dorsal"));
			corredor.setNOMBRE(resultSet.getString("nombre"));
			corredor.setAPELLIDOS(resultSet.getString("apellidos"));
			corredor.setCATEGORIA(resultSet.getString("categoria"));
			corredor.setIdCategoria(resultSet.getInt("idCategoria"));
			
			corredor.settCategoria(Corredores.getCategoriaBD(evento, corredor.getIdCategoria()));
			return corredor;
		} catch (Exception e) {
			System.out.println("FALLO AL IDENTIFICADOR POR DORSAL " + sDorsal);
			return null;
		}			
	}
	
	public static TCorredores getCorredorPorId (int identificador){
		TCorredores corredor = new TCorredores ();
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM corredores WHERE id = '" + identificador + "';";
			System.out.println(query);
			resultSet = agente.select(query);
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
			corredor.setIdCategoria(resultSet.getInt("idCategoria"));

			return corredor;
			
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR CORREDOR POR IDENTIFICADOR " + identificador);
			return null;
		}			
	}
	
	public static TResultados getResultadoCompletoCorredor (int idEvento, String dorsal){
			
		ResultSet resultSet = null;
		try {
			TCorredores corredor = getIdPorDorsal(idEvento,dorsal);
			corredor = getCorredorPorId(corredor.getID());
			TResultados resultado = new TResultados();	
			
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM resultados WHERE id_corredor = " + corredor.getID() + ";";
			System.out.println(query);
			resultSet = agente.select(query);
			
			
			while (resultSet!=null && resultSet.next()){
				resultado = new TResultados();
				resultado.setID_RESULTADO(resultSet.getInt("id_resultado"));
				resultado.setID_CARRERA(resultSet.getInt("id_carrera"));
				resultado.setID_CORREDOR(resultSet.getInt("id_corredor"));
				resultado.setTIEMPO(resultSet.getLong("tiempo"));
				resultado.setPOS_CAT(resultSet.getInt("pos_cat"));
				resultado.setPOS_GEN(resultSet.getInt("pos_gen"));
				resultado.setTIEMPO_FORMATEADO(resultSet.getString("tiempo_form"));
				resultado.setRITMO(resultSet.getString("ritmo"));
				resultado.setCorredor(corredor);			
			}
			
			resultado.setCarrera(Carreras.getCarreraPorId(idEvento, resultado.getID_CARRERA()));				
			
			
			return resultado;
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR RESULTADO POR DORSAL " + dorsal);
			return null;
		}		
	}
	
	public static ArrayList<TResultados> getResultadosCarreras (int idEvento){
		
		ArrayList<TResultados> lResultados = new ArrayList<TResultados>();
		TResultados auxiliar = null;
		ResultSet resultSet = null;
		try {
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM resultados ORDER BY id_carrera DESC, tiempo ASC;";
			System.out.println(query);
			resultSet = agente.select(query);
			
			
			while (resultSet!=null && resultSet.next()){
				auxiliar = new TResultados();
				auxiliar.setID_RESULTADO(resultSet.getInt("id_resultado"));
				auxiliar.setID_CARRERA(resultSet.getInt("id_carrera"));
				auxiliar.setID_CORREDOR(resultSet.getInt("id_corredor"));
				auxiliar.setTIEMPO(resultSet.getLong("tiempo"));
				auxiliar.setPOS_CAT(resultSet.getInt("pos_cat"));
				auxiliar.setPOS_GEN(resultSet.getInt("pos_gen"));
				auxiliar.setRITMO(resultSet.getString("ritmo"));
				auxiliar.setTIEMPO_FORMATEADO(resultSet.getString("tiempo_form"));
				
				lResultados.add(auxiliar);
			}
			
			for( TResultados resultado : lResultados ){
				resultado.setCorredor(getCorredorPorId(resultado.getID_CORREDOR()));
			}			

			for( TResultados resultado : lResultados ){
				resultado.setCarrera(Carreras.getCarreraPorId(idEvento, resultado.getID_CARRERA()));				
			}
			
			return lResultados;
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR RESULTADOS DE TODAS LAS CARRERAS ");
			return null;
		}		
	}
	
	public static void actualizaPosicionesCorredoresCarrera (ArrayList<TResultados> lResultados, int idCarrera){
		int posicionGeneral = 0;
		int posicionNinyosMasc = 0;
		int posicionNinyosFeme = 0;
		
		int posicionVetFeme = 0;
		int posicionVetMasc = 0;
		int posicionSenMasc = 0;
		int posicionSenFeme = 0;
		
		for (TResultados resultado : lResultados){
			if (idCarrera == resultado.getID_CARRERA()){
				resultado.setPOS_GEN(++posicionGeneral);
				
				if (idCarrera == Constantes.ID_CARRERA_ABSOLUTA){
					if (resultado.getCorredor().getCATEGORIA().trim().equals("SENIOR")){
						if (resultado.getCorredor().getSEXO().equals("MASC")) resultado.setPOS_CAT(++posicionSenMasc);
						else resultado.setPOS_CAT(++posicionSenFeme);
						
					} else if (resultado.getCorredor().getCATEGORIA().trim().equals("VETERANO")){
						if (resultado.getCorredor().getSEXO().equals("MASC")) resultado.setPOS_CAT(++posicionVetMasc);
						else resultado.setPOS_CAT(++posicionVetFeme);										
					}
				} else {
					if (resultado.getCorredor().getSEXO().equals("MASC")){
						resultado.setPOS_CAT(++posicionNinyosMasc);
					} else {
						resultado.setPOS_CAT(++posicionNinyosFeme);
					}				
				}
				actualizaPosicionesBBDD(resultado);
			}
			
		}
	}
	
	public static void actualizaRitmos (ArrayList<TResultados> lResultados){
		
		for (TResultados resultado : lResultados){
			resultado.setTIEMPO_FORMATEADO(resultado.getTiempoFormateado());
			double distancia = getDistancia(resultado.getID_CARRERA());
			int minutos = Integer.parseInt(resultado.getTIEMPO_FORMATEADO().substring(0, resultado.getTIEMPO_FORMATEADO().indexOf(':')));
			int segundos = Integer.parseInt(resultado.getTIEMPO_FORMATEADO().substring(resultado.getTIEMPO_FORMATEADO().indexOf(':')+1, resultado.getTIEMPO_FORMATEADO().length()));
			
			//Pasamos todo a segundos
			segundos = (minutos * 60) + segundos;
			double segundosPorKilometro = segundos / distancia;
			double minutosPorKilometro = segundosPorKilometro / 60;
			int minutosRitmo = (int)minutosPorKilometro;
			double parteDecimalRitmo = minutosPorKilometro - minutosRitmo;
			double dSegundosRitmo = (parteDecimalRitmo * 0.6 * 100);
		    String val = dSegundosRitmo + "";
		    BigDecimal big = new BigDecimal(val);
		    big = big.setScale(2, RoundingMode.HALF_DOWN);
		    System.out.println("Número : "+big);
			int segundosRitmo = (int)(big.longValue());
			
			resultado.setRITMO(minutosRitmo + ":" + (segundosRitmo < 10 ? "0" + segundosRitmo : segundosRitmo + ""));
			System.out.println(resultado.getRITMO());
			
			actualizaRitmoBBDD(resultado);
		}
	}
	
	private static void actualizaRitmoBBDD(TResultados resultado) {
		StringBuffer queryActualizacion = new StringBuffer();
		queryActualizacion.append("UPDATE resultados SET");

		queryActualizacion.append(" tiempo_form = '" + resultado.getTIEMPO_FORMATEADO() + "', ");
		queryActualizacion.append(" ritmo = '" + resultado.getRITMO() + "' ");
		queryActualizacion.append(" WHERE id_resultado = " + resultado.getID_RESULTADO() + ";");
		
		System.out.println(queryActualizacion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			agente.update(queryActualizacion.toString());
		} catch (Exception e) {
			System.out.println("FALLO AL ACTUALIZAR EL RITMO y TIEMPO FORMAT DEL RESULTADO " + resultado.getID_RESULTADO());
		}				
	}
	
	private static void actualizaPosicionesBBDD (TResultados resultado) {
		StringBuffer queryActualizacion = new StringBuffer();
		queryActualizacion.append("UPDATE resultados SET");

		queryActualizacion.append(" pos_cat = " + resultado.getPOS_CAT() + ", ");
		queryActualizacion.append(" pos_gen = " + resultado.getPOS_GEN() + " ");
		queryActualizacion.append(" WHERE id_resultado = " + resultado.getID_RESULTADO() + ";");
		
		System.out.println(queryActualizacion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			agente.update(queryActualizacion.toString());
		} catch (Exception e) {
			System.out.println("FALLO AL ACTUALIZAR EL RITMO y TIEMPO FORMAT DEL RESULTADO " + resultado.getID_RESULTADO());
		}				
	}
	
	public static void borraResultadosTODOS (String idEvento) {
		StringBuffer queryActualizacion = new StringBuffer();
		queryActualizacion.append("DELETE FROM resultados WHERE id_evento = " + idEvento + ";");

		System.out.println(queryActualizacion.toString());
		
		try {
			Agente agente = Agente.getAgente();
			agente.update(queryActualizacion.toString());
		} catch (Exception e) {
			System.out.println("FALLO AL BORRAR rESULTADOs evento " + idEvento);
		}				
	}


	public static double getDistancia (int idCarrera){
		switch (idCarrera){
			case Constantes.ID_CARRERA_CADETE:
				return 2.0;
			case Constantes.ID_CARRERA_INFANTIL:
				return 2.0;
			case Constantes.ID_CARRERA_ALEVIN:
				return 1.5;				
			case Constantes.ID_CARRERA_BENJAMIN:
				return 1.0;				
			default:
				return 6.0;
		}
	}
	
	public static double getDistancia (String categoria){
		switch (categoria){
			case Constantes.FEMINAS: case Constantes.VETERANOS: case Constantes.CADETES:
				return 12.0;
			case Constantes.MASTER50: case Constantes.JUNIOR: 
				return 18.0;
			case Constantes.MASTER40:
				return 24.0;
			case Constantes.ELITE: case Constantes.MASTER30:
				return 30.0;
			default: 
				return 12.0;
		}
	}

	public static void generaResultados(ArrayList<TLecturas> lecturas, HashMap<String, TCategorias> hmCategorias) {
		
		int contadorPasos = 1;
		long ultimaLectura = 0;
		int dorsalAnterior = -1000;
		int idCategoria = -100;
		
		int contador = 0;
		System.out.println(("Generando resultados..."));
		//ArrayList <TLecturas> lecturasDorsal = 
		for(TLecturas lectura : lecturas) {
			int dorsal = lectura.getDorsalLeido();
			
			TCorredores corredor = Resultados.getIdPorDorsal(lectura.getIdEvento(), dorsal + "");
			
			if (corredor != null) {
				if (dorsal != dorsalAnterior) {
					/************Control de que cada dorsal tiene todas sus lecturas**********************/
					//antes de pasar al siguiente dorsal. Comprobamos que se hicieron todas las lecturas que le correspondían a este dorsal según su categoria
					if (idCategoria >= 0) { //Este if es para que no entre con el primer dorsal
						
						//Sacamos los pasos que tiene la categoría del anterior corredor
						int numPasos = hmCategorias.get(idCategoria+"").getNumPasos();
						
						//Si el contador de pasos del corredor anterior es menor que el numero de pasos que debería haber tenido
						if ((contadorPasos-1) < numPasos) {
							
							if (Estatica.getInstance().getRetirados()!=null && Estatica.getInstance().isRetirado(dorsalAnterior+"")) {
								log.error("RETIRADO Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");
								System.err.println("RETIRADO Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");
							} else {
								log.error("Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");
								System.err.println("Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");								
							}

						}
					}
					/***************************************************************************************/
					
					
					//nuevo dorsal
					//reseteamos datos 
					dorsalAnterior = dorsal;
					contadorPasos = 1;
					ultimaLectura = 0;
					
					
					
					idCategoria = corredor.getIdCategoria();
					
					String tiempoConDescuento = FormatoFecha.calculaTiempoDescuento(lectura.getTiempoFinal(), hmCategorias.get(idCategoria+"").getRetraso());
					
					String datosAMandar = "";
					
					if (hmCategorias.get(idCategoria+"").getNumPasos() == contadorPasos) {
						datosAMandar = "action=agregar&evento=" + lectura.getIdEvento() + "&carrera=" + lectura.getIdCarrera()
						+ "&paso=META&dorsal=" + lectura.getDorsalLeido() + "&tiempo=" + tiempoConDescuento;	
					} else {
						datosAMandar = "action=agregar&evento=" + lectura.getIdEvento() + "&carrera=" + lectura.getIdCarrera()
						+ "&paso=" + contadorPasos + "&dorsal=" + lectura.getDorsalLeido() + "&tiempo=" + tiempoConDescuento;	
					}
					
					log.debug(datosAMandar);
					try {
						//System.out.println("http://popularia.es/android/resultados_get.php?" + datosAMandar);
						String respuesta = EnviosHTTP.sendGet("http://popularia.es/android/resultados_get.php?" + datosAMandar);	
						contador++;
						System.out.println(respuesta);
					} catch (Exception e) {

					}
					

					//System.out.println(datosAMandar);
					ultimaLectura = lectura.getFechaInsert();
					contadorPasos++;

					
					
				//si es el mismo dorsal
				} else {
					if ( (lectura.getFechaInsert() - ultimaLectura) > Constantes.TIEMPO_ENTRE_LECTURAS_EN_MILIS) {
						//System.out.println("nueva lectura del mismo dorsal");
						
						idCategoria = corredor.getIdCategoria();
						
						String tiempoConDescuento = FormatoFecha.calculaTiempoDescuento(lectura.getTiempoFinal(), hmCategorias.get(idCategoria+"").getRetraso());
						
						String datosAMandar = "";
						
						if (hmCategorias.get(idCategoria+"").getNumPasos() == contadorPasos) {
							datosAMandar = "action=agregar&evento=" + lectura.getIdEvento() + "&carrera=" + lectura.getIdCarrera()
							+ "&paso=META&dorsal=" + lectura.getDorsalLeido() + "&tiempo=" + tiempoConDescuento;	
						} else {
							datosAMandar = "action=agregar&evento=" + lectura.getIdEvento() + "&carrera=" + lectura.getIdCarrera()
							+ "&paso=" + contadorPasos + "&dorsal=" + lectura.getDorsalLeido() + "&tiempo=" + tiempoConDescuento;	
						}
						try {
							//System.out.println("http://popularia.es/android/resultados_get.php?" + datosAMandar);
							String respuesta = EnviosHTTP.sendGet("http://popularia.es/android/resultados_get.php?" + datosAMandar);	
							contador++;
							System.out.println(respuesta);
						} catch (Exception e) {
							
						}
						

						//System.out.println(datosAMandar);
						
						ultimaLectura = lectura.getFechaInsert();
						contadorPasos++;

					} else {
						//System.out.println("lectura desechada por ser del mismo tiempo: " + lectura.toString());
					}
				}				
			} else {
				//System.out.println("No se ha encontrado en este evento corredor con dorsal " + dorsal);
			}
			
	//hacer una tabla de resultados totales en local que se descargue lo que viene en la clasificacion php y lo guarde ahí para sacar los podios
	//además en este mismo proceso generar los html para los display corredores
		}
		
		
		//SOLO PARA EL ULTIMO DORSAL
		if (idCategoria >= 0) { //Este if es para que no entre con el primer dorsal
			
			//Sacamos los pasos que tiene la categoría del anterior corredor
			int numPasos = hmCategorias.get(idCategoria+"").getNumPasos();
			
			//Si el contador de pasos del corredor anterior es menor que el numero de pasos que debería haber tenido
			if ((contadorPasos-1) < numPasos) {
				if (Estatica.getInstance().getRetirados()!=null && Estatica.getInstance().isRetirado(dorsalAnterior+"")) {
					log.error("RETIRADO Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");
					System.err.println("RETIRADO Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");
				} else {
					log.error("Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");
					System.err.println("Dorsal " + dorsalAnterior + " le faltan " + (numPasos - (contadorPasos-1)) + " lecturas");								
				}
			}
		}
		
		System.out.println("Mandados " + contador + " pasos");
		
	}

	
	
}
