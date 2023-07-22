package com.mailserver.file;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;  

public class MSFile0 {

	public static void main(String[] args) throws IOException { 
		MSFile0 msfile = new MSFile0();  
		String c=msfile.fileGetContents("/Users/panhw/jmail/1681034079_1884_data.txt");
		System.out.println(c);
	}

	public String fileGetContents(String filePath) throws IOException {
		String c= Files.readString(Paths.get(filePath)); 
		return c;
	}
	public List<String> fileGetContentsBylines(String filePath) {
		List<String> allLines = null;
		try {
			allLines = Files.readAllLines(Paths.get(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allLines;
	}
	
	 
	
 

}