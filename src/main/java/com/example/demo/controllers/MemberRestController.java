package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.EvenementBean;
import com.example.demo.beans.OutilBean;
import com.example.demo.beans.PublicationBean;
import com.example.demo.entities.EnseignantChercheur;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Membre;
import com.example.demo.service.IMemberService;

@RestController
@CrossOrigin(origins = "http://localhost:4200",methods = 
{RequestMethod.POST,RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT},allowedHeaders = "*")
public class MemberRestController {
	@Autowired
	IMemberService memberService;

	@RequestMapping(value = "/membres", method = RequestMethod.GET)
	public List<Membre> findMembres() {
		List<Membre> membres = memberService.findAll();
		membres.forEach(member -> {
			List<PublicationBean> listeOfPublications = memberService.findPublicationsByAuteur(member.getId());
			List<EvenementBean> listOfEvents = memberService.findEvenementByParticipant(member.getId());
			List<OutilBean> listOfOutils = memberService.findOutilByMembre(member.getId());
			member.setPublications(listeOfPublications);
			member.setEvenements(listOfEvents);
			member.setOutils(listOfOutils);
		});
		return membres;
	}

	@GetMapping(value = "/membre/{id}")
	public Membre findOneMemberById(@PathVariable Long id) {
		List<PublicationBean> listeOfPublications = memberService.findPublicationsByAuteur(id);
		List<EvenementBean> listOfEvents = memberService.findEvenementByParticipant(id);
		List<OutilBean> listOfOutils = memberService.findOutilByMembre(id);
		Membre member = memberService.findMember(id);
		member.setPublications(listeOfPublications);
		member.setEvenements(listOfEvents);
		member.setOutils(listOfOutils);
		return member;
	}

	@GetMapping(value = "/membre/search/cin")
	public Membre findOneMemberByCin(@RequestParam String cin) {
		return memberService.findByCin(cin);
	}

	@GetMapping(value = "/membre/search/email")
	public Membre findOneMemberByEmail(@RequestParam String email) {
		return memberService.findByEmail(email);
	}

	@PostMapping(value = "/membres/enseignant")
	public Membre addMembre(@RequestBody EnseignantChercheur m) {
		return memberService.addMember(m);
	}

	@PostMapping(value = "/membres/etudiant")
	public Membre addMembre(@RequestBody Etudiant e) {
		return memberService.addMember(e);
	}

	@DeleteMapping(value = "/membres/{id}")
	public void deleteMembre(@PathVariable Long id) {
		memberService.deleteMember(id);
	}	

	@PutMapping(value = "/membres/etudiant/{id}")
	public Membre updatemembre(@PathVariable Long id, @RequestBody Etudiant p) {
		p.setId(id);
		return memberService.updateMember(p);
	}

	@PutMapping(value = "/membres/enseignant/{id}")
	public Membre updateMembre(@PathVariable Long id, @RequestBody EnseignantChercheur p) {
		p.setId(id);
		return memberService.updateMember(p);
	}
}