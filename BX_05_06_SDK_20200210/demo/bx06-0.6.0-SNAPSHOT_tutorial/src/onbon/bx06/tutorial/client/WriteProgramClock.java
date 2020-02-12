package onbon.bx06.tutorial.client;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.area.ClockBxArea;
import onbon.bx06.file.BxFileWriterListener;
import onbon.bx06.file.ProgramBxFile;

public class WriteProgramClock implements BxFileWriterListener<Bx6GScreen> {

    public static void main(String[] args) throws Exception {
        Bx6GEnv.initial("log.properties");

        //
        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
        if (!screen.connect("192.168.5.7", 5005)) {
            System.out.println("connect failed");
            return;
        }

        //
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());
        program.addPlayPeriodSetting(10, 10, 0, 10, 12, 13);
        ClockBxArea area1 = new ClockBxArea(448, 0, 48, 48, screen.getProfile());
        program.addArea(area1);

        //
        screen.writeProgramAsync(program, new WriteProgramClock());
        Thread.sleep(5000);

        //
        screen.readProgramList();

        //
        screen.disconnect();
    }

    @Override
    public void cancel(Bx6GScreen owner, String fileName, Bx6GException ex) {
        ex.printStackTrace();
    }

    @Override
    public void done(Bx6GScreen owner) {

    }

    @Override
    public void fileFinish(Bx6GScreen owner, String fileName, int total) {
        System.out.println(fileName + " fileFinish:" + total);
    }

    @Override
    public void fileWriting(Bx6GScreen owner, String fileName, int total) {
        System.out.println(fileName + " fileWriting:" + total);
    }

    @Override
    public void progressChanged(Bx6GScreen owner, String fileName, int write, int total) {
        System.out.println(fileName + " progressChanged:" + write + "/" + total);
    }
}
