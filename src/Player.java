import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import static java.lang.Thread.sleep;

public abstract class Player {

    public static final int PRIME = 11177;

    public static final String ALICE_HOST = "127.0.0.1";
    public static final String BOB_HOST = "127.0.0.1";
    public static final int ALICE_PORT = 50000;
    public static final int BOB_PORT = 50001;

    final String myHost;
    final int myPort;

    private static final int SLEEP_TIME = 1000;


    public Player(String myHost, int myPort) {
        this.myHost = myHost;
        this.myPort = myPort;
    }


    /**
     * The operations this Player does in the protocol.
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract void behave() throws IOException, InterruptedException;


    /**
     * Generates an random int between min (inclusive) and max (inclusive).
     * @param min the lower bound
     * @param max the upper bound
     * @return an random int between min and max (both inclusive)
     */
    public int randomBetween(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }


    /**
     * Returns the positive modulus N of int x.
     * The % operator will return a negative modulus for a negative integer; the mod() method will do, but it is for
     * BigInteger. Therefore I implement this method.
     * @param x
     * @param N
     * @return
     */
    public static int xModN(int x, int N) {
        return x % N >= 0 ? x % N : xModN(x + N, N);
    }


    /**
     * Receives an int from another process. Works as a server in the server-client model.
     * Because in this demo, at any given moment at most one other process is sending data to this process, this method
     * is not done in a multi-threaded way.
     * @return the int this process receives
     * @throws IOException
     */
    int receiveInt() throws IOException {
        try (ServerSocket listener = new ServerSocket(myPort)) {
            int received = -1;
            while (received == -1) {
                Socket socket = listener.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                received = dis.readInt();
                socket.close();
            }
            return received;
        }
    }


    /**
     * Sends an int to another process, which is specified by host and port.
     * @param host the host that the receiver process is on
     * @param port the port that the receiver process is on
     * @param sent the int to be sent to the receiver process
     * @throws IOException
     * @throws InterruptedException
     */
    void sendInt(String host, int port, int sent) throws IOException, InterruptedException {
        sleep(SLEEP_TIME);      // sleep for a few seconds to make sure the receiver process is listening
        try (Socket socket = new Socket(host, port)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(sent);
            dos.flush();
        }
    }

}
