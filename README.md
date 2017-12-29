[![Build Status][travis-badge]][travis-badge-url]

![](./img/etcd.png)

Spring etcd Example
==================================
This is an example of using [etcd v3.0](https://coreos.com/etcd/) with Spring 
Boot. The application uses [jetcd v0.0.1](https://github.com/coreos/jetcd), 
a Java client for etcd, to communicate with the database.

### Introduction to etcd
etcd is a distributed key-value store. It is written in Go and uses [Raft](https://raft.github.io/)
protocol to communicate between different nodes in the cluster. `Raft` is a 
protocol for implementing distributed consensus. 

`etcd` has a well-defined API to interact with clients. It uses [gRPC](https://grpc.io/)
protocol to communicate with clients. As with any RPC (Remote Procedure Call)
application, a `gRPC` client invokes methods remotely on services defined in a 
server.  `gRPC` uses protocol buffers for serializing data across the wire. 


![](./img/etcd-architecture.svg)

etcd is widely used in applications such as [Kubernetes](http://kubernetes.io/).
   
### etcd Installation
You can download the the prebuilt etcd binary [here](https://github.com/coreos/etcd/releases/).
Binaries are available for OSX, Linux, Windows, etc.

Once you download the zip (or tar) file, uncompress it and save it in a folder
of your choice. In my example, the files are saved in a folder named 
`etcd-v3.3.0`.

### Running etcd
To start th etcd server, naivigate to the folder where you saved the 
downloaded binaries earlier. In this example, the saved folder is `etcd-v3.3.0`.

Type the following command to start a single-member cluster of `etcd`,

```sh
etcd
```

It will bring up the etcd server listening on port `2379` for client 
communication and on port `2380` for server-to-server communication.

```
$ ./etcd
2017-12-28 10:39:28.289152 I | etcdmain: etcd Version: 3.3.0
2017-12-28 10:39:28.289235 I | etcdmain: Git SHA: f7a395f
2017-12-28 10:39:28.289253 I | etcdmain: Go Version: go1.9.2
2017-12-28 10:39:28.289258 I | etcdmain: Go OS/Arch: darwin/amd64
2017-12-28 10:39:28.289264 I | etcdmain: setting maximum number of CPUs to 8, total number of available CPUs is 8
2017-12-28 10:39:28.289276 N | etcdmain: failed to detect default host (default host not supported on darwin_amd64)
2017-12-28 10:39:28.289287 W | etcdmain: no data-dir provided, using default data-dir ./default.etcd
2017-12-28 10:39:28.289355 N | etcdmain: the server is already initialized as member before, starting as etcd member...
2017-12-28 10:39:28.289689 I | embed: listening for peers on http://localhost:2380
2017-12-28 10:39:28.289847 I | embed: listening for client requests on localhost:2379
2017-12-28 10:39:28.290816 I | etcdserver: name = default
2017-12-28 10:39:28.290827 I | etcdserver: data dir = default.etcd
2017-12-28 10:39:28.290834 I | etcdserver: member dir = default.etcd/member
2017-12-28 10:39:28.290842 I | etcdserver: heartbeat = 100ms
2017-12-28 10:39:28.290848 I | etcdserver: election = 1000ms
2017-12-28 10:39:28.290853 I | etcdserver: snapshot count = 100000
2017-12-28 10:39:28.290862 I | etcdserver: advertise client URLs = http://localhost:2379
2017-12-28 10:39:28.468847 I | etcdserver: restarting member 8e9e05c52164694d in cluster cdf818194e3a8c32 at commit index 17
2017-12-28 10:39:28.468922 I | raft: 8e9e05c52164694d became follower at term 4
2017-12-28 10:39:28.468942 I | raft: newRaft 8e9e05c52164694d [peers: [], term: 4, commit: 17, applied: 0, lastindex: 17, lastterm: 4]
2017-12-28 10:39:28.476762 W | auth: simple token is not cryptographically signed
2017-12-28 10:39:28.477581 I | etcdserver: starting server... [version: 3.3.0, cluster version: to_be_decided]
2017-12-28 10:39:28.477714 E | etcdserver: cannot monitor file descriptor usage (cannot get FDUsage on darwin)
2017-12-28 10:39:28.478147 I | etcdserver/membership: added member 8e9e05c52164694d [http://localhost:2380] to cluster cdf818194e3a8c32
2017-12-28 10:39:28.478265 N | etcdserver/membership: set the initial cluster version to 3.3
2017-12-28 10:39:28.478321 I | etcdserver/api: enabled capabilities for version 3.3
2017-12-28 10:39:29.070709 I | raft: 8e9e05c52164694d is starting a new election at term 4
2017-12-28 10:39:29.070760 I | raft: 8e9e05c52164694d became candidate at term 5
2017-12-28 10:39:29.070793 I | raft: 8e9e05c52164694d received MsgVoteResp from 8e9e05c52164694d at term 5
2017-12-28 10:39:29.070815 I | raft: 8e9e05c52164694d became leader at term 5
2017-12-28 10:39:29.070838 I | raft: raft.node: 8e9e05c52164694d elected leader 8e9e05c52164694d at term 5
2017-12-28 10:39:29.071171 I | embed: ready to serve client requests
2017-12-28 10:39:29.071203 I | etcdserver: published {Name:default ClientURLs:[http://localhost:2379]} to cluster cdf818194e3a8c32
2017-12-28 10:39:29.072107 N | embed: serving insecure client requests on 127.0.0.1:2379, this is strongly discouraged!
```
### Running etcdctl
`etcdctl` is a command line client for `etcd`. Before using `etcdctl`, set the
environment variable,`ETCDCTL_API`, to use version 3 API of etcd.

```bash
export ETCDCTL_API=3
```

The `etcdctl` tool is located in the same folder as `etcd`. 

#### PUT Command
Use `PUT` command to create a new key-value pair. Use the following coomand to 
create a key named `foo` with a value named `bar`.

```bash
./etcdctl put foo bar 
```

It should print out `OK` if everything goes well.

#### GET Command
Use `GET` command to retrieve the value,

```bash
./etcdctl get foo
```

The output should be,

```bash
foo
bar
```

There shouldn't be any output if no key is found.

To retrieve all keys, provide an empty string as key,

```bash
 ./etcdctl get "" --prefix=true
```

Here is the output,

```bash
bc168652-9f13-4866-bab2-158e27fe19f5
{"id":"bc168652-9f13-4866-bab2-158e27fe19f5","title":"Indra-1","author":"Indra Basak"}
c5413351-c292-4fdf-80ae-876cf8e9e8fc
{"id":"c5413351-c292-4fdf-80ae-876cf8e9e8fc","title":"indra's book","author":"indra"}
```

To retrieve all keys with prefix `Books`, use the following command,

```bash
./etcdctl get "Books" --prefix=true
```

Here is the output,

```bash
Books32a4c9fb-dd79-4a15-a139-51317da3f2e3
{"id":"32a4c9fb-dd79-4a15-a139-51317da3f2e3","title":"Indra's Chronicle","author":"Indra Basak"}
Books577c6ba4-303e-4065-90f1-bdf8ad255911
{"id":"577c6ba4-303e-4065-90f1-bdf8ad255911","title":"hello there","author":"Indra Basak"}
Booksa3e546bd-85f1-44bf-9edb-bbe7ee293a0a
{"id":"a3e546bd-85f1-44bf-9edb-bbe7ee293a0a","title":"hello there 2","author":"Indra Basak"}
```

### Build
To build the JAR, execute the following command from the parent directory:

```
mvn clean install
```

### Run
Run the executable jar from the command to start the application,

```bash
java -jar target/spring-etcd-example-1.0.0.jar
```

You should have the following output once the application starts up successfully,

```bash
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.8.RELEASE)

2017-12-28 17:20:32.659  INFO 13438 --- [           main] com.basaki.Application                   : Starting Application on ibasa-mb-46063.local with PID 13438 (/Users/spring-etcd-example/target/spring-etcd-example-1.0.0.jar started by indra.basak in /Users/spring-etcd-example)
2017-12-28 17:20:32.663  INFO 13438 --- [           main] com.basaki.Application                   : No active profile set, falling back to default profiles: default
2017-12-28 17:20:32.733  INFO 13438 --- [           main] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@2f333739: startup date [Thu Dec 28 17:20:32 PST 2017]; root of context hierarchy
...
2017-12-28 17:20:35.998  INFO 13438 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Found 1 custom documentation plugin(s)
2017-12-28 17:20:36.060  INFO 13438 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2017-12-28 17:20:36.394  INFO 13438 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2017-12-28 17:20:36.399  INFO 13438 --- [           main] com.basaki.Application                   : Started Application in 4.388 seconds (JVM running for 4.883)

```

### Usage
The application starts up at port `8080`. You can access the swagger UI at 
`http://localhost:8080/swagger-ui.html`. From the UI, you can create, retrieve,
and delete book entities.

![](./img/swagger-ui.png)

[travis-badge]: https://travis-ci.org/indrabasak/spring-etcd-example.svg?branch=master
[travis-badge-url]: https://travis-ci.org/indrabasak/spring-etcd-example/