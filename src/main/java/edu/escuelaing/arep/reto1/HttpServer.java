package edu.escuelaing.arep.reto1;
import java.net.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.io.*;

public class HttpServer {

	static final File WEB_ROOT = new File(".");
	static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final int PORT = 36000;
    private static Socket clientSocket;

    public static void main(String[] args) throws IOException {
    	
    	ServerSocket serverSocket= null;
    	 try {
    		 serverSocket= new ServerSocket(PORT);
    		 System.out.println("Server started, listening on port: "+PORT);
		} catch (Exception e) {
			System.err.println("Could not listen on port: 35000.");
			System.exit(1);
		}
    	
    	 PrintWriter out =null;
    	 BufferedReader in =null;
    	 BufferedOutputStream outputLine = null;
    	 String fileR = null;
    	 try {
    		 while (true) {
    			 clientSocket = serverSocket.accept();
    			 System.out.println("connected");
    			 //output stream to client
    			  out = new PrintWriter(clientSocket.getOutputStream(), true);
    			//read characters from client via input stream on the socket
    			  in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    			 String inputLine;
    			  outputLine = new BufferedOutputStream(clientSocket.getOutputStream());
    			 //header http
    			 inputLine = in.readLine();
    			 StringTokenizer header = new StringTokenizer(inputLine);
    			 String method = header.nextToken().toUpperCase();
    			 //http method
    			 fileR = header.nextToken().toLowerCase();
    			 
    			 //check Method
    			 if (!method.equals("GET")) {
    				 String contentMimeType = "text/html";
    			 }
    		 }
		} catch (Exception e) {
			// TODO: handle exception
		}
    
    }
    
    private static byte[] fileDataByte (File file) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[(int) file.length()];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
    
    private static void sendHeader (PrintWriter out,File file, String contentMimeType,BufferedOutputStream outputLine, String answer ) {
    	out.println("HTTP/1.1 "+ answer);
		out.println("Server: Java HTTP Server  : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + contentMimeType);
		out.println("Content-length: " + file.length());
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		// file
		try {
			outputLine.write(fileDataByte(file), 0, (int) file.length());
			outputLine.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}