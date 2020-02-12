package onbon.bx05.tutorial.rs232;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GScreenRS;
import onbon.bx05.Bx5GScreenRS.BaudRate;

public class SimpleCommand {

    public static void main(String[] args) throws Exception {
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenRS screen = new Bx5GScreenRS("MyScreen");
        if (!screen.connect("COM3", BaudRate.RATE_57600)) {
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

        //
        screen.disconnect();

    }
}
