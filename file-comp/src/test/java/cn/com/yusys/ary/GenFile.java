package cn.com.yusys.ary;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenFile {
	
	private static Logger logger = LoggerFactory.getLogger(GenFile.class);
	
	private static String ENCODE = "UTF-8";
	
	public static void main(String[] args) {
		GeneratorConfig config = GeneratorConfig.getInstance();
		try (FileOutputStream mainStream = new FileOutputStream(config.getMainFile());				
				FileOutputStream tempStream = new FileOutputStream(config.getTempFile());) {
			try (BufferedWriter mainWriter = new BufferedWriter(
					new OutputStreamWriter(mainStream, ENCODE), 4 * 1024);
				BufferedWriter tempWriter = new BufferedWriter(
					new OutputStreamWriter(tempStream, ENCODE), 4 * 1024);) {
				Random random = new Random();
				int mainRowCount = 0;
				int tempRowCount = 1;
				int mainLimit = config.getMainLimit();	
				int tempLimit = config.getTempLimit();
				String outString = String.format("CN00KEY%042d", mainRowCount) + 
						String.format("MAIN%0909d-TAC0", tempRowCount);
				tempWriter.write(outString);
				tempWriter.newLine();
				
				while (mainRowCount < mainLimit) {
					mainRowCount++;
					int num = random.nextInt(10000000);
					outString = String.format("CN00KEY%042d", mainRowCount) + 
							String.format("MAIN%0914d", num);
					mainWriter.write(outString);
					if (mainRowCount < mainLimit) {
						mainWriter.newLine();
					}
					if (mainRowCount == (tempRowCount * mainLimit / tempLimit) && tempRowCount < tempLimit) {
						tempRowCount++;
						outString = String.format("CN00KEY%042d", mainRowCount) + 
								String.format("MAIN%0909d-TAC0", tempRowCount);
						tempWriter.write(outString);
						tempWriter.newLine();
					}
					if (mainRowCount % (mainLimit / 10) == 0) {
						logger.debug(String.format("Have generated tbspacn0[%d], tbsptac0[%d].", 
								mainRowCount, tempRowCount));
					}
				}
				tempRowCount++;
				mainRowCount++;
				outString = String.format("CN00KEY%042d", mainRowCount) + 
						String.format("MAIN%0909d-TAC0", tempRowCount);
				tempWriter.write(outString);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not found.", e);
		} catch (IOException e) {
			logger.error("Error while opening file.", e);
		}
	}

}
