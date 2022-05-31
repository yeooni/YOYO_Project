package yy.project.YOYO.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yy.project.YOYO.domain.Team;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.domain.UserTeam;
import yy.project.YOYO.service.TeamService;
import yy.project.YOYO.service.UserService;
import yy.project.YOYO.service.UserTeamService;
import yy.project.YOYO.form.TeamForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MeetingController {

    private final UserService userService;
    private final TeamService teamService;
    private final UserTeamService userTeamService;


    @GetMapping("/createMeeting")
    public String home(Model model){
        model.addAttribute("user", "rabbith3");
        return "createMeeting";}

    @ResponseBody
    @PostMapping("/validateMem")
    public int validateMem(@RequestParam("sendData") String id){
        int res = userService.memberCheck(id);
        log.info("res : {}", res);
        return res;
    }

    @ResponseBody
    @PostMapping("/createMeeting")
    public void createMeeting(TeamForm teamForm){
        Team team = new Team();
        System.out.println("================"+teamForm.getMeetingDate());
        String getDates = teamForm.getMeetingDate().replace("T"," ");

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime = LocalDateTime.parse(teamForm.getMeetingDate());
        LocalDateTime dateTime = LocalDateTime.parse(getDates,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println(teamForm.getWritePlace());
        team.setDate(dateTime);
        team.setTeamName(teamForm.getMeetingName());
        if(teamForm.getWritePlace()!=""){
            team.setPlace(teamForm.getWritePlace());
        }else{
            team.setPlace("중간지점");
        }
        Team saveTeam = teamService.save(team);

        UserTeam ut = new UserTeam();

//        == 임시 로그인 계정 ==
        User loginuser = userService.findByUserID("rabbith3");
        ut.setUser(loginuser);
        ut.setTeam(saveTeam);
        ut.setBoss(true);
        userTeamService.save(ut);

        for (int i = 0; i < teamForm.getUsers().length; i++) {
            if( teamForm.getUsers()[i] != ""){
                // 추가한 구성원의 정보
                User userInfo = userService.findByUserID(teamForm.getUsers()[i]);

                // 그룹의 구성원이 추가되었으므로, 유저-팀테이블을 업데이트 해준다.
                ut = new UserTeam();
                ut.setTeam(saveTeam);
                ut.setUser(userInfo);
                ut.setBoss(false);
                // 유저-팀 객체를 UserTeam 테이블에 저장
                userTeamService.save(ut);
            }
        }

    }

}
