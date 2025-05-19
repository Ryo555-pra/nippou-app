package simplex.bn25._4.server.controler;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StockInputDto {
    @NotBlank
    private String ticker;

    @NotBlank
    private String name;

    @NotBlank
    private String market;

    @NotNull
    private Long sharesIssued;

    public StockInputDto() {
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
}
