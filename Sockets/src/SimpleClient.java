import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Demonstrates server-side and client-side sockets.
 *
 * @see SimpleServer
 * @see SimpleClient
 */
public class SimpleClient extends Thread {

	public SimpleClient() {
		super();
		this.setName("Client");
	}

	@Override
	public void run() {
		String input = null;

		try (
			Socket socket = new Socket("127.0.0.1", SimpleServer.PORT);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		) {
			System.out.println("Client: Started...");

			while (!socket.isClosed()) {
				input = reader.readLine();

				writer.println(input);
				writer.flush();

				if (input.equals(SimpleServer.EOT)) {
					System.out.println("Client: Ending client.");
					socket.close();
				}
				else if (input.equals(SimpleServer.EXIT)) {
					System.out.println("Client: Shutting down server.");
					socket.close();
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		SimpleClient client = new SimpleClient();
		client.start();
		client.join();
	}
}