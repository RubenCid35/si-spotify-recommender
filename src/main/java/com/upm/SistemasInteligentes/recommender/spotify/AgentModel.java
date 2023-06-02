package com.upm.SistemasInteligentes.recommender.spotify;

public enum AgentModel {
	VISUALIZACION(" Visualizacion"), 
	AGENTECATALOGO(" AgentCatalogo"), 
	RECOMENDADOR(" Recomendador"),
	DESCONOCIDO(" Desconocido");
	
	private final String value;

	AgentModel(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static AgentModel getEnum(String value) {
		switch (value) {
		case " Visualizacion":
			return VISUALIZACION;
		case " AgentCatalogo":
			return AGENTECATALOGO;
		case " Recomendador":
			return RECOMENDADOR;
		default:
			return DESCONOCIDO;
		}
	}
}