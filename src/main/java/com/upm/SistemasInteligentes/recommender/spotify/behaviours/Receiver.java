package com.upm.SistemasInteligentes.recommender.spotify.behaviours;
//
//		myReceiver:   a more user friendly ReceiverBehaviour
//
//		Creation: new myReceiver(Agent, Timeout (or -1), MessageTemplate )
//
//			- terminates when 1) desired message is received OR timeout expires
//       - on termination, handle(msg) is called
//                          ( J.Vaucher sept. 7 2003 )
// ---------------------------------------------------------------------------

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.*;

import com.upm.SistemasInteligentes.recommender.spotify.AgentBase;

public class Receiver extends SimpleBehaviour
{
	
	private MessageTemplate template;
	private long    timeOut, 
	                wakeupTime;

	private boolean finished;
	
	private ACLMessage msg;
	private Agent agent;
	
  public ACLMessage getMessage() { return msg; }
  
  
  public Receiver(Agent a, int millis, MessageTemplate mt) {
    super(a);
    timeOut = millis;
    template = mt;
    agent = a;
  }
  
	public void onStart() {
		wakeupTime = (timeOut<0 ? Long.MAX_VALUE
		              :System.currentTimeMillis() + timeOut);
	}
		
	public boolean done () {
		return finished;
	}
	
	public void action() 
	{
		if(template == null)
      	msg = myAgent.receive();
		else
			msg = myAgent.receive(template);

		if( msg != null) {
			finished = true;
			handle( msg );
			return;
		}
      long dt = wakeupTime - System.currentTimeMillis();
      if ( dt > 0 ) 
      	block(dt);
      else {
			finished = true;
			handle( msg );
      }
	}

	public void handle( ACLMessage m) { /* can be redefined in sub_class */ }
	
	public void reset() {
		msg = null;
		finished = false;
		super.reset();
  	}
  	
	public void reset(int dt) {
		timeOut= dt;
		reset();
  	}

}
