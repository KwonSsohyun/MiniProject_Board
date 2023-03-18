# 📋 Spring Boot 게시판 구현
<br>

## 📍 프로젝트 설명
1. 자바 파일입출력을 이용하여 로컬 영역의 게시판 제작 (DB연동X)
2. 게시글 작성/조회/수정/삭제 구현 (CRUD)
3. 게시글 작성 - 닉네임 노출 (글쓰기 작성 시 작성자 닉네임 자동 insert)
4. 게시글 목록 - 중요 게시물로 표시 (중요게시글 BG-Color 적용)
5. 게시글 검색 - 타이틀 / 닉네임 검색 기능

<br>

## 📌 기술 스택
- 개발언어 : JAVA<br>
- 프레임워크 : Spring Boot<br>
- IDE : STS(Spring Tool Suite)<br>
- View Template : Thymeleaf<br>

<br>

## 📆 개발 순서
1. 개발 환경 세팅
2. 프로젝트 생성
3. 게시글 목록
4. 게시글 작성
5. 게시글 조회/수정
6. 게시글 삭제
7. 게시글 검색

<br>

## 🗂️ 디렉토리 구조
```
├── src/main/java
│   ├── com.ksh.demo
│   │   ├── controller
│   │   │   └── MainController.java
│   │   ├── model
│   │   │   └── BoardVO.java
│   │   └── service
│   └──     └── PostServiceImpl.java
│    
└── src/main/resources
    ├── templates
    │   ├── mainList.html
    │   ├── insertForm.html
    └── └── updateForm.html
```

---

## 🌏 프로젝트 실행방법
#### ✅ 1. Maven 빌드 환경에 Java로 작성된 SpringBoot 애플리케이션 실행
###### 프로젝트를 마우스 우클릭하여 Run As > Spring Boot App을 클릭 (Alt + Shift + X 버튼을 누른 후 B버튼)

<img width="65%" src="https://user-images.githubusercontent.com/90014589/226075737-211f9a4c-787e-48a8-92f1-e0e2a793fb71.png"/>
<br>

#### ✅ 2. 접속 ▶ http://localhost:8080/
<br>
<img width="65%" src="https://user-images.githubusercontent.com/90014589/226076303-908fadde-1725-4235-a3a5-9c19e47e03d5.png"/>

<br>
