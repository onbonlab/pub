/**
 * Created by admin on 2019/8/28.
 */

import onbon.y2.Y2DynamicManager;
import onbon.y2.Y2Env;
import onbon.y2.Y2Screen;
import onbon.y2.Y2ScreenFactory;
import onbon.y2.common.AlignmentType;
import onbon.y2.message.net.SearchControllerOutput;
import onbon.y2.play.*;

import java.awt.*;

/**
 * @program: bxy_demo
 * @description:
 * @author: Mr.Feng
 * @create: 2019-08-28 09:48
 **/
public class bxy_demo {

    public static void main(String[] args)throws Exception
    {
        // Led屏中可以有多个节目，节目之前轮流播放，节目中可以有多个区域，区域之间同时播放，区域中可以有多个播放文件，播放文件之间轮流播放
        // 初始化API环境 仅一次
        Y2Env.initial("log.properties");
        //Y2ScreenFactoryTest y2_1 = new Y2ScreenFactoryTest();
        //y2_1.test31();

        SendVideo();
    }

    public static void SendProgram()throws Exception
    {
        // 连上特定的屏幕
        Y2Screen screen = new Y2Screen( "http://192.168.100.202" );

        // 登入
        // 登入时用户名和密码都是guest不可改变
        screen.login( "guest","guest" );

        screen.clearPlayResources();
        screen.turnOff();
        screen.turnOn();
        screen.syncTime();
        screen.lock();
        screen.unlock();
        screen.lockProgram(1);
        screen.unlockProgram(1);
        screen.changeBrightness(20);

        // 字幕区
        // 字幕区是一个单行文字的分区，将内容以水平移动的方式显示在屏幕上
        MarqueeArea marea = new MarqueeArea( 0,0,128,32 );

        // 添加字幕区显示内容
        marea.addContent( "仰邦科技（证券代码：833096）成立于2009年，总部位于中国著名的高科技产业集聚区——上海漕河泾经济技术开发区，下属苏州软件研发基地、深圳视频研发中心和昆山产业基地。是LED控制器领域首家新三板挂牌企业" );

        // 创建节目文件
        ProgramPlayFile pf = new ProgramPlayFile( 1 );

        // 将字幕分区添加到节目中
        pf.getAreas().add( marea );

        String list = screen.writePlaylist( pf );

        // 更新节目
        screen.play( list );
        // 登出
        screen.logout();
    }

    public static void SendPrograms()throws Exception
    {
        // 连上特定屏幕
        Y2Screen screen = new Y2Screen( "http://192.168.100.202" );
        // 登入
        screen.login( "guest","guest" );

        // 创建节目文件-1
        ProgramPlayFile pf_1 = new ProgramPlayFile( 1 );

        // 创建一个图片区
        PicArea area_pic = new PicArea( 0,0,128,96 );

        // 图片区中添加图片
        area_pic.addPage( "D:a/1.bmp" );

        // 将图片区添加到节目
        pf_1.getAreas().add( area_pic );

        // 创建节目文件-2
        ProgramPlayFile pf_2  = new ProgramPlayFile( 2 );

        // 创建一个视频区
        VideoArea area_video = new VideoArea( 0,0,128,96 );

        // 视频区中添加视频
        area_video.addVideo( "E:/Video/girls_640x480.mp4",1 );

        // 将视频区添加到节目文件—2
        pf_2.getAreas().add( area_video );

        String list = screen.writePlaylist( pf_1 ,pf_2);

        screen.play( list );

    }

    // 关于动态区
    public static void SendDynamic()throws Exception
    {
        Y2Screen screen = new Y2Screen( "http://192.168.100.202" );
        screen.login( "guest","guest" );

        // 动态区
        // 动态区管理程序
        Y2DynamicManager dyn = screen.dynamic();

        // 建立动态节目
        DynamicPlayFile file = new DynamicPlayFile();

        // 于动态节目中新增一个动态区
        DynamicArea area = file.createArea( 0,0,128,48,1 );
        DynamicArea area_1 = file.createArea( 0,48,128,48,2 );

        // 动态区中添加显示内容  字符串
        area.addText( "123456" );
        area_1.addText( "123456" );

        // 动态区中添加显示内容  也可以是图片
        area.addImage( "D:/a/004.bmp" );
        area_1.addImage( "D:/a/004.bmp" );

        // 将动态区上传
        dyn.write( file );
    }

