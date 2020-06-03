import java.sql.*;
/*
...
Разработал: Федоров Никита Эдуардович
Почта: fnik340@gmail.com
*/
public class DBFramework {
    private Connection connect;
    int initConnection(String url, String user, String password) {
        try {
            connect = DriverManager.getConnection(url, user, password);
            return 0;
        } catch (SQLException throwables) {
        }
        return 1;
    }
    /* метод позволяет выполнить запрос к БД и получить ответ.
       В случае неудачи возвращается null
     */
    ResultSet selectQuery(String sql) {
        ResultSet result = null;
        try {
            result = connect.createStatement().executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    
    void execQuery(String sql) {
        try {
            connect.prepareStatement(sql).executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    void closeConnection() {
        try {
            connect.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
