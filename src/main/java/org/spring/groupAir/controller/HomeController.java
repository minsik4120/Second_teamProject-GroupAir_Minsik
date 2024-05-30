package org.spring.groupAir.controller;

import lombok.RequiredArgsConstructor;
import org.spring.groupAir.airplane.service.AirplaneService;
import org.spring.groupAir.board.service.BoardService;
import org.spring.groupAir.commute.dto.CommuteDto;
import org.spring.groupAir.commute.dto.VacationDto;
import org.spring.groupAir.commute.service.CommuteService;
import org.spring.groupAir.commute.service.VacationService;
import org.spring.groupAir.config.MyUserDetailsImpl;
import org.spring.groupAir.department.dto.DepartmentDto;
import org.spring.groupAir.department.dto.TopDepartmentDto;
import org.spring.groupAir.department.service.DepartmentService;
import org.spring.groupAir.department.service.TopDepartmentService;
import org.spring.groupAir.member.dto.MemberDto;
import org.spring.groupAir.member.service.MemberService;
import org.spring.groupAir.schedule.service.ScheduleService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {


    private final AirplaneService airplaneService;
    private final MemberService memberService;
    private final CommuteService commuteService;
    private final ScheduleService scheduleService;
    private final BoardService boardService;
    private final VacationService vacationService;
    private final TopDepartmentService topDepartmentService;
    private final DepartmentService departmentService;


    @GetMapping({"/", "/index"})
    public String index(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "index";
    }

    @GetMapping("/role/admin")
    public String adminPage(Model model) {

        int sickVacationPeople = vacationService.sickVacationPeople();
        int vacationPeople = vacationService.vacationPeople();

        int latePeople = commuteService.latePeople();
        int leaveEarlyPeople = commuteService.leaveEarlyPeople();
        int workPeople = commuteService.workPeople();
        int workOutPeople = commuteService.workOutPeople();
        int notWorkInPeople = commuteService.notWorkInPeople();

        int todayAirplane = airplaneService.findTodayAirplane();
        int allAirplane = airplaneService.findAllAirplane();

        int managers = memberService.selectPilot().size();
        int members = memberService.countMember();

        int board1 = boardService.board1();
        int board2 = boardService.board2();
        int board3 = boardService.board3();
        int board4 = boardService.board4();

        TopDepartmentDto topDepartmentDto = new TopDepartmentDto();
//        List<DepartmentDto> departmentDtoList = departmentService.subDepartments();
        List<TopDepartmentDto> topDepartmentDtos = topDepartmentService.List(topDepartmentDto);


        model.addAttribute("sickVacationPeople", sickVacationPeople);
        model.addAttribute("vacationPeople", vacationPeople);

        model.addAttribute("latePeople", latePeople);
        model.addAttribute("leaveEarlyPeople", leaveEarlyPeople);
        model.addAttribute("workPeople", workPeople);
        model.addAttribute("workOutPeople", workOutPeople);
        model.addAttribute("notWorkInPeople", notWorkInPeople);

        model.addAttribute("todayAirplane", todayAirplane);
        model.addAttribute("allAirplane", allAirplane);

        model.addAttribute("managers", managers);
        model.addAttribute("members", members);

        model.addAttribute("board1", board1);
        model.addAttribute("board2", board2);
        model.addAttribute("board3", board3);
        model.addAttribute("board4", board4);

//        model.addAttribute("departmentDtoList", departmentDtoList);
        model.addAttribute("topDepartmentDtos", topDepartmentDtos);

        return "role/admin";
    }

    @GetMapping("/role/manager")
    public String managerPage(@AuthenticationPrincipal MyUserDetailsImpl myUserDetails, Model model) {

        List<CommuteDto> commuteDtoList = commuteService.commuteList(myUserDetails.getMemberEntity().getId());
        int boardCount = boardService.myBoardCount(myUserDetails.getMemberEntity().getId());
        int todayMyAirplaneCount = airplaneService.todayMyAirplaneCount(myUserDetails.getMemberEntity().getId());
        int myAirplaneCount = airplaneService.myAirplanes(myUserDetails.getMemberEntity().getId());
        MemberDto memberDto = memberService.memberDetail(myUserDetails.getMemberEntity().getId());

        Long deId = myUserDetails.getMemberEntity().getDepartmentEntity().getId();
        List<MemberDto> list = departmentService.getMembers(deId);

        model.addAttribute("list", list);
        model.addAttribute("commuteDtoList", commuteDtoList);
        model.addAttribute("boardCount", boardCount);
        model.addAttribute("myAirplaneCount", myAirplaneCount);
        model.addAttribute("todayMyAirplaneCount", todayMyAirplaneCount);
        model.addAttribute("memberDto", memberDto);

        return "role/manager";
    }

    @GetMapping("/role/member")
    public String memberPage(@AuthenticationPrincipal MyUserDetailsImpl myUserDetails, Model model) {

        List<CommuteDto> commuteDtoList = commuteService.commuteList(myUserDetails.getMemberEntity().getId());
        int boardCount = boardService.myBoardCount(myUserDetails.getMemberEntity().getId());
        MemberDto memberDto = memberService.memberDetail(myUserDetails.getMemberEntity().getId());

        List<VacationDto> vacationDtoList = vacationService.myVacation(myUserDetails.getMemberEntity().getId());

        Long deId = myUserDetails.getMemberEntity().getDepartmentEntity().getId();
        List<MemberDto> list = departmentService.getMembers(deId);

        model.addAttribute("list", list);
        model.addAttribute("commuteDtoList", commuteDtoList);
        model.addAttribute("boardCount", boardCount);
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("vacationDtoList", vacationDtoList);

        return "role/member";
    }
}
