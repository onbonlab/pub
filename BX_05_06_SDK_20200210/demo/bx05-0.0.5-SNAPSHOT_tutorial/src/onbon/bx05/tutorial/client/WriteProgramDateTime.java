package onbon.bx05.tutorial.client;

import java.awt.Font;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.DateStyle;
import onbon.bx05.area.DateTimeBxArea;
import onbon.bx05.area.TimeStyle;
import onbon.bx05.area.WeekStyle;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;

public class WriteProgramDateTime implements BxFileWriterListener<Bx5GScreen> {

    public static void main(String[] args) throws Exception {
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
        if (!screen.connect("192.168.5.7", 5005)) {
            System.out.println("connect failed");
        }

        //
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());

        //
        DateTimeBxArea area1 = new DateTimeBxArea(448, 0, screen.getProfile());
        area1.setFont(new Font("宋体", Font.ITALIC, 16));
        area1.setDateStyle(DateStyle.YYYY_MM_DD_1);
        area1.setTimeStyle(TimeStyle.HH_MM_1);
        area1.setWeekStyle(WeekStyle.CHINESE);
        program.addArea(area1);

        //
        screen.writeProgramAsync(program, new WriteProgramDateTime());
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
