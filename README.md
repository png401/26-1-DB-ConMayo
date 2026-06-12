# 26-1-DB-ConMayo (콘마요)

26년 1학기 데이터베이스 수업 공연 예매 관리 프로젝트입니다.

## ✨ 소개

콘마요는 공연/좌석/예매/리뷰를 관리하는 통합 예매 시스템입니다.

- **좌석별 평점 시각화**: 같은 자리를 다른 공연에서도 이용한 관람객들의 리뷰 평점을 좌석 단위로 집계하여, 좌석 선택 화면에서 평점에 따라 색상으로 구분해 보여줍니다. 시야 좋은 자리를 직관적으로 확인할 수 있습니다.
- **동시 예매 충돌 방지**: 같은 좌석에 여러 사람이 동시에 예매를 시도해도 `SELECT ... FOR UPDATE` 기반의 락 처리로 중복 예매를 방지합니다.
- **자동 블랙리스트**: 7일 내 3회 이상 예매를 취소하면 트리거를 통해 자동으로 블랙리스트에 등록되어 일정 기간 예매가 제한됩니다.
- **리뷰 작성 제한**: 실제 예매·관람한 공연에 한해, 공연 시작 이후에만 리뷰를 작성할 수 있도록 트리거로 검증합니다.

## 🚀 시작하기 (JAR 파일로 실행)

### 1. 데이터베이스 설정

MariaDB/MySQL에 접속한 후, `sql/` 경로의 쿼리 파일을 아래 순서대로 실행해주세요.

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
> 회원가입 시 이름/전화번호 등 한글 입력값이 깨져서 저장된다면, 인코딩 옵션을 추가하여 실행해주세요.
> ```
> java -Dfile.encoding=UTF-8 -jar ConMayo.jar
> ```

### 3. DB 연결 설정 (최초 1회)

JAR을 처음 실행하면 DB 연결 설정 창이 자동으로 뜹니다.

- **Host**: DB 서버 주소
  - 본인 로컬에 DB가 있다면 기본값 `localhost`를 그대로 두면 됩니다.
  - 팀원 컴퓨터 등 다른 호스트의 DB에 접속하려면 해당 IP를 입력해주세요.
- **Port**: 기본값 `3306`
- **Root 비밀번호**: 본인 로컬 DB의 `root` 계정 비밀번호

입력 후 OK를 누르면 JAR 파일 위치에 `db/` 폴더가 생성되며, 이후 실행부터는 이 설정이 자동으로 재사용되어 설정 창이 다시 뜨지 않습니다.

> `conmayo_user`, `conmayo_admin` 계정의 비밀번호는 `sql/security.sql`에 정의된 고정값이 자동으로 사용되므로 별도 입력이 필요 없습니다.

### 4. 로그인

DB 연결이 완료되면 로그인 화면이 뜹니다. 테스트 데이터에 포함된 계정으로 로그인하거나, 회원가입 후 이용할 수 있습니다.

---

## 🛠 이클립스로 실행하기 (개발용)

1. 깃허브에서 프로젝트 소스를 클론하고 이클립스로 import해주세요.
2. 위 "1. 데이터베이스 설정" 단계를 동일하게 진행해주세요.
3. `src/db/db.properties`, `db_user.properties`, `db_admin.properties`의 `url`을 본인 환경에 맞게 수정해주세요.
   ```
   url=jdbc:mysql://localhost:3306/conmayo
   ```
4. `src/main/Main.java`를 실행해주세요.

> 이클립스에서 실행할 때는 소스에 포함된 properties 파일을 그대로 사용하므로, 위 "3. DB 연결 설정" 다이얼로그는 뜨지 않습니다.

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
└── data_0610ver.sql
```

---

## 👥 팀원 및 담당 모듈

| 담당자 | 모듈 |
|--------|------|
| 박남규 | 좌석(Seat), 리뷰(Review) |
| 강민지 | 회원(Member), 블랙리스트 |
| 이채빈 | 공연(Performance), 공연장(Venue) |
| 하나경 | 예매(Booking), 취소(Cancellation) |
