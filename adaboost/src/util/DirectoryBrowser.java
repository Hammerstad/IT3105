package util;

import java.io.File;

public class DirectoryBrowser {

	public static String resources(){
		StringBuilder sb = new StringBuilder();
        File[] filesAvailable = new File("./resources/").listFiles();
        for(File file : filesAvailable){
        	String filename = file.getName();
        	sb.append(filename.substring(0, filename.length()-4)).append('\t');
        }
        return sb.toString();
	}
}
