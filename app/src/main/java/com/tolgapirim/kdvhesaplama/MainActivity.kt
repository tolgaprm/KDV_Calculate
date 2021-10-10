package com.tolgapirim.kdvhesaplama


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tolgapirim.kdvhesaplama.databinding.ActivityMainBinding
import java.math.MathContext
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private var kdvOrani:Double = 0.18
    private val KDVLI ="kdvli"
    private val KDVSIZ ="kdvsiz"
    private var islem =KDVSIZ


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // islem kısmı sonuc olarak kdvli veya kdvsiz olarak ayarladım, butona basdığımda hangisine göre işlem yapacağımı belirlemek için
            when(checkedId){
                R.id.kdv_dahil_kdvsiz_ilk_fiyat -> {binding.textView3.setText(getString(R.string.kdv_dahil_tutar)); islem= KDVSIZ}
                else -> {binding.textView3.setText(getString(R.string.kdv_haric_tutar)); islem = KDVLI}
            }

        }


        binding.radioGroupKDVOrani.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.yuzde1 -> {binding.kdvTutariText.visibility = View.INVISIBLE; kdvOrani = 0.1}
                R.id.yuzde8 -> {binding.kdvTutariText.visibility = View.INVISIBLE; kdvOrani = 0.8}
                R.id.yuzde18 -> {binding.kdvTutariText.visibility = View.INVISIBLE; kdvOrani = 0.18 }
                else -> {binding.kdvTutariText.visibility = View.VISIBLE}
            }
        }

        binding.hesaplaBtn.setOnClickListener { hesapla() }

        binding.temizleBtn.setOnClickListener { temizle() }

    }


    private fun sonucYazdir(_fiyat:Double, _sonuc:Double, islem:String){
        binding.sonucKdvDahilTutar.visibility = View.VISIBLE
        binding.sonucKdvHaricTutar.visibility = View.VISIBLE

        val sonuc = NumberFormat.getCurrencyInstance().format(_sonuc)
        val fiyat = NumberFormat.getCurrencyInstance().format(_fiyat)


        if (islem == KDVLI){
            binding.sonucKdvHaricTutar.setText(getString(R.string.kdv_haric_tutar_s,(fiyat)))
            binding.sonucKdvDahilTutar.setText(getString(R.string.kdv_dahil_tutar_s,(sonuc)))

        }else{
            binding.sonucKdvDahilTutar.setText(getString(R.string.kdv_dahil_tutar_s,(fiyat)))
            binding.sonucKdvHaricTutar.setText(getString(R.string.kdv_haric_tutar_s,(sonuc)))
        }


    }

    private fun hesapla(){
        val sonuc:Double
        val kdvTutar:Double


            val fiyat = binding.fiyat.text.toString().toDoubleOrNull()

            if(fiyat!=null){

                if(binding.radioGroup.checkedRadioButtonId == R.id.diger){
                    kdvOrani = binding.kdvTutariText.text.toString().toDouble().div(100)
                }

                if(islem == KDVLI){
                    kdvTutar = fiyat * kdvOrani!!
                    sonuc = fiyat + kdvTutar

                    sonucYazdir(fiyat,sonuc,KDVLI)



                }else if(islem == KDVSIZ){
                    sonuc = fiyat / (1+ (kdvOrani!!))
                    sonucYazdir(fiyat,sonuc,KDVSIZ)
                    println("KDVsiz Fiyat: $sonuc")
                }

            }else{
                Toast.makeText(this,  "Lütfen fiyat bilgisini giriniz", Toast.LENGTH_SHORT).show()
            }



        }


    private fun temizle(){
        binding.fiyat.setText("")

        binding.radioGroup.check(R.id.kdv_dahil_kdvsiz_ilk_fiyat)

        binding.sonucKdvDahilTutar.visibility = View.INVISIBLE
        binding.sonucKdvHaricTutar.visibility = View.INVISIBLE
        binding.kdvTutariText.setText("")
    }


}