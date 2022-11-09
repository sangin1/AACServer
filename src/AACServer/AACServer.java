package AACServer;

import java.net.ServerSocket;

import AACServer.AACThread;


public class AACServer {

	public static void main(String[] args) throws Exception{  
		ServerSocket ss = new ServerSocket(6000);
		ss.setReuseAddress(true);
		try {
			while(true) { 
				AACThread t = new AACThread(ss.accept());
				t.start();
			}
		}finally {
			ss.close();
		}
		
	}
	

}

