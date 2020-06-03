import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
...
Разработал: Федоров Никита Эдуардович
Почта: fnik340@gmail.com
*/
public class AddProduct extends JFrame {
    private JPanel mainpanel;
    private JButton save;
    private JButton exit;
    private JTextField textID;
    private JTextField textTitle;
    private JTextField textPrice;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JTable dataTable;
    private DBFramework DB;
    private DefaultTableModel model;
    private Product product;
    private boolean isEditor;
    private Font font;

    AddProduct(int width, int height, String title, Product product, boolean isEditor, Font font) {
        setContentPane(mainpanel);
        setPreferredSize(new Dimension(width, height));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width/2)-width/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height/2)-height/2);
        setTitle(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage("img/icon.png"));
        save.setFont(font);
        exit.setFont(font);
        textID.setFont(font);
        textPrice.setFont(font);
        textTitle.setFont(font);
        idLabel.setFont(font);
        nameLabel.setFont(font);
        priceLabel.setFont(font);

        DB = new DBFramework();
        this.product = product;
        this.isEditor = isEditor;
        this.font = font;

        if (product != null) {
            textID.setText(product.getId()+"");
            textTitle.setText(product.getTitle());
            textPrice.setText(product.getPrice()+"");
        }

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textID.getText().length()>0 && textTitle.getText().length()>0 && textPrice.getText().length()>0) {
                    if (checkValue(textID) == 1 || checkValue(textPrice) == 1) {
                        showMessage("Внимание", "Одно из полей содержит недопустимые значения", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (DB.initConnection("jdbc:mysql://localhost:3306/usersdatabase?serverTimezone=UTC", "root", "1234")==0) {
                        DB.execQuery("CREATE TABLE IF NOT EXISTS product(id INT(64), title VARCHAR(45), price INT(64))");

                        Product product = new Product();
                        product.setID(Integer.parseInt(textID.getText()));
                        product.setTitle(textTitle.getText());
                        product.setPrice(Integer.parseInt(textPrice.getText()));


                        //DB.execQuery("INSERT INTO product (id, title, price) VALUES ("+product.getId()+",'"+ product.getTitle()+"',"+product.getPrice()+")");
                        //model.addRow(new String[]{product.getId()+"", product.getTitle(), product.getPrice()+""});
                        if (isEditor) {
                            DB.execQuery("UPDATE product SET title='"+product.getTitle()+"', price="+product.getPrice()+" WHERE id="+product.getId());
                            model.removeRow(dataTable.getSelectedRow());
                            model.insertRow(dataTable.getSelectedRow()+1, new String[]{product.getId()+"", product.getTitle(), product.getPrice()+""});
                        } else {
                            DB.execQuery("INSERT INTO product (id, title, price) VALUES ("+product.getId()+",'"+ product.getTitle()+"',"+product.getPrice()+")");
                            model.addRow(new String[]{product.getId()+"", product.getTitle(), product.getPrice()+""});
                        }
                        DB.closeConnection();
                        dispose();
                    } else {
                        showMessage("Ошибка", "Нет доступа к серверу!", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    showMessage("Внимание", "Не все поля заполнены!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    int checkValue(JTextField textField) {
        try {
            Integer.parseInt(textField.getText());
        } catch (NumberFormatException ex) {
            return 1;
        }
        return 0;
    }

    void setModel(DefaultTableModel model) {
        this.model = model;
    }

    void showMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}
