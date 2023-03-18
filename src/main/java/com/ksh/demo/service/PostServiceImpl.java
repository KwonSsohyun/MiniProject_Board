package com.ksh.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ksh.demo.model.BoardVO;

@Service
public class PostServiceImpl {
	
	static final String folderPath = "document";
	static final String filePath = "document/post.txt";
	
	// 폴더 상태 체크
	FolderStatus folderResult;
	// 텍스트 파일 상태 체크
	TextFileStatus fileResult;
	
	// ▶ 폴더 생성 되었는지 상태
	enum FolderStatus {
		EMPTY,		// 생성이 안되어 있는 default 상태
		ALREADY		// 이미 폴더 생성 되어있는 상태
	}

	// ▶ 텍스트 파일이 생성 되었는지 상태
	enum TextFileStatus {
		EMPTY,		// 생성이 안되어 있는 default 상태
		ALREADY		// 이미 파일이 생성 되어있는 상태
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 생성 함수
	 * @Param data - 글 데이터
	 * @return 게시글이 생성되어 있는지 아닌지 boolean값을 리턴
	 */
	public boolean createPostSvc(String data) {
		boolean result = false;
		TextFileStatus fileStatus = createTextFile();
		
		// ALREADY ▶ 이미 파일 생성되어있는 상태 
		if (fileStatus.equals(TextFileStatus.ALREADY)) {
			try {
				// → 생성할 파일의 경로 및 파일명 으로 File 객체 생성
				File file = new File(filePath);
			
				// → FileOutputStream : 데이터를 파일에 바이트 스트림으로 저장하기 위해 사용한다.
				// → FileOutputStream(String fileName, boolean append) : 기존 파일에 내용을 추가 할려면 두번째 인자로 true를 적어 준다. true를 추가해도 없으면 만든다.
				FileOutputStream f = new FileOutputStream(file, true);
				String lineToAppend = data + "\r\n";

				// → 문자열을 바이트배열로 변환해서 파일에 저장한다.
				byte[] byteArr = lineToAppend.getBytes();
				f.write(byteArr);
				
				// → 사용이 끝나면 파일 스트림을 닫는다.
				f.close();
				result = true;
	    	} catch (IOException e) {
		   		System.out.println(e + "텍스트 데이터 삽입 에러");
	    	}
		 }
		return result;
	}
	
	/**
	 * @author 권소현
	 * @name 게시판 전체글 조회(메인) - 리스트 가져옴
	 * @return ArrayList<BoardVO>
	 */
	public ArrayList<BoardVO> readPostSvc() { 
		
		ArrayList<BoardVO> rows = new ArrayList<>();
		
		TextFileStatus fileStatus = createTextFile();

		if (fileStatus.equals(TextFileStatus.ALREADY)) {
			// BufferedReader ▶ 버퍼를 이용한 입력
			// FileReader ▶ 텍스트 파일을 읽기 위한 것
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				
				String line;
				
				while ((line = br.readLine()) != null) {
					// ▶ 자바 split으로 "|" 문자 자르기
					//   → 해결방법은 "\\|" 로 자르는것이다.
					String[] temp = line.split("\\|");
					
					// ▶ VO객체의 boardIdx의 값이 null일 경우 분기처리
					Integer boardIdx = Integer.parseInt(temp[0]);
					
					// ▶ 객체 rows에 한줄한줄 읽어온거 넣는다.
					//   BoardVO(int boardIdx, String boardNick, String boardTitle, String boardText, String boardCheck) 해당 순서에 맞게 넣는다.
					//   Integer.parseInt(String s) ▶ 문자열을 숫자로 변환시킴
					rows.add(new BoardVO(boardIdx, temp[1], temp[2], temp[3], temp[4]));
				} 
			} catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		return rows;
	}
	
