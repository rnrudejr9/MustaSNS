# 📑 MustaSNS
* 🦁**멋사 아니라 'musta'che 'SNS'** ..  ^_^
* **멋쟁이사자처럼 백엔드스쿨 2기 개인프로젝트**

##### <div align = "center" logoColor="green"> "로그인/회원가입, 게시글 CRUD, 댓글, 좋아요, 알림, 권한 기능이 있는 토이 프로젝트" </div>


> * DEMO 영상 : <a href="https://youtu.be/y4QBBqvU06o">유튜브 영상 주소</a>


> * swagger주소 😊 t3(swagger) : <a href="http://ec2-13-209-65-19.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/">swagger</a>
> * project UI 주소 <`진행중`> 😊 t3(view) : <a href="http://ec2-13-209-65-19.ap-northeast-2.compute.amazonaws.com:8080/view/v1/home/">DEMO</a>
    
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
* **필수 라이브러리** : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security, thymeleaf


<br>

## 🎨 진행과정

- [x] gitlab 배포파일 및 ec2 크론탭 설정
- [x] swagger 문서화 설정
- [x] 회원가입과 로그인  
- [x] 게시글 CRUD 구현
- [x] 댓글 CRUD 구현
- [x] 좋아요 기능 구현
- [x] Controller 테스트(User,Post,Comment)
- [x] Service 테스트(User,Post,Comment)
- [x] 부가기능 테스트(알림, 좋아요, 권한) 작성 
<hr>

- [x] 마이피드 기능 구현
- [x] 알람 기능 구현
- [x] admin 권한 (Role 역할) 구현 및 ADMIN 권한 부여
- [x] soft delete 구현 (SQLDelete, where 활용)
- [X] 소스코드 리펙토링 (간결화, 효율성 참고) -> validateCode, Controller, Service 완료
<hr>

- [x] UI : 화면 설정 (타임리프 템플릿 사용)
- [x] UI : 게시글 CRUD 구현 (admin CRUD 가능)
- [x] UI : 댓글 CRUD 구현
- [x] UI : 좋아요 기능 구현
- [x] UI : 마이피드 기능 구현
- [x] UI : 알람 기능 구현
- [x] UI : admin 계정 user 데이터 관리(user <-> admin)
- [ ] UI : admin 계정 user 데이터 CRUD
- [ ] UI : 화면에러처리
- [ ] UI : react 적용
- [x] UI : 웹 소켓 사용 간단 채팅방 구현

> * 진행 간 발생했던 이슈 : https://gitlab.com/rnrudejr9/mustasns/-/issues/?sort=created_date&state=all&first_page_size=20

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
| `my` | GET | /api/v1/posts/my | 마이피드조회기능 |
| `alarm` | GET | /api/v1/users/alarm | 알람조회기능 |
| `view` | GET | /view/v1/home | UI 메인화면 |
| `chat` | GET | /chat | UI 채팅방 |

<br>

## 🕹 핵심 로직


### UI 알림기능
- `HandlerInterceptor` `postHandle()` -> `view` 생성되기 이전에 실행
- 컨트롤러의 헨들러를 호출하기 전과 후에 요청과 응답을 참조하거나 가공할 수 있는 필터를 사용해 알림처리
- 로그인한 사용자는 페이지 접속할때마다 `postHandle()` 동작
- `alarm repository` 의 `read`하지 않은 데이터가 있으면 채워진 파란색 종
- 데이터가 없을 경우 빈 모양의 종
- `WebConfig` `addInterceptors()` 통해 uri에 따라 제외, 추가 함
- 관리자모드 배너도 위와같은 로직으로 구현함

### InValidChecker
- 로직 설정 간 자주사용되는 코드들을 하나의 클래스 안 메소드로 구현해 `리펙토링`
- 자주 사용하는 메소드들을 하나의 클래스에서 관리함 (`findByUserName`, `findById` 등)
- 코드 간결해지고 로직을 쉽게 이해할 수 있었음

### login 설정 및 유지상태 확인
- 세션기반 로그인 `setAttribute()`, `getAttribute()` 활용
- http세션에 요청값에 jwt 토큰에 대해서 `attribute` 를 추가함
- 세션은 30분간 유지하며 또는 브라우저 종료시 `invalidate`
- 로그인이 필요한 상황일때 `getAttribute` 하여 `Authetication` 을 받아냄



<br>

## 📺 UI 설정 내용

#### AnonymousUser
- 게시글 조회
- 댓글 조회
- 로그인
- 회원가입

#### USER
- 게시글 CRUD (작성자와 일치시 수정 삭제 가능)
- 댓글 CRUD (작성자와 일치시 수정 삭제 가능)
- 좋아요, 알림, 마이피드 기능

#### ADMIN
- 모든 게시글 및 댓글 CRUD 가능
- 좋아요, 알림, 마이피드 기능
- 회원 권한 관리 (user <-> admin)

> http://ec2-13-209-65-19.ap-northeast-2.compute.amazonaws.com:8080/view/v1/home/ 

## 📢 특이사항

##### 공부해야될것

* JWT, 인증인가 메소드, filterChain 과정 이해하기
* 테스트코드 완벽하게 이해하기

##### 22.12.27 1차 제출) 이후 리펙토링 간 해야될 내용들

