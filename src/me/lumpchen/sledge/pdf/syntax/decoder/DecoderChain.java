package me.lumpchen.sledge.pdf.syntax.decoder;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Stack;

import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class DecoderChain {

	private Stack<Decode> decoderChain;

	public DecoderChain() {
		this.decoderChain = new Stack<Decode>();
	}

	public byte[] decode(PStream stream) {
		List<PName> filters = stream.getFilters();
		if (filters == null || filters.size() == 0) {
			return stream.getStream();
		}
		for (PName filter : filters) {
			this.addDecoder(filter, stream.getDict().getValueAsDict(PName.DecodeParms));
		}
		return this.decode(stream.getStream());
	}
	
	public byte[] decode(byte[] stream) {
		ByteBuffer buf = ByteBuffer.wrap(stream);
		while (true) {
			if (this.decoderChain.isEmpty()) {
				break;
			}
			Decode decoder = this.decoderChain.pop();
			buf = decoder.decode(buf);
		}
		return buf.array();
	}
	
	public void addDecoder(PName filterName, PDictionary decodeParms) {
		Decode decoder = Decode.instance(filterName, decodeParms);
		this.decoderChain.push(decoder);
	}
}
