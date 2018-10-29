package com.apap.tugas1.controller;


import com.apap.tugas1.model.*;
import com.apap.tugas1.service.*;
import com.apap.tugas1.service.PegawaiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService PegawaiService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("listJabatan",jabatanService.getAllJabatan());
		model.addAttribute("listInstansi", instansiService.getAllInstansi());
		return "home";
	}
	@RequestMapping(value = "/pegawai/tambah/instansi",method = RequestMethod.GET)
	private @ResponseBody List<InstansiModel> cekInstansi(@RequestParam(value="provinsiId") int provinsiId) {
		
		ProvinsiModel getProv = provinsiService.getProvinsibyId(provinsiId);
		
		return instansiService.getInstansibyProvinsi(getProv);
	}
	@RequestMapping(value = "/pegawai/cari",method = RequestMethod.GET)
	private  String filter(@RequestParam(value = "idProvinsi", required=false) Optional<String> idProvinsi,
			@RequestParam(value="idInstansi",  required=false) Optional<String>  id_instansi,
			@RequestParam(value="idJabatan", required=false) Optional<String>  id_jabatan,
			Model model) {
		List<JabatanModel> allJabatan = jabatanService.getAllJabatan();
		List<InstansiModel> allInstansi = instansiService.getAllInstansi();
		List<ProvinsiModel> allProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("allInstansi",allInstansi);
		model.addAttribute("allProvinsi",allProvinsi);
		model.addAttribute("allJabatan",allJabatan);
		
		
		List<PegawaiModel> allPegawai = PegawaiService.getAllPegawai();
		List<PegawaiModel> result = new ArrayList<>();
		if (idProvinsi.isPresent()) {
			
			if (id_instansi.isPresent() && id_jabatan.isPresent()) {
				// ALL instansi, jabatan, provinsi
				//pke instansi per provinsi aja
				
				List<PegawaiModel> temp = new ArrayList<>();
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi);
				Long idJabatan = Long.parseLong(id_jabatan.get());
				
				JabatanModel jabatan = jabatanService.getJabatan(idJabatan);
				
				temp = PegawaiService.getPegawaiByInstansi(instansi);
			
				for (PegawaiModel peg : temp) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
				System.out.println(result.size());
			}
			else if (!(id_instansi.isPresent()) && id_jabatan.isPresent()) {
				//provinsi
				//jabatan
				//provinsi & jabatan
				List<PegawaiModel> temp = new ArrayList<>();
				int idProv = Integer.parseInt(idProvinsi.get());
				ProvinsiModel prov = provinsiService.getProvinsibyId(idProv);
				for (PegawaiModel peg : allPegawai) {
					if (peg.getInstansi().getProvinsi().equals(prov)) {
						temp.add(peg);
					}
				}
				Long idJabatan = Long.parseLong(id_jabatan.get());
				JabatanModel jabatan = jabatanService.getJabatan(idJabatan);
				for (PegawaiModel peg : temp) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
			}
			else if(id_instansi.isPresent() && !(id_jabatan.isPresent())) { 
				//provinsi dan instansi
				//System.out.println("provinsi dan instansi");
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi);
				result = PegawaiService.getPegawaiByInstansi(instansi);
				
			}
			else if(!(id_instansi.isPresent()) && !(id_jabatan.isPresent())) {
				//just provinsi
				int idProv = Integer.parseInt(idProvinsi.get());
				ProvinsiModel prov = provinsiService.getProvinsibyId(idProv);
				for (PegawaiModel peg : allPegawai) {
					if(peg.getInstansi().getProvinsi().equals(prov)) {
						result.add(peg);
					}
				}
			}
		}
		else {
			//jabatan
			//instansi
			//jabatan dan instansi-worked
			if (id_jabatan.isPresent() && id_instansi.isPresent()) {
				List<PegawaiModel> temp = new ArrayList<>();
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi);
				Long idJabatan = Long.parseLong(id_jabatan.get());
				JabatanModel jabatan = jabatanService.getJabatan(idJabatan);
				temp = PegawaiService.getPegawaiByInstansi(instansi);
				for (PegawaiModel peg : temp) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
			}
			
			//jabatan doang
			else if(id_jabatan.isPresent() && !(id_instansi.isPresent())) {
				Long idJabatan = Long.parseLong(id_jabatan.get());
				JabatanModel jabatan = jabatanService.getJabatan(idJabatan);
				for (PegawaiModel peg : allPegawai) {
					for (JabatanModel jab : peg.getJabatanList()) {
						if (jab.equals(jabatan)) {
							result.add(peg);
						}
					}
				}
			}
			//instansi doang
			else if(!(id_jabatan.isPresent()) && id_instansi.isPresent()) {
				Long idInstansi = Long.parseLong(id_instansi.get());
				InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi);
				result = PegawaiService.getPegawaiByInstansi(instansi);
			}
			else if(!(id_jabatan.isPresent()) && !(id_instansi.isPresent())) {
				result = null;
			}
		}
		model.addAttribute("allData",result);
		return "cari-pegawai";
	}
	@RequestMapping("/pegawai")
	private String lihatPegawai(@RequestParam(value = "nip", required = true) String nip, Model model) {
		PegawaiModel pegawai = PegawaiService.getPegawaiDetailByNip(nip).get();
		long gaji = PegawaiService.perhitunganGaji(pegawai.getJabatanList(), nip);
		model.addAttribute("pegawai",pegawai);
		model.addAttribute("gaji",gaji);
		return "view-pegawai";
	}
	
	//buat request form ubah pegawai
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.GET)
	private String ubahPegawai(@RequestParam(value = "nip", required = true) String nip, Model model) {
		PegawaiModel pegawai = PegawaiService.getPegawaiDetailByNip(nip).get();
		model.addAttribute("pegawai",pegawai);
		model.addAttribute("listInstansi", instansiService.getAllInstansi());
		model.addAttribute("listProvinsi", provinsiService.getAllProvinsi());
		model.addAttribute("listJabatan", jabatanService.getAllJabatan());
		return "update-pegawai";
	}
	
	//rownya kalau ditambah
	@RequestMapping(value="/pegawai/ubah", method = RequestMethod.POST, params= {"addRow"})
	public String addRow(@RequestParam(value = "nip", required = true) String nip,@ModelAttribute PegawaiModel pegawai,Model model) {
		
		if (pegawai.getJabatanList() == null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
        }
		pegawai.getJabatanList().add(new JabatanModel());
		
		model.addAttribute("pegawai", pegawai);
		
		model.addAttribute("listInstansi", instansiService.getAllInstansi());
		model.addAttribute("listProvinsi", provinsiService.getAllProvinsi());
		model.addAttribute("listJabatan", jabatanService.getAllJabatan());
		return "update-pegawai";
	}
	@RequestMapping(value="/pegawai/ubah", method = RequestMethod.POST, params= {"update"})
	public String update(@RequestParam(value = "nip", required = true) String nip,@ModelAttribute PegawaiModel pegawai,Model model) {
		PegawaiModel pegawaiReal = PegawaiService.getPegawaiDetailByNip(nip).get();
		PegawaiService.updatepegawai(pegawai);
		
		long gaji = PegawaiService.perhitunganGaji(pegawai.getJabatanList(), pegawaiReal.getNip() );
		model.addAttribute("pegawai",pegawaiReal);
		model.addAttribute("gaji",gaji);
		
		return "update-peg-berhasil";
	}
	@RequestMapping(value="/pegawai/termuda-tertua", method=RequestMethod.GET)
	public String pegawaiTuaMuda(@RequestParam(value="idInstansi") Long idInstansi, Model model) {
		InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi);
		List<PegawaiModel> listPegawaiMuda = PegawaiService.getPegawaiMuda(instansi);
		List<PegawaiModel> listPegawaiTua = PegawaiService.getPegawaiTua(instansi);
		PegawaiModel pegawaiMuda = listPegawaiMuda.get(0);
		PegawaiModel pegawaiTua = listPegawaiTua.get(0);
		String namaTua = listPegawaiTua.get(0).getNama();
		String namaInstansiMuda = pegawaiMuda.getInstansi().getNama();
		String namaInstansiTua = pegawaiTua.getInstansi().getNama();
		List<JabatanModel> listJabatanMuda = pegawaiMuda.getJabatanList();
		List<JabatanModel> listJabatanTua = pegawaiTua.getJabatanList();
		model.addAttribute("pegawaiMuda", pegawaiMuda);
		model.addAttribute("pegawaiTua", pegawaiTua);
		model.addAttribute("namaInstansiMuda", namaInstansiMuda);
		model.addAttribute("namaInstansiTua", namaInstansiTua);
		model.addAttribute("listJabatanMuda", listJabatanMuda);
		model.addAttribute("listJabatanTua", listJabatanTua);
		return "pegawaiTuaMuda";
	}
	
	@RequestMapping(value = "/instansi/getFromProvinsi", method = RequestMethod.GET)
	@ResponseBody
	public List<InstansiModel> getInstansi(@RequestParam (value = "provinsiId", required = true) int provinsiId) {
	    ProvinsiModel provinsi = provinsiService.getProvinsibyId(provinsiId);
		return instansiService.getInstansibyProvinsi(provinsi);
	}
	
	@RequestMapping(value="/pegawai/tambah", method = RequestMethod.GET)
	private String tambahPegawai(Model model) {
		System.out.println(provinsiService.getAllProvinsi().size());
		model.addAttribute("listInstansi", instansiService.getAllInstansi());
		model.addAttribute("listProvinsi", provinsiService.getAllProvinsi());
		model.addAttribute("pegawai", new PegawaiModel());
		
		return "tambah-pegawai";
	}
	@RequestMapping(value="/pegawai/tambah", method = RequestMethod.POST, params= {"addRow"})
	public String addRow(@ModelAttribute PegawaiModel pegawai,Model model) {
		
		if (pegawai.getJabatanList() == null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
        }
		pegawai.getJabatanList().add(new JabatanModel());
		
		model.addAttribute("pegawai", pegawai);
		
		model.addAttribute("listInstansi", instansiService.getAllInstansi());
		model.addAttribute("listProvinsi", provinsiService.getAllProvinsi());
		model.addAttribute("listJabatan", jabatanService.getAllJabatan());
		return "tambah-pegawai";
	}
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params= {"add"})
	private String tambahPegawaiSubmit(@ModelAttribute PegawaiModel pegawai,Model model) {
		System.out.println(pegawai.getJabatanList().size());
		PegawaiService.tambahPegawai(pegawai);
		String nip = PegawaiService.generateNip(pegawai);
		System.out.println(nip);
		System.out.println(pegawai.getJabatanList().size());
		//long gaji = PegawaiService.perhitunganGaji(pegawai.getJabatanList(), nip);
		
		model.addAttribute("pegawai", pegawai);
		//model.addAttribute("gaji",gaji);
		return "tambah-pegawai-berhasil";
}
}
