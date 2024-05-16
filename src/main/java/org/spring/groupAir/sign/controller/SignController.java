package org.spring.groupAir.sign.controller;

import lombok.RequiredArgsConstructor;
import org.spring.groupAir.sign.dto.SignDto;
import org.spring.groupAir.sign.service.SignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final SignService signService;

    @GetMapping({"", "/index"})
    public String index() {

        return "sign/index";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("signDto", new SignDto());
        return "sign/write";
    }

    @PostMapping("/write")
    public String writeOk(@Valid SignDto signDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "sign/write";
        }
        signService.write(signDto);
        return "redirect:/sign/signList";

    }

    @GetMapping("/signList")
    public String signList(@PageableDefault(page=0 , size=8, sort = "id" ,direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "subject",required = false ) String subject,
                            @RequestParam(name = "search" ,required = false) String search,
                            Model model) {

       Page<SignDto> pagingList = signService.signSearchPagingList(pageable,subject,search);

       int totalPages = pagingList.getTotalPages();
       int newPage = pagingList.getNumber();
       long totalElements = pagingList.getTotalElements();
       int size = pagingList.getSize();

       int blockNum = 3;
        int startPage = Math.max(1, Math.min(newPage / blockNum * blockNum + 1, totalPages));


        int endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;


        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pagingList", pagingList);


        return "sign/signList";
    }


}















