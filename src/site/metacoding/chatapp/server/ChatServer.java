package site.metacoding.chatapp.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ChatServer {

	ServerSocket serverSocket;
	List<클라이언트소켓> 클라이언트소켓리스트;

	public ChatServer() {
		try {
			serverSocket = new ServerSocket(2000);
			클라이언트소켓리스트 = new Vector<>(); // 동기화가 처리된 ArrayList
			while (true) {
				Socket socket = serverSocket.accept(); // main 스레드
				System.out.println("클라이언트 연결됨");
				클라이언트소켓 t = new 클라이언트소켓(socket);
				클라이언트소켓리스트.add(t);
				System.out.println("클라이언트소켓리스트 크기 : " + 클라이언트소켓리스트.size());
				new Thread(t).start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 내부 클래스
	class 클라이언트소켓 implements Runnable {

		Socket socket;
		BufferedReader reader;
		BufferedWriter writer;
		boolean isLogin = true;

		public 클라이언트소켓(Socket socket) {
			this.socket = socket;

			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void run() {
			while (isLogin) {
				try {
					String inputData = reader.readLine();

					for (클라이언트소켓 t : 클라이언트소켓리스트) {
						if (t != this) {
							t.writer.write(inputData + "\n");
							t.writer.flush();
						}
					}
				} catch (Exception e) {
					try {
						System.out.println("통신 실패 : " + e.getMessage());
						isLogin = false;
						클라이언트소켓리스트.remove(this);
						reader.close();
						writer.close();
						socket.close();
					} catch (Exception e1) {
						System.out.println("연결해제 프로세스 실패 " + e1.getMessage());
					}
				}

			}
		}

	}

	// 192.168.0.132
	public static void main(String[] args) {
		new ChatServer();
	}
}
