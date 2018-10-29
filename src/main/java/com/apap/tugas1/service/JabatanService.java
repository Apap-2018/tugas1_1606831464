package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	List<JabatanModel> getAllJabatan();
	void addJabatan(JabatanModel jabatan);
	JabatanModel getJabatan(long id);
	void updateJabatan(JabatanModel jabatan);
	void hapusJabatan(long id);
	int getTotalPegawai(JabatanModel jabatan);
}
