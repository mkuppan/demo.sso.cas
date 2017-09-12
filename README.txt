

Server
------

Port Configured : 8443 / SSL enabled
Server Package: cas-overlay-template-master
Build: Maven

WebApp
------

Port Configured : 8080
Server Package: cas-security-spring-boot-sample
Build: Maven

API
------

Port Configured : 8090
Server Package: demo-spring-security-cas-master
Build: Maven


Run in the following sequence using 3 different instance of STS

1) Server
2) WebApp
3) API

Now follow the demo steps below

1) Open browser and type http://localhost:8080 
2) You will be redirected to cas login page
3) Provide userid as "casuser" and password as "Mellon"
4) post authentication you will be landing into home page 
5) click on Get Time button which will access time from API service and show it in browser