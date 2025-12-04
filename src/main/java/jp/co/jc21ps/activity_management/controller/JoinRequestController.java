package jp.co.jc21ps.activity_management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import jp.co.jc21ps.activity_management.dto.JoinRequestDto;
import jp.co.jc21ps.activity_management.dto.JoinRequestSaveDto;
import jp.co.jc21ps.activity_management.dto.SessionDto;
import jp.co.jc21ps.activity_management.form.JoinRequestSaveForm;
import jp.co.jc21ps.activity_management.service.CommonService;
import jp.co.jc21ps.activity_management.service.JoinRequestService;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/joinRequest")

public class JoinRequestController {

    private final JoinRequestService joinRequestService;
    private final MessageSource messageSource;
    private final CommonService commonService;

    // サービスをセット
    public JoinRequestController(JoinRequestService joinRequestService, MessageSource messageSource,
            CommonService commonService) {
        this.joinRequestService = joinRequestService;
        this.messageSource = messageSource;
        this.commonService = commonService;
    }

    @GetMapping
    public ModelAndView getJoinRequestById(HttpSession session, JoinRequestSaveForm paramForm,
            @ModelAttribute("joinRequestCompleteMessage") String joinRequestCompleteMessage) {

        ModelAndView mav = new ModelAndView();

        // セッションからuserIdを取得
        SessionDto sessionDto = commonService.getSessionDto(session);
        //SessionDto sessionDto = commonService.getSessionDto(session);
        //String userId = sessionDto.getUserId();
        //String leaderClubId = sessionDto.getClubId();
        String userId;
        String leaderClubId;
        if (sessionDto != null) {
            userId = sessionDto.getUserId();
            leaderClubId = sessionDto.getClubId();
        } else {
            userId = "";
            leaderClubId = "";
        }

        // セッションが切れた場合、エラー画面に遷移
        if (userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        // formに値をセット
        JoinRequestSaveForm form = new JoinRequestSaveForm();
        form.setUserId(userId);

        // dtoに値をセット
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUserId(userId);

        List<JoinRequestDto> joinRequestList = joinRequestService.findRequest(joinRequestDto);
        List<JoinRequestSaveForm> responseForm = new ArrayList<>();

        // formに値をセット
        for (JoinRequestDto dto : joinRequestList) {

            JoinRequestSaveForm saveData = new JoinRequestSaveForm();
            saveData.setClubName(dto.getClubName());
            saveData.setClubDescription(dto.getClubDescription());
            saveData.setClubId(dto.getClubId());

            // responseFormにリストを追加
            responseForm.add(saveData);

        }
        // リダイレクトされてきた登録申請成功のメッセージを、paramFormにセットする
        paramForm.setMessage(joinRequestCompleteMessage);
        
        // リダイレクトされてきた登録申請成功のメッセージを、mavに追加
        if (joinRequestCompleteMessage != null && !joinRequestCompleteMessage.isEmpty()) {
            mav.addObject("joinRequestCompleteMessage", joinRequestCompleteMessage);
        }

        // 初期表示情報取得結果に応じて、条件分岐
        if (joinRequestList.isEmpty()) {
            // 申請する部署が存在しない場合、メッセージを表示
            String notRequestClubMessage = messageSource.getMessage("notRequestClubMessage", null, Locale.getDefault());
            mav.addObject("notRequestClubMessage", notRequestClubMessage);
        } else {
            // 申請可能な部署が存在する場合、リストを表示
            mav.addObject("joinRequestSaveForm", responseForm);
        }

        mav.addObject("leaderClubId", leaderClubId);

        // 部員登録申請画面に遷移
        mav.setViewName("joinRequest");
        return mav;

    }

    // インサート処理
    @PostMapping("/save")
    public ModelAndView insertRequestClub(HttpSession session, JoinRequestSaveForm paramForm,
            RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();

        // セッションからuserIdを取得
        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();

        // セッションが切れた場合、エラー画面に遷移
        if (userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        // dtoに値をセット
        JoinRequestSaveDto joinRequestSaveDto = new JoinRequestSaveDto();
        joinRequestSaveDto.setUserId(userId);
        joinRequestSaveDto.setClubId(paramForm.getClubId());

        try {
            boolean result = joinRequestService.insertJoinRequest(joinRequestSaveDto);
            
            // インサートの成功、失敗に応じて、処理を変更
            if (result) {
                // インサート成功時、成功メッセージをリダイレクト属性に追加し、部員登録申請画面にリダイレクト
                String joinRequestCompleteMessage = messageSource.getMessage("joinRequestCompleteMessage", null, Locale.getDefault());
                redirectAttributes.addFlashAttribute("joinRequestCompleteMessage", joinRequestCompleteMessage);
                mav.setViewName("redirect:/joinRequest");
            } else {
                // インサート失敗時、エラー画面に遷移
                mav.setViewName("error");
            }

        } catch (Exception e) {
            mav.setViewName("error");
        }
        return mav;
    }
}
