package com.upm.SistemasInteligentes.recommender.spotify;

import jade.core.Agent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;


public class CatalogoAgente {
	private static final String CONTROLADOR = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/songs";
	private static final String CONTRASENA = "5b7681ab5185f7f9d1283c5cd68309211e2428d34c84b04e5250495903cccc3d";
	private static final String USUARIO = "root";
	
	static {
		try {
			Class.forName(CONTROLADOR);
			System.out.println("Controlador OK");
			
		} catch (ClassNotFoundException e) {
			System.out.println("Error al cargar el controlador");
			e.printStackTrace();
		}
	}
	public Connection conectar() {
		Connection conexion = null;
		
		try {
			conexion = DriverManager.getConnection(URL,USUARIO,CONTRASENA);
			System.out.println("Conexion OK");

		}catch (SQLException e) {
			System.out.println("Error en la conexion");
			e.printStackTrace();

		}
		return conexion;
	}
	public List<String> canciones_autor_id(String id_artista) {
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> id_canciones = new ArrayList<>();
		
	    try {
	        cn = conectar();
	        String sql = "SELECT id_cancion FROM relacion_artista_cancion WHERE id_artista = ?";
	        ps = cn.prepareStatement(sql);
	        ps.setString(1, id_artista);
	        rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            String cancion = rs.getString(1);
	            id_canciones.add(cancion);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error en la consulta");
	        e.printStackTrace();
	    }
	    
	    return id_canciones;
	}
    public List<String> cancionesPorArtista(String nombreArtista) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> canciones = new ArrayList<>();

        cn = conectar();
        try {
            String sql = "SELECT c.name AS nombre_cancion " +
                    "FROM canciones c " +
                    "JOIN relacion_artista_cancion rac ON c.id_cancion = rac.id_cancion " +
                    "JOIN artistas a ON rac.id_artista = a.id_artista " +
                    "WHERE a.name = ?";
            ps = cn.prepareStatement(sql);
            ps.setString(1, nombreArtista);
            rs = ps.executeQuery();

            while (rs.next()) {
                String nombreCancion = rs.getString("nombre_cancion");
                canciones.add(nombreCancion);
            }
        } catch (SQLException e) {
            System.out.println("Error en la consulta");
            e.printStackTrace();
        }
        return canciones;
    }
}
