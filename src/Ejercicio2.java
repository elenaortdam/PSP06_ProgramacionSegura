import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ejercicio2 {

	private void validateInput() throws IOException {
		System.out.println("TAREA 6 - EJERCICIO 2 - CONTROL DE ENTRADA.");
		System.out.println("============================================");
		BufferedReader entradaDatos = new BufferedReader(new InputStreamReader(System.in));
		String username = "";
		do {
			System.out.print("Introduce el nombre de usuario: ");
			username = entradaDatos.readLine();
			if (validateUsername(username)) {
				System.out.println("\t\t\tUsuario correcto");
			} else {
				if (!username.equals("*")) {
					System.out.println("\t\t\tUsuario incorrecto");
				}
			}

		} while (!username.equals("*"));
		System.out.println("============================================");
		String productCode = "";
		do {
			System.out.print("Introduce el código de Producto: ");
			productCode = entradaDatos.readLine();
			if (validateProductCode(productCode)) {
				System.out.println("\t\t\tProducto correcto");
			} else {
				if (!productCode.equals("*")) {
					System.out.println("\t\t\tProducto incorrecto");
				}
			}
		} while (!productCode.equals("*"));
	}

	public boolean validateUsername(String username) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		if (username.length() > 8) {
			return false;
		}
		Pattern pattern = Pattern.compile("[a-z][a-z0-9]{3,7}");
		Matcher matcher = pattern.matcher(username);
		return matcher.find();
	}

	public boolean validateProductCode(String productCode) {
		if (productCode == null || productCode.isEmpty()) {
			return false;
		}
		if (productCode.length() != 17) {
			return false;
		}

		Pattern pattern = Pattern.compile("[0-1]{3}-(MT|TO|NO|CO|IR|SE|NE)-[0-9]{5}-[SHP][0-9]{3}");
		Matcher matcher = pattern.matcher(productCode);
		return matcher.find();
	}

	/**
	 * Apartado 1. El programa pide por teclado el nombre del usuario que tiene que cumplir los
	 * siguientes criterios:
	 * ➢ tiene que tener una longitud de 4 a 8 letras (minúsculas) y números
	 * ➢ debe empezar por una letra (minúscula) y
	 * ➢ a continuación le siguen letras (minúsculas) y números.
	 * La entrada del nombre de usuario finalizará cuando este sea *.
	 */

	/**
	 * Apartado 2. A continuación pedirá por teclado el código de un producto que debe tener el siguiente
	 * formato:
	 * ZZZ-CC-CODIG-ADVE
	 * Donde:
	 * ✓ ZZZ: Código de zona: los 3 primeros caracteres en formato binario indica la zona
	 * en la que
	 * se suministra el producto. Por ejemplo 000, 001, 010, etc.
	 * ✓ CC: Categoría del producto: los 2 caracteres siguientes deben ser 2 letras en mayúsculas.
	 * Las categorías son las siguientes: MT, TO, NO, CO, IR, SE y NE.
	 * ✓ CODIG: Código producto: 5 dígitos numéricos.
	 * ✓ ADVE: Advertencia: 4 caracteres siendo el primero una letra en mayúscula
	 * (S, H o P) y los
	 * siguientes 3 dígitos. Por ejemplo: S000, H290, H314, P234, P260, etc.
	 */

	public static void main(String[] args) throws IOException {
		Ejercicio2 ejercicio2 = new Ejercicio2();
		ejercicio2.validateInput();
	}
}
