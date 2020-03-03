# BX_05_06_SDK_20200210


# 1.开发前必读

## 1.1 控制器型号选择

本说明只适用于BX系列五代六代控制卡（不包括字库卡），例如：BX-5E1、BX-5M1、BX-6E1X等，其中BX-5E1、BX-5E2、BX-5E3支持动态区（4个），BX-6E1、BX-6E2、BX-6E1X、BX-6E2X、BX-6Q系列支持动态区（32个）。

## 1.2 关于动态区

根据存储介质的不同，我们将控制器上现实的内容分为两类：普通节目（数据存储在flash中）和动态区（数据存储在ram中）。普通节目可以包括各种区域：时间区、传感器区、图文区等。

普通节目，会被存储在控制器的flash中，其内容掉电不丢失，但是因为flash存储器擦写寿命只有十万次，所以，其不能用于更新频率很高的场合，例如：停车场车位信息，外部传感器状态的实时更新，车次状态的实时更新等。

动态区完全独立于普通节目，其内容可以按区域进行单独更新，它可以与普通节目一起播放，也可以单独播放，它最大的特点是内容存储在ram中，没有擦写次数限制，内容掉电不保存，通常用于信息更新频繁场合。

## 1.3 准备工作

在进行开发之前，请使用通用软件LedshowTW图文编辑软件对控制器进行相关配置，比如控制器IP地址（串口号）、屏幕参数、扫描方式等，在使用LedshowTW软件能发送普通节目到控制器兵能显示正常后，在进行二次开发。

## 1.4 常见问题

* 关于表格 控制器不支持Excel或其他表格，所以，需要将表格转换成图片添加到区域中

# 2. 接口说明

## 2.1 SDK初始化

```java 
// 五代控制器
// 初始化SDK
// 初始化有3种方法，如下
Bx5GEnv.initial();

Bx5GEnv.initial("log.properties"); // log.properties是日志配置文件

Bx6GEnv.initial("log.properties",30000);// 30000为通讯超时时间，单位是毫秒

// 六代控制卡
// 初始化SDK
// 初始化有3种方法，如下
Bx6GEnv.initial();

Bx6GEnv.initial("log.properties");// log.properties是日志配置文件

Bx6GEnv.initial("log.properties",30000);// 30000为通讯超时时间，单位是毫秒
```

## 2.2 Screen类

与控制器的所有交互都需要通过Bx5GScreen类或其子类来进行，其子类包括：
Bx5GScreenClient（客户端模式使用），Bx5GScreenRS（串口模式使用），Bx5GScreenServer（服务器模式使用）。
创建screen对象的通常代码如下：

```java 
// 五代控制器
// 创建screen对象，用于对控制器进行访问，客户端模式
Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");
// 创建screen对象，用于对控制器进行访问，串口模式
Bx5GScreenRS screen = new Bx5GScreenRS("MyScreen");

// 六代控制器创建screen对象方法，以BX-6M系列为例
// Bx6M 为控制卡型号，只有型号对应才能通讯正常，否则会出现逾时未回应
// 如果使用的控制器型号在SDK中没有定义，则用Bx6M替代
// 创建screen对象，用于对控制器进行访问，客户端模式
Bx6GScreenClient screen = new Bx6GScreenClient("MyScreen",new Bx6M());
// 创建screen对象，用于对控制器进行访问，串口模式
Bx6GScreenRS screen = new Bx6GScreenRS("MyScreen",new Bx6M());
```

## 2.3 屏幕连接

在对控制器交互之前，需要先与控制器建立连接，代码如下：

```java
// 连接控制器
// 其中，192.168.100.199为控制器的实际IP地址，请根据实际情况填写
// 如果不知道控制器IP地址，请先使用LedshowTW软件设置IP地址，软件下载地址：https://www.onbonbx.com/download/165.html
// 端口号默认为5005
// 五代控制器和六代控制器屏幕连接方法一样
screen.connect("192.168.100.199",5005);
```

与控制器交互完成后，需断开与控制器之间的连接，其代码如下：

```java
// 断开与控制器之间的连接
screen.disconnect();
```

## 2.4 屏幕控制

