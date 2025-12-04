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

    /**
     * ① 初期表示（一覧画面）
     */
    @GetMapping
<<<<<<< HEAD
    public ModelAndView getJoinRequestList(
            HttpSession session,
            @ModelAttribute("joinOkMessage") String joinOkMessage) {
=======
    public ModelAndView getJoinRequestById(HttpSession session, JoinRequestSaveForm paramForm,
            @ModelAttribute("joinRequestCompleteMessage") String joinRequestCompleteMessage) {
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f

        ModelAndView mav = new ModelAndView();

        // セッション情報
        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();
        String leaderClubId = sessionDto.getClubId();

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
<<<<<<< HEAD

        // 申請する部署がない場合のメッセージ
        String notRequestClubMessage = null;
        if (formList == null || formList.isEmpty()) {
            try {
                notRequestClubMessage = messageSource.getMessage("notRequestClubMessage", null, Locale.getDefault());
            } catch (Exception e) {
                // メッセージ取得に失敗した場合、デフォルトメッセージを設定
                notRequestClubMessage = "申請する部署がありません。";
            }
=======
        // リダイレクトされてきた登録申請成功のメッセージを、paramFormにセットする
        paramForm.setMessage(joinRequestCompleteMessage);
        
        /*
         * 初期表示情報取得結果に応じて、以下の条件文を完成させる。
         */
        // リダイレクトされてきた登録申請成功のメッセージを、mavに追加
        if (joinRequestCompleteMessage != null && !joinRequestCompleteMessage.isEmpty()) {
            mav.addObject("joinRequestCompleteMessage", joinRequestCompleteMessage);
        }
        //joinRequestList が空の場合: messages.properties から notRequestClubMessage を取得し、mav に追加
        //joinRequestList が空でない場合: responseForm を mav.addObject("joinRequestSaveForm", responseForm) で追加
        // 初期表示情報取得結果に応じて、条件分岐
        if (joinRequestList.isEmpty()) {
            // 申請する部署が存在しない場合、メッセージを表示
            String notRequestClubMessage = messageSource.getMessage("notRequestClubMessage", null, Locale.getDefault());
            mav.addObject("notRequestClubMessage", notRequestClubMessage);
        } else {
            // 申請可能な部署が存在する場合、リストを表示
            mav.addObject("joinRequestSaveForm", responseForm);
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f
        }

        mav.addObject("joinRequestSaveForm", formList);
        mav.addObject("joinRequestCompleteMessage", joinOkMessage != null ? joinOkMessage : "");
        mav.addObject("notRequestClubMessage", notRequestClubMessage);
        mav.addObject("leaderClubId", leaderClubId);

        mav.setViewName("joinRequest");
        return mav;
    }

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

<<<<<<< HEAD
        boolean result = joinRequestService.insertJoinRequest(dto);
=======
        try {
            boolean result = joinRequestService.insertJoinRequest(joinRequestSaveDto);
            /*
             * インサートの成功、失敗に応じて、処理を変更する。
             */
            // インサートの成功、失敗に応じて、処理を変更
            if (result) {
                // インサート成功時、成功メッセージをリダイレクト属性に追加し、部員登録申請画面にリダイレクト
                String joinRequestCompleteMessage = messageSource.getMessage("joinRequestCompleteMessage", null, Locale.getDefault());
                redirectAttributes.addFlashAttribute("joinRequestCompleteMessage", joinRequestCompleteMessage);
                mav.setViewName("redirect:/joinRequest");
                //messages.properties から joinRequestCompleteMessage を取得
                //redirectAttributes.addFlashAttribute でリダイレクト属性に追加
                //redirect:/joinRequest にリダイレクト
            } else {
                // インサート失敗時、エラー画面に遷移
                mav.setViewName("error");
            }
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f

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
    }
}
