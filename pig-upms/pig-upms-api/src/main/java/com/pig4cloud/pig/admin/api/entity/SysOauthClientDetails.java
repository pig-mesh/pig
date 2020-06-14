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

package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 客户端信息
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOauthClientDetails extends Model<SysOauthClientDetails> {

	private static final long serialVersionUID = 1L;

	/**
	 * 客户端ID
	 */
	@NotBlank(message = "client_id 不能为空")
	@TableId(value = "client_id", type = IdType.INPUT)
	@ApiModelProperty(value = "客户端id")
	private String clientId;

	/**
	 * 客户端密钥
	 */
	@NotBlank(message = "client_secret 不能为空")
	@ApiModelProperty(value = "客户端密钥")
	private String clientSecret;

	/**
	 * 资源ID
	 */
	@ApiModelProperty(value = "资源id列表")
	private String resourceIds;

	/**
	 * 作用域
	 */
	@NotBlank(message = "scope 不能为空")
	@ApiModelProperty(value = "作用域")
	private String scope;

	/**
	 * 授权方式（A,B,C）
	 */
	@ApiModelProperty(value = "授权方式")
	private String authorizedGrantTypes;

	/**
	 * 回调地址
	 */
	@ApiModelProperty(value = "回调地址")
	private String webServerRedirectUri;

	/**
	 * 权限
	 */
	@ApiModelProperty(value = "权限列表")
	private String authorities;

	/**
	 * 请求令牌有效时间
	 */
	@ApiModelProperty(value = "请求令牌有效时间")
	private Integer accessTokenValidity;

	/**
	 * 刷新令牌有效时间
	 */
	@ApiModelProperty(value = "刷新令牌有效时间")
	private Integer refreshTokenValidity;

	/**
	 * 扩展信息
	 */
	@ApiModelProperty(value = "扩展信息")
	private String additionalInformation;

	/**
	 * 是否自动放行
	 */
	@ApiModelProperty(value = "是否自动放行")
	private String autoapprove;

}
