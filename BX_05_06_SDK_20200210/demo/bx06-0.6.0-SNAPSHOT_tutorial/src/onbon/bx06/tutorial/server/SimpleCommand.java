package onbon.bx06.tutorial.server;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GServer;
import onbon.bx06.Bx6GServerListener;

public class SimpleCommand {

    public static void main(String[] args) throws Exception {
        Bx6GEnv.initial("log.properties");

        Bx6GServer server = new Bx6GServer("TEST", 8001,new Bx6M());

        server.addListener(new ConnectionListener());

        server.start();
        System.out.println("waiting...");

        Thread.sleep(120000);

        server.stop();
        System.out.println("done!");
    }

    public static class ConnectionListener implements Bx6GServerListener {

        @Override
        public void connected(String socketId, String controllerAddr, Bx6GScreen screen) {
            System.out.println(socketId + " online:" + controllerAddr);
            System.out.println("ping:     " + screen.ping());
            System.out.println("status:   " + screen.checkControllerStatus());
            System.out.println("frimware: " + screen.checkFirmware());
            System.out.println("id:       " + screen.readControllerId());

        }

        @Override
        public void disconnected(String socketId, String controllerAddr, Bx6GScreen screen) {
        }
    }
}
