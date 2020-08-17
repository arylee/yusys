package cn.com.yusys.ary;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenFile {
	
	private static Logger logger = LoggerFactory.getLogger(GenFile.class);
	
	private static String ENCODE = "UTF-8";
	
	private static String formatMain(int first, int second) {
		StringBuilder builder = new StringBuilder();
		builder.append("CN00KEY").append(StringUtils.leftPad(String.valueOf(first), 42, "0"))
				.append("MAIN").append(StringUtils.leftPad(String.valueOf(second), 914, "0"));
		return builder.toString();
	}
	
	private static String formatTemp(int first, int second) {
		StringBuilder builder = new StringBuilder();
		builder.append("CN00KEY").append(StringUtils.leftPad(String.valueOf(first), 42, "0"))
				.append("MAIN").append(StringUtils.leftPad(String.valueOf(second), 909, "0")).append("-TAC0");
		return builder.toString();
	}

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
				
				//String outString = String.format("CN00KEY%042dMAIN%0909d-TAC0", mainRowCount, tempRowCount);
				tempWriter.write(formatTemp(mainRowCount, tempRowCount));
				tempWriter.newLine();
				
				while (mainRowCount < mainLimit) {
					mainRowCount++;
					int num = random.nextInt(10000000);
					//outString = String.format("CN00KEY%042dMAIN%0914d", mainRowCount, num);
					mainWriter.write(formatMain(mainRowCount, num));
					if (mainRowCount < mainLimit) {
						mainWriter.newLine();
					}
					if (mainRowCount == (tempRowCount * (mainLimit / tempLimit)) && tempRowCount < tempLimit) {
						tempRowCount++;
						//outString = String.format("CN00KEY%042dMAIN%0909d-TAC0", mainRowCount, tempRowCount);
						tempWriter.write(formatTemp(mainRowCount, tempRowCount));
						tempWriter.newLine();
					}
					if (mainRowCount % (mainLimit / 10) == 0) {
						logger.debug(String.format("Have generated tbspacn0[%d], tbsptac0[%d].", 
								mainRowCount, tempRowCount));
					}
				}
				tempRowCount++;
				mainRowCount++;
				//outString = String.format("CN00KEY%042dMAIN%0909d-TAC0", mainRowCount, tempRowCount);
				tempWriter.write(formatTemp(mainRowCount, tempRowCount));
			}
		} catch (FileNotFoundException e) {
			logger.error("File not found.", e);
		} catch (IOException e) {
			logger.error("Error while opening file.", e);
		}
	}

}
