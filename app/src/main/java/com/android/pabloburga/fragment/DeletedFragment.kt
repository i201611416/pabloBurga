package com.android.pabloburga.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.pabloburga.databinding.FragmentDeletedBinding
import com.android.pabloburga.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeletedFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var _binding: FragmentDeletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeletedBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteUser()
    }

    private fun deleteUser() = with(binding) {
        btnDelete.setOnClickListener {
            val user = firebaseAuth.currentUser
            user?.delete()
                ?.addOnCompleteListener { task ->
                    val message = if (task.isSuccessful) {
                        firebaseAuth.signOut()
                        "Usuario eliminado correctamente"
                    } else "No se pudo eliminar al usuario"
                    requireContext().toast(message)
                }
        }
    }

}