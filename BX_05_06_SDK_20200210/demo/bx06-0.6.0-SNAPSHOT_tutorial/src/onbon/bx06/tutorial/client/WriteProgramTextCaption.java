package onbon.bx06.tutorial.client;

import java.util.List;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.file.BxFileWriterListener;
import onbon.bx06.file.ProgramBxFile;

public class WriteProgramTextCaption implements BxFileWriterListener<Bx6GScreen> {

    public static void main(String[] args) throws Exception {
        Bx6GEnv.initial("log.properties");

        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
        if (!screen.connect("192.168.5.67", 5005)) {
            System.out.println("connect failed");
        }

        //
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());

        //
        TextCaptionBxArea area = new TextCaptionBxArea(96, 0, 32, 32, screen.getProfile());
        area.loadFrameImage(2);
        area.setFrameSpeed(1);

        area.addPage(new TextBxPage("OK G"));

        //
        program.addArea(area);

        //
        screen.writeProgramAsync(program, new WriteProgramTextCaption());
        Thread.sleep(5000);

        //
        List<String> pgs = screen.readProgramList();
        for (String pg : pgs) {
            System.out.println(pg);
        }

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
