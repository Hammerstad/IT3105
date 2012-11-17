package util;

import java.io.File;

public class DirectoryBrowser {

	public static String resources() {
		StringBuilder sb = new StringBuilder();
		File[] filesAvailable = new File("./resources/").listFiles();
		for (File file : filesAvailable) {
			String filename = file.getName();
			if (!filename.endsWith(".txt")) {
				continue;
			}
			sb.append(filename.substring(0, filename.length() - 4))
					.append('\t');
		}
		return sb.toString();
	}

	public static File getUnusedFile(String prefix, String postFix) {
		File dir = new File("./resources/images");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File[] files = dir.listFiles();
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
