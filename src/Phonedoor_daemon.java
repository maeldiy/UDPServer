import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import sun.audio.*;


public class Phonedoor_daemon{
  Thread thread;
  DatagramSocket socket;

  public static void main(String[] args) {
	  Phonedoor_daemon u = new Phonedoor_daemon();
  }
  public Phonedoor_daemon(){
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
  //Receive request from client
				  DatagramPacket packet =  
						  new DatagramPacket(buffer, buffer.length );
				  socket.receive(packet);
				  InetAddress client = packet.getAddress();
				  int client_port = packet.getPort();
				  String donnees_recues = new String(buffer);
				  if (donnees_recues.contains("end_request_calls")) {
					  // Sending acknowledge
					  String Answer = new String("OKFROMTAB"); 
					  byte answer[] = Answer.getBytes();
					  packet = 
							  new DatagramPacket(answer, answer.length,client,client_port);
					  socket.send(packet);
					  // launch sound player
					  try {
						   // open the sound file as a Java input stream
						    String gongFile = "K:\\mp3\\70\\CUBA.mp3";
						    InputStream in = new FileInputStream(gongFile);

						    // create an audiostream from the inputstream
						    AudioStream audioStream = new AudioStream(in);

						    // play the audio clip with the audioplayer class
						    AudioPlayer.player.start(audioStream);
						    
						    
						 
						/*    
						   AudioInputStream stream;
						    AudioFormat format;
						    DataLine.Info info;
						    Clip clip;
						   
						    stream = AudioSystem.getAudioInputStream(yourFile);
						    System.err.println(stream );
						    format = stream.getFormat();
						    System.err.println(format );
						    info = new DataLine.Info(Clip.class, format);
						    
						    clip = (Clip) AudioSystem.getLine(info);
						    
						    clip.open(stream);
						    clip.start();
						*/
						}
						catch (Exception e) {
							System.err.println(e);
						}
					  
	  // Launching apps, i.e executing task ;-)
					  Runtime runtime = Runtime.getRuntime();
					  
	  // runtime.exec(new String[] { "wmplayer //play //close K:\\mp3\\doorbell.mp3" } );
	//  runtime.exec(new String[] { "C:\\Program Files\\Windows Media Player\\wmplayer K:\\mp3\\doorbell.mp3" } );
	  //runtime.exec(new String[] { "play K:\\mp3\\doorbell.mp3" } );
//					  runtime.exec(new String[] { "C:\\Users\\Loule\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe -profile-directory=\"Profile 1\""} ); // TO DO : define good user
					  String url = "http://www.centerkey.com/";
					  BrowserLauncher.openURL(url);
					  
					  //UDPClient answer = new UDPClient(String.valueOf(client),String.valueOf(client_port), "ok_received");
  	}
  
  
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