package es.florida.serverdailysense;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class GestorHTTP implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		// Habilitar accesos CORS (intercambio de recursos de origne cruzado) para
		// peticiones POST, PUT y DELETE
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
		if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) { // Caso PUT y DELETE se pide antes
																			// confirmacion desde cliente
			httpExchange.sendResponseHeaders(204, -1); // Codigo Ok, no devuelve contenido, preparado para POST, PUT o
														// DELETE
			return;
		}

		System.out.print("Peticion recibida: Tipo ");
		String requestParamValue = null;
		if ("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("GET");
			requestParamValue = handleGetRequest(httpExchange);
			try {
				handleGetResponse(httpExchange, requestParamValue);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("POST");
			requestParamValue = handlePostRequest(httpExchange);
			try {
				handlePostResponse(httpExchange, requestParamValue);
			} catch (ClassNotFoundException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("PUT".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			System.out.println("PUT");
			requestParamValue = handlePutRequest(httpExchange);
			try {
				handlePutResponse(httpExchange, requestParamValue);
			} catch (ClassNotFoundException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("DELETE".equals(httpExchange.getRequestMethod())) {
			System.out.println("DELETE");
			requestParamValue = handleDeleteRequest(httpExchange);
			handleDeleteResponse(httpExchange, requestParamValue);
		} else {
			System.out.println("DESCONOCIDA");
		}

	}

	// INICIO BLOQUE REQUEST

	private String handleGetRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo GET: " + httpExchange.getRequestURI().toString());
		return httpExchange.getRequestURI().toString().split("\\?")[1];
	}

	private String handlePostRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo POST: " + httpExchange.getRequestBody().toString());
		InputStream is = httpExchange.getRequestBody();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String handlePutRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo PUT: " + httpExchange.getRequestBody().toString());
		InputStream is = httpExchange.getRequestBody();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String handleDeleteRequest(HttpExchange httpExchange) {
		System.out.println("Recibida URI tipo DELETE: " + httpExchange.getRequestBody().toString());
		InputStream is = httpExchange.getRequestBody();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// FIN BLOQUE REQUEST

	// INICIO BLOQUE RESPONSE

	private void handleGetResponse(HttpExchange httpExchange, String requestParamValue) throws IOException, ClassNotFoundException, SQLException {

		System.out.println("El servidor pasa a procesar la peticion GET: " + requestParamValue);

		// Ejemplo de respuesta: el servidor devuelve al cliente un HTML simple:
		OutputStream outputStream = httpExchange.getResponseBody();
		String htmlResponse = getOperacion(requestParamValue);
		httpExchange.sendResponseHeaders(200, htmlResponse.length());
		outputStream.write(htmlResponse.getBytes());
		outputStream.flush();
		outputStream.close();
		System.out.println("Devuelve respuesta HTML: " + htmlResponse);


		// TODO: en vez del string htmlResponse anterior con la pagina web simple, se
		// podria incluir
		// en dicho string cualquier otro string (tipo JSON, por ejemplo) que el cliente
		// haya solicitado
		// a traves de la peticion Axios de Javascript. Por tanto, podria ser necesario
		// llamar desde este
		// este metodo a lo/s metodo/s necesario/s para acceder a una base de datos como
		// las que hemos
		// trabajado en el modulo de Acceso a Datos.

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status

	}

	private void handlePostResponse(HttpExchange httpExchange, String requestParamValue) throws IOException, ClassNotFoundException, SQLException {

		System.out.println("El servidor pasa a procesar el body de la peticion POST: " + requestParamValue);
		
		;
		//register(requestParamValue);

		// Opcion 1: si queremos que el servidor devuelva al cliente un HTML:
		 OutputStream outputStream = httpExchange.getResponseBody();
		 String htmlResponse = postOperacion(requestParamValue);
		 System.out.println("Devuelve respuesta HTML: " + htmlResponse);
		 httpExchange.sendResponseHeaders(200, htmlResponse.length());
		 System.out.println("Devuelve respuesta HTML: " + htmlResponse);
		 outputStream.write(htmlResponse.getBytes());
		 outputStream.flush();
		 outputStream.close();
		

		// Opcion 2: el servidor devuelve al cliente un codigo de ok pero sin contenido
		// HTML
		//httpExchange.sendResponseHeaders(204, -1);
		//System.out.println("El servidor devuelve codigo 204");

		// TODO: a partir de aqui todas las operaciones que se quieran programar en el
		// servidor cuando recibe
		// una peticion POST (ejemplo: insertar en una base de datos lo que nos envia el
		// cliente en requestParamValue)

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status

	}

	private void handlePutResponse(HttpExchange httpExchange, String requestParamValue) throws IOException, ClassNotFoundException, SQLException {

		System.out.println("El servidor pasa a procesar el body de la peticion PUT: " + requestParamValue);
		actualizarCuenta(requestParamValue);
		// Opcion 1: si queremos que el servidor devuelva al cliente un HTML:
		// OutputStream outputStream = httpExchange.getResponseBody();
		// String htmlResponse = "Parametro/s PUT: " + requestParamValue + " -> Se
		// procesara por parte del servidor";
		// outputStream.write(htmlResponse.getBytes());
		// outputStream.flush();
		// outputStream.close();
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);
		// httpExchange.sendResponseHeaders(200, htmlResponse.length());
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);

		// Opcion 2: el servidor devuelve al cliente un codigo de ok pero sin contenido
		// HTML
		httpExchange.sendResponseHeaders(204, -1);
		System.out.println("El servidor devuelve codigo 204");

		// TODO: a partir de aqui todas las operaciones que se quieran programar en el
		// servidor cuando recibe
		// una peticion PUT (ejemplo: actualizar en una base de datos lo que nos envia
		// el cliente en requestParamValue)

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status

	}

	private void handleDeleteResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {

		System.out.println("El servidor pasa a procesar el body de la peticion DELETE: " + requestParamValue);

		// Opcion 1: si queremos que el servidor devuelva al cliente un HTML:
		// OutputStream outputStream = httpExchange.getResponseBody();
		// String htmlResponse = "Parametro/s DELETE: " + requestParamValue + " -> Se
		// procesara por parte del servidor";
		// outputStream.write(htmlResponse.getBytes());
		// outputStream.flush();
		// outputStream.close();
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);
		// httpExchange.sendResponseHeaders(200, htmlResponse.length());
		// System.out.println("Devuelve respuesta HTML: " + htmlResponse);

		// Opcion 2: el servidor devuelve al cliente un codigo de ok pero sin contenido
		// HTML
		httpExchange.sendResponseHeaders(204, -1);
		System.out.println("El servidor devuelve codigo 204");

		// TODO: a partir de aqui todas las operaciones que se quieran programar en el
		// servidor cuando recibe
		// una peticion DELETE (ejemplo: borrar de una base de datos lo que nos indica
		// el cliente en requestParamValue)

		// NOTA: se puede incluir tambien un punto de control antes de enviar el codigo
		// resultado de la
		// operacion en el header (httpExchange.sendResponseHeaders(CODIGOHTTP, {})).
		// Por ejemplo, si
		// hay un error se enviarian codigos del tipo 400, 401, 403, 404, etc.
		// https://developer.mozilla.org/es/docs/Web/HTTP/Status
	}
	// FIN BLOQUE RESPONSE

	// INICIO METODOS GET
	//INICIO CASOS DE GET
	public static String getOperacion(String param) throws ClassNotFoundException, SQLException {

		//recibes como parametro el string de detras de ? en la url (requestParamValue)
		String responseGet = "";
		switch (param) {

		case "dependents":
			responseGet = getInfoDependent();
			break;
		case "attributes":
			responseGet = getAttribute();
			break;
		case "assistants":
			responseGet = getAssistant();
			break;
		}
		return responseGet;
	}
	//FIN CASOS DE GET
	// GET INFO DEPENDENT SLIDER
		public static String getInfoDependent() throws ClassNotFoundException, SQLException {

			String responseDependent = "";
			int object = 1; //indice del objeto que lee el select
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Dependents");
			while (rs.next()) {
				if (object == 1) { //si es el primer objeto iniciará con [
					responseDependent += "[{ \"IdDependents\":" + rs.getInt(1) + ", \"Name\":" + rs.getString(2)
							+ ", \"LastName\":" + rs.getString(3) + ", \"Adress\":" + rs.getString(4) + ", \"Gender\":"
							+ rs.getString(5) + ", \"Age\":" + rs.getInt(6) + ", \"FamilyContact\":" + rs.getInt(7)
							+ ", \"Diseases\":" + rs.getString(8) + ", \"Allergies\":" + rs.getString(9)
							+ ", \"Assistant\":" + rs.getInt(10) + ", \"DependencyLevel\":" + rs.getString(11) + "}";

				} else { //si ya ha introducido el primero, no deberá crear el array con [, debera seguir añadiendo objetos
					responseDependent += "{ \"IdDependents\":" + rs.getInt(1) + ", \"Name\":" + rs.getString(2)
							+ ", \"LastName\":" + rs.getString(3) + ", \"Adress\":" + rs.getString(4) + ", \"Gender\":"
							+ rs.getString(5) + ", \"Age\":" + rs.getInt(6) + ", \"FamilyContact\":" + rs.getInt(7)
							+ ", \"Diseases\":" + rs.getString(8) + ", \"Allergies\":" + rs.getString(9)
							+ ", \"Assistant\":" + rs.getInt(10) + ", \"DependencyLevel\":" + rs.getString(11) + "}";
				}
				object++;
			}

			responseDependent += "]"; //finalmente cuando no tiene mas objetos que leer, cierra el array
			rs.close();
			stmt.close();
			con.close();
			return responseDependent;
		}
	
		//INICIO GET DE ATRIBUTOS
		// TIPOS DE ATRIBUTO  PASTILLAS = 3 -- TAREAS= 2 -- RECORDATORIOS = 1
		public static String getAttribute() throws ClassNotFoundException, SQLException {
			String responseAttribute = "";
			int object = 1;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM attributes ");
			while (rs.next()) {
				if (object == 1) {
					responseAttribute += "[{ \"IdAttribute\":" + rs.getInt(1) + ", \"Type\":" + rs.getInt(2)
							+ ", \"Name\":" + rs.getString(3) + ", \"Description\":" + rs.getString(4) + ", \"Dependents\":"
							+ rs.getInt(5) + ", \"Date\":" + rs.getDate(6) + "}";

				} else {
					responseAttribute += "{ \"IdAttribute\":" + rs.getInt(1) + ", \"Type\":" + rs.getInt(2)
							+ ", \"Name\":" + rs.getString(3) + ", \"Description\":" + rs.getString(4) + ", \"Dependents\":"
							+ rs.getInt(5) + ", \"Date\":" + rs.getDate(6) + "}";
				}
				object++;
			}

			responseAttribute += "]";
			rs.close();
			stmt.close();
			con.close();
			return responseAttribute;
		}
		//FIN GET DE ATRIBUTOS
		
		//INICIO GET DATOS ASISTENTE
		public static String getAssistant() throws ClassNotFoundException, SQLException {
			String responseAssistant = "";
			int object = 1;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM assistant ");
			while (rs.next()) {
				if (object == 1) {
					responseAssistant += "[{ \"IdAssistant\":" + rs.getInt(1) + ", \"User\":" + rs.getString(2)
							+ ", \"Password\":" + rs.getString(3) + ", \"Gender\":" + rs.getString(4) + ", \"Email\":"
							+ rs.getString(5)+ "}";

				} else {
					responseAssistant += "{ \"IdAssistant\":" + rs.getInt(1) + ", \"User\":" + rs.getString(2)
					+ ", \"Password\":" + rs.getString(3) + ", \"Gender\":" + rs.getString(4) + ", \"Email\":"
					+ rs.getString(5)+ "}";
				}
				object++;
			}

			responseAssistant += "]";
			rs.close();
			stmt.close();
			con.close();
			return responseAssistant;
		}
	//FIN GET DE ASISTENTE
	// FIN METODOS GET
	
	
	// INICIO METODOS POST
	//INICIO CASOS DE POST
		public static String postOperacion(String param) throws ClassNotFoundException, SQLException {

			//recibes como parametro la primera posicion tipo de requestParamValue
			
			System.out.println("b");


			JSONObject obj = new JSONObject(param);
			String op =  obj.getString("op");
			System.out.println(op);

			String responseGet = "";
			switch (op) {

			case "login":
				responseGet = login(param);
				break;
			case "login2":
				responseGet = login2(param);
				break;
			case "register":
				responseGet = register(param);
				break;
			case "newAttribute":
				responseGet = newAttribute(param);
				break;
			}
			System.out.println(responseGet);
			return responseGet;
		}
		//FIN CASOS DE POST
		
		//POST LOGIN DEVUELVE ID ASSISTANT + LOGIN TRUE
		public static String login(String request) throws ClassNotFoundException, SQLException {
			System.out.println(request);
			
			int idAssistant=0;
			String arrayAssistant = "";


			JSONObject obj = new JSONObject(request);
			String user =  obj.getString("user");    // cogemos valor del string con formato json para hacer validacion en la bbdd de login
			String pwd =  obj.getString("pass");
			System.out.println(user+" "+pwd);
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT IdAssistant,User,Gender FROM assistant WHERE User = '" +user+ "' and Password='"+pwd+"'");
			
			while (rs.next()) {
				idAssistant=rs.getInt(1);		//recoger la id del asistente que coincida
				System.out.println(idAssistant);	
				if (idAssistant!=0) {
					arrayAssistant = "[{ \"correct\": \"true\", \"IdAssistant\":" + rs.getInt(1)+ ", \"User\":" + rs.getString(2)
											+ ", \"Gender\":" + rs.getString(3)+ "}]";
				} else {
					arrayAssistant = "[{ \"correct\": \"false\"}]";
				}
			}
			
			System.out.println(arrayAssistant);

			return arrayAssistant;
		}
		
		
		//INICIO METODO LOGIN
		public static String login2(String request) throws ClassNotFoundException, SQLException {
			System.out.println(request); //mensaje de comprobacion de que se recoge el id
			
			int object = 1;
			String arrayDependents = "";	
			
			JSONObject obj = new JSONObject(request);
			String idAssistant =  obj.getString("id");
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT IdDependents, Name, LastName, Age, Diseases, Allergies FROM dependents WHERE Assistant = '" +idAssistant+"'");
			while (rs.next()) {
				if (object == 1) {
					arrayDependents += "[{ \"IdDependents\":" + rs.getInt(1)+ ", \"Name\":" + rs.getString(2)+ ", \"LastName\":" + rs.getString(3)
					+ ", \"Age\":" + rs.getInt(4)+ ", \"Diseases\":" + rs.getString(5) + ", \"Allergies\":" + rs.getString(6)+ "}";

				} else {
					arrayDependents += "{ \"IdDependents\":" + rs.getInt(1)+ ", \"Name\":" + rs.getString(2)+ ", \"LastName\":" + rs.getString(3)
					+ ", \"Age\":" + rs.getInt(4)+ ", \"Diseases\":" + rs.getString(5) + ", \"Allergies\":" + rs.getString(6)+ "}";
				}
				object++;
			}
			arrayDependents += "]";
			System.out.println(arrayDependents);
			return arrayDependents;
					
			}
		//FIN METODO LOGIN

		//INICIO METODO CREAR CUENTA
			public static String register(String request) throws ClassNotFoundException, SQLException {
				

				System.out.println(request);
				

				
				JSONObject obj = new JSONObject(request);
				String user =  obj.getString("user");  
				String pwd =  obj.getString("pass");
				String email =  obj.getString("email");
				String gender =  obj.getString("gender");
				
				System.out.println(user+" "+pwd+" "+email+" "+gender);
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
				PreparedStatement psInsertar = con.prepareStatement("INSERT INTO assistant (User,Password,Gender,Email )VALUES (?,?,?,?)");
				psInsertar.setString(1,user);
				psInsertar.setString(2,pwd);
				psInsertar.setString(3,gender);
				psInsertar.setString(4,email);
				psInsertar.executeUpdate();		
				return "{ \"correct\" : \"OK\" }";
				}
			//FIN METODO CREAR CUENTA
			
			//INICIO METODO CREAR ATRIBUTO
					public static String newAttribute(String request) throws ClassNotFoundException, SQLException {
						System.out.println("a");
						System.out.println(request);
						
						System.out.println("a");
						
						JSONObject obj = new JSONObject(request);
						String type =  obj.getString("type");  
						String name =  obj.getString("name");
						String description =  obj.getString("description");
						String dependent =  obj.getString("dependents");
						String date =  obj.getString("date");
						
						System.out.println(type+" "+name+" "+description+" "+dependent+" "+date);
						
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
						PreparedStatement psInsertar = con.prepareStatement("INSERT INTO attributes (Type,Name,Description,Dependents,Date )VALUES (?,?,?,?,?)");
						psInsertar.setString(1,type);
						//psInsertar.setInt(1,Integer.parseInt(type));
						psInsertar.setString(2,name);
						psInsertar.setString(3,description);
						psInsertar.setString(4,dependent);
						psInsertar.setString(5,date);
						psInsertar.executeUpdate();		
						return "{ \"correct\" : \"OK\" }";
						}
					
					//FIN METODO CREAR ATRIBUTO
		
		// FIN METODOS POST

	

	


				//INICIO METODOS PUT
				
				//INICIO ACTUALIZAR CUENTA
				public int actualizarCuenta(String request) throws ClassNotFoundException, SQLException {
					System.out.println(request);
					
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/dailysense", "root", "");
					
					JSONObject obj = new JSONObject(request);
					String id =  obj.getString("id");  
					String user =  obj.getString("user"); 
					String pwd =  obj.getString("pwd");
					
					//System.out.println(user+" "+pwd+" "+email+" "+gender);
					System.out.println("UPDATE assistant SET User = " +user + ", Password = " + pwd + " WHERE IdAssistant = "+id);
					PreparedStatement psActualizar = con.prepareStatement("UPDATE assistant SET User = '" +user + "', Password = '" + pwd + "' WHERE IdAssistant = "+id);
					int resultadoActualizar = psActualizar.executeUpdate();
						
					return resultadoActualizar;	
					}
				//FIN ACTUALIZAR CUENTA
				//FIN METODOS PUT
	}

