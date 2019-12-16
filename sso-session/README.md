# 同一域名，多台主机共享session
> session信息根据自定义sessionid存储在redis中
> 自定义session：MySession
> 自定义cookie字段：psession  （即自定义的服务端id）

1. 用户第一次filter判断登录状态（访问模块A时），
2. 未登录用户跳转至登录页面登录，校验信息登录成功，
3. 保存登录对象至MySession属性中，
A模块没有自定义MySession，便新建空的、只包含id的mySession，然后赋值
4. 将mySession中id指定为psession-id生成cookie到客户端
5. 至此，新生成了用户对象保存在了MySession中，cookie中有自定义id
6. filter返回客户端之前，将mySession信息保存至redis中
7. 用户第二次访问模块B（此时B中无自定义MySession）
8. filter能获取到cookie中psessionid，根据psessionid能从redis中获取到用户信息
，B模块新建MySession并存入用户信息；B模块登录状态亦确定

```
Cookie cookie = new Cookie(COOKIE_NAME_SESSION, sessionId);
```
