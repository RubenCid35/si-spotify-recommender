package com.upm.SistemasInteligentes.recommender.spotify;

public enum AgentModel {
	AGENTEVISUALIZACION("Visualizacion"), 
	AGENTECATALOGO("AgentCatalogo"), 
	AGENTERECOMENDADOR("Recomendador"),
	DESCONOCIDO("Desconocido");
	
	private final String value;

	AgentModel(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static AgentModel getEnum(String value) {
		switch (value) {
		case "Visualizacion":
			return AGENTEVISUALIZACION;
		case "AgentCatalogo":
			return AGENTECATALOGO;
		case "Recomendador":
			return AGENTERECOMENDADOR;
		default:
			return DESCONOCIDO;
		}
	}
}