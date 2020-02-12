package onbon.bx06.tutorial.client;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GScreenClient;

public class SimpleCommand {

    public static void main(String[] args) throws Exception {
        Bx6GEnv.initial("log.properties", 15000);

        //
        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
        if (!screen.connect("192.168.5.67", 5005)) {
            System.out.println("connect failed");
            return;
        }

        //
        screen.ping();
        screen.checkControllerStatus();
        screen.checkFirmware();
        screen.checkMemVolumes();
        screen.syncTime();
        screen.readControllerId();

        //
        screen.checkHealth();
        screen.turnOff();
        screen.turnOn();
        screen.lock();
        screen.unlock();

        screen.manualBrightness((byte) 16);

        //
        screen.disconnect();

    }
}
