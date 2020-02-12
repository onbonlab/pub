package onbon.bx05.tutorial.server;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GServer;
import onbon.bx05.Bx5GServerListener;

public class SimpleCommand {

    public static void main(String[] args) throws Exception {
        // 初始化API
        Bx5GEnv.initial("log.properties");

        // 创建server 端口号8001
        Bx5GServer server = new Bx5GServer("TEST", 8001);

        // 添加监听
        server.addListener(new ConnectionListener());

        // 启动服务
        server.start();
        System.out.println("waiting...");

        Thread.sleep(120000);

        // 停止服务
        server.stop();
        System.out.println("done!");
    }

    public static class ConnectionListener implements Bx5GServerListener {

        @Override
        public void connected(String socketId, String netId, Bx5GScreen screen) {
            // 常用命令
            System.out.println(socketId + " online:" + netId);
            System.out.println("ping:     " + screen.ping());						// PING 命令
            System.out.println("status:   " + screen.checkControllerStatus());		// 查询控制器状态
            System.out.println("frimware: " + screen.checkFirmware());				// 查询固件状态
            System.out.println("id:       " + screen.readControllerId());			// 读取控制器id

            // TODO: 其他通讯
        }

        @Override
        public void disconnected(String socketId, String controllerAddr, Bx5GScreen screen) {
            // 断开连接

            // TODO: ���� screen �YӍ�������_�lϵ�y���M�Дྀ̎��
        }
    }
}
