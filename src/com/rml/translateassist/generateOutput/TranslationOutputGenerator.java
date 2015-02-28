/**
 * 
 */
package com.rml.translateassist.generateOutput;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.rml.translateassist.TranslationFileHandler;
import com.rml.translateassist.generateSource.TranslationSourceGenerator;

/**
 * @author Rid
 *
 */
public class TranslationOutputGenerator extends TranslationFileHandler {

	private static final String TRANSLATED_SUFFIX = "_translated.txt";
	private static final String TRANSLATED_WITH_IDS_SUFFIX = "_translated_with_ids.txt";
	private String inputSourceFile;
	private String inputTranslatedFile;

	public TranslationOutputGenerator(String inputSourceFile,
			String inputTranslatedFile) {
		this.inputSourceFile = inputSourceFile;
		this.inputTranslatedFile = inputTranslatedFile;
	}

	public void mergeTranlations() {
		try {
			regenerateSourceWithIDs();
			mergeTranslationsWithSourceFileWithIDs();
			stripTranslationIDs();
			tidyUp();
			System.out.println("Translations merged into file - " + createFilenameForMergedTranslations());

		} catch (IOException e) {
			System.out.println ("Error merging translation files - ["+e.getMessage()+"]");
		}
	}

	private void tidyUp() {
		deleteFile(TranslationSourceGenerator
				.createFilenameForAddedTranslationIDs(this.inputSourceFile));
		deleteFile(createFilenameForMergedTranslationsWithIDs());
	}

	private void regenerateSourceWithIDs() throws IOException {
		File sourceWithIDs = new File(
				TranslationSourceGenerator
						.createFilenameForAddedTranslationIDs(this.inputSourceFile));
		if (!sourceWithIDs.exists()) {
			// Regenerate the ID file
			TranslationSourceGenerator sourceGenerator = new TranslationSourceGenerator(
					this.inputSourceFile);
			sourceGenerator.createTranslateIDsFile();
			// System.out.println("Regenerated source file with translation IDs");
		}
	}

	private void stripTranslationIDs() throws IOException {
		BufferedReader mergedSourceWithIDs = new BufferedReader(
				new FileReader(createFilenameForMergedTranslationsWithIDs()));
		BufferedWriter finalOutput = new BufferedWriter(new FileWriter(
				createFilenameForMergedTranslations()));

		String line;
		while ((line = mergedSourceWithIDs.readLine()) != null) {
			if (!isAutoTranslateIDLine(line)){
				writeLine(line, finalOutput);
			}
		}		
		mergedSourceWithIDs.close();
		finalOutput.flush();
		finalOutput.close();
	}

	public void mergeTranslationsWithSourceFileWithIDs() throws IOException {
		String line;
		int count=0;
		// Read from the source input that's got the IDs added in
		BufferedReader originalSource = new BufferedReader(
				new FileReader(
						TranslationSourceGenerator
								.createFilenameForAddedTranslationIDs(this.inputSourceFile)));
		BufferedReader translations = new BufferedReader(new FileReader(
				this.inputTranslatedFile));
		BufferedWriter mergedOutput = new BufferedWriter(new FileWriter(
				createFilenameForMergedTranslationsWithIDs()));
		String currentAutoTranslateLine = null;
		while ((line = translations.readLine()) != null) {
			if (isAutoTranslateIDLine(line)) {
				// Google translate adds in spaces so remove
				currentAutoTranslateLine = line.replaceAll("\\s+", "").toUpperCase();
			} else {
				// It can only be a translation
				if (currentAutoTranslateLine == null) {
					System.out.println("WARNING: [" + line
							+ "] has no associated translate ID - ignoring");
				} else {
					// We've got an ID and a value, now replace in source
					// file
					String sourceLine;
					Boolean found = false;
					while ((sourceLine = originalSource.readLine()) != null) {
						writeLine(sourceLine, mergedOutput);
						// Found translate ID
						if (sourceLine.equals(currentAutoTranslateLine)) {
							// Read until we get to the key value pair line!
							String keyValue;
							while ((keyValue = originalSource.readLine()) != null) {
								if (isKeyValueLine(keyValue)) {
									// Have an id and a value line, so
									// replace the value with the new value
									String newKV = replaceValue(keyValue, line);
									writeLine(newKV, mergedOutput);
									found = true;
									count++;
									break;
								} else {
									// Just write it out
									writeLine(keyValue, mergedOutput);
								}
							}
							if (found)
								break;
						}
					}
				}

			}
		}
		originalSource.close();
		translations.close();
		mergedOutput.flush();
		mergedOutput.close();
		System.out.println ("Translations merged - " + count);
	}

	private String createFilenameForMergedTranslationsWithIDs() {
		return this.inputSourceFile + TRANSLATED_WITH_IDS_SUFFIX;
	}

	private String createFilenameForMergedTranslations() {
		return this.inputSourceFile + TRANSLATED_SUFFIX;
	}

	private String replaceValue(String keyValue, String newValue) {
		// third quote
		int startOfValue = keyValue.indexOf("=\"") + 2;
		if (startOfValue == 1) {
			startOfValue = keyValue.indexOf("= \"") + 3;
		}

		return keyValue.substring(0, startOfValue) + newValue + "\";";

	}
}
