package com.gmodelo.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gmodelo.utils.ConnectionManager;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

import data.inventarios.sincro.RFC_DATAMART;

public class DeltasDao {

	private Logger log = Logger.getLogger(DeltasDao.class.getName());
	
	final static String RFC_DESTINATION = "RFC_DESTINATION";
	final private static String RFC_NAME_PERSONNEL = "/BOA/ZRFC_READ_TABLE";
	//final private static String RFC_COUNT_TABLE = "ZRARMF_GET_TABLE_SIZE_RFC";
	final private static String RFC_COUNT_TABLE = "/BOA/ZGET_TABLE_SIZE_RFC";
	final static Integer masterRows = 5000;
	final static Integer masterBeginRow = 0;
	public static final String REQUESTTABLECONFIG = "SELECT TABLE_NAME, TABLE_VALUES, TABLE_SQL_FILL FROM RFC_TABLE_FILL WITH(NOLOCK)";
	
	public static final String REQUESTTABLECONFIGDELTA = "SELECT TOP 1 TABLE_NAME, TABLE_VALUES, TABLE_SQL_FILL, TABLE_REQUEST_FILTERS, TABLE_FIELD_FILTER, TABLE_TYPE_FILTER, CONVERT(DATE,LAST_REQUEST)LAST_REQUEST FROM RFC_TABLE_FILL WITH(NOLOCK)";
	/*public static final String REQUESTTABLECONFIGDELTA = "SELECT TABLE_NAME, TABLE_VALUES, TABLE_SQL_FILL, TABLE_REQUEST_FILTERS, TABLE_FIELD_MAX_MODIFIED FROM RFC_TABLE_FILL WITH(NOLOCK) WHERE LAST_REQUEST IS NULL OR "
			+ " CONVERT(date,getdate()) > CONVERT(date,dateadd(day,7,LAST_REQUEST))";
	*/
	public static final String UPDATETABLEDATAMART = "UPDATE RFC_TABLE_FILL SET LAST_REQUEST = getdate() WHERE TABLE_NAME = ?";
	
	private String HOST; 
	private String USERHOST;
    private String PASSWORDHOST;
    private JCoDestination destination;
	final static String INV_CIC_REPOSITORY = "SELECT STORED_KEY, STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY IN('SAP_HOST','SAP_USER','SAP_PASSWORD') GROUP BY STORED_KEY, STORED_VALUE ORDER BY STORED_KEY ASC";
	
	public DeltasDao(){
		destination = null;
		generatedProperties();
	}
	
