package me.lumpchen.sledge.pdf.syntax.codec;

import java.util.Stack;

import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class DecoderChain {

	private Stack<Decoder> decoderChain;

	public DecoderChain() {
	}

	public byte[] decode(PDictionary streamDict) {
		return null;
	}
	
	public void addDecoder(PName filterName) {
		Decoder decoder = Decoder.instance(filterName);
		this.decoderChain.push(decoder);
	}
	
}
