import com.github.pig.admin.PigAdminApplication;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PigAdminApplication.class)
public class PigAdminApplicationTest {
	@Autowired
	private StringEncryptor stringEncryptor;

	@Test
	public void testEnvironmentProperties() {
		System.out.println(stringEncryptor.encrypt("redis"));
		System.out.println(stringEncryptor.encrypt("pig"));
		System.out.println(stringEncryptor.encrypt("lengleng"));
		System.out.println(stringEncryptor.encrypt("root"));
		System.out.println(stringEncryptor.encrypt("g0HJr2Ltrs0k6tJDY6pDI2aVMUCPSWZDTROLcFMs"));
		System.out.println(stringEncryptor.encrypt("24760324"));
		System.out.println(stringEncryptor.encrypt("175d516debb916d3842d981dd3b76061"));
	}

}
