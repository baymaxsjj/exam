# 为考-后端

> 毕业设计：为考-考试系统，后端采用Spring Cloud，前端采用Vue，存储采用Mysql，缓存采用Redis。完成基本的考试功能。

### 系统截图

<table>
    <tr>
        <td><img src="./images/image-20221218155031704.png"/></td>
        <td><img src="./images/image-20221218151201308.png"/></td>
         <td><img src="./images/image-20221218151234715.png"/></td>
         <td><img src="./images/image-20221218151312869.png"/></td>
    </tr>
    <tr>
        <td><img src="./images/image-20230202111804.png"/></td>         
        <td><img src="./images/image-20221218151535396.png"/></td>
         <td><img src="./images/image-20221218151606964.png"/></td>
         <td><img src="./images/image-20221218151632484.png"/></td>
    </tr>
    <tr>
        <td><img src="./images/QQ截图20230202112741.png"/></td>
        <td><img src="./images/QQ截图20230202112753.png"/></td>
         <td><img src="./images/image-20221218152214509.png"/></td>
         <td><img src="./images/image-20221218152256006.png"/></td>
    </tr>
</table>

### 系统

- 框架：SpringBoot、SpringCloud、Mybatis Plus、WebSocket
- 数据库：Mysql
- 缓存：Redis
- 前端：Vue 3、Vite、Pinia、Arco UI

### 完成功能

- [x] 创建课程、添加课程

- [x]  创建班级、查看班级用户、分享班级码

- [x] 课堂互相实时聊天

- [x] 创建题库、题库树形分类

- [x] 创建题目（单、多、判、填、主观）、修改题目选项

- [x] 创建试卷、修改试卷

- [x] 创建考试、修改考试

- [x] 考试控制台、考试概览、老师批阅、数据统计

- [x] 参加考试

- [x] 行为监控

- [x] 实时消息通知

- [x] 考试数据导出

- [ ] 考生数据统计分析
### 使用说明
#### 更换 公钥和私钥
```shell
keytool -genkeypair -alias jwt -validity 3650 -keyalg RSA -dname "CN=jwt,OU=jtw,O=jwt,L=zurich,S=zurich, C=CH" -keypass 12345 -keystore jwt.jks -storepass 12345
```
把这个文件放到认证服务器的resouces目录下

```shell
keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
```
复制公钥到资源服务器的resouces目录下，public.cert
#### 更换 第三方登录信息

  