```java 
// 以下为一些简单控制命令的使用方法
// 开关机命令
screen.turnOff();// 关机
screen.turnOn();// 开机
screen.syncTime();// 校时
screen.ping();// ping命令
// 查询控制器状态
screen.checkControllerStatus();
// 查询控制器当前固件版本
screen.checkFirmware();
// 查询控制器内存
screen.checkMemVolumes();
// 锁定屏幕当前画面
screen.lock();
// 解除锁定屏幕当前画面
screen.unlock();
// 通过以下接口回读控制器状态
Bx5GScreen.Result<ReturnControllersStatus> result = screen.checkControllerStatua();
if(result.isOK())
{
    ReturnControllerStatus status = result.reply;
    status.getBrightness();// 取得亮度值
    status.getTemperature1();// 取得温度传感器温度值
    // status还有很多接口，根据实际应用进行调用
}
else
{
    ErrorType error = result.getError();
    System.out.println(error);
}
```

### 2.4.1 ping命令
```java 
screen.ping();  // ping命令
```
### 2.4.2 校时命令
```java 
screen.syncTime(); // 校时命令
```
### 2.4.3开关机命令
```java 
screen.turnOn(); // 开机命令
screen.turnOff(); // 关机命令
```

## 2.5 设置屏参
以下为设置屏参方法
注意：显示屏参数只需要在显示屏安装后使用一次即可。没有必要每次发送节目前都设置。
```java 
ControllerConfigBxFile bxFile = new ControllerConfigBxFile();
bxFile.setControllerName("test");// 设置屏幕名称
bxFile.setScreenWidth(128); // 设置显示屏的宽度 （单位：像素）
bxFile.setScreenHeigth(96); // 设置显示屏的高度 （单位：像素）
screen.writeConfig(bxFile); // 将参数文件写入控制器
// 通常参数配置完后，控制器会进行重启，需等待一段时间再对控制器进行操作
Thread.sleep(10000);
```
可以通过screen的getProfile方法取得控制器参数，也可以通过此方法验证控制器的参数是否正确
```java
Bx5GScreenProfile profile = screen.getProfile();
org.apache.log4j.Logger.getLogger(BxClientDemo.class).info("screen width:"+profile.getWidth);
org.apache.log4j.Logger.getLogger(BxClientDemo.class).info("screen heigth:"+profile.getHeigth);
```
注意：如果想提高发送节目的效率，且自己知道现实屏参的情况下，可以手动创建profile对象，screen.getProfile()此方式，是通过从控制器回读来自动创建profile的，使用方便，但是增加了PC与控制器之间的交互，从而使得发送效率降低。
```java 
profile = new Bx5GScreenProfile(128,96);
profile.setMatrixType(Bx5GScreenProfile.ScreenMatrixType.COLOR3BYTE);
```


## 2.6 节目与区域

节目主要用于组合屏幕上现实的内容，它由多个区域组成。控制器同一时间只能播放一个集美，它是控制器现实内容可以单独更新的最小单位。
创建节目过程通常如下：
* 1.创建PrograBxFile对象（节目文件）
* 2.创建一个或多个Area对象（区域，可以是图文区，也可以是时间区等）
* 3.如果是图文区（TextCaptionBxArea），可以创建page对象，按页将文本或图片添加到Area中
* 4.将Area添加到节目中
* 5.用screen对象将节目发送到控制器
以下，我们将按步骤创建一个节目，并将其发送到控制器进行显示

```java
// 创建节目文件
// 第二个参数为显示屏属性，具体可以参照Bx5GScreenProfile类
Bx5GScreenProfile profile = screen.getProfile();
ProgramBxFile p0 = new ProgramBxFile("P000",profile);
// 关于节目类的其他接口可以参考ProgramBxFile类
```
节目的其他常用接口
```java 
// 增加播放时间区段
// 参数依次为 开始时 开始分 开始秒 结束时 结束分 结束秒
p0.addPlayPeriodSetting(0,0,0,23,59,59);
// 设定结束播放日
p0.setEndDay(20);
// 设定结束播放月
po.setEndMonth(3);
// 设定结束播放年
p0.setEndYear(2021);
// 设定是否显示边框
// 需要注意的是，边框也需要占用屏幕的像素，加了节目边框后，区域坐标不能从/// （0,0）开始，会导致区域和节目边框重叠
p0.setFrameShow(false);
// 设定节目重复播放次数，多个节目时候生效
p0.setProgramPlayTimes(3);
// 设定节目播放时间长度，单位为秒s,0表示循环播放
// 当控制器上有多个节目时，会根据此设定控制节目被播放的时长
// 当控制器上只有一个节目时，此设定没有效果
// 控制节目播放有效时间，可利用addPlayPeriodSetting规划。
p0.setProgramTimeSpan(0);
// 设定开始播放日
p0.setStartDay(3);
// 设定开始播放月
p0.setStartMonth(5);
// 设定开始播放年，有效年至2099，-1表示立即播放
p0.setStartYear(2019);
// 设定播放起始日
p0.setupStartEndDate(java.util.Date startDate,java.util.Date endDate);
```


