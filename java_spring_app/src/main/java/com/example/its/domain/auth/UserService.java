package com.example.its.domain.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * IDでユーザーを取得する。見つからない場合は例外をスローする。
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません (id=" + id + ")"));
    }

    /**
     * 新規会員を登録する。
     * ユーザー名・メールアドレスの重複チェックを行い、パスワードをエンコードして保存する。
     *
     * @param form 登録フォームの入力値
     * @throws IllegalArgumentException ユーザー名またはメールアドレスが既に使われている場合
     */
    @Transactional
    public void signup(SignupForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("このユーザー名は既に使われています");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています");
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setDisplayName(form.getDisplayName());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setBio(form.getBio());

        userRepository.insert(user);
    }
}
