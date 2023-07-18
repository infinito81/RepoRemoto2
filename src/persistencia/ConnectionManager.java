package persistencia;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ConnectionManager {
	
	private static ConnectionManager inst;
	private String reader;
	private String environment;
	private String props;
	private SqlSessionFactory sqlSessionFactory;
	
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
	
	
	public static ConnectionManager getInstance() {
		if(inst==null){
			inst = new ConnectionManager();
		}
		return inst;
	}
	
	public ConnectionManager() {
		File fichero = new File("./mybatis_config.xml");
		if (fichero.exists()) System.out.println("Existe");
		
		//leemos y procesamos el fichero de configuracion
		//XMLconnectionManagerParser xmlconnectionmanagerconfig = new XMLconnectionManagerParser(new File("file://D:/Jorge/IPO-Workspace/competirepository/CompetiReal/mybatis_config.xml"));
		//xmlconnectionmanagerconfig.procesaConfiguracion();
		
		
		reader = "org/mybatis/mybatis-config.xml";
		environment = "development";
		props = "org/mybatis/development-props.xml";
		/*reader = "";
		environment = "";
		props = "";*/
		
	    /* <settings useStatementNamespaces="true"/>
	     <transactionManager type="JDBC">
	        <dataSource type="SIMPLE">
	          <property name="JDBC.Driver" 
	               value="SQLite.JDBCDriver"/>
	          <property name="JDBC.ConnectionURL"
	               value="jdbc:sqlite:/blabla"/>
	        </dataSource>
	      </transactionManager>
	     <sqlMap resource="User.xml"/> 
		*/
		
		
		
		Reader readerConfig = null;
		Properties propertiesConfig = new Properties();
		//propertiesConfig.
		try {
			readerConfig = Resources.getResourceAsReader(reader);
			propertiesConfig = Resources.getResourceAsProperties(props);
		} catch (IOException e) {
			e.printStackTrace();
		}	 		
		
		this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(readerConfig,environment,propertiesConfig);
		System.out.println("cargada sqlSessionFactory");
	}
	
	public SqlSessionFactory getSqlSessionFactory(){
		return this.sqlSessionFactory;		
	}
	

}
