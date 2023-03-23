# Verification Backed

# Introduction

This is the back-end system of the circuit verification software. The project is powered by Spring Boot.

# Configuration and Usages

Clone the repository:

```shell
git clone https://github.com/donald-trump-official/webeditor-backend
cd webeditor-backend
```

Create `application.properties` file:

```shell
cd src/main
mkdir resources
cd resources
type nul>application.properties		# For Windows users
touch application.properties		# Fot Linux users
```

This a sample of `application.properties`:

```
spring.datasource.username=<database_username>
spring.datasource.password=<databse_password>
spring.datasource.url=jdbc:mysql://<database_url>:<database_port>/<schema_name>?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

Make sure all the dependencies of Maven are installed and the project should be able to run.

# Details

## File Structure



## Server



## Verification Algorithm

See <a href='./doc/README-Verification-CN.md'>Verification Algorithm Document</a> (Chinese version)

# Update Logs

## 2023.03.23

- Re-structure source code files
- Integrate verification algorithm into this project
- Add `VerificationService` and `VerificationController`

## 2023.02.19

- Add MySQL support

# License

This project is protected by <a href='https://www.gnu.org/licenses/gpl-3.0.html'>GNU GPLv3</a>.