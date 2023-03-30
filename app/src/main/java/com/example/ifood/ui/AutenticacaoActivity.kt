package com.example.ifood.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ifood.R
import com.example.ifood.databinding.ActivityAutenticacaoBinding
import com.example.ifood.helper.FirebaseConfig
import com.example.ifood.helper.UserFirebase
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.delay

class AutenticacaoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAutenticacaoBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var tipoAcesso : SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splas = installSplashScreen()
        binding = ActivityAutenticacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        tipoAcesso = binding.switchMaterial
            tipoAcesso.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.linearOpcaoUsuario.visibility = View.VISIBLE
            } else {
                binding.linearOpcaoUsuario.visibility = View.GONE
            }
        }
    }


    fun setListener(){
        binding.appCompatButton.setOnClickListener {
            validacaoCampos()
        }
    }
    private fun validacaoCampos() {
        val email = binding.editTextEmail.text.toString()
        val senha = binding.editTextSenha.text.toString()

        if (!email.isNullOrEmpty()){
            if (!senha.isNullOrEmpty()) {
                if (binding.switchMaterial.isChecked){
                    cadastroUsuario(email, senha)
                } else {
                    loginUsuario(email, senha)
                }
            } else {
                binding.textInputEmail.error = "Preencha o campo senha!"

            }
        } else {
            binding.textInputSenha.error = "Preencha o campo email!"
        }
    }

    private fun loginUsuario(email: String, senha: String) {
        auth = FirebaseConfig.getInstanceAuth()
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener {
            if (it.isSuccessful) {
                val tipoUser = it.getResult().user!!.displayName.toString()
                 callActivity(tipoUser)
            } else {
                try {
                    it.exception
                } catch (e : Exception){
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cadastroUsuario(email: String, senha: String) {
        auth = FirebaseConfig.getInstanceAuth()
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener {
            if (it.isSuccessful){
                try {
                    val tipoUsuario = getTipoUsuario()
                    UserFirebase.atualizaTipoUser(tipoUsuario)
                    callActivity(tipoUsuario)
                } catch (exception : Exception){
                    Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                var msgError = ""
                try {
                    throw it.exception!!
                } catch (e : FirebaseAuthWeakPasswordException) {
                    msgError = "Digite uma senha mais forte"
                } catch (e : FirebaseAuthInvalidCredentialsException) {
                    msgError = "Digite um e-mail válido"
                } catch ( e: FirebaseAuthUserCollisionException) {
                    msgError = "Essa conta já está sendo utilizada"
                } catch (e : java.lang.Exception) {
                    msgError = "ao cadastrar usuário: ${e.message}"
                    e.printStackTrace()
                }
                Toast.makeText(this, msgError, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificaUserLogged(){
        val userAtual = auth.currentUser
        if (userAtual!= null){
            val tipoUser = userAtual.displayName.toString()
            callActivity(tipoUser)
        }
    }

    private fun getTipoUsuario() : String {
        return if (tipoAcesso.isChecked){
            "E"
        } else {
            "U"
        }
    }

    private fun callActivity(tipoUser : String) {
        if (tipoUser.equals("E")){
            startActivity(Intent(this, HomeEmpresasActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

}