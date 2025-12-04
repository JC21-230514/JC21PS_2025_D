package jp.co.jc21ps.activity_management.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.jc21ps.activity_management.entity.ParticipantListEntity;

@Repository
public class ParticipantListRepository {

    private final JdbcTemplate jdbcTemplate;

    public ParticipantListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 初期画面表示
    public List<ParticipantListEntity> getParticipantListData(ParticipantListEntity paramEntity) {
<<<<<<< HEAD
=======
        /*
         * TODO ➊ 初期表示情報を取得するSQLを完成させる。
         */
        String sql = 
                """
                SELECT 
                    a.activity_id, 
                    a.user_id,
                    b.activity_name,
                    c.login_name AS user_name
                FROM 
                    trn_participant a
                LEFT JOIN 
                    trn_activity b
                ON
                    a.activity_id = b.activity_id
                LEFT JOIN 
                    mst_user c 
                ON
                    a.user_id = c.user_id
                WHERE
                    a.activity_id = ?
                """;
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f

        String sql =
                "SELECT a.activity_id, a.user_id, b.activity_name, c.user_name " +
                "FROM participant_list_table a " +
                "JOIN activity_table b ON a.activity_id = b.activity_id " +
                "JOIN user_table c ON a.user_id = c.user_id " +
                "WHERE a.activity_id = ?";

        List<Map<String, Object>> participantList =
                jdbcTemplate.queryForList(sql, paramEntity.getActivityId());

        List<ParticipantListEntity> responseListEntity = new ArrayList<>();

        if (participantList.isEmpty()) {
            return responseListEntity;
        }

        for (Map<String, Object> participant : participantList) {
            ParticipantListEntity responseEntity = new ParticipantListEntity();
            responseEntity.setActivityId((String) participant.get("activity_id"));
            responseEntity.setUserId((String) participant.get("user_id"));
            responseEntity.setActivityName((String) participant.get("activity_name"));
            responseEntity.setUserName((String) participant.get("user_name"));
            responseListEntity.add(responseEntity);
        }

        return responseListEntity;
    }

    // 活動名を表示
    public String getActivityName(ParticipantListEntity paramEntity) {
<<<<<<< HEAD

        String sql =
                "SELECT activity_name " +
                "FROM activity_table " +
                "WHERE activity_id = ?";
=======
        /*
         * TODO ➋ 活動名を取得するSQLを完成させる。
         */
        String sql = """
                SELECT
                    activity_name
                FROM
                    trn_activity
                WHERE
                    activity_id = ?
                """;
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f

        List<Map<String, Object>> actNameList =
                jdbcTemplate.queryForList(sql, paramEntity.getActivityId());

        if (actNameList.isEmpty()) {
            return "";
        }

        return (String) actNameList.get(0).get("activity_name");
    }
}
