import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.opencsv.CSVWriter;


public class DataWorks 
{
	
	static Scanner s = new Scanner(System.in);
	
	static Connection dbConnection() throws SQLException, ClassNotFoundException
	{

		// Database driver and URL
	    final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	    String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	    
	    //"jdbc:oracle:thin:@localhost:1521:xe","system","root"
	    
	    // Database credentials
	    final String USER = "system";//getGlobalVariable("DB_Username");
	    final String PASS = "root";//getGlobalVariable("DB_Password");

        Connection conn = null;        

        // STEP 2: Register JDBC driver
        try 
        {
			Class.forName(JDBC_DRIVER);
		} 
        catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}

        // STEP 3: Open a connection
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
	}
	
	static void insert() throws ClassNotFoundException, SQLException
	{
		Connection conn = dbConnection();
		
		System.out.println("Enter the name: ");
		String name = s.next();
		System.out.println("Enter the email address: ");
		String email = s.next();
		System.out.println("Enter the region: ");
		String region = s.next();
		
		String sql = "insert into emp values('"
												+name+"','"
												+email+"','"
												+region+"')";
		Statement statement = conn.createStatement();
		statement.executeUpdate(sql);
		System.out.println("Data inserted");
		statement.close();
		conn.close();
	}
	
	static void delete() throws ClassNotFoundException, SQLException
	{
		Connection conn = dbConnection();
		
		System.out.println("Enter the name: ");
		String name = s.next();
		
		String sql = "delete from emp where name = '"+name+"'";
		Statement statement = conn.createStatement();
		statement.executeUpdate(sql);
		
		System.out.println("Data deleted");
		
		statement.close();
		conn.close();
	}
	
	static void display(String region) throws ClassNotFoundException, SQLException
	{
		Connection conn = dbConnection();
		
      	String SQL = "select * from emp where region ='"+region+"'";
      	
      	// Execute query and get results
    	Statement statement = conn.createStatement();
    	ResultSet resultSet = statement.executeQuery(SQL);
    	
    	if (resultSet.next()) 
    	{
    		ResultSetMetaData metaData = resultSet.getMetaData();
    		int numberOfColumns = metaData.getColumnCount();
    		System.out.println("Employee details based on region "+region+" -");

    		for (int i = 1; i <= numberOfColumns; i++) 
    		{
    			System.out.print(metaData.getColumnLabel(i) + "\t");
    		}
    		System.out.println();
    		do 
    		{
    			for (int i = 1; i <= numberOfColumns; i++) 
    			{
    				System.out.print(resultSet.getObject(i) + "\t");
    			}
    			System.out.println();
    		} 
    		while (resultSet.next());

    		System.out.println();

    	} 
    	else 
    	{
    		System.out.println("No such employee/s exist.");
    	}
	}
	
	static void generate(String region) throws IOException, SQLException, ClassNotFoundException
	{
		Connection conn = dbConnection();
		
      	String SQL = "select * from emp where region ='"+region+"'";
      	
      	// Execute query and get results
    	Statement statement = conn.createStatement();
    	ResultSet resultSet = statement.executeQuery(SQL);
    	
    	// Path to CSV file
    	String pathToCSV = "C:\\users\\"+System.getProperty("user.name")+"\\desktop\\Report.xlsx";
    	
    	FileWriter fw = new FileWriter(pathToCSV, true);
    	CSVWriter writer = new CSVWriter(mFileWriter);
    	Boolean includeHeaders = true;
    	
    	// Inserting/appending SQL results to CSV file
    	Files.write(Paths.get(pathToCSV), "\n".getBytes(), StandardOpenOption.APPEND);
    	Files.write(Paths.get(pathToCSV), SQL.getBytes(), StandardOpenOption.APPEND);
    	Files.write(Paths.get(pathToCSV), "\n".getBytes(), StandardOpenOption.APPEND);
    	writer.writeAll(resultSet, includeHeaders);
    	Files.write(Paths.get(pathToCSV), "\n".getBytes(), StandardOpenOption.APPEND);
    	System.out.println("Report generated. Reported located at - "+pathToCSV);
    	// Closing resources
    	writer.close();    	
    	fw.close();
    	resultSet.close();
    	statement.close();
        conn.close();
	}
	
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException 
	{
		while(true)
		{
			System.out.println("Choose your operation (Press 1 to 5):");
			System.out.println("1. Insert");
			System.out.println("2. Delete");
			System.out.println("3. View");
			System.out.println("4. Generate");
			System.out.println("5. Exit");
			int opt = s.nextInt();
			switch(opt)
			{
				case 1:
					System.out.println("Insertion operation selected");
					insert();
					break;
				case 2:
					System.out.println("Delete operation selected");
					delete();
					break;
				case 3:
					System.out.println("Display operation selected");
					System.out.println("Enter the region: ");
					String dregion = s.next();
					display(dregion);
					break;
				case 4:
					System.out.println("Generate operation selected");
					System.out.println("Enter the region: ");
					String region = s.next();
					generate(region);
					break;
				case 5:
					return;
				default:
					System.out.println("Wrong Input!!!!");
					break;
			}
		}
	}
}
