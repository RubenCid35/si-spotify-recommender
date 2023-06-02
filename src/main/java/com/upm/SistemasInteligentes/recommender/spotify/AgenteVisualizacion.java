package com.upm.SistemasInteligentes.recommender.spotify;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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

public class AgenteVisualizacion extends AgentBase {
	public ArrayList<String> uris_canciones = new ArrayList();
    protected void setup() {
		super.setup();
		this.type = AgentModel.AGENTEVISUALIZACION;
    	
        SequentialBehaviour seq = new SequentialBehaviour(this);
        String cancion1 = JOptionPane.showInputDialog(null, "Introduzca cancion 1", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion1));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
    	
        String cancion2 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion2));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        String cancion3 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion3));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        String cancion4 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion4));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        String cancion5 = JOptionPane.showInputDialog(null, "Introduzca el texto a buscar", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion5));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        
        addBehaviour(seq);
        registerAgentDF();
    }

    private class EnviarCancionBehaviour extends OneShotBehaviour {
    	private String cancion;
    	
		public EnviarCancionBehaviour(String cancion) {
    		this.cancion = cancion;
    	}
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(this.cancion); 
            msg.addReceiver(getAID("AgenteCatalogo"));

            send(msg);
        }
    }

    private class EsperarRespuestaBehaviour extends OneShotBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchSender(getAID("AgenteCatalogo"));
            ACLMessage msg = this.myAgent.blockingReceive(mt);
            if (msg.getContent() != "null") {
                System.out.println("1");

            	uris_canciones.add(msg.getContent());
            	System.out.println(uris_canciones);

            	
            } else {
                block();
                
            }
        } 
    }
    
    private class EnviarListaRecomendador extends CyclicBehaviour {
    	
        public void action() {
        	
        	SolicitudRecomendador contenido = new SolicitudRecomendador();
        	contenido.setSeeds(uris_canciones);
            // Crear el mensaje
            ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
            mensaje.addReceiver(getAID("AgenteRecomendador"));

            // Establecer la lista de canciones como contenido del mensaje
            try {
				mensaje.setContentObject(contenido);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Enviar el mensaje
            send(mensaje);
            System.out.println("AgenteVisualizacion envió la lista de canciones al AgenteRecomendador");

            // Esperar la respuesta del AgenteRecomendador
            MessageTemplate plantilla = MessageTemplate.MatchSender(getAID("AgenteRecomendador"));
            ACLMessage respuesta = receive(plantilla);

            if (respuesta != null) {
                System.out.println("AgenteVisualizacion recibió respuesta del AgenteRecomendador: " + respuesta.getContent());
                // Finalizar el comportamiento
                myAgent.removeBehaviour(this);
            } else {
                // Si no se recibe respuesta, se bloquea el comportamiento hasta recibir un nuevo mensaje
                block();
            }
        }
    }
    
    
    
}
