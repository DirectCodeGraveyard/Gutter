package gutter.test

import gutter.net.NetworkClient
import gutter.net.NetworkServer
import org.junit.Test

class NetworkServerTest {
    @Test
    void testServerToClientMessagingWorks() {
        def server = new NetworkServer(5670)
        server.listen()
        server.handleClient { client ->
            client.println("Hello")
            client.close()
        }
        def client = new NetworkClient("0.0.0.0", 5670)
        client.connect()
        def line = client.readLine()
        assert line == "Hello"
        client.close()
    }
}
