package db;

import dto.MemberRole;

import java.io.*;
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

    // 로그아웃 시 다시 기본 계정으로 복귀
    public static void reset() {
        loadProperties("db/db.properties");
        // 아래 코드 삭제해도 됨 (loadProperties에서 처리)
        // if (!currentHost.equals("localhost")) {
        //     URL = URL.replace("localhost", currentHost);
        // }
    }

    // 외부 properties 파일 없으면 setup 필요
    public static boolean needsSetup() {
        return !new File("db/db.properties").exists();
    }

    // Main에서 DBSetupDialog 입력값 받아 호출 — properties 3개 생성 후 로드
    public static void setup(String host, String port, String password) {
        currentHost = host;
        String baseUrl = "jdbc:mysql://" + host + ":" + port + "/conmayo";
        try {
            new File("db").mkdirs();
            writeProperties("db/db.properties",       baseUrl, "root",          password);
            writeProperties("db/db_user.properties",  baseUrl, "conmayo_user",  "user_pw123");
            writeProperties("db/db_admin.properties", baseUrl, "conmayo_admin", "admin_pw123");
        } catch (IOException e) {
            throw new RuntimeException("DB 설정 파일 저장 실패: " + e.getMessage(), e);
        }
        loadProperties("db/db.properties");
    }

    private static void loadProperties(String fileName) {
        Properties props = new Properties();

        // 외부 파일 우선, 없으면 classpath(JAR 내부) fallback
        File externalFile = new File(fileName);
        try (InputStream is = externalFile.exists()
                ? new FileInputStream(externalFile)
                : DatabaseConnector.class.getClassLoader().getResourceAsStream(fileName)) {
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

    private static void writeProperties(String path, String url, String user, String password) throws IOException {
        Properties props = new Properties();
        props.setProperty("url", url);
        props.setProperty("user", user);
        props.setProperty("password", password);
        try (OutputStream os = new FileOutputStream(path)) {
            props.store(os, null);
        }
    }

    // 외부에서 인스턴스 생성 방지 (유틸 클래스)
    private DatabaseConnector() {}

    public static void setHost(String host) {
        currentHost = host;
        URL = URL.replace("localhost", host);
    }

    // Connection 객체 반환
    // 생성된 Connection은 DAOImpl 생성자 등에 전달하여 사용
    public static Connection getConnection() throws SQLException {
        //System.out.println("현재 접속 계정: " + USER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}