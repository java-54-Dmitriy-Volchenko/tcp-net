package telran.net;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

import static telran.net.TcpConfigurationProperties.*;

public class TcpServer implements Runnable{
	Protocol protocol;
	int port;
	boolean running = true;
	private final ExecutorService executor;
	 
	public TcpServer(Protocol protocol, int port) {
		this.protocol = protocol;
		this.port = port;
		int poolSize = Runtime.getRuntime().availableProcessors();
	    this.executor = Executors.newFixedThreadPool(poolSize);
	}
	
	public void shutdown() {
		running = false;
		executor.shutdown(); 
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
		
		
	    }
		
	}
	
	public void run() {
		try(ServerSocket serverSocket = new ServerSocket(port)){
			
			System.out.println("Server is listening on port " + port);
			serverSocket.setSoTimeout(SOCKET_TIMEOUT);
			while(running) {
				try {
					Socket socket = serverSocket.accept();

					executor.execute(new TcpClientServerSession(socket, protocol, this));
					
				} catch (SocketTimeoutException e) {
					
				}
				
			}
			
				
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}