# BX_Y_SDK_20200210

# 1.开发前必读

## 1.1 控制器型号选择
本说明只适用用BX-Y系列控制器，在开发前，首先确认控制器型号是否对应，如果控制器型号是BX-YQ系列，那么本SDK是不支持的。

## 1.2 准备工作
在进行开发之前，请使用通用软件LedshowYQ（下载地址：[enter description here][1]）软件对控制器进行相关配置，比如控制器IP地址、屏幕参数、扫描方式等，在使用LedshowTW软件能发送普通节目到控制器兵能显示正常后，在进行二次开发。

# 2.接口说明
## 2.1 SDK初始化
```java 
// 初始化SDK
// 初始化Y系列API环境，只需要执行一次

// 不需要打印日志，使用以下初始化接口
Y2Env.initial();

// 需要打印日志，使用以下初始化接口
// log.properties文件放在工程目录下
Y2Env.initial("log.properties");
// 或 
Y2Env.initial("log.properties",true);

// 安卓环境初始化
Y2Env.initial(true);

```

## 2.2 创建连接
```java 
// 建立一个屏幕
// IP地址为准备工作时候用LedshowYQ软件给控制器设置的IP地址
Y2Screen screen = new Y2Screen("http://192.168.100.199");

// 登入控制器
// 登入控制器时，用户名和密码都是guest，不可改变
screen.login("guest","guest");


```

## 2.3 断开连接
```java 
// 在登入控制器后，PC就可以与控制器进行通讯
// PC与控制器通讯结束后，需要断开连接
// 登出
screen.logout();
```

## 2.4 常用命令
```java 
//  清屏命令
// 执行该命令后，LED屏上原有播放内容被清除
// LED屏上显示“请加节目”
screen.clearPlayResources();

// 锁定屏幕当前画面
screen.lock();
// 解锁屏幕当前画面
screen.unlock();

// 锁定节目命令
// 参数为节目编号
screen.lockProgram(1);
// 解锁节目命令
screen.unlockProgram(1);

// 亮度调节
// 亮度值范围0-254
screen.changeBrightness(20);

// 音量调节  音量范围  0--100
screen.changeVolume( 60 );
```

### 2.4.1 开关机命令
```java 
// 开机命令
screen.turnOn();

// 关机命令
screen.turnOff();
```

### 2.4.2 校时命令
```java 
// 校时命令
screen.syncTime();
```

## 2.5节目与区域
Led屏中可以有多个节目，节目之前轮流播放，节目中可以有多个（多种）区域，区域之间同时播放，区域中可以有多个播放文件，播放文件之间轮流播放。
### 2.5.1 节目
```java 
// 创建节目
ProgramPlayFile pf = new ProgramPlayFile(1);
```

### 2.5.2 区域
区域分类
区域可以分为以下几种区域
**AnimationArea 炫动背景区**
**ChiCalendarArea 农历区**
**ClockArea 时钟区**
**CounterArea 计时区**
**DateTimeArea 时间区**
**MarqueeArea 字幕区**
**PicArea 图片区**
**SensorTempArea 传感器区**
**TextArea 文本区**
**TextualizeArea 文本区**
**VideoArea 视频区**
**AudioArea 背景语音区**

字幕区
字幕区是一个显示单行文字的区域，将内容以水平移动的方式显示在LED屏上
```java 
// 创建一个字幕区
// 参数为区域的坐标和宽度高度
MarqueeArea marea = new MarqueeArea(0,0,128,32);

// 在区域中添加显示内容
marea.addContent("字幕区测试");

// 将字幕区添加到节目中
pf.getAreas().add(marea);

// 创建节目列表
// 多个节目可以在列表中同时添加，用“,”隔开
String list = screen.writePlaylist(pf);

// 更新节目 
screen.play(list);
```

视频区、图片区和文本区
```java 
// 创建一个视频区
VideoArea varea = new VideoArea(0,0,128,96);
// 视频区中添加视频
// 参数为视频在本地的路径和音量值 音量值范围0-99
varea.addVideo("E:/Video/video01.mp4",99);

// 创建一个图片区
PicArea parea = new PicArea(0,0,128,96);
// 图片区中添加图片
parea.addPage("E:/pic/pic01.bmp");
```

时间区
```java 
// 位置固定，大小自动调整
DateTimeArea area = new DateTimeArea( 0,0 );
// 位置固定，大小固定
DateTimeArea area = new DateTimeArea(0,0,128,96);
area.horizontalAlignment( AlignmentType.NEAR );
area.addUnits( DateTimePattern.YY_MM_DD1).fgColor(Color.red).getFont().setSize( 12 );

DateTimeArea area2 = new DateTimeArea( 0,16 );
area2.horizontalAlignment( AlignmentType.NEAR );
area2.addUnits(DateTimePattern.HH_MM_SS).fgColor(Color.red).getFont
```

## 2.6 动态区
动态区为特殊区域，可以单独播放，也可以和节目一起播放。动态区中的内容可以是图片，也可以是文本。
```java 
// 动态区
// 动态区管理
Y2DynamicManager dyn = screen.dynamic();

// 建立动态节目
DynamicPlayFile file = new DynamicPlayFile();

// 在动态节目中新增一个动态区
DynamicArea area = file.createArea(0,0,128,32,1);

// 动态区中添加文本内容
area.addText("动态区文本123abc");

// 动态区中添加图片内容
area.addImage("E:/pic/pic02.bmp");

// 更新动态区
dyn.write(file);
```

## 2.7 公告区 Bulletin Area
公告区用于即刻显示一些重要的文字信息
```java 
// 取得管理程式
Y2BulletinManager bulletin = screen.bulletin();

// 建立公告一
BulletinArea area1 = new BulletinArea(1,"公告一",0,0,128,32);
area1.bgColor(Color.black)
     .fgColor(Color.red)
     .content("公告区1显示内容");
     
// 建立公告二
BulletinArea area2 = new BulletinArea(2,"公告二",0,32,128,32);
area2.bgColor(Color.black)
     .fgColor(Color.red)
     .content("公告区2显示内容");

// 将公告上传
bulletin.write(area1);
bulletin.write(area2);

// 播放
bulletin.play();

// 删除公告二
bulletin.delete(2);

// 停播
bulletin.stop();
```

# 3. 联系我们
## 上海仰邦科技股份有限公司
地址：上海市徐汇区钦州北路1199号88栋7楼
电话：021-64554198
网址：[enter description here][2]

## 仰邦（江苏）光电实业有限公司（昆山光电产业基地）
地址：江苏省昆山市开发区富春江路 1299 号
电话：0512-36912677 0512-36912688 0512-36912699

## 苏州仰邦软件科技有限公
地址：苏州市吴中区塔韵路178号天鸿大厦501
电话：0512-66589212 0512-66589211

## 二次开发
电话：0512-66589211 0512-66589212
邮箱：dev@onbonbx.com


  [1]: https://www.onbonbx.com/download/164.html
  [2]: www.onbonbx.com