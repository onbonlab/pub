/**
 * Created by admin on 2019/8/21.
 */

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.Bx6GScreenRS;
import onbon.bx06.area.*;
import onbon.bx06.area.page.ImageFileBxPage;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.area.page.TextFileBxPage;
import onbon.bx06.cmd.dyn.DynamicBxAreaRule;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.message.common.ErrorType;
import onbon.bx06.message.led.ReturnControllerStatus;
import onbon.bx06.series.Bx6E;
import onbon.bx06.utils.DisplayStyleFactory.DisplayStyle;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.TextBinary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bx06_demo
 * @description:
 * @author: Mr.Feng
 * @create: 2019-08-21 14:52
 **/
public class bx06_demo {
    private static String ip = "192.168.100.199";
    private static int port = 5005;
    public static void main(String[] args)throws Exception
    {
        // 初始化API，此操作只在程序启动时候执行一次即可，多次执行会出现内存错误
        Bx6GEnv.initial("log.properties",30000);
        SendDynamicProgram();
    }

    // 将一个节目发送到控制器
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
        // 36:上下对开
        // 37;向右移动
        // 38:向右连移
        // 39:向下移动
        // 40:向下连移
        // 41:45度左旋
        // 42:180度左旋
        // 43:90度右旋
        // 44:45度右旋
        // 45:180度右旋
        // 46:90度右旋
        // 47:菱形打开
        // 48:菱形闭合
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        // 创建screen对象,用于与控制卡的交互
        // 第二个参数是控制卡型号，只有型号对才能正常通讯，否则会出现逾时未回应，如果使用的型号API中未定义，用new Bx6M()替代
        Bx6GScreenClient screen = new Bx6GScreenClient( "MyScreen",new Bx6E() );

        // 连接控制器
        screen.connect( ip,port);

        // 创建节目 一个节目相当于一屏显示内容
        ProgramBxFile pf = new ProgramBxFile( "P000",screen.getProfile() );

        // 创建一个分区
        // 分别输入X，Y，width，heigth
        // 注意区域坐标和宽度高度不要越界
        TextCaptionBxArea area = new TextCaptionBxArea( 0,0,160,64,screen.getProfile() );

        // 创建一个数据页
        // 第一行数据
        TextBxPage page = new TextBxPage("仰邦科技欢迎你！");
        // 第二行数据
        page.newLine( "这是第二行数据" );
        // 设置字体
        page.setFont( new Font("宋体", Font.PLAIN,12) );
        // 设置显示特技为快速打出
        page.setDisplayStyle( styles[2] );

        // 数据页可以是图片
        ImageFileBxPage iPage  = new ImageFileBxPage( "D:a/004.bmp" );

        // 数据页可以是txt文件
        TextFileBxPage tPage = new TextFileBxPage( "D:a/001.txt" );

        // 将前面的page添加到area中，page不可以是表格，如果需要Led显示表格，请先将表格绘制成图片
        area.addPage( page );
        area.addPage( iPage );
        area.addPage( tPage );

        // 将area添加到节目中，节目中可以添加多个area
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

        Bx6GScreenClient screen = new Bx6GScreenClient( "MyScreen",new Bx6E() );

        screen.connect( ip,port );

        ProgramBxFile pf = new ProgramBxFile( "P000",screen.getProfile() );

        // 创建一个时间区
        DateTimeBxArea dtArea = new DateTimeBxArea( 0,0,160,64,screen.getProfile() );

        // 设定时间区多行显示
        dtArea.setMultiline( true );

        // 设定日期显示格式 NULL表示不显示日期
        dtArea.setDateStyle( DateStyle.YYYY_MM_DD_1 );
        // 设定时间显示格式 NULL表示不显示时间
        dtArea.setTimeStyle( TimeStyle.HH12_MM_SS_1 );
        // 设定星期显示格式 NULL表示不显示星期
        dtArea.setWeekStyle( WeekStyle.CHINESE );
        // 设定时间区字体
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
        for(String program:pfs)
        {
            System.out.println( program );
        }

