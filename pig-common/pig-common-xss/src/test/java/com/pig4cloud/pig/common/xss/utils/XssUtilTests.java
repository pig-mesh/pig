package com.pig4cloud.pig.common.xss.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class XssUtilTests {

	@Test
	void preservesRichTextAndRelativeImages() {
		assertThat(XssUtil.clean("<p><strong>正文</strong><img src=\"/files/image.png\"></p>"))
			.contains("<strong>正文</strong>", "src=\"/files/image.png\"");
	}

	@Test
	void removesScriptsEventHandlersAndJavascriptLinks() {
		assertThat(
				XssUtil.clean("<script>alert(1)</script><a href=\"javascript:alert(1)\" onclick=\"alert(1)\">链接</a>"))
			.doesNotContain("script", "onclick", "href=")
			.contains("链接");
	}

}