* `DTO`를 조금더 활용하고 `ENTITY`에서 `DTO`를 추출하는 방식을 사용해야되겠다. (`entity class` 안 `static` 메소드 활용)
* `UI`를 정말 개발해보고 싶었는데, 야속한 시간 덕분에 추후 `thymeleaf` 템플릿을 활용해서 마무리 할 예정이다.
* `Alarm` 기능에 관련해서 `UI` 부분과 병행해서 진행하려고 하여 `js`의 비동기 방식에 대해 준비할 예정이다.
* `Service` 단 비즈니스 로직 자체가 에러를 처리하기 위한 중복되는 코드들이 많다. 해당 부분들을 **간소화**하고 효율적으로 정리해야되겠다 생각이 들었다.
* 마지막으로, 구글링을 통한 다양한 **이슈해결**과 짧은 시간에 계획대로 토이 프로젝트를 진행 할 수 있어서 개발에 좋은 경험이 되었다.

##### 23.01.10 2차 제출) 회고

* 댓글이나 알람 방식을 `실시간`으로 처리 요망
* 세션 만료시간에 refresh 하는 방법
* 화면 에러처리 관련한 로직을 새롭게 리펙토링

<details>
<summary>테스트 코드</summary>

<!-- summary 아래 한칸 공백 두어야함 -->
`TEST 컨트롤러`
<br>

*    회원가입 성공
*    회원가입 실패 - userName중복인 경우
*    로그인 성공
*    로그인 실패 - userName없음
*    로그인 실패 - password틀림
*    권한 변경 성공
*    권한 변경 실패 - admin 아닐 경우
*    알람 목록 조회 성공
*    알람 목록 조회 실패 - 로그인하지 않은 경우
<br>

*    댓글 작성 성공
*    댓글 작성 실패 : 로그인 하지 않음
*    댓글 작성 실패 : 게시글 존재하지 않음
*    댓글 수정 성공
*    댓글 수정 실패 : 로그인 하지 않음
*    댓글 수정 실패 : 게시글 존재하지 않음
*    댓글 수정 실패 : 작성자 불일치
*    댓글 수정 실패 : 데이터베이스 에러
*    댓글 삭제 성공
*    댓글 삭제 실패 : 로그인 하지 않음
*    댓글 삭제 실패 : 게시글 존재하지 않음
*    댓글 삭제 실패 : 작성자 불일치
*    댓글 삭제 실패 : 데이터베이스 에러
*    댓글 조회 성공
<br>

*    좋아요 누르기 성공
*    좋아요 누르기 실패(1) - 로그인 하지 않은 경우
*    좋아요 누르기 실패(2) - 해당 Post가 없는 경우
<br>

*    조회 성공) 게시글 1개 조회
*    조회 실패) 로그인 하지 않음
*    조회 성공) 전체 조회
*    작성 성공) 게시글 1개 작성
*    작성 실패) jwt 토큰값 없음
*    작성 실패) 올바르지않는 토큰값
*    수정 성공) 게시글 1개 수정
*    수정 실패) 로그인 하지 않음
*    수정 실패) 게시글 없음
*    수정 실패) 작성자와 사용자가 다름
*    수정 실패) 데이터베이스 오류
*    삭제 성공) 게시글 1개 삭제
*    삭제 실패) 로그인 하지 않음
*    삭제 실패) 게시글 없음
*    삭제 실패) 작성자와 사용자가 다름
*    삭제 실패) 데이터베이스 오류
 <br>

*    마이피드 조회 성공
*    마이피드 조회 실패 : 로그인 안함
 <br>

`TEST 서비스`
 <br>



*    로그인 실패 유저 없을 경우
*    로그인 실패 비밀번호 다를 경우
*    회원가입 성공
*    회원가입 실패 유저 중복
 <br>

*    게시글 조회 성공)
*    게시글 등록 성공)
*    게시글 등록 실패) 유저존재하지않음
*    게시글 삭제 성공)
*    게시글 삭제 실패) 유저 존재하지 않음
*    게시글 삭제 실패) 삭제할 게시글이 없음
*    게시글 삭제 실패) 삭제할 게시글의 작성자와 사용자가 다름
*    게시글 수정 성공)
*    게시글 수정 실패) 유저 존재하지 않음
*    게시글 수정 실패) 수정할 게시글이 없음
*    게시글 수정 실패) 수정할 게시글의 작성자와 사용자가 다름
 <br>

*    댓글 수정 실패 포스트 없음
*    댓글 수정 댓글이 없음
*    댓글 수정 작성자 다름
*    댓글 수정 수정완료
*    댓글 삭제 실패 포스트 없음
*    댓글 삭제 댓글이 없음
*    댓글 삭제 작성자 다름
*    댓글 삭제 삭제완료
 <br>

</details>


<br>

## 🚀 ERD 및 프로그램 사진


![image](https://user-images.githubusercontent.com/49141751/211468923-203c8315-362d-43ed-aa70-2737f4d02c8d.png)

![캡처](https://user-images.githubusercontent.com/49141751/211503666-143c8772-894e-46cd-8ce3-84384d754cc2.PNG)

![디테일포스트](https://user-images.githubusercontent.com/49141751/211503705-4b258812-f2a4-4661-9f83-3adfcba60fdb.PNG)
