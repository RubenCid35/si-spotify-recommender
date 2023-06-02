package com.upm.SistemasInteligentes.recommender.spotify;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AppTest {
	public static void main(String[] args) {
		CatalogoAgente conexion = new CatalogoAgente();
		
		System.out.println(conexion.canciones_autor_id("1fbkVAnlEb34awQtWEDa6W"));
		System.out.println(conexion.cancionesPorArtista("Ciara"));
	}
}