package onbon.bx06.tutorial.client;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.file.ControllerConfigBxFile;

public class WriteConfiguration {

    public static void main(String[] args) throws Exception {
        Bx6GEnv.initial("log.properties");

        //
        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
        if (!screen.connect("192.168.5.67", 5005)) {
            System.out.println("connect failed");
            return;
        }

        //
        ControllerConfigBxFile bxFile = new ControllerConfigBxFile();
        bxFile.setControllerName("TEST1");
        bxFile.setAddress(new byte[] { (byte) 0xb0, 0x05 });
        bxFile.setScreenWidth(128);
        bxFile.setScreenHeight(96);
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
