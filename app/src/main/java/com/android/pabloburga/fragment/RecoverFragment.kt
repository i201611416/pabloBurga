package com.android.pabloburga.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.pabloburga.databinding.FragmentRecoverBinding
import com.android.pabloburga.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecoverFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private var _binding: FragmentRecoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecoverBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnRecover.setOnClickListener {
                validationFields() {
                    firebaseAuth.sendPasswordResetEmail(edtEmail.text.toString())
                        .addOnCompleteListener { task ->
                            val message =
                                if (task.isSuccessful) "Correo electrónico de recuperación enviado"
                                else "Error al enviar el correo electrónico de recuperación"
                            requireContext().toast(message)
                        }
                }
            }
        }
    }

    private fun validationFields(onFields: () -> Unit) = with(binding) {
        val email = edtEmail.text.toString().trim()
        if (email.isNotEmpty()) onFields()
        else requireContext().toast("Email no puede estar vacío")
    }

}