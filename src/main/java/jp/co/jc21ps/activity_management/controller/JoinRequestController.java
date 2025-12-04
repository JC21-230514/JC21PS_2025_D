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
import jp.co.jc21ps.activity_management.form.JoinRequestForm;
import jp.co.jc21ps.activity_management.form.JoinRequestSaveForm;
import jp.co.jc21ps.activity_management.service.CommonService;
import jp.co.jc21ps.activity_management.service.JoinRequestService;

import org.springframework.beans.factory.annotation.Autowired;
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

    public JoinRequestController(JoinRequestService joinRequestService,
                                 MessageSource messageSource,
                                 CommonService commonService) {
        this.joinRequestService = joinRequestService;
        this.messageSource = messageSource;
        this.commonService = commonService;
    }

    @GetMapping
    public ModelAndView getJoinRequestById(HttpSession session, JoinRequestSaveForm paramForm,
            @ModelAttribute("joinOkMessage") String joinOkMessage) {
 
        ModelAndView mav = new ModelAndView();
 
        // セッションからuserIdを取得
        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();
        String leaderClubId = sessionDto.getClubId();
 
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
        paramForm.setMessage(joinOkMessage);
 
        /*
         * TODO ➊ 初期表示情報取得結果に応じて、以下の条件文を完成させる。
         */
 
        mav.addObject("leaderClubId", leaderClubId);
 
        // 部員登録申請画面に遷移
        mav.setViewName("joinRequest");
        return mav;
 
    }
    /**
     * ① 初期表示（一覧画面）
     */
    /*@GetMapping
    public ModelAndView getJoinRequestList(
            HttpSession session,
            @ModelAttribute("joinOkMessage") String joinOkMessage) {

        ModelAndView mav = new ModelAndView();

        // セッション情報
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

        if (userId == null || userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        // DTO準備
        JoinRequestDto dto = new JoinRequestDto();
        dto.setUserId(userId);

        // 一覧取得
        List<JoinRequestDto> dtoList = joinRequestService.findRequest(dto);

        List<JoinRequestForm> formList = new ArrayList<>();
        if (dtoList != null) {
            for (JoinRequestDto data : dtoList) {
                JoinRequestForm f = new JoinRequestForm();
                f.setClubId(data.getClubId());
                f.setClubName(data.getClubName());
                f.setClubDescription(data.getClubDescription());
                formList.add(f);
            }
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

        mav.addObject("joinRequestSaveForm", formList);
        mav.addObject("joinRequestCompleteMessage", joinOkMessage != null ? joinOkMessage : "");
        mav.addObject("notRequestClubMessage", notRequestClubMessage);
        mav.addObject("leaderClubId", leaderClubId);

        mav.setViewName("joinRequest");
        return mav;
    }*/

    /**
     * ② 確認画面表示
     */
    @PostMapping("/confirm")
    public ModelAndView confirmJoinRequest(
            HttpSession session,
            JoinRequestForm paramForm) {

        ModelAndView mav = new ModelAndView();

        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();

        if (userId == null || userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        // 対象データをフォームのまま渡す
        mav.addObject("confirmData", paramForm);
        mav.setViewName("joinRequestConfirm");
        return mav;
    }

    /**
     * ③ DB登録処理
     */
    @PostMapping("/save")
    public ModelAndView insertRequestClub(
            HttpSession session,
            JoinRequestSaveForm paramForm,
            RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();

        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();

        if (userId == null || userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        // DTO にセット
        JoinRequestSaveDto dto = new JoinRequestSaveDto();
        dto.setUserId(userId);
        dto.setClubId(paramForm.getClubId());

        //try{
            boolean result = joinRequestService.insertJoinRequest(dto);
            
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

        if (result) {
            // 成功メッセージ
            String successMessage = messageSource.getMessage(
                    "joinRequestCompleteMessage", null, Locale.getDefault());
            redirectAttributes.addFlashAttribute("joinOkMessage", successMessage);

            mav.setViewName("redirect:/joinRequest");
        } else {
            // 失敗メッセージ
            mav.setViewName("error");
        }

        return mav;
        //}
}
}
