package simplex.bn25._4.server.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import simplex.bn25._4.server.model.Stock;
import simplex.bn25._4.server.service.StockService;

@Controller
@RequestMapping("/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/input")
    public String showInputForm(Model model) {
        model.addAttribute("stockInputDto", new StockInputDto());
        return "stocks/input";
    }

    @PostMapping
    public String createStock(@ModelAttribute @Valid StockInputDto stockInputDto,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "stocks/input";
        }
        stockService.createStock(
                stockInputDto.getTicker(),
                stockInputDto.getName(),
                stockInputDto.getMarket(),
                stockInputDto.getSharesIssued()
        );
        return "redirect:/stocks/list";
    }

    @GetMapping("/list")
    public String listStocks(Model model) {
        model.addAttribute("stocks", stockService.findAll());
        return "stocks/list";
    }
}

