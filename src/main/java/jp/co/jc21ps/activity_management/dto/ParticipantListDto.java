package jp.co.jc21ps.activity_management.dto;

public class ParticipantListDto {

    // 活動ID
    private String activityId;

    // ユーザーID
    private String userId;

    // 活動名
    private String activityName;

    // ユーザー名
    private String userName;

    // 部署ID（追加）
    private String clubId;

    // 部署リーダーID（追加）
    private String leaderClubId;

    public ParticipantListDto() {}

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getClubId() { return clubId; }
    public void setClubId(String clubId) { this.clubId = clubId; }

    public String getLeaderClubId() { return leaderClubId; }
    public void setLeaderClubId(String leaderClubId) { this.leaderClubId = leaderClubId; }
}
