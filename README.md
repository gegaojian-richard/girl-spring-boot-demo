# Spring Boot

1. 无需xml，简化配置
2. 入门级微框架，Spring Cloud是建立在spring boot基础之上的
3. 与spring mvc没有必然的联系，可以说spring boot是spring mvc的升级版

[TOC]

## Spring基础

### Spring 应用启动方式

1. 利用IDEA的run 主类
2. 在项目目录下 终端中输入： mvn spring-boot:run
3. 先使用mvn install 指令编译，然后到target目录下使用java -jar 生成的jar包 的方式

### Spring 应用程序配置文件

#### 两种类型的配置文件类型

1. application.properties  -  繁琐
2. Application.yml   -  简明 **推荐**

#### 程序中读取使用配置文件中的key - value值

- 使用@Value("${key-name}")注入到字段中，不分字符串还是数字

- 使用@ConfigurationProperties(prefix = "..")，将前缀为 “..”的配置注入到其所注解的类的实例中，同时使用@Component注解将类的单例添加到Spring容器中

```java
@ConfigurationProperties(prefix="server")
public class ServerProperties {

	private String name;

	private Host host;

	// ... getter and setters

    // 可以嵌套类
	public static class Host {

		private String ip;

		private int port;

		// ... getter and setters

	}

}
```

> 注意：需要添加spring-boot-configuration-processor依赖

#### 多配置文件设置

- application.yml
  - 通过设置spring.profiles.active = prod/dev, 来选择使用哪个配置文件
- application-dev.yml
- application-prod.yml

在使用mvn install之后，启动项目的时候可以通过以下方式来区分开发环境和生成环境中配置文件的使用：

```shell
java -jar jar包名.jar --spring.profiles.active=prod
java -jar jar包名.jar --spring.profiles.active=dev
```



## Controller的使用

使用spring-boot-starter-web依赖

#### @Controller

处理http请求

如果方法上不加@ResponseBody，则返回的是模板引擎中的页面

#### @RestController

等同于@ResponseBody + @Controller

专门用来返回json

#### @RequestMapping

配置url映射

作用于方法之上 

```java
@RequestMapping(value = "/...", method = RequestMethod.GET) // 等同于下面的注解
@GetMapping(value = "/...")
```

#### @PathVariable

将url中的数据注入到其所注解的字段中

1. 首先将url中需要获取的内容用大括号包裹
2. 在@PathVariable中使用

```java
@RequestMapping(value = "/hello/{id}", method = RequestMethod.GET)
    public String say(@PathVariable("id") int id){
        return "Hello, " + girlProperties.getCupSize() + " cupSize and " + girlProperties.getAge() + " years old girl.";
    }

// 访问: /hello/100
```

#### @RequestParam

将**请求参数**的值注入到其所注解的字段中

```java
@RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say(@RequestParam(name = "id", required = false, defaultValue = "0") int id){
        return "Hello, " + girlProperties.getCupSize() + " cupSize and " + girlProperties.getAge() + " years old girl.";
    }

// 访问: /hello?id=100
```



## JPA

JPA(Java Persistence API)定义了一系列对象持久化的标准，不是组件也不是系统。

实现这一规范的产品有Hibernate、TOPLINK

而Spring-data-jpa就是对Hibernate的一个整合

> 添加 *spring-boot-starter-data-jpa* 依赖，以及 *mysql-connector-java* 依赖

#### 定义领域模型Entity

```java
@Entity
public class Girl {
    @Id
    @GeneratedValue
    private Integer id;
    private String cupSize;
    private Integer age;

    public Girl() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCupSize() {
        return cupSize;
    }

    public void setCupSize(String cupSize) {
        this.cupSize = cupSize;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```



#### 创建Repository

```java
public interface GirlRepository extends JpaRepository<Girl, Integer> {
}
```

#### Repository的使用

