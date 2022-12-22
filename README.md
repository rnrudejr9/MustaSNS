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
| join | POST | /api/v1/users/join | 회원가입기능 |
| login | POST | /api/v1/users/login | 로그인기능 |
| post | POST | /api/v1/posts | 글작성기능 |


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
- [ ] 게시글 CRUD
- [ ] 화면ui 설정
- [ ] admin 계정 구현
