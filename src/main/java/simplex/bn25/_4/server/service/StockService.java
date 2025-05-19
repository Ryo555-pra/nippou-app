package simplex.bn25._4.server.service;

// Jakarta EE 9+ のパッケージ
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import simplex.bn25._4.server.model.Stock;
import simplex.bn25._4.server.repository.StockRepository;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * 新規銘柄を登録します。
     */
    public Stock createStock(String ticker, String name, String market, Long sharesIssued) {
        Stock stock = new Stock();
        stock.setTicker(ticker);
        stock.setName(name);
        stock.setMarket(market);
        stock.setSharesIssued(sharesIssued);
        return stockRepository.save(stock);
    }

    /**
     * すべての銘柄を取得します。
     */
    public List<Stock> findAll() {
        return stockRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * 指定IDの銘柄を取得します。
     */
    public Stock findById(Integer id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found. id=" + id));
    }
}














//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import simplex.bn25._4.server.model.Stock;
//import simplex.bn25._4.server.repository.StockRepository;
//
//import java.util.List;
//
//@Service
//public class StockService {
//    private final StockRepository stockRepository;
//
//    @Autowired
//    public StockService(StockRepository stockRepository) {
//        this.stockRepository = stockRepository;
//    }
//
//    public List<Stock> listAll(){
//        return stockRepository.listAll();
//    }
//
//    public void register(Stock newStock){
//        stockRepository.register(newStock);
//    }
//
//
//
//}
