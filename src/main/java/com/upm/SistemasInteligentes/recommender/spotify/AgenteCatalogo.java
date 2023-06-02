package com.upm.SistemasInteligentes.recommender.spotify;

import com.upm.SistemasInteligentes.recommender.spotify.messages.SolicitudNombreCanciones;
import com.upm.SistemasInteligentes.recommender.spotify.messages.RespuestaNombreCanciones;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgenteCatalogo extends Agent {
	
	protected void setup() {
		addBehaviour(new EsperarMensajeBehaviour());
		addBehaviour(new EsperarIdsBehaviour());
	}

	private class EsperarMensajeBehaviour extends CyclicBehaviour {

		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage msg = this.myAgent.blockingReceive(mt);
			if (msg != null) {
				String cancion = msg.getContent();
				String resultado = id(cancion);

				ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM);
				respuesta.setContent(resultado);
				respuesta.addReceiver(msg.getSender());
				send(respuesta);
			} else {
				block();
			}
		}
	}

		private class EsperarIdsBehaviour extends CyclicBehaviour {
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
				ACLMessage msg = this.myAgent.blockingReceive(mt);
				if (msg != null) {
					SolicitudNombreCanciones ids = new SolicitudNombreCanciones();
					try {
						ids = (SolicitudNombreCanciones) msg.getContentObject();
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
					
					ArrayList<String> nombres = nombreCanciones(ids.getSongs());
					
					RespuestaNombreCanciones nombre_canciones = new RespuestaNombreCanciones();
					nombre_canciones.setSongs(nombres);

					ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM);
					try {
						respuesta.setContentObject((Serializable) nombre_canciones);
					} catch (IOException e) {
						e.printStackTrace();
					}
					respuesta.addReceiver(msg.getSender());
					send(respuesta);
				} else {
					block();
				}
			}
		}

		private String id(String nombre) {
			String id_cancion = null;
			Conexion conexion = new Conexion();
			Connection cn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String SQL = "SELECT id_cancion FROM canciones WHERE name LIKE ?";
			try {
				cn = conexion.conectar();
				pstmt = cn.prepareStatement(SQL);
				pstmt.setString(1, nombre);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					id_cancion = rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (cn != null) {
						cn.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return id_cancion;
		};

		private ArrayList<String> nombreCanciones(ArrayList<String> uris) {
			Conexion conexion = new Conexion();
			Connection cn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			ArrayList<String> nombres_canciones = new ArrayList<>();

			try {
				cn = conexion.conectar();
				String sql = "SELECT name FROM canciones WHERE id_cancion IN (";
				StringBuilder placeholders = new StringBuilder();

				for (int i = 0; i < uris.size(); i++) {
					placeholders.append("?");
					if (i < uris.size() - 1) {
						placeholders.append(",");
					}
				}

				sql += placeholders.toString() + ")";
				ps = cn.prepareStatement(sql);

				for (int i = 0; i < uris.size(); i++) {
					ps.setString(i + 1, uris.get(i));
				}

				rs = ps.executeQuery();

				while (rs.next()) {
					nombres_canciones.add(rs.getString(1));
				}
			} catch (SQLException e) {
				System.out.println("Error en la consulta");
				e.printStackTrace();
			}
			return nombres_canciones;
		};
}

