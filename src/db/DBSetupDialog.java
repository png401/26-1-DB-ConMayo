package db;

import javax.swing.*;
import java.awt.*;

public class DBSetupDialog {

    public static class DBConfig {
        public final String host;
        public final String port;
        public final String password;

        public DBConfig(String host, String port, String password) {
            this.host = host;
            this.port = port;
            this.password = password;
        }
    }

    /**
     * DB 연결 설정 다이얼로그를 띄우고 입력값을 반환.
     * 취소하면 System.exit(0).
     */
    public static DBConfig show() {
        JTextField hostField = new JTextField("localhost");
        JTextField portField = new JTextField("3306");
        JPasswordField passwordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 8));
        panel.add(new JLabel("Host:"));
        panel.add(hostField);
        panel.add(new JLabel("Port:"));
        panel.add(portField);
        panel.add(new JLabel("Root 비밀번호:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "ConMayo DB 연결 설정",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            System.exit(0);
        }

        return new DBConfig(
                hostField.getText().trim(),
                portField.getText().trim(),
                new String(passwordField.getPassword())
        );
    }
}