package steparrik.accountservice;

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
    //                    .bossEventLoopGroup(new NioEventLoopGroup(10)) // Группа для принятия
    // соединений
    //                    .workerEventLoopGroup(
    //                            new NioEventLoopGroup(60)) // Группа для обработки запросов
    //                    .channelType(NioServerSocketChannel.class) // Тип канала
    //                    .executor(Executors.newFixedThreadPool(100)); // Пул потоков
    //        };
    //    }
}
