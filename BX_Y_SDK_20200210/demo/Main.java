import java.awt.Color;

import onbon.y2.Y2BulletinManager;
import onbon.y2.Y2DynamicManager;
import onbon.y2.Y2Env;
import onbon.y2.Y2Exception;
import onbon.y2.Y2Screen;
import onbon.y2.common.AlignmentType;
import onbon.y2.play.BulletinArea;
import onbon.y2.play.DateTimeArea;
import onbon.y2.play.DateTimePattern;
import onbon.y2.play.DynamicArea;
import onbon.y2.play.DynamicPlayFile;
import onbon.y2.play.MarqueeArea;
import onbon.y2.play.ProgramPlayFile;
import onbon.y2.play.ProgramPlayFile.PlayMode;
import onbon.y2.play.TextArea;
import onbon.y2.play.VideoArea;

public class Main 
{
	public static void main(String[] args) throws Exception
	{
		//初始化API环境 仅一次
		Y2Env.initial("log.properties");
		
		//连上特定的屏幕并执行一些命令
		//建立一个新的屏幕
		Y2Screen screen = new Y2Screen("http://192.168.100.2");
		
		//登入
		if(!screen.login("guest", "guest"))
		{
			System.out.println("登入失败！");
			return;
		}
		
		//dosomthing
		screen.turnOff();//关机
		screen.turnOn(); //开机
		screen.syncTime();//校时
		screen.changeVolume(60); //音量调至60
		screen.clearPlayResources();//清除播放资源（删除播放节目）
		
		//字幕区
		//字幕区是一个单行文字的分区，将内容以水平移动的方式显示在屏幕上
		MarqueeArea area = new MarqueeArea(0,0,128,128);
		area.right2Left(true);
		//page1
		area.addContent("Hello ONBON")
		.fgColor(Color.green)
		.bgColor(Color.white)
		.animationSpeed(16)
		.getFont()
		.size(16)
		.bold()
		.strikethrough()
		.underline();
		//page2
		area.addContent("We are happy to announce that Y2 Java library has released.")
		.fgColor(Color.blue)
		.bgColor(Color.white)
		.animationSpeed(1)
		.getFont()
		.size(50);
		
		//图文区
		//图文区是一个以文字内容为主的分区，根据设置自动换行、分段显示在屏幕上
		//section1
		TextArea tarea = new TextArea(0,0,128,128);
		tarea.animationType(10);
		tarea.addImageSection("D:/a/004.bmp");
		tarea.addTextSection("Hello everyone!")
		.fgColor(Color.white)
		.bgColor(Color.black)
		.stayTime(10)
		.animationSpeed(16)
		.horizontalAlignment(AlignmentType.CENTER)
		.verticalAlignment(AlignmentType.CENTER)
		.rowHeight(30)
		.getFont()
		.size(24)
		.bold()
		.strikethrough()//加删除线
		.underline();//加下划线
		
		//section2
		tarea.addTextSection("ONBON Y系列闪亮登场。")
		.fgColor(Color.red)
		.bgColor(Color.black)
		.stayTime(15)
		.animationSpeed(1)
		.horizontalAlignment(AlignmentType.CENTER)
		.verticalAlignment(AlignmentType.CENTER)
		.rowHeight(20)
		.getFont()
		.size(20);
		
		//日期时间区DateTimeArea
		//日期时间区根据设定将日期、时间、星期组合显示在屏幕上。若没有设定宽与高，日期时间区会根据内容自动调整区域大小显示完整的内容
		// 固定位置与大小
		DateTimeArea dtarea = new DateTimeArea(0,0,128,128);
		// 固定位置，大小自动调整
		//DateTimeArea dtarea = new DateTimeArea(0,0);
		dtarea.bgColor(Color.black)
		.horizontalAlignment(AlignmentType.CENTER);
		// 第一行：显示时间与日期，格式为 AM 8:16 2019-4-1
		/*dtarea.addUnits(DateTimePattern.CH_AMPM_H_MM
				,DateTimePattern.YYYY_MM_DD1)
		.fgColor(Color.yellow)
		.getFont()
		.bold()
		.underline();*/
		dtarea.addUnits(DateTimePattern.CH_AMPM_H_MM_SS);
		dtarea.addUnits(DateTimePattern.CH_YYYY_MM_DD);
		// 第二行显示星期
		dtarea.addUnits(DateTimePattern.CH_WEEK);
		// 第三行显示月份
		dtarea.addUnits(DateTimePattern.CH_MONTH)
		.getFont()
		.bold();
		
		// 视频区
		VideoArea varea = new VideoArea(0,0,128,128);
		// 参数 视频路基和视频名称  视频播放音量0-100
		varea.addVideo("E:/Video/video.mp4", 100);
		
		// 公告分区
		// 公告分区用于立即显示一些重要的文字信息
		// 取得管理程序
		Y2BulletinManager bulletin = screen.bulletin();
		// 建立公告一
		BulletinArea barea = new BulletinArea(1,"公告一",0,0,128,16);
		barea.bgColor(Color.blue)
		.content("公告测测");
		bulletin.write(barea);
		bulletin.play();
		
		
		// 动态区
		// 取得管理程序
		Y2DynamicManager dyn = screen.dynamic();
		// 建立动态节目
		DynamicPlayFile file = new DynamicPlayFile();
		// 于节目中新增一个动态区
		DynamicArea darea = file.createArea(0,0,100,40);
		darea.addText("此为动态区！");
		darea.addText("动态区测试！");
		// 将动态区上传
		//dyn.write(file);
		
		
		ProgramPlayFile pr = new ProgramPlayFile(1);
		pr.getAreas().add(area);
		
		ProgramPlayFile pr2 = new ProgramPlayFile(2);
		pr2.getAreas().add(tarea);
		
		ProgramPlayFile pr3 = new ProgramPlayFile(3);
		pr3.getAreas().add(dtarea);
		
		ProgramPlayFile pr4 = new ProgramPlayFile(4);
		pr4.getAreas().add(varea);
		
		String listId = screen.writePlaylist(pr3);
		
		screen.play(listId);
		
		//登出
		screen.logout();
	}
}
