package com.example.its.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * アプリケーション全体の例外をHTTPステータスにマッピングするグローバルハンドラ。
 *
 * <ul>
 *   <li>IllegalArgumentException → 404 Not Found（存在しないリソースへのアクセス）</li>
 *   <li>IllegalStateException    → 403 Forbidden（権限のない操作）</li>
 *   <li>その他の例外              → 500 Internal Server Error</li>
 * </ul>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * IllegalArgumentException → 404 Not Found
     * 存在しないIDへのアクセス時など、Serviceがスローする。
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(IllegalArgumentException ex, Model model) {
        logger.warn("リソースが見つかりません: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    /**
     * IllegalStateException → 403 Forbidden
     * 権限のない編集・削除操作時など、Serviceがスローする。
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden(IllegalStateException ex, Model model) {
        logger.warn("アクセス拒否: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/403";
    }

    /**
     * 想定外例外 → 500 Internal Server Error
     * ログにはスタックトレースを記録するが、利用者には汎用メッセージのみ表示する。
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(Exception ex, Model model) {
        logger.error("予期しないエラーが発生しました", ex);
        model.addAttribute("errorMessage", "サーバー内部でエラーが発生しました。しばらく経ってから再度お試しください。");
        return "error/500";
    }
}
