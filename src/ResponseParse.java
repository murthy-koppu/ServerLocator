import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;


public class ResponseParse {
	public static void main(String[] args) throws IOException {
/*		URL url = new URL("jdbc:mysql://localhost:3306/co_leaning");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		//connection.
		System.out.println(connection.getContent());
		System.out.println(connection.getHeaderFields());
		*/
		/*InetAddress objInet = InetAddress.getByName("https://www.google.co.in");
		System.out.println(objInet);*/
		Socket client = new Socket("localhost", 3306);
		System.out.println("Inet address is "+client.getInetAddress());
		InputStream s1In = client.getInputStream();
		 DataInputStream dis = new DataInputStream(s1In);
		 Byte byteMsg = null;
		 long l =0;
		 while(l < 77){
			 if((byteMsg = dis.readByte()) != null)
				 System.out.print((char)(byteMsg.byteValue()));
			 l++;
		 }
		 SocketAddress serverAddr = client.getRemoteSocketAddress();
		
		 /*char ch= '\0';
		 while(true){
			 System.out.print(dis.readChar());
		 }*/
		
	}
}
