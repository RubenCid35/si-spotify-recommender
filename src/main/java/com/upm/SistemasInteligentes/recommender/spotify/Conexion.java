package com.upm.SistemasInteligentes.recommender.spotify;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	private static final String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3326/songs";
	private static final String CONTRASENA = "password";
	private static final String USUARIO = "user";

	
	public Connection conectar() {
		Connection conexion = null;
		
		try {
			Class.forName(CONTROLADOR);
			conexion = DriverManager.getConnection(URL,USUARIO,CONTRASENA);

		}catch (ClassNotFoundException  | SQLException e) {

			System.out.println("Error en la conexion");
			e.printStackTrace();

		}
		return conexion;
	}
}
