package onbon.bx06.tutorial;

/**
 * Created by TATABA on 2016/9/30.
 */

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.area.DateTimeBxArea;
import onbon.bx06.area.DynamicBxArea;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.WeekStyle;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.cmd.dyn.DynamicBxAreaRule;
import onbon.bx06.file.BxFileWriterListener;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.message.common.ErrorType;
import onbon.bx06.message.led.ReturnControllerStatus;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.DisplayStyleFactory.DisplayStyle;
import onbon.bx06.utils.TextBinary;

public class BxClientDemo implements BxFileWriterListener<Bx6GScreen> {

    public static void main(String[] args) throws Exception {

        int posX = 90;
        int posY = 4;

        //
        // SDK 初始化
        // init the SDK
        //Bx6GEnv.initial("log.properties");
        Bx6GEnv.initial("log.properties", 30000);

        //
        // 其它控制器
        // 创建 screen 对象，用于对控制器进行访问
        // Create a screen object, it will be used to control the led card
        Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());

        //
        // 连接控制器
        // 其中, 192.168.88.199 为控制器的实际 IP，请根据实际情况填写。
        // 如你不知道控制器的 IP 是多少，请先使用 LEDSHOW TW 软件对控制器进行 IP 设置
        // 端口号默认为 5005
        //
        // Connect to the screen, please use the ip of the Controller
        // The default port is 5005
        if (!screen.connect("192.168.5.67", 5005)) {
            System.out.println("connect failed");
            return;
        }

        //
        // ---------------------------------------------------------------------------------

        //
        //
        // 以下为显示屏参数的设置方法
        // ！！！注意：显示屏参数只需要在显示屏第一次安装时使用一次即可。没有必要每次发送节目前都设置一次。
        /*
        ControllerConfigBxFile bxFile = new ControllerConfigBxFile();

        bxFile.setControllerName("TEST1");
        bxFile.setAddress(new byte[] { (byte) 0xb0, 0x05 });

        // 显示屏的宽度（单位：像素）
        bxFile.setScreenWidth(128);
        // 显示屏的高度（单位：像素）
        bxFile.setScreenHeight(96);
        // 将参数文件写入控制器
        screen.writeConfig(bxFile);
        // 通常参数配置完后，控制器会进行重启，需等待一段时间再对控制器进行操作
        Thread.sleep(5000);
         */

        //
        // ---------------------------------------------------------------------------------

        //
        //
        // 以下为一些简单控制命令的使用方法
        // 更多命令可以参考 Javadoc 中的 Bx6GScreen 类
        //
        // some simple commands

        //
        // 开关机
        // turn on/off
        screen.turnOff();       // 关机
        Thread.sleep(2000);
        screen.turnOn();        // 开机

        // 校时
        // sync time with the computer
        screen.syncTime();

        // 调整亮度
        // 亮度值 为 1- 16， 16级为最高亮度
        // change the brightness
        screen.manualBrightness((byte) 8);   // 将屏幕亮度调整至 8
        Thread.sleep(2000);
        screen.manualBrightness((byte) 16);  // 将屏幕亮度调整到 16

        //
        // ---------------------------------------------------------------------------------

        //
        // 可以通过以下接口，回读控制器状态
        // You can read back the status of the controller like this
        Bx6GScreen.Result<ReturnControllerStatus> result1 = screen.checkControllerStatus();
        if (result1.isOK()) {
            ReturnControllerStatus stauts = result1.reply;
            stauts.getBrightness();
            stauts.getRtcDay();
            stauts.getScreenOnOff();
            //
            // status 还有很多其它接口，可以根据实际需求在此调用以获取相应状态
        }
        else {
            ErrorType error = result1.getError();
            System.out.println(error);
        }

        //
        // ---------------------------------------------------------------------------------

        //
        // 可以通过 screen 的 getProfile 方法获取控制器参数
        // 也可通过此方法验证控制器参数是否被正确设置
        Bx6GScreenProfile profile = screen.getProfile();
        org.apache.log4j.Logger.getLogger(BxClientDemo.class).info("screen width : " + profile.getWidth());
        org.apache.log4j.Logger.getLogger(BxClientDemo.class).info("screen height : " + profile.getHeight());

        /*
        // ！！！ 注意 ！！！
        // 如果想提高发送节目的效率
        // 且自己知道显示屏参数的情况下
        // 可以手动创建 profile 对象
        // screen.getProfile()此方式，是通过从控制器回读来自动创建profile的，使用方便
        // 但增加了ＰＣ与控制器之前的交互，从而使得发送效率降低
        profile = new Bx6GScreenProfile(128, 96);
        profile.setMatrixType(Bx6GScreenProfile.ScreenMatrixType.COLOR3BYTE);
         */

