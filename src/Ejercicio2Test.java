import org.junit.Assert;
import org.junit.Test;

public class Ejercicio2Test {

	private final Ejercicio2 ejercicio2 = new Ejercicio2();

	@Test
	public void test1() {
		Assert.assertFalse(ejercicio2.validateProductCode(""));
	}

	@Test
	public void test2() {
		Assert.assertTrue(ejercicio2.validateProductCode("001-NO-12345-S123"));
	}

	@Test
	public void test3() {
		Assert.assertTrue(ejercicio2.validateProductCode("001-MT-12345-S123"));
	}

	@Test
	public void test4() {
		Assert.assertFalse(ejercicio2.validateProductCode("001-ON-12345-S123"));
	}

	@Test
	public void test5() {
		Assert.assertFalse(ejercicio2.validateProductCode("021-ON-12345-S123"));
	}

	@Test
	public void test6() {
		Assert.assertFalse(ejercicio2.validateProductCode("021-ON-12345-Z123"));
	}
}
