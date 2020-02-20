
import onbon.y2.Y2ScreenFactory;
import onbon.y2.Y2ScreenFactory.ResponseHandler;
import onbon.y2.message.net.QueryWifiStatusOutput;
import onbon.y2.message.net.RestartNetworkOutput;
import onbon.y2.message.net.SearchControllerOutput;
import onbon.y2.message.net.SetNetworkOptionInput;
import onbon.y2.message.net.WifiConnectOutput;

//import org.junit.Test;

public class Y2ScreenFactoryTest {
	
	private String localAddress = "192.168.100.200";
	
	/*public static void main(String[] args) throws Exception
	{
		Y2ScreenFactoryTest yt = new Y2ScreenFactoryTest();
		yt.test31();
	}*/

	/**
	 * 3.1 搜索控制器 - searchController
	 * 
	 * @throws Exception Failed to test.
	 */
	//@Test
	public  void test31() throws Exception {
		Y2ScreenFactory factory = new Y2ScreenFactory(this.localAddress);
		factory.start();
		factory.listenSearchControllers(new ResponseHandler<SearchControllerOutput>() {

			@Override
			public void run(String pid, String barcode, SearchControllerOutput output) {
				System.out.println(pid);
				System.out.println(barcode);
				System.out.println(output.getIp());
			}
		});
		factory.searchControllers();
		Thread.sleep(5000);
		factory.stop();
		System.out.println("client closed");
	}
	
	/**
	 * 3.2 设置网络属性 - setNetworkOption
	 * 
	 * @throws Exception Failed to test.
	 */
	//@Test
	public void test32() throws Exception {
		SetNetworkOptionInput input = new SetNetworkOptionInput();
		input.setIpMode("static");
		input.setSubnetMask("255.255.255.0");
		input.setIp("192.168.100.140");
		input.setGateway("192.168.100.1");
		
		Y2ScreenFactory factory = new Y2ScreenFactory(this.localAddress);
		factory.start();
		factory.listenNetworkOption(new ResponseHandler<Object>() {

			@Override
			public void run(String pid, String barcode, Object output) {
				System.out.println(pid);
				System.out.println(barcode);
				System.out.println("good");
			}
		});
		factory.updateNetworkOption(input);
		Thread.sleep(5000);
		factory.stop();
		System.out.println("client closed");
	}

	/**
	 * 3.3 重启网络 - restartNetwork
	 * 
	 * @throws Exception Failed to test.
	 */
	//@Test
	public void test33() throws Exception {
		Y2ScreenFactory factory = new Y2ScreenFactory(this.localAddress);
		factory.start();
		factory.listenRestartNetwork(new ResponseHandler<RestartNetworkOutput>() {

			@Override
			public void run(String pid, String barcode, RestartNetworkOutput output) {
				System.out.println(pid);
				System.out.println(barcode);
				System.out.println(output.getMaxWaitTime());
				System.out.println(output.getMinWaitTime());
			}
		});
		factory.restartNetwork();
		Thread.sleep(5000);
		factory.stop();
		System.out.println("client closed");
	}

	/**
	 * 3.6 连接WiFi - wifiConnect
	 * 
	 * @throws Exception Failed to test.
	 */
	//@Test
	public void test36() throws Exception {
		Y2ScreenFactory factory = new Y2ScreenFactory(this.localAddress);
		factory.start();
		factory.listenConnectWifi(new ResponseHandler<WifiConnectOutput>() {

			@Override
			public void run(String pid, String barcode, WifiConnectOutput output) {
				System.out.println(pid + " connected");
			}
		});
		factory.conectWifi("JIQING", "1234567890");
		Thread.sleep(5000);
		factory.stop();
		System.out.println("client closed");
	}

	/**
	 * 3.7 查询WiFi - queryWifiStatus
	 * 
	 * @throws Exception Failed to test.
	 */
	//@Test
	public void test37() throws Exception {
		Y2ScreenFactory factory = new Y2ScreenFactory(this.localAddress);
		factory.start();
		factory.listenWifiStatus(new ResponseHandler<QueryWifiStatusOutput>() {

			@Override
			public void run(String pid, String barcode, QueryWifiStatusOutput output) {
				System.out.println(pid +", " + output.getWifiStatus());
			}
		});
		factory.queryWifiStatus();
		Thread.sleep(5000);
		factory.stop();
		System.out.println("client closed");
	}

	/**
	 * 3.8 断开WiFi - wifiDisconnect
	 * 
	 * @throws Exception Failed to test.
	 */
	//@Test
	public void test38() throws Exception {
		Y2ScreenFactory factory = new Y2ScreenFactory(this.localAddress);
		factory.start();
		factory.listenDisconnectWifi(new ResponseHandler<Object>() {

			@Override
			public void run(String pid, String barcode, Object output) {
				System.out.println(pid + " disconnected");
			}
		});
		factory.disconectWifi();
		Thread.sleep(5000);
		factory.stop();
		System.out.println("client closed");
	}

	/**
	 * 3.9 设置热点 - setApProperty
	 * 
	 * @throws Exception Failed to test.
	 */
	//@Test
	/*public void test39() throws Exception {
		Y2ScreenFactory factory = new Y2ScreenFactory(this.localAddress);
		factory.start();
		factory.listenApProperties(new ResponseHandler<Object>() {

			@Override
			public void run(String pid, String barcode, Object output) {
				System.out.println("done");
			}
		});
		factory.listenApProperties("Y04","133333","192.168.100.1");
		Thread.sleep(5000);
		factory.stop();
		System.out.println("client closed");
	}*/
}
