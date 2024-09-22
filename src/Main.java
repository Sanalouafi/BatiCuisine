import main.java.connection.DatabaseConnection;

public class Main {
    public static void main(String[] args) {

        DatabaseConnection.getInstance().getConnection();

    }
}