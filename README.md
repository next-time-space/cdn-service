# cdn-service
Content Delivery Network micro service with publish option using certificate authentication. One of the publish repository for Next Time Space Analligence - allows business users to create analytic segments and capture attentions to the segments. 

Download the latest release from https://github.com/next-time-space/cdn-service/releases

# Benifits
* Provides multiple CDN repos, and traffic can be controlled with properties. Like Beta environment 2% traffic and Standard environment 98%
* Provides secure publishing option, exiting repo files can be overwritten easily using upload option
* Provides In-Memory caching to avoid IO operation on every request.
* Provides cache clear startergy on timer basis
* Built on microservice architecture, bring as many servers as you want and brick front end load balancers.
* Open source free to use/modify. Pull requests are most welcome.
* Everthing you need can be configured. Response headers can be configured.


# Configuration

Create a conf.yml file and keep it next to jar file. Example conf.yml file can be found at https://github.com/next-time-space/cdn-service/blob/master/src/main/resources/conf.yml

### yml resource and definations
```
server:
  # Optional: configuration, if this configuration does not exist then you won't be able to publish. 
  ssl-config:
    # SSL port for allowing secure publishing to CDN
    port: 8443
    # server side keystore file and password 
    key-store: /home/next-time-space/ntscerts/server-keystore.jks
    key-store-password: secret
    key-alias: secure-server
    # trust store file and password, publish clients should provide this for successful authentication 
    trust-store: /home/next-time-space/ntscerts/server-truststore.jks
    trust-store-password: secret
  http:
    # Mandatory: port on which CDN server starts. This server will be http protocal, add front end nginx with SSL and route to this as BE 
    port: 8080
repo:
  # Mandatory: array of repo types to have multiple source of CDN files 
  - type: directory
    alias: cdn1
    # to enable or disable publish on CDN
    allow-publish: true
    # split traffic among CDN repository. In this case 60% of traffic will be responded with CDN1
    # makes sure total of all CDN repo is equal to 100
    traffic: 60
    directory:
      # absolute directory path where all files exits to be served, path can be configured with directory structure
      path: /home/next-time-space/cdn1
    response:
      # on every response, headers will be attached. We strongly recommend to use cache headers. Split multiple headers by | and key:value 
      header: "app:beta|Cache-Control:max-age=3600"
    cache-manager:
      # to avoid file reading on every incoming request. Content will be cached on app level. Not mandatory one but we recommend to have this. 
      enable: true
      type: in-memory
      clear-strategy:
        # cache can be cleared on regular intervals using tic property. 
        type: timer
        # in milliseconds 
        tic: 25000
  # added one more cdn repo for example. 
  - type: directory
    alias: cdn2
    allow-publish: true
    traffic: 40
    directory:
      path: /home/next-time-space/cdn1
    response:
      header: "app:prod|Cache-Control:max-age=3600"
    cache-manager:
      enable: true
      type: in-memory
      clear-strategy:
        type: timer
        tic: 25000   
  - type: google-cloud-storage
    traffic: 40
    directory:
      path: /home/next-time-space/cdn2

```
# Creating keystore and truststore

Run all these commands and create all required certificate files.

Server Keystore


	keytool -genkeypair -alias server -keyalg RSA -dname "CN=Name,OU=CDN,O=Next Time Space,L=City,S=State,C=US" -keypass secret -keystore server-keystore.jks -storepass secret


Client Keystore

```
keytool -genkeypair -alias client -keyalg RSA -dname "CN=Name,OU=CDN,O=Next Time Space,L=City,S=State,C=US" -keypass secret -keystore client-keystore.jks -storepass secret
```

Handshake Client and server keystore

```
keytool -exportcert -alias secure-client -file client-public.cer -keystore client-keystore.jks -storepass secret
keytool -importcert -keystore server-truststore.jks -alias clientcert -file client-public.cer -storepass secret
```
Create final client certificate

```
keytool -exportcert -alias secure-server -file server-public.cer -keystore server-keystore.jks -storepass secret
keytool -importcert -keystore client-truststore.jks -alias servercert -file server-public.cer -storepass secret
```

Configure ssl configuration in conf.yml provide file path and passphrase.


# Publishing file

Consider my folder cdn has the following structure.

```
/home/next-time-space/cdn/jquery/1.12.4/jquery.min.js
```
Meta | Value
------------ | -------------
URL | https://localhost:8443/publish/{alias}?path=/jquery/1.12.4/jquery.min.js
Method | POST
Form | 
file | `file path to upload`

If the file or folder structure is new, CDN will create those directory structure and upload the file as requested in `path` query parameter

