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
	

	FolderStatus folderResult;

	TextFileStatus fileResult;
	
	// ▶ 폴더 생성 되었는지 상태
	enum FolderStatus {
		EMPTY,		
		ALREADY		
	}

	// ▶ 텍스트 파일이 생성 되었는지 상태
	enum TextFileStatus {
		EMPTY,	
		ALREADY	
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
		
		if (fileStatus.equals(TextFileStatus.ALREADY)) {
			try {
				File file = new File(filePath);
							
				FileOutputStream f = new FileOutputStream(file, true);
				String lineToAppend = data + "\r\n";

				byte[] byteArr = lineToAppend.getBytes();
				f.write(byteArr);
				
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
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] temp = line.split("\\|");
					Integer boardIdx = Integer.parseInt(temp[0]);
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
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				
				String line;
				
				while ((line = br.readLine()) != null) {
					String[] temp = line.split("\\|");
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
	        lines = Files.readAllLines(filePath, Charset.forName("UTF-8"));

	        if (lines.isEmpty()) {
	            return "";
	        } else {
	            String lastLine = lines.get(lines.size() - 1);
	            String[] fields = lastLine.split("\\|");
	            return fields[1];
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
	    File file = new File(filePath);
	    StringBuilder sb = new StringBuilder();
	    
	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
	                if (option.equals("타이틀")) { 
	                    if (temp[2].contains(keyword)) { 
	                        rows.add(new BoardVO(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3], temp[4]));
	                    }
	                } else if (option.equals("닉네임")) {
	                    if (temp[1].contains(keyword)) { 
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

		if (!Folder.exists()) {
			try{
			    Folder.mkdir();
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
