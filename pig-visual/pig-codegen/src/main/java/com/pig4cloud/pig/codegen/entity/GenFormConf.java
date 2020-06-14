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

package com.pig4cloud.pig.codegen.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 生成记录
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@Data
@TableName("gen_form_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "生成记录")
public class GenFormConf extends Model<GenFormConf> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	@ApiModelProperty(value = "ID")
	private Integer id;

	/**
	 * 表名称
	 */
	@ApiModelProperty(value = "表名称")
	private String tableName;

	/**
	 * 表单信息
	 */
	@ApiModelProperty(value = "表单信息")
	private String formInfo;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	@ApiModelProperty(value = "删除标记")
	private String delFlag;

}
