# board-multi-module
공통모듈 연습
---
#### spec
| SW                       | Version | File Name | 설명            |
| ------------------------ |-------| --------- | --------------- |
| JDK(Amazon Corretto JDK) | 21      |           | JDK             |
| springboot               | 3.3.9   |           |                 |
| git bash                 |         |           | git 이요가능 유닉스 sh |
| IntelliJ                 |         |           | IDE             |
| Notepad++                |         |           | 문서 편집        |
| DBeaver                  |         |           | SQL 클라이언트   |
| Postman                  |         |           | API test        |
| Everthing                |         |           | 파일 검색        |

#### dependencies:
- common:
  - org.apache.commons:commons-lang3:3.12.0                      // Apache Commons Lang 유틸
- repository:
  - org.springframework.boot:spring-boot-starter-jdbc            // jdbc spring 연
  - com.h2database:h2                                            // H2 DB
  - org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3    // mybatis
- service:
  - org.springframework.boot:spring-boot-starter                 // springCore(@Service)
- web:
  - org.springframework.boot:spring-boot-starter-web

```
board-multi-module
├─.gradle
│  ├─8.8
│  │  ├─checksums
│  │  ├─dependencies-accessors
│  │  ├─executionHistory
│  │  ├─expanded
│  │  ├─fileChanges
│  │  ├─fileHashes
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  └─vcs-1
├─.idea
├─build
│  └─tmp
│      └─bootJar
├─common
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─example
│      │  └─resources
│      └─test
│          ├─java
│          └─resources
├─gradle
│  └─wrapper
├─repository
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─example
│      │  └─resources
│      └─test
│          ├─java
│          └─resources
├─service
│  └─src
│      ├─main
│      │  ├─java
│      │  └─resources
│      └─test
│          ├─java
│          └─resources
├─src
│  ├─main
│  │  ├─java
│  │  └─resources
│  └─test
│      ├─java
│      └─resources
└─web
    └─src
        ├─main
        │  ├─java
        │  │  └─com
        │  │      └─example
        │  └─resources
        └─test
            ├─java
            └─resources
```
