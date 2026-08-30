package com.example.its.domain.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.its.config.ItsUserDetails;
import com.example.its.domain.entity.EntityUser;
import com.example.its.domain.model.UserEditForm;
import com.example.its.domain.model.UserSignupForm;
import com.example.its.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void save(UserSignupForm user) {
		String encryptedPwd = passwordEncoder.encode(user.getPassword());
		userRepository.save(user.getName(), encryptedPwd, user.getEmail(), "USER");
	}
	
	@Transactional
	public void update(UserEditForm user, boolean isAdmin) {
		if (isAdmin) {
			userRepository.update(user.getId(), user.getName(), user.getRole(), user.isValid(), user.isForcePwdChange());
		} else {
			userRepository.updateUserName(user.getId(), user.getName());
		}
		if (!user.getPassword().isBlank()) {
			userRepository.updatePwd(user.getId(), passwordEncoder.encode(user.getPassword()));	
		}
	}

	@Transactional
	public void delete(int id) {
		userRepository.delete(id);
	}

	public EntityUser findUser(String email) {
		return userRepository.findByEmail(email).orElse(new EntityUser());
	}
	public boolean isEmailExist(String email) {
		return userRepository.checkEmailUnique(email) != 0;
	}
	public EntityUser findUserById(int id) {
		return userRepository.findById(id).orElse(new EntityUser());
	}

	public Page<EntityUser> findAll(Pageable pageable) {
		int count = userRepository.totalCount();
		int size = pageable.getPageSize();
		int offset = size * pageable.getPageNumber();
		List<EntityUser> users = userRepository.findAll(size, offset);
		
		return new PageImpl<EntityUser>(users, pageable, count);
	}

	@Override
	public ItsUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		EntityUser user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " was not found!"));
		if (!user.isValid()) {
			throw new UsernameNotFoundException(user.getName() + " was not valid!");
		}
		return new ItsUserDetails(user);
	}
}
