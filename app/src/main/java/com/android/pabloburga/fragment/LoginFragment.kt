package com.android.pabloburga.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.pabloburga.databinding.FragmentLoginBinding
import com.android.pabloburga.toast
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser()
        binding.btnLogin.setOnClickListener {
            validationFields {
                loginUser()
                verificationUser()
            }
        }

    }

    private fun validationFields(onFields: () -> Unit) = with(binding) {
        val email = edtEmail.text.toString().trim()
        val password = edtPwd.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()) onFields()
        else requireContext().toast("Campos no pueden estar vacios")
    }

    private fun verificationUser() = with(binding) {
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let {
                lnlContainerLogin.visibility = View.GONE
                lnlLogin.visibility = View.VISIBLE
                txtEmail.text = it.email
            } ?: run {
                lnlLogin.visibility = View.GONE
                lnlContainerLogin.visibility = View.VISIBLE
            }
        }


    }

    private fun currentUser() = with(binding) {
        firebaseAuth.currentUser?.let {
            lnlContainerLogin.visibility = View.GONE
            lnlLogin.visibility = View.VISIBLE
            txtEmail.text = it.email
        } ?: run {
            lnlLogin.visibility = View.GONE
            lnlContainerLogin.visibility = View.VISIBLE
        }
    }

    private fun loginUser() = with(binding) {
        try {
            firebaseAuth.signInWithEmailAndPassword(
                edtEmail.text.toString(), edtPwd.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    requireContext().toast("Login exitoso bienvenido")
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(
                        edtEmail.text.toString(), edtPwd.text.toString()
                    ).addOnCompleteListener { tasks ->
                        val message =
                            if (tasks.isSuccessful) "Usuario no encontrado, registro exitoso"
                            else "Correo y/o clave incorrecta"
                        requireContext().toast(message)
                    }
                }
            }
        } catch (e: ApiException) {
            Log.w("com.synapse.profinder", "signInResult:failed code=" + e.statusCode)
        }
    }

}