import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
...
Разработал: Федоров Никита Эдуардович
Почта: fnik340@gmail.com
*/
public class MainForm extends JFrame {
    private JPanel mainpanel;
    private JButton add;
    private JTable dataTable;
    private JButton delete;
    private JButton edit;
    private DefaultTableModel model;
    private DBFramework DB;
    private Font font;

    MainForm(int width, int height, String title, Font font) {
        setContentPane(mainpanel);
        setPreferredSize(new Dimension(width, height));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width/2)-width/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height/2)-height/2);
        setTitle(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage("img/icon.png"));
        model = new DefaultTableModel();

        model.addColumn("Идентификатор");
        model.addColumn("Название");
        model.addColumn("Цена");


        dataTable.setModel(model);
        dataTable.setFont(font);

        DB = new DBFramework();
        this.font = font;
        add.setFont(font);
        delete.setFont(font);
        edit.setFont(font);

        if (DB.initConnection("jdbc:mysql://localhost:3306/usersdatabase?serverTimezone=UTC", "root", "1234")==0) {
            ResultSet result = DB.selectQuery("SELECT * FROM product");
            if (result != null) {
                try {
                    while (result.next()) {
                        model.addRow(new String[]{result.getString("id"), result.getString("title"), result.getString("price")});
                    }
                } catch (SQLException ex) {

                }
            } else {
                showMessage("Ошибка", "Запрошенная таблица не существует или была удалена", JOptionPane.ERROR_MESSAGE);
            }
        }

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProduct A = new AddProduct(300, 600, "Добавить товар", null, false, font);
                A.setModel(model);
                A.setVisible(true);
                A.pack();
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataTable.getSelectedRow()>=0) {
                    DB.execQuery("DELETE FROM product WHERE id="+model.getValueAt(dataTable.getSelectedRow(), 0));
                    model.removeRow(dataTable.getSelectedRow());
                } else {
                    showMessage("Внимание", "Ни одна запись не выбрана", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataTable.getSelectedRow()>=0) {
                    Product product = new Product();
                    product.setID(Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 0) + ""));
                    product.setTitle(model.getValueAt(dataTable.getSelectedRow(), 1) + "");
                    product.setPrice(Integer.parseInt(model.getValueAt(dataTable.getSelectedRow(), 2) + ""));

                    AddProduct A = new AddProduct(300, 600, "Редактировать товар", product, true, font);
                    A.setModel(model);
                    A.setVisible(true);
                    A.pack();
                } else {
                    showMessage("Внимание", "Ни одна запись не выбрана!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}
