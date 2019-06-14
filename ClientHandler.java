package ue09_tcp_server_factorize;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
class ClientHandler {
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private final Socket cltSocket;
    private BufferedWriter out = null;
    private BufferedReader in = null;
    private boolean closed = false;
    ClientHandler(Socket cltSocket) {
        this.cltSocket = cltSocket;
        System.out.println("Client: " + this.cltSocket.getInetAddress() + this.cltSocket.getPort() + " connected!");
        new Thread(this::handleClient).start();
    }

    private void handleClient() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(cltSocket.getOutputStream(), CHARSET));
            in = new BufferedReader(new InputStreamReader(cltSocket.getInputStream(), CHARSET));
            out.write("Welcome to the Factorize Server!\r\nType \"exit\" if you want to quit!.\r\n");
            out.flush();
            while (!closed) {
                out.write("\r\nWhich Long-Number should be factorized? ");
                out.flush();
                String temp = in.readLine();
                if (!temp.trim().isEmpty()) {
                    if (temp.equals("exit")) {
                        out.write("Bye!\n");
                        out.flush();
                        this.disconnect();
                        cltSocket.close();
                        System.out.println("Client: " + this.cltSocket.getInetAddress() + this.cltSocket.getPort() + " disconnected!");
                        break;
                    } else if (temp.equals("?")) {
                        if (!Server.isFinished) {
                            out.write("At this time " + Server.getPrimSize() + " primes were found after " + Server.currentTime + "ms (largest: " + Server.getLargestPrime() + ")");
                        } else {
                            out.write("All " + Server.getPrimSize() + " prime numbers were found after " + Server.currentTime + "ms (largest: " + Server.getLargestPrime() + ")");
                        }
                        out.flush();
                    } else {
                        try {
                            if (Long.parseLong(temp) > 0) {
                                try {
                                    out.write(Server.factorize(Long.parseLong(temp)));
                                    out.flush();
                                } catch (ArithmeticException e) {
                                    out.write("Factorizing is not ready. Please wait a bit.");
                                    out.flush();
                                }
                            } else {
                                throw new NumberFormatException("Negative Number");
                            }
                        } catch (NumberFormatException e) {
                            out.write("NumberFormatError: " + temp + " is not a (positive) long-number");
                            out.flush();
                        }
                    }
                }
            }
        } catch (SocketException | NullPointerException e) {
            System.out.println("Client: " + this.cltSocket.getInetAddress() + this.cltSocket.getPort() + " disconnected!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            closed = true;
            this.in.close();
            this.out.close();
            this.cltSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