	public String getCronDelta(){
		
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;

		String INV_CIC_REPOSITORY = "SELECT STORED_VALUE FROM INV_CIC_REPOSITORY WITH(NOLOCK) WHERE STORED_KEY = ?";
		log.warning(INV_CIC_REPOSITORY);
		String cron = "";
		log.log(Level.WARNING, "[getCronDeltaDao] Preparing sentence...");
		
		try {
			stm = con.prepareStatement(INV_CIC_REPOSITORY);
			stm.setString(1, "CRON_DELTA");
			log.log(Level.WARNING, "[getCronDeltaDao] Executing query...");

			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				cron = rs.getString("STORED_VALUE");
			}

			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();
			log.log(Level.WARNING, "[getCronDeltaDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getCronDeltaDao] Some error occurred while was trying to execute the query: " + INV_CIC_REPOSITORY, e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getCronDeltaDao] Some error occurred while was trying to close the connection.", e);
			}
		}
		return cron;
	}

	public void getDataConectionSAP(){
		ConnectionManager iConnectionManager = new ConnectionManager();
		Connection con = iConnectionManager.createConnection();
		PreparedStatement stm = null;
		
		log.warning(INV_CIC_REPOSITORY);
		log.log(Level.WARNING, "[getDataConectionSAPDao] Preparing sentence...");
		
		try {
			stm = con.prepareStatement(INV_CIC_REPOSITORY);
			log.log(Level.WARNING, "[getDataConectionSAPDao] Executing query...");

			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				if(rs.getString("STORED_KEY").equalsIgnoreCase("SAP_HOST")){
					HOST = rs.getString("STORED_VALUE");
				}
				if(rs.getString("STORED_KEY").equalsIgnoreCase("SAP_USER")){
					USERHOST = rs.getString("STORED_VALUE");
				}
				if(rs.getString("STORED_KEY").equalsIgnoreCase("SAP_PASSWORD")){
					PASSWORDHOST = rs.getString("STORED_VALUE");
				}
			}
			
			System.out.println("HOST: "+ HOST + " USERHOST: "+ USERHOST + " PASSWORDHOST: "+ PASSWORDHOST);
			
			// Retrive the warnings if there're
			SQLWarning warning = stm.getWarnings();
			while (warning != null) {
				log.log(Level.WARNING, warning.getMessage());
				warning = warning.getNextWarning();
			}

			// Free resources
			rs.close();
			stm.close();
			log.log(Level.WARNING, "[getDataConectionSAPDao] Sentence successfully executed.");
		} catch (SQLException e) {
			log.log(Level.SEVERE,"[getDataConectionSAPDao] Some error occurred while was trying to execute the query: " + INV_CIC_REPOSITORY, e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "[getDataConectionSAPDao] Some error occurred while was trying to close the connection.", e);
			}
		}
	}
	
	public void generatedProperties(){
		getDataConectionSAP();
		
		log.log(Level.WARNING, "[generatedPropertiesDeltaDAO] Preparing sentence...");
		Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, HOST);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "400");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   USERHOST);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, PASSWORDHOST);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
        
        log.log(Level.WARNING, "[generatedPropertiesDeltaDAO] Call to createDestinationDataFile...");
        createDestinationDataFile(RFC_DESTINATION, connectProperties);
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,    "10");
	}
	
	public void createDestinationDataFile(String destinationName, Properties connectProperties){
        File destCfg = new File(destinationName+".jcoDestination");
        try{
        	log.log(Level.WARNING, "[createDestinationDataFileDeltaDAO] Preparing sentence...");
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "DESTINATION SAP");
            fos.close();
        }catch (Exception e){
        	log.log(Level.SEVERE,"[createDestinationDataFileDeltaDAO] Some error occurred while was trying to createDestination: " + destinationName, e);
        }
    }
	
	public void connectDestination(){
		try {
			log.log(Level.WARNING, "[connectDestinationDeltaDAO] Preparing sentence...");
			destination = JCoDestinationManager.getDestination(RFC_DESTINATION);
			//System.out.println("Attributes:");
	        //System.out.println(destination.getAttributes());
	        
	        if(destination.isValid()){
	        	System.out.println("Conectado");	
	        }
		} catch (JCoException e) {
			log.log(Level.SEVERE,"[connectDestinationDeltaDAO] Some error occurred while was connectDestination: " + destination, e);
		}
    }
	
	public void RequestFirstRun() {
		Connection con = new ConnectionManager().createConnection();
		String options = "";
		try {
			PreparedStatement stm = con.prepareStatement(REQUESTTABLECONFIGDELTA);
			System.out.println(REQUESTTABLECONFIGDELTA);
			List<RFC_DATAMART> rfc_datamarts = new ArrayList<>();
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				rfc_datamarts.add(new RFC_DATAMART(rs));
			}
			if (rfc_datamarts.size() > 0) {
				for (RFC_DATAMART datamart : rfc_datamarts) {
					datamart.setMaxRows(RequestCurretRows(datamart.getTablename()));
					System.out.println("table: "+ datamart.getTablename()+ " sizetable: "+ datamart.getMaxRows());
					
					if(datamart.getMaxRows() <= 50000){
						System.out.println("Cargar toda la tabla");
						//RequestTableViaDatabase(datamart, con, masterRows, masterBeginRow, options);
					}else{
						System.out.println("Cargar solo datos recientes");
						options = this.generatedFiltersTable(datamart.getTablename(), datamart.getTable_field_filter(), datamart.getTable_type_filter(), datamart.getLast_request());
						System.out.println(options);
						RequestTableViaDatabase(datamart, con, masterRows, masterBeginRow, options);
					}
					
					//UpdateLastExcecute(con, datamart);
				}
			} else {
				System.out.println("NODATA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JCoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Integer RequestCurretRows(String tableName) throws JCoException {
		JCoFunction function = destination.getRepository().getFunction(RFC_COUNT_TABLE);
		Integer currentRows = 0;
		if (function == null)
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		function.getImportParameterList().setValue("TABLE_NAME", tableName);
		function.getImportParameterList().setValue("CROSS_CLIENT", "");
		try {
			function.execute(destination);
			currentRows = function.getExportParameterList().getInt("TABLE_SIZE");
		} catch (AbapException e) {
			e.printStackTrace();
			currentRows = 0;
		}
		return currentRows;
	}
	
	public String generatedFiltersTable(String tableName, String field, String filterType, String lastRequest){
		String options = "";
	     switch (filterType) {
	         case "MAX":
	        	 options = field + " > '" + lastRequest +"' ";
	             break;
	         case "BETWEEN":
	             options = field + " BETWEEN '' AND '' ";
	             break;
	     }
		return options;
	}
	
	public void UpdateLastExcecute(Connection con, RFC_DATAMART datamart) {
		try {
			PreparedStatement stm = con.prepareStatement(UPDATETABLEDATAMART);
			stm.setString(1, datamart.getTablename());
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void RequestTableViaDatabase(RFC_DATAMART rfc_datamart, Connection con, Integer rowCount, Integer rowSkip, String options)
			throws JCoException, SQLException {
		if (rfc_datamart.getMaxRows() == 0) {
			for (; rowSkip <= 999999999; rowSkip += rowCount) {
				if (GetRFCTable(rfc_datamart, String.valueOf(rowSkip), String.valueOf(rowCount), con, options) == 9999) {
					break;
				}
			}
		} else {
			for (; rowSkip <= rfc_datamart.getMaxRows(); rowSkip += rowCount) {
				GetRFCTable(rfc_datamart, String.valueOf(rowSkip), String.valueOf(rowCount), con, options);
			}
		}
	}

	public Integer GetRFCTable(RFC_DATAMART rfc_datamart, String rowSkip, String rowCount, Connection con, String options)
			throws JCoException {
		Integer success = 1;
		JCoFunction function = destination.getRepository().getFunction(RFC_NAME_PERSONNEL);
		if (function == null)
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		function.getImportParameterList().setValue("QUERY_TABLE", rfc_datamart.getTablename());
		function.getImportParameterList().setValue("DELIMITER", RFC_DATAMART.RFCDELIMITER);
		function.getImportParameterList().setValue("NO_DATA", "");
		function.getImportParameterList().setValue("ROWSKIPS", rowSkip);
		function.getImportParameterList().setValue("ROWCOUNT", rowCount);
		function.getImportParameterList().setValue("CROSS_CLIENT", "");
		
		if(options != ""){
			JCoTable fields = function.getTableParameterList().getTable("OPTIONS");
			fields.appendRow();
			fields.setValue("TEXT", options);
		}
		
		rfc_datamart.StandarGetTableFields(function, rfc_datamart);
		
		if (rfc_datamart.getTable_filters() != null && !rfc_datamart.getTable_filters().isEmpty()) {
			rfc_datamart.StandartGetTableFilters(function, rfc_datamart);
		}
		try {
			function.execute(destination);
			JCoTable resultTable = null;
			resultTable = function.getTableParameterList().getTable(0);
			System.out.println("-----------------------------------------------------------------------\n\n");
			//System.out.println(rfc_datamart.getSapResourceValues(resultTable));
			
			/*PreparedStatement stm = con.prepareStatement(rfc_datamart.getTable_Insert());
			try {
				do {
					try {
						rfc_datamart.setTable_values(rfc_datamart.getSapResourceValues(resultTable));
						rfc_datamart.fillSapResourceValues(rfc_datamart, stm);

					} catch (SQLException e) {
						e.printStackTrace();
						success = 99;
					} catch (Exception e) {
						if (rfc_datamart.getMaxRows() == 0) {
							return 9999;
						} else {
							success = 99;
						}
					}
				} while (resultTable.nextRow());
				stm.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				success = 99;
			}
			*/
		} catch (AbapException e) {
			e.printStackTrace();
			success = 99;
		//} catch (SQLException e) {
		//	e.printStackTrace();
		//	success = 99;
		}
		return success;

	}
	
	public static void main(String[] args) {
		DeltasDao x = new DeltasDao();
		
		x.connectDestination();
		x.RequestFirstRun();
		/*
		try {
			x.Connect();
		} catch (JCoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//x.RequestFirstRun();
	}

}
