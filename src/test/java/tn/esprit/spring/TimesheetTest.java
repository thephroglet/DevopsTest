package tn.esprit.spring;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.EmployeServiceImpl;
import tn.esprit.spring.services.EntrepriseServiceImpl;
import tn.esprit.spring.services.TimesheetServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimesheetTest {
	private static final Logger l = LogManager.getLogger(TimesheetTest.class);
	
	@Autowired
	TimesheetServiceImpl ts;
	
	@Autowired
	EmployeRepository emr;
	
	@Autowired 
	EmployeServiceImpl ems;
	
	@Autowired
	EntrepriseServiceImpl es;
	
	@Autowired
	TimesheetRepository tr;
	
	@Autowired
	MissionRepository mr;
	
	@Autowired
	DepartementRepository dr;
	
	@Test
	public void ajouterMissionTEST() {
		try{
			l.info("In ajouterMissionTEST" );
		Mission mission = new Mission("Mission1","20/10/2021");
		l.info("Mission Creation");
		int idmission = ts.ajouterMission(mission);
		l.info("Mission added successfully.");
		Optional<Mission> missionOpt = mr.findById(idmission);
		if (missionOpt.isPresent())
			mission = missionOpt.get();
		if (mission != null)
		    mr.delete(mission);
		l.info("Mission deleted successfully.");
	} catch (NullPointerException e) {
		l.error(e.getMessage());
	}
		
	}
	@Test
	public void affecterMissionADepartementTEST() {
		try {
			l.info("In affecterMissionADepartementTEST" );

			Mission mission = new Mission("Mission1","Bugs fixing");
			l.info("Mission Creation");
			int idmission = ts.ajouterMission(mission);
			l.info("Mission added successfully.");
			Departement department = new Departement("Info");
			l.info("Department Creation.");
			int iddep = es.ajouterDepartement(department);
			l.info("Department added successfully.");
			ts.affecterMissionADepartement(idmission, iddep);
			l.info("Mission affected successfully.");
			} catch (NullPointerException e) {
				l.error(e.getMessage());
			}
		}
	
	@Test
	public void ajouterTimesheetTEST(){
		try{
			l.info("In ajouterTimesheetTEST");
		Date date = new Date();
		l.info("Timesheet Creation");
		TimesheetPK timesheetPK = new TimesheetPK();
		timesheetPK.setDateDebut(date);
		timesheetPK.setDateFin(date);
		timesheetPK.setIdEmploye(1);
		timesheetPK.setIdMission(7);
		
		Timesheet timesheet = new Timesheet();
		timesheet.setTimesheetPK(timesheetPK);
		timesheet.setValide(false); 
		tr.save(timesheet);
		l.info("Timesheet added successfully.");
	} catch (NullPointerException e) {
		l.error(e.getMessage());
	}
	}
	
	@Test
	public void findAllMissionByEmployeJPQLTEST(){
		try{
			l.info("In findAllMissionByEmployeJPQLTEST");
	tr.findAllMissionByEmployeJPQL(1);
	l.info("Missions found successfully.");
		} catch (NullPointerException e) {
			l.error(e.getMessage());
		}
}
	@Test
	public void getAllEmployeByMissionTEST()
	{
		try{
			tr.getAllEmployeByMission(7);
			l.info("Employees found successfully.");
		}catch (NullPointerException e){
			l.error(e.getMessage());
		}
	}
	
	@Test 
	public void validerTimesheetTEST(){
		l.info("In validerTimesheetTEST");
		Optional<Employe> employeOpt = emr.findById(2);
		Employe employe = new Employe();
		if (employeOpt.isPresent())
			employe = employeOpt.get();
		Optional<Mission> missionOpt = mr.findById(7);
		Mission mission = new Mission();
		if (missionOpt.isPresent())
			mission = missionOpt.get();
		if(!employe.getRole().equals(Role.CHEF_DEPARTEMENT)){
			l.info("WRONG USER! This USER should be HEAD OF DEPARTMENT.");

			return;
		}
		boolean chefDeLaMission = false;
		for(Departement dep : employe.getDepartements()){
			if(dep.getId() == mission.getDepartement().getId()){
				chefDeLaMission = true;
				break;
			}
		}
		if(!chefDeLaMission){
			l.info("This USER should be HEAD OF DEPARTMENT of the required mission...");
			return;
		}
		Date date = new Date(); 
		TimesheetPK timesheetPK = new TimesheetPK(7, 2, date, date);
		Timesheet timesheet =tr.findBytimesheetPK(timesheetPK);
		timesheet.setValide(true);
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dated = dateFormat.format(timesheet.getTimesheetPK().getDateDebut());
		if(l.isInfoEnabled() && dated != null){
		l.info(dated);
		}}

}
