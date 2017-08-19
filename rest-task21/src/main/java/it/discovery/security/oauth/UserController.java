package it.discovery.security.oauth;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
		return Collections.singletonMap("name", principal.getName());
	}
}
