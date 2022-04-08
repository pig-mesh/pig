<style>
  .jimu-icon{
      cursor: pointer;
      float: right;
  }
  .jimu-tooltip{
      float: right;
  }
</style>

<script type="text/x-template" id="function-interpretation">
  <div>
     <#--begin 常用函数-->
     <div v-if="text=='SUM'" class="interpretation">
        <p>函数描述： 对可单元格或集合表达式求最大值</p>
        <p>示例：</p>
        <p>例1：=SUM(C6),对C6单元格或数据集合求最大值</p>
        <p>例2：=SUM(A6,C6)，对A6和C6单元格计算最大值</p>
        <p>例3：=SUM(A6:C6)，对A6到C6单元格计算最大值
          <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(0)"><Icon size="14" type="ios-help-circle-outline"/></span>
          </Tooltip>
        </p>
     </div>
    <div v-if="text=='AVERAGE'" class="interpretation">
      <p>函数描述： 对可单元格或集合表达式求平均值</p>
      <p>示例：</p>
      <p>例1：=AVERAGE(C6),对C6单元格或数据集合求平均值</p>
      <p>例2：=AVERAGE(A6,C6)，对A6和C6单元格计算平均值</p>
      <p>例3：=AVERAGE(A6:C6)，对A6到C6单元格计算平均值
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(0)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='MAX'" class="interpretation">
      <p>函数描述： 对可单元格或集合表达式求最大值</p>
      <p>示例：</p>
      <p>例1：=MAX(C6),对C6单元格或数据集合求最大值</p>
      <p>例2：=MAX(A6,C6)，对A6和C6单元格计算最大值</p>
      <p>例3：=MAX(A6:C6)，对A6到C6单元格计算最大值
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(0)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='MIN'" class="interpretation">
      <p>函数描述： 对可单元格或集合表达式求最小值</p>
      <p>示例：</p>
      <p>例1：=MIN(C6),对C6单元格或数据集合求最小值</p>
      <p>例2：=MIN(A6,C6)，对A6和C6单元格计算最小值</p>
      <p>例3：=MIN(A6:C6)，对A6到C6单元格计算最小值
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(0)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='row'" class="interpretation">
      <p>函数描述： 实现自增序号</p>
      <p>示例：</p>
      <p>例1：=row()，单个数据集直接写即可进行自增序号</p>
      <p>例2：=row(1) =row(2)，多个列表需要行号，需要设置一个数字参数区分计数器
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(0)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 常用函数-->

    <#--begin 条件函数-->
    <div v-if="text=='case'" class="interpretation">
      <p>函数描述： 对数据进行判断显示</p>
      <p>示例：</p>
      <p>例1：=case(1>2,1,2),最终结果为1</p>
      <p>例2：=case('1'=='1','男','女')</p>
      <p>最终结果为男，字符串需要加上单引号或者双引号
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(1)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='if'" class="interpretation">
      <p>函数描述：对数据进行判断显示</p>
      <p>示例：</p>
      <p>=(let sex= '1'; </p>
      <p>if(sex== '1'){男}</p>
      <p><span style="color: red">elsif</span>(sex== '2'){女}</p>
      <p>else{未知}
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(1)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 基本函数-->

    <#--begin 日期函数-->
    <div v-if="text=='date'" class="interpretation">
      <p>函数描述：将字符串转换成日期型数据</p>
      <p>示例：</p>
      <p>=date('1982-08-09')</p>
      <p>返回日期：1982-08-09 00:00:00
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='time'" class="interpretation">
      <p>函数描述：将字符串转换成时间型数据</p>
      <p>示例：</p>
      <p>=time('1982-08-09 10:20:30')</p>
      <p>返回时间：10:20:30
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='now'" class="interpretation">
      <p>函数描述：获得系统此刻的日期时间</p>
      <p>示例：</p>
      <p>=now() 返回：1627294758092
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='year'" class="interpretation">
      <p>函数描述：从日期型数据中获得年信息</p>
      <p>示例：</p>
      <p>=year('1972-11-08 10:20:30') 返回：1972
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='month'" class="interpretation">
      <p>函数描述：取得指定日期所在的月份</p>
      <p>示例：</p>
      <p>=month('1972-11-08 10:20:30')</p>
      <p>返回：11
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='day'" class="interpretation">
      <p>函数描述：从日期型数据中获得该日在本月中是几号</p>
      <p>示例：</p>
      <p>day(dateTime(12345)) 返回：1
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='age'" class="interpretation">
      <p>函数描述：计算两个时间间隔的整年数</p>
      <p>示例：</p>
      <p>=age("19800227","yyyyMMdd",3)</p>
      <p>结果为27
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='nowstr'" class="interpretation">
      <p>函数描述：返回当前时间字符串</p>
      <p>示例：</p>
      <p>=nowstr()</p>
      <p>返回结果 2021-07-28 16:31:30
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(2)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 日期函数-->

    <#--begin 数学函数-->
    <div v-if="text=='rand'" class="interpretation">
      <p>函数描述：取得0-1.0之间的一个随机数</p>
      <p>示例：</p>
      <p>例1：=rand() 获得[0, 1.0)之间的一个随机数</p>
      <p>例2：=rand()*100 获得[0, 100)之间的一个随机浮点数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(3)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='round'" class="interpretation">
      <p>函数描述：对数据在指定位置上进行截取，剩余部分四舍五入</p>
      <p>示例：</p>
      <p>例1：=round(3451251.274,0) 返回：3451251.0</p>
      <p>例2：=round(3451251.274,1) 返回：3451251.3"</p>
      <p>例3：=round(3451251.274,-1) 返回：3451250.0
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(3)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='abs'" class="interpretation">
      <p>函数描述：计算参数的绝对值</p>
      <p>示例：</p>
      <p>例1：=abs(-3245.54) </p>
      <p>返回：3245.54</p>
      <p>例2：=abs(-987) 返回：987
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(3)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='floor'" class="interpretation">
      <p>函数描述：对数据在指定位置上进行截取，剩余部分只要有值全舍去</p>
      <p>示例：</p>
      <p>例1：=floor(3451231.234,0)</p>
      <p>返回：3451231.0</p>
      <p>例2：=floor(3451231.234,-1)</p>
      <p>返回：3451230.0</p>
      <p>例3：=floor(3451231.234,1)</p>
      <p>返回：3451231.2
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(3)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='ceil'" class="interpretation">
      <p>函数描述：对数据在指定位置上进行截取，剩余部分只要有值就进位</p>
      <p>示例：</p>
      <p>例1：=ceil(3450001.004,0)</p>
      <p>返回：3450002.0</p>
      <p>例2：=ceil(3450001.004,-1)</p>
      <p>返回：3450010.0</p>
      <p>例3：=ceil(3450001.004,1)</p>
      <p>返回：3450001.1
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(3)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='trunc'" class="interpretation">
      <p>函数描述：对数据取整</p>
      <p>示例：</p>
      <p>例1：=trunc(11.11) 返回：11</p>
      <p>例2：=trunc(-11.99) 返回：-11
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(3)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 数学函数-->

    <#--begin 字符串函数-->
    <div v-if="text=='char'" class="interpretation">
      <p>函数描述：根据给定的unicode编码或者ascii码取得其对应的字符</p>
      <p>示例：</p>
      <p>例1：=char(22269)返回：'国'</p>
      <p>例2：=char(101)返回：'e'
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(4)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='cnmoney'" class="interpretation">
      <p>函数描述：对数据取整</p>
      <p>示例：</p>
      <p>例1：=cnmoney(1.232)返回：壹元贰角叁分</p>
      <p>例2：=cnmoney(1.232,'b')返回：壹佰贰拾叁元贰角整
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(4)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='concat'" class="interpretation">
      <p>函数描述：对字符传进行拼接</p>
      <p>示例：</p>
      <p>=concat('hello ','word',' !')</p>
      <p>返回：hello word !
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(4)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='lower'" class="interpretation">
      <p>函数描述：将字符串转换成小写</p>
      <p>示例：</p>
      <p>=lower(ABCD) 返回：abcd
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(4)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='upper'" class="interpretation">
      <p>函数描述：将字符串转换成大写</p>
      <p>示例：</p>
      <p>=upper(abcd) 返回ABCD
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(4)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='date_str'" class="interpretation">
      <p>函数描述：将日期转成需要的字符串</p>
      <p>示例：</p>
      <p>=date_str('2021-08-24','yyyy')</p>
      <p>返回结果 2021
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(4)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 字符串函数-->
      
    <#--begin 颜色函数-->
    <div v-if="text=='color'" class="interpretation">
      <p>函数描述：设定单元格字体色、背景色</p>
      <p>示例：</p>
      <p>例1：=color('张三', '#ffffff', '#171516') 给张三设置字体颜色和背景颜色
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(5)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    
    <div v-if="text=='rowcolor'" class="interpretation">
      <p>函数描述：设定行字体色、背景色</p>
      <p>示例：</p>
      <p>例1：=rowcolor('张三', '#ffffff', '#171516')</p>
      <p>给张三设置字体颜色和背景颜色
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(5)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 颜色函数-->
      
    <#--begin 系统函数-->
    <div v-if="text=='assert'" class="interpretation">
      <p>=assert(predicate, [msg])：函数描述：断言函数，当 predicate 的结果为 false 的时候抛出 AssertFailed 异常， msg 错误信息可选。
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='sysdate'" class="interpretation">
      <p>返回当前日期对象
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='cmp'" class="interpretation">
      <p>=cmp(x, y)：比较 x 和 y 大小，返回整数，0 表示相等， 1 表达式 x > y，负数则 x < y
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='print'" class="interpretation">
      <p>=print([out],obj)：打印对象,如果指定 out 输出流，向 out 打印， 默认输出到标准输出
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='pst'" class="interpretation">
      <p>=pst([out], e)：打印异常堆栈，out 是可选的输出流，默认是标准错误输出
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='long'" class="interpretation">
      <p>=long(v)：将值转为 long 类型
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='double'" class="interpretation">
      <p>=double(v)：将值转为 double 类型
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='boolean'" class="interpretation">
      <p>=boolean(v)：将值的类型转为 boolean，除了 nil 和 false，其他都值都将转为布尔值 true
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='str'" class="interpretation">
      <p>=str(v)：将值转为 string 类型，如果是 nil（或者 java null），会转成字符串 'null'
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='bigint'" class="interpretation">
      <p>=bigint(x)：将值转为 bigint 类型
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='decimal'" class="interpretation">
      <p>=decimal(x)：将值转为 decimal 类型
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='identity'" class="interpretation">
      <p>=identity(v)：返回参数 v 自身
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='type'" class="interpretation">
      <p>=type(x)：返回参数 x 的类型，结果为字符串，如 string, long, double, bigint, decimal, function 等。Java  类则返回完整类名
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='is_a'" class="interpretation">
      <p>=is_a(x, class)：当 x 是类 class 的一个实例的时候，返回 true，例如 is_a("a", String)，class 是类名
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='is_def'" class="interpretation">
      <p>=is_def(x)：当 x 是类 class 的一个实例的时候,返回 true,例如 is_a("a", String),class 是类名
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='undef'" class="interpretation">
      <p>=undef(x)：“遗忘”变量  x，如果变量 x 已经定义，将取消定义
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='range'" class="interpretation">
      <p>=range(start, end, [step])：创建一个范围，start 到 end 之间的整数范围，不包括 end， step 指定递增或者递减步幅
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='tuple'" class="interpretation">
      <p>=tuple(x1, x2, ...)：创建一个 Object 数组，元素即为传入的参数列表
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='eval'" class="interpretation">
      <p>=eval(script, [bindings], [cached])：对一段脚本文本 script 进行求值
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='comparator'" class="interpretation">
      <p>=comparator(pred)：将一个谓词（返回布尔值）转化为 java.util.Comparator 对象，通常用于 sort 函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='constantly'" class="interpretation">
      <p>=constantly(x)：用于生成一个函数，它对任意（个数）参数的调用结果 x
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='math.abs'" class="interpretation">
      <p>=math.abs(d)：求 d 的绝对值
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='math.round'" class="interpretation">
      <p>=math.round(d)：四舍五入
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='math.floor'" class="interpretation">
      <p>=math.floor(d)：向下取整
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='math.ceil'" class="interpretation">
      <p>=math.ceil(d)：向上取整
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='math.sqrt'" class="interpretation">
      <p>=math.sqrt(d)：求 d 的平方根
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='math.pow'" class="interpretation">
      <p>=math.sqrt(d1,d2)：求 d1 的 d2 次方
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='math.log'" class="interpretation">
      <p>=math.log(d)：求 d 的自然对数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='math.log10'" class="interpretation">
      <p>=math.log10(d)：求 d 以 10 为底的对数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='math.sin'" class="interpretation">
      <p>=math.sin(d)：正弦函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='math.cos'" class="interpretation">
      <p>=math.cos(d)：余弦函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='math.tan'" class="interpretation">
      <p>=math.tan(d)：正切函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='math.atan'" class="interpretation">
      <p>=math.atan(d)：反正切函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='math.acos'" class="interpretation">
      <p>=math.acos(d)：反余弦函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='math.asin'" class="interpretation">
      <p>=math.asin(d)：反正弦函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='date_to_string'" class="interpretation">
      <p>=date_to_string(date,format)：将 Date 对象转化化特定格式的字符
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='string_to_date'" class="interpretation">
      <p>=string_to_date(source,format)：将特定格式的字符串转化为 Date 对 象
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='string.contains'" class="interpretation">
      <p>=string.contains(s1,s2)：判断 s1 是否包含 s2,返回 Boolean
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='string.length'" class="interpretation">
      <p>=string.length(s)：求字符串长度,返回 Long
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='string.startsWith'" class="interpretation">
      <p>=string.startsWith(s1,s2)：s1 是否以 s2 开始,返回 Boolean
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='string.endsWith'" class="interpretation">
      <p>=string.endsWith(s1,s2)：s1 是否以 s2 结尾,返回 Boolean
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='string.substring'" class="interpretation">
      <p>=string.substring(s,begin[,end])：截取字符串 s,从 begin 到 end,如果忽略 end 的话,将从 begin 到结尾
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='string.indexOf'" class="interpretation">
      <p>=string.indexOf(s1,s2)：求 s2 在 s1 中 的起始索引位置,如果不存在为-1
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='string.split'" class="interpretation">
      <p>=string.split(target,regex,[limit])：Java 里的 String.split 方法一致
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='string.join'" class="interpretation">
      <p>=string.join(seq,seperator)：将集合 seq 里的元素以 seperator 为间隔 连接起来形成字符串
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='string.replace_first'" class="interpretation">
      <p>=string.replace_first(s,regex,replacement)：String.replaceFirst 方法
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='string.replace_all'" class="interpretation">
      <p>=string.replace_all(s,regex,replacement)：String.replaceAll 方法
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='repeat'" class="interpretation">
      <p>=repeat(n, x)：返回一个 List，将元素 x 重复 n 次组合而成
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='repeatedly'" class="interpretation">
      <p>=repeatedly(n, f)：返回一个 List，将函数 f 重复调用 n 次的结果组合而成
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='seq.array'" class="interpretation">
      <p>=seq.array(clazz, e1, e2,e3, ...)：创建一个指定 clazz 类型的数组，并添加参数 e1,e2,e3 ...到这个数组并返回。 clazz 可以是类似 java.lang.String 的类型，也可以是原生类型，如 int/long/float 等
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.array_of'" class="interpretation">
      <p>=seq.array_of(clazz, size1, size2, ...sizes)：创建 clazz 类型的一维或多维数组，维度大小为 sizes 指定。clazz 同 seq.array 定义
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='seq.list'" class="interpretation">
      <p>=seq.list(p1, p2, p3, ...)：创建 clazz 类型的一维或多维数组，维度大小为 sizes 指定。clazz 同 seq.array 定义
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.set'" class="interpretation">
      <p>=seq.set(p1, p2, p3, ...)：创建一个 java.util.HashSet 实例，添加参数到这个集合并返回
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.map'" class="interpretation">
      <p>=seq.map(k1, v1, k2, v2, ...)：创建一个 java.util.HashMap 实例，参数要求偶数个，类似 k1,v1 这样成对作为 key-value 存入 map，返回集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.entry'" class="interpretation">
      <p>=seq.entry(key, value)：创建 Map.Entry 对象，用于 map, filter 等函数
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.keys'" class="interpretation">
      <p>=seq.keys(m)：返回 map 的 key 集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='seq.vals'" class="interpretation">
      <p>=seq.vals(m)：返回 map 的 value 集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='into'" class="interpretation">
      <p>=into(to_seq, from_seq)：用于 sequence 转换，将 from sequence 的元素使用 seq.add 函数逐一添加到了 to sequence 并返回最终的 to_seq
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='seq.contains_key'" class="interpretation">
      <p>=seq.contains_key(map, key)：当 map 中存在 key 的时候（可能为 null），返回 true。对于数组和链表，key 可以是 index，当 index 在有效范围[0..len-1]，返回 true，否则返回 false
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='seq.add'" class="interpretation">
      <p>=seq.add(coll, element)：往集合 coll 添加元素，集合可以是 java.util.Collection，也可以是 java.util.Map
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.put'" class="interpretation">
      <p>=seq.put(coll, key, value)：用于设置 seq 在 key 位置的值为 value，seq 可以是 map ，数组或者 List。 map 就是键值对， 数组或者 List 的时候， key 为索引位置整数，value 即为想要放入该索引位置的值
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='seq.remove'" class="interpretation">
      <p>=seq.remove(coll, element)：从集合或者 hash map 中删除元素或者 key
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='seq.get'" class="interpretation">
      <p>=seq.get(coll, element)：从 list、数组或者 hash-map 获取对应的元素值，对于 list 和数组， element 为元素的索引位置（从 0 开始），对于 hash map 来说， element 为 key
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='map'" class="interpretation">
      <p>=map(seq,fun)：将函数 fun 作用到集合 seq 每个元素上, 返回新元素组成的集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='filter'" class="interpretation">
      <p>=filter(seq,predicate)：	将谓词 predicate 作用在集合的每个元素 上,返回谓词为 true 的元素组成的集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='is_empty'" class="interpretation">
      <p>=is_empty(seq)：当集合为空或者 nil，返回 true
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='distinct'" class="interpretation">
      <p>=distinct(seq)：返回 seq 去重后的结果集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='is_distinct'" class="interpretation">
      <p>=is_distinct(seq)：当 seq 没有重复元素的时候，返回 true，否则返回 false
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='include'" class="interpretation">
      <p>=include(seq,element)：判断 element 是否在集合 seq 中,返回 boolean 值，对于 java.uitl.Set 是 O(1) 时间复杂度，其他为 O(n)
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='sort'" class="interpretation">
      <p>=sort(seq, [comparator])：排序集合,仅对数组和 List 有效,返回排序后的新集合，comparator 是一个 java.util.Comparator 实例，可选排序方式
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='reverse'" class="interpretation">
      <p>=reverse(seq)：将集合元素逆序，返回新的集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='reduce'" class="interpretation">
      <p>=reduce(seq,fun,init)：fun 接收两个参数,第一个是集合元素, 第二个是累积的函数,本函数用于将 fun 作用在结果值（初始值为 init 指定)和集合的每个元素上面，返回新的结果值；函数返回最终的结果值
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='take_while'" class="interpretation">
      <p>=take_while(seq, pred)：遍历集合 seq，对每个元素调用 pred(x)，返回 true则加入结果集合，最终返回收集的结果集合。也就是说从集合 seq 收集 pred 调用为 true 的元素
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='drop_while'" class="interpretation">
      <p>=drop_while(seq, pred)：与 take_while 相反，丢弃任何 pred(x) 为 true 的元素并返回最终的结果集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='group_by'" class="interpretation">
      <p>=group_by(seq, keyfn)：对集合 seq 的元素按照 keyfn(x) 的调用结果做分类，返回最终映射 map
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='zipmap'" class="interpretation">
      <p>=zipmap(keys, values)：返回一个 HashMap，其中按照 keys 和 values 两个集合的顺序映射键值对
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.every'" class="interpretation">
      <p>=seq.every(seq, fun)：fun 接收集合的每个元素作为唯一参数，返回 true 或 false。当集合里的每个元素调用 fun 后都返回 true 的时候，整个调用结果为 true，否则为 false
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.not_any'" class="interpretation">
      <p>=seq.not_any(seq, fun)：fun 接收集合的每个元素作为唯一参数，返回 true 或 false。当集合里的每个元素调用 fun 后都返回 false 的时候，整个调用结果为 true，否则为 false
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='seq.some'" class="interpretation">
      <p>=seq.some(seq, fun)：fun 接收集合的每个元素作为唯一参数，返回 true 或 false。当集合里的只要有一个元素调用 fun 后返回 true 的时候，整个调用结果立即为该元素，否则为 nil
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>  
    <div v-if="text=='seq.eq'" class="interpretation">
      <p>=seq.eq(value)：返回一个谓词,用来判断传入的参数是否跟 value 相等,用于 filter 函数,如filter(seq,seq.eq(3)) 过滤返回等于3 的元素组成的集合
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.neq'" class="interpretation">
      <p>=seq.neq(value)：与 seq.eq 类似,返回判断不等于的谓词
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.gt'" class="interpretation">
      <p>=seq.gt(value)：	返回判断大于 value 的谓词
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.ge'" class="interpretation">
      <p>=seq.ge(value)：返回判断大于等于 value 的谓词
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='seq.lt'" class="interpretation">
      <p>=seq.lt(value)：返回判断小于 value 的谓词
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div> 
    <div v-if="text=='seq.le'" class="interpretation">
      <p>=seq.le(value)：返回判断小于等于 value 的谓词
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.nil'" class="interpretation">
      <p>=seq.nil()：返回判断是否为 nil 的谓词
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.exists'" class="interpretation">
      <p>=seq.exists()：返回判断不为 nil 的谓词
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.and'" class="interpretation">
      <p>=seq.and(p1, p2, p3, ...)：组合多个谓词函数，返回一个新的谓词函数，当今仅当 p1、p2、p3 ...等所有函数都返回 true 的时候，新函数返回 true
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>   
    <div v-if="text=='seq.or'" class="interpretation">
      <p>=seq.or(p1, p2, p3, ...)：组合多个谓词函数，返回一个新的谓词函数，当 p1, p2, p3... 其中一个返回 true 的时候，新函数立即返回 true，否则返回 false
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.min'" class="interpretation">
      <p>=seq.min(coll)：返回集合中的最小元素，要求集合元素可比较（实现 Comprable 接口），比较规则遵循 aviator 规则
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='seq.max'" class="interpretation">
      <p>=seq.max(coll)：返回集合中的最大元素，要求集合元素可比较（实现 Comprable 接口），比较规则遵循 aviator 规则
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(6)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 系统函数-->
    
    <#--begin 判断函数-->
    <div v-if="text=='seq.min'" class="interpretation">
      <p>=seq.min(coll)：返回集合中的最小元素，要求集合元素可比较（实现 Comprable 接口），比较规则遵循 aviator 规则
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(7)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='isdate'" class="interpretation">
      <p>定字符串是否具有转换成时间的合法格式</p>
      <p>示例：</p>
      <p>例1：isdate("2006-10-10") 返回：true</p>
      <p>例2：isdate("2006-10-10 10:20:30") 返回：true</p>
      <p>例3：isdate("20061010") 返回：false
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(7)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='istime'" class="interpretation">
      <p>判定字符串是否具有转换成时间的合法格式</p>
      <p>示例：</p>
      <p>例1：istime("2006-10-10") 返回 false</p>
      <p>例2：istime("10:20:30") 返回 true
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(7)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>    
    <div v-if="text=='isnumber'" class="interpretation">
      <p>判定字符串是否具有转换成数值的合法格式</p>
      <p>示例：</p>
      <p>例1：isnumber("abc") 返回：false</p>
      <p>例2：isnumber("1234") 返回：true
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(7)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='intval'" class="interpretation">
      <p>判定当前字符串是否为空</p>
      <p>示例：</p>
      <p>例1：intval("") 返回：0</p>
      <p>例2：intval("1234") 返回：1234
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(7)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='strval'" class="interpretation">
      <p>判定当前字符串是否为空</p>
      <p>示例：</p>
      <p>例1：strval("") 返回：nil</p>
      <p>例2：strval("1234") 返回：1234
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(7)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--end 判断函数-->
    
    <#--#begin 统计函数-->
    <div v-if="text=='DBMAX'" class="interpretation">
      <p>函数描述： 对可扩展单元格集合所有数据进行求最大值</p>
      <p>示例：</p>
      <p>=DBMAX(db.salary)，对编码为db的数据集中的字段salary进行最大值计算
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(8)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='DBSUM'" class="interpretation">
      <p>函数描述： 对可扩展单元格集合所有数据进行求和</p>
      <p>示例：</p>
      <p>=DBSUM（a.name）,a.name 这个单元格所有数据求和
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(8)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='DBMIN'" class="interpretation">
      <p>函数描述： 对可扩展单元格集合所有数据进行求最小值</p>
      <p>示例：</p>
      <p>=DBMIN(db.salary)，对编码为db的数据集中的字段salary进行最小值计算
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(8)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <div v-if="text=='DBAVERAGE'" class="interpretation">
      <p>函数描述： 对可扩展单元格集合所有数据进行求平均值</p>
      <p>示例：</p>
      <p>=DBAVERAGE(db.salary)，对编码为db的数据集中的字段salary进行平均值计算
        <Tooltip  content="文档" placement="left" class="jimu-tooltip">
          <span class="jimu-table-tip jimu-icon" @click="questionMarkClick(8)"><Icon size="14" type="ios-help-circle-outline"/></span>
        </Tooltip>
      </p>
    </div>
    <#--#end 统计函数-->
  </div>
</script>
<script>
  Vue.component('j-function-interpretation', {
    template: '#function-interpretation',
    props: {
      text: {
        type: String,
        required: true,
        default:""
      },
    },
    data(){
      return {
        url:["http://report.jeecg.com/2332214",
             "http://report.jeecg.com/2332216",
             "http://report.jeecg.com/2332217",
             "http://report.jeecg.com/2332218",
             "http://report.jeecg.com/2332219",
             "http://report.jeecg.com/2332221",
             "http://report.jeecg.com/2358337",
             "http://report.jeecg.com/2361961",
             "http://report.jeecg.com/2365251"]
      }
    },
    methods: {
      questionMarkClick(val){
       window.open(this.url[val],"_blank") 
      }
    }
  })
</script>