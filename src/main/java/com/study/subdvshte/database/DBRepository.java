package com.study.subdvshte.database;

import com.study.subdvshte.model.mainRec;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class DBRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Boolean checkProduct(Integer id) {
        final String sql = """
                SELECT IF(COUNT(*)=0,TRUE,FALSE)
                FROM products
                WHERE id =:id
                """;
        return jdbcTemplate.queryForObject(sql, Map.of("id", id), Boolean.class);
    }

    public Map<String, Object> getProduct(Integer id) throws DataAccessException {
        final String sql = """
                SELECT p.*, IF(sm > 0, true, false) as buy
                FROM products p LEFT JOIN (SELECT product_id, sum(cnt) AS sm
                FROM availability
                WHERE product_id = :id
                GROUP BY product_id) a ON p.id = a.product_id
                WHERE id = :id;
                """;
        return jdbcTemplate.queryForMap(sql, Map.of("id", id)).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (e.getValue())));
    }

    public List<mainRec> getAvailability(String order, Integer av) throws DataAccessException {
        Map<String, List<String>> ord = Map.of("priceDown", List.of("5", "ASC"), "rate", List.of("6", "DESC"), "", List.of("1", ""));
        List<String> hav = List.of("", "HAVING cnt <> 'notAv'");
        final String sql = String.format("""
                SELECT p.id, p.arduino_board_type, p.processor_type, IFNULL(IF(a.sm>10, 'many', IF(a.sm>0, 'few', 'notAv')),'notAv') AS cnt, p.price, p.rate
                FROM products p LEFT JOIN
                (SELECT product_id, sum(cnt) AS sm
                FROM availability
                GROUP BY product_id) a ON p.id=a.product_id
                %S
                ORDER BY %S %S;
                """, hav.get(av), ord.get(order).get(0), ord.get(order).get(1));
        List<mainRec> query = jdbcTemplate.query(sql, Collections.emptyMap(), (rs, rowNum) -> new mainRec((BigInteger) rs.getObject("id"),
                (String) rs.getObject("arduino_board_type"),
                (String) rs.getObject("processor_type"),
                (String) rs.getObject("cnt"),
                (Float) rs.getObject("price"),
                (Float) rs.getObject("rate")));
        return query;
    }

    public List<mainRec> getAvailability(String order, Integer av, Integer min, Integer max, String search) throws DataAccessException {
        Map<String, List<String>> ord = Map.of("priceDown", List.of("5", "ASC"), "rate", List.of("6", "DESC"), "", List.of("1", ""));
        List<String> hav = List.of("", "HAVING cnt <> 'notAv'");
        final String sql = String.format("""
                SELECT p.id, p.arduino_board_type, p.processor_type, IFNULL(IF(a.sm>10, 'many', IF(a.sm>0, 'few', 'notAv')),'notAv') AS cnt, p.price, p.rate
                FROM products p LEFT JOIN
                (SELECT product_id, sum(cnt) AS sm
                FROM availability
                GROUP BY product_id) a ON p.id=a.product_id
                WHERE price BETWEEN %d and %d and arduino_board_type LIKE '%S%%'
                %S
                ORDER BY %S %S;
                """, min, max, search, hav.get(av), ord.get(order).get(0), ord.get(order).get(1));
        List<mainRec> query = jdbcTemplate.query(sql, Collections.emptyMap(), (rs, rowNum) -> new mainRec((BigInteger) rs.getObject("id"),
                (String) rs.getObject("arduino_board_type"),
                (String) rs.getObject("processor_type"),
                (String) rs.getObject("cnt"),
                (Float) rs.getObject("price"),
                (Float) rs.getObject("rate")));
        return query;
    }

    public List<Object> getPoints(Integer id) throws DataAccessException {
        final String sql = """
                SELECT p.address, p.tel, IFNULL(a.cnt, 0) AS cnt, p.point_id
                FROM points p LEFT JOIN (SELECT point_id, cnt FROM availability WHERE product_id = :id) a 
                ON p.point_id = a.point_id;
                """;
        return jdbcTemplate.query(sql, Map.of("id", id),
                (rs, rowNum) -> Map.of("address", rs.getObject("address"),
                        "tel", rs.getObject("tel"),
                        "cnt", rs.getObject("cnt"),
                        "point", rs.getObject("point_id")));
    }

    public void order(Integer id, Integer point) {
        final String sql = """
                UPDATE availability SET cnt = cnt - 1 WHERE product_id = :id AND point_id = :point AND cnt > 0;
                """;
        jdbcTemplate.update(sql, Map.of("id", id, "point", point));
    }

    public List<Object> getAllIdProducts() {
        final String sql = """
                SELECT id, CONCAT(arduino_board_type, ' ', processor_type) as name
                FROM products
                """;
        return jdbcTemplate.query(sql, Collections.emptyMap(), (rs, rowNum) -> Map.of("id", rs.getObject("id"),
                "name", rs.getObject("name")));
    }

    public List<Object> getAllIdPoints() {
        final String sql = """
                SELECT point_id, address
                FROM points
                """;
        return jdbcTemplate.query(sql, Collections.emptyMap(), (rs, rowNum) -> Map.of("id", rs.getObject("point_id"),
                "address", rs.getObject("address")));
    }

    public void updateCountAv(Integer id, Integer point, Integer cnt) {
        final String sql = """
                INSERT INTO availability(product_id, point_id, cnt) VALUES (:id, :point, :inc) ON DUPLICATE KEY UPDATE cnt = cnt + :inc;
                """;
        jdbcTemplate.update(sql, Map.of("id", id, "point", point, "inc", cnt));
    }
}
