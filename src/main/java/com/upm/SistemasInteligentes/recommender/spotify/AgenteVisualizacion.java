package com.upm.SistemasInteligentes.recommender.spotify;

import com.upm.SistemasInteligentes.recommender.spotify.messages.SolicitudRecomendador;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import com.upm.SistemasInteligentes.recommender.spotify.messages.*;

import com.upm.SistemasInteligentes.recommender.spotify.Utils;

public class AgenteVisualizacion extends AgentBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<String> uris_canciones = new ArrayList<String>();
    protected void setup() {
		super.setup();
		this.type = AgentModel.AGENTEVISUALIZACION;
    	
        SequentialBehaviour seq = new SequentialBehaviour(this);
        String cancion1 = JOptionPane.showInputDialog(null, "Introduzca cancion 1", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion1));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour(this, 1));
    	
        String cancion2 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion2));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour(this, 2));
        
        String cancion3 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion3));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour(this, 3));
        
        String cancion4 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion4));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour(this, 4));
        
        String cancion5 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion5));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour(this, 5));
        
        seq.addSubBehaviour(new EnviarListaRecomendador(this ));
        
        addBehaviour(seq);
        registerAgentDF();
    }

    private class EnviarCancionBehaviour extends SimpleBehaviour {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String cancion;
    	private boolean done = false;
    	
		public EnviarCancionBehaviour(String cancion) {
    		this.cancion = cancion;
    	}
        public void action() {
        	System.out.println(EnviarCancionBehaviour.class.getName());
            System.out.println(this.cancion);
        	Utils.enviarMensaje(this.myAgent, "AgentCatalogo", this.cancion, ACLMessage.REQUEST);
            System.out.println("Enviado");
        	
        	// ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        	// msg.setContent(this.cancion); 
        	// msg.addReceiver(getAID("AgenteCatalogo"))
        	// send(msg);
            done = true;
        }
        public boolean done() {
        	return done;
        }
    }

    private class EsperarRespuestaBehaviour extends SimpleBehaviour {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean done = false;
		private AgenteVisualizacion agent;
		private int idx;
		public EsperarRespuestaBehaviour(AgenteVisualizacion ag, int i) {
    		this.agent = ag;
    		this.idx = i;
    	}
		public void action() {
        	System.out.println(EsperarRespuestaBehaviour.class.getName());
        	MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = this.myAgent.blockingReceive(mt);
            if (msg != null) {
            	
            	String cancion = msg.getContent();
            	this.agent.uris_canciones.add(cancion);

            	System.out.println(this.agent.uris_canciones);
            	System.out.println("Fin Procesamiento Canción");
            	
            }
        
            done = true;
		}

		@Override
		public boolean done() {
			return done;
		} 
    }
    
    @SuppressWarnings("unused")
	private class EnviarListaRecomendador extends SimpleBehaviour {
    	
    	
    	private boolean done = false;


		private AgenteVisualizacion agent;
		private static final long serialVersionUID = 1L;

		EnviarListaRecomendador (AgenteVisualizacion ag ) {
			this.agent = ag;
		}
		
		public void action() {

        	System.out.println(EnviarListaRecomendador.class.getName());

            System.out.println("Enviando A recomendador");            
        	SolicitudRecomendador contenido = new SolicitudRecomendador();
        	contenido.setSeeds(this.agent.uris_canciones);
            // Crear el mensaje
        	Utils.enviarMensaje(this.myAgent, "Recomendador", contenido, ACLMessage.REQUEST);
        	System.out.println("AgenteVisualizacion envió la lista de canciones al AgenteRecomendador");

            // Esperar la respuesta del AgenteRecomendador
            
        	MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = this.myAgent.blockingReceive(mt);
            
            if (msg != null) {
            	InformeRecomendador body = new InformeRecomendador();
            	try {
					body = (InformeRecomendador) msg.getContentObject();
				} catch (UnreadableException e) {
					e.printStackTrace();
					done = true;
					return;
				}
            	ArrayList<String> songs = body.getSongs();
            	
            	System.out.println("\n\nRecomendaciones: ");
            	for (int i = 0; i < songs.size(); i ++ ) {
            		System.out.println("Cancion: " + songs.get(i));
            	}
            	
            	
            	
            }
        }

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
    }
    
    
    
}
