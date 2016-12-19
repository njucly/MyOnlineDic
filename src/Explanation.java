import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class Explanation {
	public static String Baidu_exp(String word){
	
		Scanner input = null;
		String explanation = "";
		try {
			URL url = new URL("http://www.iciba.com/" + word);
			input = new java.util.Scanner(url.openStream(),"UTF-8");
			boolean explanBeginFlag = false; //开始中文翻译的标志
			boolean pronBeginFlag = false; //音标字段开始的标志
			boolean tenseBeginFlag = false; //时态转换和词性转换字段开始的标志
			
			//获取音标
			while(input.hasNext()){
				String s = input.nextLine();
				if(s.indexOf("<div class=\"base-speak\">")>=0)
					pronBeginFlag=true;
				else if(!pronBeginFlag)
					continue;
				else if(s.indexOf("<div class=\"base-word\" >")>=0)
					break;
				else if(s.indexOf("<span>")>=0){
					int begin = s.indexOf("<span>") + new String("<span>").length();
					int end = s.indexOf("</span>");
					if(end<0)
						continue;
					String temp=s.substring(begin, end);
					if(temp.charAt(0)!=' ')
						explanation+="\n"+temp;
				}
			}
			
			//获取中文翻译
			while(input.hasNext()){
				String s = input.nextLine();
				if(s.indexOf("<li class=\"clearfix\">")>=0)
					explanBeginFlag=true;
				else if(!explanBeginFlag)
					continue;
				else if(s.indexOf("                                            </ul>")>=0)
					break;
				else if(s.indexOf("<span class=\"prop\">")>=0){
					int begin = s.indexOf("<span class=\"prop\">") + new String("<span class=\"prop\">").length();
					int end = s.indexOf("</span>");
					String temp=s.substring(begin, end);
					explanation+="\n"+temp;
				}
				else if(s.indexOf("<span>")>=0){
					int begin = s.indexOf("<span>") + new String("<span>").length();
					int end = s.indexOf("</span>");
					String temp=s.substring(begin, end);
					explanation+=temp;
				}
			}
			
			//获取词性和时态转换
			while(input.hasNext()){
				String s = input.nextLine();
				if(s.indexOf("<li class=\"change clearfix\">")>=0)
					tenseBeginFlag=true;
				else if(!tenseBeginFlag)
					continue;
				else if(s.indexOf("                </li>")>=0)
					break;
				else if(s.indexOf("<span>")>=0){
					int begin = s.indexOf("<span>") + new String("<span>").length();
					int end = s.length();
					String temp=s.substring(begin, end);
					explanation+="\n"+temp;
				}
				else if(s.indexOf(">")>=0){
					int begin = s.indexOf(">") + new String(">").length();
					int end = s.indexOf("</a>");
					if(end<0)
						continue;
					String temp=s.substring(begin, end);
					explanation+=temp;
				}
			}
			
			if(explanation!=null&&!explanation.equals(""))
				explanation=word+explanation;
			return explanation;
		} catch (MalformedURLException ex){
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	static String Youdao_exp(String word){
		
		java.util.Scanner input = null;
		String explanation = "";
		try {
			URL url = new URL("http://dict.youdao.com/search?le=eng&q=" + word + "&keyfrom=dict.top");
			input = new java.util.Scanner(url.openStream(),"UTF-8");
			boolean explanBeginFlag = false; //开始中文翻译的标志
			String pronType="<span class=\"pronounce\">";
			String pronBegin="<span class=\"phonetic\">";
			String pronEnd="</span>";
			//获取音标
			while(input.hasNext()){
				String s = input.nextLine();
				if(s.indexOf(pronType)>=0){
					int begin = s.indexOf(pronType) + pronType.length();
					int end = s.length();
					explanation += s.subSequence(begin, end);
				}
				else if(s.indexOf(pronBegin)>=0){
					int begin = s.indexOf(pronBegin) + pronBegin.length();
					int end = s.indexOf(pronEnd);
					explanation += s.subSequence(begin, end)+"\n";
				}
				else if(s.indexOf("<div class=\"trans-container\">")>=0){
					break;
				}
			}
			//获取中文翻译
			while(input.hasNext()){
				String s = input.nextLine();
				if(s.equals("   <ul>"))
					explanBeginFlag=true;
				else if(!explanBeginFlag)
					continue;
				else if(s.equals("    </ul>"))
					break;
				else{
					int begin = s.indexOf("<li>") + 4;
					int end = s.indexOf("</li>");
					explanation += s.subSequence(begin, end);
					explanation += "\n";
				}
			}
			if(explanation!=null&&!explanation.equals(""))
				explanation=word+"\n"+explanation;
			return explanation;
		} catch (MalformedURLException ex){
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static String Biying_exp(String word){
		
		java.util.Scanner input = null;
		String explanation = null;
		try {
			URL url = new URL("http://cn.bing.com/dict/search?q=" +word);
			input = new java.util.Scanner(url.openStream(),"UTF-8");
			String start="必应词典为您提供"+word+"的释义，";
			while(input.hasNext()){
				String s = input.nextLine();
				int index=s.indexOf(start);
				if(index>=0){
					String temp=s.substring(index+start.length(),s.length());
					int end=temp.indexOf("\"");
					if(end>=0){
						String content=temp.substring(0,end);
						explanation=content;
						break;
					}
				}	
			}
			if(explanation!=null&&!explanation.equals(""))
				explanation=word+"\n"+explanation;
			return explanation;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){}
		
		return null;
	}
}
