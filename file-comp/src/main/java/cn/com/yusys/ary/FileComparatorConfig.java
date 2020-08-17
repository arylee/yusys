package cn.com.yusys.ary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.yusys.common.ConfigureFile;

public class FileComparatorConfig extends ConfigureFile {
	
	private static Logger logger = LoggerFactory.getLogger(FileComparatorConfig.class);
	
	private static final String FILENAME = "file-comp.conf";
	
	private static final String LEFT_FILE = "left-file";
	
	private static final String RIGHT_FILE = "right-file";
	
	private static final String OUTPUT_FILE = "output-file";
	
	private static final String KEY_START_POS = "key-start-pos";
	
	private static final String KEY_END_POS = "key-end-pos";
	
	private static FileComparatorConfig instance;
	
	private String leftFile;
	
	private String rightFile;
	
	private String outputFile;
	
	private int keyStartPos;
	
	private int keyEndPos;
	
	public static synchronized FileComparatorConfig getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new FileComparatorConfig();
		}
		return instance;
	}

	private FileComparatorConfig() {
		super(FILENAME);
		keyStartPos = 0;
		keyEndPos = 0;
		if (prop != null) {
			leftFile = prop.getProperty(LEFT_FILE);
			rightFile = prop.getProperty(RIGHT_FILE);
			outputFile = prop.getProperty(OUTPUT_FILE);
			try {
				keyStartPos = Integer.parseInt(prop.getProperty(KEY_START_POS));
			} catch (Exception e) {
				logger.warn(String.format("Got invalid key-start-pos:[%s].", prop.getProperty(KEY_START_POS)));
			}
			try {
				keyEndPos = Integer.parseInt(prop.getProperty(KEY_END_POS));
			} catch (Exception e) {
				logger.warn(String.format("Got invalid key-end-pos:[%s].", prop.getProperty(KEY_END_POS)));
			}
		}
	}

	public int getKeyEndPos() {
		return keyEndPos;
	}

	public void setKeyEndPos(int keyEndPos) {
		this.keyEndPos = keyEndPos;
	}

	public String getLeftFile() {
		return leftFile;
	}

	public String getRightFile() {
		return rightFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public int getKeyStartPos() {
		return keyStartPos;
	}

}
