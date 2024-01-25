import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/ReopenAccount")
public class ReopenAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ReopenAccount() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int account_number=Integer.parseInt(request.getParameter("account_number"));
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String active="ACTIVE";
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","thanujdb","move");
			PreparedStatement ps=con.prepareStatement("update bank_accounts set status=? where account_number=? and name=? and password=?");
			ps.setString(1, active);
			ps.setInt(2, account_number);
			ps.setString(3, name);
			ps.setString(4, password);
			int i=ps.executeUpdate();
			if(i==1)out.print("<center><h1>ACCOUNT REOPENED SUCCESSFULLY!!!</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
			else out.print("<center><h1>ACCOUNT NOT REOPENED!! CHECK DETAILS</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
			con.close();		
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
