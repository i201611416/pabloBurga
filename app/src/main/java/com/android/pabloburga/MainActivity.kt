package com.android.pabloburga

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.pabloburga.databinding.ActivityMainBinding
import com.android.pabloburga.fragment.DeletedFragment
import com.android.pabloburga.fragment.LoginFragment
import com.android.pabloburga.fragment.RecoverFragment
import com.android.pabloburga.fragment.VerificationFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationFragment(LoginFragment())
        binding.setOnItemSelectedListener()
        binding.userSession()
    }

    private fun ActivityMainBinding.userSession() = with(this.navigationBottom.menu){
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            findItem(R.id.deleted_account).isEnabled = firebaseAuth.currentUser != null
            findItem(R.id.verification_account).isEnabled = firebaseAuth.currentUser != null
            if(firebaseAuth.currentUser == null){
                navigationBottom.selectedItemId = R.id.login_account
                navigationFragment(LoginFragment())
                toast("Inicia Sesión para activar la opción de eliminar y verificar")
            }
        }
    }

    private fun navigationFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun ActivityMainBinding.setOnItemSelectedListener() {
        navigationBottom.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.login_account -> LoginFragment()
                R.id.deleted_account -> DeletedFragment()
                R.id.verification_account -> VerificationFragment()
                R.id.recover_account -> RecoverFragment()
                else -> null
            }

            fragment?.let { ita ->
                navigationFragment(ita)
                true
            } ?: run { false }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseAuth.signOut()
    }


}