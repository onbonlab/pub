package onbon.bx06.tutorial.client;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.file.BxFileWriterListener;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.utils.TextBinary.Alignment;

public class WriteProgramTextCaption2 implements BxFileWriterListener<Bx6GScreen> {

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
        program.setFrameShow(true);
        program.setFrameSpeed(20);
        program.loadFrameImage(13);

        //
        TextCaptionBxArea area = new TextCaptionBxArea(448, 0, 64, 32, screen.getProfile());
        area.setFrameShow(true);
        area.setFrameSpeed(20);
        area.loadFrameImage(13);

        // code 1: using object
        TextBxPage page = new TextBxPage("�?麼啊!\n好阿\n搞什麼鬼東西");
        page.setHorizontalAlignment(Alignment.FAR);
        area.addPage(page);
        //
        program.addArea(area);

        //
        screen.writeProgramAsync(program, new WriteProgramTextCaption2());
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
