package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    // MariaDB 연결 정보
	private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
    	
    	Properties props = new Properties();
    	
    	try (FileInputStream fis =
    	         new FileInputStream("src/db/db.properties")) {

    	    props.load(fis);

    	    URL = props.getProperty("url");
    	    USER = props.getProperty("user");
    	    PASSWORD = props.getProperty("password");

    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
        
    }

    // 외부에서 인스턴스 생성 방지 (유틸 클래스)
    private DatabaseConnector() {}

    // Connection 객체 반환
    // 생성된 Connection은 DAOImpl 생성자 등에 전달하여 사용
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
