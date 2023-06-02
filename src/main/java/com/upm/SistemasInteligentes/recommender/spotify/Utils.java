package com.upm.SistemasInteligentes.recommender.spotify;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;


public class Utils
{
	//En la clase Utils definimos capacidades para la recepci�n de mensajes, env�o de mensajes, b�squeda en el DF, �
	/** Permite buscar a todos los agentes que implementa un servicio de un tipo dado
	* @param agent Agente con el que se realiza la b�squeda
	* @param tipo Tipo de servidio buscado
	* @return Listado de agentes que proporciona el servicio */
	protected static DFAgentDescription [] buscarAgentes(Agent agent, String tipo)
	{
		//indico las caracter�sticas el tipo de servicio que quiero encontrar
		DFAgentDescription template=new DFAgentDescription();
		ServiceDescription templateSd=new ServiceDescription();
		templateSd.setType(tipo); //como define el tipo el agente coordinador tamiben podriamos buscar por nombre
		template.addServices(templateSd);
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(Long.MAX_VALUE);
		try
		{
			DFAgentDescription [] results = DFService.search(agent, template, sc);
			return results;
		}
		catch(FIPAException e)
		{
			//JOptionPane.showMessageDialog(null, "Agente "+getLocalName()+": "+e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return null;
	}
	/** Env�a un objeto desde el agente indicado a un agente que proporciona un servicio del tipo dado
	* @param agent Agente desde el que se va a enviar el servicio
	* @param tipo Tipo del servicio buscado
	* @param objeto Mensaje a Enviar */
	public static void enviarMensaje(Agent agent, String tipo, Object objeto, int perfomativa)
	{
		DFAgentDescription[] dfd;
		dfd=buscarAgentes(agent, tipo);
		try
		{
			if(dfd!=null)
			{
				ACLMessage aclMessage = new ACLMessage(perfomativa);
				for(int i=0;i<dfd.length;i++)
					aclMessage.addReceiver(dfd[i].getName());
				
				aclMessage.setOntology("ontologia");
				aclMessage.setLanguage(new SLCodec().getName());
				aclMessage.setEnvelope(new Envelope());
				aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
				aclMessage.setContentObject((Serializable)objeto);
				agent.send(aclMessage);
			}
		}
		catch(IOException e)
		{
			//JOptionPane.showMessageDialog(null, "Agente "+getLocalName()+": "+e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	
	/** Permite buscar los agentes que dan un servicio de un determinado tipo. Devuelve el primero de ellos.
	* @param agent Agentes desde el que se realiza la b�squeda
	* @param tipo Tipo de servicio buscado
	* @return Primer agente que proporciona el servicio*/
	protected static DFAgentDescription buscarAgente(Agent agent, String tipo)
	{
		//indico las caracter�sticas el tipo de servicio que quiero encontrar
		DFAgentDescription template=new DFAgentDescription();
		ServiceDescription templateSd=new ServiceDescription();
		templateSd.setType(tipo); //como define el tipo el agente coordinador tamiben podriamos buscar por nombre
		template.addServices(templateSd);
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(1L);
		try
		{
			DFAgentDescription [] results = DFService.search(agent, template, sc);
			if (results.length > 0)
			{
				//System.out.println("Agente "+agent.getLocalName()+" encontro los siguientes agentes");
				for (int i = 0; i < results.length; ++i)
				{
					DFAgentDescription dfd = results[i];
					AID provider = dfd.getName();
					//un mismo agente puede proporcionar varios servicios, solo estamos interasados en "tipo"
					Iterator it = dfd.getAllServices();
					while (it.hasNext())
					{
						ServiceDescription sd = (ServiceDescription) it.next();
						if (sd.getType().equals(tipo))
						{
							System.out.println("- Servicio \""+sd.getName()+"\" proporcionado por el agente "+provider.getName());
							return dfd;
						}
					}
				}
			}
			else
			{
				//JOptionPane.showMessageDialog(null, "Agente "+getLocalName()+" no encontro ningun servicio buscador", "Error",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch(FIPAException e)
		{
			//JOptionPane.showMessageDialog(null, "Agente "+getLocalName()+": "+e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/** Env�a un objeto desde el agente indicado a un agente que proporciona un servicio del tipo dado
	* @param agent Agente desde el que se va a enviar el servicio
	* @param tipo Tipo del servicio buscado
	* @param objeto Mensaje a Enviar */
	public static File abrirFichero(String ruta)
	{
        Scanner entrada = null;
        File f = new File(ruta);

        try 
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(fileChooser);   
            
  //Ver si cambiar con el par�metro que nos pasan como ruta
            String ruta2 = fileChooser.getSelectedFile().getAbsolutePath();
            f = new File(ruta2);                                                  
            entrada = new Scanner(f);
            while (entrada.hasNext()) 
            {
                System.out.println(entrada.nextLine());
            }
        } catch (FileNotFoundException e) 
          {
            System.out.println(e.getMessage());
          } 
          catch (NullPointerException e) 
          {
            System.out.println("No se ha seleccionado ning�n fichero");
          } 
          catch (Exception e) 
          {
            System.out.println(e.getMessage());
          } 
          finally 
          {
            if (entrada != null) 
            {
       //     	return entrada;
       //         entrada.close();
            	System.out.println("Fichero enviado");
            }
        }
       return f; 
	}
	
	public static void limpiarClasificacion(boolean [] clasificacion, int i)
	{
            for (int j=0;j<i;j++){
            	clasificacion[j] = false;
            }
       
	}
        
}