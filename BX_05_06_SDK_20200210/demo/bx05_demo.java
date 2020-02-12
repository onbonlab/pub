/**
 * Created by admin on 2019/8/19.
 */

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.Bx5GScreenRS;
import onbon.bx05.area.*;
import onbon.bx05.area.page.ImageFileBxPage;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.area.page.TextFileBxPage;
import onbon.bx05.cmd.dyn7.DynamicBxAreaRule;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.message.common.ErrorType;
import onbon.bx05.message.led.ReturnControllerStatus;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.DisplayStyleFactory.DisplayStyle;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bx05_demo
 * @description:
 * @author: Mr.Feng
 * @create: 2019-08-19 10:58
 **/

// 此demo只用于五代控制卡，例如BX-5E1、BX-5M1等
public class bx05_demo {
     private static String ip = "192.168.100.199";
     private static int port =5005;
    public static void main(String[] args)throws Exception
    {
        // 初始化API,此操作只在程序启动时候执行一次即可，多次执行会出现内存错误
        Bx5GEnv.initial("log.properties",30000);
        SendDynamicProgam();
    }

    // 将一个节目发送到控制器上并显示
    public static void SendProgram()throws Exception
    {
        // 关于显示特技
        // 0:随机显示
        // 1:静止显示
        // 2:快速打出
        // 3:向左移动
        // 4:向左连移
        // 5:向上移动
        // 6:向上连移
        // 7:闪烁
        // 8:飘雪
        // 9:冒泡
        // 10:中间移出
        // 11:左右移入
        // 12:左右交叉移入
        // 13:上下交叉移入
        // 14:花卷闭合
        // 15:花卷打开
        // 16:向左拉伸
        // 17:向右拉伸
        // 18:向上拉伸
        // 19:向下拉伸
        // 20:向左镭射
        // 21:向右镭射
        // 22:向上镭射
        // 23:向下镭射
        // 24:左右交叉拉幕
        // 25:上下交叉拉幕
        // 26:分散左拉
        // 27:水平百叶
        // 28:垂直百叶
        // 29:向左拉幕
        // 30:向右拉幕
        // 31:向上拉幕
        // 32:向下拉幕
        // 33:左右闭合
        // 34:左右对开
        // 35:上下闭合
        // 36;上下对开
        // 37:向右移动
        // 38:向右连移
        // 39:向下移动
        // 40:向下连移
        // 41:45度左旋
        // 42:180度左旋
        // 43:90度左旋
        // 44:45度右旋
        // 45:180度右旋
        // 46:90度右旋
        // 47:菱形打开
        // 48:菱形闭合
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        // 创建screen对象，用于与控制卡的交互
        Bx5GScreenClient screen = new Bx5GScreenClient( "MyScreen" );

        // 连接控制器
        screen.connect( ip,port );

        // 创建节目  一个节目相当于一屏显示内容
        ProgramBxFile pf = new ProgramBxFile( 0,screen.getProfile() );

        // 创建一个文本区
        // 分别输入X，Y，width，heigth
        // 注意区域坐标和宽度高度，不要越界
        TextCaptionBxArea area = new TextCaptionBxArea( 0,0,160,64,screen.getProfile() );

        // 创建一个数据页
        // 第一行数据
        TextBxPage page = new TextBxPage("仰邦科技欢迎你");
        // 第二行数据
        page.newLine( "这是第二行" );
        // 设置字体
        page.setFont( new Font("宋体",Font.PLAIN,12) );
        // 设置显示特技为快速打出
        page.setDisplayStyle( styles[2] );

        // 数据页可以是图片
        ImageFileBxPage iPage = new ImageFileBxPage( "E;a/001.bmp" );

        // 数据页可以是txt文件
        TextFileBxPage tPage  = new TextFileBxPage("E:a/001.txt");

        // 将前面的page添加到area中 area中可以添加多个page 其中page可以是字符串，可以是txt文件，可以是图片，不可以是表格，如果需要Led屏上显示表格，请先将表格绘制成图片
        area.addPage( page );
        area.addPage( iPage );
        area.addPage( tPage );
        // 将area添加到节目中  节目中可以添加多个area
        pf.addArea( area );

        // 更新节目
        screen.writeProgram( pf );

        // 断开连接
        screen.disconnect();
    }

    // 将多个节目发送到控制器并显示
    public static void SendPrograms()throws Exception
    {
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        Bx5GScreenClient screen = new Bx5GScreenClient( "MyScreen" );

        screen.connect( ip,port );

        ProgramBxFile pf = new ProgramBxFile( "P000",screen.getProfile() );

        // 创建一个时间区
        DateTimeBxArea dtArea = new DateTimeBxArea( 0,0,160,64,screen.getProfile() );

        // 设定时间区多行显示
        dtArea.setMultiline( true );

        // 设定日期显示格式NULL表示不显示日期
        dtArea.setDateStyle( DateStyle.YYYY_MM_DD_1 );

        // 设定时间显示格式NULL表示不显示时间
        dtArea.setTimeStyle( TimeStyle.HH12_MM_SS_1 );

        // 设定星期显示格式NULL表示不显示星期
        dtArea.setWeekStyle( WeekStyle.CHINESE );

        dtArea.setFont( new Font("宋体",Font.PLAIN,12) );

        pf.addArea( dtArea );

        // 创建第二个节目
        ProgramBxFile pf_2 = new ProgramBxFile( "P001",screen.getProfile() );

        TextCaptionBxArea area = new TextCaptionBxArea( 0,0,160,64,screen.getProfile() );

        TextBxPage page = new TextBxPage( "Led控制系统首选仰邦" );

        page.setDisplayStyle( styles[4] );

        area.addPage( page );

        pf_2.addArea( area );

        // 创建一个list
        ArrayList<ProgramBxFile> plist = new ArrayList<ProgramBxFile>(  );
        plist.add( pf );
        plist.add( pf_2 );

        screen.writePrograms( plist );

        // 如果需要，可以从控制器回读控制器上已有的节目列表
        List<String> pfs = screen.readProgramList();
        for (String program :pfs)
        {
            System.out.println( program );
        }

        screen.disconnect();
    }

