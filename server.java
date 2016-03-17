import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

class server {
    public static void main(String[] args){
        ServerSocket server=null;
        Socket socket;
        String s="shallow blue server";
        String ss;
        InputStream is;
        OutputStream os;
        DataInputStream dis;
        PrintStream ps;
        try{
            server=new ServerSocket(11885);
        }
        catch (Exception e){
            System.out.println("Error:"+e);
        }
        try{
            socket=server.accept();
            System.out.printf("connection success：\n%s\n", socket.toString());
            
            is=socket.getInputStream();
            os=socket.getOutputStream();
           
            dis=new DataInputStream(is);
            ps=new PrintStream(os);
            DataInputStream in=new DataInputStream(System.in);
            while(true){
                s=dis.readLine();
                System.out.println("client:"+s);
                if (s.trim().equals("end")){
                    System.out.println("connection break");
                    break;
                }
                ss=in.readLine();
                ps.println(ss);
                if (ss.trim().equals("end")){
                    System.out.println("connection break");
                    break;
                }
            }
            dis.close();
            ps.close();
            is.close();
            os.close();
            socket.close();
        }
        catch(Exception e){
            System.out.println("Error:"+e);
        }
    }
}