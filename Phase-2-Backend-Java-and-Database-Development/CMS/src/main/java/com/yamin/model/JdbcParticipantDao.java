package com.yamin.model;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcParticipantDao implements ParticipantDao {
	
	// Define a reference to a JdbcTemplateObject we can use to access the tables in the database
	private JdbcTemplate theMoneyBar;

	// Instantiate a JdbcTemplate object and assign to reference in the constructor
	public JdbcParticipantDao(DataSource aDataSource) {
		this.theMoneyBar = new JdbcTemplate(aDataSource);
	}


	@Override
	public List<Participant> getAllParticipant() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Participant getParticipantByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addParticipant(Participant Participant) throws DataBaseInsertException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateParticipant(Participant aParticipant) throws DataBaseUpdateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteParticipant(String name) throws DataBaseDeleteException {
		// TODO Auto-generated method stub
		
	}

}
