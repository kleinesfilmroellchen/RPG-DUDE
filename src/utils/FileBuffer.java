package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import rpg.helpers.Functionality;

/**
 * Stores previously accessed files and their contents.
 * @author kleinesfilmroellchen
 * @version 0.0.0009
 * @since 0.0.0009
 */
public final class FileBuffer {

	private static ArrayList<FileData> files = new ArrayList<>();

	/** Wrapper for file information */
	private static class FileData {
		private File	file;
		private String	contents;

		/** Returns a new scanner for the file */
		Scanner scanner() throws FileNotFoundException {
			return new Scanner(file);
		}

		FileData(String path) throws FileNotFoundException, IOException {
			file = new File(path);
			contents = Functionality.readFileContents(path);
		}

		public File getFile() {
			return file;
		}

		public String getContents() {
			return contents;
		}
	}

	private static FileData getFileData(String path) throws FileNotFoundException, IOException {
		Iterator<FileData> matches = files.stream().filter(f -> f.file.getPath().equals(path)).iterator();
		if (matches.hasNext()) return matches.next();
		files.add(new FileData(path));
		return files.get(files.size() - 1);
	}

	public static File getFile(String path) throws FileNotFoundException, IOException {
		return getFileData(path).file;
	}

	public static String getContent(String path) throws FileNotFoundException, IOException {
		return getFileData(path).contents;
	}

	public static Scanner getNewScanner(String path) throws FileNotFoundException, IOException {
		return getFileData(path).scanner();
	}
}
