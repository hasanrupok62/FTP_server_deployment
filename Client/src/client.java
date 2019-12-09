import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class client implements ActionListener,Runnable
{
   public String c;
   public InputStream is;
   public BufferedReader br;
   public OutputStream os;
   public BufferedWriter bw;
   public String b;
   public Socket s;
   public Frame f;
   public Button b1,b2,b3,b4,b5,b6,b7;
   public TextField tf1,tf2,tf3,tf4;
   public TextArea ta;
   public Checkbox cb;
   public String filename;
   public static int download=0;
   public FileOutputStream fos;
   public BufferedOutputStream bos;
   public int filesize = 99999999;// 3,214,848 bytes
   public int bytesRead;
   public int current;
   
   public client()
   {
	   f=new Frame("Client");
	   f.setBackground(Color.lightGray);
	   b1=new Button("Server IP");
	   b1.setBackground(Color.BLACK);
	   b1.setForeground(Color.WHITE);
	   b2=new Button("Server Port");
	   b2.setBackground(Color.BLACK);
	   b2.setForeground(Color.WHITE);
	   b3=new Button("Connect");
	   b3.setBackground(Color.BLACK);
	   b3.setForeground(Color.WHITE);
	   b4=new Button("File Name");
	   b4.setBackground(Color.BLACK);
	   b4.setForeground(Color.WHITE);
	   b5=new Button("Send Request");
	   b5.setBackground(Color.BLACK);
	   b5.setForeground(Color.WHITE);
	   b6=new Button("Receive");
	   b6.setBackground(Color.BLACK);
	   b6.setForeground(Color.WHITE);
	   b7=new Button("Disconnect");
	   b7.setBackground(Color.BLACK);
	   b7.setForeground(Color.WHITE);
	   tf1=new TextField(53);
	   tf2=new TextField(52);
	   tf3=new TextField(65);
	   tf4=new TextField(65);
	   cb=new Checkbox("Connection");
	   ta=new TextArea(15,50);   
   }
   
   public void show()
   {
	   f.setSize(530,500);
	   f.setLayout(new FlowLayout());
	   f.add(b1);
	   f.add(tf1);
	   f.add(b2);
	   f.add(tf2);
	   f.add(b3);
	   b3.addActionListener(this);
	   b3.setActionCommand("Connect");
	   f.add(cb); 
	   f.add(tf3);
	   f.add(b4);
	   f.add(tf4);
	   f.add(b5);
	   b5.addActionListener(this);
	   b5.setActionCommand("ask");
	   f.add(b6);
	   b6.addActionListener(this);
	   b6.setActionCommand("Receive");
	   f.add(b7);
	   b7.addActionListener(this);
	   b7.setActionCommand("Disconnect");
	   b7.disable();
	   f.add(ta);
	   b1.disable();
	   b2.disable();
	   b4.disable();
	   cb.disable();
	   f.addWindowListener(new WindowAdapter()
	   {
		   public void windowClosing(WindowEvent e)
		   {
			   System.exit(0);
		   }
	   });
	   f.setVisible(true);
   }
   
   public void run()
   {
	   try 
	   {
		   is=s.getInputStream();
		   br=new BufferedReader(new InputStreamReader(is));
		  
		   c=br.readLine();
		   while(true)
		   {
			   download=0; 
			  // if(c.isEmpty()) break;
			   
			   if(c.equals("Available"))
			   {
				   ta.append("Your File is Available and in progress.\n");
			   }
			   else if(c.equals("Unavailable"))
			   {
				   ta.append("Your File is not available in the server.\n");
			   }
			   else if(c.contains("Sending"))
			   {
				   ta.append("Click receive to download your file\n");
				   while(download!=1)
				   {
						
				   }
			   }
			   else
			   {
				   ta.append(c+"\n");
			   }
			   c=br.readLine();
		   }
	   } 
	   
	   catch (IOException e) 
	   {
		e.printStackTrace();
	   }
	   
   }
   

public void actionPerformed(ActionEvent e)
   {
	  String a=e.getActionCommand();
	   if(a.equals("Connect"))
	   {	
		   if(tf2.getText().isEmpty() || tf1.getText().isEmpty() || tf1.getText().equals("Please Enter Server IP") || tf2.getText().equals("Please Enter a Port Number"))	   
		   {
			   
			   tf1.setText("Please Enter Server IP");
			   tf2.setText("Please Enter a Port Number");
		   }
		   else
		   {
			   try
			   {	
				   	String ServerIp=tf1.getText(); 
					int port=Integer.parseInt(tf2.getText());
					s=new Socket(ServerIp,port);	
					cb.setState(false);
					tf3.setText("Trying to Connect -->IP :" +ServerIp+" Port :"+port);
					is=s.getInputStream();
					br=new BufferedReader(new InputStreamReader(is));
					while(!(c=br.readLine()).equals("Connected"))
					{
						break;
					}
					cb.setState(true);
					tf3.setText("Connected with -->IP :" +ServerIp+" Port :"+port);
					b7.enable();
					Thread t=new Thread(this);
					t.start();
				
			   }
			   catch(IOException e1)
			   {
				   e1.printStackTrace();
			   }
		   }
	   }
	   else if(a.equals("ask"))
	   {
		   try 
		   {
			   
				os=s.getOutputStream();
				bw=new BufferedWriter(new OutputStreamWriter(os));
				b=tf4.getText();
				filename=b;
				bw.write("Requesting: "+b);
				bw.newLine();
				bw.flush();
				
		   } 
		   catch (IOException e1) 
		   {
			e1.printStackTrace();
		   }
	   }
	   else if(a.equals("Receive"))
	   {
		   try
		   {	   
		        current = 0;
		        byte[] mybytearray = new byte[filesize];
		        File myFile = new File ("src/"+filename);
		        os=s.getOutputStream();
		        is=s.getInputStream();
		        fos = new FileOutputStream(myFile);
		        bos = new BufferedOutputStream(fos);
		        bytesRead = is.read(mybytearray, 0, mybytearray.length);
		        current = bytesRead;
		        ta.append("Downloading ......\n"+bytesRead+"\n"+current+"\n");
		        bos.write(mybytearray, 0, current);
		        bos.flush();
		        bos.close();
		        ta.append("File is Downloaded\n");  
				download=1;
				
				bw=new BufferedWriter(new OutputStreamWriter(os));
				bw.write("package deliverd");
				bw.newLine();
				bw.flush();
		   }
		   catch(Exception ii)
		   {
			   ii.printStackTrace();
		   }
	   }
	   else if(a.equals("Disconnect"))
		  {
			  try 
			  {
				tf1.setText("");
				tf2.setText("");
				tf3.setText("");
				tf4.setText("");
				ta.setText("");
				b4.disable();
				cb.disable();
				b3.disable();
				b1.disable();
				b2.disable();
				b4.disable();
				cb.disable();
				filename="";
				download=0;
				fos.close();
				br.close();;
				bw.close();;
				is.close();;
				os.close();	
				s.close();

			  } 
			  catch (IOException e1) 
			  {	
				e1.printStackTrace();
			  }
		  }
	   
   }
}
