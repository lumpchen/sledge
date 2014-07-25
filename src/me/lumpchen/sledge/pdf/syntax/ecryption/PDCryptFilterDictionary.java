package me.lumpchen.sledge.pdf.syntax.ecryption;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;

public class PDCryptFilterDictionary {

	private String cfName;
	private PDictionary cryptFilterDictionary = null;

	public PDCryptFilterDictionary(PDictionary cryptFilterDictionary) {
		this.cryptFilterDictionary = cryptFilterDictionary;
		
		if (this.cryptFilterDictionary.size() == 1) {
			Iterator<Entry<PName, PObject>> it = this.cryptFilterDictionary.entryIterator();
			while (it.hasNext()) {
				Entry<PName, PObject> next = it.next();
				this.cfName = next.getKey().getName();
				this.cryptFilterDictionary = (PDictionary) next.getValue();
				break;
			}			
		}
	}
	
	public String getName() {
		return this.cfName;
	}

	public int getLength() {
		return this.cryptFilterDictionary.getValueAsInt(PName.Length, 40);
	}

	public String getCryptFilterMethod() throws IOException {
		return this.cryptFilterDictionary.getValueAsName(PName.CFM).getName();
	}

}
