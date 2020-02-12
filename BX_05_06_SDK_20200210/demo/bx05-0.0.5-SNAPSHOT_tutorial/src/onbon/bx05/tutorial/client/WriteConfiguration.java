package onbon.bx05.tutorial.client;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.file.ControllerConfigBxFile;

public class WriteConfiguration {

    public static void main(String[] args) throws Exception {
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
        if (!screen.connect("192.168.5.7", 5005)) {
            System.out.println("connect failed");
            return;
        }

        //
        screen.reset2Factory();
        Thread.sleep(2000);

        //
        ControllerConfigBxFile bxFile = new ControllerConfigBxFile();
        bxFile.setControllerName("TEST1");
        bxFile.setAddress(new byte[] { (byte) 0xb0, 0x05 });
        bxFile.setScreenWidth(512);
        bxFile.setScreenHeight(80);
        screen.writeConfig(bxFile);
        Thread.sleep(4000);

        //
        screen.checkControllerStatus();
        screen.checkFirmware();
        screen.checkMemVolumes();
        screen.checkHealth();
        screen.syncTime();
        screen.readControllerId();

        //
        screen.disconnect();

    }

}