```java
@Autowired
private GirlRepository girlRepository;

/**
* 查询所有女生列表
* @return
*/
@GetMapping(value = "/girls")
public List<Girl> girlList(){
    return girlRepository.findAll();
}

/**
* 添加一个女生
* @param cupSize
* @param age
* @return
*/
@PostMapping(value = "/addgirl")
public Girl girlAdd(@RequestParam("cupSize") String cupSize,
                    @RequestParam("age") Integer age){
    Girl girl = new Girl();
    girl.setCupSize(cupSize);
    girl.setAge(age);

    return girlRepository.save(girl);
}

/**
* 上面的方法可以用下面对象那个的方式直接完成
* 添加一个女生
* @param girl
* @return
*/
@PostMapping(value = "/addgirl")
public Girl girlAdd(Girl girl){
    return girlRepository.save(girl);
}

/**
* 添加一个女生,验证girl，验证结果在BindingResult对象中
* 需要在Girl类中添加一些验证信息
* 另外这里浏览器http请求的结果是200
* @param girl
* @return
*/
@PostMapping(value = "/addgirl")
public Girl girlAdd(@Valid Girl girl, BindingResult bindingResult){
    if (bindingResult.hasErrors()){
        System.out.println(bindingResult.getFieldError().getDefaultMessage());
        return null;
    }

    return girlRepository.save(girl);
}

/**
* 根据id获取
* @param id
* @return
*/
@GetMapping("/girls/{id}")
public Girl getGirl(@PathVariable("id") Integer id){
    return girlRepository.findById(id).get();
}

/**
* 更新
* @param id
* @param cupSize
* @param age
* @return
*/
@PutMapping(value = "/girls/{id}")
public Girl girlUpdate(@PathVariable("id") Integer id,
                       @RequestParam("cupSize") String cupSize,
                       @RequestParam("age") Integer age){
    Girl girl = new Girl();
    girl.setId(id);
    girl.setCupSize(cupSize);
    girl.setAge(age);

    return girlRepository.save(girl);
}

/**
* 根据id删除
* @param id
*/
@DeleteMapping(value = "/girls/{id}")
public void girlDelete(@PathVariable("id") Integer id){
    girlRepository.deleteById(id);
}

/**
* 根据年龄查询
* @param age
* @return
*/
@GetMapping(value = "/girls/age/{age}")
public List<Girl> girlListByAge(@PathVariable("age") Integer age){
    // 需要到GirlRepository中扩展一个findByAge方法
    return girlRepository.findByAge(age);
} 
```

#### @Transactional

将方法中的数据库操作原子化

## 表单验证

1. 在领域模型类的字段定义上使用*javax.validation.constraints*包中的注解

```java
@Entity
public class Girl {
    @Id
    @GeneratedValue
    private Integer id;

    private String cupSize;

    @NotEmpty(message = "必须输入金额！")
    private String money;

    @Min(value = 18, message = "未成年少女禁止进入！")
    private Integer age;

    // constructor、getandset、toString...
}

```

1. 在创建领域模型对象，注入值得时候使用*javax.validation.*包中的@Valid注解进行验证

```java
/**
* 添加一个女生
* @param girl
* @return
*/
@PostMapping(value = "/addgirl")
public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
    Result result = new Result();

    if (bindingResult.hasErrors()){
        result.setCode(1);
        result.setMsg(bindingResult.getFieldError().getDefaultMessage());
        return result;
    }

    result.setCode(0);
    result.setMsg("成功");
    result.setData(girl);

    return result;
}
```

## Sring AOP 日志记录

> [SpringAOP专栏][待建设]

切面任务：

1. 拦截所有的Controller中的方法
2. 在所有被拦截的方法执行前记录：
   1. http请求访问的url
   2. http请求的方法
   3. 客户端IP
   4. 类名.方法名
   5. 参数
3. 在所有被拦截的方法返回后记录方法的返回值

