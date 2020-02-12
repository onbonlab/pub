package onbon.bx06.tutorial.client;

import java.awt.Font;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.area.DateStyle;
import onbon.bx06.area.DateTimeBxArea;
import onbon.bx06.area.TimeStyle;
import onbon.bx06.area.WeekStyle;
import onbon.bx06.file.BxFileWriterListener;
import onbon.bx06.file.ProgramBxFile;

public class WriteProgramDateTime implements BxFileWriterListener<Bx6GScreen> {

    public static void main(String[] args) throws Exception {
        // Bx6GEnv.initial();
        Bx6GEnv.initial("log.properties");

        //
        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
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
