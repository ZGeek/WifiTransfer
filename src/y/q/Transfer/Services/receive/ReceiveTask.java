package y.q.Transfer.Services.receive;

import y.q.Transfer.Config;
import y.q.Transfer.Services.Tran.TranTool;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.Reciver.RecvingPrepareStateChangReciver;
import y.q.wifisend.Reciver.SendStateChangedReciver;
import y.q.wifisend.Utils.InputStreamUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by CFun on 2015/4/22.
 */
public class ReceiveTask extends Thread
{
	private boolean stopFlag = false;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	int port;
	private static int a = 0;
	private static int b = 0;

	public ReceiveTask(int port)
	{
		this.port = port;
	}

	@Override
	public void run()
	{
		boolean isFirstTask = true;
		try
		{
			stopFlag = false;
			try
			{
//				BaseApplication.showToast("监听端口" + (a++));
				serverSocket = new ServerSocket(port);
			} catch (IOException e)
			{
				RecvingPrepareStateChangReciver.sendBroadcast(RecvingPrepareStateChangReciver.STATE_ERROR);
				e.printStackTrace();
				return;
			}
			RecvingPrepareStateChangReciver.sendBroadcast(RecvingPrepareStateChangReciver.STATE_FINISH);

			while (!stopFlag)
			{
				clientSocket = serverSocket.accept();
				if (isFirstTask)
				{
					isFirstTask = false;
					SendStateChangedReciver.sendAllTasksStartBroadcast();
				}
				BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
				sendyReady(out);
				BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
				String line = InputStreamUtil.readLineWithoutEnd(in);
				String cmd = TranTool.analysisFirstLine(line);
				/** 在此处可以对cmd进行自定义的拦截操作*/
				if (cmd.equals(Config.AllFinishFlag))
				{
					//如果是所有任务都完成了，则发送所有任务已完成的广播
					SendStateChangedReciver.sendStatuChangedBroadcast(null, SendStatus.AllFinish, 1);
					try
					{
						clientSocket.close();
					}catch (IOException e){e.printStackTrace();}
					continue;
				}
				if (cmd.equals("send") || cmd.equals("send-part"))
				{
					ReceiveAction.doSave(true, cmd, in, clientSocket);
					continue;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			BaseApplication.showToast(e.getMessage());
		}finally
		{
			try
			{
				serverSocket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void sendyReady(BufferedOutputStream out) throws IOException
	{
		out.write("READY".getBytes());
		out.flush();

	}

	public void stopTran()
	{
		stopFlag = true;
		if (serverSocket != null && !serverSocket.isClosed())
			try
			{
				serverSocket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		if (clientSocket != null && !clientSocket.isClosed())
			try
			{
				clientSocket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
	}
}
