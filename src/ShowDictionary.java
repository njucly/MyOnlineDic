import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ShowDictionary extends JFrame{

	private JButton register = new JButton();
	private JButton login = new JButton();
	private JTextField input = new JTextField();
	private JButton search = new JButton();
	private JCheckBox baiduChoose = new JCheckBox();
	private JCheckBox youdaoChoose = new JCheckBox();
	private JCheckBox biyingChoose = new JCheckBox();
	private JList<String> userOnline = new JList<String>();
	private JTextArea baiduInterpret = new JTextArea();
	private JTextArea youdaoInterpret = new JTextArea();
	private JTextArea biyingInterpret = new JTextArea();
	private JTextField baiduUserId = new JTextField();
	private JTextField youdaoUserId = new JTextField();
	private JTextField biyingUserId = new JTextField();
	private JButton baiduSendCard = new JButton();
	private JButton youdaoSendCard = new JButton();
	private JButton biyingSendCard = new JButton();
	private JButton baiduPraise = new JButton();
	private JButton youdaoPraise = new JButton();
	private JButton biyingPraise = new JButton();
	private JButton baiduSendAll = new JButton();//群发按钮
	private JButton youdaoSendAll = new JButton();
	private JButton biyingSendAll = new JButton();
	private JPanel registerAndLogin = new JPanel();
	private JPanel baidu = new JPanel();
	private JPanel youdao = new JPanel();
	private JPanel biying = new JPanel();
	private JPanel interpret = new JPanel();//显示三个网站的翻译,设置为属性，以便后续根据点赞数量调整三个翻译的顺序
	private JPanel panel = new JPanel();//将所有组建放在panel上
	private Vector<String> vector = new Vector<String>();
	private JPanel panelUp = new JPanel();//显示登录、注册、搜索、三个选项
	private JPanel panelOfChoose = new JPanel();//显示三个选项
	private JPanel baiduSendWord = new JPanel();//显示百度用户id框和发送按钮和点赞按钮
	private JPanel youdaoSendWord = new JPanel();//显示有道用户id框和发送按钮和点赞按钮
	private JPanel biyingSendWord = new JPanel();//显示必应用户id框和发送按钮和点赞按钮
	private JPanel userpanel = new JPanel();//显示在线用户列表
	private JTextField userText = new JTextField();//用户名
	private JButton logoff = new JButton();//退出登录按钮
	private static DataOutputStream toServer;
	private static DataInputStream fromServer;
	
	JLabel lab;
	boolean skinIsPicture = false;
	
	//数据
	private String[] threeTranslation;
	private int[] threePraise;
	
	private String sendUserName;
	private String sendTranslation;
	
	private String userName;                      //当前登录用户的名称
	
	public ShowDictionary()
	{
		threeTranslation = new String[3];
		baiduUserId.setEditable(false);
		youdaoUserId.setEditable(false);
		biyingUserId.setEditable(false);
		for(int i = 0;i < 3;i++)
			threeTranslation[i] = "";
		threePraise = new int[3];
		
		panel.setLayout(new BorderLayout(10,10));
		
		panelUp.setLayout(new BorderLayout(10,10));
		registerAndLogin.setLayout(new FlowLayout(FlowLayout.RIGHT));							//显示登陆、换肤和注册按钮
		register.setText("注册");
		login.setText("登录");
		logoff.setText("退出");
		userText.setEditable(false);
		userText.setText("游客");
		
		registerAndLogin.add(login);
		registerAndLogin.add(register);
		registerAndLogin.add(logoff);
		registerAndLogin.add(userText);
		logoff.setVisible(false);
		panelOfChoose.setLayout(new FlowLayout());
		baiduChoose.setText("百度");
		youdaoChoose.setText("有道");
		biyingChoose.setText("必应");
		panelOfChoose.add(baiduChoose);
		panelOfChoose.add(youdaoChoose);
		panelOfChoose.add(biyingChoose);
		panelUp.add(registerAndLogin,BorderLayout.NORTH);
		panelUp.add(new JLabel(" Input"), BorderLayout.WEST);
		panelUp.add(input, BorderLayout.CENTER);
		search.setText("search");
		panelUp.add(search, BorderLayout.EAST);
		panelUp.add(panelOfChoose,BorderLayout.SOUTH);
		
		
		interpret.setLayout(new GridLayout(3,1));
		baiduSendWord.setLayout(new FlowLayout(FlowLayout.RIGHT));
		baiduSendCard.setText("发送单词卡");
		baiduPraise.setText("赞 ");
		baiduSendWord.add(new JLabel("用户ID："));
		baiduSendAll.setText("群发");
		baiduSendWord.add(baiduUserId);
		baiduSendWord.add(baiduSendCard);
		baiduSendWord.add(baiduSendAll);
		baiduSendWord.add(baiduPraise);
		baidu.setLayout(new BorderLayout(10,10));
		baidu.add(new JLabel("百度"),BorderLayout.WEST);
		baiduInterpret.setEditable(false);
		baiduUserId.setColumns(15);
		baidu.add(new JScrollPane(baiduInterpret),BorderLayout.CENTER);
		baidu.add(baiduSendWord,BorderLayout.SOUTH);
		youdaoSendWord.setLayout(new FlowLayout(FlowLayout.RIGHT));
		youdaoSendCard.setText("发送单词卡");
		youdaoPraise.setText("赞");
		youdaoSendWord.add(new JLabel("用户ID："));
		youdaoSendAll.setText("群发");
		youdaoSendWord.add(youdaoUserId);
		youdaoSendWord.add(youdaoSendCard);
		youdaoSendWord.add(youdaoSendAll);
		youdaoSendWord.add(youdaoPraise);
		youdao.setLayout(new BorderLayout(10,10));
		youdao.add(new JLabel("有道"),BorderLayout.WEST);
		youdaoInterpret.setEditable(false);
		youdaoUserId.setColumns(15);
		youdao.add(new JScrollPane(youdaoInterpret),BorderLayout.CENTER);
		youdao.add(youdaoSendWord,BorderLayout.SOUTH);
		biyingSendWord.setLayout(new FlowLayout(FlowLayout.RIGHT));
		biyingSendCard.setText("发送单词卡");
		biyingPraise.setText("赞");
		biyingSendWord.add(new JLabel("用户ID："));
		biyingSendAll.setText("群发");
		biyingSendWord.add(biyingUserId);
		biyingSendWord.add(biyingSendCard);
		biyingSendWord.add(biyingSendAll);
		biyingSendWord.add(biyingPraise);
		biying.setLayout(new BorderLayout(10,10));
		biying.add(new JLabel("必应"),BorderLayout.WEST);
		biyingInterpret.setEditable(false);
		biyingUserId.setColumns(15);
		biying.add(new JScrollPane(biyingInterpret),BorderLayout.CENTER);
		biying.add(biyingSendWord,BorderLayout.SOUTH);
		interpret.add(baidu);
		interpret.add(youdao);
		interpret.add(biying);
		
		panel.add(panelUp,BorderLayout.NORTH);
		userpanel.setLayout(new BorderLayout(5,5));
		userpanel.add(new JLabel("在线用户列表："),BorderLayout.NORTH);
		userpanel.add(userOnline,BorderLayout.CENTER);
		panel.add(userpanel,BorderLayout.WEST);
		panel.add(interpret,BorderLayout.CENTER);
		add(panel);
		
		userpanel.setVisible(false);
		baidu.setVisible(false);
		youdao.setVisible(false);
		biying.setVisible(false);
		
		
		addWindowListener(new WindowAdapter()                                           //退出键的监听
		{
			public void windowClosing(WindowEvent e) 
			{
				int option = JOptionPane.showConfirmDialog(null,"确定要退出吗？","提示",JOptionPane.OK_CANCEL_OPTION);
			    if (JOptionPane.OK_OPTION == option) 
			    {
			      //点击了确定按钮
			      try
			      {
			    	if(userName != null)  
					{
			    		toServer.writeInt(6);                                 //向Server发出退出请求，Server修改islogin状态
			    		toServer.writeUTF(userName);
					}		
					
				  } 
			      catch (IOException e1) 
			      {
					e1.printStackTrace();
				  }
			      System.exit(0);
			    }
			    else
			    	ShowDictionary.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		register.addActionListener(new ActionListener()									//注册按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				Register();
			}
		});
		
		login.addActionListener(new ActionListener()								    //登录按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				Login();
			}
		});
		
		search.addActionListener(new ActionListener()									//查询按钮监听
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if(!input.getText().matches("^[a-zA-Z]*"))
				{
					JOptionPane.showMessageDialog(null, "请输入英文！", "Warning!",JOptionPane.WARNING_MESSAGE); 
					input.setText("");
				}

				
				
				else if(!input.getText().equals(""))
				{
					baidu.setVisible(false);
					youdao.setVisible(false);
					biying.setVisible(false);
					try
					{
						toServer.writeInt(7);
						toServer.writeUTF(input.getText());
					} 
				
					catch (IOException e1) 
					{
						e1.printStackTrace();
					} 
				}
			}
		});
		
		input.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(!input.getText().matches("^[a-zA-Z]*"))
					{
						JOptionPane.showMessageDialog(null, "请输入英文！", "Warning!",JOptionPane.WARNING_MESSAGE); 
						input.setText("");
					}
					else if(!input.getText().equals(""))
					{
						baidu.setVisible(false);
						youdao.setVisible(false);
						biying.setVisible(false);
						try
						{
							toServer.writeInt(7);
							toServer.writeUTF(input.getText());
						} 
						
						catch (IOException e1) 
						{
							e1.printStackTrace();
						} 
					}
				}
			}
		});
		
		baiduSendCard.addActionListener(new ActionListener()								//百度发单词卡按钮监听
		{
			public void actionPerformed(ActionEvent e) 
			{
				sendCard(0);
			}
		});
		youdaoSendCard.addActionListener(new ActionListener()								//有道发单词卡按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCard(1);
			}
		});
		biyingSendCard.addActionListener(new ActionListener()								//必应发单词卡按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCard(2);
			}
		});
		baiduSendAll.addActionListener(new ActionListener()								//百度发单词卡按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCard(3);
			}
		});
		youdaoSendAll.addActionListener(new ActionListener()								//有道发单词卡按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCard(4);
			}
		});
		biyingSendAll.addActionListener(new ActionListener()								//必应发单词卡按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				sendCard(5);
			}
		});
		baiduPraise.addActionListener(new ActionListener()									//百度点赞按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					toServer.writeInt(2);     //请求更新点赞
					toServer.writeInt(1);      //baidu
					toServer.writeUTF(input.getText());
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		youdaoPraise.addActionListener(new ActionListener()									//有道点赞按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					toServer.writeInt(2);     //请求更新点赞
					toServer.writeInt(2);     //youdao
					toServer.writeUTF(input.getText());
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		biyingPraise.addActionListener(new ActionListener()									//必应点赞按钮监听
		{
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					toServer.writeInt(2);     //请求更新点赞
					toServer.writeInt(3);     //biying
					toServer.writeUTF(input.getText());
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
	
		
		logoff.addActionListener(new ActionListener(){//退出按钮监听
			public void actionPerformed(ActionEvent e)
			{
				//update state of user login database
				try
			    {
					toServer.writeInt(6);   
					toServer.writeUTF(userName);
					userName = null;
				} 
			    catch (IOException e1) 
			    {
			    	e1.printStackTrace();
				}
				userpanel.setVisible(false);
				login.setVisible(true);
				register.setVisible(true);
				userText.setText("游客");
				logoff.setVisible(false);
			}
		});
		
		userOnline.addListSelectionListener(new ListSelectionListener()
		{

			public void valueChanged(ListSelectionEvent e) 
			{
				System.out.println("选中列表!");
				int select = userOnline.getSelectedIndex();
				if (select != -1) 
				{
					String str = vector.get(select);
					baiduUserId.setText(str);
					youdaoUserId.setText(str);
					biyingUserId.setText(str);
				}
			}
		});
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

	}
		
		
	
	public void connect() 
	{
		try
		{	
			//create a socket to connect to the server
			Socket socket = new Socket("localhost",8000);
			//Socket socket = new Socket("172.26.110.46",8000);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
			
		catch(IOException ex)
		{
			System.out.println(ex);
		}
		
		while(true)
		{
			int type;
			try 
			{
				type = fromServer.readInt();                        				//读取Server发来的数据包类型
				
				//发送单词卡
				if(type == 1)
				{
					try 
					{
						sendUserName = fromServer.readUTF();
						sendTranslation = fromServer.readUTF();
					} 
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					
					
					SwingUtilities.invokeLater(new Runnable()
					{   
						String message = "From " + sendUserName + ":"+ "\n" + sendTranslation;
				        public void run() 
				        {   
				        	JOptionPane.showMessageDialog(null, message);
				         }   
				     }); 
				}
					
				//点赞次数
				else if(type == 2)
				{
					threePraise = new int[3];
					for(int i = 0;i < 3;i++)
						threePraise[i] = fromServer.readInt();
					//threePraise 数组是三个网站的点赞次数
					baiduPraise.setText("赞 "+threePraise[0]);
					youdaoPraise.setText("赞 "+threePraise[1]);
					biyingPraise.setText("赞 "+threePraise[2]);
				}
				
				//注册
				else if(type == 3)
				{
					boolean isRegisterSuccess = fromServer.readBoolean();
					if(isRegisterSuccess == true)
					{
						userName = fromServer.readUTF();
						//得到了一个成功注册的用户，显示在右上角名字
						login.setVisible(false);
						register.setVisible(false);
						logoff.setVisible(true);
						userText.setText(userName); 
						toServer.writeInt(5);
						toServer.writeUTF(userName);
					}
					else
					{
						//注册失败，给用户提示  	Warning();
						SwingUtilities.invokeLater(new Runnable() {   
				            public void run() {   
				            	JOptionPane.showMessageDialog(null, "The name has exist!\nTry again!","Warning",JOptionPane.WARNING_MESSAGE);  
								Register();
				            }   
				        }); 
					}
				}
				
				//登录
				else if(type == 4)
				{
					boolean isLoginSuccess = fromServer.readBoolean();
					if(isLoginSuccess == true)
					{
						//登录成功，名字显示在右上角  ，不能再登录
						userName = fromServer.readUTF();
						
						login.setVisible(false);
						register.setVisible(false);
						logoff.setVisible(true);
						userText.setText(userName); 
						registerAndLogin.add(userText);
						toServer.writeInt(5);
						toServer.writeUTF(userName);
						//向Server请求获取在线用户列表信息
					}
					else
					{
						//登录失败，给用户提示  	Warning();
						SwingUtilities.invokeLater(new Runnable() 
						{   
				            public void run() 
				            {   
				            	Warning();
				            }   
				        }); 
					}
					
				}
				
				//获取所有在线用户列表
				else if(type == 5)
				{
					System.out.println("现在在获取在线用户列表");
					int length = fromServer.readInt();
					vector.clear();
					
					if(length != 0)
					{
						String[] userList = new String[length];
						for(int i = 0;i < length;i++)
							userList[i] = fromServer.readUTF();
						userpanel.setVisible(true);
						for(int i = 0;i < length;i++)
							vector.add(userList[i]);
						userOnline.setListData(vector);
			                
					}
					else
						System.out.println("没有在线用户");
					//获取了在线用户列表后，显示在图形界面中
					
				}
				
				//client请求退出
				else if(type == 6);            							//Server更新数据库，Client不需要干什么
				
				//查询单词意思
				else if(type == 7)
				{
					int flag = fromServer.readInt();
					if(flag == 1)
					{
						for(int i = 0;i < 3;i++)
							threeTranslation[i] = fromServer.readUTF();
						for(int i = 0;i < 3;i++)
							threePraise[i] = fromServer.readInt();
					
						if(baiduChoose.isSelected())
						{
							baidu.setVisible(true);
							baiduInterpret.setText(threeTranslation[0]);
						}	
						if(youdaoChoose.isSelected())
						{
							youdao.setVisible(true);
							youdaoInterpret.setText(threeTranslation[1]);
						}	
						if(biyingChoose.isSelected())
						{
							biying.setVisible(true);
							biyingInterpret.setText(threeTranslation[2]);
						}	
						if((!baiduChoose.isSelected()) && (!youdaoChoose.isSelected()) && (!biyingChoose.isSelected()))
						{
							baidu.setVisible(true);
							youdao.setVisible(true);
							biying.setVisible(true);
							
							baiduInterpret.setText(threeTranslation[0]);
							youdaoInterpret.setText(threeTranslation[1]);
							biyingInterpret.setText(threeTranslation[2]);
						}
						int caseOfPraise = 0;
						caseOfPraise = sortPraise(threePraise);
						interpret.remove(baidu);
						interpret.remove(youdao);
						interpret.remove(biying);
						switch(caseOfPraise)
						{
							case 0:interpret.add(baidu);interpret.add(youdao);interpret.add(biying);break;// 顺序为 百度 有道 必应
							case 1:interpret.add(baidu);interpret.add(biying);interpret.add(youdao);break;// 顺序为 百度 必应 有道
							case 2:interpret.add(youdao);interpret.add(baidu);interpret.add(biying);break;// 顺序为 有道 百度 必应
							case 3:interpret.add(youdao);interpret.add(biying);interpret.add(baidu);break;// 顺序为 有道 必应 百度
							case 4:interpret.add(biying);interpret.add(baidu);interpret.add(youdao);break;// 顺序为 必应 百度 有道
							case 5:interpret.add(biying);interpret.add(youdao);interpret.add(baidu);break;// 顺序为 必应 有道 百度 
						}
						baiduPraise.setText("赞 "+threePraise[0]);
						youdaoPraise.setText("赞 "+threePraise[1]);
						biyingPraise.setText("赞 "+threePraise[2]);
						}
					
					else 
					{
						baidu.setVisible(true);
						youdao.setVisible(true);
						biying.setVisible(true);
						
						baiduInterpret.setText("没有找到释义");
						youdaoInterpret.setText("没有找到释义");
						biyingInterpret.setText("没有找到释义");
					}
				}
				else if(type == 8)
				{
					int length = fromServer.readInt();
					vector.clear();
					
					String[] userList = new String[length];
					for(int i = 0;i < length;i++)
					{
						userList[i] = fromServer.readUTF();
						System.out.println(userList[i]);
					}
					
					userpanel.setVisible(true);
					for(int i = 0;i < length;i++)
						vector.add(userList[i]);
					userOnline.setListData(vector);
				}
				else if(type == 9)              //指定的用户没有找到或者不在线
				{
					final String message = "指定的用户不在线或者不存在这样的用户！";
					SwingUtilities.invokeLater(new Runnable()
					{   
			            public void run() 
			            {   
			            	JOptionPane.showMessageDialog(null, message);
			            }   
			        }); 
				}
				
				else if(type == 10)             //欲登录的用户已登录状态，不能再登录
				{
					final String message = "该用户已登录，不能再登录！";
					SwingUtilities.invokeLater(new Runnable()
					{   
			            public void run() 
			            {   
			            	JOptionPane.showMessageDialog(null, message, "Warning!",JOptionPane.WARNING_MESSAGE);  
			            	Login();
			            }   
			        }); 
				}
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void sendCard(int choose)
	{
		try
		{
			int type = 1;
			sendUserName = this.userName;
			String receiveUserName;
			
			if(sendUserName != null)
			{
				if(choose == 0 || choose == 3)
				{
					receiveUserName = baiduUserId.getText();
					sendTranslation = baiduInterpret.getText();
				}
				else if(choose == 1 || choose == 4)
				{
					receiveUserName = youdaoUserId.getText();
					sendTranslation = youdaoInterpret.getText();
				}
				else
				{
					receiveUserName = biyingUserId.getText();
					sendTranslation = biyingInterpret.getText();
				}
				
				if(choose == 3 || choose == 4 || choose == 5)
				{
					toServer.writeInt(8);//群发
					toServer.writeUTF(sendUserName);
					toServer.writeUTF(sendTranslation);
				}
			
				else if(!receiveUserName.equals("") && !sendTranslation.equals(""))
				{
					toServer.writeInt(type);
					toServer.writeUTF(sendUserName);
					toServer.writeUTF(receiveUserName);
					toServer.writeUTF(sendTranslation);
				}
				else
				{
					final String message = "发送的消息不完善！";
					SwingUtilities.invokeLater(new Runnable()
					{   
						public void run() 
						{   
							JOptionPane.showMessageDialog(null, message);
						}   
					}); 
				}
			toServer.flush();
			}
			else 
			{
				final String message = "请您先登录！";
				SwingUtilities.invokeLater(new Runnable()
				{   
					public void run() 
					{   
						JOptionPane.showMessageDialog(null, message);
					}   
				}); 
			}
		}
		catch(IOException ex)
		{
			System.out.println(ex);
		}
	}
	
	int sortPraise(int[] a)
	{
		if(a[0]>=a[1]&&a[1]>=a[2])
			return 0;
		else if(a[0]>=a[2]&&a[2]>=a[1])
			return 1;
		else if(a[1]>=a[0]&&a[0]>=a[2])
			return 2;
		else if(a[1]>=a[2]&&a[2]>=a[0])
			return 3;
		else if(a[2]>=a[0]&&a[0]>=a[1])
			return 4;
		else
			return 5;
	}
	
	
	@SuppressWarnings("deprecation")
	public void Login()
	{
		JPanel jplabel,jpin,jpf;
		JLabel name,password;
		
		JTextField name_in;
		JPasswordField password_in;

		name = new JLabel("Name:");
		password = new JLabel("password:");
		jplabel = new JPanel(new GridLayout(2,1));
		jplabel.add(name);
		jplabel.add(password);
		
		name_in = new JTextField();
		password_in = new JPasswordField();
		jpin = new JPanel(new GridLayout(2,1));
		jpin.add(name_in);
		jpin.add(password_in);
		
		jpf = new JPanel(new BorderLayout());
		jpf.add(jplabel,BorderLayout.WEST);
		jpf.add(jpin,BorderLayout.CENTER);

		JOptionPane optionPane = new JOptionPane(jpf, 
				  JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog = optionPane.createDialog(this, "登录");
		dialog.setVisible(true);
		int value = (int)optionPane.getValue();
		
		if(value == optionPane.YES_OPTION)
		{
			try 
			{
				toServer.writeInt(4);
				toServer.writeUTF(name_in.getText());
				toServer.writeUTF(password_in.getText());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void Register()
	{
		JPanel jplabel,jpin,jpf;
	    JLabel name,password,password_confirm;
		JPasswordField password_in,confirm_in;
		JTextField name_in;

		name = new JLabel("Name:");
		password = new JLabel("password:");
		password_confirm = new JLabel("password confirm:");
		jplabel = new JPanel(new GridLayout(3,1));
		jplabel.add(name);
		jplabel.add(password);
		jplabel.add(password_confirm);
		
		name_in = new JTextField();
		password_in = new JPasswordField();
		confirm_in = new JPasswordField();
		jpin = new JPanel(new GridLayout(3,1));
		jpin.add(name_in);
		jpin.add(password_in);
		jpin.add(confirm_in);
		
		jpf = new JPanel(new BorderLayout());
		jpf.add(jplabel,BorderLayout.WEST);
		jpf.add(jpin,BorderLayout.CENTER);

		JOptionPane optionPane = new JOptionPane(jpf, 
				  JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog = optionPane.createDialog(this, "注册");
		dialog.setVisible(true);
		int value =  (int)optionPane.getValue();
		if(value == optionPane.YES_OPTION)
		{
			if(password_in.getText().equals(confirm_in.getText()))
			{
				try 
				{
					toServer.writeInt(3);//注册新号
					toServer.writeUTF(name_in.getText());
					toServer.writeUTF(password_in.getText());
					toServer.writeUTF("localhost");
					
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "两次密码不一致，重新输入！");
				Register();
			}
		}
	}
	
	void Warning()
	{
		JOptionPane.showMessageDialog(null, "Wrong name or password!\nLog in again!", "Warning!",JOptionPane.WARNING_MESSAGE);  
		Login();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ShowDictionary frame = new ShowDictionary();
		frame.setTitle("Dictionary");
		frame.setSize(900,700);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		frame.connect();
	}

}
