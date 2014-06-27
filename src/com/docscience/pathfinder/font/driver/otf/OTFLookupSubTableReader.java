package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public interface OTFLookupSubTableReader {

	OTFLookupSubTable read(int lookupType, TTFRandomReader rd) throws IOException, TTFFormatException;
	
}