控制器支持的区域有很多种，例如：图文区、时间区、传感器区等。其中，最常用的是图文区。图文区可以支持显示文本和图片，文字或图片可以按数据也依次添加到图文区中，每页数据均可以设置特技方式，停留时间等属性
创建图文区的步骤大致如下：
创建TextCaptionBxArea对象
创建TextBxPage或ImageFileBxPage对象
将创建好的page对象添加到TextCaptionBxArea中
如下代码，创建一个文本区，并向这个区域中添加一个文本页

```java 
// 创建一个图文区
// 参数为X、Y、width、heigth
// 注意区域左边和宽度高度，不要越界
TextCaptionBxArea area = new TextCaptionBxArea(0,0,160,64);
// 创建一个数据页，并希望显示“仰邦科技”这几个字
TextBxPage page = new TextBxPage("仰邦科技");
// 将page添加到area中
area.addPage(page);
// 将图文区添加到节目中
p0.addArea(area);
```

区域的其他常用接口
```java
// 添加图片
area.addImageFile("E:image/001.bmp");
// 添加文字内容
area.addText("显示内容",new Font("宋体",Font.PLAIN,12),Color.red,Color.black,styles[4]);
// 清除所有页面
area.clearPages();
```
数据页的其他常用接口
```java 
// 换行
page.newLine("这是第二行");
// 换行还有另一种办法，即
page = new TextBxPage("第一行数据\r\n第二行数据");
// 设定背景色
page.setBackground(Color.black);
// 设定文字颜色
page.setForeground(Color.red);
// 设定字型
// 如果设定字型后，文字在LED屏上显示方块，请先检查程序所在的系统
// 是否支持所设置的字型
// 比如在Linux系统中，没有安装中文字体“宋体”，LED屏上会显示方块
page.setFont(new Font("宋体",Font.PLAIN,12));
// 设定首位相连
// >=0 前后文字间隔的像素
// -2 前后文字被隔离
page.setHeadTailInterval(-2);
// 设定水平对齐方式 
// CENTER 居中
// FAR 居右
// NEAR 居左
page.setHorizontalAlignment(TextBinary.Alignment.CENTER);
// 设定垂直对齐方式
// CENTER 居中
// FAR 居下
// NEAR 居上
page.setVerticalAlignment(TextBinary.Alignment.CENTER);
// 设定播放特效
DisPlayStyle[] styles = DisPlayStyleFactory.getStyles().toArray(new DisPlayStyle[0]);
// 0 随机显示
// 1 静止显示
// 2 快速打出
// 3 向左移动
// 4 向左连移
// 5 向上移动
// 6 向上连移
// 7 闪烁
// 8 飘雪
// 9 冒泡
// 10 中间移出
// 11 左右移入
// 12 左右交叉移入
// 13 上下交叉移入
// 14 画卷闭合
// 15 画卷打开
// 16 向左拉伸
// 17 向右拉伸
// 18 向上拉伸
// 19 向下拉伸
// 20 向左镭射
// 21 向右镭射
// 22 向上镭射
// 23 向下镭射
// 24 左右交叉拉幕
// 25 上下交叉拉幕
// 26 分散左拉
// 27 水平百叶
// 28 垂直百叶
// 29 向左拉幕
// 30 向右拉幕
// 31 向上拉幕
// 32 向下拉幕
// 33 左右闭合
// 34 左右对开
// 35 上下闭合
// 36 上下对开
// 37 向右移动
// 38 向右连移
// 39 向下移动
// 40 向下连移
// 41 45度左旋
// 42 180度左旋
// 43 90度左旋
// 44 45度右旋
// 45 180度右旋
// 46 90度右旋
// 47 菱形打开
// 48 菱形闭合

page.setDisPlayStyle(styles[4]);
// 设定重复次数
page.setRepeatTime(1);
// 设定速度等级0最快 63最慢
page.setSpeed(18);
// 设定停留时间
page.setStayTime(30);

```

关于时间区
时间区的创建过程大致如下：
创建DateTimeBxArea对象
设置各时间单元显示格式
将DateTimeBxArea添加到节目中

