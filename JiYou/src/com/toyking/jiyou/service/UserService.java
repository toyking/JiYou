package com.toyking.jiyou.service;

import java.util.ArrayList;
import java.util.Map;

import com.toyking.jiyou.model.Job;
import com.toyking.jiyou.model.Party;
import com.toyking.jiyou.model.PartyAttender;
import com.toyking.jiyou.model.SquareStatus;
import com.toyking.jiyou.model.User;

/**
 * Created by WangTao on 2015/3/15.
 */
public interface UserService {
    public void Login(String username, String password) throws Exception;

    public void UserAdd(User user) throws Exception;

    public User GetUser(String username, String password) throws Exception;
    
    public User GetPublicUserInfoByUsername(String username) throws Exception;
    
    public void EditUser(User user) throws Exception;

    public void SquareAddStatus(SquareStatus square_status, User user) throws Exception;
    
    public ArrayList<Map<String, Object>> GetSquareStatusList(User user) throws Exception;

    public void PartyAdd(Party party, User user) throws Exception;
    
    public ArrayList<Map<String, Object>> GetPartyList(User user) throws Exception;
    
    public Party GetPartyById(int id) throws Exception;
    
    public void SquarePrizeAdd(int square_id, User user) throws Exception;
    
    public ArrayList<User> GetSquarePrize(int square_id) throws Exception; 
    
    public ArrayList<Map<String, Object>> GetPartyAttenderList(int party_id) throws Exception; 
    
    public void PartyAttenderAdd(PartyAttender party_attender,User user) throws Exception;
    
    public void JobAdd(Job job, User user) throws Exception;
    
    public ArrayList<Map<String, Object>> GetJobList(User user) throws Exception;
    
    public Job GetJobById(int job_id) throws Exception;
    
}
