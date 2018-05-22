# cdn-service
Content Delivery Network micro service with publish option using certificate authentication.

Download the latest release from https://github.com/next-time-space/cdn-service/releases


# Configuration

Create a conf.yml file and keep it next to jar file. Example conf.yml file can be found at https://github.com/next-time-space/cdn-service/blob/master/src/main/resources/conf.yml

### yml resource and definations
```
server:
  ssl-config:
  	# SSL port for allowing secure publishing to CDN
    port: 8443
    key-store: /home/pasu/ntscerts/server-keystore.jks
    key-store-password: secret
    key-alias: secure-server
    trust-store: /home/pasu/ntscerts/server-truststore.jks
    trust-store-password: secret
  http:
    port: 8080
repo:
  - type: directory
    alias: cdn1
    allow-publish: true
    traffic: 60
    directory:
      path: /home/pasu/cdn
    response:
      header: "app:beta|Cache-Control:max-age=3600"
    cache-manager:
      enable: true
      type: in-memory
      clear-strategy:
        type: timer
        tic: 25000
  
  - type: directory
    alias: cdn2
    allow-publish: true
    traffic: 40
    directory:
      path: /home/pasu/cdn1
    response:
      header: "app:prod|Cache-Control:max-age=3600"
    cache-manager:
      enable1: true
      type: in-memory
      clear-strategy:
        type: timer
        tic: 25000   
  - type: google-cloud-storage
    traffic: 100
    directory:
      path: /home/pasu/cdn

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

