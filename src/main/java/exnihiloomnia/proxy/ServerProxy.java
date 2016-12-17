package exnihiloomnia.proxy;

//Commands that only execute on the server.
public class ServerProxy extends Proxy {

    @Override
    public boolean isClient() {
        return false;
    }
}
