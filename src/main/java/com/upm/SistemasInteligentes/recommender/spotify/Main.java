package com.upm.SistemasInteligentes.recommender.spotify;



import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import com.upm.SistemasInteligentes.recommender.spotify.AgenteCatalogo;
import com.upm.SistemasInteligentes.recommender.spotify.AgenteRecomendador;
import com.upm.SistemasInteligentes.recommender.spotify.AgenteVisualizacion;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "false");
        profile.setParameter(Profile.MAIN_PORT, "10183"); // Cambiar el número de puerto según sea necesario


        AgentContainer container = rt.createMainContainer(profile);
        try {
        	
        	ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        	rt.createAgentContainer(pContainer);
        	System.out.println("Containers created");
        	System.out.println("Launching the rma agent on the main container ...");
        	
        	container.createNewAgent("rma","jade.tools.rma.rma", new Object[0]).start();
        	container.createNewAgent(AgenteCatalogo.class.getName(), AgenteCatalogo.class.getName(),  new Object[]{"0"}).start();
        	container.createNewAgent(AgenteRecomendador.class.getName(), AgenteRecomendador.class.getName(),  new Object[]{"0"}).start();
        	container.createNewAgent(AgenteVisualizacion.class.getName(), AgenteVisualizacion.class.getName(),  new Object[]{"0"}).start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
