package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class FontIndex {

	private PName index;
	
	public FontIndex(PName index) {
		this.index = index;
	}
	
	@Override
    public int hashCode() {
    	int hash = 1;
    	hash = hash * 17 + this.index.getName().hashCode();
    	return hash;
    }
    
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
        	return true;
        }
        if (null == obj || !(obj instanceof FontIndex)) {
        	return false;
        }
        
        if (!this.index.equals(((FontIndex) obj).index)) {
        	return false;
        }
        
        return true;
    }
}
