package steparrik.accountservicegrpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountServiceGrpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceGrpcApplication.class, args);
    }

//    @Bean
//    GrpcServerConfigurer grpcServerConfigurer() {
//        return builder -> {
//            ((NettyServerBuilder) builder)
//                    .workerEventLoopGroup(new EpollEventLoopGroup(4))
//                    .bossEventLoopGroup(new EpollEventLoopGroup(1))
//                    .channelType(EpollServerSocketChannel.class)
//                    .executor(Executors.newFixedThreadPool(20));
//        };
//    }

}
