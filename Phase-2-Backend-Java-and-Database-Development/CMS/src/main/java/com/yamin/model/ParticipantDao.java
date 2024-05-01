package com.yamin.model;

import java.util.List;

import com.yamin.exception.DataBaseDeleteException;
import com.yamin.exception.DataBaseInsertException;
import com.yamin.exception.DataBaseUpdateException;

public interface ParticipantDao {

	// Get all Participants
	public List<Participant> getAllParticipant();
	
	// Get a specific Participant by name
	public Participant getParticipantByName(String name);
	
	// Add a new Participant
	public void addParticipant(Participant Participant) throws DataBaseInsertException;
	
	// Update a specific Participant
	public boolean updateParticipant(Participant aParticipant) throws DataBaseUpdateException;
	
	// Delete a specific Participant
	public void deleteParticipant(String name) throws DataBaseDeleteException;
}
