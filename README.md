# MustaSNS
* **멋쟁이사자처럼 백엔드스쿨 2기 개인프로젝트**

    * swagger주소 <br>
😊 t3(sub) : http://ec2-54-180-91-171.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/#/user-controller/loginUsingPOST <br>
😊 t3(main) :
    * ec2 주소
    

## ✏ ENDPOINT

|API 종류|HTTP|URI|API 설명|
|:-----:|:------------------:|:-----------------------------:|:-----------------------------:|
| hello | GET | /api/v1/hello | testAPI return bye |
| users | POST | /api/v1/users/join | 회원가입기능 |
| users | POST | /api/v1/users/login | 로그인기능 |
| posts | POST | /api/v1/posts | 글작성기능 |
| posts | PUT | /api/v1/posts/{id} | 글수정기능 |
| posts | DELETE | /api/v1/posts/{id} | 글삭제기능 |
| posts | GET | /api/v1/posts/{id} | 글조회기능 |
| posts | GET | /api/v1/posts | 글전체조회 |


## ERD


## 개요 설명
* 에디터 : Intellij Ultimate
* 개발 툴 : SpringBoot 2.7.5
* 자바 : JAVA 11
* 빌드 : Gradle 6.8
* 서버 : AWS EC2
* 배포 : Docker
* 데이터베이스 : MySql 8.0
* 필수 라이브러리 : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security
<hr>

- [x] 회원가입과 로그인
- [x] 게시글 CRUD
- [ ] 좋아요 기능 구현
- [ ] 화면ui 설정
- [ ] admin 계정 구현
