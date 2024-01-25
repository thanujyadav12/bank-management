import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/Balance")
public class Balance extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Balance() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int account_number=Integer.parseInt(request.getParameter("account_number"));
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String status="ACTIVE";
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","thanujdb","move");
			PreparedStatement ps=con.prepareStatement("select account_balance from bank_accounts where account_number=? and name=? and password=? and status=?" );
			ps.setInt(1, account_number);
			ps.setString(2, name);
			ps.setString(3, password);
			ps.setString(4, status);
			ResultSet resultSet=ps.executeQuery();
			if(resultSet.next()) {
				out.print("<center><h1>ACCOUNT BALANCE IS "+resultSet.getDouble("account_balance")+"</h1><br><h3> CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
			}
			else out.print("<center><h1>OPERATION FAILED!! ENTER VALID DETAILS OR CHECK STATUS</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
