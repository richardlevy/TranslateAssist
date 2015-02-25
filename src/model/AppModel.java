/**
 * 
 */
package model;

/**
 * @author Rid
 *
 */
public class AppModel {

	private AppMode appMode = AppMode.UNKNOWN;
	private String inputSourceFile;
	private String inputTranslatedFile;
	
	public AppMode getAppMode() {
		return appMode;
	}
	public void setAppMode(AppMode appMode) {
		this.appMode = appMode;
	}
	public String getInputSourceFile() {
		return inputSourceFile;
	}
	public void setInputSourceFile(String inputSourceFile) {
		this.inputSourceFile = inputSourceFile;
	}
	public String getInputTranslatedFile() {
		return inputTranslatedFile;
	}
	public void setInputTranslatedFile(String inputTranslatedFile) {
		this.inputTranslatedFile = inputTranslatedFile;
	}
	
}