```java
// 下面代码创建了一个时间区
// 注意：只需输入时间区的起始坐标，区域的宽度和高度SDK会根据字体和显示方式自动计算
DateTimeBxArea dtArea = new DateTimeBxArea(0,0,screen.getProfile());
// 设置字体
dtArea.setFont(new Font("宋体",Font.PLAIN,12));
// 设置显示颜色
dtArea.setForeground(Color.yellow);
// 多行显示还是单行显示
dtArea.setMultiline(true);
// 年月日显示格式
// 如果不需要显示，设置为null
dtArea.setDateStyle(DateStyle.YYY_MM_DD_1);
dtArea.setTimeStyle(TimeStyle.HH_MM_SS_1);
dtArea.setWeekStyle(null);
// 将时间区添加到节目中
p0.addArea(dtArea);
// 关于DateTimeBxArea类的更多接口，请参考JAVADOC中相关类的说明
```


更新节目

```java 
// 以下为更新节目命令
screen.WriteProgram(p0);
// 更新节目还有很多命令，请参考JAVADOC
```

关于语音
语音功能目前只有6代部分控制卡支持
以下为一个完整的语音功能实例
```java 
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
```
关于文字转换图片
有时候，需要将文字转换成图片，然后发送给控制器，在LED屏上显示，因为控制器不支持表格，需要将表格转换成图片，SDK不支持文字的旋转显示，需要将文字转换成图片等
以下是将文字转换成图片类，仅供参考
```java 
import sun.awt.image.ImageFormatException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.utial.ArrayList;
import java.utial.Iterator;
import java.utial.List;

public class TxtToBmpFactory
{
    private static final String TAG = "TxtToBmpFactory";

    // 图片裁剪模式
    private static final int CROP_LEFT_RIGHT = 0;

    // Font
    private Font font;

    private int width = 16;
    private int height = 16;
    private int letterSpacing = 0;
    private String text = "hello";
    private int cropMode = CROP_LEFT_RIGHT;
    private boolean underline = false;


    public TxtToBmpFactory(int width, int height, String text, Font font) {
        this.width = width;
        this.height = height;
        this.font = font;
        this.text = text;
    }


    public List<BxBmpPage> create() {

        // page list
        List<BxBmpPage> bmps = new ArrayList();


        BufferedImage preview = null;

        // 获取文字属性
        FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();

        // 根据字体大小获取系统推荐的字体高度
        int charHeight = descent + ascent;

        // 字符个数
        int charCount = text.length();

        int dstX, dstY;

        // 正常排列

        // 整个字符串需要的宽度
        // 每个字符的宽度可能会不一致
        //int strWidth = (int)paint.measureText(text) + letterSpacing * (charCount - 1);
        int strWidth = letterSpacing * (charCount - 1);
        char[] chars = text.toCharArray();
        for(int i=0; i<charCount; i++) {
            //strWidth += (int)paint.measureText(text, i, i+1);
            //strWidth += fm.charWidth(chars, i, i+1);
            strWidth += fm.charWidth(chars[i]);
        }

        if(strWidth <= this.width) {

            // 如果整个字符串的长度小于屏幕宽度
            // 则只需按屏幕大小创建 bmp　文件
            //preview = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            preview = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);

            // 居中对齐
            dstX = (this.width - strWidth) / 2;
        }
        else {

            // 如果整个字符串的长度大于屏幕宽度
            // 则需按字符串的长度大小创建 bmp 文件
            //preview = Bitmap.createBitmap(strWidth, this.height, Bitmap.Config.ARGB_8888);
            preview = new BufferedImage(strWidth, height, BufferedImage.TYPE_USHORT_565_RGB);

            // 居左对齐
            dstX = 0;
        }

        //Canvas canvas = new Canvas(preview);
        Graphics2D g = preview.createGraphics();
        g.setFont(font);
        g.setColor(Color.RED);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);


        // 垂直方向居中对齐
        dstY = this.height/2 + Math.abs(ascent) - (charHeight/2);
        dstY = (int) ((float)dstY * 0.95f);
        //float ratio = (float)(ascent)*0.95f/(float)(ascent + descent);
        //dstY = (int) (height * ratio);

        g.translate(dstX, dstY);

        int x = 0;
        int y = 0;

        for(int i=0; i<charCount; i++) {
            String mChar = text.substring(i, i+1);
            g.drawString(mChar, x, y);
            x += fm.stringWidth(mChar);
            x += letterSpacing;
        }


        // 图片分割
        if(cropMode == CROP_LEFT_RIGHT) {
            int pageNum = preview.getWidth() / width;
            int i = 0;
            for (i = 0; i < pageNum; i++) {
                //BufferedImage bmp = new BufferedImage(preview, i * width, 0, width, height);
                BufferedImage bmp = preview.getSubimage(i*width, 0, width, height);
                BxBmpPage page = new BxBmpPage(bmp);
                bmps.add(page);
            }

            if((preview.getWidth() % width) > 0) {
                //Bitmap src = Bitmap.createBitmap(preview, i*width, 0, preview.getWidth()%width, height);
                BufferedImage src = preview.getSubimage(i*width, 0, preview.getWidth()%width, height);

                //Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                BufferedImage bmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g1 = bmp.getGraphics();
                g1.drawImage(src, 0, 0, src.getWidth(), src.getHeight(), new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });

                BxBmpPage page = new BxBmpPage(bmp, preview.getWidth()%width, height);
                bmps.add(page);
            }
        }
        else {

        }

        return bmps;
    }


    

   public int getWidth(){return width;}

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight(){return height;}

    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public String getText(){return text;}
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    public void getLetterSpacing()
    {
        return letterSpacing + 4;
    }
    
    public void setLetterSpacing(int letterSpacing)
    {
        this.letterSpacing = letterSpacing - 4;
    }
    
    public Font getFont(){return font;}
    
    public void setFont(Font font){this.font = font;}
}
```

