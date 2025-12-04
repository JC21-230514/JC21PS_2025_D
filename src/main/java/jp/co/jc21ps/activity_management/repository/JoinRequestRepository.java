package jp.co.jc21ps.activity_management.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import jp.co.jc21ps.activity_management.entity.JoinRequestEntity;
import jp.co.jc21ps.activity_management.entity.JoinRequestSaveEntity;

@Repository
public class JoinRequestRepository {
    private final JdbcTemplate jdbcTemplate;

    public JoinRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 初期画面表示
    public List<JoinRequestEntity> getJoinRequestById(JoinRequestEntity paramEntity) {
<<<<<<< HEAD
        String sql = """
            SELECT club_id, club_name, club_description
            FROM mst_club
            WHERE club_id NOT IN (
                SELECT club_id FROM trn_club_member WHERE user_id = ?
            )
            AND club_id NOT IN (
                SELECT club_id FROM trn_join_request WHERE user_id = ?
            )
            """;
=======
        /*
         * 初期表示情報を取得するSQL
         * ユーザーがまだ申請していない、かつ所属していない部署の一覧を取得
         */
        //mst_club から club_id, club_name, club_description を取得
        //trn_club_member で既に所属している部署を除外
        //trn_join_request で既に申請している部署を除外（delete_flg = 0 のもの）
        //パラメータとして userId を2回使用
        String sql = """
                SELECT
                    club.club_id,
                    club.club_name,
                    club.club_description
                FROM
                    mst_club as club
                WHERE
                    club.club_id NOT IN (
                        SELECT club_id
                        FROM trn_club_member
                        WHERE user_id = ?
                    )
                AND
                    club.club_id NOT IN (
                        SELECT club_id
                        FROM trn_join_request
                        WHERE user_id = ?
                        AND delete_flg = 0
                    )
                """;
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f

        List<JoinRequestEntity> responseEntity = new ArrayList<>();
        List<Map<String, Object>> joinRequestList = jdbcTemplate.queryForList(sql, paramEntity.getUserId(),
                paramEntity.getUserId());

        if (joinRequestList.isEmpty()) {
            return responseEntity;
        }

        for (Map<String, Object> joinRequest : joinRequestList) {
            JoinRequestEntity joinData = new JoinRequestEntity();
            joinData.setClubName((String) joinRequest.get("club_name"));
            joinData.setClubDescription((String) joinRequest.get("club_description"));
            joinData.setClubId((String) joinRequest.get("club_id"));
            responseEntity.add(joinData);
        }

        return responseEntity;
    }

    // 申請処理
    public void insertClub(JoinRequestSaveEntity paramEntity) {
<<<<<<< HEAD
        String sql = """
            INSERT INTO trn_join_request (user_id, club_id) VALUES (?, ?)
            """;

=======
        /*
         * 申請者の情報をインサートするSQL
         */
        //trn_join_request テーブルに user_id, club_id, delete_flg をインサート
        //delete_flg は 0 で初期化（申請中を表す）
        //パラメータとして userId と clubId を使用
        String sql = """
                INSERT INTO
                    trn_join_request
                    (user_id,
                     club_id,
                     delete_flg)
                VALUES
                    (?, ?, 0)
                """;

        // entityから値をゲットする
>>>>>>> 2e24277ed4210b651bb55b2a8fe07bba1e5fec6f
        Object[] paramList = {
                paramEntity.getUserId(),
                paramEntity.getClubId(),
        };

        jdbcTemplate.update(sql, paramList);
    }
}
