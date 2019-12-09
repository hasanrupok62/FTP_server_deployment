import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import javax.swing.*;


public class Server implements ActionListener,Runnable
{
   public int port;
   public int tempport=0,tempConnect=0;
   public String filename;
   public ArrayList<String> store=new ArrayList<String>();
   public String c;
   public InputStream is;
   public BufferedReader br;
   public OutputStream os;
   public BufferedWriter bw;
   public ServerSocket ss;
   public Socket s;
   public JFrame f;
   public JButton b1,b2,b3,b4;
   public Label l1,l2,l3,l4,l5;
   public TextField tf1,tf2;
   public TextArea ta,ta1;
   public File File1;
   public FileInputStream fis = null;
   public BufferedInputStream bis = null;
 
   public Server()
   {
	   f=new JFrame("Server");
           f.setSize(700,700);
           f.setLayout(null);
	   f.setBackground(Color.lightGray);
	   l1=new Label("Port Number:");
           l1.setBounds(15, 20,100, 30);
	   l1.setBackground(Color.BLACK);
	   l1.setForeground(Color.WHITE);
           
           tf1=new TextField(52);
	   tf1.setBounds(139, 22,200,30);
	   
	   l2=new Label("start port connection");
           l2.setBounds(490, 80,100, 30);
           l2.setBackground(Color.BLACK);
           l2.setForeground(Color.WHITE);
	   b1=new JButton("Start");
	   b1.setBounds(348,23,100, 30);
	   b1.setBackground(Color.WHITE);
	   b1.setForeground(Color.BLUE);
           
           l2=new Label("Start Port Connection");
           l2.setBounds(468, 27,120, 20);
	   l2.setBackground(Color.BLACK);
	   l2.setForeground(Color.WHITE);
           
	   b2=new JButton("Connect");
	   b2.setBounds(150, 60,100, 30);
	   b2.setBackground(Color.WHITE);
	   b2.setForeground(Color.BLUE);
           
           l3=new Label("No Connection");
           l3.setBounds(260, 65,90,20);
	   l3.setBackground(Color.WHITE);
	   l3.setForeground(Color.RED);
           
           l4=new Label("STATUS");
           l4.setBounds(15,160,100, 30);
	   l4.setBackground(Color.BLACK);
	   l4.setForeground(Color.WHITE);
           
           tf2=new TextField(65);
           tf2.setBounds(140,160,500,30);
           
	   b3=new JButton("Send File");
           b3.setBounds(140,210,100, 30);
	   b3.setBackground(Color.WHITE);
	   b3.setForeground(Color.BLUE);
           
	   b4=new JButton("Disconnect");
           b4.setBounds(440,210,100, 30);
	   b4.setBackground(Color.WHITE);
	   b4.setForeground(Color.RED);
	   
           l5=new Label("AVAILABLE FILES");
           l5.setBounds(350,250,100, 30);
	   l5.setBackground(Color.BLACK);
	   l5.setForeground(Color.WHITE);
           
	   ta1=new TextArea(20,30);
           ta1.setBounds(350,280,300,350);//for available file
	   ta=new TextArea(20,35);
           ta.setBounds(20,280,300,350);
	   
          
	   
   }
   
