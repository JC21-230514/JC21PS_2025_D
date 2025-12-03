package jp.co.jc21ps.activity_management.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import jp.co.jc21ps.activity_management.entity.TopEntity;
import jp.co.jc21ps.activity_management.entity.TopDataEntity;

@Repository
public class TopRepository {

    private final JdbcTemplate jdbcTemplate;

    public TopRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 上限人数を取得
    public int getMaxParticipants(TopDataEntity paramEntity) {

        String sqlcheck = """
                SELECT
                    max_participant
                FROM
                    trn_activity
                WHERE
                    activity_id = ?
                AND
                    club_id = ?
                """;

        Integer responseMaxParticipant = jdbcTemplate.queryForObject(sqlcheck, Integer.class,
                paramEntity.getActivityId(),
                paramEntity.getClubId());

        return responseMaxParticipant;
    }

    // 参加中の人数を取得
    public int isActivityParticipating(TopDataEntity paramEntity) {

        String sqlCheck = """
                SELECT
                    COUNT(*)
                FROM
                    trn_participant
                WHERE
                    activity_id = ?
                    AND
                    user_id = ?;
                """;

        Integer responseCount = jdbcTemplate.queryForObject(sqlCheck, Integer.class, paramEntity.getActivityId(),
                paramEntity.getUserId());

        return responseCount;

    }

    // 参加中の人数を取得
    public int isCurrentctivityParticipating(TopDataEntity paramEntity) {

        String sqlCheck = """
                SELECT
                    COUNT(*)
                FROM
                    trn_participant
                WHERE
                    activity_id = ?
                """;

        Integer responseCount = jdbcTemplate.queryForObject(sqlCheck, Integer.class, paramEntity.getActivityId());

        return responseCount;

    }

    // 参加処理
    public void saveActivity(TopDataEntity paramEntity) {

        String sqlInsert = """
                INSERT INTO
                    trn_participant
                     (activity_id,
                     user_id
                      )
                VALUES (?,?)
                """;

        Object[] paramList = {
                paramEntity.getActivityId(),
                paramEntity.getUserId(),
        };

        jdbcTemplate.update(sqlInsert, paramList);

    }

    // 不参加処理
    public void deleteActivity(TopDataEntity paramEntity) {

        String sqlDelete = """
                DELETE FROM
                    trn_participant
                 WHERE
                    activity_id = ?
                 AND
                    user_id = ?
                """;

        Object[] paramList = {
                paramEntity.getActivityId(),
                paramEntity.getUserId(),
        };

        jdbcTemplate.update(sqlDelete, paramList);

    }

