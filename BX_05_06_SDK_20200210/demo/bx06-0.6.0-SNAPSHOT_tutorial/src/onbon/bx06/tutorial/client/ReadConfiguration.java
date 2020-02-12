package onbon.bx06.tutorial.client;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.message.file.ControllerConfigFile;

public class ReadConfiguration {

    public static void main(String[] args) throws Exception {
        Bx6GEnv.initial("log.properties");

        //
        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
        if (!screen.connect("192.168.5.67", 5005)) {
            System.out.println("connect failed");
            return;
        }

        ControllerConfigFile config = screen.readConfig();
        System.out.println(config);

        //
        screen.disconnect();

    }

}
