import java.io.IOException;

import static java.lang.Thread.sleep;

public class Bob extends Player {

    private int y = -1;
    private int x_b = -1;
    private int y_b = -1;
    private int u_b = -1;
    private int v_b = -1;
    private int w_b = -1;
    private int xa_ua = -1;
    private int ya_va = -1;
    private int xb_ub = -1;
    private int yb_vb = -1;
    private int d = -1;
    private int e = -1;
    private int xy_b = -1;


    public Bob(int y) {
        super(BOB_HOST, BOB_PORT);
        this.y = y;
    }


    @Override
    public void behave() throws IOException, InterruptedException {
        System.out.println();
        System.out.println("I am Bob!");
        System.out.println("The prime number is " + PRIME + ".");
        System.out.println("y = " + y);

        System.out.println("---------------------------------------");

        // Secret share x from Alice
        System.out.println("Receiving x_b from Alice...");
        x_b = receiveInt();
        System.out.println("x_b = " + x_b);

        // Secret share y to Alice
        y_b = randomBetween(0, PRIME);
        System.out.println("y_b = " + y_b);
        int y_a = xModN(y - y_b, PRIME);
        sendInt(ALICE_HOST, ALICE_PORT, y_a);
        System.out.println("Sent y_a to Alice.");

        System.out.println("---------------------------------------");

        // Secret share u from Server
        System.out.println("Receiving u_b from Server...");
        u_b = receiveInt();
        System.out.println("u_b = " + u_b);

        // Secret share v from Server
        System.out.println("Receiving v_b from Server...");
        v_b = receiveInt();
        System.out.println("v_b = " + v_b);

        // Secret share w from Server
        System.out.println("Receiving w_b from Server...");
        w_b = receiveInt();
        System.out.println("w_b = " + w_b);

        System.out.println("---------------------------------------");

        xb_ub = xModN(x_b - u_b, PRIME);
        System.out.println("x_b - u_b = " + xb_ub);
        yb_vb = xModN(y_b - v_b, PRIME);
        System.out.println("y_b - v_b = " + yb_vb);

        System.out.println("Receiving x_a - u_a from Alice...");
        xa_ua = receiveInt();
        System.out.println("x_a - u_a = " + xa_ua);
        System.out.println("Receiving y_a - v_a from Alice...");
        ya_va = receiveInt();
        System.out.println("y_a - v_a = " + ya_va);

        sleep(1000);
        sendInt(ALICE_HOST, ALICE_PORT, xb_ub);
        System.out.println("Sent x_b - u_b to Alice.");
        sendInt(ALICE_HOST, ALICE_PORT, yb_vb);
        System.out.println("Sent y_b - v_b to Alice.");

        System.out.println("---------------------------------------");

        d = xModN(xa_ua + xb_ub, PRIME);
        e = xModN(ya_va + yb_vb, PRIME);
        System.out.println("d = (x_a - u_a) + (x_b - u_b) = " + d);
        System.out.println("e = (x_b - u_b) + (y_b - v_b) = " + e);

        System.out.println("---------------------------------------");

        xy_b = xModN(d * e + d * v_b + e * u_b + w_b, PRIME);
        System.out.println("(x * y)_b = d * e + d * v_b + e * u_b + w_b");
        System.out.printf("          = %d * %d + %d * %d + %d * %d + %d\n", d, e, d, v_b, e, u_b, w_b);
        System.out.printf("          = %d\n", xy_b);

        System.out.println("---------------------------------------");

        System.out.println("If I knew (x * y)_a, then x * y = (x * y)_a + (x * y)_b - d * e");
        System.out.println();
    }


    public static void main(String[] args) {
        Bob bob = new Bob(Integer.parseInt(args[0]));
        try {
            bob.behave();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
