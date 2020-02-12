package onbon.bx05.tutorial.client;

import java.awt.Color;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.DisplayStyleFactory.DisplayStyle;

public class WriteProgramTextCaptionWithStyle implements BxFileWriterListener<Bx5GScreen> {

    public static void main(String[] args) throws Exception {
        // Bx5GEnv.initial();
        Bx5GEnv.initial("log.properties");

        //
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
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
