package com.shallowblue;

//import java.awt.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShowDataServlet
 */
@WebServlet("/ShowDataServlet")
public class ShowDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
    	
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
        	//out.println("start retrieving!  ");
        	//Class.forName("com.mysql.jdbc.Driver"); 
            //String sql="SELECT * from games";  
            //out.println("success!!");
            String message = request.getParameter("str");
            int depth = Integer.parseInt(request.getParameter("str2"));
            //out.println(message+" depth:"+depth+" ");
            //message = message.replaceAll("<lol>", "\n");
            //out.println(message+" depth:"+depth+" ");
            String res="";
            //out.println(" number of moves: ");
            List<Move> moves=new AIMoveLocal(1.0).move(GameBoard.unpack(message),depth);
            //out.println("moves: ");
            for(Move move:moves)
            {
            	res=res+move.toString()+" ";
            }
            res=res.substring(0,res.length()-1);
            out.println(""+res+"!");
            
            
            
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //out.println("retrieving AI moves!  ");
        
        try {
            processRequest(request, response);
            //out.println("complete!");
        } catch (ClassNotFoundException ex) {
            out.println("failed to retrieve AI moves");
        }
        
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	
	

}
