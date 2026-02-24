package com.example.its.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.its.domain.auth.SignupForm;
import com.example.its.domain.auth.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    /**
     * 会員登録フォームを表示する。
     */
    @GetMapping
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }

    /**
     * 会員登録を処理する。
     * バリデーションエラーや重複チェックエラーがあれば登録フォームに戻る。
     * 成功したらログインページへリダイレクトする。
     */
    @PostMapping
    public String signup(
            @Validated @ModelAttribute SignupForm signupForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        try {
            userService.signup(signupForm);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/signup";
        }

        redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。ログインしてください。");
        return "redirect:/login";
    }
}