## 2.7 动态区

动态区是一种比较特殊的区域，其有以下几个主要特点：

* 更新次数没有限制
* 内容掉电不保存
* 独立于节目进行编辑
* 可以支持多个区域，且每个区域可以进行单独更新
* 可以和单个活多个节目绑定显示，即作为节目的一个区域进行显示
* 可以作为单独一个节目进行独立播放
* 灵活的控制方式：超时时间控制、是否立即显示灯

以下为动态区代码

```
// 五代控制器动态区（BX-5E系列）
// BX-5E系列控制卡最高支持4个动态区，当屏幕上需要同时显示多个动态区时，动态区ID不可以相同
// DynamicBxRule(id,runMode,immediatePlay,timeout);
// runMode 运行模式
// 0:循环显示
// 1:显示完成后静止显示最后一页数据
// 2:循环显示，超过设定时间后数据仍未更新时不显示
// 3:循环显示，超过设定时间后数据仍未更新时显示Logo信息
// 4:循环显示，显示完最后一页后不再显示
// immediatePlay
// 0:与异步节目一起播放
// 1:异步节目停止播放，仅播放动态区
// 2:当播放完节目编号最高的异步节目后播放该动态区
DynamicBxAreaRule rule = new DynamicBxRule(0,(byte)0,(byte)1,0);
TextCaptionBxArea darea = new TextCaptionBxArea(0,0,160,64,screen.getProfile());
TextBxPage dapge = new TextBxPage("动态区123abc");
darea.addPage(dpage);
screen.writeDynamic(rule,darea);

// 六代控制器动态区
DynamicBxAreaRule rule = new DynamicBxAreaRule();
// 设定动态区ID，此处ID为0，多个动态区ID不能相同
rule.setId(0);
// 设定异步节目停止播放，仅播放动态区
// 0:动态区与异步节目一起播放
// 1:异步节目停止播放，仅播放动态区
// 2:当播放完节目编号最高的异步节目后播放该动态区
rule.setImmediatePlay((byte)1);
// 设定动态区循环播放
// 0:循环播放
// 1:显示完成后静置显示最后一页数据
// 2:循环显示，超过设定时间后仍未更新时不再显示
// 3:循环显示，超过设定时间后仍未更新时显示Logo信息
// 4:显示完成最后一页后就不再显示
rule.setRunMode((byte)0);
DynamicBxArea area = new DynamicBxArea(0,0,160,32,screen.getProfile());
TextBxPage page = new TextBxPage("动态区abc");
area.addPage(page);
screen.writeDynamic(rule,area);
```

## 2.8 Server模式（包含GPRS）

Server使用流程如下：

* 初始化API（在一个进程内只需要初始化一次）
* 建立服务器
* 设定监听等待屏幕的连线与断线事件
* 关闭服务器

主要代码如下：

```java 
Bx5GEnv.initial(); //初始化
// 建立服务器
Bx5GServer server = new Bx5GServer("Hello Screen", 8036);
// 添加监听
server.addListener(new ConnectionListener());
// 启动服务器
server.start();
System.out.println("waiting 30 secs");
Thread.sleep(30000);
// 更新节目或者动态区
//
//
//


// 关闭服务器
server.stop();
System.out.println("done!");

// 监听
public class ConnectionListener implements Bx5GServerListener {
@Override
public void connected(String socketId, String controllerAddr,
Bx5GScreen screen) {
Result<ReturnPingStatus> result1 = screen.ping();
Result<ReturnControllerStatus> result2 =
screen.checkControllerStatus();
...
}
@Override
public void disconnected(String socketId, String controllerAddr,
Bx5GScreen screen) {
...
}
}
```





