package pe.isil.moduloseguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;


@SpringBootApplication
public class ModuloseguridadApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ModuloseguridadApplication.class, args);

		//Cargar Driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		//Crear conexion
		Connection conexion = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/moduloseg2", "root", "budakar01");


		loginByUsernameAndPass(conexion, "DNI73267572", "secret");

		//Cerrar conexion
		conexion.close();

	}


	public static void testStatement(Connection connection) throws Exception {
		//Crear statement
		Statement statement = connection.createStatement();

		//Ejecutar sentencia
		int affectedRows = statement.executeUpdate("UPDATE USERS SET name='Jose' WHERE id=3");
		System.out.println("Filas afectadas: " + affectedRows);

		ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");
		while (resultSet.next()) {
			System.out.println(resultSet.getString("name"));
		}
		System.out.println("Filas afectadas: " + affectedRows);
	}

	public static void testPreparedStatement(Connection connection) throws Exception{

		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE USERS SET name=? WHERE id=?");
		preparedStatement.setString(1,"Eduardo");
		preparedStatement.setInt(2,3);

		int affectedRows = preparedStatement.executeUpdate();

		System.out.println("Filas afectadas: " + affectedRows);
	}

	public static void testPreparedStatementResult(Connection connection) throws Exception{

		PreparedStatement pt = connection.prepareStatement("SELECT * FROM USERS WHERE username=?");
		pt.setString(1,"DNI73268723");

		ResultSet rs = pt.executeQuery();

		while(rs.next()){
			System.out.println(rs.getString("name") + " " + rs.getString("lastname"));
		}

	}

	public static void testCallableStatement(Connection connection) throws Exception{

		CallableStatement cs = connection.prepareCall("{call getAllUsers()}");
		ResultSet rs = cs.executeQuery();

		while(rs.next()){
			System.out.println(rs.getString("name") + " " +
								rs.getString("lastname") + " " +
								rs.getString("username") + " " +
								rs.getString("pass") + " " +
								rs.getString("enable")
			);
		}
	}

	public static void loginByUsernameAndPass(Connection connection,String username,String pass) throws Exception{

		CallableStatement cs = connection.prepareCall("{call login(?,?)}");
		cs.setString(1,username);
		cs.setString(2,pass);

		ResultSet rs = cs.executeQuery();

		while(rs.next()){
			System.out.println(rs.getString(1));
		}
	}

	public static void updatePassByUsername(Connection connection,String username,String pass) throws Exception{

		CallableStatement cs = connection.prepareCall("{call updatePassByUsername(?,?,?)}");
		cs.setString(1,username);
		cs.setString(2,pass);
		cs.registerOutParameter(3, Types.INTEGER);

		cs.execute();

		int affectedRows = cs.getInt(3);

		System.out.println("Filas afectadas :" +affectedRows);
	}
}
