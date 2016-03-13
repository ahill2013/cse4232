import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by armin1215 on 3/13/16.
 */
public class Client {
    public static void main (final String args[]) {
        Scanner stdin;
        try {
            stdin = new Scanner(System.in);
            System.out.println("Give an inet address as a string, a port, and a command as input");
            while (true) {
                final String inet = stdin.nextLine();
                final int port = Integer.parseInt(stdin.nextLine());
                final String command = stdin.nextLine();

                udpTest(inet, port, command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void udpTest(String inet, int port, String command) throws UnknownHostException {
        final byte[] input = command.getBytes();
        final byte[] data = new byte[32767];


        final InetAddress inetAddress = InetAddress.getByName(inet);
        DatagramSocket sock;
        try {
            sock = new DatagramSocket();
            sock.send(new DatagramPacket(input, input.length, inetAddress, port));
            DatagramPacket receipt = new DatagramPacket(data, data.length);
            sock.receive(receipt);
            System.out.println(new String(receipt.getData(), StandardCharsets.UTF_8) );
            System.out.flush();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
