package onbon.bx06.tutorial;

import java.awt.Color;
import java.awt.Font;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.page.TextFileBxPage;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.TextBinary.Alignment;

public class Case1 {

    public static void main(String[] args) throws Exception {
        Bx6GEnv.initial("log.properties", 15000);

        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
        if (!screen.connect("192.168.5.7", 5005)) {
            System.out.println("connect failed");
            return;
        }

        // 增加文本頁
        TextFileBxPage page = new TextFileBxPage("c:\\temp\\test.txt", new Font("隶书", Font.PLAIN, 55), Color.red, Color.black);
        page.setDisplayStyle(DisplayStyleFactory.getStyle(4));
        page.setVerticalAlignment(Alignment.CENTER); // 垂直置中

        // 增加圖文區
        TextCaptionBxArea area = new TextCaptionBxArea(1, 0, 1706, 64, screen.getProfile());
        area.setFrameShow(true);
        area.setFrameStyle(2);
        area.loadFrameImage(6);
        area.addPage(page);

        // 建立節目
        ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());
        program.addArea(area);

        // 傳送
        screen.writeProgram(program);

        Thread.sleep(1000);

        screen.disconnect();
    }
}
