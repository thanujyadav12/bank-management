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
@WebServlet("/Withdraw")
public class Withdraw extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Withdraw() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int account_number=Integer.parseInt(request.getParameter("account_number"));
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		double withdraw_amount=Double.parseDouble(request.getParameter("withdraw_amount"));
		String status="ACTIVE";
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","thanujdb","move");
			PreparedStatement ps=con.prepareStatement("select account_balance from bank_accounts where account_number=? and name=? and password=? and status=?");
			ps.setInt(1, account_number);
			ps.setString(2, name);
			ps.setString(3, password);
			ps.setString(4, status);
			ResultSet resultSet=ps.executeQuery();
			double account_balance,old_balance;
			int i=0;
			if(resultSet.next()) {
				
				old_balance=resultSet.getDouble("account_balance");
				if(old_balance>withdraw_amount) {
					account_balance=old_balance-withdraw_amount;
					PreparedStatement ps2=con.prepareStatement("update bank_accounts set account_balance="+account_balance+" where account_number="+account_number);
					i=ps2.executeUpdate();
					if(i==1)out.print("<center><h1>MONEY WITHDRAWN SUCCESSFULLY!!!</h1><br><h2>ORGINAL BALANCE IS "+old_balance+"</h2><br><h2>WITHDRAW AMOUNT IS "+withdraw_amount+"</h2><br><h2>ACCOUNT BALANCE IS "+account_balance+"</h2><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
					else out.print("<center><h1>MONEY NOT WITHDRAWN!!</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
				}else out.print("<center><h1>OPERATION FAILED!! INSUFFICIENT BALANCE </h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
			}else out.print("<center><h1>OPERATION FAILED!! ENTER VALID DETAILS OR CHECK STATUS </h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
			con.close();		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
