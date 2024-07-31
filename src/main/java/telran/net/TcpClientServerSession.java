package telran.net;

import java.net.*;
import java.io.*;

public class TcpClientServerSession extends Thread {
    Socket socket;
    Protocol protocol;
    boolean running = true;

    public TcpClientServerSession(Socket socket, Protocol protocol) {
        this.socket = socket;
        this.protocol = protocol;
        try {
            socket.setSoTimeout(60000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        running = false;
    }

    public void run() {
        try (Socket socketResource = socket;
             BufferedReader receiver = new BufferedReader(new InputStreamReader(socketResource.getInputStream()));
             PrintStream sender = new PrintStream(socketResource.getOutputStream())) {

            String line = null;
            while (running && (line = receiver.readLine()) != null) {
                String responseStr = protocol.getResponseWithJSON(line);
                sender.println(responseStr);
                if ("shutdown".equalsIgnoreCase(line.trim())) {
                    shutdown();
                }
            }
        } catch (SocketTimeoutException e) {
            
            System.out.println("Client do not responding. Connection closed");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
