package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.syntax.document.Page;

public interface RenderObject {

	abstract public void render(VirtualGraphics g2, Page page);
}
