package cn.com.yusys.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从本地文件或者是jar包中读取的配置文件基类
 */
public class ConfigureFile {

    private static Logger logger = LoggerFactory.getLogger(ConfigureFile.class);

    public static final String ENCODE = "UTF-8";
    
    public static final String COLON = ":";
    
    public static final String COMMA = ",";
    
    public static final String DOT = ".";

    public static final String EQUAL = "=";
    
    public static final String QUOTE = "\"";
    
    public static final String SLASH = "/";    

	public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	private InputStream fileInputStream;
    
    protected Properties prop;

    protected Scanner scanner;
    
    /**
     * 构造函数
     * @param filename 配置文件的文件名
     */
    public ConfigureFile(String filename) {
        String currentRelativePath = Paths.get("").toAbsolutePath().toString();
        File file = new File(currentRelativePath + File.separator + filename);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
            	logger.error("File not found.");
            }
        } else {
            fileInputStream = ClassLoader.getSystemResourceAsStream(filename);
            if (fileInputStream == null) {
                logger.error("Cannot read file.");
            }
        }
        if (fileInputStream != null) {
            prop = new Properties();
            //缓存输入流，可多次使用
            ByteArrayOutputStream cachedInputStream = inputStreamCache(fileInputStream);
            try {
                prop.load(getInputStream(cachedInputStream));
            } catch (IOException e) {
            	logger.error("Exception caught.", e);
            }
            scanner = new Scanner(getInputStream(cachedInputStream), ENCODE);
        }
    }

    /**
     * 缓存输入流，用于多次使用
     * @param inputStream 源输入流
     * @return 缓存后的字节流
     */
	private ByteArrayOutputStream inputStreamCache(InputStream inputStream) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return byteArrayOutputStream;
	}

	/**
	 * 多次获取输入流
	 * @param cachedByteStream 缓存的字节流
	 * @return 经过缓存处理后的输入流
	 */
	private InputStream getInputStream(ByteArrayOutputStream cachedByteStream) {
		return new ByteArrayInputStream(cachedByteStream.toByteArray());
	}

}
