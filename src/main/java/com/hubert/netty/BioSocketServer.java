package com.hubert.netty;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author hustart1@126.com
 * @version 1.0.0
 * @ClassName BioSocketServer
 * @Description
 * @createTime 2023年04月03日 22:32:00
 */
public class BioSocketServer {

	public void server(int port) throws Exception {
		ServerSocket serverSocket = new ServerSocket(port);
		while (true) {
			Socket accept = null;
			try {
				accept = serverSocket.accept();

				OutputStream outputStream = accept.getOutputStream();
				outputStream.write("Hi".getBytes(Charset.forName("UTF-8")));
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					accept.close();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
