package task7.service;

import com.redis.testcontainers.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import task7.dto.ProductDto;

@SpringBootTest // интеграционный тест с поднятием контекста
@Testcontainers // обозначаем необходимость инициализации тест-контейнеров
@ActiveProfiles("test")
public abstract class AbstractTestContainerTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Container // декларируем объект учитываемым тест-контейнером
    @ServiceConnection // автоматически назначаем параметры соединения с контейнером
    static final MySQLContainer<?> mysqlContainer =
            new MySQLContainer<>("mysql:8.0.28")
                    .withInitScript("db_mysql.sql");

    @Container // Декларируем объект учитываемым тест-контейнером
    @ServiceConnection // Автоматически назначаем параметры соединения с контейнером
    static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"));
}