    // 更新动态区
    // 只有BX-5E1、BX-5E2、BX-5E3支持动态区，其他控制卡不支持动态区
    // 动态区是完全独立于节目，其显示内容可以按区域单独更新
    // 动态区可以与节目一起播放，也可以单独播放
    // 动态区显示内容存储于ARM，掉电不保存，没有刷新次数限制

    // 动态区单独播放
    public static void SendDynamic()throws Exception
    {
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        Bx5GScreenClient screen = new Bx5GScreenClient( "MyScreen" );

        screen.connect( ip,port );

        // DynamicBxRule(id,runMode,immediatePlay,timeout);
        // runMode 运行模式
        // 0:循环显示
        // 1:显示完成后静止显示最后一页数据
        // 2:循环显示，超过设定时间后数据仍未更新时不再显示
        // 3:循环显示，超过设定时间后数据仍未更新时显示Logo信息
        // 4:循环显示，显示完最后一页后就不再显示
        // immediatePlay 是否立即播放
        // 0:与异步节目一起播放
        // 1:异步节目停止播放，仅播放动态区
        // 2:当播放完节目编号最高的异步节目后播放该动态区

        // BX-5E系列控制卡最高支持4个动态区，当屏幕上需要显示多个动态区时，动态区ID不可以相同
        // 定义一个动态区，此处动态区ID为0
        DynamicBxAreaRule rule = new DynamicBxAreaRule( 0,(byte)0,(byte)1 ,0);

        TextCaptionBxArea darea = new TextCaptionBxArea( 0,0,160,64,screen.getProfile() );

        TextBxPage dpage = new TextBxPage( "动态区测试123abc" );

        dpage.setDisplayStyle( styles[2] );

        darea.addPage( dpage );

        // 更新动态区
        screen.writeDynamic( rule,darea );

        screen.disconnect();
    }

    // 动态区和节目一起播放
    public static void SendDynamicProgam()throws Exception
    {
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        Bx5GScreenClient screen = new Bx5GScreenClient( "MyScreen" );

        screen.connect( ip,port );

        ProgramBxFile pf = new ProgramBxFile( "P000",screen.getProfile() );

        TextCaptionBxArea area = new TextCaptionBxArea( 0,0,160,32,screen.getProfile() );

        TextBxPage page = new TextBxPage( "这是节目√" );

        area.addPage( page );

        pf.addArea( area );

        screen.writeProgram( pf );

        DynamicBxAreaRule rule = new DynamicBxAreaRule( 0,(byte)0,(byte)0,0 );
        // 动态区关联上面的节目
        rule.addProgram( "P000" );

        TextCaptionBxArea darea = new TextCaptionBxArea( 0,32,160,32,screen.getProfile() );

        TextBxPage dpage = new TextBxPage( "这是动态区" );

        darea.addPage( dpage );

        screen.writeDynamic( rule,darea );

        screen.disconnect();
    }

    // 其他一些常用命令
    public static void Sendcmd()throws Exception
    {
        Bx5GScreenClient screen = new Bx5GScreenClient( "MyScreen" );
        screen.connect( ip,port );

        // 关机命令
        screen.turnOff();

        // 开机命令
        screen.turnOn();

        // ping命令
        screen.ping();

        // 查询控制器状态
        screen.checkControllerStatus();

        // 查询控制器当前固件版本
        screen.checkFirmware();

        // 查询控制器内存
        screen.checkMemVolumes();

        // 校时命令
        screen.syncTime();

        // 获取控制器ID
        screen.readControllerId();

        // 锁定屏幕当前画面
        screen.lock();

        // 解除锁定屏幕当前画面
        screen.unlock();

        // 通过以下接口回读控制器状态
        Bx5GScreen.Result<ReturnControllerStatus> result = screen.checkControllerStatus();
        if(result.isOK())
        {
            ReturnControllerStatus status = result.reply;
            status.getBrightness(); // 取得亮度值
            status.getTemperature1(); // 取得温度传感器温度值

            // status还有很多接口，根据实际应用进行调用
        }
        else
        {
            ErrorType error = result.getError();
            System.out.println( error );
        }
    }

    // 关于串口通讯
    public static void RsConnect()throws Exception
    {
        // 串口创建screen 对象方法和网口不同
        Bx5GScreenRS screen = new Bx5GScreenRS( "MyScreen" );

        // 连接控制器
        // comPort : 串口号
        // baudRate : 波特率 ，目前支持的波特率为9600/57600
        screen.connect( "COM2", Bx5GScreenRS.BaudRate.RATE_9600 );

        screen.disconnect();
    }
}
