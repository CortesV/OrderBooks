package com.softbistro.orderbooks.components.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.github.messenger4j.send.templates.ListTemplate;
import com.softbistro.orderbooks.CallBackHandler;


@RestController
public class TemplateController {

	private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	private CallBackHandler callBackHandler;
	
	public ListTemplate getListTemplate(){
		return 
	}
}
