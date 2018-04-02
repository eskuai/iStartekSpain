/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tcpsink;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import postgis.Punto;
import postgis.PuntoRepository;
import server.netty.ChannelRepository;
import server.netty.handler.SomethingChannelInitializer;

/**
 * Spring Java Configuration and Bootstrap
 *
 * @author Jibeom Jung
 */

/*
 * @ComponentScan({ "com.my.application.sampleScan1",
 * "com.my.application.sampleScan2"})
 * 
 * @EntityScan(basePackages={"com.my.application.data"})
 * 
 * @EnableAutoConfiguration
 * 
 * @Configuration
 * 
 */

@PropertySource(value = "classpath:nettyserver.properties")
@SpringBootApplication(scanBasePackages = { "postgis", "server.netty", "server.netty.handler" })
@EntityScan(basePackages = "postgis")
@EnableJpaRepositories(basePackages = { "postgis" })
@EnableTransactionManagement

public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	// @Configuration
	// @Profile("production")
	// @PropertySource("classpath:/properties/production/nettyserver.properties")
	// static class Production
	// { }
	//
	// @Configuration
	// @Profile("local")
	// @PropertySource({"classpath:/properties/local/nettyserver.properties"})
	// static class Local
	// { }

	private Point wktToGeometry(String wktPoint) {
		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try {
			geom = fromText.read(wktPoint);
		} catch (ParseException e) {
			throw new RuntimeException("Not a WKT string:" + wktPoint);
		}
		return geom.getInteriorPoint();
	}

	public static void main(String[] args) throws Exception {
		// ConfigurableApplicationContext context =
		// SpringApplication.run(Application.class, args);
		// TCPServer tcpServer = context.getBean(TCPServer.class);
		// tcpServer.start();
		SpringApplication.run(Application.class, args);
	}

	// @Bean
	// public CommandLineRunner demo(CustomerRepository repository) {
	// return (args) -> {

	@Bean
	public CommandLineRunner demo(PuntoRepository repository) throws Exception {

		return (args) -> {

			GeometryFactory factory = new GeometryFactory();

			Punto theEvent = new Punto();
			theEvent.setPId(11);
			theEvent.setPosition(factory.createPoint(new Coordinate(26, 62)));
			theEvent.getPosition().setSRID(4326);
			repository.save(theEvent);

			//
			// GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
			//
			// WKTReader reader = new WKTReader(geometryFactory);
			// //Point point = (Point) reader.read("POINT (1 1)");

	
			// fetch all Puntos
			System.out.println("Puntos found with findAll():");
			System.out.println("-------------------------------");
			repository.findAll().forEach(System.out::println);
			System.out.println();

			// // fetch an individual Punto by ID
			Punto Punto = repository.findOne(11);
			System.out.println("Punto found with findOne(1L):");
			System.out.println("--------------------------------");
			System.out.println(Punto);
			System.out.println();

		};
		// fetch Puntos by last name
		//
		// System.out.println("Puntos found within POLYGON((-107 39, -102 39, -102 41,
		// -107 41, -107 39)):");
		// System.out.println("--------------------------------");
		// repository.findWithin(wktToGeometry("POLYGON((-107 39, -102 39, -102 41, -107
		// 41, -107 39))"))
		// .forEach(System.out::println);
	}

	@Value("${tcp.port}")
	private int tcpPort;

	@Value("${boss.thread.count}")
	private int bossCount;

	@Value("${worker.thread.count}")
	private int workerCount;

	@Value("${so.keepalive}")
	private boolean keepAlive;

	@Value("${so.backlog}")
	private int backlog;

	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup(), workerGroup()).channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(somethingChannelInitializer);
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, tcpChannelOptions.get(option));
		}
		return b;
	}

	@Autowired
	@Qualifier("somethingChannelInitializer")
	private SomethingChannelInitializer somethingChannelInitializer;

	@Bean(name = "tcpChannelOptions")
	public Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}

	@Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(bossCount);
	}

	@Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(workerCount);
	}

	@Bean(name = "tcpSocketAddress")
	public InetSocketAddress tcpPort() {
		return new InetSocketAddress(tcpPort);
	}

	@Bean(name = "channelRepository")
	public ChannelRepository channelRepository() {
		return new ChannelRepository();
	}

}