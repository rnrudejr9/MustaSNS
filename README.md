# MustaSNS
* 🦁**멋사 아니라 mustache SNS**
* **멋쟁이사자처럼 백엔드스쿨 2기 개인프로젝트**

    * swagger주소 <br>
😊 t3(sub) : http://ec2-54-180-91-171.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/ <br>
😊 t3(main) :
    * ec2 주소
    

## ✏ ENDPOINT


|API 종류|HTTP|URI|API 설명|
|:-----:|:------------------:|:-----------------------------:|:-----------------------------:|
| <b>hello | GET | /api/v1/hello | testAPI return String |
| <span style="color:red"> users </span> | POST | /api/v1/users/join | 회원가입기능 |
| users | POST | /api/v1/users/login | 로그인기능 |
| posts | POST | /api/v1/posts | 글작성기능 |
| posts | PUT | /api/v1/posts/{id} | 글수정기능 |
| posts | DELETE | /api/v1/posts/{id} | 글삭제기능 |
| posts | GET | /api/v1/posts/{id} | 글조회기능 |
| posts | GET | /api/v1/posts | 글전체조회 |
| comment | POST | /api/v1/posts/{id}/comment | 댓글작성기능 |
| comment | PUT | /api/v1/posts/{postid}/comment/{id} | 댓글수정기능 |
| comment | DELETE | /api/v1/posts/{postid}/comment/{id} | 댓글삭제기능 |
| comment | GET | /api/v1/posts/{id}/comment | 댓글조회기능 |
| good |  |  | 좋아요기능 |



## ERD


## 개요 설명
<div align="center">
 <img src="https://img.shields.io/badge/SpringBoot-6DB33F.svg?logo=Spring-Boot&logoColor=white" />
 <img src="https://img.shields.io/badge/SpringSecurity-6DB33F.svg?logo=Spring-Security&logoColor=white" />
 <img src="https://img.shields.io/badge/MySQL-3776AB.svg?logo=MySql&logoColor=white" />
 <img src="https://img.shields.io/badge/Docker-2496ED.svg?logo=Docker&logoColor=white" />
 <img src="https://img.shields.io/badge/AmazonEC2-FF9900.svg?logo=Amazon-EC2&logoColor=white" />
</div>

* 에디터 : Intellij Ultimate
* 개발 툴 : SpringBoot 2.7.5
* 자바 : JAVA 11
* 빌드 : Gradle 6.8
* 서버 : AWS EC2
* 배포 : Docker, gitlab
* 데이터베이스 : MySql 8.0
* 필수 라이브러리 : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security
<hr>

- [x] 회원가입과 로그인
- [x] 게시글 CRUD
- [x] 댓글 기능 구현
- [ ] 좋아요 기능 구현
- [x] 테스트 코드 작성하기
- [ ] 화면ui 설정
- [ ] admin 권한 (Role 역할) 구현
