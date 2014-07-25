package me.lumpchen.sledge.pdf.syntax.lang;


public abstract class PObject {

	public static enum ClassType {
		Array("Array"), 
		Boolean("Boolean"), 
		Dict("Dict"), 
		String("String"), 
		Name("Name"),
		Number("Number"), 
		Null("Null"), 
		Stream("Stream"),
		IndirectObject("Indirect Object"), 
		IndirectRef("Indirect Ref");

		private String typeString;
		
		ClassType(String typeString) {
			this.typeString = typeString;
		}
		
		public String toString() {
			return this.typeString;
		}
	};

	protected PObject parent;
	protected ClassType classType;

	protected PObject() {
	}

	public PObject getParent() {
		return this.parent;
	}

	public void setParent(PObject parent) {
		this.parent = parent;
	}

	public ClassType getClassType() {
		return this.classType; 
	}
}
