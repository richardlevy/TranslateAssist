package com.rml.translateassist.generateSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.rml.translateassist.TranslationFileHandler;

public class TranslationSourceGenerator extends TranslationFileHandler{
	private String inputFile;

	public TranslationSourceGenerator(String inputFile) {
		this.inputFile = inputFile;
	}

	public void generateTranslationSource(){
		try {
			createTranslateIDsFile();
			generateSourceForTranslation();
			tidyUp();
		} catch (IOException e){
			System.out.println ("Error generating translation source - ["+e.getMessage()+"]");
		}
	}
	
	private void tidyUp() {
		deleteFile(createFilenameForAddedTranslationIDs(this.inputFile));
	}

	public void createTranslateIDsFile() throws IOException {
		String line;
		int translationIDCounter = 0;
		BufferedReader br = new BufferedReader(new FileReader(this.inputFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				createFilenameForAddedTranslationIDs(this.inputFile)));
		while ((line = br.readLine()) != null) {
			if (isKeyValueLine(line)) {
				String translateID = createTranslateIDFor(translationIDCounter);
				writeTranslateIDAndKeyValue(translateID, line, bw);
				translationIDCounter++;
			} else {
				writeLine(line, bw);
			}
		}
		br.close();
		bw.flush();
		bw.close();
		System.out.println("Generated translationID count = "
				+ translationIDCounter);
		System.out.println("Temporary source file written to : "
				+ createFilenameForAddedTranslationIDs(this.inputFile));
	}

	public void generateSourceForTranslation() throws IOException {
		String line;
		int idCount = 0;
		// Read from temporary input
		BufferedReader br = new BufferedReader(new FileReader(
				createFilenameForAddedTranslationIDs(this.inputFile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				createFilenameForTranslationSource(this.inputFile)));
		String currentAutoTranslateLine = null;
		while ((line = br.readLine()) != null) {
			if (isAutoTranslateIDLine(line)) {
				if (currentAutoTranslateLine != null) {
					System.out.println("WARNING: " + currentAutoTranslateLine
							+ " has been overriden by " + line);
				}
				currentAutoTranslateLine = line;
			}
			if (isKeyValueLine(line)) {
				if (currentAutoTranslateLine == null) {
					System.out.println("WARNING: [" + line
							+ "] has no associated translate ID - ignoring");
				} else {
					writeTranslateIDAndKeyValue(currentAutoTranslateLine,
							getValueFromKeyValueLine(line), bw);
					idCount++;
					currentAutoTranslateLine = null;
				}

			}
		}
		br.close();
		bw.flush();
		bw.close();
		System.out
				.println("Completed extraction of original source for translation.");
		System.out.println("Total translation keys - " + idCount);
		System.out.println("Filename :  - "
				+ createFilenameForTranslationSource(this.inputFile));
	}


}
