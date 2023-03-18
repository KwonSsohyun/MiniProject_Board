package com.ksh.demo.model;

public class BoardVO {
	
	// 인덱스
	private Integer boardIdx;
	
	// 닉네임
	private String boardNick;
	
	// 타이틀
	private String boardTitle;
	
	// 본문
	private String boardText;
	
	// 중요 게시물 체크
	private String boardCheck;
	
	
	public BoardVO(Integer boardIdx, String boardNick, String boardTitle, String boardText, String boardCheck) {
		this.boardIdx = boardIdx;
		this.boardNick = boardNick;
		this.boardTitle = boardTitle;
		this.boardText = boardText;
		this.boardCheck = boardCheck;
	}


	public Integer getBoardIdx() {
		return boardIdx;
	}


	public void setBoardIdx(Integer boardIdx) {
		this.boardIdx = boardIdx;
	}


	public String getBoardNick() {
		return boardNick;
	}


	public void setBoardNick(String boardNick) {
		this.boardNick = boardNick;
	}


	public String getBoardTitle() {
		return boardTitle;
	}


	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}


	public String getBoardText() {
		return boardText;
	}


	public void setBoardText(String boardText) {
		this.boardText = boardText;
	}


	public String getBoardCheck() {
		return boardCheck;
	}


	public void setBoardCheck(String boardCheck) {
		this.boardCheck = boardCheck;
	}
	
	
    @Override
    public String toString() {
        return "Board{" +
                "boardIdx=" + boardIdx +
                ", boardNick='" + boardNick + '\'' +
                ", boardTitle='" + boardTitle + '\'' +
                ", boardText='" + boardText + '\'' +
                ", boardCheck='" + boardCheck + '\'' +
                '}';
    }
	
}
