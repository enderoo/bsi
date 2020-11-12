import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main 
{
    public static void main(String[] args) 
    {
	System.out.println("Starting...");
	Scanner scanner = new Scanner(System.in);

	boolean x = true;
	do{
		System.out.println("Applications works type anything to start.");
		String input = scanner.nextLine();
		x = !(input != null && !input.isEmpty()); 
	}while(x);

	String close = null;
	do{
		Connection connection = null;
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		    String url = "jdbc:mysql://10.0.10.3:3306/pfswcholab4?serverTimezone=UTC";

		    System.out.println("Connecting...");
		    connection = DriverManager.getConnection(url, "mkempny", "mkempny");
		    System.out.println("Connected...");
		    Statement statement = connection.createStatement();
		    statement.executeUpdate(createTableQuerry());
		    statement.execute(addEntityQuerry("default1",  "default11", "1"));
		    statement.execute(addEntityQuerry("default2",  "default22", "2"));
		    statement.execute(addEntityQuerry("default3",  "default33", "3"));
		    System.out.println("Created table in given database...");
		    
		    while(doMainInterface(statement)) {};

		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    
		} catch (ClassNotFoundException ex) {
		    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally {
		    try {
			if(connection != null) {
			    connection.close();
			}
		    } 
		    catch(SQLException ex) { }
		}

		System.out.println("");
		System.out.println("Type any thing to retry or 0 to close.");
		close = scanner.nextLine();
	}while(!"0".equals(close));
    }
    
    private static String createTableQuerry(){
        return  "CREATE TABLE IF NOT EXISTS people " +
                "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                " first VARCHAR(255), " + 
                " last VARCHAR(255), " + 
                " age INTEGER, " + 
                " PRIMARY KEY ( id ))";
    }
    
    private static String selectStarQuerry(){
        return  "SELECT * FROM people";
    }
    
    private static String addEntityQuerry(String first,  String last, String age){
        return  "INSERT INTO people (first,last,age) VALUES ('"+first+"','"+last+"',"+age+")";
    }
    private static String modifyEntityQuerry(String id, String first,  String last, String age){
        return  "UPDATE people SET first = '"+first+"', last = '"+last+"', age = "+age+" WHERE id=" + id;
    }
    private static String removeEntityQuerry(String id){
        return  "DELETE FROM people WHERE id=" + id;
    }
    
    private static boolean doMainInterface(Statement statement) throws SQLException{
        Scanner scanner = new Scanner(System.in); 
        System.out.println("Enter action index:");
        System.out.println("0 - show table");
        System.out.println("1 - add entity");
        System.out.println("2 - modify entity");
        System.out.println("3 - remove entity");
        System.out.println("any other - exit");
        boolean exit = false;
        String answer = scanner.nextLine();
        switch(answer){
            case "0":
                HandleShowing(statement);
                break;
            case "1":
                HandleAddingEntity(statement);
                break;
            case "2":
                HandleModyfingEntity(statement);
                break;
            case "3":
                HandleRemovingEntity(statement);
                break;
            default:
                exit = true;
                break;
        }
        System.out.println();
        System.out.println();
        return !exit;
    }
    
    private static void HandleShowing(Statement statement) throws SQLException{
        ResultSet results = statement.executeQuery(selectStarQuerry());
        while(results.next()){
            String result = "";
            result += "id: " + results.getInt("id") + " ";
            result += "age: " + results.getInt("age") + " ";
            result += "first: " + results.getString("first")+ " ";
            result += "last: " + results.getString("last")+ " ";
            System.out.println(result);
        }
    }
    
    private static void HandleAddingEntity(Statement statement) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter first:");
        String first = scanner.nextLine();
        
        System.out.println("Enter last:");
        String last = scanner.nextLine();
        
        System.out.println("Enter age:");
        String age = scanner.nextLine();
        
        statement.execute(addEntityQuerry(first, last, age));
        System.out.println("Entity added.");
    }
    
    private static void HandleModyfingEntity(Statement statement) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter id:");
        String id = scanner.nextLine();
        
        System.out.println("Enter first:");
        String first = scanner.nextLine();
        
        System.out.println("Enter last:");
        String last = scanner.nextLine();
        
        System.out.println("Enter age:");
        String age = scanner.nextLine();
        
        statement.execute(modifyEntityQuerry(id, first, last, age));
        System.out.println("Entity modified.");
    }
    
    private static void HandleRemovingEntity(Statement statement) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter id:");
        String id = scanner.nextLine();
        
        statement.execute(removeEntityQuerry(id));
        System.out.println("Entity removed.");
    }
}

