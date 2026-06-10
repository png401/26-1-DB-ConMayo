package db;

import dto.MemberRole;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    private static String currentHost = "localhost";

    static {
        // 시작은 기존 db.properties (root) — 로그인 전 회원가입 등에 사용
        loadProperties("db/db.properties");
    }
    
    // 로그인 성공 후 role에 따라 DB 계정 전환
    public static void init(MemberRole role) {
        switch (role) {
            case ADMIN -> loadProperties("db/db_admin.properties");
            case USER  -> loadProperties("db/db_user.properties");
        }
    }
    
    public static void reset() {
        loadProperties("db/db.properties");
        // 아래 코드 삭제해도 됨 (loadProperties에서 처리)
        // if (!currentHost.equals("localhost")) {
        //     URL = URL.replace("localhost", currentHost);
        // }
    }
    
    

    private static void loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream is =
                DatabaseConnector.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                System.out.println(fileName + " 파일을 찾을 수 없습니다.");
                return;
            }
            props.load(is);
            URL      = props.getProperty("url");
            USER     = props.getProperty("user");
            PASSWORD = props.getProperty("password");
            
            // 추가: IP 바꿔놨으면 loadProperties 후에도 유지
            if (currentHost != null && !currentHost.equals("localhost")) {
                URL = URL.replace("localhost", currentHost);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 외부에서 인스턴스 생성 방지 (유틸 클래스)
    private DatabaseConnector() {}
    
    public static void setHost(String host) {
        currentHost = host;
        URL = URL.replace("localhost", host);
    }
    /*
    // Connection 객체 반환
    // 생성된 Connection은 DAOImpl 생성자 등에 전달하여 사용
    public static Connection getConnection() throws SQLException {
    	//System.out.println("현재 접속 계정: " + USER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }*/
    
    public static Connection getConnection() throws SQLException {
        System.out.println("URL = " + URL);
        System.out.println("USER = " + USER);
        System.out.println("PASSWORD = " + PASSWORD);

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}