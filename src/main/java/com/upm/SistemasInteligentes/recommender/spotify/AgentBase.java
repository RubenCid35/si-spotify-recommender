package com.upm.SistemasInteligentes.recommender.spotify;

import com.upm.SistemasInteligentes.recommender.spotify.AgentModel;

import java.util.Arrays;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;



public abstract class AgentBase extends Agent {
	protected AgentModel type;
	protected String[] params;

	protected void setup() {
		super.setup();
		this.params = Arrays.asList(getArguments()).toArray(new String[getArguments().length]);
	}

	public DFAgentDescription[] getAgentsDF(AgentModel type) {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType(type.getValue());
		template.addServices(templateSd);
		DFAgentDescription[] result = new DFAgentDescription[0];
		try {
			result = DFService.search(this, template);
		} catch (FIPAException e) {
			e.printStackTrace();
			loge("DFService.search don't work!!!");
		}
		return result;
	}

	public void registerAgentDF() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.type.getValue());
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		try {
			DFAgentDescription[] results = DFService.search(this, dfd);
			if (results == null || results.length == 0)
				DFService.register(this, dfd);
		} catch (FIPAException e) {
			loge("Unable to register.");
			this.doDelete();
		}
	}

	public void deregisterAgentDF() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			loge("Unable to deregister.");
			this.doDelete();
		}
	}

	@Override
	public void doDelete() {
		super.doDelete();
		loge(": Exit!!!");
	}

	public String[] getParams() {
		return this.params;
	}

	public void log(String s) {
		System.out.println(
				System.currentTimeMillis() + ": " + getLocalName() + "(" + getClass().getSimpleName() + ") " + s);
	}

	public void loge(String s) {
		System.err.println(
				System.currentTimeMillis() + ": " + getLocalName() + "(" + getClass().getSimpleName() + ") " + s);
	}
}