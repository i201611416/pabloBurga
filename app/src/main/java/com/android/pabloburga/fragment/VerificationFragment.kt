package com.android.pabloburga.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.pabloburga.databinding.FragmentVerificationBinding
import com.android.pabloburga.toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VerificationFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerificationBinding.inflate(layoutInflater)
        return _binding?.root
    }

    private fun initVerification() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            val message =
                if (task.isSuccessful) "El mensaje de verificacion ya fue enviado a su correo"
                else "El mensaje ya fue enviado a su correo"
            requireContext().toast(message)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVerification()
        binding.btnVerification.setOnClickListener {
            val user = firebaseAuth.currentUser
            val message =
                if (user?.isEmailVerified == true) "El correo electrónico del usuario ya ha sido verificado"
                else "El correo electrónico del usuario aún no ha sido verificado"
            requireContext().toast(message)
        }
    }

}