	/**
	 * @author 권소현
	 * @name 게시판 해당글 가져옴
	 * @return BoardVO
	 */
	public BoardVO readPostRowSvc(BoardVO vo) {
		
		BoardVO board = null;
		
		TextFileStatus fileStatus = createTextFile();

		if (fileStatus.equals(TextFileStatus.ALREADY)) {
			// BufferedReader ▶ 버퍼를 이용한 입력
			// FileReader ▶ 텍스트 파일을 읽기 위한 것
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				
				String line;
				
				while ((line = br.readLine()) != null) {
					// ▶ 자바 split으로 "|" 문자 자르기
					//   → 해결방법은 "\\|" 로 자르는것이다.
					String[] temp = line.split("\\|");
					
					// ▶ VO객체의 boardIdx의 값이 null일 경우 분기처리
					Integer boardIdx = Integer.parseInt(temp[0]);
					
	                if (boardIdx.equals(vo.getBoardIdx())) {
	                    board = new BoardVO(boardIdx, temp[1], temp[2], temp[3], temp[4]);
	                    break;
	                }
				} 
			} catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		return board;
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 작성 - 사용자 닉네임 전달
	 * @return String
	 */
	public String nickSendSvc() {
		Path filePath = Paths.get("document/post.txt");
	    List<String> lines;
	    
	    try {
	    	// Files.readAllLines ▶ 파일의 모든 줄을 읽어온다.
	        lines = Files.readAllLines(filePath, Charset.forName("UTF-8"));

	        if (lines.isEmpty()) {
	            return "";
	        } else {
	        	// lines.size() - 1 ▶ 파일에서 읽은 데이터의 마지막 줄을 읽기 위함이다.
	        	//                    ex) 인덱스는 0부터 세니까 | 2개가 담겨있으면 size는 2이다. | 2-1 하면 [1] 인 2번째 마지막 가져온다.
	            String lastLine = lines.get(lines.size() - 1);
	            String[] fields = lastLine.split("\\|");
	            return fields[1]; // 닉네임 넣어서 보낸다.
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 수정
	 */
	public void updatePostRowSvc(BoardVO vo) {
		
		// 기존에 저장되어 있는 게시글 파일을 읽어서 수정하고, 다시 파일에 쓰는 작업이 필요
		// StringBuilder와 BufferedReader, BufferedWriter를 사용하여 해당 작업을 수행하도록 코드를 작성
	    
	    File file = new File(filePath);
	    // 수정된 게시글을 저장할 StringBuilder 객체 sb를 선언
	    StringBuilder sb = new StringBuilder();
	    
	    // ▶ BufferedReader와 BufferedWriter 사용
	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	    	// BufferedReader를 사용하여 게시글 파일을 읽으면서, 기존 게시글에서 수정 대상 게시글을 찾으면 sb 객체에 수정된 게시글을 추가
	        String line;

	        while ((line = br.readLine()) != null) {
	            String[] temp = line.split("\\|");

	            Integer boardIdx = Integer.parseInt(temp[0]);
	           
	            if (boardIdx == vo.getBoardIdx()) {
	                sb.append(vo.getBoardIdx()).append("|")
	                  .append(vo.getBoardNick()).append("|")
	                  .append(vo.getBoardTitle()).append("|")
	                  .append(vo.getBoardText()).append("|")
	                  .append(vo.getBoardCheck()).append("\n");
	            } else {
	                sb.append(line).append("\n");
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
	        bw.write(sb.toString());
	        bw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 삭제
	 */
	public void deletePostRowSvc(BoardVO vo) {	
	    File file = new File(filePath);

	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        StringBuilder sb = new StringBuilder();
	        String line;
	        int deletedIdx = vo.getBoardIdx();
	        boolean deleted = false;

	        while ((line = br.readLine()) != null) {
	            String[] temp = line.split("\\|");
	            int boardIdx = Integer.parseInt(temp[0]);
	            if (boardIdx == deletedIdx) {
	                deleted = true;
	                continue;
	            }
	            if (deleted) {
	                boardIdx--;
	                temp[0] = String.valueOf(boardIdx);
	            }
	            sb.append(String.join("|", temp)).append("\n");
	        }

	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
	            bw.write(sb.toString());
	            bw.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 검색
	 * @param option - 검색 조건
	 * @param keyword - 검색 키워드
	 * @return ArrayList<BoardVO>
	 */
	public ArrayList<BoardVO> searchPostSvc(String option, String keyword) {

	    ArrayList<BoardVO> rows = new ArrayList<>();
	    
	    TextFileStatus fileStatus = createTextFile();
	    
	    if (fileStatus.equals(TextFileStatus.ALREADY)) {
	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] temp = line.split("\\|");
	                if (option.equals("타이틀")) { // 수정된 부분
	                    if (temp[2].contains(keyword)) { // 제목으로 검색
	                        rows.add(new BoardVO(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3], temp[4]));
	                    }
	                } else if (option.equals("닉네임")) {
	                    if (temp[1].contains(keyword)) { // 닉네임으로 검색
	                        rows.add(new BoardVO(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3], temp[4]));
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return rows;
	}
	
    /**
     * @author 권소현
     * @name 파일 생성 함수
     * @return 파일 생성 상태값 리턴
     */		
	public TextFileStatus createTextFile() {
		
		File file = new File(filePath);
        
        try {
            if (file.createNewFile()) {
                System.out.println("파일 생성");
                fileResult = TextFileStatus.ALREADY;
            } else {
                System.out.println("파일 이미 존재");
                fileResult = TextFileStatus.ALREADY;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return fileResult;
    }		
	
    /**
     * @author 권소현
     * @name 폴더 생성 함수
     * @return 폴더 생성 상태값 리턴
     */		
	public FolderStatus createFolder() {
		File Folder = new File(folderPath);

		// 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); //폴더 생성합니다.
			    System.out.println("폴더 생성");
			    folderResult = FolderStatus.ALREADY;
	        } catch(Exception e){
			    e.getStackTrace();
			}        
		} else {
			System.out.println("폴더 이미 존재");
			folderResult = FolderStatus.ALREADY;
		}
        return folderResult;
    }
	
    /**
     * @author 권소현
     * @name 한개의 데이터 바인딩 함수
     * @return 한개의 데이터 바인딩 스트링
     */	
	public String createDataSet(BoardVO vo) {
		return vo.getBoardIdx() + "|" + vo.getBoardNick() + "|" + vo.getBoardTitle() + "|" + vo.getBoardText() + "|" + vo.getBoardCheck();
	}
}
