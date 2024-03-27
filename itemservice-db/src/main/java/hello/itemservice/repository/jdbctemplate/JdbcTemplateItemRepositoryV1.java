package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {
    private final JdbcTemplate template;
    public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }
    @Override
    public Item save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); //자동 키 증가를 위함

        template.update(connection -> { //update() -> INSERT, UPDATE, DELETE 문에서 사용
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // id가 자동 증가 키

            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);

        //DB에서 자동생성되는 키는 트랜잭션이 끝나고 리턴받아야하기 때문에 아래의 작업이 필요함.
        long key = keyHolder.getKey().longValue(); //DB에서 자동 증가 키를 받아옴
        item.setId(key);
        return item;
    }
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=?, price=?, quantity=? where id=?";

        //자동 키 증가가 없으면 아래와 같이 간단하게 작성 가능
        //update() -> INSERT, UPDATE, DELETE 문에서 사용
        template.update(sql, updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId);
    }
        @Override
        public Optional<Item> findById(Long id) {
            String sql = "select id, item_name, price, quantity from item where id = ?";

            try {
                Item item = template.queryForObject(sql, itemRowMapper(), id); //queryForObject() -> SELECT 문에서 사용, 반환되는 row의 개수가 한개 일 때
                //RowMapper 는 DB의 반환 결과인 ResultSet을 객체로 변환

                //Optional.of(value): null이 아닌 명시적인 값을 가지는 Optional 객체를 반환한다. 만약 인자로 넘긴 값이 null이라면, 즉시 NullPointerException을 발생시킨다.
                //Optional.ofNullable(value): 인자로 넘긴 값이 null일 수도 있는 경우 사용한다. 값이 null이면 빈 Optional 객체를 반환한다.
                return Optional.of(item);
            } catch (EmptyResultDataAccessException e) {
                return Optional.empty(); //빈 Optional 객체를 생성
            }
    }
        @Override
        public List<Item> findAll(ItemSearchCond cond) {
            String itemName = cond.getItemName();
            Integer maxPrice = cond.getMaxPrice();
            String sql = "select id, item_name, price, quantity from item";

            //동적 쿼리
            if (StringUtils.hasText(itemName) || maxPrice != null) { //검색 이름 텍스트 또는 가격조건이 있다면
                sql += " where";
            }
            boolean andFlag = false;
            List<Object> param = new ArrayList<>();
            if (StringUtils.hasText(itemName)) { //검색 이름 텍스트가 존재한다면
                sql += " item_name like concat('%',?,'%')"; //concat('%',?,'%')는 입력한 문자열이 item_name 열의 어느 위치에든 나타날 수 있음을 의미
                param.add(itemName);
                andFlag = true;
            }
            if (maxPrice != null) { //최대 가격이 존재한다면
                if (andFlag) {
                    sql += " and";
                }
                sql += " price <= ?";
                param.add(maxPrice);
            }
            log.info("sql={}", sql);
            return template.query(sql, itemRowMapper(), param.toArray()); //query() : 결과가 하나 이상일 때 사용, 결과가 없으면 빈 컬렉션을 반환
        }
    private RowMapper<Item> itemRowMapper() { //Item 타입의 객체로 매핑하기 위한 RowMapper의 구현

        //itemRowMapper() -> 데이터베이스의 조회 결과를 객체로 변환할 때 사용

        //람다 표현식은 ResultSet의 현재 행을 Item 객체로 변환하는 로직을 포함
        //rs: 현재 처리 중인 행에 대한 ResultSet 객체
        //rowNum: 처리 중인 현재 행의 번호, 이 값은 일반적으로 로직 내에서 사용되지 않지만, 특정 상황에서 행 번호를 기반으로 추가적인 로직을 수행할 때 유용할 수 있다.
        return (rs, rowNum) -> {
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        };
    }
}