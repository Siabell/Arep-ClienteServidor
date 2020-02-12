package edu.escuelaing.arep;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;

public class HttpServer {
	
	static final String DEFAULT_FILE = "index.html";
	static final File WEB_ROOT = new File(System.getProperty("user.dir") + "/src/main/resources");

	public static void main(String[] args) throws IOException {
		   ServerSocket serverSocket = null;
		   try { 
		      serverSocket = new ServerSocket(36000);
		   } catch (IOException e) {
		      System.err.println("Could not listen on port: 35000.");
		      System.exit(1);
		   }

		   Socket clientSocket = null;
		   try {
		       System.out.println("Listo para recibir ...");
		       clientSocket = serverSocket.accept();
		   } catch (IOException e) {
		       System.err.println("Accept failed.");
		       System.exit(1);
		   }
		   PrintWriter out = new PrintWriter(
		                         clientSocket.getOutputStream(), true);
		   BufferedReader in = new BufferedReader(
		                         new InputStreamReader(clientSocket.getInputStream()));
		   String inputLine, outputLine;
		   while ((inputLine = in.readLine()) != null) {
		      System.out.println("Recibí: " + inputLine);
		      if (!in.ready()) {break; }
		   }
		   File fileNotFound = new File(WEB_ROOT,DEFAULT_FILE);
		   outputLine = 
				   "HTTP/1.1 200 OK\r\n"
				+ "Access-Control-Allow-Origin: *\r\n"
				+ "Content-Type: text/html\r\n"
		         + "\r\n"
				 + DEFAULT_FILE + inputLine;
		    out.println(outputLine);
		    out.close(); 
		    in.close(); 
		    clientSocket.close(); 
		    serverSocket.close(); 
		  }
    
    
}