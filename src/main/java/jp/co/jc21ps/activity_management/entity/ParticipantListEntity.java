package jp.co.jc21ps.activity_management.entity;

public class ParticipantListEntity {

    // 活動ID
    private String activityId;

    // ユーザーID
    private String userId;

    // 活動名
    private String activityName;

    // ユーザー名
    private String userName;

    // ★ 追加：クラブ名
    private String clubName;

    public ParticipantListEntity() {
    }

    public ParticipantListEntity(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // ★ 追加：クラブ名 Getter/Setter
    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}
