package persistencia;

/*import hbs.config.Config;
import hbs.config.ConfigException;
import hbs.config.XMLConfigErrorHandler;
import hbs.dao.ExportarConfigException;
import hbs.dao.ManejadorFicheros;
import hbs.data.ConnectionManager;
import hbs.data.ConnectionManagerConfigException;*/

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XMLconnectionManagerParser extends DefaultHandler {
	
	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";//
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";//
	
	private String reader;
	private String environment;
	private String props;
	
	private File file;
	private SAXParser saxparser;
	public StringBuffer textBuffer = new StringBuffer("INICIALIZADO");
	
	public String getReader() {
		return reader;
	}


	public void setReader(String reader) {
		this.reader = reader;
	}


	public String getEnvironment() {
		return environment;
	}


	public void setEnvironment(String environment) {
		this.environment = environment;
	}


	public String getProps() {
		return props;
	}


	public void setProps(String props) {
		this.props = props;
	}
	
	
	public XMLconnectionManagerParser(File file){
		this.file = file;
	}
	
	
	@Override
	public final void startElement(String ns,String name,String qname,Attributes attrs) throws SAXException{

		if (qname.equals("reader")) {				
			textBuffer.delete(0,textBuffer.length());
		}

		if (qname.equals("environment")) {				
			textBuffer.delete(0,textBuffer.length());
		}	

		if (qname.equals("props")) {				
			textBuffer.delete(0,textBuffer.length());
		}		

	}

	@Override
	public final void endElement(String ns, String name, String qname){
		if (qname.equals("reader")) {				
			reader = textBuffer.toString();
		}
		if (qname.equals("environment")) {				
			environment = textBuffer.toString();
		}	
		if (qname.equals("props")) {				
			props = textBuffer.toString();
		}		
	}

	public void procesaConfiguracion(){

		try {
			//Generamos SAXParser
			SAXParserFactory sax_parser_factory = SAXParserFactory.newInstance();	


			//validamos el XML
			sax_parser_factory.setValidating(true);
			sax_parser_factory.setNamespaceAware(true);
			saxparser = sax_parser_factory.newSAXParser();
			saxparser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

			//validamos
			XMLReader reader = saxparser.getXMLReader();
			//reader.setErrorHandler(new XMLConfigErrorHandler());
			reader.setErrorHandler(null);

			reader.parse(new InputSource(file.getPath()));

			//parseamos el XML
			saxparser.parse(file.getPath(), this);

		} catch (SAXException e) {
			System.out.println("Error Configuracion: <"+file+"> causa: "+e.getMessage());
		} catch (IOException e) {
			System.out.println("Error Configuracion: <"+file+"> causa: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error Configuracion: <"+file+"> causa: "+e.getMessage());
		}
	}

	public void guardaConfiguracion(){
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//ROOT
			Document doc = docBuilder.newDocument();
			Element configElement = doc.createElement("config");
			configElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			configElement.setAttribute("xsi:noNamespaceSchemaLocation", "./ConnectionManagerConfigSchema.xsd");
			doc.appendChild(configElement);

			//INI-reader 
			Element numerotrazos = doc.createElement("reader");
			numerotrazos.appendChild(doc.createTextNode(ConnectionManager.getInstance().getReader()));
			configElement.appendChild(numerotrazos);
			//FIN-reader

			//INI-environment 
			Element dispositivo = doc.createElement("environment");
			dispositivo.appendChild(doc.createTextNode(ConnectionManager.getInstance().getEnvironment()));
			configElement.appendChild(dispositivo);
			//FIN-environment	

			//INI-props 
			Element horasvalidez = doc.createElement("props");
			horasvalidez.appendChild(doc.createTextNode(ConnectionManager.getInstance().getProps()));
			configElement.appendChild(horasvalidez);
			//FIN-props				




			//ManejadorFicheros.exportar(doc, new File(Config.getInstance().getModuleConfigByName("LockFactory").getConfigFile()));


		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws org.xml.sax.SAXException{

		String s = new String(arg0, arg1, arg2);	
		s = s.replace("&", "&amp;");
		if (textBuffer == null){		    
			textBuffer = new StringBuffer(s);		  
		}else{		    
			textBuffer.append(s);
		}		 			 
	}
}
