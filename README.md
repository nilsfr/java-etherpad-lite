Java Etherpad Lite Client
=========================

A Java client for Etherpad Lite’s HTTP JSON API.
Etherpad Lite is a collaborative editor provided by the Etherpad Foundation.

See the [Etherpad lite project](https://github.com/ether/etherpad-lite) for information on how to
install and configure your own Etherpad Lite instance, and read the
[Etherpad documentation](http://etherpad.org/doc/v1.5.7/) for an in-depth description of
Etherpad Lite’s HTTP API.

### DOWNLOAD ###
The jar file is available in [Maven Central](http://search.maven.org/#artifactdetails|net.gjerull.etherpad|etherpad_lite_client|1.2.12|jar).

Add this to your maven pom:
```xml
<dependency>
    <groupId>net.gjerull.etherpad</groupId>
    <artifactId>etherpad_lite_client</artifactId>
    <version>1.2.12</version>
</dependency>
```

### DOCUMENTATION ###
For now, the best documentation is reading through the methods available in the
[EPLiteClient class](https://raw.githubusercontent.com/nilsfr/java-etherpad-lite/master/src/main/java/net/gjerull/etherpad/client/EPLiteClient.java)
The methods are 1:1 with the EPLite API methods.

### DEPENDENCIES ###
Depends on JSON.simple (https://github.com/fangyidong/json-simple).

### NOTES ###
Latest release currently targets Etherpad Lite API v1.2.12.
The plan is to keep up with the most recent version of the Etherpad Lite API.
(Note that we're talking about API versions here, not release versions).

### EXAMPLE ###
```java
EPLiteClient client = new EPLiteClient("http://localhost:9001", "K8OF91QMQYUvrNu3e9rJ7FnnVgaB3m9q");

// Create pad and set text
client.createPad("my_pad");
client.setText("my_pad", "foo!!");

// Get pad text
String text = client.getText("my_pad").get("text").toString();

// Get list of all pad ids
Map result = client.listAllPads();
List padIds = (List) result.get("padIDs");
```

### INTEGRATION TESTING ###
Integration testing requires a copy of EtherpadLite running at http://localhost:9001 with an API key
of a04f17343b51afaa036a7428171dd873469cd85911ab43be0503d29d2acbbd58

Integration tests are not run using `maven test`.
