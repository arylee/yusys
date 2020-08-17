package cn.com.yusys.ary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.yusys.common.ConfigureFile;

public class GeneratorConfig extends ConfigureFile {
	
	private static Logger logger = LoggerFactory.getLogger(GeneratorConfig.class);
	
	private static final String FILENAME = "gen-data.conf";
	
	private static String MAIN_FILE	= "main-file";
	
	private static String TEMP_FILE = "temp-file";

	private static String MAIN_LIMIT = "main-limit";
	
	private static String TEMP_LIMIT = "temp-limit";
	
	private static GeneratorConfig instance;
	
	private String mainFile;
	
	private String tempFile;
	
	private int mainLimit;
	
	private int tempLimit;
	
	public static synchronized GeneratorConfig getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new GeneratorConfig();
		}
		return instance;
	}
	
	public GeneratorConfig() {
		super(FILENAME);
		if (prop != null) {
			mainFile = prop.getProperty(MAIN_FILE);
			tempFile = prop.getProperty(TEMP_FILE);
			try {
				mainLimit = Integer.parseInt(prop.getProperty(MAIN_LIMIT));
			} catch (Exception e) {
				logger.warn(String.format("Got invalid main-count:[%s].", prop.getProperty(MAIN_LIMIT)));
			}
			try {
				tempLimit = Integer.parseInt(prop.getProperty(TEMP_LIMIT));
			} catch (Exception e) {
				logger.warn(String.format("Got invalid temp-count:[%s].", prop.getProperty(TEMP_LIMIT)));
			}
		}

	}

	public String getMainFile() {
		return mainFile;
	}

	public String getTempFile() {
		return tempFile;
	}

	public int getMainLimit() {
		return mainLimit;
	}

	public int getTempLimit() {
		return tempLimit;
	}

}
