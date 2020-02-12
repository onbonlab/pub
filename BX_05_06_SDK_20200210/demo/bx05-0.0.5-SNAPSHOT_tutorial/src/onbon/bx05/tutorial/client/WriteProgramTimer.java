package onbon.bx05.tutorial.client;

import java.awt.Font;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.TimerBxArea;
import onbon.bx05.area.unit.TimerBxUnit.TimerBxUnitFormat;
import onbon.bx05.area.unit.TimerBxUnit.TimerBxUnitMode;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;

public class WriteProgramTimer implements BxFileWriterListener<Bx5GScreen> {

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
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());

        TimerBxArea area1 = new TimerBxArea(448, 0, 64, 28, 100, screen.getProfile());
        area1.setFont(new Font("Tahoma", Font.ITALIC, 20));
        area1.setMode(TimerBxUnitMode.B);
        area1.setFormat(new TimerBxUnitFormat(false, false, true, false));
        program.addArea(area1);

        //
        screen.writeProgramAsync(program, new WriteProgramTimer());
        Thread.sleep(5000);

        //
        screen.readProgramList();

        //
        screen.disconnect();
    }

    @Override
    public void cancel(Bx5GScreen owner, String fileName, Bx5GException ex) {
        ex.printStackTrace();
    }

    @Override
    public void done(Bx5GScreen owner) {

    }

    @Override
    public void fileFinish(Bx5GScreen owner, String fileName, int total) {
        System.out.println(fileName + " fileFinish:" + total);
    }

    @Override
    public void fileWriting(Bx5GScreen owner, String fileName, int total) {
        System.out.println(fileName + " fileWriting:" + total);
    }

    @Override
    public void progressChanged(Bx5GScreen owner, String fileName, int write, int total) {
        System.out.println(fileName + " progressChanged:" + write + "/" + total);
    }
}
