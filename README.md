# TCPChatFactorizer

Creates a "Chat Server" on port 10023 which you can connect to with telnet like this:
'telnet localhost 10023'.

As soon as the server is started, it starts prime number generation with a slightly optimized Sieve of Eratosthenes algorithm. It takes less than 20 seconds to generate all prime numbers up to 3037000499 (the square root of long, so you can factorize every long number).

After ~20 seconds you are able to factorize any (long) number by typing it in the chat.

If you type '?' you get a overview how long it has been running and how many numbers were found while generating, and how long it has took and how many were found after it has finished. 

If you type 'exit' you disconnect from the server.

If you type anything else than a positive long number, it will give you NumberFormatError.
