package site.metacoding.chatapp.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatClientProgram extends JFrame {

	// GUI ����
	JPanel northPanel, southPanel;
	TextArea chatList;
	ScrollPane scroll;
	JTextField txtHost, txtPort, txtMsg;
	JButton btnConnect, btnSend;

	// ��� ����
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;

	public ChatClientProgram() {
		initUI();
		initListener();

		setVisible(true);
	}

	private void initUI() {
		// 1. ��� ������
		northPanel = new JPanel();
		txtHost = new JTextField(20); // ������ 20
		txtHost.setText("127.0.0.1");
		txtPort = new JTextField(5);
		txtPort.setText("2000");
		btnConnect = new JButton("Connect");

		northPanel.add(txtHost);
		northPanel.add(txtPort);
		northPanel.add(btnConnect);

		// 2. ���� ������
		chatList = new TextArea(10, 30);
		chatList.setBackground(Color.ORANGE);
		chatList.setForeground(Color.BLUE);
		scroll = new ScrollPane();
		scroll.add(chatList);

		// 3. �ϴ� ������
		southPanel = new JPanel();
		txtMsg = new JTextField(25); // ������ 20
		btnSend = new JButton("Send");
		southPanel.add(txtMsg);
		southPanel.add(btnSend);

		// 4. ������ ����
		setTitle("MyChat1.0");
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(northPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	private void initListener() {
		btnConnect.addActionListener((e) -> {
			connect(); // ���� ����
		});

		btnSend.addActionListener((e) -> {
			send(); // �޽��� ����
		});
	}

	private void connect() {
		try {
			// 1. host�ּҿ� port ��������
			String host = txtHost.getText();
			int port = Integer.parseInt(txtPort.getText());

			// 2. ���� �����ϰ� ���۴ޱ�
			socket = new Socket(host, port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 3. �б� ���� ������ �����
			new Thread(() -> {
				read();
			}).start();

			System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �޽��� ���� : ���ν�����
	private void send() {
		try {
			String msg = txtMsg.getText();
			chatList.append(msg + "\n");
			txtMsg.setText(""); // ����
			txtMsg.requestFocus(); // Ŀ�� �α�

			writer.write(msg + "\n");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �޽��� �б� : ���ο� ������
	private void read() {
		try {
			while (true) {
				String msg = reader.readLine();
				chatList.append("[����] " + msg + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ChatClientProgram();
	}
}
