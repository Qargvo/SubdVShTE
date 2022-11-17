package com.study.subdvshte.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class DBRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Map<String, Object> getProduct(Integer id) throws DataAccessException {
        final String sql = """
                SELECT p.*, IF(sm > 0, true, false) as biy
                FROM products p LEFT JOIN (SELECT product_id, sum(cnt) AS sm
                FROM availability
                WHERE product_id = :id
                GROUP BY product_id) a ON p.id = a.product_id
                WHERE id = :id;
                """;
        return jdbcTemplate.queryForMap(sql, Map.of("id", id)).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (e.getValue())));
    }

    public List<Object> getAvailability() throws DataAccessException {
        final String sql = """
                SELECT p.id, p.arduino_board_type, p.processor_type, IFNULL(IF(a.sm>10, 'many', IF(a.sm>0, 'few', 'notAv')),'notAv') AS cnt, p.price
                FROM products p LEFT JOIN
                (SELECT product_id, sum(cnt) AS sm
                FROM availability
                GROUP BY product_id) a ON p.id=a.product_id;
                """;
        return jdbcTemplate.query(sql, Collections.emptyMap(),
                (rs, rowNum) -> Map.of("id", rs.getObject("id"),
                        "arduino_board_type", rs.getObject("arduino_board_type"),
                        "processor_type", rs.getObject("processor_type"),
                        "cnt", rs.getObject("cnt"),
                        "price", rs.getObject("price")));
    }

    public List<Object> getPoints(Integer id) throws DataAccessException {
        final String sql = """
                SELECT p.address, p.tel, IFNULL(a.cnt, 0) AS cnt
                FROM points p LEFT JOIN (SELECT point_id, cnt FROM availability WHERE product_id = :id) a 
                ON p.point_id = a.point_id;
                """;
        return jdbcTemplate.query(sql, Map.of("id", id),
                (rs, rowNum) -> Map.of("address", rs.getObject("address"),
                        "tel", rs.getObject("tel"),
                        "cnt", rs.getObject("cnt")));
    }
}
