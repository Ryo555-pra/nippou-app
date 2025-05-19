package simplex.bn25._4.server.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import simplex.bn25._4.server.model.Stock;
import simplex.bn25._4.server.service.StockService;
import simplex.bn25._4.server.service.TradeService;

@Controller
@RequestMapping("/trades")
public class TradeController {
    private final TradeService tradeService;
    private final StockService stockService;

    public TradeController(TradeService tradeService, StockService stockService) {
        this.tradeService = tradeService;
        this.stockService = stockService;
    }

    @GetMapping("/input/{stockId}")
    public String showInputForm(@PathVariable Integer stockId, Model model) {
        Stock stock = stockService.findById(stockId);
        model.addAttribute("stock", stock);
        TradeInputDto tradeInputDto = new TradeInputDto();
        tradeInputDto.setStockId(stockId);
        model.addAttribute("tradeInputDto", tradeInputDto);
        return "trades/input";
    }

    @PostMapping
    public String createTrade(@ModelAttribute @Valid TradeInputDto tradeInputDto,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            // stock 情報を再設定してフォームを再表示
            Stock stock = stockService.findById(tradeInputDto.getStockId());
            model.addAttribute("stock", stock);
            return "trades/input";
        }
        tradeService.createTrade(
                tradeInputDto.getStockId(),
                tradeInputDto.getCreatedDatetime(),
                tradeInputDto.getTradePrice()
        );
        return "redirect:/trades/list";
    }

    @GetMapping("/list")
    public String listTrades(Model model) {
        model.addAttribute("trades", tradeService.findAllWithStock());
        return "trades/list";
    }
}

