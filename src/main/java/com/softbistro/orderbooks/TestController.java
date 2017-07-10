package com.softbistro.orderbooks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/orderbooks/v1/")
public class TestController {

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public String test() {
		return "Hello";
	}
}
