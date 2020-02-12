package onbon.bx05.tutorial.client;

import java.awt.Font;
import java.util.Calendar;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.CounterBxArea;
import onbon.bx05.area.unit.CounterBxUnit.CounterBxUnitFormat;
import onbon.bx05.area.unit.CounterBxUnit.CounterBxUnitMode;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;

public class WriteProgramCounter implements BxFileWriterListener<Bx5GScreen> {

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

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);

        CounterBxArea area1 = new CounterBxArea(448, 0, 64, 28, cal.getTime(), screen.getProfile());
        area1.setFont(new Font("Tohoma", Font.ITALIC, 24));
        area1.setMode(CounterBxUnitMode.D);
        area1.setFormat(new CounterBxUnitFormat(false, true, true, true));
        program.addArea(area1);

        //
        screen.writeProgramAsync(program, new WriteProgramCounter());
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
