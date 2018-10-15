package com.visa.vau.issr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Iterator;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;

public class PropertyLookup {
	private static String propertyFilePath = null;
	private static CombinedConfiguration properties = new CombinedConfiguration();
	private static PropertyLookup INSTANCE = null;
	final static Logger logger = Logger.getLogger(PropertyLookup.class);

	public static String getProperty(String property) throws Exception {
		if (properties == null || properties.isEmpty()) {
			loadProperties();
		}
		return (String) properties.getString(property);
	}

	private static void loadProperties() throws Exception {
		try {
			if (properties == null || properties.isEmpty()) {
				if (propertyFilePath == null || propertyFilePath.isEmpty()) {

					if (System.getProperty("properties.directory") != null
							&& !System.getProperty("properties.directory").isEmpty()) {
						File file = new File(System.getProperty("properties.directory"));
						FilenameFilter filterPropertiesFile;
						String[] propertiesFiles;
						filterPropertiesFile = new WildcardFileFilter("*.properties");
						propertiesFiles = file.list(filterPropertiesFile);

						for (String fileName : propertiesFiles) {
							propertyFilePath = file + File.separator + fileName;
							properties.addConfiguration(new PropertiesConfiguration(propertyFilePath));
						}

					}else {
						propertyFilePath = System.getProperty("properties.file");
						properties.addConfiguration(new PropertiesConfiguration(propertyFilePath));
					}
				} 
				Iterator<String> itr = properties.getKeys();
				while (itr.hasNext()) {
					String key = itr.next();
					logger.info("Key=[" + key + "] Value=[" + properties.getString(key) + "]");
					// System.out.println("Key=["+key+"]
					// Value=["+properties.getString(key)+"]");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static CombinedConfiguration getPropertiesObject() throws Exception {
		if (properties == null || properties.isEmpty()) {
			loadProperties();
		}
		return properties;
	}

	public static PropertyLookup getInstance() throws Exception {
		if (null == INSTANCE) {
			synchronized (PropertyLookup.class) {
				if (null == INSTANCE) {
					try {
						INSTANCE = new PropertyLookup();
					} catch (Exception e) {
						INSTANCE = null;
						e.printStackTrace();

					}
				}
			}
		}
		return INSTANCE;
	}

	private PropertyLookup() throws ConfigurationException, Exception {
		if (properties == null || properties.isEmpty()) {
			loadProperties();
		}
	}

	public String[] getValueAsStringArray(String string) {
		return properties.getStringArray(string);
	}

	public static FileInputStream loadFiles(String propertyFile) {
		FileInputStream fileInput = null;

		try {
			StringBuffer lookupFile = new StringBuffer();
			lookupFile.append(System.getProperty("properties.directory"));
			lookupFile.append(System.getProperty("file.separator"));
			lookupFile.append(propertyFile);

			File file = new File(lookupFile.toString());
			fileInput = new FileInputStream(file);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileInput;
	}

	public static void loadProperties(String propertyFile) {
		try {
			properties.addConfiguration(new PropertiesConfiguration(propertyFile));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void setProperties(String propertyFile, String key, String value) throws ConfigurationException {
		PropertiesConfiguration config = new PropertiesConfiguration(propertyFile);
		config.setProperty(key, value);
		config.save();
	}
	
	public static void setProperty(String key, String value){
		
	}

}

