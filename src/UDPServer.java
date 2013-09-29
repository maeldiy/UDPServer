import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class UDPServer{
  JFrame frame;
  JPanel panel;
  JButton button1,button2;
  JTextArea area;
  JScrollPane pane;
  Thread thread;
  DatagramSocket socket;

  public static void main(String[] args) {
  UDPServer u = new UDPServer();
  }
  public UDPServer(){
	  
  frame = new JFrame("Text Server");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setUndecorated(true);
  frame.getRootPane()
  .setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
  panel = new JPanel();
  panel.setLayout(null);
  area = new JTextArea();
  area.setEditable(false);
  button1 = new JButton("Start");
  button1.setBounds(210, 10, 75, 40);
  button1.addActionListener(new ActionListener(){
  public void actionPerformed(ActionEvent ae){
  
  }
  });
  panel.add(button1);
  button2 = new JButton("Stop");
  button2.setBounds(300, 10, 75, 40);
  button2.addActionListener(new ActionListener(){
  public void actionPerformed (ActionEvent ae){
 // thread.interrupted();
 // socket.close();
  area.append("Server is stopped\n");
  button1.setEnabled(true);
  button2.setEnabled(true);
  }
  });
  button2.setEnabled(true);
  panel.add(button2);
  pane = new JScrollPane(area);
  pane.setBounds(10, 60, 365, 250);
  panel.add(pane);
  frame.add(panel);
  frame.setSize(400, 400);
  frame.setVisible(true);
  new StartThread();
  }
  public class StartThread implements Runnable{
  StartThread(){
  thread = new Thread(this);
  thread.start();

  }
  public void run(){
  try{
  byte[] buffer = new byte[1024];
  int port = 5650;
  try{
  socket = new DatagramSocket(port);
  while(true){
  try{
  area.append("Server is started\n");
  //Receive request from client
  DatagramPacket packet =  
  new DatagramPacket(buffer, buffer.length );
  socket.receive(packet);
  InetAddress client = packet.getAddress();
  int client_port = packet.getPort();
  area.append(" Received "
  +new String(buffer)+" from "+client);
  String Answer = new String("ok_received"); 
  byte answer[] = Answer.getBytes();
  packet = 
  new DatagramPacket(answer, answer.length,client,client_port);
  socket.send(packet);
  //UDPClient answer = new UDPClient(String.valueOf(client),String.valueOf(client_port), "ok_received");
  
  }
  catch(UnknownHostException ue){}
  }
  }
  catch(java.net.BindException b){}
  }
  catch (IOException e){
  System.err.println(e);
  }
  }
  }
}