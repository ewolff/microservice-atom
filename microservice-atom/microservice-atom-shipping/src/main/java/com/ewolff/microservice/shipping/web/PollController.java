package com.ewolff.microservice.shipping.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ewolff.microservice.shipping.poller.OrderPoller;

@Controller
public class PollController {

	private OrderPoller poller;

	@Autowired
	public PollController(OrderPoller poller) {
		this.poller = poller;
	}

	@RequestMapping(value = "/poll", method = RequestMethod.POST)
	public String poll() {
		poller.poll();
		return "redirect:/";
	}

}
