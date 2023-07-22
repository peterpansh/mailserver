package com.mailserver.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MSFile {

	public static void main(String[] args) throws IOException {
		MSFile msfile = new MSFile();
		String c = msfile.fileGetContents("/Users/panhw/jmail/1681816351_7681_data.txt");
		System.out.println(c);
	}

	public String fileGetContents(String filePath) throws IOException {

		StringBuffer sb1 = new StringBuffer("");

		String fileNamex = filePath;
		try (Scanner sc = new Scanner(new FileReader(fileNamex))) {
			while (sc.hasNextLine()) { // 按行读取字符串 
				sb1.append(sc.nextLine());
				sb1.append("\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb1.toString();

	}

	public String fileGetContents_V2_old(String filePath) throws IOException {

		String c = "";
		FileReader fileReader = null;
		try {
			File file = new File(filePath);
			fileReader = new FileReader(file);

			char[] cbuf = new char[5];
			int num;

			while ((num = fileReader.read(cbuf)) != -1) {
				for (int i = 0; i < num; i++) {
					// System.out.print(cbuf[i]);
					c = c + cbuf[i];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("e.getMessage():");
			System.out.println(e.getMessage());
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return c;

	}

}