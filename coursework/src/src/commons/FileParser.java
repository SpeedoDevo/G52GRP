package commons;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class FileParser implements Serializable {
	
	private static final long serialVersionUID = 7720130696459839049L;
	private ArrayList<String[]> raw;
	private String[][] data;
	private String[] headers;
	private boolean firstLineIsHeader = true;

	/** Parses a delimited file to a 2D array
	 * @param filePath path to file
	 * @param delimiter
	 * @throws FileNotFoundException if <code>filePath</code> is not found
	 */
	public FileParser(String filePath, String delimiter) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		raw = new ArrayList<>();
		try {
			while((line = br.readLine()) != null) {
				//eliminate empty lines
				if (!line.equals("")) {
					String[] split = line.split(delimiter); 
					for (int i = 0; i < split.length; i++) {
						split[i] = split[i].trim();
					}
					raw.add(split);
				}
			}
		} catch (IOException e) {
			System.err.println("File couldn't be read.");
		}
		
		
		String[] firstLine = raw.get(0); 
		int startsAt = 0;
		for (int i = 0; i < firstLine.length; i++) {
			//if at least one is parseable as a float then it's not a header 
			try {
				Float.parseFloat(firstLine[i]);
				//if it falls through here we know that the first line is not a header
				firstLineIsHeader = false;
				break;
			} catch (NumberFormatException e) {
				//do nothing, it's just a string
			}
		}

		if (firstLineIsHeader) {
			headers = firstLine;
			startsAt = 1;
		}
		
		data = new String[raw.size()-startsAt][raw.get(0).length];
		
		for (int i = startsAt; i < raw.size(); i++) {
			data[i-startsAt] = raw.get(i);
		}
	}
	
	/** @return parsed 2D array, doesn't contain headers
	 */
	public String[][] getParsed() {
		return data;
	}

	/** @return headers if the parsed file contained it.
	 */
	public String[] getHeaders() {
		return headers;
	}

	/** Check this first and then use {@link FileParser#getHeaders()} to get headers.
	 *  @return true if the file had headers
	 */
	public boolean hasHeader() {
		return firstLineIsHeader;
	}
}
