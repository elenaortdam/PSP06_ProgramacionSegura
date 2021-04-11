import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio1 {

	private FTPClient client;
	private final List<String> usersLog = new ArrayList<>();

	public void logging() throws IOException {
		try {
//			BufferedReader entradaDatos = new BufferedReader(new InputStreamReader(System.in));

			String username;
			Console console = System.console();
			do {
				if (console == null) {
					throw new NullPointerException("Fallo al escribir por consola");
				}
				username = console.readLine("Introduce Nombre de usuario: ");
//				System.out.print("Nombre de usuario: ");
//				username = entradaDatos.readLine();
				if (username.isEmpty()) {
					throw new IllegalArgumentException("El campo no puede estar vacío");
				}
				if (!username.trim().equals("*")) {
					char[] passwordArray = console.readPassword("Contraseña: ");
					if (passwordArray == null) {
						throw new IllegalArgumentException("El campo no puede ser nulo");
					}
//					System.out.print("Contraseña: ");
					String password = new String(passwordArray);
//					String password = entradaDatos.readLine();
					writeLog(username, password);
				}

			} while (!username.equals("*"));

			System.out.println("Enviado!");
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		} finally {
			this.client.disconnect();
		}
	}

	private void writeLog(String user, String password) throws IOException {
		if (!login(user, password)) {
			System.out.println("\t\tLogin incorrecto...");
			return;
		}
		System.out.println("\t\tNos conectamos a: localhost");
		//Nos quedamos con el número
		int userId;
		try {
			userId = Integer.parseInt(user.replaceAll("\\D+", ""));
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error al obtener el identificador del usuario...");
			return;
		}
		usersLog.add(user);
		String fileName = "LOG.txt";
		String systemDrive = System.getProperty("user.dir");
		String directoryName = systemDrive + File.separator + user;
		String pathname = directoryName + File.separator + fileName;
		File directory = new File(directoryName);
		if (!directory.exists()) {
			if (!directory.mkdir()) {
				System.err.println("Ha ocurrido un error creando el directorio del usuario");
			}
		}
		File file = new File(pathname);
		if (!file.exists()) {
			if (!file.createNewFile()) {
				System.err.println("Ha ocurrido un error creando el archivo LOG.txt");
			}
		}
		FileOutputStream out = new FileOutputStream(file);
		client.changeWorkingDirectory("LOG");
		FTPFile[] ftpFiles = client.listFiles();
		if (ftpFiles.length <= 0) {
			out.write(String.format("Conexiones realizadas por el Usuario %d.\n", userId).getBytes());
			out.flush();
		} else {
			client.retrieveFile("LOG.txt", out);
		}
		String date = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz uuuu")) + "\n";
		out.write(date.getBytes());
		out.flush();
		String remoteFile = String.format("%s/%s", client.printWorkingDirectory(), fileName);
		InputStream inputStream = new FileInputStream(file);
		client.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
		client.enterLocalPassiveMode();
		out.close();
		boolean done = client.storeFile(remoteFile, inputStream);
		System.out.print(client.getReplyString());
		inputStream.close();
		if (!done) {
			System.out.println("\tHa ocurrido un error al actualizar el LOG.txt");
		}

	}

	private boolean login(String user, String password) {
		this.client = new FTPClient();

		try {
			client.connect("localhost");
			return client.login(user, password);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static void main(String[] args) throws IOException {
		Ejercicio1 ejercicio1 = new Ejercicio1();
		ejercicio1.logging();
	}
}