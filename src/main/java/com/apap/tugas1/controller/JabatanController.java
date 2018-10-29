package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class JabatanController {
	@Autowired
	private PegawaiService PegawaiService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value="/jabatan/tambah", method = RequestMethod.GET)
	private String tambahJabatan(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "tambah-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String tambahJabatanSubmit(@ModelAttribute JabatanModel jabatan,Model model,RedirectAttributes redirectAttrs) {
		jabatanService.addJabatan(jabatan);
		String message = "Jabatan " + jabatanService.getJabatan(jabatan.getId()).getNama() + " berhasil ditambah";
		redirectAttrs.addFlashAttribute("message", message);
		return "redirect:/jabatan/tambah";
}
	@RequestMapping("/jabatan")
	private String lihatJabatan(@RequestParam(value = "idJabatan", required = true) long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatan(idJabatan);
		model.addAttribute("jabatan",jabatan);
		model.addAttribute("infoGaji", (long) jabatan.getGajiPokok().doubleValue());
		model.addAttribute("totalPegawai", jabatanService.getTotalPegawai(jabatan));
		return "view-jabatan";
	}
	@RequestMapping(value = "/jabatan/ubahh", method = RequestMethod.GET)
	private String ubahPegawai(@RequestParam(value = "idJabatan", required = true) long idJabatan, Model model) {
		JabatanModel jabatan = new JabatanModel();
		jabatan.setId(idJabatan);
		model.addAttribute("jabatan",jabatan);
		return "ubah-jabatan";
	}
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String ubahPegawaiSubmit(@ModelAttribute JabatanModel jabatan, Model model,RedirectAttributes redirectAttrs) {
		jabatanService.updateJabatan(jabatan);
		String message = "Jabatan " + jabatanService.getJabatan(jabatan.getId()).getNama() + " berhasil diubah";
		redirectAttrs.addFlashAttribute("message", message);
		redirectAttrs.addAttribute("idJabatan", jabatan.getId());
		return "redirect:/jabatan/ubahh";
	}
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.POST)
	private String hapusPegawaiSubmit(long idJabatan, Model model,RedirectAttributes redirectAttrs) {
		if(jabatanService.getJabatan(idJabatan).getPegawaiList().isEmpty()) {
			String message = "Jabatan " + jabatanService.getJabatan(idJabatan).getNama() + " berhasil dihapus";
			jabatanService.hapusJabatan(idJabatan);
			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/";
		}
		else {
			String message = "Jabatan " + jabatanService.getJabatan(idJabatan).getNama() + " tidak berhasil dihapus";
			redirectAttrs.addFlashAttribute("message", message);
			redirectAttrs.addAttribute("idJabatan", idJabatan);
			return "redirect:/jabatan";
		}
	}
	@RequestMapping(value="/jabatan/viewall", method = RequestMethod.GET)
	private String viewallJabatan(Model model) {
		model.addAttribute("listJabatan",jabatanService.getAllJabatan());
		return "view-allJabatan";
	}
}