        screen.disconnect();
    }

    // 更新动态区
    // 六代卡中，只有BX-6E系列、BX-6EX系列和BX-6Q系列支持动态区
    // 动态区是完全独立于节目，其显示内容可以按区域单独更新
    // 动态区可以与节目一起播放，也可以单独播放
    // 动态区显示内容存储于ARM，掉电不保存，没有刷新次数限制

    // 动态区单独播放
    public static void SendDynamic()throws Exception
    {
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        Bx6GScreenClient screen = new Bx6GScreenClient( "MyScreen",new Bx6E() );

        screen.connect( ip,port );

        // 创建动态区
        // BX-6E BX-6EX系列支持4个动态区，BX-6Q系列支持32个动态区
        DynamicBxAreaRule rule = new DynamicBxAreaRule();
        // 设定动态区ID ，此处ID为0 ，多个动态区ID不能相同
        rule.setId(0);
        // 设定异步节目停止播放，仅播放动态区
        // 0:与异步节目一起播放
        // 1:异步节目 停止播放，仅播放动态区
        // 2:当播放完节目编号坐高的异步节目后播放该动态区
        rule.setImmediatePlay( (byte)1 );
        // 设定动态区循环播放
        // 0:循环显示
        // 1:显示完成后静止显示最后一页数据
        // 2:循环显示，超过设定时间后数据仍未更新时不再显示
        // 3:循环显示，超过设定时间后数据仍未更新时显示Logo信息
        // 4:循环显示，显示完成最后一页后就不再显示
        rule.setRunMode( (byte)0 );

        DynamicBxArea area = new DynamicBxArea( 0,0,160,32,screen.getProfile() );

        TextBxPage page = new TextBxPage( "第一个动态区" );

        page.setFont( new Font( "宋体",Font.PLAIN,12 ) );

        page.setDisplayStyle( styles[2] );

        area.addPage( page );

        screen.writeDynamic( rule,area );

        // 创建第二个动态区
        DynamicBxAreaRule rule_2 = new DynamicBxAreaRule();
        rule_2.setId( 1 );
        rule_2.setImmediatePlay( (byte)1 );
        rule_2.setRunMode( (byte)0 );
        DynamicBxArea area_2 = new DynamicBxArea( 0,32,160,32,screen.getProfile() );
        TextBxPage page_2 = new TextBxPage( "第二个动态区" );
        page_2.setFont( new Font("宋体",Font.PLAIN,12) );
        page_2.setDisplayStyle( styles[2] );
        area_2.addPage( page_2 );
        screen.writeDynamic( rule_2,area_2 );

        screen.disconnect();
    }

    // 动态区和节目一起播放
    public static void SendDynamicProgram()throws Exception
    {
        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        Bx6GScreenClient screen = new Bx6GScreenClient( "MyScreen",new Bx6E() );

        screen.connect( ip,port );

        ProgramBxFile pf = new ProgramBxFile( 0,screen.getProfile() );

        TextCaptionBxArea area = new TextCaptionBxArea( 0,0,160,32,screen.getProfile() );

        TextBxPage page = new TextBxPage( "这是节目" );

        area.addPage( page );

        pf.addArea( area );

        screen.writeProgram( pf );

        DynamicBxAreaRule rule = new DynamicBxAreaRule();
        rule.setId(0);
        rule.setRunMode( (byte)0 );
        // 新增动态区关联异步节目
        // 一旦关联了某个异步节目，则该节目和动态区一起播放
        // 设置动态区和节目关联
        // 设定是否关联全部节目
        // true: 所有异步节目播放是都允许播放该动态区
        // false:由规则来决定
        rule.setRelativeAllPrograms( false );
        rule.addRelativeProgram( 0 );

        DynamicBxArea dArea = new DynamicBxArea( 0,32,160,32,screen.getProfile() );

        TextBxPage dPage = new TextBxPage( "这是动态区" );
        dPage.setDisplayStyle( styles[2] );
        dPage.setFont( new Font( "宋体",Font.PLAIN,12 ) );

        dArea.addPage( dPage );

        screen.writeDynamic( rule,dArea );

        List<String> pfs = screen.readProgramList();
        for(String program : pfs)
        {
            System.out.println( program );
        }

        screen.disconnect();
    }

    // 关于语音播报区域
    // 语音播放目前只有六代部分卡支持
    public static void SendSound()throws Exception
    {
        Bx6GScreenClient screen = new Bx6GScreenClient( "MyScreen",new Bx6E() );

        screen.connect( ip,port );

        DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

        ProgramBxFile pf = new ProgramBxFile( "P000",screen.getProfile() );

        // 语音部分
        TextCaptionBxArea area_sound = new TextCaptionBxArea( 0,0,160,16,screen.getProfile());
        area_sound.setVoiceContent( "黑A12345请到淀粉副产品库DF-01月台" );// 该字符串会被语音播报
        area_sound.setVoiceFlag( true );
        area_sound.setVoiceReplayTimes( 2 );// 设置重复播报3次，如果不设置，默认一直播报
        // 语音的其他设置都在area_sound中设置

        // 显示部分_1
        TextCaptionBxArea area_display_1 = new TextCaptionBxArea( 0,0,160,48,screen.getProfile() );
        TextBxPage page_display_1 = new TextBxPage( "黑A12345" );
        page_display_1.setFont( new Font( "宋体",Font.PLAIN,30 ) );
        page_display_1.setVerticalAlignment( TextBinary.Alignment.CENTER );// 设置水平居中
        page_display_1.setHorizontalAlignment( TextBinary.Alignment.CENTER );// 设置垂直居中
        page_display_1.setDisplayStyle( styles[2] );
        area_display_1.addPage( page_display_1 );

        // 显示部分_2
        TextCaptionBxArea area_display_2 = new TextCaptionBxArea( 0,48,160,48,screen.getProfile() );
        TextBxPage page_diaplay_2 = new TextBxPage( "请到淀粉副产品库" );
        page_diaplay_2.newLine( "DF-01月台" );
        page_diaplay_2.setFont( new Font( "宋体",Font.PLAIN,16 ) );
        page_diaplay_2.setVerticalAlignment( TextBinary.Alignment.CENTER );
        page_diaplay_2.setHorizontalAlignment( TextBinary.Alignment.CENTER );
        page_diaplay_2.setDisplayStyle( styles[2] );
        area_display_2.addPage( page_diaplay_2 );

        pf.addArea( area_sound );
        pf.addArea( area_display_1 );
        pf.addArea( area_display_2 );

        screen.writeProgram( pf );

        screen.disconnect();
    }

    // 其他一些常用命令
    public static void  SendCmd()throws Exception
    {
        Bx6GScreenClient screen = new Bx6GScreenClient( "MyScreen",new Bx6E() );
        screen.connect( ip,port );
        // 关机命令
        screen.turnOff();
        // 开机命令
        screen.turnOn();
        // ping命令
        screen.ping();
        // 查询控制器状态
        screen.checkControllerStatus();
        // 查询控制器内存
        screen.checkMemVolumes();
        // 校时命令
        screen.syncTime();
        // 锁定屏幕当前画面
        screen.lock();
        // 解除锁定屏幕当前画面
        screen.unlock();
        // 通过以下接口回读控制器状态
        Bx6GScreen.Result<ReturnControllerStatus> result = screen.checkControllerStatus();
        if(result.isOK())
        {
            ReturnControllerStatus status = result.reply;
            status.getBrightness(); // 取得亮度值
            status.getTemperature1(); // 取得温度传感器温度值
            // status 还有很多接口，根据实际应用进行调用
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
        // 串口创建screen对象  和网口不同
        Bx6GScreenRS screen = new Bx6GScreenRS( "MyScreen",new Bx6E() );
        // 连接控制器  串口号必须大写，否则会出现连接失败
        screen.connect( "COM2", Bx6GScreenRS.BaudRate.RATE_9600 );
        //
        // 断开连接
        screen.disconnect();
    }
}
