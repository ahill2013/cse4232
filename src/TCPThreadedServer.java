import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by armin1215 on 3/10/16.
 */
public class TCPThreadedServer implements Runnable {

    private String _dbFile;
    private int _port;
    private boolean _running = true;


    public TCPThreadedServer(int port, String dbFile) {
        _port = port;
        _dbFile = dbFile;
    }


    @Override
    public synchronized void run() {
        try {
            ServerSocket tcpServer = new ServerSocket(_port);

            System.out.println("Waiting for connection from client...\n");
            while (_running) {
                final Thread tcpHandler = new Thread(new TCPHandler(tcpServer.accept(), _dbFile));
                tcpHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        _running = false;
    }
}
