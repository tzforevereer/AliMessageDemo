# 1. 技术栈

- java
- springboot
- maven



# 2.进入阿里云官网

https://www.aliyun.com/

## 2.1 登录=>选择进入短信服务

![3.png](https://i.loli.net/2020/04/29/qDPk1Nuoc3E5hSd.png)

## 2.2 开通服务=>进入管理控制台

注：我已经开通服务，所以显示的是管理控制台

![4.png](https://i.loli.net/2020/04/29/CZbhL54caGi3Kn1.png)

 ## 2.3 添加模板管理

**选择 国内消息 - 模板管理 - 添加模板**

![9.png](https://i.loli.net/2020/04/29/xMTbW1wk7IOv3fr.png)

**点击 添加模板，进入到添加页面，输入模板信息**

- 选择模板类型：验证码
- 模板名称：xxxxx (一定要有具体的意义，否则审核不通过)
- 模板内容： 输入框下方可以使用常用模板库
- 申请说明： xxxx (有具体说明)

![10.png](https://i.loli.net/2020/04/29/Unp8hMwgz4YKWsO.png)

**点击提交，等待审核，审核通过后可以使用**

## 2.4 添加签名管理

**选择 国内消息 - 签名管理 - 添加签名**

![5.png](https://i.loli.net/2020/04/29/BCRQvHFPZeWlIds.png)

**点击添加签名，进入添加页面，填入相关信息注意：签名要写的有实际意义**

- 签名
- 使用场景
- 申请说明：可不写

![7.png](https://i.loli.net/2020/04/29/rPLvoeDSkMtXBui.png)

**点击提交，等待审核，审核通过后可以使用**

## 2.5 注意自己的Accesskey

**保存自己的AccessKeyID和AccessKeySecret**

![11.png](https://i.loli.net/2020/04/29/paUqV2YCHb4XIT5.png)

# 3. 项目初始化

## 3.1创建空的maven项目

![1.png](https://i.loli.net/2020/04/29/nC4PEjJ5vUiBHOs.png)

![2.png](https://i.loli.net/2020/04/29/aNtirj6VGuP1Qq2.png)

## 3.2 添加项目依赖

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.2.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.28</version>
        </dependency>
        <dependency>
            <groupId>com.aliyun</groupId>
            <artifactId>aliyun-java-sdk-core</artifactId>
            <version>4.3.3</version>
        </dependency>
        <!--lombok用来简化实体类：需要安装lombok插件-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
            <scope>provided </scope>
        </dependency>
        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <version>2.2.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.7.0</version>
        </dependency>
</dependencies>
```

## 3.3 创建application.properties

```properties
server.port=8001

spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.database= 0
spring.redis.timeout=1800000

spring.redis.lettuce.pool.max-active=20
spring.redis.lettuce.pool.max-wait=-1

spring.redis.lettuce.pool.max-idle=5
spring.redis.lettuce.pool.min-idle=0

spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8
```

## 3.4 创建启动器AliApplication

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //避免不连接数据库报错
public class AliApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliApplication.class,args);
    }
}
```

## 3.5 创建接口定义返回码

```java
public interface ResultCode {

    public static Integer SUCCESS = 20000;

    public  static  Integer ERROR = 20001;
}
```

## 3.5 创建统一结果返回类

```java
@Data
public class R {
    //是否成功
    private Boolean success;

    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private Map<String, Object> data = new HashMap<String, Object>();

    private R(){};

    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }
    public static R error(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }
    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
```
## 3.6 创建utils包下的RandomUtil

```java
package com.demo.aliservice.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * 获取随机数
 * 
 * @author qianyi
 *
 */
public class RandomUtil {

	private static final Random random = new Random();

	private static final DecimalFormat fourdf = new DecimalFormat("0000");

	private static final DecimalFormat sixdf = new DecimalFormat("000000");

	public static String getFourBitRandom() {
		return fourdf.format(random.nextInt(10000));
	}

	public static String getSixBitRandom() {
		return sixdf.format(random.nextInt(1000000));
	}

	/**
	 * 给定数组，抽取n个数据
	 * @param list
	 * @param n
	 * @return
	 */
	public static ArrayList getRandom(List list, int n) {

		Random random = new Random();

		HashMap<Object, Object> hashMap = new HashMap<Object, Object>();

		// 生成随机数字并存入HashMap
		for (int i = 0; i < list.size(); i++) {

			int number = random.nextInt(100) + 1;

			hashMap.put(number, i);
		}

		// 从HashMap导入数组
		Object[] robjs = hashMap.values().toArray();

		ArrayList r = new ArrayList();

		// 遍历数组并打印数据
		for (int i = 0; i < n; i++) {
			int position = Integer.parseInt(String.valueOf(robjs[i]));
			r.add(list.get(position));
		}
		return r;
	}
}

```

## 3.7 创建servie接口和service的实现类

service接口

```java
public interface AliService {
    boolean send(Map<String, Object> param, String phone);
}
```

service的实现了

```java
@Service
public class AliServiceImpl implements AliService {
    public boolean send(Map<String, Object> param, String phone) {
        if(StringUtils.isEmpty(phone)) return false;

                /**
        参数一，default
        参数二，阿里云的AccessKeyID
        参数三，阿里云的AccessKeySecret：
        **/
        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAaASQDF1CGTEEFGUXZHMA", "WRTSbya6qe1SDS6qwe13aWSH0");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关固定的参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关的参数
        request.putQueryParameter("PhoneNumbers",phone); //手机号
        request.putQueryParameter("SignName","我的xxx在线网站"); //申请阿里云 签名名称
        request.putQueryParameter("TemplateCode","SMS_139315701"); //申请阿里云 模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); //验证码数据，转换json数据传递

        try {
            //最终发送
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

## 3.8 创建控制器controller

```java
@RestController
@RequestMapping("/msm")
public class AliController {
    @Autowired
    private AliService aliService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("send/{phone}")
    public R sedMsm(@PathVariable String phone){
        // 1. 从redis中获取验证码，获取不到就发送
        String code1 = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code1)){
            return R.ok();
        }

        //生成随机值，传递阿里云进行发送
        String code = RandomUtil.getFourBitRandom();

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("code",code);

        boolean isSend =  aliService.send(param,phone);
        if(isSend){
            //发送成功，把成功的验证码放到redis中，设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else{
            return R.error().message("短信发送失败");
        }

    }
}
```

## 3.9整体项目结构

![13.png](https://i.loli.net/2020/04/29/Pr9ha5bpmUlZSiL.png)

# 4. 运行代码并使用postman调试

![12.png](https://i.loli.net/2020/04/29/Ii3UdsYqk51M84D.png)

收到信息：
![13jpg.jpg](https://i.loli.net/2020/04/29/geWFKq9v35lGkxN.jpg)
