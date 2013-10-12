import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JOptionPane;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


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
		  // Determining OS Launching GUI 		
				//	  Runtime runtime = Runtime.getRuntime();
					  int OS = PlatformDetector.detect();
					    try {
					        if (OS == PlatformDetector.WINDOWS)
					        	Runtime.getRuntime().exec(new String[] {"java -jar GUI.jar"});
					        else { // assume Unix or Linux
					        Runtime.getRuntime().exec(new String[] { "wmplayer //play //close K:\\mp3\\doorbell.mp3" } );
					        }
					      } catch (Exception e) {
					        JOptionPane.showMessageDialog(null,
					             ":\n" + e.getLocalizedMessage());
					      }		
					    // launch sound player
					  try {
						  //for test : read parameters from text file 
			        		final List<String> lignes = Files.readAllLines(FileSystems.getDefault().getPath("C:\\Phone_door\\config.txt"), StandardCharsets.ISO_8859_1);  //iso 8859 for windows , utf-8 for linux
			        		String audiofilepath = lignes.get(1);
			        		System.out.println("bla bla" + audiofilepath);
						  
						  // open the sound file as a Java input stream
						   	String gongFile = audiofilepath;//"K:\\mp3\\70\\CUBA.mp3";
						   	System.out.println(gongFile);
						    InputStream in = new FileInputStream(gongFile);
						    // create an audiostream from the inputstream
						    AudioStream audioStream = new AudioStream(in);
						    // play the audio clip with the audioplayer class
						    AudioPlayer.player.start(audioStream);
						}
						catch (Exception e) {
								JOptionPane.showMessageDialog(null,"error" + 
						             ":\n" + e.getLocalizedMessage());
							System.err.println(e);
						}	
					  // Launching browser
					  String url = "http://www.wikipedia.com/";
					  BrowserLauncher.openURL(url);

  	}
  }
  catch(UnknownHostException ue){					
		JOptionPane.showMessageDialog(null,"error eerh" + 
          ":\n" + ue.getLocalizedMessage());}
  }
  }
  catch(java.net.BindException b){
		JOptionPane.showMessageDialog(null,"error eeeee" + 
	             ":\n" + b.getLocalizedMessage());
  }
  }
  catch (IOException e){
	  JOptionPane.showMessageDialog(null,"error aaaa" + 
	             ":\n" + e.getLocalizedMessage());
  System.err.println(e);
  }
  }
  }
}