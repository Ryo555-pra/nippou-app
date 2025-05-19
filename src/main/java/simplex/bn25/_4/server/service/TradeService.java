package simplex.bn25._4.server.service;

// Jakarta EE 9+ のパッケージ
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import simplex.bn25._4.server.model.Trade;
import simplex.bn25._4.server.model.Stock;
import simplex.bn25._4.server.repository.TradeRepository;
import simplex.bn25._4.server.repository.StockRepository;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;
    private final StockRepository stockRepository;

    public TradeService(TradeRepository tradeRepository, StockRepository stockRepository) {
        this.tradeRepository = tradeRepository;
        this.stockRepository = stockRepository;
    }

    /**
     * 新規取引を登録します。
     */
    public Trade createTrade(Integer stockId, LocalDateTime createdDatetime, BigDecimal tradePrice) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found. id=" + stockId));

        Trade trade = new Trade();
        trade.setStock(stock);
        trade.setCreatedDatetime(createdDatetime);
        trade.setTradePrice(tradePrice);
        trade.setInputTime(LocalDateTime.now());

        return tradeRepository.save(trade);
    }

    /**
     * すべての取引を取得します（銘柄情報を含む）。
     */
    public List<Trade> findAllWithStock() {
        return tradeRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDatetime"));
    }
}

















//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import simplex.bn25._4.server.model.Stock;
//import simplex.bn25._4.server.model.Trade;
//import simplex.bn25._4.server.repository.StockRepository;
//
//import java.util.List;
//
//@Service
//public class TradeService {
//    private final TradeService tradeRepository;
//
//    @Autowired
//    public TradeService(TradeService tradeRepository) {
//        this.tradeRepository = tradeRepository;
//    }
//
//    public List<Trade> listAll(){
//        return tradeRepository.listAll();
//    }
//
//    public void register(Stock newTrade){
//        tradeRepository.register(newTrade);
//    }
//}
