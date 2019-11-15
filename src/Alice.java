import java.io.IOException;

import static java.lang.Thread.sleep;

public class Alice extends Player {

    private int x = -1;
    private int x_a = -1;
    private int y_a = -1;
    private int u_a = -1;
    private int v_a = -1;
    private int w_a = -1;
    private int xa_ua = -1;
    private int ya_va = -1;
    private int xb_ub = -1;
    private int yb_vb = -1;
    private int d = -1;
    private int e = -1;
    private int xy_a = -1;


    public Alice(int x) {
        super(ALICE_HOST, ALICE_PORT);
        this.x = x;
    }


    @Override
    public void behave() throws IOException, InterruptedException {
        System.out.println();
        System.out.println("I am Alice!");
        System.out.println("The prime number is " + PRIME + ".");
        System.out.println("x = " + x);

        System.out.println("---------------------------------------");

        // Secret share x to Bob
        x_a = randomBetween(0, PRIME);
        System.out.println("x_a = " + x_a);
        int x_b = xModN(x - x_a, PRIME);
        sendInt(BOB_HOST, BOB_PORT, x_b);
        System.out.println("Sent x_b to Bob.");

        // Secret share y from Bob
        System.out.println("Receiving y_a from Bob...");
        y_a = receiveInt();
        System.out.println("y_a = " + y_a);

        System.out.println("---------------------------------------");

        // Secret share u from Server
        System.out.println("Receiving u_a from Server...");
        u_a = receiveInt();
        System.out.println("u_a = " + u_a);

        // Secret share v from Server
        System.out.println("Receiving v_a from Server...");
        v_a = receiveInt();
        System.out.println("v_a = " + v_a);

        // Secret share w from Server
        System.out.println("Receiving w_a from Server...");
        w_a = receiveInt();
        System.out.println("w_a = " + w_a);

        System.out.println("---------------------------------------");

        xa_ua = xModN(x_a - u_a, PRIME);
        System.out.println("x_a - u_a = " + xa_ua);
        ya_va = xModN(y_a - v_a, PRIME);
        System.out.println("y_a - v_a = " + ya_va);

        sleep(1000);
        sendInt(BOB_HOST, BOB_PORT, xa_ua);
        System.out.println("Sent x_a - u_a to Bob.");
        sendInt(BOB_HOST, BOB_PORT, ya_va);
        System.out.println("Sent y_a - v_a to Bob.");

        System.out.println("Receiving x_b - u_b from Bob...");
        xb_ub = receiveInt();
        System.out.println("x_b - u_b = " + xb_ub);
        System.out.println("Receiving y_b - v_b from Bob...");
        yb_vb = receiveInt();
        System.out.println("y_b - v_b = " + yb_vb);

        System.out.println("---------------------------------------");

        d = xModN(xa_ua + xb_ub, PRIME);
        e = xModN(ya_va + yb_vb, PRIME);
        System.out.println("d = (x_a - u_a) + (x_b - u_b) = " + d);
        System.out.println("e = (x_b - u_b) + (y_b - v_b) = " + e);

        System.out.println("---------------------------------------");

        xy_a = xModN(d * e + d * v_a + e * u_a + w_a, PRIME);
        System.out.println("(x * y)_a = d * e + d * v_a + e * u_a + w_a");
        System.out.printf("          = %d * %d + %d * %d + %d * %d + %d\n", d, e, d, v_a, e, u_a, w_a);
        System.out.printf("          = %d\n", xy_a);

        System.out.println("---------------------------------------");

        System.out.println("If I knew (x * y)_b, then x * y = (x * y)_a + (x * y)_b - d * e");
        System.out.println();
    }


    public static void main(String[] args) {
        Alice alice = new Alice(Integer.parseInt(args[0]));
        try {
            alice.behave();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
