package com.apap.tugas1.service;
import java.util.List;
import java.util.Optional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;


public interface PegawaiService {
	Optional<PegawaiModel> getPegawaiDetailByNip(String nip);
	
	String getInstansiPegawai(String nip);
	
	long perhitunganGaji(List<JabatanModel> listJabatan,String nip) ;
	void updatepegawai(PegawaiModel pegawai);
	String generateNip(PegawaiModel pegawai);
	List<PegawaiModel> getPegawaiMuda(InstansiModel instansi);
	List<PegawaiModel> getPegawaiTua(InstansiModel instansi);
	void tambahPegawai(PegawaiModel pegawai);
	
}
