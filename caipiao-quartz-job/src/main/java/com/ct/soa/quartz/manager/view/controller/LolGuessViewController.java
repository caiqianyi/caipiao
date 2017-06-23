package com.ct.soa.quartz.manager.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/lol/guess")
public class LolGuessViewController {
	
	@RequestMapping(value="lpl_current", method=RequestMethod.GET)
	public String lpl_current(){
		return "/lol/guess/lpl_current.jsp";
	}
}