# 26-1-DB-ConMayo (콘마요)

26년 1학기 데이터베이스 수업 공연 예매 관리 프로젝트

## 🚀 시작하기 (JAR 파일로 실행)

### 1. 데이터베이스 설정

MariaDB/MySQL에 접속한 후, `sql/` 경로의 쿼리 파일을 아래 순서대로 실행한다.

| 순서 | 파일 | 설명 |
|------|------|------|
| 1 | `sql/schema.sql` | 데이터베이스 및 테이블 생성 |
| 2 | `sql/security.sql` | `conmayo_user`, `conmayo_admin` 계정 및 권한 생성 |
| 3 | `sql/db_objects.sql` | 함수/프로시저 등 DB 객체 생성 |
| 4 | `sql/trigger.sql` | 트리거 생성 |
| 5 | `sql/data_0610ver.sql` | 테스트 데이터 삽입 |

### 2. JAR 실행

```
java -jar ConMayo.jar
```

> ⚠️ **Windows 환경에서 한글이 깨지는 경우**
> 회원가입 시 이름/전화번호 등 한글 입력값이 깨져서 저장되는 경우, 인코딩 옵션을 추가하여 실행한다.
> ```
> java -Dfile.encoding=UTF-8 -jar ConMayo.jar
> ```

### 3. DB 연결 설정 (최초 1회)

JAR 최초 실행 시 DB 연결 설정 창이 자동으로 뜬다.

- **Host**: DB 서버 주소
  - 본인 로컬에 DB가 있다면 기본값 `localhost` 그대로 두면 된다.
  - 팀원 컴퓨터 등 다른 호스트의 DB에 접속하려면 해당 IP를 입력한다.
- **Port**: 기본값 `3306`
- **Root 비밀번호**: 본인 로컬 DB의 `root` 계정 비밀번호

입력 후 OK를 누르면 JAR 파일 위치에 `db/` 폴더가 생성되며, 이후 실행부터는 이 설정이 자동으로 재사용되어 설정 창이 다시 뜨지 않는다.

> `conmayo_user`, `conmayo_admin` 계정의 비밀번호는 `sql/security.sql`에 정의된 고정값이 자동으로 사용되므로 별도 입력이 필요 없다.

### 4. 로그인

DB 연결이 완료되면 로그인 화면이 뜬다. `sql/data.sql`에 포함된 테스트 계정으로 로그인하거나, 회원가입 후 이용할 수 있다.

---

## 🛠 이클립스로 실행하기 (개발용)

1. 깃허브에서 프로젝트 소스를 클론하고 이클립스로 import한다.
2. 위 "1. 데이터베이스 설정" 단계를 동일하게 진행한다.
3. `src/db/db.properties`, `db_user.properties`, `db_admin.properties`의 `url`을 본인 환경에 맞게 수정한다.
   ```
   url=jdbc:mysql://localhost:3306/conmayo
   ```
4. `src/main/Main.java`를 실행한다.

> 이클립스 실행 시에는 소스에 포함된 properties 파일을 그대로 사용하므로, 위 "3. DB 연결 설정" 다이얼로그는 뜨지 않는다.

---

## 📁 프로젝트 구조

```
src/
├── controller/   # View ↔ Service 연결, 화면 흐름 제어
├── dao/          # DAO 인터페이스
├── daoImpl/      # JDBC 기반 DAO 구현
├── db/           # DB 연결 관리 (DatabaseConnector, DBSetupDialog 등)
├── dto/          # 데이터 전송 객체
├── main/         # 진입점 (Main.java)
├── service/      # 비즈니스 로직
└── view/         # Swing UI

sql/
├── schema.sql
├── security.sql
├── db_objects.sql
├── trigger.sql
└── data.sql
```

---

## 👥 팀원 및 담당 모듈

| 담당자 | 모듈 |
|--------|------|
| 박남규 | 좌석(Seat), 리뷰(Review) |
| 강민지 | 회원(Member), 블랙리스트 |
| 이채빈 | 공연(Performance), 공연장(Venue) |
| 하나경 | 예매(Booking), 취소(Cancellation) |
