package com.example.ifood.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.ifood.R
import com.example.ifood.databinding.ActivitySettingUserBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.helper.UserFirebase
import com.example.ifood.model.EnderecoUsuario
import com.example.ifood.model.User
import com.google.firebase.database.DatabaseReference

class SettingUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingUserBinding
    private lateinit var idUserLogged : String
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        setInstanceFirebase()

        binding.buttonSalvarSettingUser.setOnClickListener {
            validarCampos()
        }
    }

    private fun validarCampos() {
       val nome = binding.editTextNomeSettingsUser.text
        val telefone = binding.editTextTelefoneSettingUser.text
        val endereco = binding.editTextLogradouroSettingUser.text
        if (!nome.isNullOrEmpty()){
            if (!telefone.isNullOrEmpty()){
                    val user = User(
                        idUser = idUserLogged,
                        nome = nome.toString(),
                        endereco = endereco.toString(),
                        telefone = telefone.toString()
                    )
                Toast.makeText(this, "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show()
                    user.salvar()
                    finish()

            }
        }
    }

//    private fun validarEndereco(): EnderecoUsuario {
//
//        val logradouro = binding.editTextLogradouroSettingUser
//        val numero = binding.editTextNumeroSettingUser
//        val cep = binding.editTextCepSettingUser
//        val bairro = binding.editTextBairroSettingsUser
//        val cidade = binding.editTextCidadeSettingUser
//        val estado = binding.editTextEstadoSettingUser
//
//        if (validarCamposVazios(logradouro, numero, cep, bairro, cidade, estado)){
//            val endereco = EnderecoUsuario(
//                logradouro = logradouro.text.toString(),
//                numero = numero.text.toString(),
//                cep = cep.text.toString(),
//                bairro = bairro.text.toString(),
//                cidade = cidade.text.toString()
//            )
//            return endereco
//
//        }
//        val teste = EnderecoUsuario()
//        return teste
//
//    }
//
//    fun validarCamposVazios(vararg campos : EditText) : Boolean {
//        for (campo in campos){
//            if (campo.text.toString().isEmpty()) {
//                campo.error = "o campo é obrigatório"
//                return false
//        }
//
//        return true
//    }
//

    private fun setInstanceFirebase() {
        firebaseRef = FirebaseConfig.getInstanceDataBase()
        idUserLogged = UserFirebase.getIdUser()
    }

    private fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}