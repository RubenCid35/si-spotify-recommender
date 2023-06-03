package com.upm.SistemasInteligentes.recommender.spotify;

import com.upm.SistemasInteligentes.recommender.spotify.messages.SolicitudRecomendador;
import com.upm.SistemasInteligentes.recommender.spotify.messages.SolicitudNombreCanciones;
import com.upm.SistemasInteligentes.recommender.spotify.messages.RespuestaNombreCanciones;
import com.upm.SistemasInteligentes.recommender.spotify.messages.InformeRecomendador;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;

import java.net.*;
import java.io.*;

import jade.lang.acl.*;

import java.util.ArrayList;
import java.util.List;

public class AgenteRecomendador extends AgentBase {
	
	// Estado de la Carga de los Datos
	boolean servidor_activo = false;

	// Conexión al servicio de conexión
	Socket socket;
	SequentialBehaviour cyclic_behaviourr;

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
		this.type = AgentModel.AGENTERECOMENDADOR;

        cyclic_behaviourr = new SequentialBehaviour();
        cyclic_behaviourr.addSubBehaviour(new ReceptorSolicitud(this));
        cyclic_behaviourr.addSubBehaviour(new RecomendadorProxy(this));
        cyclic_behaviourr.addSubBehaviour(new RecogerCanciones(this) );
        cyclic_behaviourr.addSubBehaviour(new InformarCanciones(this));
    
        // Initial State
        //cyclic_behaviourr.setExecutionState("Step1");
        addBehaviour(cyclic_behaviourr);
        registerAgentDF();
        
        
	}
	
	protected void takeDonw() {
		try {
			this.socket.close();
			
		} catch ( IOException e ) {
			e.printStackTrace();
		}		
	}
	
	class ReceptorSolicitud extends SimpleBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		boolean done = false;
		int i = 0;
		private AgenteRecomendador agent;
		public ReceptorSolicitud (AgenteRecomendador agent ) {
			super(agent);
			this.agent = agent;
		}
		public void action() {

			i+= 1;
			System.out.println("Inicio Recomendaciones");
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage msg = this.myAgent.blockingReceive(mt);
			
			if (msg != null) {
				SolicitudRecomendador solicitud = new SolicitudRecomendador();
				try {
					solicitud = (SolicitudRecomendador) msg.getContentObject();
				}catch ( UnreadableException e) {}
				seeds = solicitud.getSeeds();
				
				System.out.println("----------------------------------------------------------------------");				
				System.out.println(seeds);				
			}
			done = true;
		}
		public int onEnd() {
			return 1;
		}
		public boolean done() {
			// TODO Auto-generated method stub
			return done;
		}

	}
	
	class RecomendadorProxy extends SimpleBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
		public int onEnd() {
			return 1;
		}
	}

	class RecogerCanciones extends SimpleBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		boolean done = false;
		private AgenteRecomendador agent;
		
		public RecogerCanciones (AgenteRecomendador agent ) {
			super(agent);
			this.agent = agent;
		}
		public void action () {
			if (this.agent.songs.isEmpty()) {
				done = true;
				return ;
			}
			
			
			System.out.println("Recogiendo Los nombres de recomendaciones");
			SolicitudNombreCanciones content = new SolicitudNombreCanciones();
			content.setSongs(agent.songs);
            Utils.enviarMensaje(this.myAgent, "AgentCatalogo", content, ACLMessage.QUERY_IF);
            
    	    MessageTemplate template_cancion  = MessageTemplate.MatchPerformative( ACLMessage.INFORM );
    	    ACLMessage msg = this.myAgent.blockingReceive(template_cancion);
            if (msg != null ) {

    			System.out.println("Obtenidos Los nombres");
            	try {
            		RespuestaNombreCanciones respuesta = new RespuestaNombreCanciones();
            		respuesta = (RespuestaNombreCanciones) msg.getContentObject();
            		this.agent.songs_names = respuesta.getSongs();
        			
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
		public int onEnd() {
			return 1;
		}
}
	
	
	class InformarCanciones extends SimpleBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		boolean done = false;
		private AgenteRecomendador agent;
		
		public InformarCanciones (AgenteRecomendador agent ) {
			super(agent);
			this.agent = agent;
		}
		public void action () {
			
			
			InformeRecomendador contenido = new InformeRecomendador();
			contenido.setSongs(this.agent.songs_names);
			Utils.enviarMensaje(this.myAgent, "Visualizacion", contenido, ACLMessage.INFORM);

        	this.agent.seeds = new ArrayList<String>();
        	this.agent.songs = new ArrayList<String>();
        	this.agent.songs_names = new ArrayList<String>();
        	
        	done = true;
        }
		public boolean done() {
			return done;
		}
	}
	
	public static class RequestRecSocket implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public boolean all = true;
		public ArrayList<String> seeds;

		RequestRecSocket ( ArrayList<String> seed ) {
			all = true;
			seeds = seed;
		}
		
	}
	public static class ResponseRecSocket  implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1667804459801773890L;
		public int status;
		public List<String> uris;
	}

	
	
}