        //
        // ---------------------------------------------------------------------------------

        //
        //
        // 以下为创建节目的方法
        // 在以下DEMO中，将创建 P000 和 P001, P002 三个节目
        // We will create 3 programs below, they are called P000, P001, P002

        //
        // 创建节目的通常过程如下：
        // 1. 创建一个 ProgramBxFile 对象（节目文件）
        // 2. 创建一个或多个 Area 对象（区域，可以是图文区，也可以是时间区等）
        // 3. 如果是图文区（TextCaptionBxArea), 可以创建 Page 对象，按页将文本或图片添加到Area中
        // 4. 将 Area 对象添加到节目中
        // 5. 使用 screen 对象将节目下发到控制器

        // The process to create a Prgoram
        // 1. create a ProgramBxFile Object (program file)
        // 2. create 1 or more than 1 Area Object (Area, it can be a Text, Picture, or Time)
        // 3. If you create a text/pic area, you can create Page Object, And then add page to Area object
        // 4. add area object to program object
        // 5. send the programs to the controller

        //
        // 获取显示特技方式列表，此变量会下面用到
        // 关于 style 的定义，请参考 Javadoc 中，相关类（DisplayStyleFactory）的定义
        //
        // Get the display styles list, it will be used below
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);
        for (DisplayStyle style : styles) {
            System.out.println(style);
        }

        //
        // ---------------------------------------------------------------------------------

        //
        // 下面开始创建第一个节目，P000
        // 此节目包括只包括一个图文区, 图文区中包括两个数据页：一页文本，一页图片
        // 显示节目边框
        // 区域显示边框

        //
        // Now we begin to create the first program, P000

        // 创建节目文件
        // Create Program file
        ProgramBxFile p000 = new ProgramBxFile(0, profile);
        // 是否显示节目边框
        // set the program frame show or not
        p000.setFrameShow(true);
        // 节目边框的移动速度
        // frame move speed
        p000.setFrameSpeed(20);
        // 使用第几个内置边框
        // select which frame
        p000.loadFrameImage(7);
        p000.setProgramTimeSpan(20);

        // 创建一个文本区
        // 分别输入其X,Y,W,H
        // 屏幕左上角坐标为 (0, 0)
        // 注意区域坐标和宽高，不要越界

        // Create a Text Area
        // (x, y, w, h)
        TextCaptionBxArea tArea = new TextCaptionBxArea(posX, posY, 32, 32, screen.getProfile());
        // 使能区域边框
        // enable the area frame
        tArea.setFrameShow(true);
        // 使用内置边框3
        // select which frame
        tArea.loadFrameImage(3);

        // 创建一个数据页，并希望显示 “P000” 这几个文字
        // create a page, and add some text to the page
        TextBxPage page = new TextBxPage("P000 - This is the first test");
        // 对文本的处理是否自动换行
        
        // 设置文本水平对齐方式
        page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
        // 设置文本垂直居中方式
        page.setVerticalAlignment(TextBinary.Alignment.CENTER);
        // 设置文本字体
        page.setFont(new Font("consolas", Font.PLAIN, 14));         // 字体
        // 设置文本颜色
        page.setForeground(Color.red);
        // 设置区域背景色，默认为黑色
        page.setBackground(Color.darkGray);
        // 调整特技方式
        page.setDisplayStyle(styles[4]);

        // 调整特技速度
        page.setSpeed(1);
        // 调整停留时间, 单位 10ms
        page.setStayTime(0);
        //
        page.setHeadTailInterval(-2);

        // 将前面创建的 page 添加到 area 中
        tArea.addPage(page);

        // 再创建一个数据页，用于显示图片
        /**
        ImageFileBxPage iPage = new ImageFileBxPage("d:/1.png");
        // 调整特技方式
        iPage.setDisplayStyle(styles[1]);
        // 调整特技速度
        iPage.setSpeed(1);
        // 调整停留时间, 单位 10ms
        iPage.setStayTime(100);

        // 将前面创建的 iPage 添加到 area 中
        tArea.addPage(iPage);
         */

        // 将 area 添加到节目中
        p000.addArea(tArea);

        if (p000.validate() != null) {
            System.out.println("P000 out of range");
            return;
        }

        //
        // ---------------------------------------------------------------------------------

        //
        // 下面开始创建第三个节目，P001
        // 此节目用于展示如何添加时间区
        // 此节目包括只包括一个时间区
        // 不显示节目边框
        // 区域显示边框

        // 创建节目文件
        ProgramBxFile p001 = new ProgramBxFile(1, profile);
        // 是否显示节目边框
        p001.setFrameShow(false);

        //
        // 下面代码创建了一个时间区
        // 注意：只需要输入时间区的起始坐标，区域的宽度和高度SDK会根据字体和显示方式自动计算
        DateTimeBxArea dtArea = new DateTimeBxArea(posX, posY, screen.getProfile());
        // 设置颜色
        dtArea.setForeground(Color.yellow);
        // 多行显示还是单行显示
        dtArea.setMultiline(true);
        //
        // 年月日的显示方式
        // 如果不显示，则设置为 null
        dtArea.setDateStyle(null);
        dtArea.setTimeStyle(null);
        dtArea.setWeekStyle(WeekStyle.ENG);

        //
        // 注意时间区也可像P001中图文区那样设置背景或添加背景区域

        // 将时间区添加到节目中
        p001.addArea(dtArea);
        if (p001.validate() != null) {
            System.out.println("P001 out of range");
            return;
        }

        //
        // ---------------------------------------------------------------------------------

        //
        // 创建一个 list
        ArrayList<ProgramBxFile> plist = new ArrayList<ProgramBxFile>();
        plist.add(p000);
        plist.add(p001);

        //
        screen.deletePrograms();
        screen.deleteAllDynamic();

        //
        // 将节目文件发送到控制器
        // 发送节目有三种方式
        // 可以根据自己的需求进行选择

        //
        // 1. writeProgramsAsync - 异步方式，即SDK会自己起线程来发送
        // 此时需传入 BxFileWriterListener
        // 可在相应的接口对相应的事件进行处理
        //screen.writeProgramsAsync(plist, new WriteProgramTextCaptionWithStyle());

        //
        // 2. writePrograms - 同步方式，即SDK会BLOCK住一直等到节目发送完毕
        screen.writePrograms(plist);

        //
        // 此方法通常不用
        // 3. 同步方式将节目写入控制器，本方法不做任何检查，从而提高发送效率
        //screen.writeProgramQuickly(pf);

        Thread.sleep(20000);

        //
        // 如何需要，可以从控制器回读控制器上已有的节目列表
        List<String> pgs = screen.readProgramList();
        for (String pg : pgs) {
            System.out.println(pg);
        }

        //
        // 以下是动态区部分 Demo
        // 动态区的特点是

        //DynamicBxArea(int id, byte runMode, byte immediatePlay, int timeout)
        //运行模式：
        //0：循环显示。
        //1：显示完成后静止显示最后一页数据。
        //2：循环显示，超过设定时间后数据仍未更新时不再显示。
        //3：循环显示，超过设定时间后数据仍未更新时显示 Logo信息。
        //4：循环显示，显示完最后一页后就不再显示。

        //是否立即播放：
        //0：与异步节目一起播放。
        //1：异步节目停止播放，仅播放动态区域。
        //2：当播放完节目编号最高的异步节目后播放该动态区域。

        //
        // 定义一个动态区
        // 可以通过ID来更新不同的动态区内容, 此处 ID 为0

        DynamicBxAreaRule dRule = new DynamicBxAreaRule();
        dRule.setId(0);
        dRule.setImmediatePlay((byte) 2);
        dRule.setRunMode((byte) 4);
        dRule.addRelativeProgram(1);

        DynamicBxArea dArea = new DynamicBxArea(posX, posY, 32, 32, profile);
        page = new TextBxPage("Hello This is Dynamic");
        page.setDisplayStyle(styles[4]);
        page.setHeadTailInterval(-2);
        dArea.addPage(page);
        screen.writeDynamic(dRule, dArea);

        Thread.sleep(5000);

        //
        // 继开与控制器之间的链接
        screen.disconnect();
    }

    @Override
    public void fileWriting(Bx6GScreen bx6GScreen, String s, int i) {

    }

    @Override
    public void fileFinish(Bx6GScreen bx6GScreen, String s, int i) {

    }

    @Override
    public void progressChanged(Bx6GScreen bx6GScreen, String s, int i, int i1) {

    }

    @Override
    public void cancel(Bx6GScreen bx6GScreen, String s, Bx6GException e) {

    }

    @Override
    public void done(Bx6GScreen bx6GScreen) {

    }
}
