package com.softbistro.orderbooks;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.softbistro.orderbooks.components.entity.Book;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;

@RestController
public class TemplateController {

	@RequestMapping(value = "/catalog", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getCatalog() throws JsonParseException, JsonMappingException, IOException {
		String jsonText = null;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));

		WebResource wr = client.resource("http://80.91.191.79:19200/catalog/" + "biology");
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		jsonText = response.getEntity(String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readValue(jsonText,
				TypeFactory.defaultInstance().constructCollectionType(List.class, Book.class));
	}

}
