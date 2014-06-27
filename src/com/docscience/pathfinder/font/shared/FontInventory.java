package com.docscience.pathfinder.font.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class FontInventory implements Serializable {

	private TreeMap<String, TreeMap<FontTechnology, FontGroup>> inventory = new TreeMap<String, TreeMap<FontTechnology, FontGroup>>();

	private transient ArrayList<FontGroup> fontGroups;
	private transient ArrayList<String> avalibleFontFamilies;
	private transient ArrayList<String> avalibleFontStyles;

	public List<FontGroup> getFontGroups() {
		updateCache();
		return Collections.unmodifiableList(fontGroups);
	}

	public int getFontGroupCount() {
		updateCache();
		return fontGroups.size();
	}

	public FontGroup getFontGroup(int index) {
		updateCache();
		return fontGroups.get(index);
	}

	public void addFontGroup(FontGroup fontGroup) {
		TreeMap<FontTechnology, FontGroup> groups = inventory.get(fontGroup
				.getFontFamily());
		if (groups == null) {
			groups = new TreeMap<FontTechnology, FontGroup>();
			inventory.put(fontGroup.getFontFamily(), groups);
		}
		groups.put(fontGroup.getTechnology(), fontGroup);
		clearCache();
	}

	public void addFont(String fontFamily, String fontStyle,
			FontTechnology technology, long fontId) {
		TreeMap<FontTechnology, FontGroup> groups = inventory.get(fontFamily);
		if (groups == null) {
			groups = new TreeMap<FontTechnology, FontGroup>();
			inventory.put(fontFamily, groups);
		}

		FontGroup group = groups.get(technology);
		if (group == null) {
			group = new FontGroup(fontFamily, technology);
			groups.put(technology, group);
		}

		group.addEntry(new FontEntry(fontStyle, fontId));
	}

	private void clearCache() {
		fontGroups = null;
		avalibleFontFamilies = null;
		avalibleFontStyles = null;
	}

	private void updateCache() {
		if (fontGroups == null) {
			fontGroups = new ArrayList<FontGroup>();
			for (TreeMap<FontTechnology, FontGroup> groups : inventory.values()) {
				for (FontGroup group : groups.values()) {
					fontGroups.add(group);
				}
			}
		}

		if (avalibleFontFamilies == null || avalibleFontStyles == null) {
			TreeSet<String> families = new TreeSet<String>();
			TreeSet<String> styles = new TreeSet<String>();
			for (TreeMap<FontTechnology, FontGroup> groups : inventory.values()) {
				for (FontGroup group : groups.values()) {
					families.add(group.getFontFamily());
					for (FontEntry entry : group.getEntries()) {
						styles.add(entry.getFontStyle());
					}
				}
			}
			avalibleFontFamilies = new ArrayList<String>(families);
			avalibleFontStyles = new ArrayList<String>(styles);
		}
	}

	public void removeFontGroup(FontGroup fontGroup) {
		fontGroups.remove(fontGroup);
		TreeMap<FontTechnology, FontGroup> groups = inventory.get(fontGroup
				.getFontFamily());
		if (groups != null) {
			groups.remove(fontGroup.getTechnology());
		}
		clearCache();
	}

	public FontGroup findFontGroup(String fontFamily, FontTechnology technology) {
		return findFontGroup(fontFamily, technology, false);
	}

	public FontGroup findFontGroup(String fontFamily,
			FontTechnology technology, boolean exactlyMatch) {
		TreeMap<FontTechnology, FontGroup> groups = inventory.get(fontFamily);
		if (groups == null) {
			return null;
		}

		if (technology != null) {
			FontGroup group = groups.get(technology);
			if (group != null || exactlyMatch) {
				return group;
			}
		}
		return groups.get(groups.firstKey());
	}

	public FontEntry findFontEntry(String fontFamily, String fontStyle,
			FontTechnology technology) {
		return findFontEntry(fontFamily, fontStyle, technology, false);
	}

	public FontEntry findFontEntry(String fontFamily, String fontStyle,
			FontTechnology technology, boolean exactlyMatch) {
		FontGroup group = findFontGroup(fontFamily, technology, exactlyMatch);
		if (group == null) {
			return null;
		}

		FontEntry entry = group.getEntry(fontStyle);
		if (entry != null) {
			return entry;
		}

		if (exactlyMatch) {
			return null;
		} else {
			return group.getDefaultEntry();
		}
	}

	public FontEntry findFontEntry(FontDescriptor descriptor) {
		return findFontEntry(descriptor, false);
	}

	public FontEntry findFontEntry(FontDescriptor d, boolean exactlyMatch) {
		return findFontEntry(d.getFontFamily(), d.getFontStyle(),
				d.getTechnoloy(), exactlyMatch);
	}

	public ArrayList<String> getAvalibleFontFamilies() {
		updateCache();
		return avalibleFontFamilies;
	}

	public ArrayList<String> getAvalibleFontStyles() {
		updateCache();
		return avalibleFontStyles;
	}

}
