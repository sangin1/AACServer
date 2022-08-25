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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



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
						classlist.delete(classlist.length()-1,classlist.length());
						classCode.delete(classCode.length()-1,classCode.length());
						classCode.delete(0,2);
						out.println(classlist);
						out.flush();
						classlist.setLength(0);
					}catch(Exception e){
						e.printStackTrace();
					}	
					
				}else if(input[0].equals("2")==true){
					result = classCode.toString().split("-");
					for(i=0;i<result.length;i++) {
						try(Connection conn = DriverManager.getConnection(
								dbconnect,"root","1234");
							Statement stmt = conn.createStatement();
							 
							ResultSet rs = stmt.executeQuery(String.format("select * from word where idCode = 1 and classCode = %s",result[i]));
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
					wordlist2.setLength(0);;
				}else if(input[0].equals("3")==true){
					result = classCode.toString().split("-");
					for(i=0;i<result.length;i++) {
						try(Connection conn = DriverManager.getConnection(
								dbconnect,"root","1234");
							Statement stmt = conn.createStatement();
							 
							ResultSet rs = stmt.executeQuery(String.format("select * from word where idCode = 1 and classCode = %s",result[i]));
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
					
					out.println(imagelist2);
					out.flush();
					
					
					imageall.setLength(0);
					imagelist.setLength(0);
					imagelist2.setLength(0);
					classCode.setLength(0);
				}else if(input[0].equals("addmember")==true){
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
}
