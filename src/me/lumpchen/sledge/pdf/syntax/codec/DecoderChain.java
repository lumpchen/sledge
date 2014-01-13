package me.lumpchen.sledge.pdf.syntax.codec;

import java.util.List;
import java.util.Stack;

import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class DecoderChain {

	private Stack<Decoder> decoderChain;

	public DecoderChain() {
	}

	public byte[] decode(PStream stream) {
		List<PName> filters = stream.getFilters();
		if (filters == null || filters.size() == 0) {
			return stream.getStream();
		}
		for (PName filter : filters) {
			this.addDecoder(filter);
		}
		return this.decode(stream.getStream());
	}
	
	public byte[] decode(byte[] stream) {
		while (true) {
			if (this.decoderChain.isEmpty()) {
				break;
			}
			Decoder decoder = this.decoderChain.pop();
			stream = decoder.decode(stream);
		}
		return stream;
	}
	
	public void addDecoder(PName filterName) {
		Decoder decoder = Decoder.instance(filterName);
		this.decoderChain.push(decoder);
	}
	
}
