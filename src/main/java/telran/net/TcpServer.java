package telran.net;
import java.io.IOException;
import java.net.*;
public class TcpServer {
	Protocol protocol;
	int port;
	boolean running = true;
	public TcpServer(Protocol protocol, int port) {
		this.protocol = protocol;
		this.port = port;
	}
	public void shutdown() {
		running = false;
	}
	public void run() {
		try(ServerSocket serverSocket = new ServerSocket(port)){
			serverSocket.setSoTimeout(3000);
			System.out.println("Server is listening on port " + port);
			while (running) {
                try {
                    Socket socket = serverSocket.accept();

                    TcpClientServerSession session = new TcpClientServerSession(socket, protocol);
                    session.start();
                } catch (SocketTimeoutException e) {
                 
                    
               }
           }
            System.out.println("Server stopped");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		}
	
}