    // 初期画面表示
    public List<TopEntity> getTopData(TopEntity paramEntity) {

        String sql = """
                SELECT
                    distinct activity.*,
                    club.club_id,
                    club.club_name,
                    count.count,
                    isnull(participant.user_id) != 1 as participation_flg
                FROM
                     trn_activity as activity
                INNER JOIN
                    mst_club as club USING(club_id)
                INNER JOIN
                    trn_club_member as member ON club.club_id = member.club_id
                LEFT JOIN
                    (SELECT activity_id,count(*) as count FROM trn_participant GROUP BY activity_id) as count ON count.activity_id = activity.activity_id
                LEFT JOIN
                    trn_participant as participant ON participant.user_id = ?
                AND
                    participant.activity_id = activity.activity_id
                AND activity.activity_start_time > now()
                WHERE
                    member.user_id = ?
                    AND activity.activity_start_time > now()
                ORDER BY
                    club.club_id ASC,activity.activity_start_time ASC;
                """;

        List<Map<String, Object>> activityList = jdbcTemplate.queryForList(sql, paramEntity.getUserId(),
                paramEntity.getUserId());

        List<TopEntity> responseEntity = new ArrayList<>();

        // リストが空だった場合
        if (activityList.isEmpty()) {
            return responseEntity;
        }

        // 活動時間をHH:mm形式に指定
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

        // 活動日をyyyy-MM-dd形式に指定
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 番号付与の初期値
        int index = 1;

        String tmpClubId = null;

        int exeCount = 0;

        for (Map<String, Object> activity : activityList) {

            // entityに値をセットする
            TopEntity topData = new TopEntity();

            // 部署ID
            topData.setClubId((String) activity.get("club_Id"));

            // 部署名
            topData.setClubName((String) activity.get("club_name"));

            // 活動ID
            topData.setActivityId((Integer) activity.get("activity_id"));

            // 活動名
            topData.setActivityName((String) activity.get("activity_name"));

            // 活動場所
            topData.setActivityPlace((String) activity.get("activity_place"));

            // 活動時間（自）
            // LocalDateTime型に変換
            Object startTimeObj = activity.get("activity_start_time");

            if (startTimeObj instanceof LocalDateTime) {
                LocalDateTime startTime = (LocalDateTime) startTimeObj;
                topData.setActivityStartTime(startTime.format(formatterTime));

            } else if (startTimeObj instanceof String) {
                LocalDateTime startTime = LocalDateTime.parse((String) startTimeObj, formatterTime);
                topData.setActivityStartTime(startTime.format(formatterTime));
            }

            // 活動時間（至）
            // LocalDateTime型に変換
            Object endTimeObj = activity.get("activity_end_time");

            if (endTimeObj instanceof LocalDateTime) {
                LocalDateTime endTime = (LocalDateTime) endTimeObj;
                topData.setActivityEndTime(endTime.format(formatterTime));

            } else if (endTimeObj instanceof String) {
                LocalDateTime endTime = LocalDateTime.parse((String) endTimeObj, formatterTime);
                topData.setActivityEndTime(endTime.format(formatterTime));
            }

            // 活動日
            // LocalDateTime型に変換
            Object DateObj = activity.get("activity_start_time");

            if (DateObj instanceof LocalDateTime) {
                LocalDateTime Date = (LocalDateTime) DateObj;
                topData.setDispActivityDate(Date.format(formatterDate));

            } else if (DateObj instanceof String) {
                LocalDateTime Date = LocalDateTime.parse((String) DateObj, formatterDate);
                topData.setDispActivityDate(Date.format(formatterDate));
            }

            // 活動説明
            topData.setActivityDescription((String) activity.get("activity_description"));

            // 参加人数
            // int型に変換
            Object countObj = activity.get("count");
            if (countObj instanceof Long) {
                Long countLong = (Long) countObj;
                if (countLong >= Integer.MIN_VALUE && countLong <= Integer.MAX_VALUE) {
                    topData.setParticipantsCount(countLong.intValue());
                } else {
                    throw new IllegalArgumentException("Count value out of range for int: " + countLong);
                }
            } else if (countObj instanceof Number) {
                Number countNumber = (Number) countObj;
                topData.setParticipantsCount(countNumber.intValue());
            }

            // 上限人数
            // int型に変換
            Object maxParticipantObj = activity.get("max_participant");
            String maxParticipantString = "";
            if (maxParticipantObj instanceof Number) {
                Integer maxParticipant = ((Number) maxParticipantObj).intValue();
                maxParticipantString = Integer.toString(maxParticipant);
            }
            topData.setMaxParticipant(maxParticipantString);

            // 参加者フラグ
            // int型に変換
            Object participationFlgObj = activity.get("participation_flg");
            if (participationFlgObj instanceof Number) {
                int participationFlgInt = ((Number) participationFlgObj).intValue();
                topData.setIsParticipationFlg(participationFlgInt == 1);
            } else {
                topData.setIsParticipationFlg(false); // デフォルト値
            }

            // 番号を設定
            if (exeCount == 0) {
                topData.setNo(index++);
                tmpClubId = (String) activity.get("club_id");

            } else if (!tmpClubId.equals((String) activity.get("club_id"))) {
                index = 1;
                topData.setNo(index++);
                tmpClubId = (String) activity.get("club_id");
            } else {
                topData.setNo(index++);
            }
            exeCount++;
            responseEntity.add(topData);
        }
        return responseEntity;
    }
}
