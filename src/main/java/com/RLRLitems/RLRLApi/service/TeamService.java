package com.RLRLitems.RLRLApi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RLRLitems.RLRLApi.entity.Team;
import com.RLRLitems.RLRLApi.repository.TeamRepository;


@Service
public class TeamService {
	
	private static final Logger logger = LogManager.getLogger(ProductService.class);

	
	@Autowired
	private TeamRepository repo;
	
	public Iterable<Team> getTeams(){
		return repo.findAll();
	}
	
	public Team createTeam(Team team) {
		return repo.save(team);
	}
	
	public Team updateTeam(Team team, Long id) throws Exception{
		try {
			Team updateTeam = repo.findOne(id);
			updateTeam.setName(team.getName());
			return repo.save(updateTeam);
		} catch (Exception e) {
			logger.error("Exception occured while trying to update team: " + id, e);
			throw new Exception("Unable to update team.");
		}
	}
	
	public void removeTeam(Long id) throws Exception {
		try {
			repo.delete(id);			
		}catch(Exception e) {
			logger.error("Exception occured while trying to delete team: " + id, e);
			throw new Exception("Unable to delete team.");
		}
	}
	
	public Team getTeam(Long id) {
		return repo.findOne(id);
	}
	
	

}
