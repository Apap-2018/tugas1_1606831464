package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDb;

@Service
@Transactional
public class JabatanServiceImpl implements JabatanService{
	@Autowired
    private JabatanDb jabatanDb;
	@Override
	public List<JabatanModel> getAllJabatan() {
		return jabatanDb.findAll();
	}
	@Override
	public void addJabatan(JabatanModel jabatan) {
		jabatanDb.save(jabatan);
		
	}
	@Override
	public JabatanModel getJabatan(long id) {
		return jabatanDb.findById(id).get();
	}
	@Override
	public void updateJabatan(JabatanModel jabatan) {
		JabatanModel jabatanAsli = jabatanDb.findById(jabatan.getId()).get();
		jabatanAsli.setDeskripsi(jabatan.getDeskripsi());
		jabatanAsli.setGajiPokok(jabatan.getGajiPokok());
		jabatanAsli.setNama(jabatan.getNama());
	}
	@Override
	public void hapusJabatan(long id) {
		
		jabatanDb.deleteById(id);
		
	}

}
