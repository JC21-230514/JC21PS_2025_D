package jp.co.jc21ps.activity_management.form;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class RegisterActivitySaveForm {

    // 活動ID
    private String activityId;

    // 部署名
    private String clubName;

    // 部署ID
    private String clubId;

    // 活動名
    /*
     * TODO ➊ activityNameに対し、バリデーションの条件を付与する
     * 1.空白、nullを制御
     * 2.入力値制御(ヒント : @○○(max = ○○, message = "{Size}")
     */

    @NotBlank
    @Size(max = 30, message = "{Size}")
    private String activityName;

    // 活動日
    /*
     * TODO ➋ activityDateに対し、バリデーションの条件を付与する
     * 1.空白、nullを制御 (ヒント : @○○(message = "{NotBlank}"))
     * 2.日付形式の制御(ヒント : @○○(pattern = "{DateTimeFormat}")
     */

    @NotBlank(message = "{NotBlank}")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message  = "{DateTimeFormat}")
    private String activityDate;

    // 過去の日付が入力されたとき
    @AssertTrue(message = "{AssertTrue.activityDate}")
    public boolean isActivityDateValid() {
        try {
            if (activityDate != null) {
                LocalDate inputDate = LocalDate.parse(activityDate);
                return !inputDate.isBefore(LocalDate.now());
            }
            return true;

        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // 活動場所
    /*
     * TODO ➌ activityDateに対し、バリデーションの条件を付与する
     * 1.空白、nullを制御 (ヒント : @○○(message = "{NotBlank}"))
     * 2.入力値制御(ヒント : @○○(max = ○○, message = "{Size}")
     */

    @NotBlank(message = "{NotBlank}")
    @Size(max = 30, message = "{Size}")
    private String activityPlace;

    /*
     * TODO ➍ activityStartTimeに対し、バリデーションの条件を付与する
     * 1.空白、nullを制御 (ヒント : @○○(message = "{NotBlank}"))
     */
    // 活動時間(自)

    @NotBlank(message = "{NotBlank}")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "{Pattern.activityStartTime}") // hh:mm形式
    private String activityStartTime;

    // 活動時間(至)
    /*
     * TODO ➎ activityEndTimeに対し、バリデーションの条件を付与する
     * 1.空白、nullを制御 (ヒント : @○○(message = "{NotBlank}"))
     */

    @NotBlank(message = "{NotBlank}")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "{Pattern.activityEndTime}") // hh:mm形式
    private String activityEndTime;

    // 時間の前後関係チェック
    @AssertTrue(message = "{AssertTrue}")
    public boolean isDateValid() {
        try {

            if (activityEndTime != null || activityStartTime != null) {
                int activityEndTimeInt = Integer.parseInt(activityEndTime.replace(":", ""));
                int activityStartTimeInt = Integer.parseInt(activityStartTime.replace(":", ""));

                // 時間の(:)を削除し、intに変換
                if (activityEndTimeInt >= activityStartTimeInt) {
                    return true;
                }
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            // 数字形式でない場合、無効として扱う
            return false;
        }
    }

    // 活動説明
    /*
     * TODO ➏ activityDescriptionに対し、バリデーションの条件を付与する
     * 1.空白、nullを制御 (ヒント : @○○(message = "{NotBlank}"))
     * 2.入力値制御(ヒント : @○○(max = ○○, message = "{Size}")
     */

    @NotBlank(message = "{NotBlank}")
    @Size(max = 400, message = "{Size}")
    private String activityDescription;

    // 募集人数
    /*
     * TODO ➐ maxParticipantに対し、バリデーションの条件を付与する
     * 1.空白、nullを制御 (ヒント : @○○(message = "{NotBlank}"))
     * 2.最小値制御(ヒント : @○○(value = ○, message = "{Min}")
     * 3.最大値制御(ヒント : @○○(value = ○○, message = "{Max}")
     */

    @NotBlank(message = "{NotBlank}")
    @Min(value = 1, message = "{Min}")
    @Max(value = 100, message = "{Max}")
    @Pattern(regexp = "^[0-9]*$", message = "{Pattern.maxParticipant}") // 半角数字

    private String maxParticipant;

    private String message;

    public RegisterActivitySaveForm() {

    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityPlace() {
        return activityPlace;
    }

    public void setActivityPlace(String activityPlace) {
        this.activityPlace = activityPlace;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getMaxParticipant() {
        return maxParticipant;
    }

    public void setMaxParticipant(String maxParticipant) {
        this.maxParticipant = maxParticipant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
