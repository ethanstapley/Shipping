
fun main() {
    Thread { startServer() }.apply { isDaemon = true }.start()
    startClient()
}
