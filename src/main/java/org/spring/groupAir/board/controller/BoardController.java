package org.spring.groupAir.board.controller;

import lombok.RequiredArgsConstructor;
import org.spring.groupAir.board.dto.BoardDto;
import org.spring.groupAir.board.dto.BoardReplyDto;
import org.spring.groupAir.board.dto.BoardSeparateDto;
import org.spring.groupAir.board.entity.BoardEntity;
import org.spring.groupAir.board.entity.BoardSeparateEntity;
import org.spring.groupAir.board.service.BoardService;
import org.spring.groupAir.board.service.ReplyService;
import org.spring.groupAir.config.MyUserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

  private final BoardService boardService;

  private final ReplyService boardReplyService;

  // id 값을 가져오면 넘어가야하는곳
// @GetMapping("/category/{id}")
//  public String getBoardBySeparate(@PathVariable Long id) {
//   boardService.findById(id);
//   return "board/boardList"; // 해당 카테고리에 넘어가게 하고
//  }
//
//  @GetMapping("/category/}")
//  public String<BoardSeparateEntity> getAllCategories() {
//   boardService.getAllCategories();
//  }


  // 나중에 지울거
  @GetMapping("/write")
  public String write(@AuthenticationPrincipal MyUserDetailsImpl myUserDetails, BoardDto boardDto, Model model) {


    model.addAttribute("memberId", myUserDetails.getMemberEntity().getId());
    model.addAttribute("boardDto", boardDto);




    //model.addAttribute("boardSeparateName",boardDto.getBoardSeparateEntity().getBoardSeparateName());

    return "board/write";
  }


  // 로그인 상태에서만 들어갈 수 있음
 /* @GetMapping("/write")         // 로그인 상태 정보
  public String write(BoardDto boardDto, Model model , @AuthenticationPrincipal MyUserDetailsImpl myUserDetails) {   //


    model.addAttribute( "memberId" , myUserDetails.getMemberEntity().getId());
    return "board/write";

  }*/

  @PostMapping("/write")
  public String writeOk(BoardDto boardDto) throws IOException {

    boardService.insertBoard(boardDto);

    return "redirect:/board/boardList";
  }


  // ----------------------------------------


  //  로그인 한 회원 만 들어 갈 수 있게 하는 거
/*  @GetMapping("/write")
  public String write(@AuthenticationPrincipal MyUserDetailsImpl myUserDetails,
                      BoardDto boardDto,
                      Model model){
    model.addAttribute("memberId"
        , myUserDetails.getMemberEntity().getId());

    return "board/write";
  }


  //  로그인 한 회원 만 들어 갈 수 있게 하는 거




  @PostMapping("/write") // 유효성 검사 폼 데이터를 처리 Java 객체에 매핑
  public String writeOk(@Valid BoardDto boardDto ,
                          BindingResult bindingResult){
    if (bindingResult.hasErrors()){
      return "board/write";
    }
    boardService.insertBoard(boardDto);

    return "redirect:/board/boardList";
  }*/




  @GetMapping("/boardList")
  public String boardList(@RequestParam(name = "subject", required = false) String subject,
                          @RequestParam(name = "search", required = false) String search,
                          @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC)
                          Pageable pageable, Model model) {


    Page<BoardDto> pagingList = boardService.boardSearchPagingList(pageable, subject, search);

    int totalPages = pagingList.getTotalPages(); // 전체
    int nowPage = pagingList.getNumber(); // 현재
    long totalElements = pagingList.getTotalElements(); // 전체 레코드
    int size = pagingList.getSize(); // 보이는 개수

    int blockNum = 3;  // 브라우저에 보이는 페이지 번호

    int startPage = (int) ((Math.floor(nowPage / blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(nowPage / blockNum) * blockNum) + 1 : totalPages);
    int endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("pagingList", pagingList);


    return "board/boardList";
  }


  @GetMapping("/detail/{id}")
  public String detail(@PathVariable("id") Long id, Model model, BoardSeparateEntity boardSeparateEntity) {

    // 조회 -> BoardEntity id -> 파일있을 경우 FileEntity newFIleName
    BoardDto board = boardService.detail(id);
    // "board"이름으로 조회한 게시글(파일이있으면 파일포함)를 저장

    // -- >board/detail1.html

    //게시글 존재하면 ->게시글에 연결된 덧글리스트
    List<BoardReplyDto> replyList = boardReplyService.replyList(board.getId());

    model.addAttribute("board", board);
    model.addAttribute("replyList", replyList);

//    model.addAttribute("name", boardSeparateEntity.getBoardSeparateName());


    return "board/detail";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id") Long id) {

    boardService.deleteBoard(id);

    return "redirect:/board/boardList";
  }



  @GetMapping("/Lists")
  public String getBoardsBySeparateId(@RequestParam("boardSeparateId") Long boardSeparateId, Model model) {

    List<BoardEntity> boards = boardService.getBoardsBySeparateId(boardSeparateId);


    model.addAttribute("boards", boards);


    return "board/boardsPage";
  }




}
