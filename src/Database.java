import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	Connection connection;
	Statement statement;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	
	Explanation explanation = new Explanation();
	
	public Database() 
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");                         		//加载驱动程序
			System.out.println("load driver success");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost/myonlinedic","root","123456");
			System.out.println("connection success");
			
			statement = connection.createStatement();
			
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}                                   
		
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public int findPort(String curName)
	{
		int port = -1;                        //若用户不在线，则返回-1
		if(curName == null)
			return -1;
		try
		{
			preparedStatement = connection.prepareStatement("select port from user where name = ? and isLogin = 1");
			preparedStatement.setString(1,curName);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next())
				port = resultSet.getInt(1);                                 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return port;
	}
	
	public int isLoginSuccess(String curName,String curPassword)                                  //判断是否登录成功
	{
		int loginSuccess = 0; //代表登录失败
		try
		{
			preparedStatement = connection.prepareStatement("select * from user where name = ? and password = ?");
			preparedStatement.setString(1,curName);
			preparedStatement.setString(2,curPassword);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				if(resultSet.getInt(5) == 0)
					loginSuccess = 1; //成功登录
				else
					loginSuccess = 2;//已被登录
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return loginSuccess;
	}
	
	public boolean isRegisterSuccess(String curName)
	{
		boolean isSuccess = true;
		try
		{
			preparedStatement = connection.prepareStatement("select * from user where name = ? ");
			preparedStatement.setString(1,curName);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next())
				isSuccess = false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	
	public void insertRegister(String curName,String curPassword,String curIP_address,int curPort)       //注册时向数据库插入一条信息
	{
		try
		{
			preparedStatement = connection.prepareStatement("insert into user values(?,?,?,?,?)");
			preparedStatement.setString(1,curName);
			preparedStatement.setString(2,curPassword);
			preparedStatement.setString(3,curIP_address);
			preparedStatement.setInt(4,curPort);
			preparedStatement.setInt(5,1);                                                              //是登录状态
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{                  
			e.printStackTrace();
		}
	}
	
	public void changeToLoginState(String curName)
	{
		try
		{
			preparedStatement = connection.prepareStatement("update user set isLogin = 1 where name = ? ");
			preparedStatement.setString(1,curName);
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void changeToExitState(String curName)
	{
		try
		{
			preparedStatement = connection.prepareStatement("update user set isLogin = 0 where name = ? ");
			preparedStatement.setString(1,curName);
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void changePort(int curPort,String curName)
	{
		try
		{
			preparedStatement = connection.prepareStatement("update user set port = ? where name = ? ");
			preparedStatement.setInt(1,curPort);
			preparedStatement.setString(2,curName);
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public int countOnlineUser()
	{
		int number = 0;
		try
		{
			statement =connection.createStatement();
			resultSet = statement.executeQuery("select count(*) from user where isLogin = 1");
			if(resultSet.next())
				number = resultSet.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return number;
	}
	
	public int[] getPorts()
	{
		int length = countOnlineUser();
		
		int[] loginPort = new int[length];
		try
		{			
			statement =connection.createStatement();
			resultSet = statement.executeQuery("select port from user where isLogin = 1");
			int index =0;
			while(resultSet.next())
			{
				loginPort[index] = resultSet.getInt(1);
				index++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return loginPort;
	}
	
	public int getPort(String curname)
	{
		int port = 0;
		try
		{	
			preparedStatement = connection.prepareStatement("select port from user where name = ?");
			preparedStatement.setString(1,curname);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next())
			{
				port = resultSet.getInt(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return port;
	}
	
	public String[] getUserList()
	{
		String[] userList = null;
		try
		{
			statement =connection.createStatement();
			resultSet = statement.executeQuery("select name from user where isLogin = 1");
			int length = 0;
			while(resultSet.next())
				length++;
			if(length == 0)
				return null;
			
			userList = new String[length];
			for(int i = 0;i < length;i++)
				userList[i] = "";
			
			
			resultSet.first();
			for(int index = 0;index < length;index++)
			{
				userList[index] = resultSet.getString(1);
				resultSet.next();
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return userList;
	 }
	
	/*翻译和点赞*/
	public int[] getThreePraise(String curWord)                                	   //返回指定单词在百度、有道、必应的点赞次数
	{
		int[] resultArr = new int[3];
		try 
		{
			preparedStatement = connection.prepareStatement("select baidu_praise,youdao_praise,biying_praise from words where word = ?");
			preparedStatement.setString(1,curWord);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next())
			{
			    resultArr[0] = resultSet.getInt(1);
			    resultArr[1] = resultSet.getInt(2);
			    resultArr[2] = resultSet.getInt(3);
			}	
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultArr;
	}
	
	public void updateBaiduPraise(String curWord)
	{
		try
		{
			preparedStatement = connection.prepareStatement("update words set baidu_praise = baidu_praise + 1 where word = ?");
			preparedStatement.setString(1,curWord);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateYoudaoPraise(String curWord)
	{
		try
		{
			preparedStatement = connection.prepareStatement("update words set youdao_praise = youdao_praise + 1 where word = ?");
			preparedStatement.setString(1,curWord);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateBiyingPraise(String curWord)
	{
		try
		{
			preparedStatement = connection.prepareStatement("update words set biying_praise = biying_praise + 1 where word = ?");
			preparedStatement.setString(1,curWord);
			
			preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String[] getThreeTranslation(String curWord)
	{
		String[] threeTranslation = new String[3];
		for(int i = 0;i < 3;i++)
			threeTranslation[i] = "";
		try 
		{
			preparedStatement = connection.prepareStatement("select baidu_exp,youdao_exp,biying_exp from words where word = ?");
			preparedStatement.setString(1,curWord);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next())
			{
				threeTranslation[0] = resultSet.getString(1);
				threeTranslation[1] = resultSet.getString(2);
				threeTranslation[2] = resultSet.getString(3);
			}
			else                                          					//未在当地数据库中找到，爬网！更新数据库
			{
				threeTranslation[0] = explanation.Baidu_exp(curWord);
				threeTranslation[1] = explanation.Youdao_exp(curWord);
				threeTranslation[2] = explanation.Biying_exp(curWord);
				
				if(threeTranslation[0] != null)
				{
					preparedStatement = connection.prepareStatement("insert into words values(?,?,?,?,?,?,?)");
					preparedStatement.setString(1,curWord);
					preparedStatement.setString(2,threeTranslation[0]);
					preparedStatement.setInt(3,0);
					preparedStatement.setString(4,threeTranslation[1]);
					preparedStatement.setInt(5,0);                                                              
					preparedStatement.setString(6,threeTranslation[2]);
					preparedStatement.setInt(7,0);    
					preparedStatement.executeUpdate();
				}
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return threeTranslation;
	}
	
}
