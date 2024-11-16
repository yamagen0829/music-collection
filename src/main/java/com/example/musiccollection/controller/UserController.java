package com.example.musiccollection.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.musiccollection.entity.User;
import com.example.musiccollection.form.UserEditForm;
import com.example.musiccollection.repository.UserRepository;
import com.example.musiccollection.security.UserDetailsImpl;
import com.example.musiccollection.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    
    public UserController(UserRepository userRepository, UserService userService) {
    	this.userRepository = userRepository;
    	this.userService = userService;
    }
    
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
    	User user = userRepository.getReferenceById(userDetailsImpl.getUser().getUserId());
    	
    	model.addAttribute("user", user);
    	
    	return "user/index";
    }
    
    @GetMapping("/userEdit")
    public String userEdit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
    	User user = userRepository.getReferenceById(userDetailsImpl.getUser().getUserId());
    	UserEditForm userEditForm = new UserEditForm(user.getUserId(), user.getUserName(), user.getFurigana(), user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), user.getUserEmail());
    	
    	model.addAttribute("userEditForm", userEditForm);
    	
    	return "user/user_edit";
    }
    
    @PostMapping("/userUpdate")
    public String userUpdate(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	//メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
    	if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getUserEmail())) {
    		FieldError fieldError = new FieldError(bindingResult.getObjectName(), "userEmail", "すでに登録済みのメールアドレスです。");
    		bindingResult.addError(fieldError);
    	}
    	
    	if (bindingResult.hasErrors()) {
    		return "user/user_edit";
    	}
    	
    	userService.update(userEditForm);
    	redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
    	
    	return "redirect:/user";
    }
}
