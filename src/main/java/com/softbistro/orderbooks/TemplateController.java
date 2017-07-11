package com.softbistro.orderbooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TemplateController {

	private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	private CallBackHandler callBackHandler;
	
	
}
