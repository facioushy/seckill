package com.hik.seckill.controller;

import com.hik.seckill.redis.KeyPrefix;
import com.hik.seckill.redis.RedisService;
import com.hik.seckill.validator.MobileCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author SYSTEM
 */
@Controller
public class BaseController {

    @Value("#{'${pageCache.enable}'}")
    private boolean pageCacheEnable;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    public String render(HttpServletRequest request, HttpServletResponse response,
                         Model model, String tplName, KeyPrefix prefix, String key) {
        if (!pageCacheEnable) {
            return tplName;
        }
        // 取缓存
        String html = (String) redisService.get(prefix, key, String.class);
        if (!StringUtils.isEmpty(html)) {
            out(response, html);
            return null;
        }
        WebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process(tplName, ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(prefix, key, html);
        }
        out(response, html);
        return null;
    }

    public static void out(HttpServletResponse res, String html){
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        try{
            OutputStream out = res.getOutputStream();
            out.write(html.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