    // 其他命令
    public static void SendCmd()throws Exception
    {
        Y2Screen screen = new Y2Screen( "http://192.168.100.202" );
        screen.login( "guest","guest" );

        // 关机命令
        screen.turnOff();

        // 开机命令
        screen.turnOn();

        // 校时命令
        screen.syncTime();

        // 音量调节  音量范围  0--100
        screen.changeVolume( 60 );

        // 清除播放资源
        screen.clearPlayResources();

        // 锁定当前屏幕画面
        screen.lock();

        // 解锁当前屏幕画面
        screen.unlock();

        // 锁定节目
        screen.lockProgram( 1 );

        //

    }

    // 动态区（文字）和图片区叠加显示
    public static void SendDynamicPic()throws Exception
    {
        Y2Screen screen = new Y2Screen( "http://192.168.100.202" );
        screen.login( "guest","guest" );

        ProgramPlayFile pf = new ProgramPlayFile( 1 );

        PicArea area = new PicArea( 0,0,256,128 );
        area.addPage( "D:/a/0.bmp" );

        pf.getAreas().add( area );

        String list = screen.writePlaylist( pf );

        screen.play( list );

        Y2DynamicManager dyn = screen.dynamic();

        // 建立动态节目
        DynamicPlayFile file = new DynamicPlayFile();

        // 于动态节目中新增一个动态区
        DynamicArea area_1 = file.createArea( 0,0,256,128,1 );
        //DynamicArea area_1 = file.createArea( 0,48,128,48,2 );

        // 动态区中添加显示内容  字符串
        area_1.addText( "123456" );
        area_1.setTransparency( 0 );
        area_1.transparency( 0 );
        //area_1.addText( "123456" );
        DynamicAreaTextUnit unit1 = ((DynamicAreaTextUnit)area_1.getUnits().get(0));
        unit1.setBgColor( new Color( 0,0,0,0 ) );

        // 动态区中添加显示内容  也可以是图片
        //area.addImage( "D:/a/004.bmp" );
        //area_1.addImage( "D:/a/004.bmp" );

        // 将动态区上传
        dyn.write( file );

        screen.logout();
        screen.close();

    }

    public static void SendProgram_gif()throws Exception
    {
        Y2Screen screen = new Y2Screen( "http://192.168.100.202" );
        screen.login( "guest","guest" );
        PicArea area = new PicArea( 0,0,128,96 );
        area.addPage( "D:a/28/28.gif"  );
        area.setStayTime( 0 );
        area.stayTime( 0 );
        area.animation( 1,0 );
        ProgramPlayFile pf = new ProgramPlayFile( 1 );
        pf.getAreas().add( area );
        String list = screen.writePlaylist( pf );
        screen.play( list );
    }

    public static void SendDateTime()throws Exception
    {
        Y2Screen screen = new Y2Screen( "http://192.168.100.120" );
        screen.login( "guest","guest" );
        DateTimeArea area = new DateTimeArea( 0,0 );
        area.horizontalAlignment( AlignmentType.NEAR );
        area.addUnits( DateTimePattern.YY_MM_DD1 ).fgColor( Color.red ).getFont().setSize( 12 );

        DateTimeArea area2 = new DateTimeArea( 0,16 );
        area2.horizontalAlignment( AlignmentType.NEAR );
        area2.addUnits( DateTimePattern.HH_MM_SS).fgColor( Color.red ).getFont().setSize( 12 );

        ProgramPlayFile pf = new ProgramPlayFile( 1 );
        pf.getAreas().add( area );
        pf.getAreas().add( area2 );

        String list = screen.writePlaylist( pf );

        // 更新节目
        screen.play( list );
        // 登出
        screen.logout();
    }

    public static void SendVideo()throws Exception
    {
        Y2Screen screen = new Y2Screen( "http://192.168.100.120" );
        screen.login( "guest","guest" );
        for(int i = 1;i<=10;i++) {
            VideoArea area = new VideoArea( 0, 0, 128, 96 );
            area.addVideo( "D:a/video/video10.mp4", 1 );

            ProgramPlayFile pf = new ProgramPlayFile( i );
            pf.getAreas().add( area );
            String list = screen.writePlaylist( pf );
            screen.play( list );
        }
        screen.logout();
    }






}
