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
package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenDatasourceConf;

/**
 * 数据源表
 *
 * @author lengleng
 * @date 2019-03-31 16:00:20
 */
public interface GenDatasourceConfService extends IService<GenDatasourceConf> {

	/**
	 * 保存数据源并且加密
	 * @param genDatasourceConf
	 * @return
	 */
	Boolean saveDsByEnc(GenDatasourceConf genDatasourceConf);

	/**
	 * 更新数据源
	 * @param genDatasourceConf
	 * @return
	 */
	Boolean updateDsByEnc(GenDatasourceConf genDatasourceConf);

	/**
	 * 更新动态数据的数据源列表
	 * @param datasourceConf
	 * @return
	 */
	void addDynamicDataSource(GenDatasourceConf datasourceConf);

	/**
	 * 校验数据源配置是否有效
	 * @param datasourceConf 数据源信息
	 * @return 有效/无效
	 */
	Boolean checkDataSource(GenDatasourceConf datasourceConf);

	/**
	 * 通过数据源名称删除
	 * @param dsId 数据源ID
	 * @return
	 */
	Boolean removeByDsId(Integer dsId);

}
