package com.upm.SistemasInteligentes.recommender.spotify.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class SolicitudRecomendador implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> seeds;

	
	public void setSeeds( ArrayList<String> seed) {
		seeds = new ArrayList<>(seed);
	}

	public ArrayList<String> getSeeds( ) {
		return seeds;
	}

	
}

