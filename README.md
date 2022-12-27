# 📑 MustaSNS
* 🦁**멋사 아니라 'musta'che 'SNS'** ..  ^_^
* **멋쟁이사자처럼 백엔드스쿨 2기 개인프로젝트**

##### <div align = "center" logoColor="green"> "로그인/회원가입, 게시글 CRUD, 댓글, 좋아요, 알림, 권한 기능이 있는 토이 프로젝트" </div>


> * swagger주소 😊 t3(swagger) : http://ec2-54-180-91-171.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/ 
    
<br>


## ✏ 개요 설명
<div align="center">
 <img src="https://img.shields.io/badge/SpringBoot-6DB33F.svg?logo=Spring-Boot&logoColor=white" />
 <img src="https://img.shields.io/badge/SpringSecurity-6DB33F.svg?logo=Spring-Security&logoColor=white" />
 <img src="https://img.shields.io/badge/MySQL-3776AB.svg?logo=MySql&logoColor=white" />
 <img src="https://img.shields.io/badge/Docker-2496ED.svg?logo=Docker&logoColor=white" />
 <img src="https://img.shields.io/badge/AmazonEC2-FF9900.svg?logo=Amazon-EC2&logoColor=white" />
</div>

* **에디터** : Intellij Ultimate
* **개발 툴** : SpringBoot 2.7.5
* **자바** : JAVA 11
* **빌드** : Gradle 6.8
* **서버** : AWS EC2
* **배포** : Docker, gitlab
* **데이터베이스** : MySql 8.0
* **필수 라이브러리** : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security

<br>

## 🎨 진행과정

- [x] gitlab 배포파일 및 ec2 크론탭 설정
- [x] swagger 문서화 설정
- [x] 회원가입과 로그인
- [x] 게시글 CRUD
- [x] 댓글 기능 구현
- [x] 좋아요 기능 구현
- [x] 테스트 코드 작성하기 
- [ ] 알람 기능 구현
- [x] admin 권한 (Role 역할) 구현
- [ ] 화면ui 설정
- [ ] 마이페이지 기능 구현


<br>

## 🎯 ENDPOINT


|API 종류|HTTP|URI|API 설명|
|:-----:|:------------------:|:-----------------------------:|:-----------------------------:|
| `hello` | GET | /api/v1/hello | testAPI return String |
| `users` | POST | /api/v1/users/join | 회원가입기능 |
| `users` | POST | /api/v1/users/login | 로그인기능 |
| `posts` | POST | /api/v1/posts | 글작성기능 |
| `posts` | PUT | /api/v1/posts/{id} | 글수정기능 |
| `posts` | DELETE | /api/v1/posts/{id} | 글삭제기능 |
| `posts` | GET | /api/v1/posts/{id} | 글조회기능 |
| `posts` | GET | /api/v1/posts | 글전체조회 |
| `comment` | POST | /api/v1/posts/{id}/comment | 댓글작성기능 |
| `comment` | PUT | /api/v1/posts/{postid}/comment/{id} | 댓글수정기능 |
| `comment` | DELETE | /api/v1/posts/{postid}/comment/{id} | 댓글삭제기능 |
| `comment` | GET | /api/v1/posts/{id}/comment | 댓글조회기능 |
| `good` | POST | /api/v1/posts/{id}/likes | 좋아요+취소기능 |
| `good` | GET | /api/v1/posts/{id}/likes | 좋아요조회기능 |
| `users` | POST | /api/v1/users/{id}/role/change | 사용자권한변경기능 |

<br>

## ERD

![image](https://user-images.githubusercontent.com/49141751/209630586-be6fa917-368e-45c0-9a3b-d0713e9ace80.png)
