package edu.escuelaing.arep.reto1;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;

public class HttpServer {

	static final File WEB_ROOT = new File(System.getProperty("user.dir") + "/src/main/resources");
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
    	 String fileReq = null;
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
    			 fileReq = header.nextToken().toLowerCase();
    			 
    			 //check Method
    			 if (method.equals("GET")) {
    				 /*
    				 System.out.println("method");
    				 File file = new File(WEB_ROOT,DEFAULT_FILE);
    				 sendHeader(out,file,"text/html",outputLine,"200 ok");*/
    				 if (fileReq.endsWith("/")) {
    					 File file = new File(WEB_ROOT,DEFAULT_FILE);
		 				 sendResponse(out,file,"text/html",outputLine,"200 ok");
		 			}else {
		 				File file = new File(WEB_ROOT,fileReq);
		 				if (!file.exists()) {
		 					//impelentar
		 				} else {
		 					
		 					String contentMimeType = defineContentType(fileReq);
		 					//implementar si no se encuentra
		 					sendResponse(out,file,contentMimeType,outputLine,"200 ok");
		 				}
		 			}
    			 } else {
    				 //metodo no permitido
    				 if (fileReq.endsWith("/")) {
    						fileReq += DEFAULT_FILE;
    					}
    				 File file = new File(WEB_ROOT,DEFAULT_FILE);
    				 sendResponse(out,file,"text/html",outputLine,"200 ok");
    			 }
    		 }
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Server error : " + e);
		} finally {
			out.close();
			in.close();
			outputLine.close();
			clientSocket.close();
			serverSocket.close();
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
    
    private static void sendResponse (PrintWriter out,File file, String contentType,BufferedOutputStream outputLine, String answer ) {
    	out.println("HTTP/1.1 "+ answer);
		out.println("Server: Java HTTP Server  : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + contentType);
		out.println("Content-length: " + file.length());
		out.println(); // blank line between headers and content,
		out.flush(); // flush character output stream buffer
		// file
		try {
			String[] type = contentType.split("/");
			System.out.println(contentType);
			if (contentType.equals("image/jpg") && contentType.equals("image/png") ) {
				BufferedImage image = ImageIO.read(file);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		        ImageIO.write(image, type[1], byteArrayOutputStream);
		        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
		        outputLine.write(size);
		        outputLine.write(byteArrayOutputStream.toByteArray());
		        outputLine.flush();
			}else {
				System.out.println("texto");
				outputLine.write(fileDataByte(file), 0, (int) file.length());
				outputLine.flush();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
 
    private static String defineContentType(String fileReq ) {
    	String answer = null;
    	if (fileReq.endsWith(".htm")  ) {
    		answer = "text/html";
    	} else if (fileReq.endsWith(".html")  ) {
    		answer = "text/html";
    	} else if (fileReq.endsWith(".jpg") ) {
    		answer = "image/jpg";
    	} else if (fileReq.endsWith(".png") ) {
    		answer = "image/png";
    	}
    	else {
    		answer = "text/plain";
    	}
    	System.out.println("la respuesta es :  "+fileReq);
    	System.out.println("la termina es :  "+fileReq.endsWith(".html"));
    	System.out.println("la respuesta es :  "+answer);
    	return answer;
    }
    
    
    
}