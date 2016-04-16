import java.io.IOException;

import com.bbd.saas.api.mongo.AdminUserService;
import com.bbd.saas.mongoModels.AdminUser;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "classpath:dubbo/bbd-saas-consumer.xml" });
        context.start();
        AdminUserService service = (AdminUserService) context.getBean("adminUserService");
        AdminUser a = service.findAdminUserByUserName("哈哈");
        System.out.println(a.getAppkey());
        System.in.read();
    }

}