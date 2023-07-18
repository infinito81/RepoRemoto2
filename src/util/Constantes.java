package util;

import java.util.Random;

public class Constantes {
	
	
	public static final int PESTANYA_CLASIF_BENJAMIN = 0;
	public static final int PESTANYA_CLASIF_ALEVIN = 1;
	public static final int PESTANYA_CLASIF_INFANTIL = 2;
	public static final int PESTANYA_CLASIF_CADETE = 3;
	public static final int PESTANYA_CLASIF_ABSOLUTA = 4;
	public static final int PESTANYA_CLASIF_SENIOR = 5;
	public static final int PESTANYA_CLASIF_VETERANO = 6;
	public static final int PESTANYA_CLASIF_LOCAL = 7;
	
	public static final int ID_CARRERA_BENJAMIN = 5;
	public static final int ID_CARRERA_ALEVIN = 4;
	public static final int ID_CARRERA_INFANTIL = 3;
	public static final int ID_CARRERA_CADETE = 2;
	public static final int ID_CARRERA_ABSOLUTA = 1;
	
	public static final String FEMINAS = "FÉMINAS";
	public static final String CADETES = "CADETES";
	public static final String JUNIOR = "JUNIOR";
	public static final String MASTER30 = "MÁSTER 30";
	public static final String MASTER40 = "MÁSTER 40";
	public static final String MASTER50 = "MÁSTER 50";
	public static final String VETERANOS = "VETERANOS";
	public static final String ELITE = "ÉLITE";
	
	//ROJO
	//public static final int [] CODIGO_COLOR_FONDO = {194, 29, 23};
	
	// GRANATE (Dany)
	public static final int [] CODIGO_COLOR_FONDO = {0, 0, 0};
	
	//public static final int [] CODIGO_COLOR_FONDO = {232, 232, 231};
	public static final int [] CODIGO_COLOR_TEXTOS = {101, 211, 33};
	
	
	//Tiempo mínimo entre dos lecturas (en milisegundos)
	public static final double TIEMPO_ENTRE_LECTURAS_EN_MILIS = 300000; //5 minutos
	
	//Tiempo inicial no hay lecturas (En milisegundos)
	public static final double TIEMPO_INICIAL_NO_LECTURAS = 180000; //3 minutos
	
	//Tiempo que pasa entre peticiones de lectura automática a los lectores conectados al sistema
	public static final long TIEMPO_SONDA_PETICION_LECTURA = 15000; //5 minutos y pico
	
	//Clave que se mete antes para indicar que un dorsal se ha retirado
	public static final String LECTURA_CORREDOR_RETIRADO = "RET";
	
	//Tiempo que pasa para mostrar clasificaciones
	public static final long TIEMPO_SONDA_CLASIFICACIONES= 20000; //20 SEGUNDOS
	
	/**************************** Mensajes para los lectores ***************************/
	public static final String MENSAJE_LECTORES_UFH_CMD_TODAS_ANTENAS_ENABLE_SONIDO = "7CFFFF81311C1E016E545D666F7882020A0001051E0A0F0F100101020002000000201D";
	public static final String MENSAJE_LECTORES_UFH_CMD_TODAS_ANTENAS_DISABLE_SONIDO = "7CFFFF81311C1E016E545D666F7882020A0001051E0A0F0F100100020002000000201D";
	public static final String MENSAJE_LECTORES_UFH_CMD_PONTE_A_LEER = "7CFFFF11320043";
	
	public static final String MENSAJE_LECTORES_UHF_RESPUESTA_CMD_CONFIGURACION = "CCFFFF810000B5";
	public static final String MENSAJE_LECTORES_UHF_RESPUESTA_CMD_PONTE_A_LEER = "CCFFFF11010024";
	/**********************************************************************************/
	
	
	
	
	
}
