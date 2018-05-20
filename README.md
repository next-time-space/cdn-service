# cdn-service
Content Delivery Network micro service with publish option using certificate authentication

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

curl -F file=@/home/pasu/loop http://localhost:8080/publish/cdn1?path=newfile/loop.xml



