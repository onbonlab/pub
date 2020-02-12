package onbon.bx06.tutorial.client;

import java.awt.Color;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.file.BxFileWriterListener;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.DisplayStyleFactory.DisplayStyle;

public class WriteProgramTextCaptionWithStyle implements BxFileWriterListener<Bx6GScreen> {

    public static void main(String[] args) throws Exception {
        // Bx6GEnv.initial();
        Bx6GEnv.initial("log.properties");

        //
        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
        if (!screen.connect("192.168.5.7", 5005)) {
            System.out.println("connect failed");
        }

        // Style Object, reference java doc
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        //
        TextCaptionBxArea area = new TextCaptionBxArea(448, 0, 64, 32, screen.getProfile());
        area.setFrameShow(false);

        TextBxPage page = new TextBxPage("What do you want to display");
        page.setLineBreak(true);
        page.setForeground(Color.GREEN);
        page.setDisplayStyle(styles[6]);
        area.addPage(page);

        //
        ProgramBxFile pf = new ProgramBxFile("P000", screen.getProfile());
        pf.setFrameShow(true);
        pf.setFrameSpeed(20);
        pf.loadFrameImage(13);
        pf.addArea(area);

        //
        screen.writeProgramAsync(pf, new WriteProgramTextCaptionWithStyle());
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
