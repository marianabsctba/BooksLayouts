package br.infnet.marianabs.layouts.ui.user.authentication

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.infnet.marianabs.layouts.R
import br.infnet.marianabs.layouts.ui.user.utils.Validation
import br.infnet.marianabs.layouts.ui.viewmodel.ViewModelPattern
import br.infnet.marianabs.layouts.model.Users
import com.google.firebase.auth.FirebaseAuth

class SignUp : Fragment() {
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var tvToLogin: TextView
    private lateinit var btnSignup: Button

    private val ref1 = FirebaseAuth.getInstance()
    private val vm by lazy {
        ViewModelProvider(this)[ViewModelPattern::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sign_up, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignup = view.findViewById(R.id.btn_signup)
        username = view.findViewById(R.id.et_username2)
        email = view.findViewById(R.id.et_email2)
        password = view.findViewById(R.id.et_password2)
        tvToLogin = view.findViewById(R.id.tv_to_log_in)


        this.tvToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment2_to_loginFragment2)
        }

        this.btnSignup.setOnClickListener {


            when {
                TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        getString(R.string.please_enter_email),
                        Toast.LENGTH_LONG
                    ).show()
                }

                TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        getString(R.string.please_enter_password),
                        Toast.LENGTH_LONG
                    ).show()


                }
                ! Validation.email(email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        getString(R.string.invalid_email),
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    ref1.createUserWithEmailAndPassword(
                        email.text.toString().trim().toLowerCase(),
                        password.text.toString().trim()
                    ).addOnCompleteListener { register ->

                        if (register.isSuccessful) {


                            sendUserData(
                                username.text.toString(),
                                email.text.toString()
                            )

                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.try_again),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun sendUserData(username: String, emaile: String) {

        val user = Users(username, emaile)
        saveUserFireStore(user)

    }

    private fun saveUserFireStore(user: Users) {// = CoroutineScope(Dispatchers.IO).launch {
        vm.saveDataBooker(user)
        findNavController().navigate(R.id.action_signUpFragment2_to_bookFragment)
    }
}
