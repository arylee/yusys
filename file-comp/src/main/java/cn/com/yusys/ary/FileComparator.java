package cn.com.yusys.ary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileComparator {
	
	private static Logger logger = LoggerFactory.getLogger(FileComparator.class);
	
	private static String ENCODE = "UTF-8";
	
	private enum CompareResult {
		LEFT, MATCH, RIGHT
	}
	
	private int keyStartPos;
	
	private int keyEndPos;
	
	private boolean hasLeftLine;
	
	private boolean hasRightLine;
	
	public void process() {
		FileComparatorConfig config = FileComparatorConfig.getInstance();
		keyStartPos = config.getKeyStartPos() - 1;
		keyEndPos = config.getKeyEndPos();
		try (FileInputStream leftStream = new FileInputStream(config.getLeftFile());				
				FileInputStream rightStream = new FileInputStream(config.getRightFile());) {
			if (leftStream != null && rightStream != null) {
				try (BufferedReader readerLeft = new BufferedReader(new InputStreamReader(leftStream));
						BufferedReader readerRight = new BufferedReader(new InputStreamReader(rightStream));
						FileOutputStream fos = new FileOutputStream(config.getOutputFile());
						BufferedWriter writer = new BufferedWriter(
								new OutputStreamWriter(fos, ENCODE), 4 * 1024);) {
					String leftLine = null;
					String rightLine = null;
					hasLeftLine = ((leftLine = readerLeft.readLine()) != null);
					hasRightLine = ((rightLine = readerRight.readLine()) != null);				
					int count = 0;
					while (hasLeftLine || hasRightLine) {
						switch (compare(leftLine, rightLine)) {
							case LEFT:
								hasLeftLine = ((leftLine = readerLeft.readLine()) != null);
								break;
							case RIGHT:
								writer.write(rightLine);
								writer.newLine();
								count++;
								hasRightLine = ((rightLine = readerRight.readLine()) != null);				
								break;
							case MATCH:
								writer.write(leftLine);
								writer.newLine();
								count++;
								hasLeftLine = ((leftLine = readerLeft.readLine()) != null);
								hasRightLine = ((rightLine = readerRight.readLine()) != null);				
								break;
						}						
						if (count % 1000000 == 0) {
							logger.debug(String.format("[%d] lines had been writen.", count));
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("File not found.", e);
		} catch (IOException e) {
			logger.error("Error while opening file.", e);
		}
	}
	
	private CompareResult compare(String left, String right) {
		if (!hasLeftLine || left == null) {
			return CompareResult.RIGHT;
		} 
		if (!hasRightLine || right == null) {
			return CompareResult.LEFT;
		}
		String keyLeft = left.substring(keyStartPos, keyEndPos);
		String keyRight = right.substring(keyStartPos, keyEndPos);
		int result = keyLeft.compareTo(keyRight);
		if (result < 0) {
			return CompareResult.LEFT;
		} else if (result > 0) {
			return CompareResult.RIGHT;
		}
		return CompareResult.MATCH;
	}

}
