package com.example.ifood.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.ifood.R
import com.example.ifood.databinding.ActivitySettingEmpresaBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.helper.UserFirebase
import com.example.ifood.model.Empresa
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class SettingEmpresaActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingEmpresaBinding
    private lateinit var storageReference : StorageReference
    private lateinit var dialog : AlertDialog
    private lateinit var idUserLogged : String
    private lateinit var resultGalery : ActivityResultLauncher<Intent>
    private lateinit var requestGalery : ActivityResultLauncher<String>
    private lateinit var firebaseRef : DatabaseReference
    private var urlIniciada : Uri? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingEmpresaBinding.inflate(layoutInflater)
        setContentView(binding.root)

       storageReference = FirebaseConfig.getInstanceStorage()
        idUserLogged  = UserFirebase.getIdUser()
        firebaseRef = FirebaseConfig.getInstanceDataBase()
        
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setListener()
        setRequestGalery()
        setResultGalery()
        recuperarDadosEmpresa()
      //  setImageProfile()

        binding.appCompatButtoSalvar.setOnClickListener {
            validarDadosEmpresas()
        }
    }

    private fun recuperarDadosEmpresa() {
        val empresaRef = firebaseRef
            .child("empresas")
            .child(idUserLogged)
            empresaRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot != null) {
                        val empresa = snapshot.getValue(Empresa()::class.java)
                        binding.editTextNomeEmpresa.setText( empresa?.nome)
                        binding.editTextCategoriaEmpresa.setText(empresa?.categoria)
                        binding.editTextTaxaEntrega.setText(empresa?.precoEntrega.toString())
                        binding.editTempoEspera.setText(empresa?.tempo)


                        val url = empresa?.urlImagem
                            if (url.toString() != ""){
                                Glide.with(this@SettingEmpresaActivity)
                                    .load(url)
                                    .into(binding.profileImage)

                            } else {
                                binding.profileImage.setImageResource(R.drawable.perfil)
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SettingEmpresaActivity, "algo deu errado", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onCancelled: ${error.message}")
                }

            })
    }

    private fun setRequestGalery() {
        requestGalery = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission){
                resultGalery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            } else {
                showDialogPermisson()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListener(){
        binding.profileImage.setOnClickListener {
            verificaPermissaoGaleria()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun verificaPermissaoGaleria() {
        val permissaoGaleira = validaPermissao(PERMISSAO_GALERIA)
        when {
            permissaoGaleira -> {
                resultGalery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }
            shouldShowRequestPermissionRationale(PERMISSAO_GALERIA) -> showDialogPermisson()
            else -> requestGalery.launch(PERMISSAO_GALERIA)
        }
    }

    private fun showDialogPermisson() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Atenção")
            .setMessage("Precisamos do acesso a galeria do dispositivo, clique em Permitir para continuar")
            .setNegativeButton("nao"){ _, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("sim") {_, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package",this.packageName , null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }
        dialog = builder.create()
        dialog.show()
    }

    private fun validaPermissao(permissao: String)  =
        (ContextCompat.checkSelfPermission(this, permissao) ==
                PackageManager.PERMISSION_GRANTED)


//    private fun setImageProfile() {
//        binding.fab.setOnClickListener {
//            val i = Intent(Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            if (i.resolveActivity(packageManager)!= null){
//                startActivityForResult(i, SELECAO_GALERIA)
//            }
//        }
//    }

    private fun setResultGalery(){
        resultGalery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.data?.data != null) {
                val bitMap : Bitmap =
                    if (Build.VERSION.SDK_INT < 28){
                        MediaStore.Images.Media.getBitmap(this.contentResolver, result.data?.data)
                    } else {
                        val source =
                            ImageDecoder.createSource(
                                this.contentResolver!!, result.data?.data!!
                            )
                        ImageDecoder.decodeBitmap(source)
                    }
                binding.profileImage.setImageBitmap(bitMap)

            val baos = ByteArrayOutputStream()


            bitMap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val dadosImagem = baos.toByteArray()
            val imagemRef = storageReference
                .child("imagens")
                .child("empresas")
                .child("$idUserLogged.jpeg")
            var uploadTask = imagemRef.putBytes(dadosImagem)
            uploadTask.addOnFailureListener{
                Toast.makeText(this, "erro ao carregar imagem", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "setResultGalery: ${it.message.toString()}")
            }
            uploadTask.addOnSuccessListener {
                imagemRef.downloadUrl.addOnCompleteListener {

                    urlIniciada = it.result

                }
            }

    }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK){
//            var imagem : Bitmap?
//            try{
//                when(requestCode){
//                    SELECAO_GALERIA -> {
//                        val localImagem : Uri = data!!.data!!
//                        imagem = MediaStore.Images.Media.getBitmap(
//                            contentResolver, localImagem
//                        )
//
//                        if (imagem != null){
//                            binding.profileImage.setImageBitmap(imagem)
//
//                            val baos = ByteArrayOutputStream()
//                            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos)
//                            val dadosImagem = baos.toByteArray()
//                            val imagemRef = storageReference
//                                .child("imagens")
//                                .child("empresas")
//                                .child("$idUserLogged.jpeg")
//                            val uploadTask = imagemRef.putBytes(dadosImagem)
//                            uploadTask.addOnFailureListener{
//                                Toast.makeText(this, "erro ao carregar imagem", Toast.LENGTH_SHORT).show()
//                            }
//                                uploadTask.addOnSuccessListener {
//                                    imagemRef.downloadUrl.addOnCompleteListener {
//
//                                    urlIniciada = it.result
//
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//    }


    private fun validarDadosEmpresas() {
        with(binding) {
            val nome = this.editTextNomeEmpresa.text.toString()
            val categoria = this.editTextCategoriaEmpresa.text.toString()
            val tempoEspera = this.editTempoEspera.text.toString()
            val taxaEntrega = this.editTextTaxaEntrega.text.toString()
            if (!nome.isNullOrEmpty() &&
                !categoria.isNullOrEmpty() &&
                !tempoEspera.isNullOrEmpty() &&
                taxaEntrega.isNullOrEmpty() &&
                    urlIniciada.toString().isNullOrEmpty()){
                textInputTaxaEntrega.error = "Preencha todos os campos!"
            } else {
                val empresa = Empresa(
                    idUserLogged,
                    urlIniciada.toString(),
                    nome,
                    tempoEspera,
                    categoria,
                    taxaEntrega.toDouble()

                )
                empresa.salvar()
                finish()
            }
        }
    }

    companion object {
        const val SELECAO_GALERIA = 200
        private val PERMISSAO_GALERIA = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
}