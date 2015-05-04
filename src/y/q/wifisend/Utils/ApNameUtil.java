package y.q.wifisend.Utils;

import y.q.wifisend.Entry.ApNameInfo;

import java.io.UnsupportedEncodingException;

/**
 * Created by Cfun on 2015/5/1.
 */
public class ApNameUtil
{
	public static ApNameInfo decodeApName(String apName)
	{
		int index1 = apName.indexOf('-');
		if(index1 < 0) return null;
		String str1 = apName.substring(0, index1);
		String str2 = apName.substring(index1 + 1);
		if(str1.length() != 4)
			return null;
		if(!jiaoyan(str2).equals(str1.substring(0,2)))
			return null;

		String str3 = null;
		try
		{
			str3 = new String(HexByteConvert.hexStringToBytes(str2), "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			return null;
		}
		ApNameInfo info = new ApNameInfo();
		info.setName(str3);
		info.setPhotoId((int)HexByteConvert.hexStringToBytes(str1.substring(2, 4))[0]);
		return info;
	}
	public static String encodeApName(ApNameInfo info)
	{
		String str = null;
		try
		{
			str = HexByteConvert.bytesToHexString(info.getName().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e)
		{
		}
//		return str+"-"+jiaoyan(str)+(info.isIP43()?1:0)+info.getPhotoId();
		return jiaoyan(str)+HexByteConvert.byte2Hex((byte)info.getPhotoId())+"-"+str;
	}

	protected static String jiaoyan (String str)
	{
		byte[] bytes = null;
		try
		{
			bytes =str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e)
		{

		}
		byte b = (byte) 255;
		for(int i =0; i<bytes.length; i++)
		{
			b = (byte) (b^bytes[i]);
		}
		return HexByteConvert.byte2Hex(b);
	}

	public static void main(String[] argv)
	{
		String str = "hello我们是好孩子";
		ApNameInfo info = new ApNameInfo();
		info.setName(str);
		info.setPhotoId(1);
		System.out.println(encodeApName(info));
		info = decodeApName(encodeApName(info));
		String aaa= "bbb";
	}
}
