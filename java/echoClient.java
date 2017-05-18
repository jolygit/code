import java.io.*;
import java.net.*;
 
public class echoClient {
    public static void main(String[] args) throws IOException {
         
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
	    // BufferedReader in =
	    //  new BufferedReader(
              InputStream in=echoSocket.getInputStream();
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String userInput;
	    userInput = stdIn.readLine();
            out.println(userInput);
	    while(true){
	    if(in.available()>0){
		int num=in.available();
		byte[] cbuff= new byte[num];
		in.read(cbuff);
		String str1 = new String(cbuff);
		System.out.println("echo: " + String.valueOf(num)+str1);//+ cbuff.toString(
	     break;
	    }
	    }
           
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
