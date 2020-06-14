/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pig4cloud.pig.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2019-11-28
 */
@Component
@Primary
@RequiredArgsConstructor
public class SwaggerProviderConfiguration implements SwaggerResourcesProvider {

	private static final String API_URI = "/v2/api-docs";

	private final RouteLocator routeLocator;

	private final GatewayProperties gatewayProperties;

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		List<String> routes = new ArrayList<>();
		routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
		gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
				.forEach(routeDefinition -> routeDefinition.getPredicates().stream()
						.filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
						.filter(predicateDefinition -> !"pig-auth".equalsIgnoreCase(routeDefinition.getId()))
						.forEach(predicateDefinition -> resources
								.add(swaggerResource(routeDefinition.getId(), predicateDefinition.getArgs()
										.get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", API_URI)))));
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion("2.0");
		return swaggerResource;
	}

}
