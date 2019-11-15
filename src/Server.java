import java.io.IOException;

public class Server extends Player {

    private int u = -1;
    private int u_a = -1;
    private int u_b = -1;
    private int v = -1;
    private int v_a = -1;
    private int v_b = -1;
    private int w = -1;
    private int w_a = -1;
    private int w_b = -1;


    public Server(String myHost, int myPort) {
        super(myHost, myPort);
    }


    @Override
    public void behave() throws IOException, InterruptedException {
        System.out.println();
        System.out.println("I am Server!");
        System.out.println("The prime number is " + PRIME + ".");
        u = randomBetween(0, PRIME);
        v = randomBetween(0, PRIME);
        w = xModN(u * v, PRIME);
        System.out.println("u = " + u);
        System.out.println("v = " + v);
        System.out.println("w = u * v = " + w);

        System.out.println("---------------------------------------");

        // Secret share u to Alice & Bob
        u_a = randomBetween(0, PRIME);
        u_b = xModN(u - u_a, PRIME);
        sendInt(ALICE_HOST, ALICE_PORT, u_a);
        System.out.println("Sent u_a to Alice.");
        sendInt(BOB_HOST, BOB_PORT, u_b);
        System.out.println("Sent u_b to Bob.");

        // Secret share v to Alice & Bob
        v_a = randomBetween(0, PRIME);
        v_b = xModN(v - v_a, PRIME);
        sendInt(ALICE_HOST, ALICE_PORT, v_a);
        System.out.println("Sent v_a to Alice.");
        sendInt(BOB_HOST, BOB_PORT, v_b);
        System.out.println("Sent v_b to Bob.");

        // Secret share w to Alice & Bob
        w_a = randomBetween(0, PRIME);
        w_b = xModN(w - w_a, PRIME);
        sendInt(ALICE_HOST, ALICE_PORT, w_a);
        System.out.println("Sent w_a to Alice.");
        sendInt(BOB_HOST, BOB_PORT, w_b);
        System.out.println("Sent w_b to Bob.");

        System.out.println("---------------------------------------");

        System.out.println("My job is done!");
        System.out.println("But I don't know anything about x * y.");
        System.out.println(":(");
        System.out.println();
    }


    public static void main(String[] args) {
        Server server = new Server(null, -1);
        try {
            server.behave();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
