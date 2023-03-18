# 📋 Spring Boot 게시판 구현

## 프로젝트 설명
1. 자바 파일입출력을 이용하여 로컬 영역의 게시판 제작 (DB연동X)
2. 게시글 작성/조회/수정/삭제 구현 (CRUD)
3. 게시글 작성 - 닉네임 노출 (글쓰기 작성 시 작성자 닉네임 자동 insert)
4. 게시글 목록 - 중요 게시물로 표시 (중요게시글 BG-Color 적용)
5. 게시글 검색 - 타이틀 / 닉네임 검색 기능


## 기술 스택
- 개발언어 : JAVA<br>
- 프레임워크 : Spring Boot<br>
- IDE : STS(Spring Tool Suite)<br>
- View Template : Thymeleaf<br>


## 개발 순서
1. 개발 환경 세팅
2. 프로젝트 생성
3. 게시글 목록
4. 게시글 작성
5. 게시글 조회/수정
6. 게시글 삭제
7. 게시글 검색


## 디렉토리 구조
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

## 실행방법
