package y.q.wifisend.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by Cfun on 2015/4/27.
 */
public class InputStreamUtil
{
	public static String readLine(BufferedInputStream inputStream) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int ch = 'a';
		while(ch != '\n')
		{
			ch= inputStream.read();
			if(ch >-1)
				sb.append(ch);
			else
				break;
		}
		return sb.toString();
	}
	public static String readLineWithoutEnd(BufferedInputStream inputStream) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int ch =  inputStream.read();
		while(ch != -1)
		{
			if(ch != '\n')
				sb.append((char)ch);
			else
				break;
		}
		if(sb.charAt(sb.length()-1) == '\r')
			sb.setLength(sb.length()-1);
		return sb.toString();
	}
}
