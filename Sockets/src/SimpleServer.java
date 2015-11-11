import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Demonstrates server-side and client-side sockets.
 *
 * @see SimpleServer
 * @see SimpleClient
 */
public class SimpleServer extends Thread {
	public static final int PORT = 5554;
	public static final String EOT = "EOT";
	public static final String EXIT = "SHUTDOWN";

	public SimpleServer() {
		super();
		this.setName("Server");
	}

	@Override
	public void run() {
		String input = null;

		try (ServerSocket server = new ServerSocket(PORT);) {
			while (!server.isClosed()) {
				System.out.println("Server: Waiting for connection...");

				try (
					Socket socket = server.accept();
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				) {
					while (!socket.isClosed()) {
						input = reader.readLine();
						System.out.println("Server: " + input);

						if (input.equals(EOT)) {
							System.out.println("Server: Closing socket.");
							socket.close();
						}
						else if (input.equals(EXIT)) {
							System.out.println("Server: Shutting down.");
							socket.close();
							server.close();
						}
					}
				}

				System.out.println("Server: Client disconnected.");

				/*
				 * Note that this inner try-with-resources block does not have
				 * a catch block. It will still auto-close the resources, and
				 * the exceptions thrown will eventually be caught by the
				 * outer catch block.
				 */
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		SimpleServer server = new SimpleServer();
		server.start();
		server.join();
	}
}