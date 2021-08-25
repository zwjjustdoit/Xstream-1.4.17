# Xstream-1.4.17

（以上Xstream Demo环境经本人搭建，漏洞利用环境以JDK1.8_u131为准，现支持本地测试和HTTP远程发包）

XSTREAM&lt;=1.4.17漏洞复现（CVE-2021-39141、CVE-2021-39144、CVE-2021-39150、CVE-2021-39152）

不提供本实验利用POC，仅展示复现利用效果，为避免恶意人员的利用，仅提供官网POC：http://x-stream.github.io/changes.html


### CVE-2021-39141（RCE）
攻击机起一个http服务供Exploit.class的访问，再用marshalsec工具起一个LDAP监听器，如下：

![1](https://user-images.githubusercontent.com/50495555/130566157-959b7c90-58a6-41a9-a920-2648ea1b9345.png)
请求发包即利用成功

![2](https://user-images.githubusercontent.com/50495555/130566165-345a27c7-ea75-4d3a-8011-02b779e9f5f4.png)

### CVE-2021-39144（RCE）
发包即利用

![3](https://user-images.githubusercontent.com/50495555/130566170-333f18e9-60bb-4f86-b1ca-4f18fe7c00fc.png)

### CVE-2021-39150（SSRF）
A Server-Side Forgery Request can be activated unmarshalling with XStream to access data streams from an arbitrary URL referencing a resource in an intranet or the local host.
发包即利用

![4](https://user-images.githubusercontent.com/50495555/130566177-2cbc2fa2-f437-4ac7-8834-d3bd9a63d16e.png)

### CVE-2021-39152（SSRF）
All versions until and including version 1.4.17 are affected, if using the version out of the box with Java runtime version 14 to 8.
发包即利用

![5](https://user-images.githubusercontent.com/50495555/130576671-dcdaa3db-81d1-4623-a1cc-7e520ef33edb.png)

经测试发现内外网的IP或者域名仅能解析/探测一次

![1](https://user-images.githubusercontent.com/50495555/130576798-ac0c21e3-c049-4bdd-b5d9-22a6a800d25e.png)



