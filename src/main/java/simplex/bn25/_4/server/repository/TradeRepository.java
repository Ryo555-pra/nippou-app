package simplex.bn25._4.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import simplex.bn25._4.server.model.Trade;
import java.util.List;

/**
 * 取引情報の CRUD を提供するリポジトリ
 */
public interface TradeRepository extends JpaRepository<Trade, Integer> {
    /**
     * 取引一覧を銘柄情報とともに取得（N+1 問題を回避）
     */
    @Query("SELECT t FROM Trade t JOIN FETCH t.stock ORDER BY t.createdDatetime ASC")
    List<Trade> findAllWithStock();
}






//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.DataClassRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import simplex.bn25._4.server.model.Stock;
//import simplex.bn25._4.server.model.Trade;
//
//import java.util.List;
//
//@Repository
//public class TradeRepository {
//    private final JdbcTemplate jdbcTemplate;
//    private final DataClassRowMapper<Trade> mapper = new DataClassRowMapper<>(Trade.class);
//
//    @Autowired
//    public TradeRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Trade> listAll(){
//        return this.jdbcTemplate.query("select * from trade", mapper);
//    }
//
//    public Trade register(Trade nerTrade){
//        return this.jdbcTemplate.query("insert into trade(ticker, name, market, shares_issued) values(?,?,?,?,?)",
//                mapper,
//                nerTrade.getTicker(),
//                nerTrade.getName(),
//                nerTrade.getSide().name(),
//                nerTrade.getTraded_date(),
//                nerTrade.getTraded_price()
//        ).get(0);
//    }
//}
