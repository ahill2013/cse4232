package server;

import asn1objects.ASN1Task;
import datatypes.Task;

import java.util.TimerTask;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by armin1215 on 4/29/16.
 */
public class SendTracked extends TimerTask implements Runnable {
    private Task _t;
    public SendTracked(Task t) {
        _t = t;
    }
    @Override
    public void run() {
        try {
            byte[] buffer;
            DatagramSocket receiver = new DatagramSocket();
            buffer = new ASN1Task(_t).getEncoder().getBytes();
            DatagramPacket send = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(_t.getIP()), _t.getPort());
            receiver.send(send);
        } catch (SocketException e) {
            System.err.println("Failed to send task");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
