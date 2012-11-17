package util;

import java.io.File;

/**
 * @author Eirik Mildestveit Hammerstad
 * @author Nicklas Utgaard
 */
public class DirectoryBrowser {

	/**
	 * Prints a string with all available DataSets (that would be all *.txt files in the /resources/ folder.
	 * 
	 * @return String with all files you can choose from.
	 */
	public static String resources() {
		StringBuilder sb = new StringBuilder();
		File[] filesAvailable = new File("./resources/").listFiles();
		for (File file : filesAvailable) {
			String filename = file.getName();
			if (!filename.endsWith(".txt")) {
				continue;
			}
			sb.append(filename.substring(0, filename.length() - 4)).append('\t');
		}
		return sb.toString();
	}

	/**
	 * Used in GraphViz making.
	 * @param prefix - file starts with
	 * @param postFix - file ends with
	 * @return probable file
	 */
	public static File getUnusedFile(String prefix, String postFix) {
		File dir = new File("./resources/images");
		if (!dir.exists()) {
			dir.mkdir();
		}
		String filenameWanted;
		int index = 1;
		while (true) {
			filenameWanted = prefix + index + postFix;
			File wanted = new File(dir, filenameWanted);
			if (!wanted.exists()) {
				return wanted;
			}
			index++;
		}
	}
}
