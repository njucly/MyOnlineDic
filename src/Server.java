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
						   		jta.append("�����͵��ʿ�\n");
						   		inputFromClient = new DataInputStream(cursocket.getInputStream());
						   		sendUserName = inputFromClient.readUTF();
						   		String receiveUserName = inputFromClient.readUTF();
								sendTranslation = inputFromClient.readUTF();
								jta.append("from " + sendUserName + "to " + receiveUserName + "\n");
								port = database.findPort(receiveUserName);
								
								if(port == -1)                           
								{
									outputToClient = new DataOutputStream(cursocket.getOutputStream());
									outputToClient.writeInt(9);           				//�������ͣ�clientҪ���͵��û�û�ҵ�
								}
								
								else
								{
									outputToClient = new DataOutputStream(socketArr[port].getOutputStream());		
									outputToClient.writeInt(type);
									outputToClient.writeUTF(sendUserName);
									outputToClient.writeUTF(sendTranslation);	
								}
							}
						   	
						    //�ͻ���������µ��޴���
							else if(type == 2)         
							{
								inputFromClient = new DataInputStream(cursocket.getInputStream());
								int web = inputFromClient.readInt();
								word = inputFromClient.readUTF();
								jta.append("���� " + word + "���޴���\n");
								if(web == 1)
									database.updateBaiduPraise(word);
								else if(web == 2)
									database.updateYoudaoPraise(word);
								else if(web == 3)
									database.updateBiyingPraise(word);
								
								outputToClient = new DataOutputStream(cursocket.getOutputStream());
								threePraise = database.getThreePraise(word);
								outputToClient.writeInt(type);
								outputToClient.writeInt(threePraise[0]);
								outputToClient.writeInt(threePraise[1]);
								outputToClient.writeInt(threePraise[2]);
							}
							else if(type == 3)
							{
								jta.append("ע��\n");
								inputFromClient = new DataInputStream(cursocket.getInputStream());
								outputToClient = new DataOutputStream(cursocket.getOutputStream());
								//ע�ᣬ��user���в���һ����¼
								userName = inputFromClient.readUTF();
								password = inputFromClient.readUTF();
								IP_address = inputFromClient.readUTF();
							    jta.append("IP:" + IP_address + "\n");
								boolean isSuccess = database.isRegisterSuccess(userName);
								
								if(isSuccess)
								{
									database.insertRegister(userName,password,IP_address,curclientNo);
									outputToClient.writeInt(type);
									outputToClient.writeBoolean(true);
									outputToClient.writeUTF(userName);
								}
								else
								{
									outputToClient.writeInt(type);
									outputToClient.writeBoolean(false);
								}
							}
							else if(type == 4)
							{
								jta.append("��¼\n");
								inputFromClient = new DataInputStream(cursocket.getInputStream());
								outputToClient = new DataOutputStream(cursocket.getOutputStream());
								//��¼������user��
								userName = inputFromClient.readUTF();
								password = inputFromClient.readUTF();
								jta.append("login in:"+userName+"\n");
								int isLoginSuccess = database.isLoginSuccess(userName,password);
								jta.append(isLoginSuccess + "\n");
								if(isLoginSuccess == 1)//�ɹ���¼
								{
									database.changeToLoginState(userName);
									database.changePort(curclientNo,userName);
									outputToClient.writeInt(type);
									outputToClient.writeBoolean(true);
									outputToClient.writeUTF(userName);
								}
								else if(isLoginSuccess == 0) //��¼ʧ��
								{
									outputToClient.writeInt(type);
									outputToClient.writeBoolean(false);
								}
								else if(isLoginSuccess == 2) //�Ѿ���¼
								{
									outputToClient.writeInt(10);
								}
									
							}
						   	
						   	//��client���������û��б�
							else if(type == 5)
							{
								jta.append("���������û�\n");
								inputFromClient = new DataInputStream(cursocket.getInputStream());
								userName = inputFromClient.readUTF(); 
								int length = database.countOnlineUser();
								String[] userList = new String[length];
								userList = database.getUserList();
								int[] loginPort = new int[length];
								loginPort = database.getPorts();

								if(length == 1)
								{
									outputToClient = new DataOutputStream(cursocket.getOutputStream());
									outputToClient.writeInt(5);
									outputToClient.writeInt(length);
								
									for(int k = 0;k < length;k++)
										outputToClient.writeUTF(userList[k]);
								}
								
								else
								{
									int i = 0;
									while(i < length)
									{
										outputToClient = new DataOutputStream(socketArr[loginPort[i]].getOutputStream());
										outputToClient.writeInt(8);
										outputToClient.writeInt(length);
								
										for(int j = 0;j < length;j++)
											outputToClient.writeUTF(userList[j]);
										i++;
									}
								
									outputToClient = new DataOutputStream(cursocket.getOutputStream());
									outputToClient.writeInt(5);
									outputToClient.writeInt(length);
								
									for(int k = 0;k < length;k++)
										outputToClient.writeUTF(userList[k]);
								}
								
							}
						   	
						   	//��¼���û������˳�
							else if(type == 6)
							{
								jta.append("���û��˳��������б�\n");
								inputFromClient = new DataInputStream(cursocket.getInputStream());
								userName = inputFromClient.readUTF();
								jta.append(userName + "�˳���¼\n");
								database.changeToExitState(userName);	
								int length = database.countOnlineUser();
								if(length == 0)
									jta.append("no user in it");
																
								else
								{	
									String[] userList = new String[length];
									userList = database.getUserList();
								    
								    int[] loginPort = new int[length];
									loginPort = database.getPorts();
								    
									int i = 0;
									while(i < length)
									{
										outputToClient = new DataOutputStream(socketArr[loginPort[i]].getOutputStream());
										outputToClient.writeInt(8);
										outputToClient.writeInt(length);
								
										for(int j = 0;j < length;j++)
											outputToClient.writeUTF(userList[j]);
										i++;
									}
								}
							}
						   	
						   	//client�����ȡ������˼�����ظõ�����������վ�ĵ��޴���
							else if(type == 7)
							{ 
								jta.append("��ȡ���ʼ����޴���\n");
								inputFromClient = new DataInputStream(cursocket.getInputStream());
								jta.append("here get into type 7\n");
								word = inputFromClient.readUTF();
								jta.append(word);
								threeTranslation = database.getThreeTranslation(word);
								
								threePraise = database.getThreePraise(word);
								
								outputToClient = new DataOutputStream(cursocket.getOutputStream());
								outputToClient.writeInt(type);
								
								if(threeTranslation[0] != null)
								{
									outputToClient.writeInt(1);        	   //��ʾ������
									for(int i = 0;i < 3;i++)
										outputToClient.writeUTF(threeTranslation[i]);
									for(int i = 0;i < 3;i++)
										outputToClient.writeInt(threePraise[i]);
								}
								else
									outputToClient.writeInt(0);           //δ�鵽��������
							}
						   	
							else if(type == 8) //Ⱥ�����ʿ�
							{
								jta.append("Ⱥ�����ʿ�\n");
								inputFromClient = new DataInputStream(cursocket.getInputStream());
								String senderName = inputFromClient.readUTF();
								String translation = inputFromClient.readUTF();
								int length = database.countOnlineUser();
								jta.append("length:" + length + "\n");
								if(length == 0)
									jta.append("no user in it");
																
								else
								{					       
								    int[] loginPort = new int[length];
									loginPort = database.getPorts();
								    
									int i = 0;
									while(i < length)
									{
										if(socketArr[loginPort[i]] != cursocket)
										{
											outputToClient = new DataOutputStream(socketArr[loginPort[i]].getOutputStream());
											outputToClient.writeInt(1);
											outputToClient.writeUTF(senderName);
											outputToClient.writeUTF(translation);
										}
										i++;
									}
								}
							}
						   	jta.append("************************end\n");
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
