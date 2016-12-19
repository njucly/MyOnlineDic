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
	private JButton baiduSendAll = new JButton();//Ⱥ����ť
	private JButton youdaoSendAll = new JButton();
	private JButton biyingSendAll = new JButton();
	private JPanel registerAndLogin = new JPanel();
	private JPanel baidu = new JPanel();
	private JPanel youdao = new JPanel();
	private JPanel biying = new JPanel();
	private JPanel interpret = new JPanel();//��ʾ������վ�ķ���,����Ϊ���ԣ��Ա�������ݵ��������������������˳��
	private JPanel panel = new JPanel();//�������齨����panel��
	private Vector<String> vector = new Vector<String>();
	private JPanel panelUp = new JPanel();//��ʾ��¼��ע�ᡢ����������ѡ��
	private JPanel panelOfChoose = new JPanel();//��ʾ����ѡ��
	private JPanel baiduSendWord = new JPanel();//��ʾ�ٶ��û�id��ͷ��Ͱ�ť�͵��ް�ť
	private JPanel youdaoSendWord = new JPanel();//��ʾ�е��û�id��ͷ��Ͱ�ť�͵��ް�ť
	private JPanel biyingSendWord = new JPanel();//��ʾ��Ӧ�û�id��ͷ��Ͱ�ť�͵��ް�ť
	private JPanel userpanel = new JPanel();//��ʾ�����û��б�
	private JTextField userText = new JTextField();//�û���
	private JButton logoff = new JButton();//�˳���¼��ť
	private static DataOutputStream toServer;
	private static DataInputStream fromServer;
	
	JLabel lab;
	boolean skinIsPicture = false;
	
	//����
	private String[] threeTranslation;
	private int[] threePraise;
	
	private String sendUserName;
	private String sendTranslation;
	
	private String userName;                      //��ǰ��¼�û�������
	
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
		registerAndLogin.setLayout(new FlowLayout(FlowLayout.RIGHT));							//��ʾ��½��������ע�ᰴť
		register.setText("ע��");
		login.setText("��¼");
		logoff.setText("�˳�");
		userText.setEditable(false);
		userText.setText("�ο�");
		
		registerAndLogin.add(login);
		registerAndLogin.add(register);
		registerAndLogin.add(logoff);
		registerAndLogin.add(userText);
		logoff.setVisible(false);
		panelOfChoose.setLayout(new FlowLayout());
		baiduChoose.setText("�ٶ�");
		youdaoChoose.setText("�е�");
		biyingChoose.setText("��Ӧ");
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
		baiduSendCard.setText("���͵��ʿ�");
		baiduPraise.setText("�� ");
		baiduSendWord.add(new JLabel("�û�ID��"));
		baiduSendAll.setText("Ⱥ��");
		baiduSendWord.add(baiduUserId);
		baiduSendWord.add(baiduSendCard);
		baiduSendWord.add(baiduSendAll);
		baiduSendWord.add(baiduPraise);
		baidu.setLayout(new BorderLayout(10,10));
		baidu.add(new JLabel("�ٶ�"),BorderLayout.WEST);
		baiduInterpret.setEditable(false);
		baiduUserId.setColumns(15);
		baidu.add(new JScrollPane(baiduInterpret),BorderLayout.CENTER);
		baidu.add(baiduSendWord,BorderLayout.SOUTH);
		youdaoSendWord.setLayout(new FlowLayout(FlowLayout.RIGHT));
		youdaoSendCard.setText("���͵��ʿ�");
		youdaoPraise.setText("��");
		youdaoSendWord.add(new JLabel("�û�ID��"));
		youdaoSendAll.setText("Ⱥ��");
		youdaoSendWord.add(youdaoUserId);
		youdaoSendWord.add(youdaoSendCard);
		youdaoSendWord.add(youdaoSendAll);
		youdaoSendWord.add(youdaoPraise);
		youdao.setLayout(new BorderLayout(10,10));
		youdao.add(new JLabel("�е�"),BorderLayout.WEST);
		youdaoInterpret.setEditable(false);
		youdaoUserId.setColumns(15);
		youdao.add(new JScrollPane(youdaoInterpret),BorderLayout.CENTER);
		youdao.add(youdaoSendWord,BorderLayout.SOUTH);
		biyingSendWord.setLayout(new FlowLayout(FlowLayout.RIGHT));
		biyingSendCard.setText("���͵��ʿ�");
		biyingPraise.setText("��");
		biyingSendWord.add(new JLabel("�û�ID��"));
		biyingSendAll.setText("Ⱥ��");
		biyingSendWord.add(biyingUserId);
		biyingSendWord.add(biyingSendCard);
		biyingSendWord.add(biyingSendAll);
		biyingSendWord.add(biyingPraise);
		biying.setLayout(new BorderLayout(10,10));
		biying.add(new JLabel("��Ӧ"),BorderLayout.WEST);
		biyingInterpret.setEditable(false);
		biyingUserId.setColumns(15);
		biying.add(new JScrollPane(biyingInterpret),BorderLayout.CENTER);
		biying.add(biyingSendWord,BorderLayout.SOUTH);
		interpret.add(baidu);
		interpret.add(youdao);
		interpret.add(biying);
		
		panel.add(panelUp,BorderLayout.NORTH);
		userpanel.setLayout(new BorderLayout(5,5));
		userpanel.add(new JLabel("�����û��б�"),BorderLayout.NORTH);
		userpanel.add(userOnline,BorderLayout.CENTER);
		panel.add(userpanel,BorderLayout.WEST);
		panel.add(interpret,BorderLayout.CENTER);
		add(panel);
		
		userpanel.setVisible(false);
		baidu.setVisible(false);
		youdao.setVisible(false);
		biying.setVisible(false);
		
		
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
				type = fromServer.readInt();                        				//��ȡServer���������ݰ�����
				
				//���͵��ʿ�
				if(type == 1)
				{
					
				}
					
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
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
