package com.docscience.pathfinder.font.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings("serial")
public class FontEncoding implements Serializable {
	
	public static class Range implements Serializable {
		private int start;
		private int end;
		private short[] glyphIds;
		private boolean compressed;
		
		protected Range() {
			// do nothing here
		}
		
		public Range(int start, int end, int glyphId) {
			this.start = start;
			this.end = end;
			this.glyphIds = new short[] {(short) glyphId};
			this.compressed = false;
		}
		
		public Range(int start, short[] glyphIds) {
			this.start = start;
			this.end = start + glyphIds.length - 1;
			this.glyphIds = glyphIds;
			
			short[] temp = RunLengthEncoding.diffAndEncode(glyphIds);
			if (temp.length < this.glyphIds.length) {
				this.glyphIds = temp;
				this.compressed = true;
			}
		}
		
		public int getStart() {
			return this.start;
		}
		
		public int getEnd() {
			return this.end;
		}
		
		public int getGlyphId(int codepoint) {
			if (this.glyphIds.length == 1) {
				return (this.glyphIds[0] + codepoint) & 0xffff;
			} else {
				if (this.compressed) {
					this.glyphIds = RunLengthEncoding.decodeAndFlat(this.glyphIds);
					this.compressed = false;
				}
				return (this.glyphIds[codepoint - this.start]) & 0xffff;
			}
		}

		@Override
		public String toString() {
			return "Range [start=" + start + ", end=" + end + ", glyphIds="
					+ Arrays.toString(glyphIds) + ", compressed=" + compressed
					+ "]";
		}
		
	}

	private static final int NOT_DEFINED = 0;
	
	private String name;

	private ArrayList<Range> ranges = new ArrayList<Range>();
	
	private transient HashMap<Integer, Integer> glyphIdCache = new HashMap<Integer, Integer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Range> getRanges() {
		return ranges;
	}
	
	public void setRanges(ArrayList<Range> ranges) {
		this.ranges = ranges;
	}

	public int getFirstCodePoint() {
		return this.ranges.get(0).getStart();
	}
	
	public int getLastCodePoint() {
		return this.ranges.get(this.ranges.size() - 1).getEnd();
	}
	
	public int getGlyphId(int codePoint) {
		Integer cachedGlyphId = glyphIdCache.get(codePoint);
		if (cachedGlyphId == null) {
			cachedGlyphId = getGlyphIdReal(codePoint);
			glyphIdCache.put(codePoint, cachedGlyphId);
		}
		return cachedGlyphId;
	}

	private int getGlyphIdReal(int codepoint) {
		for (int i = 0, len = ranges.size(); i < len; i++) {
			Range r = ranges.get(i);
			if (codepoint <= r.getEnd() && codepoint >= r.getStart()) {
				return r.getGlyphId(codepoint);
			}
		}		
		return NOT_DEFINED;
	}
	
	@Override
	public String toString() {
		return "FontEncoding [name=" + name + ", ranges=" + ranges + "]";
	}
	
}
