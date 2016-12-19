import java.io.*;
import java.net.*;
import java.awt.*;

import javax.swing.*;

import java.util.concurrent.*;

public class Server extends JFrame{

	private JTextArea jta = new JTextArea();
	private final int MAX = 100;
	private Socket[] socketArr = new Socket[MAX];  
	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;
	
	private Database database = new Database();                               //新增连接数据库的类
	
	//数据类型
	private int type = 0;
	private String word;
	private String[] threeTranslation;
	private int[] threePraise;
	
	private String sendUserName;
	private String sendTranslation;
	
	private String userName;
	private String password;
	private String IP_address;
	private int port;

	ExecutorService executor = Executors.newFixedThreadPool(50);
	public static void main(String[] args) throws IOException 
	{
		new Server();
	}
	
	
	public Server() throws IOException
	{
		
	}
}
