


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "false");
        profile.setParameter(Profile.MAIN_PORT, "1179"); // Cambiar el número de puerto según sea necesario


        AgentContainer container = rt.createMainContainer(profile);
        try {
            AgentController agVisualizacion = container.createNewAgent("AgenteVisualizacion", "AgenteVisualizacion", null);
            AgentController agCatalogo = container.createNewAgent("AgenteCatalogo", "AgenteCatalogo", null);

            agVisualizacion.start();
            agCatalogo.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
