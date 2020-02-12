package edu.escuelaing.arep;

import java.io.*;
import java.net.ServerSocket;
import java.net.*;

public class HttpServerV {
	
	static final String DEFAULT_FILE = "index.html";
	static final File WEB_ROOT = new File(System.getProperty("user.dir") + "/src/main/resources");


	
	public static void main(String[] args) throws IOException {
		int port = getPort();
		ServerSocket serverSocket = null;
		try { 
		      serverSocket = new ServerSocket(port);
		   } catch (IOException e) {
		      System.err.println("Could not listen on port: " + port);
		      System.exit(1);
		   }
		
		PrintWriter out =null;
    	BufferedReader in =null;
    	String inputLine;
    	BufferedOutputStream outputLine = null;
		Socket clientSocket = null;
		
		while (true) {
			try {
			       System.out.println("Listo para recibir ...");
			       clientSocket = serverSocket.accept();
			   } catch (IOException e) {
			       System.err.println("Accept failed.");
			       System.exit(1);
			   }
			
			
	    	out = new PrintWriter(clientSocket.getOutputStream(), true);
	    	in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	
	    	while ((inputLine = in.readLine()) != null) {
			      System.out.println("Recibí: " + inputLine);
			      
			      if (in.ready()) {
			    	   
			      } else {
			    	  break;
			      }
			}
	    
	    	
	    	outputLine = new BufferedOutputStream(clientSocket.getOutputStream());
	    	
	    	out.print("HTTP/1.1 200 OK\r\n");
			out.print("Server: Java HTTP Server  : 1.0\r\n");
			out.print("Content-type: text/html\r\n");
			out.print("\r\n");
			out.flush();
			File file = new File(WEB_ROOT,DEFAULT_FILE);
			outputLine.write(fileDataByte(file), 0, (int) file.length());
			outputLine.flush();
			//System.out.println("texto");
			    //out.println(outputLine);
			out.close();
			in.close(); 
			clientSocket.close(); 
		}

		
		 
	
	}
	
	/**
     * Retorna el puerto por el que va a escuchar el servidor
     * @return puerto por el que va a escuchar el servidor
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
    
    /**
     * Convierte un archivo a bytes
     * @param file
     * @return un arreglo de bytes con la informacion del archivo
     * @throws IOException
     */
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
    
    
    
}
