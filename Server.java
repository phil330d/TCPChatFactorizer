package ue09_tcp_server_factorize;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
public class Server {
    private static final int SERVER_PORT = 10_023;
    private static long[] primeNumbers = new long[146144318];
    static boolean isFinished = false;
    public static void main(String[] args) {
        try {
            ServerSocket srvSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on Port " + SERVER_PORT);
            new Thread(Server::primeNumbers).start();
            new Thread(() -> {
                try {
                    while (true) {
                        new ue09_tcp_server_factorize.ClientHandler(srvSocket.accept());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static int numberOfPrimes = 0;
    private static long largestPrime = 3;
    static long currentTime;
    private static void primeNumbers() {
        long startTime = System.currentTimeMillis();
        BitSet bitSet = new BitSet();
        bitSet.set(0, (int) (Math.sqrt(Long.MAX_VALUE) / 3), true);
        long numbers = (long) Math.sqrt(Long.MAX_VALUE);
        for (long i = 5, t = 2; i < Math.sqrt(numbers); i += t, t = 6 - t) {
            currentTime = System.currentTimeMillis() - startTime;
            if (bitSet.get((int) (i / 3))) {
                for (long j = i * i, v = t; j < numbers; j += v * i, v = 6 - v) {
                    bitSet.set((int) (j / 3), false);
                }
            }
        }

        primeNumbers[0] = 2;
        primeNumbers[1] = 3;
        long num = 5;
        int c = 2;
        for (int i = 1; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                currentTime = System.currentTimeMillis() - startTime;
                numberOfPrimes++;
                largestPrime = num;
                primeNumbers[c] = num;
                c++;
            }
            if (i % 2 != 0) {
                num += 2;
            } else {
                num += 4;
            }
        }

        System.out.println("Found all prime numbers in: " + (System.currentTimeMillis() - startTime) + "ms");
        isFinished = true;
    }

    private static List<Long> getPrimFactors(long number) {
        List<Long> primFactors = new ArrayList<>();
        if (number == 1) {
            primFactors.add(1L);
            return primFactors;
        }
        long temp = number;
        for (long primeNumber : primeNumbers) {
            if (primeNumber > Math.sqrt(number)) break;
            while (temp % primeNumber == 0) {
                primFactors.add(primeNumber);
                temp = temp / primeNumber;
            }
        }
        if (temp != 1) primFactors.add(temp);
        return primFactors;
    }

    public static String factorize(long number) {
        if (BigInteger.valueOf(number).isProbablePrime(100)) {
            return "The number " + number + " is a prime number.";
        } else {
            List<Long> primes = getPrimFactors(number);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(number).append(" = ");
            for (Long prime : primes) {
                stringBuilder.append(prime).append("*");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
    }

    public static int getPrimSize() {
        synchronized (primeNumbers) {
            return numberOfPrimes;
        }
    }

    public static long getLargestPrime() {
        return largestPrime;
    }
}
