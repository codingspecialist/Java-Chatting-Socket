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
	List<Ŭ���̾�Ʈ����> Ŭ���̾�Ʈ���ϸ���Ʈ;

	public ChatServer() {
		try {
			serverSocket = new ServerSocket(2000);
			Ŭ���̾�Ʈ���ϸ���Ʈ = new Vector<>(); // ����ȭ�� ó���� ArrayList
			while (true) {
				Socket socket = serverSocket.accept(); // main ������
				System.out.println("Ŭ���̾�Ʈ �����");
				Ŭ���̾�Ʈ���� t = new Ŭ���̾�Ʈ����(socket);
				Ŭ���̾�Ʈ���ϸ���Ʈ.add(t);
				System.out.println("Ŭ���̾�Ʈ���ϸ���Ʈ ũ�� : " + Ŭ���̾�Ʈ���ϸ���Ʈ.size());
				new Thread(t).start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ���� Ŭ����
	class Ŭ���̾�Ʈ���� implements Runnable {

		Socket socket;
		BufferedReader reader;
		BufferedWriter writer;
		boolean isLogin = true;

		public Ŭ���̾�Ʈ����(Socket socket) {
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

					for (Ŭ���̾�Ʈ���� t : Ŭ���̾�Ʈ���ϸ���Ʈ) {
						if (t != this) {
							t.writer.write(inputData + "\n");
							t.writer.flush();
						}
					}
				} catch (Exception e) {
					try {
						System.out.println("��� ���� : " + e.getMessage());
						isLogin = false;
						Ŭ���̾�Ʈ���ϸ���Ʈ.remove(this);
						reader.close();
						writer.close();
						socket.close();
					} catch (Exception e1) {
						System.out.println("�������� ���μ��� ���� " + e1.getMessage());
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
