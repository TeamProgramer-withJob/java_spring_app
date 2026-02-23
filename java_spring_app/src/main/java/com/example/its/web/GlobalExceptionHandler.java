package com.example.its.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

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
     * 静的リソース未検出（favicon など）は404で処理する。
     * favicon.ico はブラウザの自動アクセスで頻発するため、WARN/ERROR は出さない。
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleNoResource(NoResourceFoundException ex, HttpServletRequest request, Model model) {
        String requestUri = request.getRequestURI();
        if ("/favicon.ico".equals(requestUri)) {
            logger.debug("favicon.ico が見つかりません");
            return ResponseEntity.notFound().build();
        }

        logger.warn("静的リソースが見つかりません: {}", requestUri);
        model.addAttribute("errorMessage", "指定されたページまたはリソースは見つかりません。");
        return "error/404";
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
