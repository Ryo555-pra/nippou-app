package simplex.bn25._4.server.model;

// Jakarta EE 9+ のパッケージ
import jakarta.persistence.EntityNotFoundException;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String ticker;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String market;

    @Column(name = "shares_issued", nullable = false)
    private Long sharesIssued;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trade> trades = new ArrayList<>();

    // --- Constructors ---
    public Stock() {
    }

    // --- Getters & Setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Long getSharesIssued() {
        return sharesIssued;
    }

    public void setSharesIssued(Long sharesIssued) {
        this.sharesIssued = sharesIssued;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}














//
//public class Stock {
//    private int id;
//    private String ticker;
//    private String name;
//    private Market market;
//    private long sharesIssued;
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
//    public Market getMarket() {
//        return market;
//    }
//
//    public void setMarket(Market market) {
//        this.market = market;
//    }
//
//    public long getSharesIssued() {
//        return sharesIssued;
//    }
//
//    public void setSharesIssued(long sharesIssued) {
//        this.sharesIssued = sharesIssued;
//    }
//}
