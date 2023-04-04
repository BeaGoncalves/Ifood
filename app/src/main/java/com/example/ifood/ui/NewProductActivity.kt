package com.example.ifood.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.ifood.R
import com.example.ifood.databinding.ActivityNewProductBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.helper.UserFirebase
import com.example.ifood.model.Produto
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class NewProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewProductBinding
    private lateinit var idUserLogged :String
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Novo produto"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idUserLogged = UserFirebase.getIdUser()

        storageReference = FirebaseConfig.getInstanceStorage()


        binding.abuttonSalvarNovoProduto.setOnClickListener {
            validarDadosProdutos()
        }
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK){
//            var imagem : Bitmap? = null
//            try {
//                when(requestCode) {
//                    SELECAO_GALERIA -> {
//                        var localImagem = data?.data
//                        imagem = MediaStore.Images.Media.getBitmap(
//                            contentResolver, localImagem
//                        )
//                    }
//                }
//                if (imagem!= null){
//                    binding.imageProduto.setImageBitmap(imagem)
//                    val baos = ByteArrayOutputStream()
//                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                    val dadosImagem = baos.toByteArray()
//                    val imagemRef : StorageReference = storageReference
//                        .child("imagem")
//                        .child("produto")
//                        .child("$idUserLogged.jpeg")
//
//                    val uploadTask = imagemRef.putBytes(dadosImagem)
//                    uploadTask.addOnFailureListener{ task ->
//                        Toast.makeText(this, "Erro ao fazer o upload", Toast.LENGTH_SHORT).show()
//                    }
//                    uploadTask.addOnSuccessListener {
//                        imagemRef.downloadUrl.addOnCompleteListener { task ->
//                            urlIniciada = task.result.toString()
//                        }
//                    }
//                }
//            } catch (e: Exception){
//                Log.d(TAG, "onActivityResult:${e.message}")
//            }
//        }
//    }

    private fun validarDadosProdutos() {
        with(binding) {
            val nomeProduto = this.editTextNomeProduto.text.toString()
            val descricaoProduto = this.editTextDescricaoProduto.text.toString()
            val precoProduto = this.editPreco.text.toString()
            if (!nomeProduto.isNullOrEmpty()){
                    if(!descricaoProduto.isNullOrEmpty()){
                        if(!precoProduto.isNullOrEmpty()) {

                        val produto = Produto(
                            idUser = idUserLogged,
                            nome = nomeProduto,
                            descricao = descricaoProduto,
                            preco = precoProduto.toDouble()
                        )
                        produto.salvar()
                        finish()
                        Toast.makeText(this@NewProductActivity, "deu certo ", Toast.LENGTH_SHORT).show()
                }
            }    else {

               binding.textInputNomeProduto.error = "algo deu errado"


                }
                }
        }
    }

}