# 26-1-DB-ConMayo
26년 1학기 데이터베이스 수업 공연예매 관리 프로젝트 

classDiagram
    direction TOP_DOWN

    class DBConnection {
        +Connection conn
        +getConnection() Connection
        +closeConnection() void
    }

    class MainApplication {
        +main(String[] args) void
    }

    %% Controller / View Layer
    namespace Controller_View_Layer {
        class MenuController {
            -Scanner scanner
            -UserService userService
            -RecipeService recipeService
            +startMenu() void
            -displayMainMenu() void
        }
    }

    %% Service Layer
    namespace Service_Layer {
        class UserService {
            -UserRepository userRepo
            +registerUser(UserDTO user) boolean
            +login(String id, String pw) UserDTO
        }
        class RecipeService {
            -RecipeRepository recipeRepo
            +getRecipeList() List~RecipeDTO~
            +searchRecipe(String keyword) List~RecipeDTO~
        }
    }

    %% Repository / Data Access Layer
    namespace Repository_Layer {
        class UserRepository {
            -DBConnection db
            +save(UserDTO user) int
            +findById(String id) UserDTO
        }
        class RecipeRepository {
            -DBConnection db
            +findAll() List~RecipeDTO~
            +findByKeyword(String keyword) List~RecipeDTO~
        }
    }

    %% DTO / Model Layer
    namespace Model_Layer {
        class UserDTO {
            +String userId
            +String password
            +String userName
        }
        class RecipeDTO {
            +int recipeId
            +String title
            +String ingredients
            +String steps
        }
    }

    %% Relationships
    MainApplication --> MenuController : 구동
    MenuController --> UserService : 사용자 비즈니스 로직 요청
    MenuController --> RecipeService : 레시피 비즈니스 로직 요청
    
    UserService --> UserRepository : DB 접근
    RecipeService --> RecipeRepository : DB 접근
    
    UserRepository --> DBConnection : Connection 활용
    RecipeRepository --> DBConnection : Connection 활용

    UserRepository ..> UserDTO : 데이터 전달
    RecipeRepository ..> RecipeDTO : 데이터 전달
    MenuController ..> UserDTO : 세션 유지
