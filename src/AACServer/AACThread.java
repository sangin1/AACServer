package AACServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class AACThread extends Thread{
	final static String dbconnect = "jdbc:mysql://localhost:3306/accdb?useUnicode=true&serverTimezone=UTC";
	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	StringBuilder classlist = new StringBuilder();
	StringBuilder classCode = new StringBuilder();
	StringBuilder wordlist = new StringBuilder();
	StringBuilder wordlist2 = new StringBuilder();
	StringBuilder allword = new StringBuilder();
	StringBuilder imagelist = new StringBuilder();
	StringBuilder imagelist2 = new StringBuilder();
	StringBuilder imageall = new StringBuilder();
	String a;
	String[] result;
	int i = 9;
	public AACThread(Socket socket) {
		this.socket = socket; 
	}
	@SuppressWarnings("unlikely-arg-type")
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e1) { 
				e1.printStackTrace();
			}
			while(true) {
				a = in.readLine(); 
				if(a == null) {
					break;
				}
				String[] input = a.split("--");
				if(input[0].equals("1")==true){
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();	
						ResultSet rs = stmt.executeQuery("select * from class where idCode = 1");
					){
						while(rs.next()){
								classlist.append(rs.getString("class"));
								classlist.append("-");
								classCode.append(rs.getString("classCode"));
								classCode.append("-");	
						}
						classCode.delete(0,2);						
					}catch(Exception e){
						e.printStackTrace();
					}
					if(input[1].equals("1")!=true) {
						try(Connection conn = DriverManager.getConnection(
								dbconnect,"root","1234");
							Statement stmt = conn.createStatement();					 
							ResultSet rs = stmt.executeQuery(String.format("select * from class where idCode = %s",input[1]));
						){
							while(rs.next()){
									classlist.append(rs.getString("class"));
									classlist.append("-");
									classCode.append(rs.getString("classCode"));
									classCode.append("-");	
							}
							classlist.delete(classlist.length()-1,classlist.length());
							classCode.delete(classCode.length()-1,classCode.length());
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					out.println(classlist);
					out.flush();
					
					classlist.setLength(0);
				}else if(input[0].equals("12")==true){
					out.println(classCode);
					out.flush();
				}else if(input[0].equals("2")==true){
					result = classCode.toString().split("-");
					for(i=0;i<result.length;i++) {
						try(Connection conn = DriverManager.getConnection(
								dbconnect,"root","1234");
							Statement stmt = conn.createStatement();
							 
							ResultSet rs = stmt.executeQuery(String.format("select * from word where classCode = %s",result[i]));
						){
							while(rs.next()){
									wordlist.append(rs.getString("word"));
									wordlist.append("-");
									allword.append(rs.getString("word"));
									allword.append("-");
							}
							wordlist.delete(wordlist.length()-1,wordlist.length());
						}catch(Exception e){
							e.printStackTrace();
						}
						wordlist2.append(wordlist);
						wordlist2.append("@");
						wordlist.setLength(0);
					}
					allword.delete(allword.length()-1,allword.length());
					allword.append("@");
					wordlist2.delete(wordlist2.length()-1,wordlist2.length());
					wordlist2.insert(0, allword);
					out.println(wordlist2);
					out.flush();
					allword.setLength(0);
					wordlist.setLength(0);
					wordlist2.setLength(0);
					
				}else if(input[0].equals("3")==true){
					result = classCode.toString().split("-");
					for(i=0;i<result.length;i++) {
						try(Connection conn = DriverManager.getConnection(
								dbconnect,"root","1234");
							Statement stmt = conn.createStatement();
							 
							ResultSet rs = stmt.executeQuery(String.format("select * from word where classCode = %s",result[i]));
						){
							while(rs.next()){
								/*if(rs.getString("image").equals("null")) {
									imagelist.append("b'iVBORw0KGgoAAAANSUhEUgAAArgAAAI0CAIAAAB50M3bAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAm1SURBVHhe7duxahRhGEDR3RVjiJUQAqYxZfAB9P3LxDIQi2BlYRohYCQJODNO1oCF3sLFajin2Bn2Bf7L93+znqZpBQDwN5unJwDAH4QCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQ1tM0Pb0CAAsyH/Hj+HjKbzbr2a8//5VQAIAFGobh9vbuy/XXORGOjw8PDvafbXa5RnD1AABLM03TXAlnZ5eraTX8GD6cf7y/f9htNGCiAABLMwzj1dXn+eX09M38e3Hx6eDl/snJ6x2GCiYKALBA6/XvWcA4TuuVHQUAYOvx6uH73fnZ5dHRq7kSbm6+vXv/dv/F3g4rjUIBABZoGMf7u4fr7TLj6+PDvb3nuy0zCgUAWKb5iB+3p/z260hXDwDA/2aZEQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEACKvVT+EWbNG26YEZAAAAAElFTkSuQmCC'");
									imagelist.append("-");
									System.out.println("1");
									imageall.append("b'iVBORw0KGgoAAAANSUhEUgAAArgAAAI0CAIAAAB50M3bAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAm1SURBVHhe7duxahRhGEDR3RVjiJUQAqYxZfAB9P3LxDIQi2BlYRohYCQJODNO1oCF3sLFajin2Bn2Bf7L93+znqZpBQDwN5unJwDAH4QCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQhAIAkIQCAJCEAgCQ1tM0Pb0CAAsyH/Hj+HjKbzbr2a8//5VQAIAFGobh9vbuy/XXORGOjw8PDvafbXa5RnD1AABLM03TXAlnZ5eraTX8GD6cf7y/f9htNGCiAABLMwzj1dXn+eX09M38e3Hx6eDl/snJ6x2GCiYKALBA6/XvWcA4TuuVHQUAYOvx6uH73fnZ5dHRq7kSbm6+vXv/dv/F3g4rjUIBABZoGMf7u4fr7TLj6+PDvb3nuy0zCgUAWKb5iB+3p/z260hXDwDA/2aZEQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEASEIBAEhCAQBIQgEACKvVT+EWbNG26YEZAAAAAElFTkSuQmCC'");
									imageall.append("-");
								}else {*/
									imagelist.append(rs.getString("image"));
									imagelist.append("-");
									imageall.append(rs.getString("image"));
									imageall.append("-");
								//}
							}
							imagelist.delete(imagelist.length()-1,imagelist.length());
						}catch(Exception e){
							e.printStackTrace();
						}
						imagelist2.append(imagelist);
						imagelist2.append("@");
						imagelist.setLength(0);
					}
					imageall.delete(imageall.length()-1,imageall.length());
					imageall.append("@");
					imagelist2.delete(imagelist2.length()-1,imagelist2.length());
					imagelist2.insert(0, imageall);
					
					int len = imagelist2.toString().getBytes().length;
					System.out.println(imagelist2.toString());
					out.println(len);
					out.flush();
					
					imageall.setLength(0);
					imagelist.setLength(0);
					classCode.setLength(0);
				}else if(input[0].equals("32")==true){
					out.println(imagelist2);
					out.flush();
					imagelist2.setLength(0);
				}
				else if(input[0].equals("addmember")==true){
					String check="0";
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
						 
						ResultSet rs = stmt.executeQuery(String.format("select * from login where id='%s'",
								input[1]));
					){
						if(rs.next()){ 
							check="1";
							out.println(check);
							out.flush();
							continue;
						}

					}catch(Exception e){
						e.printStackTrace();
					}
					
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
					){
						stmt.executeUpdate(String.format("insert into login(id, pw) value ('%s', '%s')",
								input[1],input[2]));
					}catch(Exception e){
						e.printStackTrace();
					}
					out.println(check);
					out.flush();
				}else if(input[0].equals("login")==true){
					String idCode = "0"; 
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
						 
						ResultSet rs = stmt.executeQuery(String.format("select * from login where id='%s' and pw='%s'",
								input[1],input[2]));
					){
						if(rs.next()){ 
							idCode = rs.getString("idCode");
							out.println(idCode);
							out.flush();
							continue;
						}

					}catch(Exception e){
						e.printStackTrace();
					}
					out.println("0");
					out.flush();
				}else if(input[0].equals("addword")==true){					
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
					){
						stmt.executeUpdate(String.format("insert into word(idCode, word, classCode) value (%s, '%s', %s)",
								input[3],input[2],input[1]));
					}catch(Exception e){
						e.printStackTrace();
						out.println('0');
						out.flush();
					}
					out.println('1');
					out.flush();
				}else if(input[0].equals("updateword")==true){					
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234"); 			
							PreparedStatement pstmt = conn.prepareStatement(String.format("update word set word = '%s' where word = '%s' and idCode = %s and classCode = %s",
									input[2], input[4], input[3], input[1]));				
					){ 		 
						pstmt.executeUpdate();   
						 
					}catch(Exception e){
						e.printStackTrace();
						out.println('0');
						out.flush();
					}
					out.println('1');
					out.flush();
				}else if(input[0].equals("delword")==true){					
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
							Statement stmt = conn.createStatement();
							
					){ 		
						stmt.execute(String.format("delete from word where idCode = %s and word = '%s' and classCode = %s",
								input[3],input[2],input[1]));
					}catch(Exception e){
						e.printStackTrace();
						out.println('0');
						out.flush();
					}
					out.println('1');
					out.flush();
				}else if(input[0].equals("addclass")==true){
					String classCode = "";
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
						 
						ResultSet rs = stmt.executeQuery(String.format("select * from class where idCode = %s and class = '%s'",
								input[3], input[1]));
					){
						if(rs.next()){ 
							out.println('1');
							out.flush();
							continue;
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
					){
						stmt.executeUpdate(String.format("insert into class(class,idCode) value ('%s', %s)",
								input[1],input[3]));
					}catch(Exception e){
						e.printStackTrace();
					}
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
						 
						ResultSet rs = stmt.executeQuery(String.format("select * from class where idCode = %s and class = '%s'",
								input[3], input[1]));
					){
						if(rs.next()){ 
							classCode = rs.getString("classCode");
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();
					){
						stmt.executeUpdate(String.format("insert into word(idCode, word, classCode) value (%s, '%s', %s)",
								input[3],input[2],classCode));
					}catch(Exception e){
						e.printStackTrace();
						
					}
					out.println('0');
					out.flush();
				}else if(input[0].equals("updateclass")==true){					
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234"); 			
							PreparedStatement pstmt = conn.prepareStatement(String.format("update class set class = '%s' where idCode = %s and classCode = %s",
									input[2], input[3], input[1]));				
					){ 		 
						pstmt.executeUpdate();   
						 
					}catch(Exception e){
						e.printStackTrace();
						out.println('0');
						out.flush();
					}
					out.println('1');
					out.flush();
				}else if(input[0].equals("delclass")==true){					
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
							Statement stmt = conn.createStatement();
							
					){ 		
						stmt.execute(String.format("delete from class where idCode = %s and classCode = %s",
								input[1],input[2]));
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(input[0].equals("upin")==true){
					try(Connection conn = DriverManager.getConnection(
							dbconnect,"root","1234");
						Statement stmt = conn.createStatement();						 
						ResultSet rs = stmt.executeQuery(String.format("select * from class where idCode = %s",input[1]));
					){
						if(rs.next()){
							classlist.append(rs.getString("class"));
							classlist.append("-");
							classCode.append(rs.getString("classCode"));
							classCode.append("-");
							while(rs.next()){
								classlist.append(rs.getString("class"));
								classlist.append("-");
								classCode.append(rs.getString("classCode"));
								classCode.append("-");	
							}
						}else {
							out.println("0");
							out.flush();
							continue;
						}
						classlist.delete(classlist.length()-1,classlist.length());
						classCode.delete(classCode.length()-1,classCode.length());
					}catch(Exception e){
						e.printStackTrace();
					}
					out.println(classlist);
					out.flush();
					out.println(classCode);
					out.flush();
					classlist.setLength(0);
				}else if(input[0].equals("upin2")==true){
					result = classCode.toString().split("-");
					for(i=0;i<result.length;i++) {
						try(Connection conn = DriverManager.getConnection(
								dbconnect,"root","1234");
							Statement stmt = conn.createStatement();							 
							ResultSet rs = stmt.executeQuery(String.format("select * from word where classCode = %s",result[i]));
						){
							while(rs.next()){
									wordlist.append(rs.getString("word"));
									wordlist.append("-");
							}
							wordlist.delete(wordlist.length()-1,wordlist.length());
						}catch(Exception e){
							e.printStackTrace();
						}
						wordlist2.append(wordlist);
						wordlist2.append("@");
						wordlist.setLength(0);
					}
					wordlist2.delete(wordlist2.length()-1,wordlist2.length());
					wordlist2.insert(0, "없음@");
					out.println(wordlist2);
					out.flush();
					wordlist.setLength(0);
					wordlist2.setLength(0);
					classCode.setLength(0);
				}else if(input[0].equals("cword")==true){
					String cword = "";
					int k,j;
					for(k=0;k<10;k++) {
						for(j=0;j<10;j++) {
							cword+=createWord();
							cword+="-";
						}
						cword+="--";
					}
					out.println(cword);
					out.flush();
				}else if(input[0].equals("wordCheck")==true){
					String wordc = input[1];
					int result = checkWord(wordc);
					out.println(Integer.toString(result));
					out.flush();
				}					
			}
		}catch(IOException e) { 
			System.out.println("클라이언트 처리실패"+e);
		}finally {
			try {
				socket.close();
			}catch(IOException e) {
				System.out.println("소켓종료오류 "+e);
			} 
		}		
	}
	public int checkWord(String word) {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {return null;}
	        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	     }};
		try {
			SSLContext sc;
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			String url = "https://stdict.korean.go.kr/api/search.do?certkey_no=4440&key=26615A34353C0D5A03B4905843A63EE6&type_search=search&req_type=xml&q="+word;
			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("channel");
			Node nNode = nList.item(0);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){				
				Element eElement = (Element) nNode;
				NodeList nlList = eElement.getElementsByTagName("total").item(0).getChildNodes();
				Node nValue = (Node) nlList.item(0);
				if(Integer.parseInt(nValue.getNodeValue())>0) {
					return 1;
				}else {				
					return 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return 0;
	}
	public String createWord() {
		int word = 0;
		int one = 0;
		int two = 0;
		int three = 0;
		Random random = new Random();
		one=random.nextInt(19);
		while(true) {
			two=random.nextInt(22);
			if(two==3 || two==7 || two==15 || two==10)
				continue;
			break;
		}		
		if(random.nextInt(2)==1) {
			three=random.nextInt(8);
			switch(three){			
				case 0:
					three = 1;
					break;
				case 1:
					three = 4;
					break;
				case 2: 
					three = 7;
					break;
				case 3: 
					three = 8;
					break;
				case 4: 
					three = 16;
					break;
				case 5: 
					three = 17;
					break;
				case 6: 
					three = 19;
					break;	
				case 7: 
					three = 21;
					break;
			}
		}else {
			three=0;
		}
		switch(one) {
			case 1: case 4: case 8: case 10: case 13:
				three = 0;
				break;
		}
		switch(two) {
			case 1: case 2: case 3: case 5: case 7: case 9: case 10: case 11: case 14: case 15: case 16: case 17: case 19:
				three = 0;
				break;
		}
		word = (one * 21 * 28) + (two * 28) + three;
		return String.valueOf((char)(word + 0xAC00));
	}
}
