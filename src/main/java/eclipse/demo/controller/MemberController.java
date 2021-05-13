package eclipse.demo.controller;

import eclipse.demo.domain.Member;
import eclipse.demo.dto.MemberDto;
import eclipse.demo.exception.ControllerException;
import eclipse.demo.exception.CustomExceptionHandler;
import eclipse.demo.service.FilesService;
import eclipse.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final FilesService filesService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberDto());
        return "members/signUpMember";
    }


    @PostMapping("/members/new")
    public String create(@Valid @ModelAttribute("memberForm") MemberDto form, BindingResult result) throws ControllerException {

        if (result.hasErrors()) {
            return "members/signUpMember";
        }

        if (form.getPassword() == form.getPassword_confirm()){
            throw new ControllerException("비밀번호가 중복입니다.");
        }

        memberService.join(new Member(form.getUsername(), passwordEncoder.encode(form.getPassword()), form.getNickname(), true));

        return "redirect:/members";
    }

    @GetMapping("/members")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new MemberDto());
        return "members/login";
    }

    @GetMapping("/members/profile")
    public String proFile(@AuthenticationPrincipal Member member, Model model){
        Member findMember = memberService.findOne(member.getId());

        model.addAttribute("form", new MemberDto());
        model.addAttribute("memberForm", findMember);

        return "members/profile";
    }

    @GetMapping("/members/edit_password")
    public String edit_password(){
        return "members/edit_password";
    }

    @PostMapping("/members/update_profile")
    public String update_profile(@AuthenticationPrincipal Member member, MemberDto form) throws Exception {

        memberService.update(member.getId(),form.getUsername(),form.getNickname());

        return "redirect:/";
    }

}