   public void show()
   {
	  
	   f.add(l1);
           f.add(l2);
           f.add(l3);
           f.add(l4);
           f.add(l5);
	   f.add(tf1);
	   f.add(b1);
	   b1.addActionListener(this);  
	   f.add(b2);
	   b2.addActionListener(this);  
	   f.add(tf2);
	   f.add(b3);	  
	   b3.addActionListener(this);
	   b3.setActionCommand("Send");
	   f.add(b4);
	   b4.addActionListener(this);
	   
	   f.add(ta1);
	  // ta1.append("\n");
           try {
                       
                       Scanner scan=new Scanner(System.in);
                       BufferedReader x = new BufferedReader(new FileReader("src/Storage.txt"));
                       String p = new Scanner(x).useDelimiter("\\Z").next();
                       ta1.append(p);
                       ta1.append("\n");
                       x.close();
                   } 
           catch (IOException e1) 
	   {
		e1.printStackTrace();
	   }
  
	   f.add(ta);
	   f.addWindowListener(new WindowAdapter()
	   {
		   public void windowClosing(WindowEvent e)
		   {
			   System.exit(0);
		   }
	   });
	   f.setVisible(true);
           f.addMouseListener(new MouseAdapter() {				
			public void mouseClicked(MouseEvent e){
				System.out.println(e.getX()+"..."+e.getY());
			}
		});
    	   try 
    	   {
    		   File1=new File("src/Storage.txt");
    		   br=new BufferedReader(new FileReader(File1));
    		   while((c=br.readLine())!=null)
    		   {
    			   store.add(c);
    			   ta1.append(c+"\n");
    		   }
    		   br.close();
    	   } 
    	   
    	   catch (IOException e1) 
    	   {
    		e1.printStackTrace();
    	   }
   }
   public void run()
   {
	  try 
	   {
			is=s.getInputStream();
			br=new BufferedReader(new InputStreamReader(is));
			os=s.getOutputStream();
			bw=new BufferedWriter(new OutputStreamWriter(os));
			c=br.readLine();
			while(true)
			{	
				//if(c.isEmpty()) break;
				if(c.contains("Requesting: "))
				{
					
					String[] a=c.split(" ");
					File1=new File("src/"+a[1]);
					System.out.println(File1.getName());
					if(store.contains(File1.getName()))
					{
						String str="Package requested from : "+tempport+" is available.";
						ta.append(str+"\n");
						//cb3.setState(true);
						//cb3.setLabel("Available");
						//b3.setEnabled(true);
						bw.write("Available");
						bw.newLine();
						bw.flush();
						while(true)
						{
							c=br.readLine();
							if(c.contains("package deliverd"))
							{
								ta.append("Package Delivered\n");
								break;
							}
						}
					}
					
					else if(!store.contains(File1.getName()))
					{
						String str="Package requested from : "+tempport+" is not in server.\n";
						ta.append(str+"\n");
						//cb3.setState(false);
						//cb3.setLabel("Unavailable");
						//b3.disable();
						bw.write("Unavailable");
						bw.newLine();
						bw.flush();
					}
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
	  
	   if(a.equals("Start"))
	   {
		   
	   		if(tf1.getText().isEmpty() || tf1.getText().equals("Please Enter a Port Number"))
	   		{
	   			tf1.setText("Please Enter a Port Number");		
	   		}
	   		else
	   		{
	   			port=Integer.parseInt(tf1.getText());
	   			if(tempport==port)
	   			{
	   				//cb.setState(true);
					l2.setText("Port already Opened");
	   			}
		   		else	
				{	
		   			try
		   			{
						//cb.setState(true);
						ss=new ServerSocket(port);
						l2.setText("Port is Opened");
						tempport=port;	
		   			}
		   			catch(Exception eport)
		   			{
		   				eport.printStackTrace();
		   			}
		   		}
	   		}
	  }
	   
	   else if(a.equals("Connect"))
		  {
			  try 
			   {
				  if(tempport!=0)
				  {
					  if(tempConnect==0 || tempport==ss.getLocalPort())
					  	{  	
						  	
					  		s=ss.accept();
					  		if(s.isConnected()==true)
							{	
								//cb2.setState(true);
								l3.setText("Connected");
								os=s.getOutputStream();
								bw=new BufferedWriter(new OutputStreamWriter(os));
								bw.write("Connected");
								bw.newLine();
								bw.flush();
								tf2.setText("Connection Established with Client on Port : "+tempport);	
								//b4.setEnabled(true);
								
								
								 File myFile1 = new File ("src/Storage.txt");
						          byte [] mybytearray  = new byte [(int)myFile1.length()];
						          fis = new FileInputStream(myFile1);
						          bis = new BufferedInputStream(fis);
						          bis.read(mybytearray,0,mybytearray.length);   
						          os.write(mybytearray,0,mybytearray.length);
						          os.flush();
								
								
								
								
								tempConnect=1;
								Thread t=new Thread(this);
								t.start();
							}
					  	}
					  	else 
					  	{
					  		tf2.setText("Connection can't be Established with Client on Port : "+tempport);
							tempConnect=1;
					  	}
				  }
				  else
				  {
					  tf2.setText("Connection can't be Established with Client on Port : "+tempport);
				  }
			   } 
			   catch (Exception econnect) 
			   {
				   econnect.printStackTrace();
			   }
		  }
	  else if(a.equals("Send"))
	  {
		  try 
		  {
			  ta.append("Sending Packet\n");
			  os=s.getOutputStream();
			  bw=new BufferedWriter(new OutputStreamWriter(os));
			  bw.write("Sending");
			  bw.newLine();
			  bw.flush();
			  File myFile = new File ("src/"+File1.getName());
	          byte [] mybytearray  = new byte [(int)myFile.length()];
	          fis = new FileInputStream(myFile);
	          bis = new BufferedInputStream(fis);
	          bis.read(mybytearray,0,mybytearray.length);   
	          os.write(mybytearray,0,mybytearray.length);
	          os.flush();
		  }
		  catch(Exception esend)
		  {
			  esend.printStackTrace();
		  }	    
	  }
	  else if(a.equals("Disconnect"))
	  {
		  try 
		  {
			tf1.setText("");
			tf2.setText("");
			ta.setText("");
			
			/*b4.disable();
			cb.disable();
			cb2.disable();
			cb3.disable();
			b3.disable();*/
			port=0;
			tempport=0;
			tempConnect=0;
			filename="";
			br.close();;
			bw.close();;
			is.close();;
			os.close();	
			s.close();
			ss.close();;
		  } 
		  catch (IOException e1) 
		  {	
			e1.printStackTrace();
		  }
	  }
   }
   public static void main(String[] args) 
	{
		Server s=new Server();
		s.show();
	}

   
}



