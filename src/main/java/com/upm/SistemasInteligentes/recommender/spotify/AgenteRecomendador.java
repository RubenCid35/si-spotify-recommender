package com.upm.SistemasInteligentes.recommender.spotify;

import jade.core.Agent;
import com.upm.SistemasInteligentes.recommender.spotify.AgentBase;
import com.upm.SistemasInteligentes.recommender.spotify.AgentModel;
import com.upm.SistemasInteligentes.recommender.spotify.behaviours.Receiver;
import com.upm.SistemasInteligentes.recommender.spotify.messages.SolicitudRecomendador;
import com.upm.SistemasInteligentes.recommender.spotify.messages.SolicitudNombreCanciones;

import com.upm.SistemasInteligentes.recommender.spotify.behaviours.Receiver;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;

import java.io.IOException;
import java.net.*;
import java.io.*;

import jade.core.AID;
import jade.lang.acl.*;

import java.util.ArrayList;

public class AgenteRecomendador extends AgentBase {
	
	// Estado de la Carga de los Datos
	boolean servidor_activo = false;

	// Conexión al servicio de conexión
	Socket socket;
	FSMBehaviour cyclic_behaviourr;

	private static final long serialVersionUID = 1L;
	public static final String NICKNAME = "Recomendador";
	
	// Comm 
	
	ACLMessage msg_request = null;
	ACLMessage msg_inform  = null;
	
	
	
	// Common Variables
	ArrayList<String> seeds = new ArrayList<String>();
	ArrayList<String> songs = new ArrayList<String>();
	ArrayList<String> songs_names = new ArrayList<String>();
	
	public AgenteRecomendador () {
		try {
			socket = new Socket("localhost", 9090);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
	}
	
	
	protected void setup() {
		
		super.setup();
		this.type = AgentModel.RECOMENDADOR;

	    MessageTemplate template_peticion = MessageTemplate.MatchPerformative( ACLMessage.REQUEST );
	    MessageTemplate template_cancion  = MessageTemplate.MatchPerformative( ACLMessage.INFORM );
	          
	    
        cyclic_behaviourr = new FSMBehaviour();
        cyclic_behaviourr.registerState(new Receiver(this, 1000, template_peticion) {
			private static final long serialVersionUID = 1L;

			public void handle( ACLMessage msg) 
            {  
				if (msg != null) {
					SolicitudRecomendador solicitud = new SolicitudRecomendador();
					try {
						solicitud = (SolicitudRecomendador) msg.getContentObject();
					}catch ( UnreadableException e) {}
					seeds = solicitud.getSeeds();
				}
            }
         }, "Step1");
        
        cyclic_behaviourr.registerState(new RecomendadorProxy(this), "Step2");
        cyclic_behaviourr.registerState(new RecogerCanciones(this) , "Step3");
        cyclic_behaviourr.registerState(new InformarCanciones(this), "Step4");

        cyclic_behaviourr.registerDefaultTransition("Step1", "Step2");
        cyclic_behaviourr.registerDefaultTransition("Step2", "Step3");
        cyclic_behaviourr.registerDefaultTransition("Step3", "Step4");
        cyclic_behaviourr.registerDefaultTransition("StepN", "Step1");
    
        // Initial State
        cyclic_behaviourr.setExecutionState("Step1");
        addBehaviour(cyclic_behaviourr);
        registerAgentDF();
        
	}
	
	class RecomendadorProxy extends SimpleBehaviour {
		boolean done = false;
		private AgenteRecomendador agent;
		public RecomendadorProxy (AgenteRecomendador agent ) {
			super(agent);
			this.agent = agent;
		}
		
		public void action () {
			

            try {
            	ObjectMapper mapper = new ObjectMapper();
                OutputStream outputStream = this.agent.socket.getOutputStream();
				InputStream inputStream = this.agent.socket.getInputStream();

			    PrintWriter writer = new PrintWriter(outputStream, true);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				
				// Crear un objeto para enviar datos al servidor
	            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

	            // Enviar un paquete al servidor
	            RequestRecSocket message = new RequestRecSocket(this.agent.seeds);
	            String json_msg = mapper.writeValueAsString(message);
	            writer.println(json_msg);
	            objectOutputStream.flush();

	            // Leer la respuesta del servidor
	            String json_response = reader.readLine();
	            ResponseRecSocket response = mapper.readValue(json_response, ResponseRecSocket.class);

	            if (response.status == 202 ) {
	            	songs = new ArrayList<String>(response.uris);
	            }else {
	              	songs = new ArrayList<String>();
	            }
	            done = true;
	            
            
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
              	songs = new ArrayList<String>();
			}
            done = true;
		}

		@Override
		public boolean done() {
			return done;
		}

	}

	class RecogerCanciones extends SimpleBehaviour {
		boolean done = false;
		private AgenteRecomendador agent;
		
		public RecogerCanciones (AgenteRecomendador agent ) {
			super(agent);
			this.agent = agent;
		}
		public void action () {
			
			SolicitudNombreCanciones content = new SolicitudNombreCanciones();
			content.setSongs(agent.songs);
			ACLMessage mensaje = new ACLMessage(ACLMessage.QUERY_IF);
            
			mensaje.addReceiver(getAID("AgentCatalogo"));
			try {
	            mensaje.setContentObject(content);
			}catch (IOException e) {
				e.printStackTrace(); 
				done = true;
				return ;
			}
            send(mensaje);
		
    	    MessageTemplate template_cancion  = MessageTemplate.MatchPerformative( ACLMessage.INFORM );
    	    ACLMessage msg = this.myAgent.blockingReceive(template_cancion);
            if (msg != null ) {
            	try {
            		ArrayList<String> respuesta = new ArrayList<String>();
            		respuesta = (ArrayList<String>) msg.getContentObject();
            		
            		this.agent.songs_names = respuesta;
            			
    			}catch (UnreadableException e) {
    				e.printStackTrace(); 
    				done = true;
    				return ;
    			}            }
    	    
            done = true;
		}
		public boolean done() {
			return done;
		}
}
	
	
	class InformarCanciones extends SimpleBehaviour {
		boolean done = false;
		private AgenteRecomendador agent;
		
		public InformarCanciones (AgenteRecomendador agent ) {
			super(agent);
			this.agent = agent;
		}
		public void action () {
			ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
			mensaje.addReceiver(getAID("AgentCatalogo"));
			try {
	            mensaje.setContentObject(this.agent.songs_names);
			}catch (IOException e) {
				e.printStackTrace(); 
				done = true;
				return ;
			}
            send(mensaje);
            

        	this.agent.seeds = new ArrayList<String>();
        	this.agent.songs = new ArrayList<String>();
        	this.agent.songs_names = new ArrayList<String>();
        	
        	done = true;
        	}
		public boolean done() {
			return done;
		}
	}
	
	class RequestRecSocket {
		boolean all;
		ArrayList<String> seeds;

		RequestRecSocket ( ArrayList<String> seed ) {
			all = false;
			seeds = seed;
		}
	}
	class ResponseRecSocket {
		int status;
		ArrayList<String> uris;
	}

	
	
}

