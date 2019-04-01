官方文档请点击
（https://github.com/newsettle/ns4_gear_idgen/blob/master/docs/ns4_gear_idgen_v1.0.docx）

#### 2018-11-12
 ## ID生成器 版本1.0
 #### 1. 配置 -- ID生成器数据库定制化配置 
        
        key_name(区分业务) 
        key_length(id长度) 
        key_cache(缓存数量) 
        key_prefix(前缀) 
        key_subffix(后缀) 
        key_digit(key生成规则 key_type 2、3 时支持10进制、36进制或者62进制)
        
*  1.1 key_name(区分业务)
    
*  1.2 key_length(id长度) 
    
*  1.3 key_cache(步长 每次分配号段的长度) 
    
*  1.4 key_prefix 可配为 ${date6} / PRE${date6}
    
        e.g. 
         
            ${date6}   yyMMdd
        
            ${date8}   yyMMddHH
        
            ${date10}  yyMMddHHmm
        
            ${date12}  yyMMddHHmmss
        
            ${date14}  yyyyMMddHHmmss
        
            ${date15}  yyMMddHHmmssSSS
        
            ${date16}  yyyMMddHHmmssSSS
        
            ${date17}  yyyyMMddHHmmssSSS
    
*  1.5 key_subffix(后缀) 
    
*  1.6 key_digit(key生成规则 key_type 2、3 时支持10进制、36进制或者62进制)
    
*  1.7 **注意：**   
    *  1.7.1 原则上在应用启动时不修改数据库
          数据库不允许删除key 若删除 请不要在访问
          数据库keyValue不允许变小
            
     
    
 #### 2.访问

*   2.1 获取单个ID 请求方式 http://IP:PORT/getId?keyType=KEY_TYPE&keyName=KEY_NAME    
     
        KEY_TYPE(可选 1 long  2 String)     KEYNAME keyName(按业务区分)
    
    *   2.1.1 KEY_TYPE_LONG=1    
    e.g.   [http://127.0.0.1:8080/getId?keyType=1&keyName=TEST-KEY](http://127.0.0.1:8089/getId?keyType=1&keyName=TEST-KEY)
       
    *   2.1.2 KEY_TYPE_STRING=2  
    e.g.   [http://127.0.0.1:8080/getId?keyType=2&keyName=TEST-KEY](http://127.0.0.1:8089/getId?keyType=2&keyName=TEST-KEY)
         
*   2.2 获取批量ID 请求方式 http://IP:PORT/getId?keyType=KEY_TYPE&keyName=KEY_NAME$$$COUNT  

        KEY_TYPE(3 batch)  KEY_NAME keyName(按业务区分) $$$(分隔符) COUNT(获取ID数量)
    
    *   2.2.1 KEY_TYPE_BATCH=3  
    e.g.   [http://127.0.0.1:8080/getId?keyType=3&keyName=TEST-KEY$$$10](http://127.0.0.1:8089/getId?keyType=3&keyName=TEST-KEY%24%24%2410)
      
 #### 3.性能

*   3.1 测试环境（2核 8G）
    
| ns4.rpc.queue | ns4.rpc.executor| 请求线程数 | keyCache | 吞吐量/sec  | 
|---------------|-----------------|----------|----------|---------|
| 100           | 60              | 160      | 1000     | 192     |
| 100           | 70              | 170      | 1000     | 213     |
| 100           | 80              | 180      | 1000     | 166     |
| 100           | 90              | 190      | 1000     | 180     |
| 100           | 100             | 200      | 1000     | 220     |
| 100           | 110             | 210      | 1000     | 123     | 
| 100           | 120             | 220      | 1000     | 103     | 
| 100           | 130             | 230      | 1000     | 181     | 
| 100           | 140             | 240      | 1000     | 220     | 
| 100           | 150             | 250      | 1000     | 112     | 
| 100           | 160             | 260      | 1000     | 180     | 
| 100           | 170             | 270      | 1000     | 190     | 
| 100           | 180             | 280      | 1000     | 180     | 
| 100           | 190             | 290      | 1000     | 190     | 
| 100           | 200             | 300      | 1000     | 116     | 
| 100           | 250             | 350      | 1000     | 76      | 
| 100           | 300             | 400      | 1000     | 145     | 
| 100           | 350             | 450      | 1000     | 120     | 
| 100           | 400             | 500      | 1000     | 205     | 
| 100           | 450             | 550      | 1000     | 112     | 
| 100           | 500             | 600      | 1000     | 70      | 
| 100           | 550             | 650      | 1000     | 145     | 
| 100           | 600             | 700      | 1000     | 104     | 
|               |                 |          |          |         | 
|               |                 |          |          |         | 
| 200           | 60              | 260      | 1000     | 223     |
| 300           | 60              | 360      | 1000     | 220     |
| 400           | 60              | 460      | 1000     | 220     |
| 1000          | 60              | 1060     | 1000     | 220     |
|               |                 |          |          |         |
|               |                 |          |          |         |
| 200           | 70              | 270      | 1000     | 220     |
|               |                 |          |          |         |
|               |                 |          |          |         |
| 200           | 200             | 400      | 1000     | 230     |
| 200           | 220             | 420      | 1000     | 220     |
| 200           | 250             | 450      | 1000     | 220     |
| 200           | 260             | 460      | 1000     | 204     |
| 200           | 270             | 470      | 1000     | 227     | 
| 200           | 280             | 480      | 1000     | 205     | 
| 200           | 300             | 500      | 1000     | 170     | 
|               |                 |          |          |         |
|               |                 |          |          |         |
| 500           | 500             | 1000     | 1000     | 221     |
| 1000          | 1000            | 2000     | 1000     | 236     |
| 2000          | 2000            | 4000     | 1000     | 150     |
| 3000          | 3000            | 6000     | 1000     | 120     |
| 5000          | 5000            | 10000    | 1000     | 110     |

总结：
    1.用户请求线程数小于等于queue + executor ，若大于 ns4框架响应服务超限
    2.当executor固定 queue 增加 不会吞吐量有影响
    3.queue 约等于 executor时 吞吐量较佳
    4.queue 超过1000 吞吐量有所下降
    5.吞吐量约在230左右
    
*   3.2 准生产环境（双机）
    
| ns4.rpc.queue | ns4.rpc.executor| 请求线程数 | keyCache | 吞吐量/sec  | 
|---------------|-----------------|----------|----------|---------|
| 200           | 200             | 400      | 1000     | 330     |
| 1000          | 1000            | 2000     | 1000     | 314     |
| 1000          | 1000            | 4000     | 1000     | 317     |
