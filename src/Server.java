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
	
	private Database database = new Database();                               //�����������ݿ����
	
	//��������
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
		for(int i =0; i< MAX;i++)
			socketArr[i] = null;
		setLayout(new BorderLayout());
		add(new JScrollPane(jta),BorderLayout.CENTER);
		setTitle("Server");
		setSize(500,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		ServerSocket serverSocket = new ServerSocket(8000);
		threeTranslation = new String[3];
		for(int i = 0;i < 3;i++)
			threeTranslation[i] = "";
		threePraise = new int[3];
		
		
		int clientNo = 0;
		
		//���տͻ��˵Ĵ򿪣�����266~267ҳ
		while(true)
		{
			socketArr[clientNo] = serverSocket.accept();
			InetAddress inetAddress = socketArr[clientNo].getInetAddress();
			String name = inetAddress.getHostName();
			String address = inetAddress.getHostAddress(); 
			jta.append("" + (clientNo) +'\n');
			jta.append(name +'\n');
			jta.append(address +'\n');
			
			HandleAClient task = new HandleAClient(socketArr[clientNo],(clientNo));
			executor.execute(task);
			clientNo++;
			
		}
	}
	
	class HandleAClient implements Runnable
	{
		private Socket cursocket;
		private int curclientNo;

		public HandleAClient(Socket cursocket,int clientNo)
		{
			this.cursocket = cursocket;
			this.curclientNo = clientNo;
		}
		
		public void run()
		{
			try{
				while(true){
					inputFromClient = new DataInputStream(cursocket.getInputStream());
						if(inputFromClient != null)
						{
							//Client�������ݰ��ǹ̶�һ�¸�ʽ�ģ���Server��Ӧ��ʱtype�������typeһ��
						    type = inputFromClient.readInt();
						    jta.append("************************begin\n");
						    jta.append("type is " + type + "\n");								   
			
						   	//�����͸�ָ���û����ʿ�
						   	if(type == 1)
							{
						   		
							}
						}	
						   
				}
			}
			catch(IOException ex)
			{
				System.out.println(ex);
			} 
		}
	}
}
