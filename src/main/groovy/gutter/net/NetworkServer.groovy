package gutter.net

class NetworkServer {
    final String listenHost
    final int listenPort
    final ServerSocket serverSocket
    final List<NetworkClient> clients = []
    private Closure handler

    NetworkServer(String host = "0.0.0.0", int port) {
        this.listenHost = host
        this.listenPort = port
        this.serverSocket = new ServerSocket()
    }

    void listen(Closure callback) {
        serverSocket.bind(new InetSocketAddress(listenHost, listenPort))
        callback()
    }

    void handleClient(Closure closure) {
        if (handler) {
            throw new IllegalStateException("Can't override the existing handler.")
        }
        this.handler = closure
        Thread.startDaemon("ClientHandler[${listenHost}:${listenPort}}]") { ->
            serverSocket.accept(true) { socket ->
                def client = new NetworkClient(socket)
                Thread.currentThread().uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
                    @Override
                    void uncaughtException(Thread t, Throwable e) {
                        clients.remove(client)
                    }
                }
                clients << client
                handler(client)
            }
        }
    }
}
