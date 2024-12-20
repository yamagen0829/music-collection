package com.example.musiccollection.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musiccollection.entity.Role;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.form.SignupForm;
import com.example.musiccollection.form.UserEditForm;
import com.example.musiccollection.repository.RoleRepository;
import com.example.musiccollection.repository.UserRepository;

@Service
public class UserService {
     private final UserRepository userRepository;
     private final RoleRepository roleRepository;
     private final PasswordEncoder passwordEncoder;
     
     public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    	 this.userRepository = userRepository;
    	 this.roleRepository = roleRepository;
    	 this.passwordEncoder = passwordEncoder;
     }
     
     @Transactional
     public User create(SignupForm signupForm) {
    	 User user = new User();
    	 Role role = roleRepository.findByRoleName("ROLE_GENERAL");
    	 
    	 user.setUserName(signupForm.getUserName());
    	 user.setFurigana(signupForm.getFurigana());
    	 user.setPostalCode(signupForm.getPostalCode());
    	 user.setAddress(signupForm.getAddress());
    	 user.setPhoneNumber(signupForm.getPhoneNumber());
    	 user.setUserEmail(signupForm.getUserEmail());
    	 user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
    	 user.setRole(role);
         user.setEnabled(false);
         
         return userRepository.save(user);
     }
     
     @Transactional
     public void update(UserEditForm userEditForm) {
    	 User user = userRepository.getReferenceById(userEditForm.getUserId());
    	 
    	 user.setUserName(userEditForm.getUserName());
    	 user.setFurigana(userEditForm.getFurigana());
    	 user.setPostalCode(userEditForm.getPostalCode());
    	 user.setAddress(userEditForm.getAddress());
    	 user.setPhoneNumber(userEditForm.getPhoneNumber());
    	 user.setUserEmail(userEditForm.getUserEmail());
    	 
    	 userRepository.save(user);
     }
     
     //メールアドレスが登録済みかどうかをチェックする
     public boolean isEmailRegistered(String userEmail) {
//    	 Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
    	 return userRepository.findByUserEmail(userEmail).isPresent();
     }
     
     //パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
     public boolean isSamePassword(String password, String passwordConfirmation) {
    	 return password.equals(passwordConfirmation);
     }
     
     //ユーザーを有効にする
     @Transactional
     public void enableUser(User user) {
    	 user.setEnabled(true);
    	 userRepository.save(user);
     }
     
     //メールアドレスが変更されたかどうかをチェックする
     public boolean isEmailChanged(UserEditForm userEditForm) {
    	 User currentUser = userRepository.getReferenceById(userEditForm.getUserId());
    	 return !userEditForm.getUserEmail().equals(currentUser.getUserEmail());
     }
}
