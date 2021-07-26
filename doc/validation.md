###1.后端如何使用注解优雅的进行参数校验
####1.1 常用注解

    @Null(message = "XXXX不能为空") 被注释的元素必须为 null, message尽量要写不然前端不知道是哪个字段
    
    @NotNull(message = "XXXX不能为空") 被注释的元素必须不为 null, message尽量要写不然前端不知道是哪个字段
    
    @Length 被注释的字符串的大小必须在指定的范围内，注意只能用在String上 否则会报错, message尽量要写不然前端不知道是哪个字段
    
    @NotEmpty 被注释的字符串的必须非空，注意只能用在String上 否则会报错, message尽量要写不然前端不知道是哪个字段
    
    @AssertTrue(message = "XXXX") 被注释的元素必须为 true, message尽量要写不然前端不知道是哪个字段
    
    @AssertFalse 被注释的元素必须为 false
    
    @Min(value=L,message="XXXX") 被注释的元素必须是一个数字，其值必须大于等于指定的最小值, message尽量要写不然前端不知道是哪个字段
    
    @Max(value=L,message="XXXX") 被注释的元素必须是一个数字，其值必须小于等于指定的最小值, message尽量要写不然前端不知道是哪个字段
    
    @DecimalMin(value=L,message="XXXX")  被注释的元素必须是一个数字，其值必须大于等于指定的最小值, message尽量要写不然前端不知道是哪个字段
    
    @DecimalMax(value=L,message="XXXX")  被注释的元素必须是一个数字，其值必须小于等于指定的最大值, message尽量要写不然前端不知道是哪个字段
    
    @Size(max, min)  被注释的元素的大小必须在指定的范围内, message尽量要写不然前端不知道是哪个字段
    
    @Digits (integer, fraction) 被注释的元素必须是一个数字，其值必须在可接受的范围内, message尽量要写不然前端不知道是哪个字段
    
    @Past 被注释的元素必须是一个过去的日期, message尽量要写不然前端不知道是哪个字段
    
    @Future 被注释的元素必须是一个将来的日期, message尽量要写不然前端不知道是哪个字段
    
    @Pattern(value) 被注释的元素必须符合指定的正则表达式, message尽量要写不然前端不知道是哪个字段
    
    @Email 被注释的元素必须是电子邮箱地址, message尽量要写不然前端不知道是哪个字段
    
    @Range 被注释的元素必须在合适的范围内, message尽量要写不然前端不知道是哪个字段
    
    @NotBlank 验证字符串非null，且长度必须大于0，注意只能用在String上 否则会报错
    
然后需要在controller方法体添加@Validated 或 @Valid 不加校验会不起作用
![输入图片说明](https://images.gitee.com/uploads/images/2021/0716/165424_ce989231_2351834.png "屏幕截图.png")
####1.2 例子
![输入图片说明](https://images.gitee.com/uploads/images/2021/0716/165100_f3ce1dee_2351834.png "屏幕截图.png")
####1.3 如何扩展（如何自定义校验注解）
（1）定义注解，必须包含message、groups、Payload三个属性
```java
/**
 * 边界值校验
 * @author Lilu
 * @date 2021-7-16 16:57
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { IntegerValidImpl.class})
public @interface IntegerValid {

    int max();//最大值

    int min();//最小值

    String message() default "{你不对劲}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
```
（1）实现接口
```java
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Lilu
 * @date 2021-7-16 16:57
 */
/**
 * 自定义类，用于对校验注解规则的实现
 * 实现 ConstraintValidator 接口，泛型，第一个是对什么注解进行实现，第二个是检验的数据的数据类型 ；
 */
@Component
public class IntegerValidImpl implements ConstraintValidator<IntegerValid, Integer> {

    private int min;
    private int max;

    /**
     * @Description 初始化
     * @Date 2021-7-16 17:09
     * @Param [constraintAnnotation]
     * @return void
     **/
    @Override
    public void initialize(IntegerValid constraintAnnotation) {
        min=constraintAnnotation.min();
        max=constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        System.out.println(value);
        if(value>min&&value<max){
            return true;
        }
        return false;
    }
}
```
（3）使用
```java
@ApiOperation(value = "测试接口", notes = "测试接口")
@PostMapping("test")
public ResponseEntity test(@Validated @RequestBody TestBean testBean){
    return ResponseEntity.ok(testBean);
}


public static class TestBean{

    @IntegerValid(min = 1, max = 20)
    private Integer aa;

    @IntegerValid(min = 1, max = 20)
    private Integer bb;

    public Integer getAa() {
        return aa;
    }

    public void setAa(Integer aa) {
        this.aa = aa;
    }

    public Integer getBb() {
        return bb;
    }

    public void setBb(Integer bb) {
        this.bb = bb;
    }
}
```
###1.3 分组如何使用
    groups可以指定注解使用的场景，一个实体类可能会在多个场合有使用，如插入，删除等。通过groups可以指定该注解在插入/删除的环境下生效。
    payload往往对bean进行使用。
#####例子
(1)定义group最后可以再定义两个接口作为group，代表两种不同的操作。
```java
/**
 * @author Lilu
 * @date 2021-7-16 17:45
 */
public interface Insert {
}
```
```java
/**
 * @author Lilu
 * @date 2021-7-16 17:45
 */
public class Update {
}
``` 
(2)使用
```java
import com.jc.purchase.annotation.IntegerValid;
import com.jc.purchase.annotation.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

/**
 * @author Lilu
 * @date 2021-7-16 17:45
 */
@Api(tags = "通用接口")
@RestController
@RequestMapping("/api/se/general")
public class GeneralController {

    private final Logger log = LoggerFactory.getLogger(GeneralController.class);


    @ApiOperation(value = "测试新增")
    @PostMapping("testAdd")
    public ResponseEntity add(@Validated(Insets.class) @RequestBody TestBean testBean){
        return ResponseEntity.ok(testBean);
    }

    @ApiOperation(value = "测试修改")
    @PutMapping("testPut")
    public ResponseEntity put(@Validated(Update.class) @RequestBody TestBean testBean){
        return ResponseEntity.ok(testBean);
    }


    public static class TestBean{

        @IntegerValid(min = 1, max = 20,groups = Update.class)//修改时验证生效
        private Integer aa;

        @IntegerValid(min = 1, max = 20,groups = Insets.class)//新建时验证生效
        private Integer bb;

        public Integer getAa() {
            return aa;
        }

        public void setAa(Integer aa) {
            this.aa = aa;
        }

        public Integer getBb() {
            return bb;
        }

        public void setBb(Integer bb) {
            this.bb = bb;
        }
    }
}
```
###2.全局异常响应码封装
####2.1 后端代码

![输入图片说明](https://images.gitee.com/uploads/images/2021/0716/163937_8910e3f4_2351834.png "屏幕截图.png")

####2.2 验证未通过请求响应结果
    {
        status: 400,
        code:1,
        msg:""
    }