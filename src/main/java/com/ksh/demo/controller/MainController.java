package com.ksh.demo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ksh.demo.model.BoardVO;
import com.ksh.demo.service.PostServiceImpl;

@Controller
public class MainController {
	
	@Autowired
	PostServiceImpl postService;

	/**
	 * @author 권소현
	 * @name 게시판 전체글 조회
	 * @return ModelAndView
	 */
	@RequestMapping("/")
	public ModelAndView mainList() {
		ModelAndView modelview = new ModelAndView();
		
		postService.createFolder();
		ArrayList<BoardVO> rows = postService.readPostSvc();
		System.out.println("전체리스트 : " + rows.toString());
		
		modelview.addObject("postList", rows);
		modelview.setViewName("mainList");
		return modelview;
	}
	
	/**
	 * @author 권소현
	 * @name 검색 조건에 맞게 해당글 조회
	 * @param searchOption - 검색 조건
	 * @param searchKeyword - 검색 키워드
	 * @return String
	 */
	@RequestMapping(value = "/searchList", method = RequestMethod.GET)
	public ModelAndView searchList(Model model,
            @RequestParam(value="searchOption", defaultValue="") String searchOption,
            @RequestParam(value="searchKeyword", defaultValue="") String searchKeyword) {
		
		ModelAndView modelview = new ModelAndView();
	    ArrayList<BoardVO> rows = new ArrayList<>();
	    if (!searchOption.equals("") && !searchKeyword.equals("")) {
	        rows = postService.searchPostSvc(searchOption, searchKeyword);
	    }
	    modelview.addObject("postList", rows);
		modelview.setViewName("mainList");
		
		return modelview;
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 작성 ▶ 사용자 닉네임 전달 로직
	 * @return String
	 */
	@RequestMapping("/insertForm")
	public String insertForm(Model model) {
		String nick = postService.nickSendSvc();
		model.addAttribute("boardNick", nick);
		return "insertForm";
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 작성 ▶ 글쓰기 내용 저장
	 * @return ModelAndView
	 */
	@RequestMapping("/insertList*")
	public ModelAndView insertList(BoardVO vo, Model model) {
		ModelAndView modelview = new ModelAndView();
		if(vo.getBoardIdx() == null){
			vo.setBoardIdx(1);
		}
		
		// ▶ 서비스단 호출
		//   → 기존 값 읽어온다. 왜? 인덱스 맨 끝 넣어주기 위함
		ArrayList<BoardVO> rows = postService.readPostSvc();
		
		if(rows.size() > 0) { // 게시글 번호 늘이기
			int postNumber = rows.size() + 1;
			vo.setBoardIdx(postNumber);
		}
		String data =  postService.createDataSet(vo);
		 
		// ▶ 서비스단 호출
		//   → 사용자가 입력한 새글 DB에 넣는다.
		postService.createPostSvc(data);
		 
		// ▶ 서비스단 호출
		//   → 지금 사용자가 새글 적은것까지 리스트에 보여져야 되니까 다시 가져온다.
		rows = postService.readPostSvc();
		 
		modelview.addObject("postList",rows);
		modelview.setViewName("mainList");
			 
		return modelview;
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 수정/조회 페이지
	 * @return ModelAndView
	 */
	@RequestMapping("/updateForm/{boardIdx}")
	public ModelAndView updateForm(@PathVariable("boardIdx") Integer boardIdx, BoardVO vo) {
		ModelAndView modelview = new ModelAndView();
		
		BoardVO rows = postService.readPostRowSvc(vo);
		modelview.addObject("postrows", rows);
		modelview.setViewName("updateForm");
		return modelview;
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 수정
	 * @return String
	 */
	@RequestMapping(value = "/updateList", method = RequestMethod.POST)
	public String updateList(BoardVO vo) {
		postService.updatePostRowSvc(vo);
		return "redirect:/";
	}
	
	/**
	 * @author 권소현
	 * @name 게시글 삭제
	 * @return String
	 */
	@RequestMapping("/deleteForm/{boardIdx}")
	public String deleteForm(@PathVariable("boardIdx") Integer boardIdx, BoardVO vo) {
		postService.deletePostRowSvc(vo);
		return "redirect:/";
	}
	
}