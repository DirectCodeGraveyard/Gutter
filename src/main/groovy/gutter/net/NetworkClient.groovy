package gutter.net

import groovy.transform.CompileStatic

import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

@CompileStatic
class NetworkClient {
    final InetSocketAddress address
    private SocketChannel channel

    NetworkClient(String host, int port) {
        address = new InetSocketAddress(host, port)
        channel = new Socket().channel
    }

    SocketChannel getChannel() {
        channel
    }

    void connect() {
        if (channel.connected) {
            throw new IllegalStateException("Socket already connected!")
        }
        channel.connect(address)
    }

    void write(byte[] bytes) {
        channel.write(ByteBuffer.wrap(bytes))
    }

    void writeln(String line) {
        channel.write(ByteBuffer.wrap("${line}\n".bytes))
    }

    void write(String string) {
        channel.write(ByteBuffer.wrap(string.bytes))
    }

    String readLine() {
        String line = ""
        while (true) {
            def buffer = ByteBuffer.allocate(1)
            def amount = channel.read(buffer)
            if (amount == 0) {
                break
            }
            line += buffer.getChar(0)
        }
        line
    }
}
