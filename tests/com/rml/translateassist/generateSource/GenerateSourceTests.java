package com.rml.translateassist.generateSource;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GenerateSourceTests {

	@Test
	public void canGetValueFromKeyValueLine(){
		TranslationSourceGenerator tsg = new TranslationSourceGenerator("test");
		String ipvalue="this is a test";
		String ip = "\"key\"=\""+ipvalue+"\"";
		
		String value = tsg.getValueFromKeyValueLine(ip);
		
		assertEquals(ipvalue, value);
	}
}
