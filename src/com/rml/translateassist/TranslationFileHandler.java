package com.rml.translateassist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class TranslationFileHandler {

	private static final String FILE_FOR_TRANSLATION_SUFFIX = "_for_translation.txt";
	private static final String INPUT_WITH_IDS_SUFFIX = "_with_translation_ids.txt";
	public static final String AUTO_TRANSLATE_ID_LINE = "//########";

	public String getValueFromKeyValueLine(String line) {
		// third quote
		int startOfValue = line.indexOf("=\"") + 2;
		if (startOfValue == 1) {
			startOfValue = line.indexOf("= \"") + 3;
		}
		int endOfValue = line.lastIndexOf("\"");

		return line.substring(startOfValue, endOfValue);
	}

	public void writeLine(String line, BufferedWriter bw) throws IOException {
		bw.write(line);
		bw.newLine();
	}
	
	public boolean isKeyValueLine(String line) {
		int countQuotes = line.length() - line.replace("\"", "").length();
		boolean startsWithQuote = line.startsWith("\"");

		return countQuotes == 4 && line.contains("=") && startsWithQuote;
	}
	
	public boolean isAutoTranslateIDLine(String line) {
		if (line != null) {
			String lineUpper = line.toUpperCase();
			if (lineUpper.startsWith(AUTO_TRANSLATE_ID_LINE)) {
				return true;
			}
			String lineNoSpace = lineUpper.replaceAll("\\s+", "");
			if (lineNoSpace.startsWith(AUTO_TRANSLATE_ID_LINE)){
				return true;
			}
			byte[] b = lineUpper.getBytes();
			String newLine;
			try {
				newLine = new String(b, "UTF-8");
				String newLineNoSpace = newLine.replace("\\s+", "");
				if (newLineNoSpace.startsWith(AUTO_TRANSLATE_ID_LINE)){
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return false;
	}

	public void writeTranslateIDAndKeyValue(String autoTranslateLine,
			String line, BufferedWriter bw) throws IOException {
		writeLine(autoTranslateLine, bw);
		bw.write(line);
		bw.newLine();
	}

	public String createFilenameForTranslationSource(String inputFilename) {
		return inputFilename + FILE_FOR_TRANSLATION_SUFFIX;
	}

	public static String createFilenameForAddedTranslationIDs(
			String inputFilename) {
		return inputFilename + INPUT_WITH_IDS_SUFFIX;
	}
	
	protected String createTranslateIDFor(int translationIDCounter) {
		return AUTO_TRANSLATE_ID_LINE + translationIDCounter;
	}

	protected void deleteFile(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			f.delete();
		}
	}

}
