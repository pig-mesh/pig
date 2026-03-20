package com.pig4cloud.pigx.auth.template;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LoginTemplateTests {

	@Test
	void shouldRenderLoginTemplateWhenTenantCaptchaJsonIsPresent() throws Exception {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_33);
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));

		Template template = configuration.getTemplate("ftl/login.ftl");
		StringWriter writer = new StringWriter();

		template.process(Map.of("tenantList", List.of(Map.of("id", 1L, "name", "默认租户")), "selectedTenantId", 1L,
				"showCaptcha", true, "tenantCaptchaEnabledJson", "{\"1\":true}", "authClientId", "demo-client",
				"error", "<script>alert(1)</script>"),
				writer);

		assertThat(writer.toString()).contains("const tenantCaptchaEnabledMap = JSON.parse('{\\\"1\\\":true}')")
			.contains("&lt;script&gt;alert(1)&lt;/script&gt;").contains("option value=\"1\" selected")
			.contains("name=\"client_id\" value=\"demo-client\"")
			.contains("maxlength=\"6\"").contains("inputmode=\"numeric\"")
			.contains("看不清，再换一张").contains("replace(/\\D/g, '').slice(0, 6)");
	}

}
