package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // MariaDB 연결 정보
    private static final String URL      = "jdbc:mariadb://localhost:3306/conmayo"; // DB URL
    private static final String USER     = "root";       // DB 계정
    private static final String PASSWORD = "비밀번호입력";  // DB 비밀번호

    // 외부에서 인스턴스 생성 못하게 막음 (유틸 클래스)
    private DatabaseConnector() {}

    // Connection 객체 반환 — 호출할 때마다 새 연결 생성
    // DAOImpl 생성자에서 Main이 이 메서드로 conn을 받아서 주입
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
