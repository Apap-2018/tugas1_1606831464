package com.apap.tugas1.service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;
@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {
	@Autowired
    private PegawaiDb pegawaiDb;
 
	@Override
    public Optional<PegawaiModel> getPegawaiDetailByNip(String nip) {
        return pegawaiDb.findByNip(nip);
    }

	@Override
	public String getInstansiPegawai(String nip) {
		return pegawaiDb.findByNip(nip).get().getNama();
	}
	
	//hitung gaji
	@Override
	public long perhitunganGaji(List<JabatanModel> listJabatan, String nip) {
		double gajiFix = 0;
		for(int x=0;x<listJabatan.size();x++) {
			if(listJabatan.get(x).getGajiPokok() > gajiFix) {
				gajiFix = listJabatan.get(x).getGajiPokok();
			}
		}
		double tunjangan = pegawaiDb.findByNip(nip).get().getInstansi().getProvinsi().getPresentaseTunjangan();
		double gaji = gajiFix + ((tunjangan/100)*gajiFix);
		return (long)gaji;
	}

	@Override
	public void updatepegawai(PegawaiModel pegawai) {
		PegawaiModel pegawainya = pegawaiDb.findByNip(pegawai.getNip()).get();
		System.out.println(pegawainya.getInstansi().getProvinsi().getId());
		System.out.println(pegawai.getInstansi().getProvinsi().getId());
		pegawainya.setNip(generateNip(pegawai));
		pegawainya.setInstansi(pegawai.getInstansi());
		pegawainya.setJabatanList(pegawai.getJabatanList());
		pegawainya.setNama(pegawai.getNama());
		pegawainya.setTahunMasuk(pegawai.getTahunMasuk());
		pegawainya.setTanggalLahir(pegawai.getTanggalLahir());
		pegawainya.setTempatLahir(pegawai.getTempatLahir());
		pegawainya.getInstansi().setProvinsi(pegawai.getInstansi().getProvinsi());
		System.out.println(pegawainya.getInstansi().getProvinsi().getId());
		pegawaiDb.save(pegawainya);
	}
	
	//generate nip mantul
	@Override
	 public String generateNip(PegawaiModel pegawai) {
		DateFormat df = new SimpleDateFormat("ddMMYY");
		Date tglLahir = pegawai.getTanggalLahir();
		String formatted = df.format(tglLahir);
	  String idInstansi = "" + pegawai.getInstansi().getId();
	 
	  
	  String tahunMasuk = pegawai.getTahunMasuk();
	  String nip = idInstansi +formatted+ tahunMasuk;
	  int akhir = 1;
	  for(int i = 0; i < pegawaiDb.findAll().size(); i++) {
	   if(pegawaiDb.findAll().get(i).getNip().substring(0, 14).equalsIgnoreCase(nip)) {
	    akhir++;
	   }
	  }
	  String akhirnya = "" + akhir;
	  if(akhir < 10) {
		   akhirnya = "0" + akhirnya;
	}
	  
	  return nip+akhirnya;
	 }
	
	//kedua method ini untuk fitur 10
	@Override
	public List<PegawaiModel> getPegawaiMuda(InstansiModel instansi) {
		return pegawaiDb.findAllByInstansiOrderByTanggalLahirDesc(instansi);
	}

	@Override
	public List<PegawaiModel> getPegawaiTua(InstansiModel instansi) {
		return pegawaiDb.findAllByInstansiOrderByTanggalLahirAsc(instansi);
	}
}
