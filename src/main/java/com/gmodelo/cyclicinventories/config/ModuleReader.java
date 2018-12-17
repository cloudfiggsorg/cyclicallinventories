package com.gmodelo.cyclicinventories.config;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

public class ModuleReader {

	private static final String PROPERTIES_FILENAME = "settings.properties";
	public static Properties settings = null;
	private static Logger log = Logger.getLogger(ModuleReader.class.getName());
	
	private static InputStream fileRequest;
	public static StringWriter writerRequest = new StringWriter();
	
	private static InputStream fileResponse;
	public static StringWriter writerResponse = new StringWriter();

	static {
		settings = new Properties();
		fileRequest = Thread.currentThread().getContextClassLoader().getResourceAsStream("MAIL_SEND_REQUEST.html");
		fileResponse = Thread.currentThread().getContextClassLoader().getResourceAsStream("MAIL_SEND_RESPONSE.html");
		try {
			log.info("Triying to load properties file: " + PROPERTIES_FILENAME);
			settings.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILENAME));			
			log.info(PROPERTIES_FILENAME + " successfuly loaded...");
		} catch (IOException e) {
			log.error("Some errors ocurred while triying to load " + PROPERTIES_FILENAME, e);							
		}
		
		try {
			IOUtils.copy(fileRequest, writerRequest, "UTF-8");
			IOUtils.copy(fileResponse, writerResponse, "UTF-8");
		} catch (IOException e) {
			log.error("Some errors ocurred while triying to load HTML files for sendEmail ", e);	
		}
	}
	
	public static String getProperty(String key) {

		if (settings == null) {
			return null;
		} else {
			return settings.getProperty(key);
		}
		
	}

	public Set<Object> getKeys() {

		if (settings == null) {
			return null;
		} else {
			return settings.keySet();
		}

	}

	
}