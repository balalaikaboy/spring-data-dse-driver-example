# Using DataStax Enterprise Driver 1.1.x with Spring Data Cassandra 2.0.x

This repository provides a basic example of using the DataStax Enterprise Driver version 1.1.x with Spring Data
Cassandra 2.0.x.

In particular, this demonstrates how to use a DSE-specific authenticator, which is provided in the DSE driver, with Spring
Data.

This example follows the [basic](https://github.com/spring-projects/spring-data-examples/tree/master/cassandra/example/src/main/java/example/springdata/cassandra/basic)
example in the [spring-data-examples](https://github.com/spring-projects/spring-data-examples) repository.

[The main adjustment](./pom.xml#L52-L67) needed to use the `dse-driver` with `spring-data-cassandra` is to explicitly exclude `cassandra-driver-core` as a
dependency of `spring-data-cassandra`.  `dse-driver` is then defined as a separate dependency.   This should allow the
`dse-driver` to be used as a drop in replacement for `cassandra-driver-core`.

[The second adjustment](./src/main/java/com/datastax/examples/AppConfig.java#L37-L43) in this particular example is to override the `getAuthProvider()` method in the
`AbstractCassandraConfiguration` to use `DsePlainTextAuthProvider`.

## Setting up a Local DSE Node for Testing

To run the tests, you first need to set up a local DSE node that is preconfigured.  The simplest way to accomplish this
is to use [ccm](https://github.com/pcmanus/ccm), but you may use an alternative means if you like.  The following steps demonstrate how to set up a single-node DSE ccm cluster with authentication:

1. Create the ccm cluster (with username and password being that which you use to download DSE normally):

    ```bash
    ccm create -n 1 -v 5.0.7 --dse dse507_1 --dse-username=username --dse-password=password
    ```

2. Configure DSE to use authentication.  In this particular case we use the internal authenticator which has a default
   `cassandra` user (with password `cassandra`), but you may use ldap or kerberos if appropriate:
   
   ```bash
   ccm updateconf authenticator:com.datastax.bdp.cassandra.auth.DseAuthenticator
   ccm updatedseconf authentication_options.enabled:true authentication_options.default_scheme:internal
   ```

3. Start the ccm cluster

   ```bash
   ccm start
   ```

4. Create the 'myks' keyspace

   ```bash
   ccm node1 cqlsh -e "create keyspace if not exists myks with replication={'class' : 'SimpleStrategy', 'replication_factor': 1}" -u cassandra -p cassandra
   ```

## Running the Tests

With a single node CCM cluster set up, we can now run the tests to verify spring data is configured correctly.  To do
this simply run `mvn verify`.  If everything goes well, the tests should pass and the output should look like:


```
Running com.datastax.examples.UserRepositoryTest
14:20:05.248 INFO  o.s.b.t.c.SpringBootTestContextBootstrapper - Neither @ContextConfiguration nor @ContextHierarchy found for test class [com.datastax.examples.UserRepositoryTest], using SpringBootContextLoader
14:20:05.254 INFO  o.s.t.c.s.AbstractContextLoader - Could not detect default resource locations for test class [com.datastax.examples.UserRepositoryTest]: no resource found for suffixes {-context.xml, Context.groovy}.
14:20:05.298 INFO  o.s.b.t.c.SpringBootTestContextBootstrapper - Loaded default TestExecutionListener class names from location [META-INF/spring.factories]: [org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener, org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener, org.springframework.boot.test.autoconfigure.restdocs.RestDocsTestExecutionListener, org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerResetTestExecutionListener, org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrintOnlyOnFailureTestExecutionListener, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverTestExecutionListener, org.springframework.test.context.web.ServletTestExecutionListener, org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener, org.springframework.test.context.support.DependencyInjectionTestExecutionListener, org.springframework.test.context.support.DirtiesContextTestExecutionListener, org.springframework.test.context.transaction.TransactionalTestExecutionListener, org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener]
14:20:05.314 INFO  o.s.b.t.c.SpringBootTestContextBootstrapper - Could not instantiate TestExecutionListener [org.springframework.test.context.web.ServletTestExecutionListener]. Specify custom listener classes or make the default listener classes (and their required dependencies) available. Offending class: [javax/servlet/ServletContext]
14:20:05.314 INFO  o.s.b.t.c.SpringBootTestContextBootstrapper - Using TestExecutionListeners: [org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener@2ed0fbae, org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener@212bf671, org.springframework.test.context.support.DirtiesContextTestExecutionListener@14a2f921, org.springframework.test.context.transaction.TransactionalTestExecutionListener@3c87521, org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener@2aece37d, org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrintOnlyOnFailureTestExecutionListener@548a102f, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverTestExecutionListener@5762806e, org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener@17c386de, org.springframework.boot.test.autoconfigure.web.client.MockRestServiceServerResetTestExecutionListener@5af97850, org.springframework.boot.test.autoconfigure.restdocs.RestDocsTestExecutionListener@5ef60048, org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener@1d548a08]

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.2.RELEASE)

14:20:05.555 INFO  c.d.e.UserRepositoryTest - Starting UserRepositoryTest on hostname with PID 25989 (started by atolbert in /Users/atolbert/Documents/Projects/spring-data-dse-driver-example)
14:20:05.555 INFO  c.d.e.UserRepositoryTest - No active profile set, falling back to default profiles: default
14:20:05.571 INFO  o.s.c.a.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@3f56875e: startup date [Wed Apr 12 14:20:05 CDT 2017]; root of context hierarchy
14:20:05.780 INFO  o.s.d.r.c.RepositoryConfigurationDelegate - Multiple Spring Data modules found, entering strict repository configuration mode!
14:20:05.837 INFO  o.s.b.f.s.DefaultListableBeanFactory - Overriding bean definition for bean 'cassandraTemplate' with a different definition: replacing [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=com.datastax.examples.AppConfig$CassandraConfig; factoryMethodName=cassandraTemplate; initMethodName=null; destroyMethodName=(inferred); defined in com.datastax.examples.AppConfig$CassandraConfig] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration; factoryMethodName=cassandraTemplate; initMethodName=null; destroyMethodName=(inferred); defined in class path resource [org/springframework/boot/autoconfigure/data/cassandra/CassandraDataAutoConfiguration.class]]
14:20:06.218 INFO  c.d.d.c.ClockFactory     - Using native clock to generate timestamps.
14:20:06.425 INFO  c.d.d.c.NettyUtil        - Did not find Netty's native epoll transport in the classpath, defaulting to NIO.
14:20:06.736 WARN  c.d.d.c.Cluster          - You listed localhost/0:0:0:0:0:0:0:1:9042 in your contact points, but it wasn't found in the control host's system.peers at startup
14:20:06.827 INFO  c.d.d.c.p.DCAwareRoundRobinPolicy - Using data-center name 'Cassandra' for DCAwareRoundRobinPolicy (if this is incorrect, please provide the correct datacenter name with DCAwareRoundRobinPolicy constructor)
14:20:06.829 INFO  c.d.d.c.Cluster          - New Cassandra host localhost/127.0.0.1:9042 added
14:20:07.234 INFO  c.d.e.UserRepositoryTest - Started UserRepositoryTest in 1.853 seconds (JVM running for 2.623)
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.572 sec
14:20:08.358 INFO  o.s.c.a.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@3f56875e: startup date [Wed Apr 12 14:20:05 CDT 2017]; root of context hierarchy

Results :

Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```
