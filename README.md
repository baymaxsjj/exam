# 为考-考试系统

> 毕业设计：为考-考试系统，后端采用Spring Cloud，前端采用Vue，存储采用Mysql，缓存采用Redis。完成基本的考试功能。完成了自动组卷、文本导入题库、对数据进行可视化分析等一系列便捷功能
## 说明
> 本项目为本人毕业设计，未经许可，不可用于任何商业用途。禁止通过该项目进行毕设项目买卖
>
> 第一次接触Spring boot和Cloud，为了尽快的完成该项目，很多功能没有进行太多的规划，代码逻辑较乱。
>
> 接下来不会有太多的维护，即将毕业找工作可真难啊~~。
## 介绍
###  项目地址

为考-考试系统-前端：[https://gitee.com/for-the-exam/exam-web](https://gitee.com/for-the-exam/exam-web)

为考-考试系统-后端：[https://gitee.com/for-the-exam/exam](https://gitee.com/for-the-exam/exam)
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
     <tr>
        <td><img src="./images/QQ截图20230222201506.png"/></td>
        <td><img src="./images/QQ截图20230314161322.png"/></td>
        <td><img src="./images/QQ截图20230314162657.png"/></td>
    </tr>
</table>


### 系统

- 框架：SpringBoot、SpringCloud、Mybatis Plus、WebSocket
- 数据库：Mysql
- 缓存：Redis
- 前端：Vue 3、Vite、Pinia、Arco UI

### 完成功能

- [x] 创建课程、添加课程
- [x] 创建班级、查看班级用户、分享班级码
- [x] 课堂互相实时聊天
- [x] 创建题库、题库树形分类
- [x] 创建题目（单、多、判、填、主观）、修改题目选项
- [x] 创建试卷、修改试卷、遗传算法自动组卷
- [x] 创建考试、修改考试
- [x] 考试控制台、考试概览、老师批阅、数据统计
- [x] 参加考试
- [x] 行为监控
- [x] 实时消息通知
- [x] 考试数据导出
- [x] 考生数据统计分析
# 为考-前端
##  安装pnpm
```
npm install pnpm -g
```
##  安装依赖

```
pnpm install
```

## 运行项目

```
pnpm run dev
```

## 打包

```
//编译
pnpm run build 
```

# 为考后端

## 使用说明

### 下载和启动nacos

```shell
startup.cmd -m standalone
```

### 启动数据库，Redis,并修改项目中的密码

### 更换 公钥和私钥（可选）

```shell
keytool -genkeypair -alias jwt -validity 3650 -keyalg RSA -dname "CN=jwt,OU=jtw,O=jwt,L=zurich,S=zurich, C=CH" -keypass 12345 -keystore jwt.jks -storepass 12345
```
把这个文件放到认证服务器的resouces目录下

```shell
keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
```
复制公钥到资源服务器的resouces目录下，public.cert
### 更换邮箱和密码 （可选）

> fte-service/fte-mails-service/src/main/resources/application.yml

```text
  mail:
    #smtp服务主机  qq邮箱则为smtp.qq.com
    host: smtp.qq.com
    port: 587
    #服务协议
    #    protocol: smtps
    # 编码集
    default-encoding: UTF-8
    nickname: baymax
    #发送邮件的账户
    username: youremail 
    #授权码
    password: password
```

### 更换 第三方登录信息（可选）

> fte-service/fte-user-service/src/main/resources/application.yml

```text
justauth:
  enabled: true
  type:
    gitee:
      client-id: idorkey
      client-secret: idorkey
      redirect-uri: http://127.0.0.1:3030/uapi/public/user/login/GITEE/callback
    qq:
      client-id: idorkey
      client-secret: idorkey
      redirect-uri: http://127.0.0.1:3030/uapi/public/user/login/gitee/callback
    github:
      client-id: idorkey
      client-secret: idorkey
      redirect-uri: http://127.0.0.1:3030/uapi/public/user/login/github/callback
```

### 更改图片上传地址

> 本地通过开启nginx服务进行静态访问，前端在.env文件中更改图片服务器的地址


``` text
# 文件存储服务器地址：建议使用Nginx
VITE_FILE_PATH=http://localhost:10030
```

## 知识点总结
### 自动批卷

### 班级码生成

### 遗传组卷
主要思路参考：[GADemo](https://github.com/jslixiaolin/GADemo)，根据需求更改了大部分的逻辑，如初始化种群的时候根据配置，分配题型等

遗传算法是一种模拟自然界中生物进化过程的优化算法，它可以用来解决一些复杂的组合优化问题，比如自动组卷。自动组卷就是根据一定的规则和要求，从题库中选取合适的试题，生成试卷的过程。

遗传算法自动组卷的基本思想是：将每张试卷看作一个个体，每个个体由一串编码表示，编码中包含了试题的信息。然后对初始种群进行选择、交叉和变异等操作，产生新的种群，并根据适应度函数评价每个个体的质量。重复这个过程直到满足终止条件或达到最大迭代次数，输出最优或较优的个体作为最终试卷。
## 待完善功能

- [ ] 通过定时器，在考试开始前将考试信息放入缓存，考试结束后将未提交的自动提交
- [ ] 自定义第三方登录
- [ ] 校园认证
- [ ] 后台管理功能
