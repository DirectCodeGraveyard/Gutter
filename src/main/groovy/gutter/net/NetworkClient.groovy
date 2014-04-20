package gutter.net

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

@CompileStatic
class NetworkClient {
    final SocketAddress address
    private Socket socket
    private BufferedReader input
    private PrintStream output

    NetworkClient(String host, int port) {
        address = new InetSocketAddress(host, port)
        socket = new Socket()
    }

    NetworkClient(Socket socket) {
        this.socket = socket
        this.address = socket.remoteSocketAddress
        this.input = socket.inputStream.newReader()
        this.output = new PrintStream(socket.outputStream)
    }

    void connect() {
        if (socket.connected) {
            throw new IllegalStateException("Socket already connected!")
        }
        socket.connect(address)
        input = socket.inputStream.newReader()
        output = new PrintStream(socket.outputStream)
    }

    void write(byte[] bytes) {
        output.write(bytes)
    }

    void println(String line) {
        output.println(line)
    }

    void print(String string) {
        output.print(string)
    }

    String readLine() {
        input.readLine()
    }

    void eachLine(@ClosureParams(value = SimpleType, options = ["String"]) Closure closure) {
        input.eachLine(closure)
    }

    List<String> readLines() {
        input.readLines()
    }

    void close() {
        socket.close()
    }

    byte read() {
       input.read()
    }

    void read(char[] chars) {
        input.read(chars)
    }
}
