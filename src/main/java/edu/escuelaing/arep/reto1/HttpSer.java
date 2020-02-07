package edu.escuelaing.arep.reto1;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

// The tutorial can be found just here on the SSaurel's Blog : 
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class HttpSer  { 
	
	static final File WEB_ROOT = new File(System.getProperty("user.dir") + "/src/main/resources");
	static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "notSupported.html";
    static final int PORT = 36000;
    private static Socket clientSocket;

	
	// verbose mode
	static final boolean verbose = true;
	
	// Client Connection via Socket Class
	private static Socket connect;
	
	public HttpSer(Socket c) {
		connect = c;
	}
	
	public static void main(String[] args) {
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
			
			// we listen until user halts server execution
			while (true) {
				HttpSer myServer = new HttpSer(serverConnect.accept());
				BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
				String fileRequested = null;
				
				try {
					// we read characters from the client via input stream on the socket
					in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
					// we get character output stream to client (for headers)
					out = new PrintWriter(connect.getOutputStream());
					// get binary output stream to client (for requested data)
					dataOut = new BufferedOutputStream(connect.getOutputStream());
					
					// get first line of the request from the client
					String input = in.readLine();
					// we parse the request with a string tokenizer
					StringTokenizer parse = new StringTokenizer(input);
					String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
					// we get file requested
					fileRequested = parse.nextToken().toLowerCase();
					
					
						
						// we return the not supported file to the client
						File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
						int fileLength = (int) file.length();
						String contentMimeType = "text/html";
						//read content to return to client
						byte[] fileData = readFileData(file, fileLength);
							
						// we send HTTP Headers with data to client
						out.println("HTTP/1.1 501 Not Implemented");
						out.println("Server: Java HTTP Server from SSaurel : 1.0");
						out.println("Date: " + new Date());
						out.println("Content-type: " + "text/html");
						out.println("Content-length: " + fileLength);
						out.println(); // blank line between headers and content, very important !
						out.flush(); // flush character output stream buffer
						// file
						dataOut.write(fileData, 0, fileLength);
						dataOut.flush();
						
					
					
				} catch (FileNotFoundException fnfe) {
					
					
				} catch (IOException ioe) {
					System.err.println("Server error : " + ioe);
				} finally {
					try {
						in.close();
						out.close();
						dataOut.close();
						connect.close(); // we close socket connection
					} catch (Exception e) {
						System.err.println("Error closing stream : " + e.getMessage());
					} 
					
					if (verbose) {
						System.out.println("Connection closed.\n");
					}
				}
				
				
			}
			
		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}

	
	
	private static byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
	
	// return supported MIME Types
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
			return "text/html";
		else
			return "text/plain";
	}
	
	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
		File file = new File(WEB_ROOT, FILE_NOT_FOUND);
		int fileLength = (int) file.length();
		String content = "text/html";
		byte[] fileData = readFileData(file, fileLength);
		
		out.println("HTTP/1.1 404 File Not Found");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		
		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();
		
		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}
	}
	
}