import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Ejercicio1 {

	private FTPClient client;
	private Logger logger;

	public void logging(String logFilePath) throws IOException {
		File directory = new File(logFilePath);
		if (!directory.exists()) {
			System.out.println("NO EXISTE EL DIRECTORIO EN EL DISCO DURO");
			System.exit(1);
		}
		String LOG_FILE_NAME = "MyLogFile.log";
		String logPath = logFilePath + File.separator + LOG_FILE_NAME;
		File file = new File(logPath);
		if (!file.exists()) {
			if (!file.createNewFile()) {
				System.err.println("Ha ocurrido un error creando el archivo " + LOG_FILE_NAME);
				System.exit(1);
			}
		}
		this.logger = Logger.getLogger("MyLog");
		FileHandler fileHandler;
		try {
			// Configuro el logger y establezco el formato
			fileHandler = new FileHandler(logPath, true);
			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			System.out.println("Ha ocurrido un error al crear el log");
		}

		try {
			BufferedReader entradaDatos = new BufferedReader(new InputStreamReader(System.in));

			String username;
			Console console = System.console();
			System.out.println("TAREA 6 - EJERCICIO 1 - CONTROL DE LOG.");
			System.out.println("=========================================");
			do {
//				if (console == null) {
//					throw new NullPointerException("Fallo al escribir por consola");
//				}
//				username = console.readLine("Introduce Nombre de usuario: ");
				System.out.print("Introduce Nombre de usuario: ");
				username = entradaDatos.readLine();
				if (username.isEmpty()) {
					throw new IllegalArgumentException("El campo no puede estar vacío");
				}
				if (!username.trim().equals("*")) {
//					char[] passwordArray = console.readPassword("Contraseña: ");
//					if (passwordArray == null) {
//						throw new IllegalArgumentException("El campo no puede ser nulo");
//					}
					System.out.print("Contraseña: ");
//					String password = new String(passwordArray);
					String password = entradaDatos.readLine();
					logger.log(Level.INFO, "# Conectando con el servidor...");
					writeLog(username, password);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!username.equals("*"));

		} catch (
				Exception e) {
			System.err.println(e.getLocalizedMessage());
		} finally {
			this.client.disconnect();
		}

	}

	private void writeLog(String user, String password) throws IOException {
		logger.log(Level.INFO, String.format("#Haciendo login con el usuario: %s", user));
		if (!login(user, password)) {
			logger.log(Level.INFO, String.format("#No se ha podido conectar: %s", user));
			return;
		}
		logger.log(Level.INFO, "#Login correcto");
//		System.out.println("\t\tNos conectamos a: localhost");
		//Nos quedamos con el número
		logger.log(Level.INFO, String.format("#Hora de conexión: %s", LocalDateTime.now()));
		int userId;
		try {
			userId = Integer.parseInt(user.replaceAll("\\D+", ""));
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error al obtener el identificador del usuario...");
			return;
		}
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
		inputStream.close();
		if (!done) {
			System.out.println("\tHa ocurrido un error al actualizar el LOG.txt");
		}
		logger.log(Level.INFO, String.format("#Desconectando usuario: %s", user));
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
		if (args.length == 0) {
			System.out.println("ARGUMENTOS INCORRECTOS, intentalo de nuevo");
			System.out.println("Formato de ejecución:");
			System.out.println("\t\t\tjava -jar Capitulo6Ejercicio1.jar ubicacioncarpeta");
			System.exit(1);
		}
		ejercicio1.logging(args[0]);
	}
}
