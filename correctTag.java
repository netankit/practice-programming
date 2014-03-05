	import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class correctTag {
	
	/**
	 * Creates a hashmap of the words given in the dict.txt dictionary file
	 * 
	 * @return dict
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public HashMap<String, String> createMap() throws IOException,
			FileNotFoundException {
		File fileDir = new File("./dict.txt"); // Path to dictionary file
		HashMap<String, String> dict = new HashMap<String, String>();
		BufferedReader in2 = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileDir), "UTF8"));
		String str;
		while ((str = in2.readLine()) != null) {
			str = str.trim();
			String[] val;
			val = str.split("=");
			dict.put(val[0], val[1]);
		}
		in2.close();
		return dict;
	}

	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	/**
	 * lookup function: Assigns correct tag to an alphanumeric inputWord by
	 * performing a dictionary lookup.
	 * 
	 * @param inputWord
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String lookup(String inputWord) throws FileNotFoundException,
			IOException {
		String correctTag = "";
		HashMap<String, String> dict1;
		correctTag ct = new correctTag();
		dict1 = ct.createMap();
		if (dict1.containsKey(inputWord)) {
			correctTag = dict1.get(inputWord);
		} else {
			correctTag = "UNK";
		}
		return correctTag;
	}

	/**
	 * findtag(): Assigns a correct tag to input word. It is called from main()
	 * function
	 * 
	 * @param inputWord
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String findTag(String inputWord)
			throws FileNotFoundException, IOException {
		String correctTag = "";
		inputWord = inputWord.trim();

		String patternAlphanumeric = "^[\\p{L}\\p{N}\\p{Z}\\p{M}]*$";
		String patternFloat = "[-+]?\\p{N}+(\\.\\p{N}+)?$";
		String patternSpl = "\\$\\p{N}+\\$";

		// Create a Pattern object
		Pattern alphanum = Pattern.compile(patternAlphanumeric);
		Pattern flt = Pattern.compile(patternFloat);
		Pattern spc = Pattern.compile(patternSpl);

		// Now create matcher object.
		Matcher m1 = alphanum.matcher(inputWord);
		Matcher m3 = flt.matcher(inputWord);
		Matcher m4 = spc.matcher(inputWord);

		boolean m2;
		correctTag ct1 = new correctTag();
		m2 = ct1.isInteger(inputWord);

		// && !m2 && !m3.find() && !m4.find()

		if (m1.find() && !m2) {
			correctTag = lookup(inputWord);
		} else if (m2) {
			correctTag = "NUM";
		} else if (m3.find()) {
			correctTag = "FLT";
		} else if (m4.find()) {
			correctTag = "SPC";
		}

		else {
			correctTag = "NULL";
		}
		return correctTag;
	}

	/**
	 * Main function: Reads input.txt in utf and writes output.txt in utf too
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// FILE READ : UTF8
			File fileDir = new File("./input.txt"); // Path to input file
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileDir), "UTF8"));

			// FILE READ : UTF-8 -- note how its written here
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("output.txt"), "UTF-8")); // output
																	// file name
			String str;
			String[] wordInDoc;
			String outputString;
			String tag = "";
			// READING Input File
			while ((str = in.readLine()) != null) {
				wordInDoc = str.split(" ");
				for (String word : wordInDoc) {
					tag = findTag(word); // Calls the findTag function which
											// finds the correct tag.
					outputString = word + "|" + tag + " ";
					out.write(outputString); // Writes output string to file
				}
			}
			// Closing input and output streams.
			in.close();
			out.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}