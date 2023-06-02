package com.upm.SistemasInteligentes.recommender.spotify;
import com.upm.SistemasInteligentes.recommender.spotify.messages.SolicitudRecomendador;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgenteVisualizacion extends Agent {
	public ArrayList<String> uris_canciones = new ArrayList();
    protected void setup() {
        SequentialBehaviour seq = new SequentialBehaviour(this);
        String cancion1 = JOptionPane.showInputDialog(null, "Introduzca cancion 1", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion1));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
    	
        String cancion2 = JOptionPane.showInputDialog(null, "Introduzca cancion 2", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion2));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        String cancion3 = JOptionPane.showInputDialog(null, "Introduzca cancion 3", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion3));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        String cancion4 = JOptionPane.showInputDialog(null, "Introduzca cancion 4", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion4));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        String cancion5 = JOptionPane.showInputDialog(null, "Introduzca cancion 5", "Input Dialog", JOptionPane.PLAIN_MESSAGE);
        seq.addSubBehaviour(new EnviarCancionBehaviour(cancion5));
        seq.addSubBehaviour(new EsperarRespuestaBehaviour());
        
        seq.addSubBehaviour(new EnviarUrisBehaviour());
        
        addBehaviour(seq);
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

            	uris_canciones.add(msg.getContent());
            	System.out.println(uris_canciones);

            	
            } else {
                block();
                
            }
        } 
    }
    
    private class EnviarUrisBehaviour extends OneShotBehaviour {
        public void action() {
        	//ACLMessage uris = new ACLMessage(ACLMessage.INFORM);
        	SolicitudRecomendador uris = new SolicitudRecomendador();
        	uris.setSeeds(uris_canciones);
        	ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        	try {
				msg.setContentObject((Serializable) uris);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            send(msg);
        }
    }
    
}
