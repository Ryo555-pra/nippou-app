package simplex.bn25._4.server.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LayoutController {

    /**
     * /layout-demo で layouts.html を丸ごと表示
     */
    @GetMapping("/layout-demo")
    public String showLayoutDemo(Model model) {
        // ページタイトルだけセットしておくと見栄えがわかりやすいです
        model.addAttribute("pageTitle", "レイアウトデモ");
        return "layouts";
    }
}
