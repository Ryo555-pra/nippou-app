package simplex.bn25._4.server.model;

// Jakarta EE 9+ のパッケージ
import jakarta.persistence.EntityNotFoundException;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trade")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createdDatetime;

    @Column(name = "trade_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal tradePrice;

    @Column(name = "input_time", nullable = false)
    private LocalDateTime inputTime;

    // --- Constructors ---
    public Trade() {
    }

    // --- Getters & Setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public LocalDateTime getInputTime() {
        return inputTime;
    }

    public void setInputTime(LocalDateTime inputTime) {
        this.inputTime = inputTime;
    }
}










//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//public class Trade {
//    private int id;
//    private String ticker;
//    private String name;
//    private LocalDateTime traded_date;
//    private Side side;
//    private long quantity;
//    private BigDecimal traded_price;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTicker() {
//        return ticker;
//    }
//
//    public void setTicker(String ticker) {
//        this.ticker = ticker;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public LocalDateTime getTraded_date() {
//        return traded_date;
//    }
//
//    public void setTraded_date(LocalDateTime traded_date) {
//        this.traded_date = traded_date;
//    }
//
//    public Side getSide() {
//        return side;
//    }
//
//    public void setSide(Side side) {
//        this.side = side;
//    }
//
//    public long getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(long quantity) {
//        this.quantity = quantity;
//    }
//
//    public BigDecimal getTraded_price() {
//        return traded_price;
//    }
//
//    public void setTraded_price(BigDecimal traded_price) {
//        this.traded_price = traded_price;
//    }
//}
