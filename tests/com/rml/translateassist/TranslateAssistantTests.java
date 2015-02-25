package com.rml.translateassist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import model.AppMode;
import model.AppModel;

import org.junit.Before;
import org.junit.Test;


public class TranslateAssistantTests {

	private TranslateAssist translateAssist;

	@Before
	public void setup(){
		this.translateAssist = new TranslateAssist();
	}
	
	@Test
	public void generateTranslationModeParametersRecognisedCorrectly(){
		AppModel appModel;
		
		String inputFile ="Localizable.strings";
		String[] ipArs = new String[] {inputFile};
		
		appModel = translateAssist.determineAppMode(ipArs);
		
		assertTrue(appModel.getAppMode()==AppMode.GENERATE_TRANSLATION_SOURCE);
		assertEquals(appModel.getInputSourceFile(), inputFile);
		assertEquals(appModel.getInputTranslatedFile(), null);
	}

	@Test
	public void processTranslationModeParametersRecognisedCorrectly(){
		AppModel appModel;
		
		String inputFile ="Localizable.strings";
		String translatedInputFile = "translated.strings";
		String[] ipArs = new String[] {inputFile, translatedInputFile};
		
		appModel = translateAssist.determineAppMode(ipArs);
		
		assertTrue(appModel.getAppMode()==AppMode.CREATE_TRANSLATED_OUTPUT);
		assertEquals(appModel.getInputSourceFile(), inputFile);
		assertEquals(appModel.getInputTranslatedFile(), translatedInputFile);
		
	}
	
	@Test
	public void emptyParametersCreatesUnknownAppMode(){
		AppModel appModel;
		
		appModel = translateAssist.determineAppMode(new String[] {});
		
		assertTrue(appModel.getAppMode()==AppMode.UNKNOWN);
		assertEquals(appModel.getInputSourceFile(), null);
		assertEquals(appModel.getInputTranslatedFile(), null);
	}

	@Test
	public void tooManyParametersCreatesUnknownAppMode(){
		AppModel appModel;
		
		appModel = translateAssist.determineAppMode(new String[] {"0","1","2"});
		
		assertTrue(appModel.getAppMode()==AppMode.UNKNOWN);
		assertEquals(appModel.getInputSourceFile(), null);
		assertEquals(appModel.getInputTranslatedFile(), null);
	}
	
}
