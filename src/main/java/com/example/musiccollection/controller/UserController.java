package com.example.musiccollection.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.musiccollection.UserNotFoundException;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.form.UserEditForm;
import com.example.musiccollection.repository.UserRepository;
import com.example.musiccollection.security.UserDetailsImpl;
import com.example.musiccollection.service.StripeService;
import com.example.musiccollection.service.UserService;
import com.stripe.exception.StripeException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final StripeService stripeService;
    
    public UserController(UserRepository userRepository, UserService userService, StripeService stripeService) {
    	this.userRepository = userRepository;
    	this.userService = userService;
    	this.stripeService = stripeService;
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
    
    @GetMapping("/paid")
    public String showPaidRegistration(HttpServletRequest httpServletRequest, Model model) {
        User user = userService.getCurrentUser();
        if(user == null) {
            throw new UserNotFoundException("Current user is not authenticated");
        }
        try {
        String sessionId = stripeService.createStripeSession(user.getUserEmail(), httpServletRequest);
        
        System.out.println("UserController paid :" + sessionId);
        
        model.addAttribute("user", user);
        model.addAttribute("sessionId", sessionId);
        } catch (StripeException e) {
        	model.addAttribute("errorMessage", "Stripeセッションの作成中にエラーが発生しました。");
            return "error/stripe";
        }
        return "user/paid";
    }
    
    @PostMapping("/upgrade")
    public String upgrade(@RequestParam("userId") Integer userId, RedirectAttributes redirectAttributes) {
        try {
            userService.upgrade(userId);
            redirectAttributes.addFlashAttribute("successMessage", "有料会員へアップグレードしました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }  
        return "redirect:/user";
    }
    
    @PostMapping("/downgrade")
    public String downgrade(@RequestParam("userId") Integer userId, RedirectAttributes redirectAttributes) {
        try {
            userService.downgrade(userId);
            redirectAttributes.addFlashAttribute("successMessage", "無料会員へダウングレードしました。");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }  
        return "redirect:/user";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam("userId") Integer userId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            userService.cancel(userId);
         // セッションを無効化し、ユーザーをログアウトさせる
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            redirectAttributes.addFlashAttribute("successMessage", "退会が完了しました。");
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }  
        return "redirect:/";
    }
    
    @GetMapping("/success")
    public String success(@RequestParam("session_id") String sessionId, RedirectAttributes redirectAttributes) {
        // 成功した場合の処理
        redirectAttributes.addFlashAttribute("successMessage", "サブスクリプション支払いが成功しました。");
        return "redirect:/user"; // 成功メッセージページにリダイレクト
    }

    @GetMapping("/cancel")
    public String cancel(RedirectAttributes redirectAttributes) {
        // キャンセルした場合の処理
        redirectAttributes.addFlashAttribute("errorMessage", "サブスクリプション支払いをキャンセルしました。");
        return "redirect:/user"; // キャンセルページにリダイレクト
    }

    @GetMapping("/checkout")
    public String checkoutPage() {
        // チェックアウトページの表示
        return "user/checkout";
    }

    @PostMapping("/checkout")
    @ResponseBody
    public Map<String, Object> checkout(@RequestBody Map<String, String> payload, HttpServletRequest httpServletRequest) {
        String userEmail = payload.get("email");
        
        try {
        String sessionId = stripeService.createStripeSession(userEmail, httpServletRequest);
        
        System.out.println("UserController checkout" + sessionId);

            return Map.of("sessionId", sessionId);
        } catch (StripeException e) {
        	e.printStackTrace();
        return Map.of("error", e.getMessage());
      }
    }
    
    @PostMapping("/cancel-subscription")
    @ResponseBody
    public Map<String, Object> cancelSubscription(@RequestBody Map<String, String> payload, @AuthenticationPrincipal User user) {
        String subscriptionId = payload.get("subscriptionId");

        try {
            stripeService.cancelSubscription(subscriptionId);
            return Map.of("success", true);
        } catch (StripeException e) {
            return Map.of("success", false, "error", e.getMessage());
        }
    }
    
 // Billingポータルのセッションを作成するエンドポイント
    @PostMapping("/billingPortal")
    @ResponseBody
    public Map<String, Object> billingPortal(@RequestBody Map<String, String> payload, HttpServletRequest httpServletRequest) {
    	Map<String, Object> response = new HashMap<>();
        try {
            String requestUrl = httpServletRequest.getRequestURL().toString();
            String host = requestUrl.replace(httpServletRequest.getRequestURI(), "");
            String returnUrl = host + "/user/paid";  // 編集後に戻るページを設定

            User user = userService.findByUserEmail(payload.get("email"));
            if (user == null || user.getStripeCustomerId() == null) {
              	
                throw new UserNotFoundException("No user found or User does not have a Stripe Customer ID");
            }
            
            String url = stripeService.createBillingPortalSession(user.getStripeCustomerId(), returnUrl);
            response.put("url", url);
        } catch (Exception e) {
          //  logger.error("Error creating billing portal session", e);
            response.put("errorMessage", "Billingポータルの生成でエラーが発生しました。");
        }
        return response;
    }
}
