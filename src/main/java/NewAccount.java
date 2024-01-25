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
@WebServlet("/NewAccount")
public class NewAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public NewAccount() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int account_number=Integer.parseInt(request.getParameter("account_number"));
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String password2=request.getParameter("password2");
		double account_balance=Double.parseDouble(request.getParameter("account_balance"));
		String address=request.getParameter("address");
		long mobile=Long.parseLong(request.getParameter("mobile"));
		String active="ACTIVE";
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		if(password.equals(password2)) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","thanujdb","move");
				PreparedStatement ps=con.prepareStatement("insert into bank_accounts values(?,?,?,?,?,?,?)");
				ps.setInt(1, account_number);
				ps.setString(2, name);
				ps.setString(3, password);
				ps.setDouble(4,account_balance);
				ps.setString(5, address);
				ps.setLong(6, mobile);
				ps.setString(7, active);
				int i=ps.executeUpdate();
				if(i==1)out.print("<center><h1>NEW ACCOUNT CREATED SUCCESSFULLY!!!!!</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
				else out.print("<center><h1>NEW ACCOUNT CREATION FAILED</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
				con.close();
			}catch (Exception e) {
					e.printStackTrace();
			}
		}else out.print("<center><h1>BOTH PASSWORDS DID NOT MATCH...TRY AGAIN....</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
	}
}
