package jp.co.jc21ps.activity_management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.context.MessageSource;
import jp.co.jc21ps.activity_management.dto.ParticipantListDto;
import jp.co.jc21ps.activity_management.form.ParticipantListForm;
import jp.co.jc21ps.activity_management.service.CommonService;
import jp.co.jc21ps.activity_management.service.ParticipantListService;
import jp.co.jc21ps.activity_management.dto.ParticipantDto;
import jp.co.jc21ps.activity_management.dto.SessionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/participantList")
public class ParticipantListController {

    private final ParticipantListService participantListService;
    private final CommonService commonService;
    private final MessageSource messageSource;

    public ParticipantListController(ParticipantListService participantListService, CommonService commonService,
            MessageSource messageSource) {
        this.participantListService = participantListService;
        this.commonService = commonService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ModelAndView dispParticipantList(@RequestParam(value = "activityId", required = true) String activityId,
            HttpSession session) {

        ModelAndView mav = new ModelAndView();

        if (activityId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        // セッション情報取得
        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();
        String clubId = sessionDto.getClubId();

        if (userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        // DTOに値をセット
        ParticipantListDto dto = new ParticipantListDto();
        dto.setUserId(userId);
        dto.setActivityId(activityId);
<<<<<<< HEAD
        dto.setClubId(clubId);
        dto.setLeaderClubId(leaderClubId);

        try {

            // サービス呼び出し
            List<ParticipantDto> participantDtoList = participantListService.getParticipantListData(dto);
=======

        try {
            // ➌TODO participantListServiceのgetParticipantListDataメソッドを呼び出す。
            ParticipantDto returnDto = participantListService.getParticipantListData(dto);
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f

            // 返却用フォームリスト
            List<ParticipantListForm> responseListForm = new ArrayList<>();

<<<<<<< HEAD
            // ParticipantDto → List<ParticipantListDto> を展開
            for (ParticipantDto participantDto : participantDtoList) {

                List<ParticipantListDto> list = participantDto.getPariticipantListDto();

                if (list != null) {
                    for (ParticipantListDto data : list) {

                        ParticipantListForm form = new ParticipantListForm();

                        form.setParticipantUserId(data.getUserId());
                        form.setParticipantUserName(data.getUserName());
                        form.setParticipantClubName(data.getActivityName());

                        responseListForm.add(form);
                    }
=======
            /*
             * ➍ TODO responseListFormに値をセット
             */

                for (ParticipantListDto participantListDto : returnDto.getPariticipantListDto()) {
                    ParticipantListForm form = new ParticipantListForm();
                    form.setActivityId(participantListDto.getActivityId());
                    form.setUserId(participantListDto.getUserId());
                    form.setActivityName(participantListDto.getActivityName());
                    form.setUserName(participantListDto.getUserName());
                    responseListForm.add(form);
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f
                }
            }

            // 画面に渡す
            mav.addObject("participantList", responseListForm);
            mav.addObject("activityId", activityId);

            // メッセージ
            String resultMessage = messageSource.getMessage("notpariticipant", null, Locale.getDefault());
            mav.addObject("message", resultMessage);
<<<<<<< HEAD
=======
            mav.addObject("leaderClubId", clubId);
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f

            mav.setViewName("participantList");

        } catch (Exception e) {
            mav.setViewName("error");
        }

        return mav;
    }
}
