package estaticos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import persistencia.Agente;

public class Dorsales {
	public static HashMap<String, String> hashUhfTagDorsales;
	
	static {
		
		cargaDorsales();

	}
	
	
	public static int guardaDorsal(String dorsal, String tag) {
		int resultado = 0;
		try {
			hashUhfTagDorsales = new HashMap<String, String>();
			Agente agente = Agente.getAgente();
			String insert = "INSERT INTO DORSALES VALUES('" + tag + "','" + dorsal + "');";
			//System.out.println(insert);
			resultado = agente.insert(insert);

			if (resultado > 0) {
				System.out.println("INSERTADO uhftag " + tag + " y dorsal " + dorsal);
			} else {
				System.out.println("ERROR al insertar uhftag " + tag + " y dorsal " + dorsal);
			}
			
		} catch (Exception e) {
			System.out.println("ERROR AL INSERTAR DUPLA " + e.getMessage());			
		}
		return resultado;
	}
	
	public static void cargaDorsales () {
		ResultSet resultSet = null;
		try {
			hashUhfTagDorsales = new HashMap<String, String>();
			Agente agente = Agente.getAgente();
			String query = "SELECT * FROM dorsales;";
			System.out.println(query);
			resultSet = agente.select(query);
			
			 while (resultSet.next()) {				
				 String tag = resultSet.getString("tag_uhf");
				 if (tag.length()>=38) {
					 tag = tag.substring(14, 38);
					 hashUhfTagDorsales.put(tag, resultSet.getString("dorsal"));
				 }
			 }			
		} catch (Exception e) {
			System.out.println("FALLO AL RECUPERAR LOS TAG UHF CON LOS DORSALES");			
		}	
		
		System.out.println("Dorsales cargados. Tamaño HashMap: " + hashUhfTagDorsales.size());
	}
}

