package y.q.Transfer.Services.send;

import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Reciver.SendStateChangedReciver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by CFun on 2015/4/22.
 */
public class SendTask extends Thread
{
	private boolean stopFlag = false;
	private Socket socket;
	private InetAddress address;
	private int port;
	private List<SendFileInfo> tasks;

	public SendTask(InetAddress address, int port, List<SendFileInfo> tasks)
	{
		this.address = address;
		this.port = port;
		this.tasks = tasks;
	}

	@Override
	public void run()
	{
		try
		{
			stopFlag = false;
			for (int i = 0; i < tasks.size() && !stopFlag; i++)
			{
				try
				{
					socket = new Socket();
					SocketAddress socketAddress = new InetSocketAddress(address, port);
					socket.connect(socketAddress, 30000);
					SendFileInfo task = tasks.get(i);
					BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
					BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
					if(!isOK(in))
						throw new IOException("Not Ready");
					SendAction.doSend(out, task);
					out.flush();
					out.close();
					in.close();
					socket.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			socket = new Socket(address, port);
			BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
			AllTaskFinishAction.doSend(outputStream);
		} catch (Exception e)
		{
			e.printStackTrace();
			BaseApplication.showToast(e.getMessage());
		}
	}
	public boolean isOK(BufferedInputStream in) throws IOException
	{
		byte[] buffer = new byte[512];
		int len =  in.read(buffer);
		if(len == -1)
			return false;
		String ok = new String(buffer, 0, len, Charset.forName("UTF-8"));
		if("READY".equals(ok))
			return true;
		return false;
	}
	public void stopTran()
	{
		stopFlag = true;
		if (socket != null && !socket.isClosed())
			try
			{
				socket.close();
			} catch (IOException e)
			{
			}
	}
}
