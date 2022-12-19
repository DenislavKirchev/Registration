import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Register extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField pfPassword;
    private JTextField tfDate;
    private JTextField tfCountry;
    private JCheckBox CheckBox;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public Register(JFrame parent){
        super(parent);
        setTitle("Create new account");
        Container registerPanel;
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }


    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String date = tfDate.getText();
        String country = tfCountry.getText();
        String password = String.valueOf(pfPassword.getText());


        if (name.isEmpty() || date.isEmpty() || email.isEmpty() || country.isEmpty() || password.isEmpty() || !CheckBox.isValid()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        user = addUserToDatabase(name, email, date, country, password);
        if (user != null){
            dispose();
        }else{
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public User user;
    private User addUserToDatabase(String name, String email, String phone, String address, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/registration_form";
        final String USERNAME = "root";
        final String PASSWORD = "denis2001";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users(name, email, date, country, password" +
                    "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, country);
            preparedStatement.setString(5, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.date = date;
                user.country = country;
                user.password = password;
            }
            stmt.close();
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }


    public static void main (String[]args){
        Register myForm = new Register(null);
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of: " + user.name);
        }else{
            System.out.println("Registration canceled");
        }
    }

}
}
