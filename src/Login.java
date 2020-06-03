import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
Окно входа в систему - необходимо для входа в систему, осщуествляет проверку доступа к БД,
логин для входа - admin, пароль для входа 1234 и т.д.
Разработал: Федоров Никита Эдуардович
Почта: fnik340@gmail.com
*/
public class Login extends JFrame {
    private JPanel mainpanel;
    private JTextField login;
    private JPasswordField password;
    private JButton ok;
    private JButton exit;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private final int WIDTH = 400, HEIGHT = 300;
    private DBFramework DB;
    private Font font;
    Login () {
        setContentPane(mainpanel);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width/2)-WIDTH/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height/2)-HEIGHT/2);
        setTitle("Вход в систему");
        setIconImage(Toolkit.getDefaultToolkit().getImage("img/icon.png"));
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("fonts/Comfortaa-Regular.ttf"))).deriveFont(Font.PLAIN, 12);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DB = new DBFramework();

        login.setFont(font);
        loginLabel.setFont(font);
        passwordLabel.setFont(font);
        password.setFont(font);
        ok.setFont(font);
        exit.setFont(font);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (login.getText().length()>0 && password.getText().length()>0) {
                    if (DB.initConnection("jdbc:mysql://localhost:3306/usersdatabase?serverTimezone=UTC", "root", "1234")==0) {
                        ResultSet result = DB.selectQuery("SELECT * FROM users");
                        try {
                            while (result.next()) {
                                if (login.getText().equals(result.getString("login")) && password.getText().equals(result.getString("password"))) {
                                    //showMessage("Отлично", "Тут должно быть новое окно...", JOptionPane.WARNING_MESSAGE);
                                    MainForm F = new MainForm(600,500, "Склад", font);
                                    F.setVisible(true);
                                    F.pack();
                                    dispose();
                                    DB.closeConnection();
                                    return;
                                }
                            }
                            showMessage("Внимание", "Логин или пароль введены неверно!", JOptionPane.WARNING_MESSAGE);
                        } catch (SQLException ex) {

                        }
                    } else showMessage("Ошибка", "Нет доступа к серверу!", JOptionPane.ERROR_MESSAGE);
                } else {
                    showMessage("Внимание", "Не все поля заполнены!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
    public static void main(String[] args) {
        Login L = new Login();
        L.pack();
        L.setVisible(true);
    }
}
