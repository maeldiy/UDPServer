import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JOptionPane;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class Phonedoor_daemon{
  Thread thread;
  DatagramSocket socket;
  List<String> lignes = null; // init of List<String> for file parameter reading
  int OS = 0;
  byte[] buffer = new byte[1024];
  int port = 5650;
  String audiofilepath = null;
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
	  try{
		  socket = new DatagramSocket(port);
// read all parameter once from file		  
		// Determining OS
		  OS = PlatformDetector.detect();
//		  try {
			  //for test : read parameters from text file 
			  if (OS == PlatformDetector.WINDOWS){
                  lignes = Files.readAllLines(FileSystems.getDefault().getPath("C:\\Phone_door\\config.txt"), StandardCharsets.ISO_8859_1);  //iso 8859 for windows , utf-8 for linux
			      audiofilepath = lignes.get(1); //"K:\\mp3\\70\\CUBA.mp3";
			      System.out.println(audiofilepath);
			  
					}
			  else {
				  lignes = Files.readAllLines(FileSystems.getDefault().getPath("/etc/config.txt"), StandardCharsets.UTF_8);  //iso 8859 for windows , utf-8 for linux
				  audiofilepath = lignes.get(3); //"K:\\mp3\\70\\CUBA.mp3";
			      System.out.println(audiofilepath);
			  }
 	  }

		  catch (Exception e) {
					JOptionPane.showMessageDialog(null,"error, perhaps  could not find file settings" + 
			             ":\n" + e.getLocalizedMessage());
				System.err.println(e);
			}
	  
		  //waiting for call request	  
		  while(true){
			  try{
  //Receive request from client
				  DatagramPacket packet =  
						  new DatagramPacket(buffer, buffer.length );
				  socket.receive(packet);
				  InetAddress client = packet.getAddress();
				  int client_port = packet.getPort();
				  String donnees_recues = new String(buffer);
				  // GUI launch alone for settings
				  if (donnees_recues.contains("GUI")) {
					  PhoneDoor_GUI a = new PhoneDoor_GUI(lignes);	
				  }
				  // Launch app
				  if (donnees_recues.contains("end_request_calls")) {
					  // Sending acknowledge
					  String Answer = new String("OKFROMTAB"); 
					  byte answer[] = Answer.getBytes();
					  packet = 
							  new DatagramPacket(answer, answer.length,client,client_port);
					  socket.send(packet);
					  //  Launching GUI 
					  PhoneDoor_GUI a = new PhoneDoor_GUI(lignes);						    
					  //System.exit(0)
					    // launch sound player					  	  
					  						  // open the sound file as a Java input stream
						    InputStream in = new FileInputStream(audiofilepath);
						    // create an audiostream from the inputstream
						    AudioStream audioStream = new AudioStream(in);
						    // play the audio clip with the audioplayer class
						    AudioPlayer.player.start(audioStream);	
					  // Launching viewer
						    if (OS == PlatformDetector.WINDOWS){
				            Runtime runtime = Runtime.getRuntime();
						     runtime.exec(new String[] { "C:\\Users\\Loule\\AppData\\Local\\VirtualStore\\Program Files\\Xeoma\\xeoma.exe"} ); 
						    //entering sleep mode command, no need for windows device ; if the system runs it's because someone is on						    
				//		    Runtime.getRuntime().exec("rundll32.exe powrprof.dll,SetSuspendState 0,1,0"); 
						   	  }
							  else { Runtime runtime = Runtime.getRuntime();
							  	runtime.exec(new String[] { "vlc -vvv http://192.168.0.10/cgi/mjpg/mjpeg.cgi?.mjpeg &"});
							   // runtime.exec(new String[] { "motion -c motion.conf &"} ); // could use zoneminder alternitavely ?
							    runtime.exec(new String[] { "xfcse-screensaver &" }); // see http://ubuntuforums.org/showthread.php?t=1810262 for scrrensaver enable/disabele
							//    sudo shutdown -s now
							  }

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
  
  catch (IOException e){
	  JOptionPane.showMessageDialog(null,"error aaaa" + 
	             ":\n" + e.getLocalizedMessage());
  System.err.println(e);
  }
  }
}
}  
