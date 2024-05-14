package org.spring.groupAir.board.service.serviceInterface;

import org.spring.groupAir.board.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardServiceInterface {
  void insertBoard(BoardDto boardDto);


  Page<BoardDto> boardSearchPagingList(Pageable pageable, String subject, String search);

  BoardDto detail(Long id);
}
