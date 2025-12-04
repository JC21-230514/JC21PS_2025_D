package jp.co.jc21ps.activity_management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;
import jp.co.jc21ps.activity_management.dto.JoinApprovalDataDto;
import jp.co.jc21ps.activity_management.dto.JoinApprovalDto;
import jp.co.jc21ps.activity_management.dto.SessionDto;
import jp.co.jc21ps.activity_management.form.JoinApprovalDataForm;
import jp.co.jc21ps.activity_management.form.JoinApprovalForm;
import jp.co.jc21ps.activity_management.service.CommonService;
import jp.co.jc21ps.activity_management.service.JoinApprovalService;
import jp.co.jc21ps.activity_management.dto.JoinApprovalNameDto;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/joinApproval")
public class JoinApprovalController {

    private final JoinApprovalService joinApprovalService;
    private final CommonService commonService;
    private final MessageSource messageSource;

    public JoinApprovalController(JoinApprovalService joinApprovalService, CommonService commonService,
            MessageSource messageSource) {

        this.joinApprovalService = joinApprovalService;
        this.commonService = commonService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ModelAndView getjoinApproval(HttpSession session) {

        ModelAndView mav = new ModelAndView();

        // セッションからuserId,clubIdを取得
        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();
        String leaderClubId = sessionDto.getClubId();

        // セッションが切れた場合、エラー画面に遷移
        if (leaderClubId == null || leaderClubId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        try {
            // 取得したuserIdが空の場合、エラー画面に遷移
            if (userId == null || userId.isEmpty()) {
                mav.setViewName("error");
                return mav;
            }

            // dtoに値をセット
            JoinApprovalDto joinApprovalDto = new JoinApprovalDto();
            joinApprovalDto.setUserId(userId);
            joinApprovalDto.setClubId(leaderClubId);

            // パラメータから、データベースを取得
            // trn_join_requestテーブル（申請テーブル）から取得
            JoinApprovalNameDto viewList = joinApprovalService.getJoinApprovalData(joinApprovalDto);
            List<JoinApprovalForm> responseForm = new ArrayList<>();

            // formに値をセット
            if (viewList != null && viewList.getJoinApprovalDto() != null) {
                for (JoinApprovalDto dto : viewList.getJoinApprovalDto()) {

                    JoinApprovalForm requestList = new JoinApprovalForm();
                    requestList.setClubId(dto.getClubId());
                    requestList.setUserId(dto.getUserId());
                    requestList.setClubName(dto.getClubName());
                    requestList.setUserName(dto.getUserName());
                    requestList.setLeaderFlg(dto.isLeaderFlg());

                    // responseFormにリストを追加
                    responseForm.add(requestList);

                }
            }

            // データベースからclubNameを取得し、オブジェクトに追加する
            String clubName = (viewList != null && viewList.getClubName() != null) 
                ? viewList.getClubName() : "";
            mav.addObject("clubName", clubName);

            // メッセージプロパティファイルからメッセージを取得("notrequest")して画面に表示する
            String resultMessage = messageSource.getMessage("notrequest", null, Locale.getDefault());

            // 取得したメッセージをオブジェクトに追加する
            mav.addObject("message", resultMessage);
            
            // データベースをオブジェクトに追加する
            mav.addObject("joinApprovalform", responseForm);
            
            // セッションで保持している部署IDをsessionから取得したclubIdに設定
            mav.addObject("leaderClubId", leaderClubId);
            
            // 部員登録承認画面に遷移
            mav.setViewName("JoinApproval");

        } catch (Exception e) {
            // DB接続に失敗した場合、エラー画面に遷移
            e.printStackTrace();
            if (leaderClubId == null) {
                leaderClubId = "";
            }
            mav.addObject("leaderClubId", leaderClubId);
            mav.setViewName("error");
        }
        return mav;
    }

    // 否認
    @PostMapping("/denial")
    public ModelAndView denialRequest(JoinApprovalDataForm paramForm, HttpSession session) {

        // paramDtoに値をセット
        JoinApprovalDataDto paramDto = new JoinApprovalDataDto();
        paramDto.setUserId(paramForm.getUserId());
        paramDto.setClubId(paramForm.getClubId());
        paramDto.setLeaderFlg(paramForm.isLeaderFlg());

        // セッションからuserId,clubIdを取得
        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();
        String leaderClubId = sessionDto.getClubId();

        ModelAndView mav = new ModelAndView();

        // 取得したuserIdが空の場合、エラー画面に遷移
        if (userId == null || userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }
        if (leaderClubId == null || leaderClubId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        try {
            // パラメータから、削除処理を実行
            // trn_join_requestテーブルから削除
            joinApprovalService.deleteRequestInfo(paramDto);

            // セッションで保持しているleaderClubIdに、sessionから取得したclubIdを設定
            mav.addObject("leaderClubId", leaderClubId);
            
            // /joinApprovalにリダイレクト
            mav.setViewName("redirect:/joinApproval");
        } catch (Exception e) {
            // DB接続に失敗した場合、エラー画面に遷移
            e.printStackTrace();
            if (leaderClubId == null) {
                leaderClubId = "";
            }
            mav.addObject("leaderClubId", leaderClubId);
            mav.setViewName("error");
        }

        return mav;

    }

    // 承認
    @PostMapping("/approval")
    public ModelAndView approvalRequest(JoinApprovalDataForm paramForm, HttpSession session) {
        // paramDtoに値をセット
        JoinApprovalDataDto paramDto = new JoinApprovalDataDto();
        paramDto.setUserId(paramForm.getUserId());
        paramDto.setClubId(paramForm.getClubId());
        paramDto.setLeaderFlg(paramForm.isLeaderFlg());

        // セッションからuserId,clubIdを取得
        SessionDto sessionDto = commonService.getSessionDto(session);
        String userId = sessionDto.getUserId();
        String leaderClubId = sessionDto.getClubId();

        ModelAndView mav = new ModelAndView();

        // 取得したuserIdが空の場合、エラー画面に遷移
        if (userId == null || userId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }
        if (leaderClubId == null || leaderClubId.isEmpty()) {
            mav.setViewName("error");
            return mav;
        }

        try {
            // パラメータから、登録処理を実行
            // trn_club_memberテーブルにinsert
            joinApprovalService.insertRequestInfo(paramDto);
            
            // パラメータから、削除処理を実行（申請テーブルから削除しないと、承認画面に承認済みユーザーが表示され続けるため）
            // trn_join_requestテーブルから削除
            joinApprovalService.deleteRequestInfo(paramDto);

            // セッションで保持しているleaderClubIdに、sessionから取得したclubIdを設定
            mav.addObject("leaderClubId", leaderClubId);
            
            // /joinApprovalにリダイレクト
            mav.setViewName("redirect:/joinApproval");

        } catch (Exception e) {
            // DB接続に失敗した場合、エラー画面に遷移
            e.printStackTrace();
            if (leaderClubId == null) {
                leaderClubId = "";
            }
            mav.addObject("leaderClubId", leaderClubId);
            mav.setViewName("error");
        }
        return mav;
    }

}