```java
@Aspect
@Component
public class LogAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut(value = "execution(public * com.gegaojian.girl.controller.*.*(..))")
    public void controllerMethods(){

    }

    @Before("controllerMethods()")
    public void beforeLog(JoinPoint joinPoint){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // url
        logger.info("url = {}", request.getRequestURL());

        // method
        logger.info("method = {}", request.getMethod());

        // ip
        logger.info("ip = {}", request.getRemoteAddr());

        // 类方法
        logger.info("class_method = {}", joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName());

        // 参数
        logger.info("args = {}", joinPoint.getArgs());
    }


    @AfterReturning(pointcut = "controllerMethods()", returning = "object")
    public void AfterControllerMethods(Object object){
        logger.info("response = {}", object.toString());
    }
}
```

- 使用@Pointcut注解定义切点（JoinPoint的集合）,并命名，即这里的**controllerMethods**
- 使用Advice增强，编写切面的逻辑，这里使用了@Before、@AfterReturning这两种增强

## 统一异常处理

### 统一返回结果

```json
// 失败
{
    "code": errorCode,
    "msg": "...",
    "data": null
}

// 成功
{
    "code": 0,
    "msg": "成功",
    "data": {
        "key":value
        ...
    }  // 无则为null
}
```

- 领域模型定义：

```java
/**
 * http请求返回的最外层对象
 */
public class Result<T> {
    /**
     * 错误码
     * 0 - 成功
     * 1 - 错误
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 具体内容
     */
    private T data;

    // getter and setter..
}
```

- 进一步封装创建过程，静态工厂类：

```java
public class ResultUtil {

    /**
     * 创建有返回结果的成果结果对象
     * @param object
     * @return
     */
    public static Result success(Object object){
        Result result = new Result();

        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);

        return result;
    }

    /**
     * 创建无返回结果的成功结果对象
     * @return
     */
    public static Result success(){
        return success(null);
    }

    /**
     * 创建失败结果对象
     * @param code
     * @param msg
     * @return
     */
    public static Result error(Integer code, String msg){
        Result result = new Result();

        result.setCode(code);
        result.setMsg(msg);

        return result;
    }
}
```

- 使用统一返回结果

```java
/**
* 添加一个女生
* @param girl
* @return
*/
@PostMapping(value = "/addgirl")
public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
    if (bindingResult.hasErrors()){
        return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
    }

    return ResultUtil.success(girlRepository.save(girl));
}
```

### 定义领域异常

>  Spring中只有抛出RuntimeException才会进行事务回滚, 所以必须扩展RuntimeException

```java
public class GirlException extends RuntimeException{
    Integer code;

    public GirlException(ExceptionCodeEnum exceptionCodeEnum) {
        super(exceptionCodeEnum.getMsg());
        this.code = exceptionCodeEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
```

### 异常代码统一管理

```java
/**
 * 异常代码统一维护
 */
public enum ExceptionCodeEnum {
    UNKONW_ERROR(-1, "未知错误而"),
    SUCCESS(0, "成功"),
    PRIMARY_SCHOOL(100, "小学"),
    MIDDLE_SCHOOL(101, "中学");

    private Integer code;
    private String msg;

    ExceptionCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
```

### 异常统一处理

> @ControllerAdvice + @ExceptionHandler

```java
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e){
        if (e instanceof GirlException){
            GirlException girlException = (GirlException) e;
            return ResultUtil.error(girlException.getCode(), girlException.getMessage());
        }else {
            logger.error("[系统异常] {}" + e);
            return ResultUtil.error(-1, "未知错误");
        }
    }
}
```



## 单元测试

### service测试

直接使用IDEA自动生成

1. 使用IDEA自动生成service类的测试类

2. 添加@RunWith(SpringRunner.class)和@SpringBootTest注解

3. 注入Service

4. 编写测试逻辑

5. 通过Assert断言来判断结果是否与预期一样

   ```java
   Assert.assertEquals(Object except, Object actual);
   ```


### API测试

使用MockMvc类来模拟Http请求

添加@AutoConfigureMockMvc注解

注入MockMvc类

```java
// 使用
mvc.perform(MockMvcRequestBuilders.get("url")).andExpect(MockMvcResultMatchers.content().string("expectedContent")); // status
```



### 运行测试

项目打包时自动进行所有的单元测试 *mvn clean package*

打包时跳过单元测试 *mvn clean package -Dmaven.test.skip=true*
