package com.upm.SistemasInteligentes.recommender.spotify.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class RespuestaNombreCanciones implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<String> songs;

	
	public void setSongs( ArrayList<String> song) {
		songs = new ArrayList<>(song);
	}

	public ArrayList<String> getSongs( ) {
		return songs;
	}
	
}