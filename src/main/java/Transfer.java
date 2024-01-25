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
@WebServlet("/Transfer")
public class Transfer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Transfer() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int account_number=Integer.parseInt(request.getParameter("account_number"));
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		int beneficiary_account_number=Integer.parseInt(request.getParameter("beneficiary_account_number"));
		double transfer_amount=Double.parseDouble(request.getParameter("transfer_amount"));
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
			PreparedStatement ps3=con.prepareStatement("select account_balance from bank_accounts where account_number=? and status=?");
			ps3.setInt(1, beneficiary_account_number);
			ps3.setString(2, status);
			ResultSet resultSet1=ps.executeQuery();
			ResultSet resultSet2=ps3.executeQuery();
			double sender_account_balance,sender_old_balance,beneficiary_old_balance,beneficiary_account_balance;
			int i=0,j=0;
			if(resultSet1.next()&&resultSet2.next()) {
				sender_old_balance=resultSet1.getDouble("account_balance");
				beneficiary_old_balance=resultSet2.getDouble("account_balance");
					if(sender_old_balance>transfer_amount) {
						sender_account_balance=sender_old_balance-transfer_amount;
						beneficiary_account_balance=beneficiary_old_balance+transfer_amount;
						PreparedStatement ps2=con.prepareStatement("update bank_accounts set account_balance="+sender_account_balance+" where account_number="+account_number);
						PreparedStatement ps4=con.prepareStatement("update bank_accounts set account_balance="+beneficiary_account_balance+" where account_number="+beneficiary_account_number);
						i=ps2.executeUpdate();
						j=ps4.executeUpdate();
						if(i==1&&j==1)out.print("<center><h1>MONEY TRANSFERED SUCCESSFULLY!!!</h1><br><h2>SENDER ORGINAL BALANCE IS "+sender_old_balance+"</h2><br><h2>BENEFICIARY ORGINAL BALANCE IS "+beneficiary_old_balance+"</h2><br><h2>TRANSFER AMOUNT IS "+transfer_amount+"</h2><br><h2>SENDER ACCOUNT BALANCE IS "+sender_account_balance+"</h2><br><h2>BENEFICIARY ACCOUNT BALANCE IS "+beneficiary_account_balance+"</h2><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
						else out.print("<center><h1>MONEY NOT TRANSFERED!! ERROR!!</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
					}else out.print("<center><h1>OPERATION FAILED!! INSUFFICIENT BALANCE </h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
				}else out.print("<center><h1>OPERATION FAILED!! ENTER VALID DETAILS OR CHECK STATUS</h1><br><h3>CLICK HERE FOR HOME PAGE <a href=\"bank_home.html\" target=\"_blank\">Home</a></h3></center>");
			con.close();		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
