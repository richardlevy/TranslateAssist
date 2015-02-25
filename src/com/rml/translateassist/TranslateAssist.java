/**
 * 
 */
package com.rml.translateassist;

import model.AppMode;
import model.AppModel;

import com.rml.translateassist.generateOutput.TranslationOutputGenerator;
import com.rml.translateassist.generateSource.TranslationSourceGenerator;

/**
 * @author Rid
 *
 */
public class TranslateAssist {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TranslateAssist ta = new TranslateAssist();
		AppModel appModel = ta.determineAppMode(args);
		switch (appModel.getAppMode()) {
		case GENERATE_TRANSLATION_SOURCE:
			ta.generateTranslationSource(appModel);
			break;

		case CREATE_TRANSLATED_OUTPUT:
			ta.createTranslatedOutput(appModel);
			break;

		default:
			System.out.println("TranslateAssist <input source string file> [<input translated file>]");
		}
	}

	private void generateTranslationSource(AppModel appModel) {
		new TranslationSourceGenerator(appModel.getInputSourceFile()).generateTranslationSource();
	}

	private void createTranslatedOutput(AppModel appModel) {
		new TranslationOutputGenerator(appModel.getInputSourceFile(), appModel.getInputTranslatedFile()).mergeTranlations();
	}

	public AppModel determineAppMode(String[] args) {
		AppModel appModel = new AppModel();
		if (args.length == 1) {
			appModel.setAppMode(AppMode.GENERATE_TRANSLATION_SOURCE);
			appModel.setInputSourceFile(args[0]);
		}
		if (args.length == 2) {
			appModel.setAppMode(AppMode.CREATE_TRANSLATED_OUTPUT);
			appModel.setInputSourceFile(args[0]);
			appModel.setInputTranslatedFile(args[1]);
		}
		return appModel;
	}

	
}
