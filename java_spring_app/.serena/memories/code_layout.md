# Code Layout

## Configuration
- `src/main/java/com/example/its/config/SecurityConfig.java` - Spring Security 6 configuration with SecurityFilterChain pattern

## Domain Layer
### Authentication
- `src/main/java/com/example/its/domain/auth/User.java` - User entity for authentication
- `src/main/java/com/example/its/domain/auth/UserRepository.java` - MyBatis repository for user data access
- `src/main/java/com/example/its/domain/auth/CustomUserDetails.java` - UserDetails implementation for Spring Security
- `src/main/java/com/example/its/domain/auth/CustomUserDetailsService.java` - UserDetailsService implementation for user authentication

### Issue Management
- `src/main/java/com/example/its/domain/issue/IssueEntity.java` - Issue entity representing a single issue/task
- `src/main/java/com/example/its/domain/issue/IssueRepository.java` - MyBatis repository for issue data access
- `src/main/java/com/example/its/domain/issue/IssueService.java` - Business logic layer for issue management

## Web Layer
- `src/main/java/com/example/its/web/IndexController.java` - Controller for home page rendering
- `src/main/java/com/example/its/web/issue/IssueController.java` - Controller for issue CRUD operations
- `src/main/java/com/example/its/web/issue/IssueForm.java` - Form bean for issue input validation with Jakarta Bean Validation

## Application Entry Point
- `src/main/java/com/example/its/ItsApplication.java` - Spring Boot application main class

## Tests
- `src/test/java/com/example/its/ItsApplicationTests.java` - Basic Spring Boot context loading test

## Build & Dependencies
- `build.gradle` - Gradle build configuration with Spring Boot 3.2.1, Java 21, MyBatis 3.0.3
- `gradle/wrapper/gradle-wrapper.properties` - Gradle 8.5 wrapper configuration

## Documentation
- `README.md` - Comprehensive setup guide for team members
- `CLAUDE.md` - AI development assistance guidelines and coding standards
- `doc/SecurityConfig-Migration-Guide.md` - Detailed guide for Spring Security 5 to 6 migration

## Resources
- `src/main/resources/application.properties` - Application configuration
- `src/main/resources/schema.sql` - Database schema initialization
- `src/main/resources/data.sql` - Initial data loading
- `src/main/resources/templates/` - Thymeleaf HTML templates
